package com.mrporter.pomangam.common.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * SQLInjectionDefender
 * 
 * @version 1.0 [2018. 2. 10.]
 * @author Choi
 */
final public class SQLInjectionDefender {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLInjectionDefender.class);
	
	/* 특수문자 공백 처리 */
	static final Pattern SpecialChars = Pattern.compile("['\"\\-#()@;=*/+<>]");
	/* sql예약어 처리 */
	static final String regex = "(union|select|from|where|substring|"
			+ "information_schema|sysobjects|table_schema|declare|update|join|drop|insert|delete)";
	static final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	 
	public static boolean isSQLInjection(String userInput) {
		if(userInput == null || userInput.length() == 0) return false;
		
		Matcher matcher = pattern.matcher(userInput);
		userInput = SpecialChars.matcher(userInput).replaceAll("");
		
		if(matcher.find()){
			log("[SQL INJECTION] " + userInput);
			return true;
		} 
		else
			return false;
	}

	public static boolean isSQLInjection(String...userInputs) {
		for(String arg : userInputs) {
			if(isSQLInjection(arg))
				return true;
		}
		return false;
	}
	
	static void log(String text) {
		long time = System.currentTimeMillis(); 
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("X-FORWARDED-FOR");
		int port = 0;
		if(ip == null) {
			ip = request.getRemoteAddr();
			port = request.getRemotePort();
		}
		System.out.println(request.getRemoteAddr() + " " + request.getRemoteHost() + " " + request.getRemotePort() + " " + request.getRemoteUser());
		
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String timestr = dayTime.format(new Date(time));
		
		logger.info(ip + ":" + port + " - " + text + " - " + timestr);
	}
}

