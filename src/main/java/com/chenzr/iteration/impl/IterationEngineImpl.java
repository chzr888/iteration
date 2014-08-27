package com.chenzr.iteration.impl;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chenzr.iteration.IterationContext;
import com.chenzr.iteration.IterationEngine;
import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.compile.CompileService;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.function.FunMgr;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.parser.AntlrParser;
import com.greenpineyu.fel.parser.FelNode;

/**
 * 迭代运算引擎实现
 * @author chenzr
 *
 */
public class IterationEngineImpl extends FelEngineImpl implements IterationEngine {
	
	private MessageDigest md5 = null;

	private char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };
	
	private Map<String, FelNode> preparedNode = new HashMap<String, FelNode>();
	
	private Map<String, Expression> compileExpMap = new HashMap<String, Expression>();

	
	public IterationEngineImpl() {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IterationEngineImpl(FelContext context) {
		super.setContext(context);
		super.setCompiler(new CompileService());
		super.setParser(new AntlrParser(this));
		super.setFunMgr(new FunMgr());
	}
	
	@Override
	public FelNode parse(String exp) {
		// long time = System.currentTimeMillis();
		String md5 = getMd5(exp);
		FelNode node = preparedNode.get(md5);
		if (null == node) {
			node = super.parse(exp);
			preparedNode.put(md5, node);
		}
		// System.out.println(System.currentTimeMillis() - time);
		return node;
	}

	public synchronized static IterationEngine getInstance() {
		IterationEngine engine = new IterationEngineImpl();
		autoLoadFun(engine);
		return engine;
	}

	public static void autoLoadFun(IterationEngine engine) {
		try {
			if(null == engine){
				return ;
			}
			String pckgName = "com.chenzr.iteration.function";
			Class<?>[] classes = getClasses(pckgName);
			for (int i = 0; i < classes.length; i++) {
				Class<?> cls = classes[i];
				if (Function.class.isAssignableFrom(cls)) {
					//System.out.println(cls);
					Function fun = (Function) cls.newInstance();
					engine.addFun(fun);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Class<?>[] getClasses(String pckgName)
			throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = pckgName.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgName + " (" + directory
					+ ") does not appear to be a valid package a");
		}
		if (directory.exists()) {
			List<String> classList = new ArrayList<String>();
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				iteratorFile(files[i], classList, pckgName);
			}
			for (int i = 0; i < classList.size(); i++) {
				classes.add(Class.forName(classList.get(i)));
			}
		} else {
			throw new ClassNotFoundException(pckgName
					+ " does not appear to be a valid package b");
		}
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

	private static void iteratorFile(File file, List<String> classList,
			String pckgName) {
		if (null == file) {
			return;
		}
		if (file.isDirectory()) {
			String pack = file.getName();
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				iteratorFile(files[i], classList, pckgName + "." + pack);
			}
		} else {
			String name = file.getName();
			if (name.endsWith(".class")) {
				String classPath = pckgName + '.'
						+ name.substring(0, name.length() - 6);
				classList.add(classPath);
			}
		}
	}
	
	private String getMd5(String str) {

		md5.update(str.getBytes());

		return toHexString(md5.digest());
	}
	
	private String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	@Override
	public Expression compileExp(String exp, IterationContext ctx) {
		String md5 = getMd5(exp);
		Expression ie = compileExpMap.get(md5);
		if(null == ie){
			ie =  super.compile(exp, ctx, null);
			compileExpMap.put(md5, ie);
		}
		return ie;
	}
}
