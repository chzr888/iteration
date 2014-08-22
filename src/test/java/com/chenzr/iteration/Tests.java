package com.chenzr.iteration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;


public class Tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			BigDecimal bd = new BigDecimal(0);
//			bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
//			BigDecimal bd2 = new BigDecimal("1.1199199");
//			BigDecimal bd3 = bd.add(bd2);
//			System.out.println(bd3.doubleValue());
////			FelEngineImpl engine = new FelEngineImpl();
////			Object eval = engine.eval("1.09999");
////			if(eval instanceof Integer){
////				System.out.println("Integer");
////			}
////			if(eval instanceof Double){
////				System.out.println("Double");
////			}
////			System.out.println(eval);
////			System.out.println(1.19211+2.0123);
//			FelEngineImpl engine = new FelEngineImpl();
//			FelContext ctx = engine.getContext();
//			ctx.set("数量", 5);
//			Object object = engine.eval("数量^2/10", ctx);
//			System.out.println(object);
//			String string = null;
//			if(null == string || string.isEmpty()){
//				System.out.println("null");
//			}
//			
//			String sql = "CREATE TABLE ttttt (id int,d decimal)";
//
//			Connection connection = getH2Connection();
//			connection.createStatement().executeUpdate(sql);
//			sql = "INSERT INTO ttttt (id,d) VALUES (2.1,2.1199991)";
//			connection.createStatement().executeUpdate(sql);
//			sql = "select * from ttttt";
//			ResultSet rSet = connection.createStatement().executeQuery(sql);
//			while (rSet.next()) {
//				System.out.println(rSet.getString(1));
//				System.out.println(rSet.getString(2));
//				
//			}
//			Project p = new Project();
//			
//			Map m = new HashMap<String, String>();
//			m.put("a1", "a1");
//			m.put("a2", "a2");
//			m.put("a3", "a3");
//			m.put("a4", "a4");
//			
//			Map m1 = new HashMap<String, String>();
//			String key = "a1";
//			m1.put(key, m.get(key));
//			
//			System.out.println(m.size());
//			System.out.println(m1.size());
			
			long time = System.currentTimeMillis();

//			FelEngineImpl engine = new FelEngineImpl();
			time = System.currentTimeMillis();
//			FelContext ctx = new ArrayCtxImpl();
//			ctx.set("数量", 5);
//			ctx.set("数量1", 2);
			System.out.println(System.currentTimeMillis() - time);
			time = System.currentTimeMillis();

//			Object object = engine.eval("$('Math').random() * 10000", ctx);
//			object = engine.eval("数量  == 数量1", ctx);
//			System.out.println(object);
			System.out.println(System.currentTimeMillis() - time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Connection getH2Connection() throws Exception {
		Connection memConnection = null;
		try {
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:mem:VICTOPTEST;MVCC=TRUE";
			String username = "sa";
			String password = "sa";
			Connection conn = DriverManager.getConnection(url, username,
					password);
			memConnection = conn;
		} catch (Exception e) {
			throw e;
		}
		return memConnection;
	}

}
