#!/bin/bash
#当前环境dev|test|prod
CUR_ENV=prod
#当前git分支,测试环境对应qapri/test
GIT_BRANCH=develop
#应用的仓库名称
GIT_PROJECT_NAME=wqApiService
#应用名称
APP_NAME=wqApiService

#执行脚本传入的参数
SCRIPT=$0
#获取操作符start|stop|status|restart
OPERATOR=$1

PROJECT_DIR=/data/project
GIT_DIR=${PROJECT_DIR}/git/${GIT_PROJECT_NAME}
RUN_DIR=${PROJECT_DIR}/run/${APP_NAME}
ORIGIN_JAR_FILE=${GIT_DIR}/target/${APP_NAME}.jar
#此脚本和运行的jar都位于RUN_DIR下
RUN_JAR_FILE=${RUN_DIR}/${APP_NAME}.jar
LOG_DIR=/data/logs/${APP_NAME}

#jvm参数-堆
JAVA_OPTS="-server -Xms2g -Xmx2g -XX:NewRatio=2 -XX:SurvivorRatio=6 -Xss256k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
#jvm参数-GC
JAVA_OPTS=" ${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly"
#jvm参数-优化
JAVA_OPTS=" ${JAVA_OPTS} -XX:+AlwaysPreTouch -Djava.awt.headless=true -XX:-OmitStackTraceInFastThrow"
#jvm参数-内存溢出
JAVA_OPTS=" ${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG_DIR}"
#jvm参数-GC LOG
JAVA_OPTS=" ${JAVA_OPTS} -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime -Xloggc:${LOG_DIR}/gc.log"

usage() {
    echo "Usage: .   $SCRIPT [start|stop|restart|status]"
    exit 1
}

#判断是否输入了参数
if [ $# != 1 ]; then
    usage
fi

is_exist(){
  #过滤grep命令本身
  #pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
  #使用sh xx.sh命令执行的话，启用下面代码
  pid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v $SCRIPT|awk '{print $2}' `
  if [ -z "${pid}" ]; then
    return 1
  else
    return 0
  fi
}

start(){
    echo "***********************************************************************************************"
    echo "****************`date`"
    echo "****************${APP_NAME} 开始启动...."
    echo "***********************************************************************************************"
    echo ""
    echo ""

    JAVA_VERSION=`java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`
    echo "*************************检查JDK版本,当前版本 ${JAVA_VERSION}*********************`date`"
    JAVA_BIG_VERSION=${JAVA_VERSION:2:1}
    if [ ${JAVA_BIG_VERSION} -lt 8 ]; then
        echo "需要JDK8及以上的版本才能运行！"
        return
    fi

    echo "*************************检查进程是否存在*********************`date`"
    is_exist
    if [ $? -eq "0" ]; then
        echo "${APP_NAME} is already running. pid=${pid} ."
        return
    fi

    cd ${GIT_DIR}
    echo "*************************开始拉取代码,当前分支 ${GIT_BRANCH}*********************`date`"
    git checkout ${GIT_BRANCH}
    git pull

    git_commit_id=`git rev-parse ${GIT_BRANCH}`
    echo "*************************最新commitId ${git_commit_id}*********************`date`"
    if [ -f "${RUN_JAR_FILE}.${git_commit_id}" ]; then
        echo "*************************此版本的jar文件已存在*********************`date`"
    else
        echo "*************************开始打包*********************`date`"
        mvn clean install -U -Dmaven.test.skip=true
        cp -p ${ORIGIN_JAR_FILE} ${RUN_JAR_FILE}.${git_commit_id}
        cp -f -p ${ORIGIN_JAR_FILE} ${RUN_JAR_FILE}
    fi

    echo "*************************开始启动进程*********************`date`"
    nohup java ${JAVA_OPTS} -Dspring.application.name=${APP_NAME} -Dspring.profiles.active=${CUR_ENV} -jar ${RUN_JAR_FILE} > /dev/null 2>&1 &

    sleep 3s
    echo ""
    echo ""
    echo "***********************************************************************************************"
    echo "****************`date`"
    echo "****************${APP_NAME} 启动完毕."
    echo "****************请检查启动日志：${LOG_DIR}/bus.log"
    echo "***********************************************************************************************"
}

stop(){
  is_exist
  if [ $? -eq "0" ]; then
    kill -9 $pid
  else
    echo "${APP_NAME} is not running"
  fi
}

status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is running. Pid is ${pid}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}

restart(){
  stop
  start
}

case "$OPERATOR" in
  "start")
    start ;;
  "stop")
    stop ;;
  "status")
    status ;;
  "restart")
    restart ;;
  *)
    usage ;;
esac