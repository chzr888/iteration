package com.chenzr.iteration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class TestIN {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		try {
			conn = getBizConn();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 10000; i++) {
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append("'"+(i+800000)+"'");
			}
			String sql = "SELECT * FROM iterationtable2xx WHERE id in("+sb.toString()+")";
			long time = System.currentTimeMillis();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			System.out.println(System.currentTimeMillis() - time);
			while (rs.next()) {
				System.out.println(rs.getString("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static Connection getBizConn() {
		Connection conn = null;
		try {
			String url = "jdbc:mysql://192.168.2.3:3306/test";
			String username = "root";
			String password = "victop";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
