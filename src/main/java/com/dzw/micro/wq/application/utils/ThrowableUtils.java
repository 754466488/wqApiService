package com.dzw.micro.wq.application.utils;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author lyb
 * @date 2021/7/19
 */
public class ThrowableUtils {
	/**
	 * 每一段Cause by的前5行
	 */
	public static final int BASE_LINE = 5;
	/**
	 * 需要保留的包名前缀
	 */
	public static final List<String> RETAIN_LINE_PREFIX_LIST = Lists.newArrayList("com.edaijia", "com.zhouyutong", "com.ejiyun");

	private ThrowableUtils() {
	}

	/**
	 * 简化版的getStackTraceAsString
	 *
	 * @param throwable
	 * @return
	 * @see Throwables
	 */
	public static String getStackTraceAsSimpleString(Throwable throwable) {
		return getStackTraceAsSimpleString(throwable, null);
	}

	/**
	 * 简化版的getStackTraceAsString
	 *
	 * @param throwable
	 * @param retainLinePrefixList - 需要保留的行前缀列表
	 * @return
	 * @see Throwables
	 */
	public static String getStackTraceAsSimpleString(Throwable throwable, List<String> retainLinePrefixList) {
		String[] retainLinePrefixArray = null;
		if (retainLinePrefixList != null && retainLinePrefixList.size() > 0) {
			retainLinePrefixArray = retainLinePrefixList.toArray(new String[0]);
		} else {
			retainLinePrefixArray = RETAIN_LINE_PREFIX_LIST.toArray(new String[0]);
		}

		StringBuilder sb = new StringBuilder();
		String sts = Throwables.getStackTraceAsString(throwable);
		String[] lineArray = org.apache.commons.lang3.StringUtils.split(sts, "\n");

		int i = 0;
		for (String line : lineArray) {
			if (i++ < BASE_LINE) {
				sb.append(line).append("\n");
				continue;
			}
			if (org.apache.commons.lang3.StringUtils.startsWith(line, "Caused by")) {
				sb.append(line).append("\n");
				i = 0;
				continue;
			}
			if (org.apache.commons.lang3.StringUtils.containsAny(line, retainLinePrefixArray)) {
				sb.append(line).append("\n");
			}
		}
		return sb.toString();
	}
}
