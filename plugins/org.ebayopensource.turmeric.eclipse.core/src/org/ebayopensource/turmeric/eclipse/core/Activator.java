/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDParser;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	private static final String EXT_XSD = ".xsd";
	// The plug-in ID
	public static final String PLUGIN_ID_PREFIX = "org.ebayopensource.turmeric.eclipse";
	public static final String PLUGIN_ID = PLUGIN_ID_PREFIX + ".core";

	// The shared instance
	private static Activator plugin;
	
	public final static String MY_PROPERTIES = "plugin.properties";
	private static BundleContext context = null;
	private static SOALogger soaLogger = SOALogger.getLogger();

    protected PropertyResourceBundle pluginProperties;

    public PropertyResourceBundle getPluginProperties(){
    	if (pluginProperties == null){
    		try {
    			pluginProperties = new PropertyResourceBundle(
    					FileLocator.openStream(getBundle() == null ? context.getBundle() : getBundle(),
    							new Path(MY_PROPERTIES),false));
    		} catch (IOException e) {
    			soaLogger.error(e);
    		}
    	}
    	return pluginProperties;
    }	

	/**
	 * The constructor
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
		StringBuffer buf = new StringBuffer();
		buf.append("SOAPlugin.start - ");
		buf.append(JDTUtil.getBundleInfo(context.getBundle(), SOALogger.DEBUG));
		SOALogger.getLogger().info(buf);
	}

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

	public static XSDSchema parseSchema(InputStream inputStream)
			throws IOException {
		try {
			XSDParser xSDParser = new XSDParser();
			xSDParser.parse(inputStream);
			return xSDParser.getSchema();
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	public static XSDSchema parseSchema(URL url) throws IOException {
		InputStream inputStream = null;
		inputStream = url.openStream();
		return parseSchema(inputStream);
	}

	/**
	 * Just appending the extn to the XSD name. Extn names could be changed :)),
	 * You never know. This is just a dumb helper
	 * 
	 * @param typeName
	 * @return
	 */
	public static String getXsdFileNameFromTypeName(String typeName) {
		String retValue = typeName;
		if (!StringUtils.isEmpty(typeName) && !typeName.endsWith(EXT_XSD))
			retValue = retValue + EXT_XSD;
		return retValue;
	}

	/**
	 * @param typeName
	 * @return
	 * 
	 *         Answers the file location relative to the project structure. In
	 *         short project.getFile() with this output should return the file.
	 */
	public static String getXsdFileLocation(String typeName, IProject project) {
		String retValue = "";
		if (!StringUtils.isEmpty(typeName)) {
			if (isNewTypLibrary(project)) {
				retValue = FOLDER_META_SRC_TYPES + WorkspaceUtil.PATH_SEPERATOR
						+ project.getName() + WorkspaceUtil.PATH_SEPERATOR
						+ typeName + EXT_XSD;

			} else {
				retValue = FOLDER_META_SRC_TYPES + WorkspaceUtil.PATH_SEPERATOR
						+ typeName + EXT_XSD;
			}
		}
		return retValue;
	}

	/**
	 * TODO: these need to be setup through an extension point or preference
	 * page.
	 */
	public static final String TYPES_LOCATION_IN_JAR = "types";
	private static final String FOLDER_GEN_SRC = "gen-src";
	private static final String FOLDER_GEN_META_SRC = "gen-meta-src";
	private static final String FOLDER_GEN_META_SRC_META_INF = "gen-meta-src/META-INF";
	private static final String FOLDER_META_SRC = "meta-src";
	private static final String FOLDER_META_SRC_META_INF = "meta-src/META-INF";
	private static final String FOLDER_META_SRC_TYPES = "meta-src/types";
	private static final String INFO_DEP_XML_PATH_IN_JAR = "META-INF/";

	/**
	 * Old type library jar(NOT in workspace) has the dir structure \types\<xsd>
	 * and the new one has meta-src\types\<typeLibName>\<xsd>
	 * 
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static boolean isNewTypLibrary(URL jarURL, String projectName)
			throws IOException {
		File file = new File(jarURL.getPath());
		JarFile jarFile;
		jarFile = new JarFile(file);
		return jarFile.getEntry(TYPES_LOCATION_IN_JAR
				+ WorkspaceUtil.PATH_SEPERATOR + projectName) != null;

	}

	/**
	 * Old type library project (in workspace) has the dir structure
	 * meta-src\types\<xsd> and the new one has
	 * meta-src\types\<typeLibName>\<xsd>
	 * 
	 * @return
	 */
	public static boolean isNewTypLibrary(IProject project) {
		return project.getFolder(
				FOLDER_META_SRC_TYPES + WorkspaceUtil.PATH_SEPERATOR
						+ project.getName()).exists();
	}

	public static String formatContents(String contents) throws IOException,
			CoreException {
		FormatProcessorXML formatProcessor = new FormatProcessorXML();
		return formatProcessor.formatContent(contents);
	}

}
