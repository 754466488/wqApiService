package com.dzw.micro.wq.application.log;

import com.dzw.micro.wq.application.utils.SpringContextUtil;
import com.dzw.micro.wq.service.base.BusinessAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lyb
 * @date 2021/1/13
 */
@Slf4j
public class Log {
	private static BusinessAlarmService businessAlarmService = SpringContextUtil.getBean(BusinessAlarmService.class);
	private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

	public static final void warn(String s) {
		errorLogger.warn(s);
	}

	public static final void warn(String s, Object var) {
		errorLogger.warn(s, var);
	}

	public static final void warn(String s, Object... vars) {
		errorLogger.warn(s, vars);
	}

	public static final void error(String s, Throwable throwable) {
		errorLogger.error(s, throwable);
		//businessAlarmService.sendAlarm(s, throwable);
	}

	public static final void alarm(String s) {
		log.info(s);
		//businessAlarmService.sendAlarm(s);
	}

	public static final void error(String s) {
		errorLogger.error(s);
		//businessAlarmService.sendAlarm(s);
	}

	public static final void info(String s) {
		log.info(s);
	}

	public static final void info(String s, Object var) {
		log.info(s, var);
	}

	public static final void info(String s, Object... vars) {
		log.info(s, vars);
	}
}
