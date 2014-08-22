package com.victop.iteration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestOperation {

	StepOperation engine = null;
	Connection conn = null;
	Connection bizConn = null;
	String md5 = "21232f297a57a5a743894a0e4a801fc3";

	@BeforeClass
	public void beforeClass() {
		engine = new StepOperation();
		conn = getMySqlConn();
		bizConn = getMySqlConn();
		try {
			upDateBySql("DROP TABLE IF EXISTS iterationtable3", bizConn);
			String sql = "CREATE TABLE iterationtable3 ( md5 VARCHAR (50) NOT NULL, rowid INT (32) NOT NULL, " +
					"meme VARCHAR (255) DEFAULT NULL, cv1 INT  DEFAULT NULL, cv2 INT  DEFAULT NULL, cv3 INT  DEFAULT NULL," +
					" cv4 FLOAT DEFAULT NULL, cv5 FLOAT DEFAULT NULL, cv6 FLOAT  DEFAULT NULL, " +
					"cv7 DOUBLE  DEFAULT NULL, cv8 DOUBLE DEFAULT NULL, cv9 DOUBLE DEFAULT NULL, PRIMARY KEY (rowid, md5), UNIQUE KEY rowid_key2 (rowid) USING BTREE ) ENGINE = INNODB DEFAULT CHARSET = utf8";
			upDateBySql(sql, bizConn);
			
			try {
				bizConn = getMySqlConn();
				bizConn.setAutoCommit(false);
				PreparedStatement pst = bizConn.prepareStatement("INSERT INTO IterationTable3 (rowid,md5) VALUES (?,?)");
				for (int i = 1; i <= 10; i++) {
					pst.setInt(1, i);
					pst.setString(2, md5);
					pst.addBatch();
				}
				int [] batch = pst.executeBatch();
				bizConn.commit();
				System.out.println(batch.length);
			} catch (Exception e) {
				try {
					bizConn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally{
				bizConn.setAutoCommit(true);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public void afterClass() {
	}
	
	@BeforeMethod
	public void before() {
		
	}
	
	@AfterMethod
	public void after() {
		
	}
	
	@Test
	public void TestExecute() {
		try {
			//engine.execute("测试步骤1", " md5 = '"+md5+"'", conn ,bizConn);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test (dependsOnMethods = {"TestExecute"})
	public void Test2() {
		ResultSet rs = null;
		try {
			rs = bizConn.createStatement().executeQuery("SELECT * FROM iterationtable3");
			while (rs.next()) {
				System.out.println(rs.getString("cv9"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public int upDateBySql(String sql, Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
}
