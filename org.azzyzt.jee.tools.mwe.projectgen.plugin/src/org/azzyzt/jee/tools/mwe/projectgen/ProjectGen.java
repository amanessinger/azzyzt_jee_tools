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

package org.azzyzt.jee.tools.mwe.projectgen;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ProjectGen extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.azzyzt.jee.tools.mwe.projectgen"; //$NON-NLS-1$

	// Azzyzt release. Don't edit it, this is set automatically by VersionBumper
    public static final String AZZYZT_RELEASE = "1.2.4";
	
	// The ID of the associated nature
	public static final String AZZYZTED_NATURE_ID = "org.azzyzt.jee.mwe.nature.id"; //$NON-NLS-1$

	// names of runtime and tools jars
	public static final String JEE_RUNTIME_JAR = "org.azzyzt.jee.runtime.jar";
	public static final String JEE_RUNTIME_SITE_JAR = "org.azzyzt.jee.runtime.site.jar";
	public static final String JEE_TOOLS_MWE_JAR = "org.azzyzt.jee.tools.mwe.jar";

	// The shared instance
	private static ProjectGen plugin;
	
	// Data shared between extensions
	private static URL jeeRuntimeJarUrl = null;
	private static URL jeeRuntimeSiteJarUrl = null;
	private static URL jeeToolsMweJarUrl = null;
	private static List<URL> toolsLibJarUrls = new ArrayList<URL>();
	private static List<URL> cxfRestClientLibJarUrls = new ArrayList<URL>();

	/**
	 * The constructor
	 */
	public ProjectGen() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		/*
		 * TODO the next call is a workaround. It seems to be necessary to somehow start
		 * the JpaProjectManage before installing the JPA facet. Otherwise installing
		 * the facet does not create a persistence.xml
		 * 
		 * TODO check with current milestone and report a bug
		 * 
		 * Symptom: first time after starting Eclipse it will produce a project without
		 * plugin.xml. Delete the structured project, create anew and it works. 
		 * 
		 */
		Class<?> jpaPlugin = null;
		for (String name : Arrays.asList(
				"org.eclipse.jpt.jpa.core.JptJpaCorePlugin", // introduced with Indigo
				"org.eclipse.jpt.core.JptCorePlugin"         // Galileo and Helios
				)) {
			try {
				jpaPlugin = Class.forName(name);
				break;
			} catch (ClassNotFoundException ex) {
				continue;
			}
		}
		try {
			Method m = jpaPlugin.getMethod("getJpaProjectManager", (Class<?>[])null);
			m.invoke(null, (Object[])null);
		} catch (NoSuchMethodException e) {
			// Galileo does not have this method at all, but does not need the call either 
		}
		
		if (!successfullyInitializedLibraryJarUrls()) {
			throw new Exception("Couldn't initialize library JAR URLs"); // TODO any better idea?
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ProjectGen getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private boolean successfullyInitializedLibraryJarUrls() {
		Bundle bundle = ProjectGen.getDefault().getBundle();
		boolean recurse = true;
		Enumeration<URL> jars = (Enumeration<URL>)bundle.findEntries("lib", "*.jar", recurse);
		while (jars.hasMoreElements()) {
			URL jar = jars.nextElement();
			if (jar.getPath().endsWith(JEE_RUNTIME_JAR)) {
				jeeRuntimeJarUrl = jar;
				continue;
			}
			if (jar.getPath().endsWith(JEE_RUNTIME_SITE_JAR)) {
				jeeRuntimeSiteJarUrl = jar;
				continue;
			}
			if (jar.getPath().endsWith(JEE_TOOLS_MWE_JAR)) {
				jeeToolsMweJarUrl = jar;
				continue;
			}
			if (jar.getPath().contains("/lib/tools/")) {
				toolsLibJarUrls.add(jar);
				continue;
			}
			if (jar.getPath().contains("/lib/cxf_rest_client/")) {
				cxfRestClientLibJarUrls.add(jar);
			}
		}
		return jeeRuntimeJarUrl != null 
			|| jeeRuntimeSiteJarUrl != null 
			|| jeeToolsMweJarUrl != null
			;
	}

	public static Map<String, URL> getAllRuntimeJarUrls() {
		Map<String, URL> runtimeJars = new HashMap<String, URL>();
		runtimeJars.put(JEE_RUNTIME_JAR, getJeeRuntimeJarUrl());
		runtimeJars.put(JEE_RUNTIME_SITE_JAR, getJeeRuntimeSiteJarUrl());
		return runtimeJars;
	}

	public static URL getJeeRuntimeJarUrl() {
		return jeeRuntimeJarUrl;
	}

	public static URL getJeeRuntimeSiteJarUrl() {
		return jeeRuntimeSiteJarUrl;
	}

	public static URL getJeeToolsMweJarUrl() {
		return jeeToolsMweJarUrl;
	}

	public static List<URL> getToolsLibJarUrls() {
		return toolsLibJarUrls;
	}

	public static List<URL> getCxfRestClientLibJarUrls() {
		return cxfRestClientLibJarUrls;
	}
	
}
