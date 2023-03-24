package com.dzw.micro.wq.application.utils;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Random;

/**
 * 字符串utils
 *
 * @author zhoutao
 */
public class StringUtils {

	private StringUtils() {
	}

	/**
	 * 截取字符串
	 *
	 * @param string
	 * @param start  - 开始位置
	 * @param end    - 结束位置
	 * @return
	 */
	public static String substr(String string, int start, int end) {
		if (string == null) {
			return string;
		}
		if (string.length() <= end) {
			return string;
		} else {
			return string.substring(start, end);
		}
	}

	/**
	 * 截取字符串
	 *
	 * @param obj
	 * @param start - 开始位置
	 * @param end   - 结束位置
	 * @return
	 */
	public static String substr(Object obj, int start, int end) {
		return substr(obj == null ? null : obj.toString(), start, end);
	}

	/**
	 * 生成n位数字随机码
	 *
	 * @return code
	 */
	public static String geneVcode(int n) {
		Random random = new Random();
		StringBuilder nine = new StringBuilder();
		for (int i = 0; i < n; i++) {
			nine.append("9");
		}
		int randomNum = random.nextInt(Integer.parseInt(nine.toString()));
		String format = "%0" + n + "d";
		return String.format(format, randomNum);
	}

	/**
	 * 驼峰法转换为全小写带下划线
	 *
	 * @param camelName
	 * @return
	 */
	public static String underscoreName(String camelName) {
		if (camelName == null) {
			return "";
		} else {
			StringBuilder result = new StringBuilder();
			result.append(lowerCaseName(camelName.substring(0, 1)));

			for (int i = 1; i < camelName.length(); ++i) {
				String s = camelName.substring(i, i + 1);
				String slc = lowerCaseName(s);
				if (!s.equals(slc)) {
					result.append("_").append(slc);
				} else {
					result.append(s);
				}
			}

			return result.toString();
		}
	}

	private static String lowerCaseName(String name) {
		return name.toLowerCase(Locale.US);
	}

	public static boolean isBlank(CharSequence cs) {
		return org.apache.commons.lang3.StringUtils.isBlank(cs);
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean equals(CharSequence cs1, CharSequence cs2) {
		return org.apache.commons.lang3.StringUtils.equals(cs1, cs2);
	}

	public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
		return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(cs1, cs2);
	}

	public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
		return org.apache.commons.lang3.StringUtils.equalsAny(string, searchStrings);
	}

	public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
		return org.apache.commons.lang3.StringUtils.equalsAny(string, searchStrings);
	}

	public static String toStringIgnoreNullOrEmptyOrZero(Object entity) {
		final StringBuilder sb = new StringBuilder();
		sb.append(entity.getClass().getSimpleName());
		sb.append("{");

		Field[] fields = entity.getClass().getDeclaredFields();
		int i = 0;
		for (Field field : fields) {
			String propertyName = field.getName();
			try {
				field.setAccessible(true);
				Object value = field.get(entity);
				if (value == null) {
					continue;
				}
				if (value instanceof String && org.apache.commons.lang3.StringUtils.isBlank((String) value)) {
					continue;
				}
				if (value instanceof Number && ((Number) value).doubleValue() == 0D) {
					continue;
				}
				if (i++ > 0) {
					sb.append(", ");
				}
				sb.append(propertyName).append("=").append(value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
