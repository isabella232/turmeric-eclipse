/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle.
 */
public class ErrorLibraryActivator extends AbstractUIPlugin {

	// The plug-in ID
	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.errorlibrary";

	/** The Constant ICON_PATH. */
	public static final String ICON_PATH = "icons/";
	
	// The shared instance
	private static ErrorLibraryActivator plugin;
	
	/**
	 * The constructor.
	 */
	public ErrorLibraryActivator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		StringBuffer buf = new StringBuffer();
    	buf.append("SOAPlugin.start - ");
    	buf.append(JDTUtil.getBundleInfo(context.getBundle(), SOALogger.DEBUG));
        SOALogger.getLogger().info(buf);
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
	public static ErrorLibraryActivator getDefault() {
		return plugin;
	}
	
	/**
	 * Gets the image from registry.
	 *
	 * @param path the path
	 * @return the image from registry
	 */
	public static ImageDescriptor getImageFromRegistry(String path) {
		if (path == null)
			return null;
		path = StringUtils.replaceChars(path, '\\', '/');
		final String iconPath = path.startsWith(ICON_PATH) ? path : ICON_PATH
				+ path;

		ImageDescriptor image = getDefault().getImageRegistry().getDescriptor(
				iconPath);
		if (image == null) {

			final ImageDescriptor descriptor = imageDescriptorFromPlugin(
					PLUGIN_ID, iconPath);
			if (descriptor != null) {
				getDefault().getImageRegistry().put(iconPath, descriptor);
				image = getDefault().getImageRegistry().getDescriptor(iconPath);
			}
		}
		return image;
	}

}
