package com.chenzr.iteration.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.chenzr.iteration.utils.SqlUtil;


public abstract class Dialect {
	
	private static final int DATABASE_NAME_CODE_H2 = 1;
	private static final int DATABASE_NAME_CODE_MYSQL = 2;
	private static final int DATABASE_NAME_CODE_ORACLE = 3;
	private static final int DATABASE_NAME_CODE_SQLSERVER = 4;
	private static Map<String, Integer> DATABASE_NAME_CODE_MAP = new HashMap<String, Integer>();
	
	protected SqlUtil util = new SqlUtil();
	
	static{
		DATABASE_NAME_CODE_MAP.put("H2", 1);
		DATABASE_NAME_CODE_MAP.put("MYSQL", 2);
		DATABASE_NAME_CODE_MAP.put("ORACLE", 3);
		DATABASE_NAME_CODE_MAP.put("MICROSOFT SQL SERVER", 4);
	}
	
	//字段类型
	public Map<String, String> typeNames = new HashMap<String, String>();
	
	/**
	 * 分页
	 * @param query
	 * @param offset
	 * @param limit
	 * @return
	 */
	public abstract String getLimitString(String query, int offset, int limit);
	
	/**
	 * 分页
	 * @param query
	 * @param offset
	 * @param limit
	 * @return
	 */
	public abstract String getLimitString(String query, int offset, int limit,String key);
	
	
	/**
	 * 返回最大行数
	 * @param query
	 * @return
	 */
	public int getMaxRowNum(String query, Connection conn) throws Exception {
		int num = 0;
		try {
			String sql = "SELECT COUNT(*) FROM ("+query+") www";
			num = util.getIntBySql(conn, sql);
		} catch (Exception e) {
			throw e;
		}
		return num;
	}
	
	/**
	 * 共多少页
	 * @param maxRowNum 多少行
	 * @param pageSize 每一页多少行
	 * @return
	 */
	public int totalPage(int maxRowNum,int pageSize) {
		int totalSize = 0;
		totalSize = maxRowNum / pageSize;
		if(maxRowNum % pageSize == 0){
			return totalSize;
		}
		totalSize ++;
		return totalSize;
	}
	
	/**
	 * 注册字段类型
	 * @param dataType
	 * @param JdbcDataType
	 */
	public void registerColumnType(String dataType, String JdbcDataType) {
		typeNames.put( dataType, JdbcDataType );
	}
	
	public Map<String, String> getColumnType() {
		return this.typeNames;
	}

	/**
	 * 返回字符类型
	 * @param dataType
	 * @return
	 */
	public String getColumnType(String dataType) {
		return this.typeNames.get(dataType);
	}
	
	/**
	 * 返回实例
	 * @param conn
	 * @return
	 */
	public static synchronized Dialect getInstance(Connection conn) {
		if(null == conn){
			return null;
		}
		String dataBaseName="";
		try {
			dataBaseName = conn.getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int code = DATABASE_NAME_CODE_MAP.get(dataBaseName.toUpperCase());
		switch (code) {
		case DATABASE_NAME_CODE_H2:
			return new H2Dialect();
		case DATABASE_NAME_CODE_MYSQL:
			return new MySQLDialect();
		case DATABASE_NAME_CODE_ORACLE:
			return new OracleDialect();
		case DATABASE_NAME_CODE_SQLSERVER:
			return new SQLServerDialect();
		default:
			break;
		}
		return null;
	}

}
