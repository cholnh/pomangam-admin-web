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
	
	public List<Integer> getOrderedRestaurant(String time) throws Exception {
		
		List<Map<String, Object>> lom 
		= sqlQuery(
				"SELECT idxes_payment FROM payment_index WHERE receive_date = ? AND receive_time = ?", Date.getCurDay(), time+"시");
		
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
	
	public String getTodaySettlementList() throws Exception {
		return getTodaySettlementList(Date.getCurDay());
	}
	
	public String getTodaySettlementList(String date) throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery(
				"SELECT " +
					"pay.idx, pi.idx as pi_idx, res.name as res_name, pay.amount, pay.additional, pro.name as pro_name, pro.price, pi.status, pi.cashreceipt, pro.c_commission_prc, pro.s_commission_prc " +
				"FROM " +
					"payment pay, product pro, restaurant res, payment_index pi " +
				"WHERE " + 
					"pi.receive_date = ? and " +
		 			"pi.idx = pay.idx_payment_index  and " +
					"pay.idx_product = pro.idx and " +
					"pay.idx_restaurant = res.idx and pi.status = (1|3) ORDER BY res_name", date);
		
		Gson gson = new Gson();
		List<PaymentIndexBean> list = gson.fromJson(gson.toJson(lom), new TypeToken<List<PaymentIndexBean>>() {}.getType());
		
		return new Gson().toJson(lom);
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
	
	public String getTodayJsonWithTimeAndRes(String time, String res) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND receive_time = ? order by receive_time desc, idx_box desc;",
				Date.getCurDay(), time+"시");

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
	public String getTodayJsonWithTime(String time) throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? AND receive_time = ? order by receive_time desc, idx_box desc;",
				Date.getCurDay(), time+"시");
		Gson gson = new Gson();
		return gson.toJson(lom);
	}

	public String getTodayJson() throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? order by receive_time desc, idx_box desc;",
		//= sqlQuery("SELECT * FROM payment_index WHERE receive_date = ? order by idx desc;",
				Date.getCurDay());
		Gson gson = new Gson();
		return gson.toJson(lom);
	}
	
	public String getTotalJson() throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM payment_index order by idx desc;");
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
