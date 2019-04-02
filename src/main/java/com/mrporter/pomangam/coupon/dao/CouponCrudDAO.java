package com.mrporter.pomangam.coupon.dao;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.coupon.vo.CouponBean;

/**
 * CouponCrudDAO
 * 
 * @version 1.0 [2019. 3. 30.]
 * @author Choi
 */
public class CouponCrudDAO extends Crud<CouponBean> {
	private static final String TABLENAME = "coupon"; 
	
	/**
	 * @param tableName
	 */
	public CouponCrudDAO() {
		super(TABLENAME);
	}
	
	public CouponBean findByCpno(String cpno) throws Exception {
		if(cpno==null||cpno.equals("")) {
			return null;
		}
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM " + TABLENAME + " WHERE cpno = ? AND state_active = 1 AND start_date <= NOW() AND (end_date IS NULL OR end_date > NOW()) AND availability > 0", 
				cpno);
		
		CouponBean bean = null;
		if(lom!=null&&!lom.isEmpty()) {
			Gson gson = new Gson();
			bean = gson.fromJson(gson.toJson(lom.get(0)), new TypeToken<CouponBean>() {}.getType());
		}
		return bean;
	}
	
	public List<CouponBean> findByUsername(String username) throws Exception {
		if(username==null||username.equals("")) {
			return null;
		}
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM " + TABLENAME + " WHERE reg_username = ? AND state_active = 1 AND start_date <= NOW() AND (end_date IS NULL OR end_date > NOW()) AND availability > 0", 
				username);
		
		List<CouponBean> bean = null;
		if(lom!=null&&!lom.isEmpty()) {
			Gson gson = new Gson();
			bean = gson.fromJson(gson.toJson(lom), new TypeToken<List<CouponBean>>() {}.getType());
		}
		return bean;
	}
	
	// 아무나 쓸 수 있는 쿠폰 
	// 쿠폰 번호 형태 반환
	public String makeCoupon(String code, int availability) throws Exception {
		return makeCoupon(code, null, null, null, availability);
	}
	
	// reg_username 고객만 쓸 수 있는 쿠폰
	// 쿠폰 번호 형태 반환
	public String makeCoupon(String code, String reg_username, int availability) throws Exception {
		return makeCoupon(code, reg_username, null, null, availability);
	}
	
	// reg_username 고객만 쓸 수 있는 쿠폰
	// start ~ end 까지 사용 가능
	// 쿠폰 번호 형태 반환
	public String makeCoupon(String code, String reg_username, String start_date, String end_date, int availability) throws Exception {
		String cpno = makeCpno(code);
		
		if(start_date==null) {
			if(end_date==null) {
				sqlUpdate("INSERT INTO " + TABLENAME + " (cpno, reg_username, availability) VALUES(?,?,?)",
						cpno, reg_username, availability);
			} else {
				sqlUpdate("INSERT INTO " + TABLENAME + " (cpno, reg_username, end_date, availability) VALUES(?,?,?,?)",
						cpno, reg_username, end_date, availability);
			}
		} else {
			sqlUpdate("INSERT INTO " + TABLENAME + " (cpno, reg_username, start_date, end_date, availability) VALUES(?,?,?,?,?)",
					cpno, reg_username, start_date, end_date, availability);
		}
		return cpno;
	}
	
	public String makeCpno(String code) throws Exception {
		String cpno = "";
		Random rand = new Random();
		
		do {
			if(code==null||code.equals("")) {
				for(int i=0; i<4;i++) {
					if(rand.nextBoolean()) {
						code += String.valueOf((char) ((int) (rand.nextInt(26)) + 65));
					} else {
						code += String.valueOf(rand.nextInt(10));
					}
				}
			}
			for(int i=0; i<4;i++) {
				if(rand.nextBoolean()) {
					cpno += String.valueOf((char) ((int) (rand.nextInt(26)) + 65));
				} else {
					cpno += String.valueOf(rand.nextInt(10));
				}
			}
			cpno += "-";
			for(int i=0; i<4;i++) {
				if(rand.nextBoolean()) {
					cpno += String.valueOf((char) ((int) (rand.nextInt(26)) + 65));
				} else {
					cpno += String.valueOf(rand.nextInt(10));
				}
			}
		} while(isDuplicated(cpno));
		return code.toUpperCase() + "-" + cpno;
	}
	
	public boolean isDuplicated(String cpno) throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery("SELECT * FROM " + TABLENAME + " WHERE cpno = ?", 
				cpno);
		return lom!=null&&!lom.isEmpty();
	}
	
}
