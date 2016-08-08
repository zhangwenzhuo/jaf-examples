package com.jaf.examples.java8.dateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Joda Time 的一些基本用法
 * 官方主页：http://www.joda.org/joda-time/
 * 
 * @author liaozhicheng.cn@163.com
 * @date 2016年7月30日
 * @since 1.0
 */
public class JodaTimeTests {
	
	private static final String DEFAULT_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMATTER_YMD = "yyyy-MM-dd";
	
	@Test
	public void baseTest() {
		DateTime now = new DateTime();  // 取的当前时间
		println(now);

		// 年,月,日,时,分,秒,毫秒  方式初始化时间
		DateTime date1 = new DateTime(2016, 7, 30, 20, 20, 20, 333);
		println(date1);

		// 时间解析
		DateTimeFormatter format = DateTimeFormat .forPattern(DEFAULT_DATE_FORMATTER);
		DateTime dateTime2 = DateTime.parse("2012-12-21 23:22:45", format);
		println(dateTime2);

		// 前一天，后一天
		println(DateTime.now().minusDays(1));
		println(DateTime.now().plusDays(1));

		// 1个月后，1年后
		println(DateTime.now().plusMonths(1));
		println(DateTime.now().plusYears(1));

		// 当前时间所在周的第一天和最后一天
		println(DateTime.now().dayOfWeek().withMinimumValue());
		println(DateTime.now().dayOfWeek().withMaximumValue());

		// 当前月的第一天，当前年最后一天
		println(DateTime.now().dayOfMonth().withMinimumValue());
		println(DateTime.now().dayOfYear().withMaximumValue());

		// 当前日期 2016-07-30，获取当前日期在3月份的日期，即 2016-03-30
		println(DateTime.now().monthOfYear().setCopy(3));
		println(DateTime.now().monthOfYear().setCopy(2));  // 2月没有30号，这里得到的是 2016-02-29
		println(DateTime.now().monthOfYear().addToCopy(-4));  // 另一种实现方式

		// 打印当前月的所有日期
		System.out.println("======= days of month ==========");
		List<Long> daysOfMonth = getDaysOfMonth(DateTime.now());
		daysOfMonth.stream().map(input -> {
			return new DateTime(input.longValue());
		}).forEach(this::printlnYMD);

		// 时间计算
		DateTime lastDayOfYear = DateTime.now().dayOfYear().withMaximumValue();
		Duration duration = new Duration(DateTime.now(), lastDayOfYear);
		System.out.println("到年底还有多少天：" + duration.getStandardDays());
		System.out.println("到年底还有多少个小时：" + duration.getStandardHours());

		// joda time 和 jdk 中的 Date 转换
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateTime.now().plusDays(1).toDate());
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMATTER);
		System.out.println(sdf.format(calendar.getTime()));
	}
	
	private void println(DateTime dateTime) {
		System.out.println(dateTime.toString(DEFAULT_DATE_FORMATTER));
	}
	
	private void printlnYMD(DateTime dateTime) {
		System.out.println(dateTime.toString(DATE_FORMATTER_YMD));
	}
	
	/**
	 * 获取当前月所有的日期列表
	 * @param date
	 * @return
	 */
	public static List<Long> getDaysOfMonth(DateTime date) {
		final ImmutableList.Builder<Long> dayList = ImmutableList.builder();
		
		LocalDate firstDayOfMonth = date.toLocalDate().withDayOfMonth(1);
		final LocalDate nextMonthFirstDay = firstDayOfMonth.plusMonths(1);
		while (firstDayOfMonth.isBefore(nextMonthFirstDay)) {
			dayList.add(firstDayOfMonth.toDateTimeAtStartOfDay().getMillis());
			firstDayOfMonth = firstDayOfMonth.plusDays(1);
		}
		return dayList.build();
	}
	
}