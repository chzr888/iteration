package com.chenzr.iteration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.chenzr.iteration.bean.TableField;

public class TestLaunchOperation {
	
	private LaunchOperation launchOperation = null;
	private Connection conn = null;
	private Connection bizConn = null;
	private Connection memConn = null;
	private String stepName = "测试步骤";
	private String tableName = "";
	private	Statement stamtConn = null;
	private Statement stamtBizConn = null;
	private Statement stamtMemConn = null;
	
	private	int chunkSize = 1000;
	private int dataSize = (int) (1000000 * Math.random());
	
	@BeforeClass
	public void beforeClass() {
		try {
			launchOperation = new LaunchOperation();
			memConn = getH2Connection();
			bizConn = getBizConn();
			conn = getMySqlConn();
			
			stamtConn = conn.createStatement();
			stamtBizConn = bizConn.createStatement();
			stamtMemConn = memConn.createStatement();
			
//			//初始化
//			init();
//			
//			//导入数据
//			initData();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//初始化配置
	@Test
	private void init(){
		try {
			List<String> list = new ArrayList<String>();
			list.add("DROP TABLE IF EXISTS gsprojectstepdefined");
			list.add("DROP TABLE IF EXISTS gsprojectstepdatastore");
			list.add("DROP TABLE IF EXISTS gsprojectstepcolumnfield");
			list.add("DROP TABLE IF EXISTS gmstepoperation");
			//建表
			String sql = "CREATE TABLE gsprojectstepdefined ( jndiname VARCHAR (50) DEFAULT NULL, sqltablename VARCHAR (50) DEFAULT NULL, memo VARCHAR (500) DEFAULT NULL, datasettype VARCHAR (50) DEFAULT NULL, datarevolve INT (10) DEFAULT NULL, stamp datetime DEFAULT NULL, stepname VARCHAR (50) NOT NULL, actived INT (10) DEFAULT NULL, ispublic INT (10) DEFAULT NULL, classpath VARCHAR (4000) DEFAULT NULL, preparedataclass VARCHAR (4000) DEFAULT NULL, stampstateclass VARCHAR (4000) DEFAULT NULL, prerequisite VARCHAR (4000) DEFAULT NULL, PRIMARY KEY (stepname))";
			list.add(sql);
			String sql2 = "CREATE TABLE gsprojectstepdatastore ( fieldname VARCHAR (50) NOT NULL, fieldcaption VARCHAR (50) DEFAULT NULL, datatype VARCHAR (50) DEFAULT NULL, mastername VARCHAR (50) DEFAULT NULL, propname VARCHAR (50) DEFAULT NULL, stamp datetime DEFAULT NULL, profilefield VARCHAR (50) DEFAULT NULL, stepname VARCHAR (50) NOT NULL, tablename VARCHAR (50) DEFAULT NULL, NO INT (10) DEFAULT NULL, iskey INT (10) DEFAULT NULL, dateformat VARCHAR (50) DEFAULT NULL, notsave INT (10) DEFAULT NULL, datarevolvefield VARCHAR (50) DEFAULT NULL, PRIMARY KEY (fieldname, stepname))";
			list.add(sql2);
			String sql3 = "CREATE TABLE gsprojectstepcolumnfield ( columnfield VARCHAR (50) DEFAULT NULL, columntype VARCHAR (50) DEFAULT NULL, decimalsize INT (10) DEFAULT NULL, stepname VARCHAR (50) NOT NULL, columnid VARCHAR (50) NOT NULL, stamp datetime DEFAULT NULL, isencryption VARCHAR (50) DEFAULT NULL, PRIMARY KEY (stepname, columnid))";
			list.add(sql3);
			String sql4 = "CREATE TABLE gmstepoperation ( stepname VARCHAR (50) NOT NULL, NO INT (11) NOT NULL, columnId VARCHAR (50) DEFAULT NULL, columnName VARCHAR (50) DEFAULT NULL, type VARCHAR (50) DEFAULT NULL, formula VARCHAR (500) DEFAULT NULL, operateMode VARCHAR (50) DEFAULT NULL, updateCriteria VARCHAR (500) DEFAULT NULL, PRIMARY KEY (stepname, NO))";
			list.add(sql4);
			for (int i = 0; i < list.size(); i++) {
				stamtConn.executeUpdate(list.get(i));
			}
			//插入数据
			stamtConn.executeUpdate("INSERT INTO gsprojectstepdefined ( sqltablename, stepname, actived, ispublic ) VALUES ( 'iterationtable2xx', '"+stepName+"', '1', '0' )");
			
			stamtConn.executeUpdate("INSERT INTO gsprojectstepdatastore ( fieldname, fieldcaption, datatype, stepname, NO, iskey ) VALUES ( 'id', 'ID', '字符', '"+stepName+"', 0, 1 )");
			int k = 0;

			//整型
			for (int i = 0; i < 100; i++) {
				k++;
				String fieldname = "cv"+k;
				String fieldcaption = "字段"+k;
				stamtConn.executeUpdate("" +
						"INSERT INTO gsprojectstepdatastore " +
						"( fieldname, fieldcaption, datatype, stepname, NO, iskey ) VALUES " +
						"( '"+fieldname+"', '"+fieldcaption+"', '整型', '"+stepName+"', "+k+", 0 )");
			}
			
			//金额
			for (int i = 0; i <100; i++) {
				k++;
				String fieldname = "cv"+k;
				String fieldcaption = "字段"+k;
				stamtConn.executeUpdate("" +
						"INSERT INTO gsprojectstepdatastore " +
						"( fieldname, fieldcaption, datatype, stepname, NO, iskey ) VALUES " +
						"( '"+fieldname+"', '"+fieldcaption+"', '金额', '"+stepName+"', "+k+", 0 )");
			}
			
			//字符
			for (int i = 0; i < 54; i++) {
				k++;
				String fieldname = "cv"+k;
				String fieldcaption = "字段"+k;
				stamtConn.executeUpdate("" +
						"INSERT INTO gsprojectstepdatastore " +
						"( fieldname, fieldcaption, datatype, stepname, NO, iskey ) VALUES " +
						"( '"+fieldname+"', '"+fieldcaption+"', '字符', '"+stepName+"', "+k+", 0 )");
			}
			
			stamtConn.executeUpdate("INSERT INTO gsprojectstepcolumnfield ( stepname, columnid, columnfield, columntype, decimalsize )" +
					" SELECT stepname, fieldcaption, fieldname, datatype, 3 AS decimalsize FROM gsprojectstepdatastore WHERE stepname = '"+stepName+"' AND datatype IN ('整型', '金额')");
			
			stamtConn.executeUpdate("INSERT INTO gmstepoperation ( stepname, NO, columnId, columnName, type, operateMode, updateCriteria, formula )" +
					" SELECT stepname, NO, fieldname, fieldcaption, '栏目运算' AS type, '行运算' AS operateMode, 'ID==ID' AS updateCriteria, 'RANDOM(1000) * RANDOM(1000)' AS formula FROM gsprojectstepdatastore WHERE stepname = '"+stepName+"' AND datatype IN ('整型')");

			stamtConn.executeUpdate("INSERT INTO gmstepoperation ( stepname, NO, columnId, columnName, type, operateMode, updateCriteria, formula )" +
					" SELECT stepname, NO, fieldname, fieldcaption, '栏目运算' AS type, '行运算' AS operateMode, 'ID==ID' AS updateCriteria, 'RANDOM(10000) * RANDOM(1000)' AS formula FROM gsprojectstepdatastore WHERE stepname = '"+stepName+"' AND datatype IN ('金额')");
		
			stamtConn.executeUpdate("INSERT INTO gmstepoperation ( stepname, NO, columnId, columnName, type, operateMode, updateCriteria, formula )" +
					" SELECT stepname, NO, fieldname, fieldcaption, '栏目运算' AS type, '行运算' AS operateMode, 'ID==ID' AS updateCriteria, 'UUID()' AS formula FROM gsprojectstepdatastore WHERE stepname = '"+stepName+"' AND fieldname <> 'id' AND datatype IN ('字符')");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

	}
	
	//初始化表结构
	@Test (dependsOnMethods = {"init"})
	private void initData(){
		try {
			System.out.println(dataSize);
			List<TableField> fieldsList = launchOperation.getTableFields(stepName, conn);
			tableName = launchOperation.getTableName(stepName, conn);
			StepOperation operation = new StepOperation();
			operation.createMemoryTable(tableName, fieldsList, bizConn);
			
			String sqlStr = "INSERT INTO "+tableName+" (id) VALUES (?)";
			bizConn.setAutoCommit(false);
			PreparedStatement pst = bizConn.prepareStatement(sqlStr);
			int step = 0;
			for (int i = 0; i < dataSize; i++) {
				step ++;
				pst.setString(1, UUID.randomUUID().toString());
				pst.addBatch();
				if(step/chunkSize == 1){
					pst.executeBatch();
					step = 0;
				}
			}
			pst.executeBatch();
			pst.close();
			bizConn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	@Test (dependsOnMethods = {"initData"})
	public void launch(){
		try {
			String msg = launchOperation.launch(stepName, "", conn, bizConn, memConn, false);
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	private Connection getH2Connection()  {
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
	
	private Connection getH2TConnection()  {
		Connection memConnection = null;
		try {
			//一天
			int timeout = 24 * 60 * 60 * 1000;
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:d:/temp/ttt"+System.currentTimeMillis()+";" +
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
	
	private Connection getBizConn() {
		Connection conn = null;
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			String username = "root";
			String password = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	private Connection getMySqlConn() {
		Connection conn = null;
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			String username = "root";
			String password = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		try {
			TestLaunchOperation test = new TestLaunchOperation();
			test.beforeClass();
			test.init();
			test.initData();
			long stime = System.currentTimeMillis();
			test.launch();
			System.out.println("运算耗时 :"+(System.currentTimeMillis() - stime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
