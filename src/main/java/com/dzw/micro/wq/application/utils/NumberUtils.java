package com.dzw.micro.wq.application.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数字工具类
 *
 * @author lyb
 */
public class NumberUtils {

	private NumberUtils() {
	}

	/**
	 * double转换为string，避免科学计数法
	 *
	 * @param num
	 * @param retainDecimalDigits - 百分比中保留的小数位数
	 * @param roundingMode
	 * @return
	 */
	public static String convertToString(double num, int retainDecimalDigits, RoundingMode roundingMode) {
		BigDecimal bigDecimal = BigDecimal.valueOf(num).setScale(retainDecimalDigits, roundingMode);
		return bigDecimal.toString();
	}

	/**
	 * 转换成百分比
	 *
	 * @param num
	 * @param retainDecimalDigits - 百分比中保留的小数位数
	 * @param roundingMode
	 * @return
	 */
	public static String convertToPercent(double num, int retainDecimalDigits, RoundingMode roundingMode) {
		BigDecimal bigDecimal = BigDecimal.valueOf(num * 100).setScale(retainDecimalDigits, roundingMode);
		if (retainDecimalDigits <= 0) {
			return bigDecimal.intValue() + "%";
		} else {
			return bigDecimal.doubleValue() + "%";
		}
	}

	/**
	 * 小数除法运算
	 *
	 * @param dividend            - 被除数
	 * @param divisor             - 除数
	 * @param retainDecimalDigits - 保留小数位数
	 * @param roundingMode
	 * @return
	 */
	public static double divide(double dividend, double divisor, int retainDecimalDigits, RoundingMode roundingMode) {
		BigDecimal divisorDec = BigDecimal.valueOf(divisor);
		BigDecimal dividendDec = BigDecimal.valueOf(dividend);
		return dividendDec.divide(divisorDec, retainDecimalDigits, roundingMode).doubleValue();
	}
}
