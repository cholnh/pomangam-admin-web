package com.mrporter.pomangam.common.index;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.common.security.model.UserService;
import com.mrporter.pomangam.common.security.model.domain.User;
import com.mrporter.pomangam.member.dao.AdminCrudDAO;
import com.mrporter.pomangam.member.vo.AdminBean;
import com.mrporter.pomangam.order.dao.PaymentCrudDAO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class IndexController {
	@Autowired
    UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	//@Secured({"ROLE_USER"})
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String openDefaultPage(HttpServletRequest request, 
									HttpServletResponse response) {
		//logger.info("Welcome home!");
		
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		if(obj == null) {
			Cookie cookie = WebUtils.getCookie(request, "loginCookie");
			if(cookie != null) {
				String session_key = cookie.getValue();
				//System.out.println("session_key : " + session_key);
				AdminBean bean = new AdminCrudDAO().getMemberWithSession(session_key);
				if(bean != null) {
					//System.out.println("0");
					User user = userService.loadUserByUsername(bean.getUsername());
					user.setPassword("");
		            session.setAttribute("user", new Gson().toJson(user));
				} else {
					//System.out.println("1");
					try {
						response.sendRedirect("./login.do");
					} catch (IOException e) {
						e.printStackTrace();
					} 
					return "login";
				}
			} else {
				//System.out.println("2");
				try {
					response.sendRedirect("./login.do");
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return "login";
			}
		}
		
		return "contents/home";
	}
	
	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String openIndexPage() {
		return "contents/home";
	}
	
	@RequestMapping(value = "/test.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String test() throws Exception {

		String bean = null;
		bean = new PaymentCrudDAO().test();
		return bean;
	}
	
	@ExceptionHandler
	public @ResponseBody Status handle(Exception e, HttpServletResponse response) {
		if(e.getClass().getSimpleName().equals("AccessDeniedException")) {
			try {
				response.sendRedirect("./login.do"); 
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		logger.info("Exception Handler - " + e.getMessage());
		return new Status(400, "Exception handled!");
	}
}
