package com.mrporter.pomangam.common.map.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.map.vo.MapBean;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.common.sql.Config;

/**
 * MapCrudDAO
 * 
 * @version 1.0 [2018. 3. 29.]
 * @author Choi
 */
public class MapCrudDAO extends Crud<MapBean> {
	private static final String TABLENAME = "common_map"; 
	
	/**
	 * @param tableName
	 */
	public MapCrudDAO() {
		super(TABLENAME);
	}
	
	public String getValue(String key) {
		Connection conn = Config.getInstance().sqlLogin();
		MapBean result = null;
		List<Map<String, Object>> listOfMaps = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			listOfMaps = queryRunner.query(conn, "SELECT * FROM " + TABLENAME + " WHERE " + TABLENAME + ".key = ?;", 
						new MapListHandler(), key);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		if(!listOfMaps.isEmpty()) {
			Gson gson = new Gson();
			result = new Gson().fromJson(gson.toJson(listOfMaps.get(0)), 
					new TypeToken<MapBean>() {}.getType());
		}
		return result.getValue();
	}
}
