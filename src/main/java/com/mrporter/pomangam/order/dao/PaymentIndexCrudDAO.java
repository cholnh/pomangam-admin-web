package com.mrporter.pomangam.order.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.common.util.Date;
import com.mrporter.pomangam.order.vo.PaymentIndexBean;
import com.mrporter.pomangam.order.vo.SettlementBean;

/**
 * PaymentIndexCrudDAO
 * 
 * @version 1.0 [2018. 8. 19.]
 * @author Choi
 */
public class PaymentIndexCrudDAO extends Crud<PaymentIndexBean> {
	private static final Logger logger = LoggerFactory.getLogger(PaymentIndexCrudDAO.class);
	private static final String TABLENAME = "payment_index"; 
	
	/**
	 * @param tableName
	 */
	public PaymentIndexCrudDAO() {
		super(TABLENAME);
	}
	
	public String getCpList(String curTarget) throws Exception {
		return getCpList(Date.getCurDay(), curTarget);
	}
	
	public String getCpList(String date, String curTarget) throws Exception {
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom = sqlQuery(
					"SELECT " +
							"pi.idx, cp.cpname, pi.cpno, cp.discount_prc, cp.use_username " +
						"FROM " +
							"payment_index pi, coupon cp " +
						"WHERE " + 
							"pi.receive_date = ? " +
				 			"AND pi.cpno IS NOT NULL " +
							"AND pi.cpno = cp.cpno ", date);
		} else {
			lom = sqlQuery(
					"SELECT " +
							"pi.idx, cp.cpname, pi.cpno, cp.discount_prc, cp.use_username " +
						"FROM " +
							"payment_index pi, coupon cp " +
						"WHERE " + 
							"pi.receive_date = ? " +
				 			"AND pi.cpno IS NOT NULL " +
							"AND pi.cpno = cp.cpno AND pi.idx_target = ? ", date, curTarget);
		}
		
