package com.mrporter.pomangam.common.index;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.order.dao.PaymentCrudDAO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String openDefaultPage() {
		logger.info("Welcome home!");
		
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
