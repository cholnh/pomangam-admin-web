package com.mrporter.pomangam.target.dao;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.target.vo.TargetBean;
import com.mrporter.pomangam.target.vo.TargetDetailBean;

/**
 * LiveCrudDAO
 * 
 * @version 1.0 [2018. 8. 15.]
 * @author Choi
 */
public class TargetCrudDAO extends Crud<TargetBean> {
	private static final String TABLENAME = "target"; 
	
	/**
	 * @param tableName
	 */
	public TargetCrudDAO() {
		super(TABLENAME);
	}
	
	public String getCompactList() throws Exception {
		List<Map<String, Object>> listOfMaps 
			= sqlQuery("SELECT idx, name, location, category FROM target;"); 
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	public int getSumOrder() throws Exception {
		List<Map<String, Object>> lom = sqlQuery("SELECT sum(cnt_order) FROM target;");
		if(lom==null||lom.isEmpty()) 
			return 0;
		else
			return Integer.parseInt(lom.get(0).get("sum(cnt_order)")+"");
	}
	
	public List<TargetBean> getCompactBeanList() throws Exception {
		List<TargetBean> targetList = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT * FROM target");
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			targetList = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<TargetBean>>() {}.getType());
		}
		return targetList;
	}
	
	public List<TargetDetailBean> getAllDetailList() throws Exception {
		List<TargetDetailBean> detailList = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT d.*, t.name as target_name FROM target_delivery_detail d, target t WHERE d.idx_target = t.idx");
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			detailList = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<TargetDetailBean>>() {}.getType());
		}
		return detailList;
	}
	
	public List<TargetDetailBean> getDetailList(Integer idx_target) throws Exception {
		List<TargetDetailBean> detailList = null;
		List<Map<String, Object>> lom = sqlQuery(
				"SELECT d.*, t.name as target_name FROM target_delivery_detail d, target t WHERE d.idx_target = t.idx AND idx_target = ?", idx_target);
		if(!lom.isEmpty()) {
			Gson gson = new Gson();
			detailList = new Gson().fromJson(gson.toJson(lom), 
					new TypeToken<List<TargetDetailBean>>() {}.getType());
		}
		return detailList;
	}
	
	public void addCountOrder(Integer idx) throws Exception {
		sqlUpdate("UPDATE target SET cnt_order = cnt_order + 1 WHERE idx = ?;", idx);
	}
}
