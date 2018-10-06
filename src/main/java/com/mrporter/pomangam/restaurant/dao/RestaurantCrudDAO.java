package com.mrporter.pomangam.restaurant.dao;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.restaurant.vo.RestaurantBean;

/**
 * RestaurantCrudDAO
 * 
 * @version 1.0 [2018. 8. 15.]
 * @author Choi
 */
public class RestaurantCrudDAO extends Crud<RestaurantBean> {
	private static final String TABLENAME = "restaurant"; 
	
	/**
	 * @param tableName
	 */
	public RestaurantCrudDAO() {
		super(TABLENAME);
	}
	
	public String getCompactList() throws Exception {
		List<Map<String, Object>> listOfMaps 
			= sqlQuery("SELECT idx, name, location, tel, start, end FROM restaurant;"); 
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	public List<RestaurantBean> getBeanList() throws Exception {
		List<RestaurantBean> result = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT * FROM " + TABLENAME);
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<RestaurantBean>>() {}.getType());
		}
		return result;
	}
	
	public List<RestaurantBean> getBeanWithLimitCount(Integer idx) throws Exception {
		List<RestaurantBean> result = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT " +
					"* " +
				"FROM " + 
					"restaurant " +
				"WHERE " + 
					"idx_target = ?", idx);
		/*
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT " +
					"r.idx, r.idx_target, r.name, r.location, r.tel, r.description, r.imgpath, r.cnt_star, r.cnt_comment, sum(p.cnt_limit) as sum_limit " +
				"FROM " + 
					"restaurant r, product p " +
				"WHERE " + 
					"r.idx=p.idx_restaurant AND r.idx_target = ? " +
				"GROUP BY " + 
					"r.idx;"
				, idx);
		*/
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<RestaurantBean>>() {}.getType());
		}
		return result;
	}
	
}
