package org.azzyzt.jee.tools.mwe.projectgen.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.security.PrivilegedAction;

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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static ClassLoader createClassLoader(final URL[] items) {
		return AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {
			public URLClassLoader run() {
				return new URLClassLoader(items, Object.class.getClassLoader());
			}
		});
	}
}
