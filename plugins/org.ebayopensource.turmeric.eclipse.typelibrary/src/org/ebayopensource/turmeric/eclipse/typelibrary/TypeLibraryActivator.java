/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.TypeLibMoveDeleteHook;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 * 
 * @author smathew
 * 
 */
public class TypeLibraryActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.typelibrary";
	public static final String ICON_PATH = "icons/";
	private TypeLibMoveDeleteHook typeLibMoveDeleteHook;
	// The shared instance
	private static TypeLibraryActivator plugin;

	/**
	 * The constructor
	 */
	public TypeLibraryActivator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		StringBuffer buf = new StringBuffer();
		buf.append("SOAPlugin.start - ");
		buf.append(JDTUtil.getBundleInfo(context.getBundle(), SOALogger.DEBUG));
		SOALogger.getLogger().info(buf);
		typeLibMoveDeleteHook = new TypeLibMoveDeleteHook();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				typeLibMoveDeleteHook, IResourceChangeEvent.POST_CHANGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (typeLibMoveDeleteHook != null)
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(
					typeLibMoveDeleteHook);
		plugin = null;
		super.stop(context);

	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static TypeLibraryActivator getDefault() {
		return plugin;
	}

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

	public static ImageDescriptor getImageDescriptor(final String path) {
		ImageDescriptor descriptor = imageDescriptorFromPlugin(PLUGIN_ID, path);
		return descriptor;
	}

}
