package com.mrporter.pomangam.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date
 * 
 * @version 1.0 [2018. 4. 19.]
 * @author Choi
 */
public class Date {

	public static String getCurDate() {
		return getCurDate(0);
	}
	public static String getCurDate(long n) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now.plusDays(n));
	}
	
	public static String getCurTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
	
	public static String getCurTime2() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
	
	public static String getCurDay() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
}
