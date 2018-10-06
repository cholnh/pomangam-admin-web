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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.restaurant.dao.RestaurantCrudDAO;

@Controller
public class OwnerController {
	
	private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);
	private static final String TABLENAME = "owner"; 

	
	@Secured({"ROLE_OWNER_VIEW"})
	@RequestMapping(value = "/"+TABLENAME+".do")
	public ModelAndView openIndexPage(
			HttpServletRequest request) throws Exception {
		
		setRSA(request, 1024);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("contents/" + TABLENAME);
		model.addObject("restaurantList", new RestaurantCrudDAO().getBeanList());
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
