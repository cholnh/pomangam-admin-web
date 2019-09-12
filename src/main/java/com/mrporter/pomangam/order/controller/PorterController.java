package com.mrporter.pomangam.order.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mrporter.pomangam.common.pattern.vo.Status;
import com.mrporter.pomangam.common.security.model.UserService;
import com.mrporter.pomangam.order.dao.PaymentCrudDAO;
import com.mrporter.pomangam.order.dao.PaymentIndexCrudDAO;
import com.mrporter.pomangam.product.dao.ProductCrudDAO;
import com.mrporter.pomangam.restaurant.dao.RestaurantCrudDAO;
import com.mrporter.pomangam.target.dao.TargetCrudDAO;

@Controller
public class PorterController {
	@Autowired
    UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(PorterController.class);
	private static final String MAPPINGNAME = "porter";
	
	@RequestMapping(value = "/session/setItem.do")
	public void sessionSetItem(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "retUrl", required = false) String retUrl) throws Exception {
		if(retUrl == null || retUrl.isEmpty()) {
			retUrl = "/admin";
		}
		
		HttpSession session = request.getSession();
		session.setAttribute(key, value);
		response.sendRedirect(retUrl);
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+".do")
	public ModelAndView openIndexPage(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value = "time", required = false) String time) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		//PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		ModelAndView model = new ModelAndView();
		model.setViewName("contents/" + MAPPINGNAME);
		
		model.addObject("targetList", new TargetCrudDAO().getCompactList());
		model.addObject("restaurantList", new RestaurantCrudDAO().getCompactList());
		model.addObject("restaurantBeanList", new RestaurantCrudDAO().getBeanList());
		model.addObject("productList", new ProductCrudDAO().getCompactList());
		
		if(curTarget == null || curTarget.trim().equals("") || curTarget.equals("0")) {
			model.addObject("detailList", new TargetCrudDAO().getAllDetailList());
			model.addObject("orderTimeList", new PaymentCrudDAO().getAllOrderTimeList());
		} else {
			int idxTarget = Integer.parseInt(curTarget);
			model.addObject("detailList", new TargetCrudDAO().getDetailList(idxTarget));
			model.addObject("orderTimeList", new PaymentCrudDAO().getAllOrderTimeList(idxTarget));
		}
		
		//model.addObject("additionalList", new ProductCrudDAO().getAdditionalCompactList());
		
		if(time != null && time.length() > 0) {
			model.addObject("orderedRestaurantList", new PaymentIndexCrudDAO().getOrderedRestaurant(time, curTarget));
		}
		
		return model;
	}
	
	//private final static String msg1 = "[포만감] 인증번호 : ";
    //private final static String msg2 = "\n정확히 입력해주세요.";
    //private final static String tmplId = "pmg_auth_1";
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/setdone.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void setDone(
			@RequestParam(value = "idxes", required = false) String idxes,
			@RequestParam(value = "isMsg", required = false) boolean isMsg) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		PaymentCrudDAO payDAO = new PaymentCrudDAO();
		if(idxes!=null) {
			for(String idx : idxes.split(",")) {
				int pi = Integer.parseInt(idx);
				indexDAO.setStatus(1, pi);
				if(isMsg) {
					payDAO.sendOrderMsg(pi);
				}
			}
		}
		
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/deliveryarrive.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void deliveryArrive(
			HttpServletRequest request, 
			@RequestParam(value = "receive_time", required = false) String receive_time,
			@RequestParam(value = "where", required = false) String where) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		PaymentCrudDAO payDAO = new PaymentCrudDAO();
		payDAO.sendDeliveryArrive(receive_time, where.equals("")?null:where, curTarget);
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/deliverydelay.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void deliveryDelay(
			HttpServletRequest request, 
			@RequestParam(value = "delay_min", required = false) int delay_min,
			@RequestParam(value = "delay_reason", required = false) String delay_reason,
			@RequestParam(value = "receive_time", required = false) String receive_time,
			@RequestParam(value = "where", required = false) String where) throws Exception {
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		PaymentCrudDAO payDAO = new PaymentCrudDAO();
		payDAO.sendDeliveryDelay(delay_min, delay_reason, receive_time, where.equals("")?null:where, curTarget);
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/setcancel.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void setCancel(
			@RequestParam(value = "idxes", required = false) String idxes,
			@RequestParam(value = "isMsg", required = false) boolean isMsg) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		PaymentCrudDAO payDAO = new PaymentCrudDAO();
		if(idxes!=null) {
			for(String idx : idxes.split(",")) {
				int pi = Integer.parseInt(idx);
				indexDAO.setStatus(4, pi);
				if(isMsg) {
					payDAO.sendFailMsg(pi, "취소");
				}
			}
		}
		
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/setrefund.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody void setRefund(
			@RequestParam(value = "idxes", required = false) String idxes,
			@RequestParam(value = "isMsg", required = false) boolean isMsg) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		PaymentCrudDAO payDAO = new PaymentCrudDAO();
		if(idxes!=null) {
			for(String idx : idxes.split(",")) {
				int pi = Integer.parseInt(idx);
				indexDAO.setStatus(5, pi);
				if(isMsg) {
					payDAO.sendFailMsg(pi, "환불");
				}
			}
		}
		
	}
	
	/*
	public static void main(String...args) {
		//System.out.println(BizmApi.send("821064784899", (msg1 + "1234" + msg2), tmplId));
		sendtest();
	}
	
	
	public static void sendtest() {
		String tmplId = "pmg_admin_3";
		
		Integer boxNumber = 1234;
		Integer orderNumber = 554422;
		String arrivalDateTime = "2019-03-07 12:00";
		String takeTime = "11:45";
		String detail = "test^^";
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		try {
			cal1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalDateTime));
			cal2.setTime(new SimpleDateFormat("HH:mm").parse(takeTime));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String text = "주문번호 : " + boxNumber + " (no." + orderNumber + ")" + System.lineSeparator() +
					"배달시간 : " + cal1.get(Calendar.YEAR) + "-" + timeWithZero(cal1.get(Calendar.MONTH)) + "-" + timeWithZero(cal1.get(Calendar.DATE)) + " (" + timeWithZero(cal1.get(Calendar.HOUR_OF_DAY)) + "시 " + timeWithZero(cal1.get(Calendar.MINUTE)) + "분)" + System.lineSeparator() +
					"※ " + timeWithZero(cal2.get(Calendar.HOUR_OF_DAY)) + "시 " + timeWithZero(cal2.get(Calendar.MINUTE)) + "분에 방문 예정 ※" + System.lineSeparator() +
					"------------------------" + System.lineSeparator() +
					"품명 : " + detail;
		System.out.println(text);
		//System.out.println(BizmApi.send("821064784899", text, tmplId));
	}
	
	private static String timeWithZero(int n) {
		if(n < 10) {
			return "0"+n;
		}
		return n+"";
	}
	*/
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
			HttpServletRequest request, 
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "res", required = false) String res) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		if(time != null && res != null) {
			return new PaymentIndexCrudDAO().getTodayJsonWithTimeAndRes(time, res, curTarget);
		}
		if(time != null) {
			return new PaymentIndexCrudDAO().getTodayJsonWithTime(time, curTarget);
		}

		return new PaymentIndexCrudDAO().getTodayJson(curTarget);
	}
	
	@RequestMapping(value = "/"+MAPPINGNAME+"/gettotallist.do", 
			produces = "application/json; charset=utf-8")
	public @ResponseBody String getTotalList(HttpServletRequest request) throws Exception {
		
		String curTarget = request.getSession().getAttribute("curTarget") == null ? null : request.getSession().getAttribute("curTarget")+"";
		
		return new PaymentIndexCrudDAO().getTotalJson(curTarget);
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
