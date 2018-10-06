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
import com.mrporter.pomangam.common.security.password.PasswordEncoding;
import com.mrporter.pomangam.common.security.password.SHAPasswordEncoder;
import com.mrporter.pomangam.common.sql.Config;
import com.mrporter.pomangam.member.vo.MemberBean;

/**
 * MemberCrudDAO
 * 
 * @version 1.0 [2018. 9. 3.]
 * @author Choi
 */
public class MemberCrudDAO extends Crud<MemberBean> {
	private static final String TABLENAME = "member"; 
	
	/**
	 * @param tableName
	 */
	public MemberCrudDAO() {
		super(TABLENAME);
	}
	
	public MemberBean getMember(String username) {
		Connection conn = Config.getInstance().sqlLogin();
		MemberBean result = null;
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
					new TypeToken<MemberBean>() {}.getType());
		}
		return result;
	}
	
	@Override
	public Integer insert(MemberBean bean) throws Exception {
		bean.setPassword(getBcryptCipher(bean.getPassword()));
		return super.insert(bean);
	}
	
	@Override
	public void update(MemberBean bean) throws Exception {
		super.update(bean);
		
		if(bean.getPassword() != null) {
			bean.setPassword(getBcryptCipher(bean.getPassword()));
			sqlUpdate("UPDATE " + TABLENAME + " SET password=?  WHERE username = ?;",
					bean.getPassword(),
					bean.getUsername());
		}
	}
	
	public void setTarget(String username, Integer idx_target) throws Exception {
		sqlUpdate("UPDATE member SET idx_target = ? WHERE username = ?", idx_target, username);
	}
	
	private String getBcryptCipher(String plaintext) {
		SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
		shaPasswordEncoder.setEncodeHashAsBase64(true);
		PasswordEncoding pwEncoder = new PasswordEncoding(shaPasswordEncoder);
		PasswordEncoding bCryptEncoder = new PasswordEncoding(new BCryptPasswordEncoder());
		
		return bCryptEncoder.encode(pwEncoder.encode(plaintext));
	}
	
	public void rememberSession(String session_key, Date session_limit, String username) throws Exception {
		sqlUpdate("UPDATE member SET session_key = ?, session_limit = ? WHERE username = ? ", session_key, session_limit, username);
	}
	
	public MemberBean getMemberWithSession(String session_key) {
		Connection conn = Config.getInstance().sqlLogin();
		MemberBean result = null;
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
					new TypeToken<MemberBean>() {}.getType());
		}
		return result;
	}
}
