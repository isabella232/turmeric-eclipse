/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle.
 */
public class UIActivator extends AbstractUIPlugin {

	// The plug-in ID
	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.ui";
	
	/** The Constant ICON_PATH. */
	public static final String ICON_PATH = "icons/";

	// The shared instance
	private static UIActivator plugin;
	private static SOALogger logger = SOALogger.getLogger();

	/** The plugin properties. */
	protected PropertyResourceBundle pluginProperties;

	/**
	 * Gets the plugin properties.
	 *
	 * @return the plugin properties
	 */
	public PropertyResourceBundle getPluginProperties() {
		if (pluginProperties == null) {
			try {
				pluginProperties = JDTUtil.getPluginProperties(getBundle());
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return pluginProperties;
	}

	/**
	 * The constructor.
	 */
	public UIActivator() {
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
		
		TypeLibRegistryJob registryJob = new TypeLibRegistryJob("Initialize Type library");
		registryJob.setUser(false);
		registryJob.schedule(1000);
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
	public static UIActivator getDefault() {
		return plugin;
	}

	
	/**
	 * Gets the image from registry.
	 *
	 * @param uiPlugin the ui plugin
	 * @param iconPathRoot the icon path root
	 * @param path the path
	 * @return the image from registry
	 */
	public static Image getImageFromRegistry(AbstractUIPlugin uiPlugin, 
			String iconPathRoot, String path) {
		if (path == null)
			return null;
		path = StringUtils.replaceChars(path, '\\', '/');
		final String iconPath = path.startsWith(iconPathRoot) ? path : iconPathRoot
				+ path;
		
		Image image = uiPlugin.getImageRegistry().get(iconPath);
		if (image == null) {

			final ImageDescriptor descriptor = imageDescriptorFromPlugin(
					uiPlugin.getBundle().getSymbolicName(), iconPath);
			if (descriptor != null) {
				uiPlugin.getImageRegistry().put(iconPath, descriptor);
				image = uiPlugin.getImageRegistry().get(iconPath);
			}
		}
		return image;
		
	}

	/**
	 * Gets the image from registry.
	 *
	 * @param path the path
	 * @return the image from registry
	 */
	public static Image getImageFromRegistry(String path) {
		return getImageFromRegistry(getDefault(), ICON_PATH, path);
	}
	
	/**
	 * Gets the image descriptor.
	 *
	 * @param pluginID the plugin id
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String pluginID, 
			final String path) {
		ImageDescriptor descriptor = imageDescriptorFromPlugin(pluginID, path);
		return descriptor;
	}

	/**
	 * Gets the image descriptor.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return getImageDescriptor(PLUGIN_ID, path);
	}
	
	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 * Parses the global config categories the format is CATEGORIES =
	 * {COMMON, DOMAIN, SERVICE}
	 */
	public static List<String> getCategories() {
		List<String> categories = new ArrayList<String>();
		String categoryStr = "";
		try {
			categoryStr = SOAGlobalConfigAccessor.getCategoriesForTypeLib();

		} catch (IOException e) {
			SOALogger.getLogger().error(e);
		}
		if (!StringUtils.isEmpty(categoryStr)) {
			categories = Arrays.asList(StringUtils.split(StringUtils
					.substringBetween(categoryStr, "{", "}"), ","));
		}
		return categories;
	}	
	
	/**
	 * Gets the xSD.
	 *
	 * @param libraryName the library name
	 * @param typeName the type name
	 * @return the xSD
	 * @throws Exception the exception
	 */
	public static URL getXSD(String libraryName, String typeName)
			throws Exception {
		String jarLocation = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry()
				.getAssetLocation(libraryName);
		if (!StringUtils.isEmpty(jarLocation)
 	            && jarLocation.endsWith("jar")) {
			String xsdName = TurmericCoreActivator.getXsdFileNameFromTypeName(typeName);
			URL jarURL = IOUtil.toURL(jarLocation);
			String jarEntryPath = "";
			if (TurmericCoreActivator.isNewTypLibrary(jarURL, libraryName)) {
				jarEntryPath = TurmericCoreActivator.TYPES_LOCATION_IN_JAR
						+ WorkspaceUtil.PATH_SEPERATOR + libraryName
						+ WorkspaceUtil.PATH_SEPERATOR + xsdName;
			} else {
				jarEntryPath = TurmericCoreActivator.TYPES_LOCATION_IN_JAR
						+ WorkspaceUtil.PATH_SEPERATOR + xsdName;
			}
			return IOUtil.getNonLockingURL(jarURL, jarEntryPath);
		} else {
			IProject project = WorkspaceUtil.getProject(libraryName);
			if (project.isAccessible()) {
				IFile file = project.getFile(TurmericCoreActivator.getXsdFileLocation(typeName, project));
				return file.getLocation().toFile().toURI().toURL();
			}
			return null;
		}
	}
	
	/**
	 * Gets the xSD.
	 *
	 * @param libType the lib type
	 * @return the xSD
	 * @throws Exception the exception
	 */
	public static URL getXSD(LibraryType libType) throws Exception {
		return getXSD(libType.getLibraryInfo().getLibraryName(),
				libType.getName());
	}
	
	/**
	 * Gets the name space.
	 *
	 * @param projectName the project name
	 * @return the name space
	 * @throws Exception the exception
	 */
	public static String getNameSpace(String projectName) throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
				.getTypeLibrary(projectName).getLibraryNamespace();
	}
	
	/**
	 * Get all template Category files.
	 *
	 * @return the template category files
	 */
	public static Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> getTemplateCategoryFiles() {
		try {
			final String organization = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem()
					.getActiveOrganizationProvider().getName();
			return SOAConfigExtensionFactory.getXSDTemplates(organization);
		} catch (SOAConfigAreaCorruptedException e) {
			UIUtil.showErrorDialog(e);
		}
		return null;
	}

	/**
	 * Get files inside a template category.
	 *
	 * @param subType the sub type
	 * @return the files
	 */
	public static List<SOAConfigTemplate> getFiles(SOAXSDTemplateSubType subType) {
		Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> templates = getTemplateCategoryFiles();
		if (templates != null) {
			return templates.get(subType);
		}
		return null;
	}
	

}
