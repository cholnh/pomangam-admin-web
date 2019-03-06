package com.mrporter.pomangam.member.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.common.security.SQLInjectionDefender;
import com.mrporter.pomangam.common.security.password.PasswordEncoding;
import com.mrporter.pomangam.common.security.password.SHAPasswordEncoder;
import com.mrporter.pomangam.common.sql.Config;
import com.mrporter.pomangam.member.vo.AdminBean;

/**
 * AdminCrudDAO
 * 
 * @version 1.0 [2018. 3. 29.]
 * @author Choi
 */
public class AdminCrudDAO extends Crud<AdminBean> {
	private static final String TABLENAME = "admin"; 
	
	/**
	 * @param tableName
	 */
	public AdminCrudDAO() {
		super(TABLENAME);
	}
	
	public AdminBean getMemberWithSession(String session_key) {
		Connection conn = Config.getInstance().sqlLogin();
		AdminBean result = null;
		List<Map<String, Object>> listOfMaps = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, "SELECT * FROM " + TABLENAME + " WHERE session_key = ? AND session_limit > now();", 
						new MapListHandler(), session_key);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		if(!listOfMaps.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(listOfMaps.get(0)), 
					new TypeToken<AdminBean>() {}.getType());
		}
		return result;
	}
	public void rememberSession(String session_key, Date session_limit, String username) throws Exception {
		sqlUpdate("UPDATE " + TABLENAME + " SET session_key = ?, session_limit = ? WHERE username = ? ", session_key, session_limit, username);
	}
	
	public String getAdminList() throws Exception {
		return getAdminList("");
	}
	public String getAdminList(String tail) throws Exception {
		String s = "점주";
		System.out.println(s);
		List<Map<String, Object>> listOfMaps;
		listOfMaps = sqlQuery("SELECT * FROM " + TABLENAME + " WHERE role != \"" + s + "\" " + 
						(SQLInjectionDefender.isSQLInjection(tail) ? "" : (tail==null?"":tail)) + ";");
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	public String getOwnerList() throws Exception {
		return getOwnerList("");
	}
	public String getOwnerList(String tail) throws Exception {
		String s = "점주";
		System.out.println(s);
		List<Map<String, Object>> listOfMaps;
		listOfMaps = sqlQuery("SELECT * FROM " + TABLENAME + " WHERE role = \"" + s + "\" " + 
						(SQLInjectionDefender.isSQLInjection(tail) ? "" : (tail==null?"":tail)) + ";");
		Gson gson = new Gson();
		return gson.toJson(listOfMaps);
	}
	
	public AdminBean getMember(String username) {
		Connection conn = Config.getInstance().sqlLogin();
		AdminBean result = null;
		List<Map<String, Object>> listOfMaps = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, "SELECT * FROM " + TABLENAME + " WHERE username = ?;", 
						new MapListHandler(), username);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		if(!listOfMaps.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(listOfMaps.get(0)), 
					new TypeToken<AdminBean>() {}.getType());
		}
		return result;
	}
	
	@Override
	public Integer insert(AdminBean bean) throws Exception {
		bean.setPassword(getBcryptCipher(bean.getPassword()));
		return super.insert(bean);
	}
	
	@Override
	public void update(AdminBean bean) throws Exception {
		super.update(bean);
		
		if(bean.getPassword() != null) {
			bean.setPassword(getBcryptCipher(bean.getPassword()));
			sqlUpdate("UPDATE " + TABLENAME + " SET password=?  WHERE username = ?;",
					bean.getPassword(),
					bean.getUsername());
		}
	}
	
	private String getBcryptCipher(String plaintext) {
		SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
		shaPasswordEncoder.setEncodeHashAsBase64(true);
		PasswordEncoding pwEncoder = new PasswordEncoding(shaPasswordEncoder);
		PasswordEncoding bCryptEncoder = new PasswordEncoding(new BCryptPasswordEncoder());
		
		return bCryptEncoder.encode(pwEncoder.encode(plaintext));
	}
}
