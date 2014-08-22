package com.chenzr.iteration.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.CachedRowSetImpl;

@SuppressWarnings("restriction")
public class SqlUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SqlUtil.class);

	public ResultSet getResultSetBySql(Connection conn, String sql)
			throws SQLException {
		logger.debug(sql);
		return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE).executeQuery(sql);
	}
	
	public int getIntBySql(Connection conn, String sql)
			throws Exception {
		int k = 0;
		ResultSet rs = null;
		try {
			rs = getResultSetBySql(conn, sql);
			if(rs.next()){
				k = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			close(rs);
		}
		return k;
	}
	
	public int upDateBySql(String sql,Connection conn) throws Exception {
		Statement stmt = null;
		try {
			logger.debug(sql);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		}finally{
			close(stmt);
		}
	}

	public void close(ResultSet rs) {
		if (null != rs) {
			try {
				if (!rs.isClosed()) {
					Statement stmt = rs.getStatement();
					rs.close();
					rs = null;
					close(stmt);
				}
			} catch (Exception e) {
			}
		}
	}

	public void close(Statement stmt) {
		if (null != stmt) {
			try {
				if (!stmt.isClosed()) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public Connection getMemoryConnection() throws Exception {
		Connection conn = null;
		try {
			//一天
			int timeout = 24 * 60 * 60 * 1000;
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:mem:VICTOPTEST;" +
					"MVCC=TRUE;" +
					"LOCK_TIMEOUT="+timeout+";" +
					"MAX_OPERATION_MEMORY=0";
			String username = "sa";
			String password = "sa";
			conn = DriverManager.getConnection(url, username,
					password);
			Statement stmt = conn.createStatement();
			stmt.execute("SET DEFAULT_TABLE_TYPE MEMORY");
			stmt.execute("SET IGNORECASE TRUE");
			
		} catch (Exception e) {
			throw e;
		}
		return conn;
	}
	
	/**
	 * 删除表
	 * @param tableName
	 * @param memConn
	 */
	public void dropMemoryTable(String tableName,Connection memConn){
		try {
			if(null != tableName || !tableName.isEmpty()){
				upDateBySql("DROP TABLE IF EXISTS "+tableName, memConn);
			}
		} catch (Exception e) {
			logger.error("{}",e);
		}
	}

	public void showTable(String tableName, Connection conn)
			throws SQLException {
		showResult("SELECT * FROM " + tableName + "", conn);
	}

	public void showTable(String[] tableNames, Connection conn)
			throws SQLException {
		for (int i = 0; i < tableNames.length; i++) {
			showTable(tableNames[i], conn);
		}
	}

	public void showResult(String sql, Connection conn)
			throws SQLException {
		logger.debug(sql);
		logger.debug("Connection URL : " + conn.getMetaData().getURL());
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		showResult(rs);
		stmt.close();
	}

	public void showResult(ResultSet rs) throws SQLException {
		CachedRowSet crs = new CachedRowSetImpl();
		crs.populate(rs);
		ResultSetMetaData rm = crs.getMetaData();
		// int PrintLength = rm.getColumnCount();
		int[] column = new int[rm.getColumnCount()];
		for (int i = 0; i < rm.getColumnCount(); i++) {
			int tmp = checkPrintLength(rm.getColumnLabel(i + 1));
			column[i] = tmp;
		}
		while (crs.next()) {
			for (int i = 0; i < rm.getColumnCount(); i++) {
				int tmp = checkPrintLength(crs.getString(i + 1));
				if (tmp > column[i]) {
					column[i] = tmp;
				}
			}
		}
		System.out.println();
		for (int i = 0; i < column.length; i++) {
			if (i == 0) {
				System.out.print("┌");
			}
			int col = column[i];
			for (int j = 0; j < col; j++) {
				System.out.print("–");
			}
			if (i == column.length - 1) {
				System.out.print("┐");
			} else {
				System.out.print("┬");
			}
		}
		System.out.println();
		System.out.print("│");
		for (int i = 0; i < column.length; i++) {
			String colString = rm.getColumnLabel(i + 1);
			int t = column[i] - checkPrintLength(colString);
			System.out.print(colString);
			for (int j = 0; j < t; j++) {
				System.out.print(" ");
			}
			System.out.print("│");
		}
		System.out.println();
		for (int i = 0; i < column.length; i++) {
			if (i == 0) {
				System.out.print("├");
			}
			int col = column[i];
			for (int j = 0; j < col; j++) {
				System.out.print("–");
			}
			if (i == column.length - 1) {
				System.out.print("┤");
			} else {
				System.out.print("┼");
			}
		}
		System.out.println();
		crs.beforeFirst();
		while (crs.next()) {
			System.out.print("│");
			for (int i = 0; i < column.length; i++) {
				String colString = crs.getString(i + 1);
				int t = column[i] - checkPrintLength(colString);
				System.out.print(colString);
				for (int j = 0; j < t; j++) {
					System.out.print(" ");
				}
				System.out.print("│");
			}
			System.out.println();
		}
		for (int i = 0; i < column.length; i++) {
			if (i == 0) {
				System.out.print("└");
			}
			int col = column[i];
			for (int j = 0; j < col; j++) {
				System.out.print("–");
			}
			if (i == column.length - 1) {
				System.out.print("┘");
			} else {
				System.out.print("┴");
			}
		}
		System.out.println();
		crs.close();
	}

	private int checkPrintLength(String str) {
		int n = 0;
		if (null == str) {
			return 4;
		}
		if ("".equals(str)) {
			return 0;
		}
		int length = str.length();
		byte[] b;
		for (int i = 0; i < length; i++) {
			b = str.substring(i).getBytes();
			if ((b[0] & 0xff) > 128) {
				n += 2;
			} else {
				n++;
			}
		}
		return n;
	}
}
