package com.dzw.micro.wq.application.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description 日期工具类 存储为long类型
 * @Author lyb
 * @Date Created in 2023/1/31
 */
public class LongDateUtils {
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMAT_YYYY_MM_DD_V2 = "yyyy年MM月dd日HH时mm分";
	public static final String[] SUPPORT_FORMAT_ARRAY = new String[]{
			FORMAT_YYYY_MM_DD,
			FORMAT_YYYY_MM_DD_HH,
			FORMAT_YYYY_MM_DD_HH_MM,
			FORMAT_YYYY_MM_DD_HH_MM_SS,
			FORMAT_YYYY_MM_DD_HH_MM_SS_SSS,
			FORMAT_YYYY_MM_DD_V2
	};
	// 北京时间时区
	private static final TimeZone TIMEZONE = TimeZone.getTimeZone("GMT+8");

	private LongDateUtils() {
	}

	/**
	 * 当前时间格式化为 20160415112036 格式
	 *
	 * @return long
	 */
	public static long formatNow2Long() {
		return format2Long(new Date());
	}

	/**
	 * 格式化Date日期为 20160415112036 格式
	 *
	 * @param date 日期
	 * @return long
	 */
	public static long format2Long(Date date) {
		return format2Long(date, false);
	}

	/**
	 * 格式化Date日期为 20160415 格式
	 *
	 * @param date 日期
	 * @return int
	 */
	public static int format2Int(Date date) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.setTime(date);
		String year = String.format("%04d", calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		return Integer.parseInt(year + month + day);
	}

	/**
	 * 格式化Date日期为 20160415112036 格式 或 20160415112036007 格式
	 *
	 * @param date           日期
	 * @param hasMillisecond 是否包含毫秒
	 * @return long
	 */
	public static long format2Long(Date date, boolean hasMillisecond) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.setTime(date);
		String year = String.format("%04d", calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
		String second = String.format("%02d", calendar.get(Calendar.SECOND));
		String str = year + month + day + hour + minute + second;
		if (hasMillisecond) {
			String millisecond = String.format("%03d", calendar.get(Calendar.MILLISECOND));
			str = str.concat(millisecond);
		}
		return Long.parseLong(str);
	}

