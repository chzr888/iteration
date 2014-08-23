package com.chenzr.iteration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

public class H2Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int fieldSize = 250;
		int pageSize = 10000;
		int rowNum = pageSize * 100;
		Connection conn = null;
		Statement stmt = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getH2Connection();
			StringBuffer sb = new StringBuffer();
			sb.append("CREATE TABLE mytest ");
			sb.append("(");
			sb.append("id VARCHAR (36) NOT NULL,");
			for (int i = 0; i < fieldSize; i++) {
				sb.append("cv");
				sb.append(i);
				sb.append(" decimal");
				sb.append(",");
			}
			sb.append(" PRIMARY KEY (id))");
			stmt = conn.createStatement();
			stmt.executeUpdate(sb.toString());
			System.out.println(System.currentTimeMillis() - startTime);
			
			startTime = System.currentTimeMillis();
			conn.setAutoCommit(false);
			PreparedStatement pst = conn.prepareStatement("INSERT INTO mytest ( id ) VALUES ( ? )");
			for (int i = 0; i < rowNum; i++) {
				pst.setString(1, UUID.randomUUID().toString());
				pst.addBatch();
				if(i % pageSize == 0 ){
					pst.executeBatch();
					System.out.println("插入时间："+(System.currentTimeMillis() - startTime));
					startTime = System.currentTimeMillis();
				}
			}
			pst.executeBatch();
			conn.commit();
			System.out.println(System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();
			
			conn.setAutoCommit(false);
			Statement stmtStatement = conn.createStatement();
			for (int i = 0; i <fieldSize; i++) {
				stmtStatement.executeUpdate("UPDATE mytest SET cv"+i+"= RAND()*100000/100 WHERE id=id");
				System.out.println("更新时间："+(System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
				conn.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection getH2Connection()  {
		Connection memConnection = null;
		try {
			//一天
			int timeout = 24 * 60 * 60 * 1000;
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:mem:VICTOPTEST"+System.currentTimeMillis()+";" +
					"MVCC=TRUE;" +
					"LOCK_TIMEOUT="+timeout+";" +
					"MAX_OPERATION_MEMORY=0";
			String username = "sa";
			String password = "sa";
			Connection conn = DriverManager.getConnection(url, username,
					password);
			memConnection = conn;
			Statement stmt = conn.createStatement();
			stmt.execute("SET DEFAULT_TABLE_TYPE MEMORY");
			stmt.execute("SET IGNORECASE TRUE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memConnection;
	}
}
