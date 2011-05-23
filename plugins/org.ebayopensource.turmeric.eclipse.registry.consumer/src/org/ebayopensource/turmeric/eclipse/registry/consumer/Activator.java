/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.registry.consumer;

import org.ebayopensource.turmeric.eclipse.registry.consumer.resources.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;



/**
 * The Class Activator.
 *
 * @author yayu
 */
public class Activator extends AbstractUIPlugin {
	
	/** The DEBUG. */
	public static boolean DEBUG;
	

	// The plug-in ID
	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.registry.consumer";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		DEBUG = isDebugging() && "true".equalsIgnoreCase(
				Platform.getDebugOption(PLUGIN_ID + "/debug"));
	}

	/**
	 * {@inheritDoc}
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
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * Convenient method for logging to the platform.
	 *
	 * @param e the e
	 */
	public void log(Exception e) {
		getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID,
						e.getLocalizedMessage(), e));
	}

	/**
	 * Convenient method for logging to the platform.
	 *
	 * @param msg the msg
	 */
	public void log(String msg) {
		getLog().log(new Status(IStatus.INFO, PLUGIN_ID, msg));
	}
	
	/**
	 * Log argument message.
	 *
	 * @param msg the msg
	 * @param args the args
	 */
	public void logArgumentMessage(String msg, Object... args) {
		log(Messages.formatString(msg, args));
	}

}
