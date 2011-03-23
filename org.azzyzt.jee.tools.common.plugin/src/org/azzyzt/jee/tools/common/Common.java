package org.azzyzt.jee.tools.common;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Common extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.azzyzt.jee.tools.common.plugin"; //$NON-NLS-1$

	// The shared instance
	private static Common plugin;
	
	/**
	 * The constructor
	 */
	public Common() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public void log(String msg) {
		log(msg, null);
	}

	public void log(String msg, Exception e) {
		getLog().log(new Status(Status.INFO, PLUGIN_ID, Status.OK, msg, e));
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Common getDefault() {
		return plugin;
	}

}
