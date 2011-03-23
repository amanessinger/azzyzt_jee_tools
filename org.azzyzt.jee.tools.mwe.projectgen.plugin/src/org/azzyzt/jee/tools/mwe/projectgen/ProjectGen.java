package org.azzyzt.jee.tools.mwe.projectgen;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

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
		@SuppressWarnings("unchecked")
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
		}
		return jeeRuntimeJarUrl != null 
			|| jeeRuntimeSiteJarUrl != null 
			|| jeeToolsMweJarUrl != null
			;
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

	public static List<URL> extraURLsForToolMainClass() {
		List<URL> extraUrls = new ArrayList<URL>();
		extraUrls.add(getJeeToolsMweJarUrl());
		extraUrls.add(getJeeRuntimeJarUrl());
		extraUrls.add(getJeeRuntimeSiteJarUrl());
		extraUrls.addAll(getToolsLibJarUrls());
		return extraUrls;
	}
	
}
