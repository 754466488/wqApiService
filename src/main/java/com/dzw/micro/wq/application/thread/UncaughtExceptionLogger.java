package com.dzw.micro.wq.application.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 1、该异常Handler只作用于直接使用 ThreadPoolExecutor.execute方法提交的任务
 * 2、ThreadPoolExecutor.submit 提交的任务运行抛出的异常需要通过返回值 Future获取
 * 3、而使用spring注解@async的任务会自动被spring体系下的AsyncUncaughtExceptionHandler捕获并打印
 * @author lyb
 * @date 2022/12/7
 */
@Slf4j
public class UncaughtExceptionLogger implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		log.error("thread[" + t.getName() + "] occurred uncaughtException", e);
	}
}