	/**
	 * 当前时间加上指定秒数后格式化为 20160415112036 格式
	 *
	 * @param second 秒
	 * @return long
	 */
	public static long nowAddSecond2Long(int second) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.add(Calendar.SECOND, second);
		return format2Long(calendar.getTime());
	}


	/**
	 * 当前时间加上指定分钟数后格式化为 20160415112036 格式
	 *
	 * @return long
	 * @minute 分钟
	 */
	public static long nowAddMinute2Long(int minute) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.add(Calendar.MINUTE, minute);
		return format2Long(calendar.getTime());
	}

	/**
	 * 当前时间加上指定分钟数后格式化为 20160415112036 格式
	 *
	 * @param hour 小时
	 * @return long
	 */
	public static long nowAddHour2Long(int hour) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return format2Long(calendar.getTime());
	}

	/**
	 * 数字型可视化时间转换为Date
	 *
	 * @param format 数字时间
	 * @return date
	 */
	public static Date parseDate(long format) {
		String str = String.valueOf(format);
		if (str.length() >= 8) {
			Calendar calendar = Calendar.getInstance(TIMEZONE);
			calendar.clear();
			calendar.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));
			calendar.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6)) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(6, 8)));
			if (str.length() >= 14) {
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(8, 10)));
				calendar.set(Calendar.MINUTE, Integer.parseInt(str.substring(10, 12)));
				calendar.set(Calendar.SECOND, Integer.parseInt(str.substring(12, 14)));
			}
			if (str.length() == 17) {
				calendar.set(Calendar.MILLISECOND, Integer.parseInt(str.substring(14, 17)));
			}
			return calendar.getTime();
		}
		return null;
	}

	/**
	 * 数字型可视化时间转换为String  2022-01-01 00:00:00
	 *
	 * @param dateTime long类型时间  20160415112036
	 * @return String
	 */
	public static String format(long dateTime) {
		if (dateTime <= 0) {
			return "";
		}
		try {
			return format(dateTime, DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 数字型可视化时间转换为String  2022-01-01 00:00:00
	 *
	 * @param dateTime long类型时间  20160415112036
	 * @param format   格式化
	 * @return String
	 */
	public static String format(long dateTime, String format) {
		if (dateTime <= 0) {
			return "";
		}
		try {
			Date date = parseDate(dateTime);
			Preconditions.checkArgument(date != null, "Param date must be not null");
			Preconditions.checkArgument(StringUtils.isNotBlank(format), "Param format must be not null or empty");
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将字符串格式时间转为数字型时间   2022-01-01 23:01:40->20220101230140
	 *
	 * @param time   String时间 2022-01-01 23:01:40
	 * @param format 格式化
	 */
	public static long parseTime(String time, String format) {
		Preconditions.checkNotNull(time);
		if (!org.apache.commons.lang3.StringUtils.equalsAny(format, SUPPORT_FORMAT_ARRAY)) {
			throw new IllegalArgumentException("不支持的日期格式:" + format);
		}
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException("无法转换的日期:" + time);
		}
		return format2Long(date);
	}

	/**
	 * 两个时间间隔时间 单位秒
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 */
	public static long durationSeconds(long startDate, long endDate) {
		DateTime startTimeD = new DateTime(DateUtils.parse(startDate));
		DateTime endTimeD = new DateTime(DateUtils.parse(endDate));
		Duration duration;
		if (startTimeD.isBefore(endTimeD)) {
			duration = new Duration(startTimeD, endTimeD);
		} else {
			duration = new Duration(endTimeD, startTimeD);
		}

		return duration.getStandardSeconds();
	}

	/**
	 * 两个时间间隔时间 单位分钟
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 */
	public static long durationMinutes(long startDate, long endDate) {
		DateTime startTimeD = new DateTime(DateUtils.parse(startDate));
		DateTime endTimeD = new DateTime(DateUtils.parse(endDate));
		Duration duration;
		if (startTimeD.isBefore(endTimeD)) {
			duration = new Duration(startTimeD, endTimeD);
		} else {
			duration = new Duration(endTimeD, startTimeD);
		}

		return duration.getStandardMinutes();
	}

	/**
	 * 两个时间间隔时间 单位小时
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 */
	public static long durationHour(long startDate, long endDate) {
		DateTime startTimeD = new DateTime(DateUtils.parse(startDate));
		DateTime endTimeD = new DateTime(DateUtils.parse(endDate));
		Duration duration;
		if (startTimeD.isBefore(endTimeD)) {
			duration = new Duration(startTimeD, endTimeD);
		} else {
			duration = new Duration(endTimeD, startTimeD);
		}

		return duration.getStandardHours();
	}

	/**
	 * 两个时间间隔时间 单位天
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 */
	public static long durationDay(long startDate, long endDate) {
		DateTime startTimeD = new DateTime(DateUtils.parse(startDate));
		DateTime endTimeD = new DateTime(DateUtils.parse(endDate));
		Duration duration;
		if (startTimeD.isBefore(endTimeD)) {
			duration = new Duration(startTimeD, endTimeD);
		} else {
			duration = new Duration(endTimeD, startTimeD);
		}

		return duration.getStandardDays();
	}

	/**
	 * 当前时间新增N秒
	 *
	 * @param dateTime 时间
	 * @param amount
	 */
	public static long addSeconds(long dateTime, int amount) {
		final Date date = org.apache.commons.lang3.time.DateUtils.addSeconds(DateUtils.parse(dateTime), amount);
		return DateUtils.format2Long(date);
	}

	/**
	 * 当前时间新增N分钟
	 *
	 * @param dateTime 时间
	 * @param amount
	 */
	public static long addMinutes(long dateTime, int amount) {
		final Date date = org.apache.commons.lang3.time.DateUtils.addMinutes(DateUtils.parse(dateTime), amount);
		return DateUtils.format2Long(date);
	}

	/**
	 * 当前时间新增N小时
	 *
	 * @param dateTime 时间
	 * @param amount
	 */
	public static long addHours(long dateTime, int amount) {
		final Date date = org.apache.commons.lang3.time.DateUtils.addHours(DateUtils.parse(dateTime), amount);
		return DateUtils.format2Long(date);
	}

	/**
	 * 当前时间新增N天
	 *
	 * @param dateTime 时间
	 * @param amount
	 */
	public static long addDays(long dateTime, int amount) {
		final Date date = org.apache.commons.lang3.time.DateUtils.addDays(DateUtils.parse(dateTime), amount);
		return DateUtils.format2Long(date);
	}

}
