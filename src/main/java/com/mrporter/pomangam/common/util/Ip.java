package com.mrporter.pomangam.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Ip
 * 
 * @version 1.0 [2018. 8. 24.]
 * @author Choi
 */
public class Ip {
	public static String getInfo() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("X-FORWARDED-FOR");
		int port = 0;
		if(ip == null) {
			ip = request.getRemoteAddr();
			port = request.getRemotePort();
		}
		return "Ip : " + ip + " Port : " + port;
	}
}
