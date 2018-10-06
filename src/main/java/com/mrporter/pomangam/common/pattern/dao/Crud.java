package com.mrporter.pomangam.common.pattern.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.security.SQLInjectionDefender;
import com.mrporter.pomangam.common.sql.Config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Crud
 * 
 * @version 1.0 [2018. 3. 28.]
 * @author Choi
 */
@Data @AllArgsConstructor
public class Crud<T> {
	
	/**
	 * 사전에 정의 된 table name
	 */
	private String tableName;
	
	/**
	 * 사전에 정의 된 table index의 column name
	 */
	private static final String IDX = "idx"; 
	
	/**
	 * 모든 컬럼 반환
	 * 
	 * @return db내의 모든 컬럼을 bean으로 변환 후 json형태로 반환
	 */
	public String getList() throws Exception {
		return getList("");
	}
	public String getList(String tail) throws Exception {
		List<Map<String, Object>> listOfMaps;
		listOfMaps = sqlQuery("SELECT * FROM " + tableName + " " + 
						(SQLInjectionDefender.isSQLInjection(tail) ? "" : (tail==null?"":tail)) + ";");
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	/**
	 * 모든 컬럼을 list 형태로 반환
	 * 
	 * @return db내의 모든 컬럼을 bean으로 변환 후 list형태로 반환
	 */
	public List<T> getBeanList() throws Exception {
		return new Gson().fromJson(getList(), 
				new TypeToken<List<T>>() {}.getType());
	}
	public List<T> getBeanList(String tail) throws Exception {
		return new Gson().fromJson(getList(tail), 
				new TypeToken<List<T>>() {}.getType());
	}
	
	/**
	 * idx에 해당하는 컬럼 반환 
	 * 
	 * @param idx 반환 할 컬럼의 인덱스
	 * @return idx에 해당하는 컬럼이 검색되었다면 해당 컬럼을 bean형태로 반환, 그렇지 않으면 null반환
	 */
	public String getBean(Integer idx) throws Exception {
		return getBean(idx, "");
	}
	public String getBean(Integer idx, String tail) throws Exception {
		String result = "[]";
		List<Map<String, Object>> listOfMaps;
		listOfMaps = sqlQuery("SELECT * FROM " + tableName + " WHERE " + IDX + " = ? " + 
						(SQLInjectionDefender.isSQLInjection(tail) ? "" : (tail==null?"":tail)) + ";", idx);
		
		if(!listOfMaps.isEmpty()) {
			result = new Gson().toJson(listOfMaps);
		}
		return result;
	}
	public String getBean(String column, String value) throws Exception {
		String result = "[]";
		List<Map<String, Object>> listOfMaps;
		listOfMaps = sqlQuery("SELECT * FROM " + tableName + " WHERE " + 
				(SQLInjectionDefender.isSQLInjection(column)?"":column) + " = ?;", value);
		
		if(!listOfMaps.isEmpty()) {
			result = new Gson().toJson(listOfMaps);
		}
		return result;
	}
	
	/**
	 * 삽입 연산
	 * 
	 * @param bean 삽입 할 컬럼의 내용
	 */
	public Integer insert(T bean) throws Exception {
		Integer idx;
		Field[] fields = bean.getClass().getDeclaredFields();
		Object[] params = new Object[fields.length];
		StringBuilder ps = new StringBuilder();
		List<Map<String, Object>> listOfMaps;
		
		for(int i=0; i<fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			params[i] = field.get(bean);
			ps.append("?");
			if(i < fields.length-1) {
				ps.append(",");
			}
		}
		
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			
			queryRunner.update(conn, "INSERT INTO " + tableName + " VALUES(" + ps + ")", params);
			
			listOfMaps = queryRunner.query(conn, "SELECT LAST_INSERT_ID();", new MapListHandler());
		} finally {
			DbUtils.closeQuietly(conn);
		}
		idx = Integer.parseInt(listOfMaps.get(0).get("LAST_INSERT_ID()")+"");

		return idx;
	}
	
