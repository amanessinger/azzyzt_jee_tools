/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.tools.common;

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
		String logLevel = System.getenv("AZZYZT_LOG_LEVEL");
		if (logLevel != null && logLevel.matches("^(error|info|debug)$")) {
			this.args = new String[args.length + 1];
			this.args[0] = "--"+logLevel;
			System.arraycopy(args, 0, this.args, 1, args.length);
		} else {
			this.args = args;
		}
		this.log = log;
	}
	
	@Override
	public void run() {
		ClassLoader newLoader = createClassLoader(classPathEntries);
		
		String msg = "Invoking sideEntrance on class "+fqMainClassName;
		Common.getDefault().log(msg);
		String errMsg = msg+" failed";
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
			Common.getDefault().log(errMsg, e);
		} catch (SecurityException e) {
			Common.getDefault().log(errMsg, e);
		} catch (NoSuchMethodException e) {
			Common.getDefault().log(errMsg, e);
		} catch (IllegalArgumentException e) {
			Common.getDefault().log(errMsg, e);
		} catch (IllegalAccessException e) {
			Common.getDefault().log(errMsg, e);
		} catch (InvocationTargetException e) {
			Common.getDefault().log(errMsg, e);
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
