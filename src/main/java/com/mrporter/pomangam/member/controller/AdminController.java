package com.mrporter.pomangam.member.controller;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.mrporter.pomangam.common.security.crypt.RSA;
import com.mrporter.pomangam.common.util.Date;
import com.mrporter.pomangam.member.dao.AdminCrudDAO;
import com.mrporter.pomangam.member.dao.AuthorityCrudDAO;
import com.mrporter.pomangam.member.vo.AdminBean;

@Controller
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	private static final String TABLENAME = "admin"; 
	private static AdminCrudDAO defaultDAO = new AdminCrudDAO();
	
	@Secured({"ROLE_ADMIN_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+".do")
	public ModelAndView openIndexPage(
			HttpServletRequest request) throws Exception {
		
		setRSA(request, 1024);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("contents/" + TABLENAME);
		model.addObject("authnames", new AuthorityCrudDAO().getAuthName());
		return model;
	}
	
	@Secured({"ROLE_ADMIN_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+"/getlist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getList(
			@RequestParam(value = "tail", required = false) String tail) throws Exception {
		
		String list;
		if(tail == null) {
			list = defaultDAO.getList();
		} else {
			list = defaultDAO.getList(tail);
		}
		return list;
	}
	
	@Secured({"ROLE_ADMIN_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+"/getadminlist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getAdminList(
			@RequestParam(value = "tail", required = false) String tail) throws Exception {
		
		String list;
		if(tail == null) {
			list = defaultDAO.getAdminList();
		} else {
			list = defaultDAO.getAdminList(tail);
		}
		return list;
	}
	
	@Secured({"ROLE_ADMIN_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+"/getownerlist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getOwnerList(
			@RequestParam(value = "tail", required = false) String tail) throws Exception {
		
		String list;
		if(tail == null) {
			list = defaultDAO.getOwnerList();
		} else {
			list = defaultDAO.getOwnerList(tail);
		}
		return list;
	}

	@Secured({"ROLE_ADMIN_VIEW"})
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

	@Secured({"ROLE_ADMIN_EDIT"})
	@RequestMapping(value = "/"+TABLENAME+"/insert.do")
	public @ResponseBody Status insert(
			AdminBean bean,
			HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		bean.setUsername(decryptRSA(request, bean.getUsername()));
		bean.setPassword(decryptRSA(request, bean.getPassword()));
		bean.setModdate(Date.getCurDate());
		
		defaultDAO.insert(bean);
		return new Status(200);
	}

	@Secured({"ROLE_ADMIN_EDIT"})
	@RequestMapping(value = "/"+TABLENAME+"/update.do")
	public @ResponseBody Status update(
			AdminBean bean,
			HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		bean.setUsername(decryptRSA(request, bean.getUsername()));
		bean.setPassword(decryptRSA(request, bean.getPassword()));
		bean.setModdate(Date.getCurDate());
		
		defaultDAO.update(bean);
		return new Status(200);
	}

	@Secured({"ROLE_ADMIN_EDIT"})
	@RequestMapping(value = "/"+TABLENAME+"/delete.do")
	public @ResponseBody Status delete(
			@RequestParam(value = "idx", required = false) Integer idx,
			@RequestParam(value = "idxes", required = false) String idxes) throws Exception {

		if(idx != null) {
			defaultDAO.delete(idx);
		}
		if(idxes != null) {
			String[] str = idxes.split(",");
			Integer[] parts = new Integer[str.length];
			for(int i=0; i<str.length; i++) {
				parts[i] = Integer.parseInt(str[i]);
			}
			defaultDAO.delete(parts);
		}
		return new Status(200);
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
	
	private String decryptRSA(HttpServletRequest request, String cipher) {
		if(cipher == null || cipher.length()==0) return null;
		
		String plainText = null;
		PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("__rsaPrivateKey__");
		if (privateKey == null) {
			throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
		} else {
			RSA rsa = new RSA(); 
			plainText = rsa.decrypt(privateKey, cipher);
		}
		return plainText;
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
