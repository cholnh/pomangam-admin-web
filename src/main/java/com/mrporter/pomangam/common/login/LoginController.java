package com.mrporter.pomangam.common.login;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.common.security.model.UserService;
import com.mrporter.pomangam.common.security.model.domain.User;
import com.mrporter.pomangam.common.util.Ip;
import com.mrporter.pomangam.member.dao.AdminCrudDAO;
import com.mrporter.pomangam.member.vo.AdminBean;


@Controller
public class LoginController {
	@Autowired
    UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private static final int KEY_SIZE = 1024;
	   
	@RequestMapping(value = "/login.do")
	public ModelAndView openIndexPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) 
			throws Exception {
		logger.info("login index - " + error);

		ModelAndView model = new ModelAndView();
		
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		if(obj == null) {
			Cookie cookie = WebUtils.getCookie(request, "loginCookie");
			if(cookie != null) {
				String session_key = cookie.getValue();
				//System.out.println("session_key : " + session_key);
				AdminBean bean = new AdminCrudDAO().getMemberWithSession(session_key);
				if(bean != null) {
					User user = userService.loadUserByUsername(bean.getUsername());
					user.setPassword("");
		            session.setAttribute("user", new Gson().toJson(user));
				}
			}
		} else {
			response.sendRedirect("./"); 
		}
		
		/*
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!authentication.getName().equals("anonymousUser")) {
			response.sendRedirect("./"); 
		}
		*/
		
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}
		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		
		setRSA(request, KEY_SIZE);
		
		model.setViewName("login");
		return model;
	}

	
	@RequestMapping(value = "/logout.do")
	public void logout(
			@RequestParam(value = "data", required = false) String data,
			final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		logger.info("logout");
		
		HttpSession session= request.getSession(false);
		session.removeAttribute("user");
		SecurityContextHolder.clearContext();
		session= request.getSession(false);
		
		if(session != null) {
			String username = "";
			Object obj = session.getAttribute("user");
			if(obj != null) {
				User user = new Gson().fromJson(obj+"", new TypeToken<User>() {}.getType());
				username = user.getUsername();
			}
			session.removeAttribute("user");
			session.invalidate();
			
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
            if ( loginCookie != null ){
                loginCookie.setPath("/");
                loginCookie.setMaxAge(0);
                response.addCookie(loginCookie);
                 
                Date session_limit = new Date(System.currentTimeMillis());
                new AdminCrudDAO().rememberSession(session.getId(), session_limit, username);
            }
		}
		
		for(Cookie cookie : request.getCookies()) {
			cookie.setMaxAge(0);
		}
		
		response.sendRedirect("./login.do?logout=true"); 
	}

	@RequestMapping(value = "/denied")
	public ModelAndView denied(
			@RequestParam(value = "data", required = false) String data,
			final HttpServletResponse response) throws Exception {
		logger.info("denied - " + Ip.getInfo());

		ModelAndView model = new ModelAndView();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getName().equals("anonymousUser")) {
			response.sendRedirect("./login.do"); 
		}
		model.setViewName("denied");
		return model;
	}
	
	@ExceptionHandler
	public @ResponseBody Status handle(Exception e, HttpServletResponse response) {
		if(e.getClass().getSimpleName().equals("AccessDeniedException")) {
			try {
				response.sendRedirect("./denied"); 
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		logger.info("Exception Handler - " + e.getMessage());
		return new Status(400, "Exception handled!");
	}
	
	private void setRSA(HttpServletRequest request, final int KEY_SIZE) throws Exception {
    	request.setCharacterEncoding("UTF-8");

    	HttpSession session = request.getSession();
    	
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(KEY_SIZE);

        KeyPair keyPair = generator.genKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        session.setAttribute("__rsaPrivateKey__", privateKey);

        RSAPublicKeySpec publicSpec = 
        		(RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

        String publicKeyModulus = publicSpec.getModulus().toString(16);
        String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

        request.setAttribute("publicKeyModulus", publicKeyModulus);
        request.setAttribute("publicKeyExponent", publicKeyExponent);
	}
}
