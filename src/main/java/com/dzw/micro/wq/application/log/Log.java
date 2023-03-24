package com.dzw.micro.wq.application.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log打印统一入口
 *
 * @author zhoutao
 * @date 2021/1/13
 */
@Slf4j
public class Log {
	private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

	public static final boolean isWarnEnabled() {
		return errorLogger.isWarnEnabled();
	}

	public static final void warn(String s) {
		errorLogger.warn(s);
	}

	public static final void warn(String s, Object... vars) {
		errorLogger.warn(s, vars);
	}

	public static final boolean isErrorEnabled() {
		return errorLogger.isErrorEnabled();
	}

	public static final void error(String s, Object... vars) {
		errorLogger.error(s, vars);
	}

	public static final void error(String s, Throwable throwable) {
		errorLogger.error(s, throwable);
	}

	public static final void error(String s, Throwable throwable, Object... vars) {
		String str = s == null ? "" : s;
		if (vars != null && vars.length > 0) {
			for (Object var : vars) {
				str = str.replaceFirst("\\{\\}", var == null ? "" : var.toString());
			}
		}
		errorLogger.error(str, throwable);
	}

	public static final boolean isInfoEnabled() {
		return log.isInfoEnabled();
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

	public static final boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public static final void debug(String s) {
		log.debug(s);
	}

	public static final void debug(String s, Object var) {
		log.debug(s, var);
	}

	public static final void debug(String s, Object... vars) {
		log.debug(s, vars);
	}
}