	/**
	 * 수정 연산
	 * 
	 * 값이 null일 경우 수정되지 않음.
	 * 
	 * @param bean 수정 할 컬럼의 내용 (idx에는 수정 할 인덱스가 포함)
	 */
	public void update(T bean) throws Exception {
		Field[] fields = bean.getClass().getDeclaredFields();
		List<Object> arr = new ArrayList<Object>();
		StringBuilder ps = new StringBuilder();
		
		for(int i=0; i<fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			if(field.getName().equals(IDX) || field.get(bean) == null) {
				continue;
			}
			arr.add(field.get(bean));
			
			ps.append(tableName+"."+field.getName()+"=? ");
			ps.append(",");
		}
		ps.deleteCharAt(ps.length()-1);
		arr.add(fields[0].get(bean));
		
		sqlUpdate("UPDATE " + tableName + " SET " + ps + "WHERE " + IDX + " = ?;",  arr.toArray());
	}
	
	/**
	 * 삭제 연산
	 * 
	 * @param idx 삭제할 컬럼의 idx
	 */
	public void delete(Integer idx) throws Exception {
		sqlUpdate("DELETE FROM " + tableName + " WHERE " + IDX + " = ?;", idx);
	}
	public void delete(Integer[] idxes) throws Exception {
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			for(Integer idx : idxes) {
				queryRunner.update(conn, "DELETE FROM " + tableName + " WHERE " + IDX + " = ?;", idx);
			}
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	/**
	 * data structure fields 반환
	 * 
	 * @param cls data structure가 정의된 Class (Bean)
	 * @return fields를 json형태로 반환
	 */
	public String getFields(@SuppressWarnings("rawtypes") Class cls) {
		Field[] fields = cls.getDeclaredFields();
		String[] names = new String[fields.length];
		
		for(int i=0; i<fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			names[i] = field.getName();
		}
		return new Gson().toJson(names);
	}
	
	/**
	 * 중복 검사
	 * 
	 * @param where SQL WHERE 필드 이름
	 * @param value WHERE절에 들어갈 값
	 * @return 중복되면 TRUE, 중복되지 않으면 FALSE 반환
	 */
	public boolean idDuplicated(String where, String value) throws Exception {
		if(where == null) {
			throw new RuntimeException("idDuplicated: WHERE argument cannot be null");
		} else if(SQLInjectionDefender.isSQLInjection(where)) {
			throw new RuntimeException("idDuplicated: sql injection");
		}
		
		List<Map<String, Object>> listOfMaps = 
				sqlQuery("SELECT idx FROM "+tableName+" WHERE "+where+"=?", value);
		
		return !listOfMaps.isEmpty();
	}
	

	public List<Map<String, Object>> sqlQuery(String sql, Object...obj) throws Exception {
		List<Map<String, Object>> listOfMaps = null;
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, sql, new MapListHandler(), obj);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return listOfMaps;
	}
	/*
	public List<Map<String, Object>> sqlQuery(String sql) throws Exception {
		return sqlQuery(sql);
	}
	public List<Map<String, Object>> sqlQuery(String sql, Object[] obj) throws Exception {
		List<Map<String, Object>> listOfMaps = null;
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, sql, new MapListHandler(), obj);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return listOfMaps;
	}
	public List<Map<String, Object>> sqlQuery(String sql, Object obj) throws Exception {
		List<Map<String, Object>> listOfMaps = null;
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			if(obj == null) {
				listOfMaps = queryRunner.query(conn, sql, new MapListHandler());
			} else {
				listOfMaps = queryRunner.query(conn, sql, new MapListHandler(), obj);
			}
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return listOfMaps;
	}
	*/
	
	public void sqlUpdate(String sql, Object...obj) throws Exception {
		Connection conn = Config.getInstance().sqlLogin();
		try {
			new QueryRunner().update(conn, sql, obj);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	/*
	public void sqlUpdate(String sql) throws Exception {
		sqlUpdate(sql, null);
	}
	public void sqlUpdate(String sql, Object[] obj) throws Exception {
		Connection conn = Config.getInstance().sqlLogin();
		try {
			new QueryRunner().update(conn, sql, obj);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	public void sqlUpdate(String sql, Object obj) throws Exception {
		Connection conn = Config.getInstance().sqlLogin();
		try {
			if(obj == null) {
				new QueryRunner().update(conn, sql);
			} else {
				new QueryRunner().update(conn, sql, obj);
			}
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	*/
}
