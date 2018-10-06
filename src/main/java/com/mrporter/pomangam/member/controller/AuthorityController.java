package com.mrporter.pomangam.member.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.member.dao.AuthorityCrudDAO;

@Controller
public class AuthorityController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorityController.class);
	private static final String TABLENAME = "authority"; 
	private static AuthorityCrudDAO defaultDAO = new AuthorityCrudDAO();
	
	@Secured({"ROLE_AUTHORITY_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+".do")
	public ModelAndView openIndexPage() throws Exception {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("contents/" + TABLENAME);
		model.addObject("authnames", defaultDAO.getAuthName());
		model.addObject("authlist", defaultDAO.getList());
		return model;
	}

	@Secured({"ROLE_AUTHORITY_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+"/getlist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getList() throws Exception {
		
		String list = defaultDAO.getList();
		return list;
	}

	@Secured({"ROLE_AUTHORITY_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+"/getbean.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getBean(
			@RequestParam(value = "idx", required = false) Integer idx,
			@RequestParam(value = "column", required = false) String column,
			@RequestParam(value = "value", required = false) String value) throws Exception {

		String bean = null;
		if(idx != null) {
			bean = defaultDAO.getBean(idx);
		} else if(column!=null && value!=null) {
			bean = defaultDAO.getBean(column, value);
		}
		return bean;
	}

	@Secured({"ROLE_AUTHORITY_EDIT"})
	@RequestMapping(value = "/"+TABLENAME+"/insert.do")
	public @ResponseBody Status insert(
			HttpServletRequest request,
			@RequestParam(value = "authname", required = true) String authname,
			@RequestParam(value = "authorityDefault", required = false) boolean authorityDefault) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		List<String> authnames = defaultDAO.getAuthName();
		if(authnames.contains(authname)) {
			return new Status(400, "권한 이름이 중복됩니다.");
		} else {
			defaultDAO.insert(authname, authorityDefault);
			return new Status(200);
		}
	}

	@Secured({"ROLE_AUTHORITY_EDIT"})
	@RequestMapping(value = "/"+TABLENAME+"/update.do")
	public @ResponseBody Status update(
			HttpServletRequest request,
			@RequestParam(value = "authname", required = true) String authname,
			@RequestParam(value = "auth", required = true) String auth) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		defaultDAO.update(authname, auth);
		return new Status(200);
	}

	@Secured({"ROLE_AUTHORITY_EDIT"})
	@RequestMapping(value = "/"+TABLENAME+"/delete.do")
	public @ResponseBody Status delete(
			@RequestParam(value = "idx", required = false) Integer idx,
			@RequestParam(value = "authname", required = false) String authname) throws Exception {
		if(idx != null) {
			defaultDAO.delete(idx);
			return new Status(200);
		} else if(authname != null) {
			defaultDAO.delete(authname);
			return new Status(200);
		} else {
			return new Status(400, "idx 또는 authname이 없습니다.");
		}
		
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
		return new Status(400, "Exception handled!" + e.getMessage());
	}
	
}
