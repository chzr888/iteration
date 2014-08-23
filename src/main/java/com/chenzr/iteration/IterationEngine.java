package com.chenzr.iteration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.function.Function;

/**
 * 迭代运算引擎
 * 
 * @author chenzr
 * 
 */
public class IterationEngine {

	public synchronized static FelEngine getInstance() {
		FelEngine engine = new FelEngineImpl();
		autoLoadFun(engine);
		return engine;
	}

	public static void autoLoadFun(FelEngine engine) {
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

	public static void iteratorFile(File file, List<String> classList,
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
}
