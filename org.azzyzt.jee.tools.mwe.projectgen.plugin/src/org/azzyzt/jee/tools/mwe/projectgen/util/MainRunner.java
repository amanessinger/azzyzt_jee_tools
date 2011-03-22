package org.azzyzt.jee.tools.mwe.projectgen.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.security.PrivilegedAction;

import org.azzyzt.jee.tools.mwe.projectgen.Activator;

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
		
		String errMsg = "Invoking sideEntrance on class "+fqMainClassName+" failed";
		try {
			Class<?> clazz = Class.forName(fqMainClassName, true, newLoader);
			Method main = null;
			Object[] params;
			try {
				main = clazz.getMethod("sideEntrance", String[].class, ConcurrentLinkedQueue.class);
				params = new Object[2];
				params[0] = args;
				params[1] = log;
			} catch (NoSuchMethodException nsm) {
				main = clazz.getMethod("main", String[].class);
				params = new Object[1];
				params[0] = args;
			}
			main.invoke(null, params);
		} catch (ClassNotFoundException e) {
			Activator.getDefault().log(errMsg, e);
		} catch (SecurityException e) {
			Activator.getDefault().log(errMsg, e);
		} catch (NoSuchMethodException e) {
			Activator.getDefault().log(errMsg, e);
		} catch (IllegalArgumentException e) {
			Activator.getDefault().log(errMsg, e);
		} catch (IllegalAccessException e) {
			Activator.getDefault().log(errMsg, e);
		} catch (InvocationTargetException e) {
			Activator.getDefault().log(errMsg, e);
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
