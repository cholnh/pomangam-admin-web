package com.mrporter.pomangam.coupon.controller;

import java.io.IOException;

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
import com.mrporter.pomangam.common.util.Date;
import com.mrporter.pomangam.coupon.dao.CouponCrudDAO;
import com.mrporter.pomangam.coupon.vo.CouponBean;

@Controller
public class CouponController {
	
	private static final Logger logger = LoggerFactory.getLogger(CouponController.class);
	private static final String MAPPINGNAME = "coupon"; 
	private static CouponCrudDAO defaultDAO = new CouponCrudDAO();
	
	@RequestMapping(value = "/"+MAPPINGNAME+".do")
	public ModelAndView openIndexPage() throws Exception {

		
		ModelAndView model = new ModelAndView();

		model.setViewName("contents/" + MAPPINGNAME);
		return model;
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/getlist.do", 
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
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/getbean.do", 
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
	
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/gencode.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody Status genCode(
			@RequestParam(value = "ctgcode", required = false) String ctgcode) throws Exception {

		return new Status(200, defaultDAO.makeCpno(ctgcode));
	}
	
	@Secured({"ROLE_COUPON_EDIT"})
	@RequestMapping(value = "/"+MAPPINGNAME+"/insert.do")
	public @ResponseBody Status insert(
			CouponBean bean,
			@RequestParam(value = "makecnt", required = false) Integer makecnt,
			@RequestParam(value = "ctgcode", required = false) String ctgcode,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		
		if(makecnt != null) {
			for(int i=0; i<makecnt.intValue(); i++) {
				CouponBean b = new CouponBean();
				b.setCpno(defaultDAO.makeCpno(ctgcode));
				b.setAvailability(bean.getAvailability());
				b.setCpname(bean.getCpname());
				b.setDiscount_prc(bean.getDiscount_prc());
				b.setReg_username(bean.getReg_username());
				b.setState_active(bean.getState_active());
				
				b.setRegister_date(Date.getCurDate());
				b.setModify_date(Date.getCurDate());
				b.setStart_date(Date.getCurDate());
				b.setModify_date(Date.getCurDate());
				defaultDAO.insert(b);
			}
		} else {
			bean.setRegister_date(Date.getCurDate());
			bean.setModify_date(Date.getCurDate());
			bean.setStart_date(Date.getCurDate());
			bean.setModify_date(Date.getCurDate());
			defaultDAO.insert(bean);
		}
		
		
		
		
		return new Status(200);
	}

	@Secured({"ROLE_COUPON_EDIT"})
	@RequestMapping(value = "/"+MAPPINGNAME+"/update.do")
	public @ResponseBody Status update(
			CouponBean bean,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		
		bean.setModify_date(Date.getCurDate());
		
		defaultDAO.update(bean);
		return new Status(200);
	}

	@Secured({"ROLE_COUPON_EDIT"})
	@RequestMapping(value = "/"+MAPPINGNAME+"/delete.do")
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
