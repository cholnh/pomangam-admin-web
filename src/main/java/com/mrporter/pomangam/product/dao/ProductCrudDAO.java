package com.mrporter.pomangam.product.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.map.dao.MapCrudDAO;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.product.vo.ProductBean;

/**
 * ProductCrudDAO
 * 
 * @version 1.0 [2018. 8. 15.]
 * @author Choi
 */
public class ProductCrudDAO extends Crud<ProductBean> {
	private static final String TABLENAME = "product"; 
	private static Calendar start;
	private static Calendar end;
	//private static int start_hour;
	//private static int start_minute;
	//private static int end_hour;
	//private static int end_minute;
	
	/**
	 * @param tableName
	 */
	public ProductCrudDAO() {
		super(TABLENAME);
		String[] starttime = new MapCrudDAO().getValue("time_start").split(":");
		String[] endtime = new MapCrudDAO().getValue("time_end").split(":");
		int start_hour = Integer.parseInt(starttime[0]);
		int start_minute = Integer.parseInt(starttime[1]);
		int end_hour = Integer.parseInt(endtime[0]);
		int end_minute = Integer.parseInt(endtime[1]);
		
		start = Calendar.getInstance();
		start.set(Calendar.MINUTE, start_minute);
		if(start_hour>=12) {
			start.set(Calendar.AM_PM, Calendar.PM);
			start.set(Calendar.HOUR, start_hour-12);
		} else {
			start.set(Calendar.AM_PM, Calendar.AM);
			start.set(Calendar.HOUR, start_hour);
		}
		
		end = Calendar.getInstance();
		end.set(Calendar.MINUTE, end_minute);
		if(end_hour>=12) {
			end.set(Calendar.AM_PM, Calendar.PM);
			end.set(Calendar.HOUR, end_hour-12);
		} else {
			end.set(Calendar.AM_PM, Calendar.AM);
			end.set(Calendar.HOUR, end_hour);
		}
		
	}
	
	public String getAdditionalCompactList() throws Exception {
		List<Map<String, Object>> listOfMaps 
			= sqlQuery("SELECT * FROM product_additional;");
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	public String getCompactList() throws Exception {
		List<Map<String, Object>> listOfMaps 
			= sqlQuery("SELECT idx, name, price, cnt_sell FROM product;");
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	public List<ProductBean> getBeanList(Integer idx) throws Exception {
		List<ProductBean> result = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT * FROM " + TABLENAME + " WHERE idx_restaurant=?", idx);
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<ProductBean>>() {}.getType());
		}
		return result;
	}
	
	public List<ProductBean> getRecommend(Integer idx) throws Exception {
		List<ProductBean> result = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT p.idx, p.imgpath, p.name, p.price " +
				"FROM product p, product_recommend r " +
				"WHERE r.idx_product = ? AND p.idx = r.idx_product_recommend;", idx);
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<ProductBean>>() {}.getType());
		}
		return result;
	}
	
	/*
	public void reduceLimitCount(long difftime) throws Exception {
		sqlUpdate(
				"UPDATE " +
					"product " +
				"SET " + 
					"cnt_limit = (?*unit_amount/unit_time) ", difftime);
	}
	*/
	
	public void addCountSell(Integer idx) throws Exception {
		
		List<Map<String, Object>> lom = 
				sqlQuery("SELECT idxes_payment FROM payment_index WHERE idx = ?;", idx);
		
		String[] idxes = (lom.get(0).get("idxes_payment")+"").split(",");
		for(String idx_payment : idxes) {
			List<Map<String, Object>> lom2 = 
					sqlQuery("SELECT idx_product, amount FROM payment WHERE idx = ?;", idx_payment);
			
			sqlUpdate("UPDATE product SET cnt_sell = cnt_sell + ? WHERE idx = ?", 
					lom2.get(0).get("amount"),
					lom2.get(0).get("idx_product"));
		}
	}
	
	public void resetCountSell() throws Exception {
		sqlUpdate("UPDATE product SET cnt_sell = 0");
	}
	
	public int getAmount(Integer idx_product, int minute) throws Exception {
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT " + 
					"TRUNCATE(?*unit_amount/unit_time, 0) - cnt_sell " + 
					"as amount " + 
				"FROM " + 
					"product " +
				"WHERE idx = ?;", minute, idx_product);
		int amount = Integer.parseInt(lom.get(0).get("amount")+"");
		return amount>0?amount:0;
	}
	
	public Calendar getTime(Integer idx_product, int amount) throws Exception {
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT " + 
					"TRUNCATE(((?*unit_time/unit_amount) + " + 
					"(SELECT (cnt_sell*unit_time/unit_amount) FROM product WHERE idx = ?)) * 60, 0) " + 
					"as seconds " + 
				"FROM " + 
					"product " +
				"WHERE idx = ?;", amount, idx_product, idx_product);
		int seconds = Integer.parseInt(lom.get(0).get("seconds")+"");
		Calendar cur = Calendar.getInstance();
		cur.add(Calendar.SECOND, seconds);
		Calendar next = getNext(cur);
		//System.out.println("next : "+new Date(next.getTimeInMillis()));
		return next;
	}
	
	public Calendar getNext() {
		return getNext(Calendar.getInstance());
	}
	
	public Calendar getNext(Calendar cal) {
		start.set(Calendar.DATE, cal.get(Calendar.DATE));
		end.set(Calendar.DATE, cal.get(Calendar.DATE));
		
		//System.out.println("start : "+ new Date(start.getTimeInMillis()).toString());
		//System.out.println("end : " + new Date(end.getTimeInMillis()).toString());
		//System.out.println("cur : " + new Date(cal.getTimeInMillis()).toString());
		
		if(cal.compareTo(end) > 0) { // if 'cal' is after the 'end'
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.AM_PM, Calendar.PM);
			cal.set(Calendar.MINUTE, 0);
			return cal;
		}
		
		if(cal.compareTo(start) < 0) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.AM_PM, Calendar.PM);
			cal.set(Calendar.MINUTE, 0);
			return cal;
		}
		
		cal.set(Calendar.SECOND, 0);
		if(cal.get(Calendar.MINUTE) >= 30) {
			cal.set(Calendar.HOUR, (cal.get(Calendar.HOUR)+1));
			cal.set(Calendar.MINUTE, 30);
		} else {
			cal.set(Calendar.MINUTE, 30);
		}
		
		cal.add(Calendar.MINUTE, 30); // 배달시간 +30분
		return cal;
	}
}
