package com.mrporter.pomangam.order.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.order.dao.PaymentIndexCrudDAO;

@Controller
public class SettlementController {
	
	private static final Logger logger = LoggerFactory.getLogger(SettlementController.class);
	private static final String MAPPINGNAME = "settlement";
	
	@RequestMapping(value = "/"+MAPPINGNAME+".do")
	public ModelAndView openIndexPage() throws Exception {

		
		ModelAndView model = new ModelAndView();
		model.setViewName("contents/" + MAPPINGNAME);
		
		return model;
	}

	@RequestMapping(value = "/"+MAPPINGNAME+"/getlist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getTotalList(
			HttpServletRequest request, 
			@RequestParam(value = "date", required = false) String date) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		if(date == null) {
			return new PaymentIndexCrudDAO().getTodaySettlementList(curTarget);
		} else {
			return new PaymentIndexCrudDAO().getTodaySettlementList(date, curTarget);
		}
		
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/getcplist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getCpList(
			HttpServletRequest request, 
			@RequestParam(value = "date", required = false) String date) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		if(date == null) {
			return new PaymentIndexCrudDAO().getCpList(curTarget);
		} else {
			return new PaymentIndexCrudDAO().getCpList(date, curTarget);
		}
		
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/getautolist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getTotalAutoList(
			HttpServletRequest request, 
			@RequestParam(value = "date", required = false) String date) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		if(date == null) {
			return new PaymentIndexCrudDAO().getAutoSettlement(curTarget);
		} else {
			return new PaymentIndexCrudDAO().getAutoSettlement(date,curTarget);
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
		if(e.getClass().getSimpleName().equals("MethodArgumentTypeMismatchException")) {
			try {
				response.sendRedirect("./");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		logger.info("Exception Handler - " + e.getMessage());
		return new Status(400, "Exception handled!");
	}
	
}
