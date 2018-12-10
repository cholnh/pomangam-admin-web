package com.mrporter.pomangam.order.controller;

import java.io.IOException;

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
import com.mrporter.pomangam.product.dao.ProductCrudDAO;
import com.mrporter.pomangam.restaurant.dao.RestaurantCrudDAO;
import com.mrporter.pomangam.target.dao.TargetCrudDAO;

@Controller
public class PorterController {
	
	private static final Logger logger = LoggerFactory.getLogger(PorterController.class);
	private static final String MAPPINGNAME = "porter";
	
	@RequestMapping(value = "/"+MAPPINGNAME+".do")
	public ModelAndView openIndexPage(
			@RequestParam(value = "time", required = false) String time) throws Exception {

		//PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		
		ModelAndView model = new ModelAndView();
		model.setViewName("contents/" + MAPPINGNAME);
		model.addObject("targetList", new TargetCrudDAO().getCompactList());
		model.addObject("restaurantList", new RestaurantCrudDAO().getCompactList());
		model.addObject("restaurantBeanList", new RestaurantCrudDAO().getBeanList());
		model.addObject("productList", new ProductCrudDAO().getCompactList());
		//model.addObject("additionalList", new ProductCrudDAO().getAdditionalCompactList());
		
		if(time != null && time.length() > 0) {
			model.addObject("orderedRestaurantList", new PaymentIndexCrudDAO().getOrderedRestaurant(time));
		}
		
		return model;
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/setdone.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void setDone(
			@RequestParam(value = "idxes", required = false) String idxes) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		if(idxes!=null) {
			for(String idx : idxes.split(",")) {
				indexDAO.setStatus(1, Integer.parseInt(idx));
			}
		}
		
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/setorderdone.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void setOrderDone(
			@RequestParam(value = "idxes", required = false) String idxes) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		if(idxes!=null) {
			for(String idx : idxes.split(",")) {
				indexDAO.setOrderStatus(1, Integer.parseInt(idx));
			}
		}
		
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/getdetail.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getDetail(
			@RequestParam(value = "idxes_payment", required = false) String idxes_payment) throws Exception {
		
		return new PaymentIndexCrudDAO().getDetail(idxes_payment);
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/gettotaylist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getTodayList(
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "res", required = false) String res) throws Exception {
		
		if(time != null && res != null) {
			return new PaymentIndexCrudDAO().getTodayJsonWithTimeAndRes(time, res);
		}
		if(time != null) {
			return new PaymentIndexCrudDAO().getTodayJsonWithTime(time);
		}

		return new PaymentIndexCrudDAO().getTodayJson();
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/gettotallist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getTotalList() throws Exception {
		
		return new PaymentIndexCrudDAO().getTotalJson();
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
