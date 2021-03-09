package com.mrporter.pomangam.common.sql;

import java.sql.Connection;
import java.sql.DriverManager;

public class Config {
	private static class Singleton {
		private static final Config instance = new Config();
	}
	
	public static Config getInstance() {
		return Singleton.instance;
	}
	// DB 커넥션 Variable
	private Connection conn = null;

	// DB 정보
	private final String tool = "jdbc:mysql://";
	private final String domain = "localhost";
	private final String id = "";
	private final String port = "3306";
	private final String pw = "";
	private final String dbname = "";

	
	private String url = tool + domain + ":" + port + "/" + dbname
			+ "?autoReconnect=true&useSSL=false&validationQuery=select 1";

	public Connection sqlLogin() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("DB 연결 완료");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("DB 연결 실패");
		}
		return conn;
	}
}
