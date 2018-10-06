package com.mrporter.pomangam.common.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mrporter.pomangam.common.pattern.vo.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UnauthorizedEntryPoint
 * 
 * @version 1.0 [2018. 4. 2.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor @Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	private String loginFormUrl;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		String accept = request.getHeader("accept"); 
		Status responseError = new Status();
		
		if( StringUtils.indexOf(accept, "html") > -1 ) {
			System.out.println("handle");
			
			responseError.setMessage("Unauthorized");
			responseError.setCode(401); 
			request.getSession().setAttribute("status", responseError);
			response.sendRedirect(loginFormUrl); 
			//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token was either missing or invalid."); 
		} else { 
			System.out.println("json handle");
			
			response.setContentType("application/json"); 
			response.setCharacterEncoding("utf-8"); 
			
			if( StringUtils.indexOf(accept, "json") > -1 ) { 
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
				responseError.setMessage("Unauthorized");
				responseError.setCode(401); 
			} else { 
				response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); 
				responseError.setMessage("415 Unsupported Media Type"); 
				responseError.setCode(415); 
			}
			PrintWriter out = response.getWriter(); 
			out.print(new Gson().toJson(responseError)); 
			out.flush(); 
			out.close(); 
		}
	}

}

