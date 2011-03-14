package org.azzyzt.jee.tools.mwe.projectgen.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentLinkedQueue;

class MainRunner implements Runnable {
	
	private ConcurrentLinkedQueue<String> log;
	private URL[] classPathEntries;
	private String fqMainClassName;
	private String[] args;
	
	public MainRunner(
			final URL[] classPathEntries, 
			final String fqMainClassName,
			final String[] args,
			final ConcurrentLinkedQueue<String> log) 
	{
		this.classPathEntries = classPathEntries;
		this.fqMainClassName = fqMainClassName;
		this.args = args;
		this.log = log;
	}
	
	@Override
	public void run() {
		ClassLoader newLoader = createClassLoader(classPathEntries);
		
		try {
			Class<?> clazz = Class.forName(fqMainClassName, true, newLoader);
			Method main = clazz.getMethod("sideEntrance", String[].class, ConcurrentLinkedQueue.class);
			Object[] params = new Object[2];
			params[0] = args;
			params[1] = log;
			main.invoke(null, params);
		} catch (Exception e) {
			// TODO do something
		}
	}
	
	public static ClassLoader createClassLoader(URL[] items) {
		return new URLClassLoader(items, Object.class.getClassLoader());
	}
}
