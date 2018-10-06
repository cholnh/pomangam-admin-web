package com.mrporter.pomangam.member.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.mrporter.pomangam.common.constant.MenuType;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.common.sql.Config;
import com.mrporter.pomangam.member.vo.AuthorityBean;

/**
 * AuthorityCrudDAO
 * 
 * @version 1.0 [2018. 3. 29.]
 * @author Choi
 */
public class AuthorityCrudDAO extends Crud<AuthorityBean> {
	private static final String TABLENAME = "authority"; 
	
	/**
	 * @param tableName
	 */
	public AuthorityCrudDAO() {
		super(TABLENAME);
	}
	
	public List<String> getAuthName() {
		Connection conn = Config.getInstance().sqlLogin();
		List<String> result = null;
		List<Map<String, Object>> listOfMaps = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, "SELECT authname FROM " + TABLENAME + " group by authname;", 
						new MapListHandler());
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		if(!listOfMaps.isEmpty()) {
			result = new ArrayList<>(listOfMaps.size());
			
			for(Map<String, Object> map : listOfMaps) {
				result.add((String) map.get("authname"));
			}
		}
		return result;
	}
	
	public void insert(String authorityName, boolean authorityDefault) throws Exception {
		
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			
			String authority = authorityDefault?"11":"00";
			MenuType[] types = MenuType.values();
			for(MenuType type : types) {
				queryRunner.update(conn, "INSERT INTO " + TABLENAME + " VALUES(?,?,?,?)",
						0,
						authorityName,
						type.getDbValue(),
						authority);
			}
			
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	public void update(String authname, String auth) throws Exception {
		Connection conn = Config.getInstance().sqlLogin();
		try {
			QueryRunner queryRunner = new QueryRunner();
			String[] parts = auth.split("");
			MenuType[] types = MenuType.values();
			for(int i=0,j=0; i<types.length; i++,j=j+2) {
				MenuType type = types[i];
				queryRunner.update(conn, "UPDATE " + TABLENAME + " SET authority=? WHERE authname=? AND type=?",
						parts[j]+parts[j+1],
						authname,
						type.getDbValue());
			}
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public void delete(String type) throws Exception {
		Connection conn = Config.getInstance().sqlLogin();
		try {
			new QueryRunner().update(conn, "DELETE FROM " + TABLENAME + " WHERE authname = ?;", type);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	public String getAuthority(String authname, String type) {
		Connection conn = Config.getInstance().sqlLogin();
		String result = null;
		List<Map<String, Object>> listOfMaps = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, "SELECT authority FROM " + TABLENAME + " WHERE authname=? AND type=?;", 
						new MapListHandler(), authname, type);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		if(!listOfMaps.isEmpty()) {
			result = (String) listOfMaps.get(0).get("authority");
		}
		return result;
	}
}