		return new Gson().toJson(lom);
	}
	
	public List<Integer> getOrderedRestaurant(String time, String curTarget) throws Exception {
		
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom = sqlQuery(
					"SELECT idxes_payment FROM payment_index WHERE receive_date = ? AND receive_time = ?", Date.getCurDay(), time+"시");
		} else {
			lom = sqlQuery(
					"SELECT idxes_payment FROM payment_index WHERE receive_date = ? AND receive_time = ? AND idx_target = ? ", Date.getCurDay(), time+"시", curTarget);
		}
		
		
		List<Integer> result = new ArrayList<>();
		Set<String> set = new HashSet<>();
		
		if(lom!=null && !lom.isEmpty()) {
			
			for(Map<String, Object> map : lom) {
				String[] idxes_payment = (map.get("idxes_payment")+"").split(",");
				for(String idx : idxes_payment) {
					List<Map<String, Object>> lom2 
					= sqlQuery(
							"SELECT idx_restaurant FROM payment WHERE idx = ?", idx);
					if(lom2!=null && !lom2.isEmpty()) {
						String idx_restaurant = lom2.get(0).get("idx_restaurant")+"";
						set.add(idx_restaurant);
					}
				}
			}
		}
		
		for(String idx : set) {
			result.add(Integer.parseInt(idx));
		}
		
		return result;
	}
	
	public void setOrderStatus(Integer status, Integer idx) throws Exception {
		sqlUpdate("UPDATE payment SET status = ? WHERE idx = ?", status, idx);
	}
	
	public void setStatus(Integer status, Integer idx) throws Exception {
		sqlUpdate("UPDATE payment_index SET status = ? WHERE idx = ?", status, idx);
	}
	
	public String getTodaySettlementList(String curTarget) throws Exception {
		return getTodaySettlementList(Date.getCurDay(), curTarget);
	}
	
	public String getTodaySettlementList(String date, String curTarget) throws Exception {
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom = sqlQuery(
					"SELECT " +
							"pay.idx, pi.idx as pi_idx, res.name as res_name, pay.amount, pay.additional, pro.name as pro_name, pro.price, pi.status, pi.cashreceipt, pro.c_commission_prc, pro.s_commission_prc, pi.cpno AS cpno " +
						"FROM " +
							"payment pay, product pro, restaurant res, payment_index pi " +
						"WHERE " + 
							"pi.receive_date = ? and " +
				 			"pi.idx = pay.idx_payment_index  and " +
							"pay.idx_product = pro.idx and " +
							"pay.idx_restaurant = res.idx ORDER BY res_name", date);
		} else {
			lom = sqlQuery(
					"SELECT " +
							"pay.idx, pi.idx as pi_idx, res.name as res_name, pay.amount, pay.additional, pro.name as pro_name, pro.price, pi.status, pi.cashreceipt, pro.c_commission_prc, pro.s_commission_prc, pi.cpno AS cpno " +
						"FROM " +
							"payment pay, product pro, restaurant res, payment_index pi " +
						"WHERE " + 
							"pi.receive_date = ? and " +
				 			"pi.idx = pay.idx_payment_index  and " +
							"pay.idx_product = pro.idx and " +
							"pay.idx_restaurant = res.idx AND pi.idx_target = ? ORDER BY res_name", date, curTarget);
		}
		
		return new Gson().toJson(lom);
	}
	
	public String getAutoSettlement(String curTarget) throws Exception {
		return getAutoSettlement(Date.getCurDay(), curTarget);
	}
	
	public String getAutoSettlement(String date, String curTarget) throws Exception {
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom 
			= sqlQuery(
					"SELECT " + 
					    "res.name AS res_name, " +
					    "SUM((pro.c_commission_prc + pro.s_commission_prc) * pay.amount) AS commission " +
					"FROM " +
					    "payment pay, product pro, restaurant res, payment_index pi " +
					"WHERE " + 
					    "pi.receive_date = ? " +
					        "AND pi.idx = pay.idx_payment_index " +
					        "AND pay.idx_product = pro.idx " +
					        "AND pay.idx_restaurant = res.idx " +
					        "AND pi.status IN (1, 3) " +
					"GROUP BY res_name", date);
		} else {
			lom 
			= sqlQuery(
					"SELECT " + 
					    "res.name AS res_name, " +
					    "SUM((pro.c_commission_prc + pro.s_commission_prc) * pay.amount) AS commission " +
					"FROM " +
					    "payment pay, product pro, restaurant res, payment_index pi " +
					"WHERE " + 
					    "pi.receive_date = ? " +
					        "AND pi.idx = pay.idx_payment_index " +
					        "AND pay.idx_product = pro.idx " +
					        "AND pay.idx_restaurant = res.idx " +
					        "AND pi.status IN (1, 3) " +
					        "AND pi.idx_target = ? " +
					"GROUP BY res_name", date, curTarget);
		}
		
		if(lom==null||lom.isEmpty()) { 
			return null; 
		}
		
		List<SettlementBean> list = new ArrayList<>();
		
		for(Map<String, Object> map : lom) {
			SettlementBean bean = new SettlementBean();
			String res_name = map.get("res_name")+"";
			int commission = map.get("commission")==null
					? 0
					: Integer.parseInt(map.get("commission")+"");
			
			List<Map<String, Object>> lom2 
			= sqlQuery(
					"SELECT " + 
					    "pay.amount AS amount, pay.additional AS additional, pro.price AS price " +
					"FROM " +
					    "payment pay, product pro, restaurant res, payment_index pi " +
					"WHERE " + 
					    "pi.receive_date = ? " +
					        "AND pi.idx = pay.idx_payment_index " +
					        "AND pay.idx_product = pro.idx " +
					        "AND pay.idx_restaurant = res.idx " +
					        "AND pi.status IN (1, 3) " +
					        "AND res.name = ?", date, res_name);
			
			int amountTotal = 0;
			int sales = 0;
			for(Map<String, Object> map2 : lom2) {
				int additionalPrice = 0;
				int amount = Integer.parseInt(map2.get("amount")+"");
				amountTotal += amount;
				if(!(map2.get("additional")+"").trim().equals("")) {
					String[] additionals = (map2.get("additional")+"").split(",");
					for(String part : additionals) {
						String[] additional = part.split("-");
						additionalPrice += Integer.parseInt(additional[1]) * Integer.parseInt(additional[2]);
					}
				}
				sales += (additionalPrice + Integer.parseInt(map2.get("price")+"")) * amount;
			}
			
			bean.setRes_name(res_name);
			bean.setAmount(amountTotal);
			bean.setSales(sales);
			bean.setCommission(commission);
			bean.setPurchase(sales-commission);
			list.add(bean);
		}
		return new Gson().toJson(list);
	}
	
	public static void main(String...args) {
		try {
			new PaymentIndexCrudDAO().getAutoSettlement("2019-03-20");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getDetail(String idxes_payment) throws Exception {
		if(idxes_payment==null || idxes_payment.length()==0) return "";
		
		Gson gson = new Gson();
		String[] idxes = idxes_payment.split(",");
		List<Map<String, Object>> res = new ArrayList<>(); 
		for(String idx : idxes) {
			List<Map<String, Object>> lom 
			= sqlQuery("SELECT * FROM payment WHERE idx = ?;", idx);
			
			res.add(lom.get(0));
		}
		return gson.toJson(res);
	}
	
	public List<PaymentIndexBean> getTodayList() throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? order by receive_time desc, idx_box desc;",
		//= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? order by idx desc;",
				Date.getCurDay());
		Gson gson = new Gson();
		List<PaymentIndexBean> list = gson.fromJson(gson.toJson(lom), new TypeToken<List<PaymentIndexBean>>() {}.getType());
		return list;
	}
	
	public List<PaymentIndexBean> getTotalList() throws Exception {
		List<Map<String, Object>> lom 
		//= sqlQuery("SELECT * FROM payment_index order by receive_date, receive_time, idx_box desc;");
		= sqlQuery("SELECT * FROM payment_index order by idx desc;");
		Gson gson = new Gson();
		List<PaymentIndexBean> list = gson.fromJson(gson.toJson(lom), new TypeToken<List<PaymentIndexBean>>() {}.getType());
		return list;
	}
	
	public String getTodayJsonWithTimeAndRes(String time, String res, String curTarget) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom 
			= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND receive_time = ? order by receive_time desc, idx_box desc;",
					Date.getCurDay(), time+"시");
		} else {
			lom 
			= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND receive_time = ? AND idx_target = ? order by receive_time desc, idx_box desc;",
					Date.getCurDay(), time+"시", curTarget);
		}

		if(lom!=null && !lom.isEmpty()) {
			
			for(Map<String, Object> map : lom) {
				String[] idxes_payment = (map.get("idxes_payment")+"").split(",");
				
				
				for(String idx : idxes_payment) {
					List<Map<String, Object>> lom2 
					= sqlQuery(
							"SELECT idx_restaurant FROM payment WHERE idx = ?", idx);
					if(lom2!=null && !lom2.isEmpty()) {
						String idx_restaurant = lom2.get(0).get("idx_restaurant")+"";
						if(idx_restaurant.equals(res)){
							result.add(map);
							break;
						}
					}
				}
				
			}
		}

		Gson gson = new Gson();
		return gson.toJson(result);
	}
	public String getTodayJsonWithTime(String time, String curTarget) throws Exception {
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom 
			= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND receive_time = ? order by receive_time desc, idx_box desc;",
					Date.getCurDay(), time+"시");
		} else {
			lom 
			= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND receive_time = ? AND idx_target = ? order by receive_time desc, idx_box desc;",
					Date.getCurDay(), time+"시", curTarget);
		}
		
		Gson gson = new Gson();
		return gson.toJson(lom);
	}

	public String getTodayJson(String curTarget) throws Exception {
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom 
			= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? order by receive_time desc, idx_box desc;",
					Date.getCurDay());
		} else {
			lom 
			= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND idx_target = ? order by receive_time desc, idx_box desc;",
			//= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? order by idx desc;",
					Date.getCurDay(), curTarget);
		}
		
		Gson gson = new Gson();
		return gson.toJson(lom);
	}
	
	public String getTotalJson(String curTarget) throws Exception {
		List<Map<String, Object>> lom;
		if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
			lom = sqlQuery("SELECT * FROM payment_index order by idx desc;");
		} else {
			lom = sqlQuery("SELECT * FROM payment_index WHERE idx_target = ? order by idx desc;", curTarget);
		}
		
		Gson gson = new Gson();
		return gson.toJson(lom);
	}
	
	public int getTodayOrder() throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT count(*) FROM " + TABLENAME
				+ " WHERE timestamp > '" + Date.getCurDay() + " 00:00' "
				+ "AND timestamp < '" + Date.getCurDay() + " 24:00';");
		if(lom==null||lom.isEmpty()) 
			return 0;
		else
			return Integer.parseInt(lom.get(0).get("count(*)")+"");
	}
	
	public int makeBoxNumber(Integer idx_target, String receive_date, String receive_time) throws Exception {
		List<Map<String, Object>> lom 
		= super.sqlQuery(
				"SELECT " +
					"max(idx_box)+1 AS nextbn " + 
				"FROM " +
					"payment_index " +
				"WHERE " +
					"idx_target = ? AND receive_date = ? AND receive_time = ?;", 
					idx_target, receive_date, receive_time);
		Object obj = lom.get(0).get("nextbn");
		if(obj == null) {
			return 0;
		} else {
			return Integer.parseInt(obj+"");
		}
	}
	
	public int getBoxNumber(Integer idx) throws Exception {
		List<Map<String, Object>> lom 
		= super.sqlQuery(
				"SELECT " +
					"idx_box " + 
				"FROM " +
					"payment_index " +
				"WHERE " +
					"idx = ?;", idx);
		return Integer.parseInt(lom.get(0).get("idx_box")+"");
	}
	
	public boolean check(Integer idx, Integer PG_price) throws Exception {
		String idxes = getIdxes(idx);
		
		int sumAmount = 0;
		int sumPrice = 0;
		for(String i : idxes.split(",")) {
			List<Map<String, Object>> lom 
			= super.sqlQuery(
					"SELECT " +
							"pay.amount, pro.price " +
					"FROM " +
							"payment pay, product pro " +
					"WHERE "+
							"pay.idx_product = pro.idx AND pay.idx = ?;", i);
			int amount = Integer.parseInt(lom.get(0).get("amount")+"");
			int price = Integer.parseInt(lom.get(0).get("price")+"");
			sumPrice += amount * price;
			sumAmount += amount;
		}
		
		/*
		int tAmount = sumAmount>1?sumAmount-1:0;
		int tmp = tAmount * 500;
		int tPrice = (tmp > 2000 ? 2000 : tmp) + 2000;
		sumPrice += tPrice;
		 */
		boolean result = (PG_price.intValue() == sumPrice);
		if(!result) {
			logger.info("금액불일치\n"
					+ "idx : " + idx + "\n"
					+ "주문번호 : " + idxes + "\n"
					+ "배달수량 : " + sumAmount + "\n"
					+ "최종 : " + sumPrice + "\n"
					+ "PG_price : "+ PG_price + "\n"
					+ Date.getCurDate());
		}
		return result;
	}
	
	public String getPW(Integer idx) throws Exception {
		List<Map<String, Object>> lom 
		= super.sqlQuery(
				"SELECT " +
						"password " +
				"FROM " +
						"payment_index " +
				"WHERE "+
						"idx = ?;", idx);
		return (String) lom.get(0).get("password");
	}
	
	public void transactionFail(Integer idx) throws Exception {
		String idxes = getIdxes(idx);
		for(String i : idxes.split(",")) {
			super.sqlUpdate(
					"UPDATE " +
							"payment " +
					"SET " +
							"status=? " +
					"WHERE "+
							"idx = ?;", i, 1);
		}
	}
	
	public String getIdxes(Integer idx) throws Exception {
		String result = null;
		List<Map<String, Object>> lom 
		= super.sqlQuery(
				"SELECT idxes_payment FROM payment_index WHERE idx=?", idx);
		if(!lom.isEmpty()) {
			result = (String) lom.get(0).get("idxes_payment");
		}
		return result;
	}
	
	public int getSum(String[] idxes) throws Exception {
		int sum = 0;
		for(String idx : idxes) {
			List<Map<String, Object>> lom 
			= super.sqlQuery(
					"SELECT " +
							"pay.amount, pro.price " +
					"FROM " +
							"payment pay, product pro " +
					"WHERE "+
							"pay.idx_product = pro.idx AND pay.idx = ?;",idx);
			int amount = Integer.parseInt(lom.get(0).get("amount")+"");
			int price = Integer.parseInt(lom.get(0).get("price")+"");
			sum += amount * price;
		}
		
		return sum;
	}
}
