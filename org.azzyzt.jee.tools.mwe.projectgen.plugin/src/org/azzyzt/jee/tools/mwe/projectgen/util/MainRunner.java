package org.azzyzt.jee.tools.mwe.projectgen.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.IProgressMonitor;

class MainRunner implements Runnable {
	
	private static int logId = 0;
	
	public static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<LogEntry>> logMap;
	
	private ConcurrentLinkedQueue<LogEntry> log;
	private IProgressMonitor monitor;
	private URL[] classPathEntries;
	private String fqMainClassName;
	private String[] args;
	private int thisId;
	
	public MainRunner(
			final IProgressMonitor monitor,
			final URL[] classPathEntries, 
			final String fqMainClassName,
			final String[] args) 
	{
		this.monitor = monitor;
		this.classPathEntries = classPathEntries;
		this.fqMainClassName = fqMainClassName;
		this.args = args;
		this.log = new ConcurrentLinkedQueue<LogEntry>();
		logMap.put(logId, log);
		thisId = logId;
		logId++;
	}
	
	@Override
	public void run() {
		ClassLoader newLoader = createClassLoader(classPathEntries);
		
		try {
			Class<?> clazz = Class.forName(fqMainClassName, true, newLoader);
			Method main = clazz.getMethod("main", String[].class);
			Object[] params = new Object[1];
			params[0] = args;
			main.invoke(null, params);
		} catch (Exception e) {
			// 
		}
	}
	
	public static ClassLoader createClassLoader(URL[] items) {
		return new URLClassLoader(items, Object.class.getClassLoader());
	}
}
