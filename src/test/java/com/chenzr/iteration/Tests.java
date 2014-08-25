package com.chenzr.iteration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import com.chenzr.iteration.impl.IterationContextImpl;
import com.chenzr.iteration.impl.IterationEngineImpl;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import com.greenpineyu.fel.function.Function;

public class Tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String [] a = new String[100];
			// System.out.println(a.length);
			//
			// long time = System.currentTimeMillis();
			// FelEngine engine = new FelEngineImpl();
			// time = System.currentTimeMillis();
			// FelContext ctx = engine.getContext();
			// Function fun = new MyFunction();
			// //添加函数到引擎中。
			// engine.addFun(fun);
			// for (int i = 0; i < 300; i++) {
			// ctx.set("数量"+i, i);
			// }
			// System.out.println(System.currentTimeMillis() - time);
			// time = System.currentTimeMillis();
			// int count = 10000 * 2;
			// for (int i = 0; i < count; i++) {
			// for (int j = 0;j < 300; j++) {
			// ctx.set("数量"+j,j);
			// }
			// //System.out.println(ctx.length());
			// int k = 1;
			// k++;
			// engine.eval("数量1 + 数量2 +数量3", ctx);
			// }
			// //time = System.currentTimeMillis();
			// engine.eval("COUNT('员工')", ctx);
			// Object object = engine.eval("$('Math').random() * 10000", ctx);
			// object = engine.eval("数量  == 数量1", ctx);
			// System.out.println(object);
			// System.out.println(System.currentTimeMillis() - time);
			//
			// double a = 0.1;
			// double b = 0.19;
			//
			// BigDecimal bd1 = new BigDecimal(a);
			// BigDecimal bd2 = new BigDecimal(b);
			// BigDecimal bd3 = bd1.add(bd2);
			// System.out.println(bd3);
			//
			// System.out.println(a+b);
			// double c = a * 1000 + b * 1000;
			// System.out.println(c/1000);
			// int num = 10000 * 100;
			// FelEngine engine = FelEngine.instance;
			// for (int i = 0; i < num; i++) {
			// Object eval = engine.eval("0.1+0.19");
			// //System.out.println(eval);
			// double cc = (0.1 + 0.19);
			// double dd = NumberUtil.toDouble(eval) ;
			// if (dd != 0.29) {
			// throw new Exception("不稳定");
			// }
			// }
			// System.out.println(Integer.MAX_VALUE);//打印最大整数:2147483647
			// System.out.println(Integer.MIN_VALUE);//打印最小整数:-2147483648
			// //相应的浮点数：
			// System.out.println(Float.MAX_VALUE) ;
			// System.out.println(Float.MIN_VALUE) ;
			// //双精度：
			// System.out.println(Double.MAX_VALUE) ;
			// System.out.println(Double.MIN_VALUE);
			// Float xx = 2.2f;
			// Float yy = 2.0f;
			// Float tt = xx - yy;
			//
			// BigDecimal b1 = new BigDecimal(Float.toString(xx));
			// BigDecimal b2 = new BigDecimal(Float.toString(yy));
			// float ss = b1.subtract(b2).floatValue();
			// System.out.println("ssss----" + ss);
			// System.out.println("tttttt-----" + tt);

//			// IterationEngine.getInstance();
//			String className = "d://tmp/FunctionUUID.class";
//			FileInputStream fis = new FileInputStream(className);
//			// 使用 ChangeVersionAdapter 修改 class 文件的版本
//			ClassReader cr = new ClassReader(fis);
//			ClassVisitor ca = new ClassVisitor() {
//
//				@Override
//				public void visitSource(String source, String debug) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void visitOuterClass(String owner, String name,
//						String desc) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public MethodVisitor visitMethod(int access, String name,
//						String desc, String signature, String[] exceptions) {
//					System.out.println(" " + name + desc);
//					return null;
//				}
//
//				@Override
//				public void visitInnerClass(String name, String outerName,
//						String innerName, int access) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public FieldVisitor visitField(int access, String name,
//						String desc, String signature, Object value) {
//					System.out.println(" " + desc + " " + name);
//					return null;
//				}
//
//				@Override
//				public void visitEnd() {
//					System.out.println("}");
//				}
//
//				@Override
//				public void visitAttribute(Attribute attr) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public AnnotationVisitor visitAnnotation(String desc,
//						boolean visible) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public void visit(int version, int access, String name,
//						String signature, String superName, String[] interfaces) {
//					StringBuffer impl = new StringBuffer();
//					for (int i = 0; i < interfaces.length; i++) {
//						if(impl.length()>0){
//							impl.append(",");
//						}
//						impl.append(interfaces[i]);
//					}
//					System.out.println(name + " extends " + superName + " implements "+impl.toString()+" {");
//				}
//			};
//			cr.accept(ca, ClassReader.EXPAND_FRAMES);
//			String string = cr.getSuperName();
//			System.out.println(string);
//			// cr.getInterfaces();
//			if (fis != null) {
//				fis.close();
//			}
//			// ClasssWriter cw = new ClassWriter(0);
//			// ChangeVersionAdapter 类是我们自定义用来修改 class 文件版本号的类
//			// ClassAdapter ca = new ChangeVersionAdapter (cw);
//			// cr.accept(ca, 0);
//			// byte[] b2 = cw.toByteArray();
//			long startTime = System.currentTimeMillis();
//			
//			IterationEngine engine = IterationEngineImpl.getInstance();
//			System.out.println("耗时： "+(System.currentTimeMillis() - startTime));
//			for (int i = 0; i < 100; i++) {	
//				startTime = System.currentTimeMillis();
//				FelContext ctx = engine.getContext();
//				ctx.set("数量", 10);
//				ctx.set("单价", 3);
//				ctx.set("人员数", 2);
//				ctx.set("消耗费", 20);
//				String exp = "数量 * 单价 * 人员数  + 消耗费 + RANDOM()";
//				System.out.println("耗时： "+(System.currentTimeMillis() - startTime));
//				startTime = System.currentTimeMillis();
//				Object value = engine.eval(exp, ctx);
//				System.out.println(value);
//				System.out.println("耗时： "+(System.currentTimeMillis() - startTime));
//			}
			Map<String, Object> map = new HashMap<String,Object>();
			IterationContext ctx = new IterationContextImpl();
			ctx.clear();
			map.put("人员", "小明");
			map.put("工号", 007);
			ctx.set(map);
			map.put("人员", "小明a");
			Object value = ctx.get("人员");
			System.out.println(value);
			
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
