/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class RepositorySystemActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.repositorysystem";

	// The shared instance
	private static RepositorySystemActivator plugin;
	
	/**
	 * The constructor
	 */
	public RepositorySystemActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		StringBuffer buf = new StringBuffer();
    	buf.append("SOAPlugin.start - ");
    	buf.append(JDTUtil.getBundleInfo(context.getBundle(), SOALogger.DEBUG));
        SOALogger.getLogger().info(buf);
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
	public static RepositorySystemActivator getDefault() {
		return plugin;
	}
	
	private IScopeContext instanceScope = new InstanceScope();
	/**
	 * Gets the IEclipsePreferences for this particular plugin.  This uses the newer IEclipsePreferences
	 * instead of the deprecated getPluginPreferences method.
	 * 
	 * @return returns the preferences for the repository system.
	 */
	public IEclipsePreferences getPreferences() {
		IEclipsePreferences node =  instanceScope.getNode(plugin.getBundle().getSymbolicName());
		return node;
	}

}
