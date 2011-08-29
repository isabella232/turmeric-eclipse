/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi;

import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.mavenapi.intf.IMavenEclipseApi;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 */
public class MavenApiPlugin extends Plugin {

	/**
	 *  The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.maveneclipseapi";

	// The shared instance
	private static MavenApiPlugin plugin;

	// The shared API instance
	private MavenEclipseApi mavenEclipseApi;

	/**
	 * The constructor.
	 */
	public MavenApiPlugin() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// Create the service object
		mavenEclipseApi = new MavenEclipseApi();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static MavenApiPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the shared API instance.
	 * 
	 * @return the shared instance
	 */
	public IMavenEclipseApi getMavenEclipseApi() {
		return mavenEclipseApi;
	}
}
