/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.config.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.config.ConfigActivator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;


/**
 * Single access point for all configurations. Configurations includes the
 * templates, configuration files etc. Templates can be different for different
 * organization and so are configurations also.
 * 
 * @author smathew
 * 
 */
public class SOAGlobalConfigAccessor {
	public static final String CONF_FOLDER = "configurations";
	public static final String GLOBAL_CONFIG_PATH = CONF_FOLDER + "/global_config.properties";
	public static final String ORGANIZATION_CONFIG_FILE_NAME = "org_conf.properties";
	public static String KEY_DEFAULT_COMPILER_LEVEL = "project.compiler.level";
	private static Properties globalConfigurations = null;
	private static Map<String, Map<String, Properties>> orgConfigsCache = 
		new ConcurrentHashMap<String, Map<String, Properties>>();
	
	public static final String KEY_CATEGORIES = "Type.Library.CATEGORIES";
	private static String strCategories = null;

	public static final String KEY_ORGANIZATION = "Error.Lib.ORGANIZATION";
	public static final String KEY_PREFERRED_ERROR_LIB_CONTENT_PROVIDER = "Preferred.Error.Lib.Content.Provider";
	public static final String KEY_ERROR_LIB_CENTRAL_LOCATION = "Error.Lib.Central.Location";

	
	private static SOAServiceConfiguration soaServiceConfiguration = null;

	public static URL getResource(Bundle bundle, String path) throws IOException {
		if (bundle == null)
			bundle = ConfigActivator.getDefault().getBundle();
		URL url = FileLocator.find(bundle, new Path(path), null);
		if (url != null) {
			return FileLocator.resolve(url);
		}
		return null;
	}
	
	private static Properties loadConfigurations(String path) throws IOException {
		Properties props = null;
		URL url = getResource(ConfigActivator.getDefault().getBundle(), path);
		if (url != null) {
			props = new Properties();
			InputStream in = null;
			try {
				in = url.openStream();
				props.load(in);
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
		return props;
	}
	
	public static Properties getGlobalConfigurations() throws IOException {
		if (globalConfigurations == null) {
			globalConfigurations = loadConfigurations(GLOBAL_CONFIG_PATH);
		}
		return globalConfigurations;
	}
	
	public static URL getOrganizationResource(
			String buildSystem, String oraganization, String path) throws IOException {
		path = CONF_FOLDER + "/" + buildSystem.toLowerCase(Locale.US) + "/" 
		+ oraganization.toLowerCase(Locale.US) + "/" + path;
		return getResource(ConfigActivator.getDefault().getBundle(), path);
	}
	
	
	public static Properties getOrganizationConfigurations(String buildSystemName, String organization) 
	throws IOException {
		buildSystemName = buildSystemName.toLowerCase(Locale.US);
		organization = organization.toLowerCase(Locale.US);
		Map<String, Properties> repoConfig = orgConfigsCache.get(buildSystemName);
		if (repoConfig == null) {
			repoConfig = new ConcurrentHashMap<String, Properties>();
			orgConfigsCache.put(buildSystemName, repoConfig);
		}
		Properties props = repoConfig.get(organization);
		if (props == null) {
			String path = CONF_FOLDER + File.separator + buildSystemName + File.separator 
			+ organization + File.separator + ORGANIZATION_CONFIG_FILE_NAME;
			props = loadConfigurations(path);
			repoConfig.put(organization, props);
		}
		return props;
	}
	
	public static String getDefaultCompilerLevel() throws IOException {
		return StringUtils.trim(getGlobalConfigurations().getProperty(KEY_DEFAULT_COMPILER_LEVEL));
	}

	/**
	 * Returns the type library categories. This can be different for ebay and
	 * pay pal. Later we might even give a preference based UI to edit this.Fs
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getCategoriesForTypeLib() throws IOException {
		if (strCategories == null) {
			Properties props = getGlobalConfigurations();
			if (props != null) {
				strCategories = StringUtils.trim(props.getProperty(KEY_CATEGORIES));
			}
		}
		return strCategories;
	}

	/**
	 * Return the preferred content provider. In SOA right now we have only the
	 * provider for V4. By default it returns V4 provider now and later if
	 * somebody wants to implement a provider they can very well do that and
	 * update the properties file.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getPreferredErrorLibraryContentProvider(String buildSystemName, 
			String organization)
			throws IOException {
		final Properties props = getOrganizationConfigurations(buildSystemName, organization);
		return StringUtils.trim(props.getProperty(
				KEY_PREFERRED_ERROR_LIB_CONTENT_PROVIDER));
	}

	/**
	 * 
	 * Returns the central location for error id generation. This is being used
	 * for error id creation. This is now a file based algorithm and the
	 * SOATools expect this location to be passed to the generation algorithm.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getErrorLibraryCentralLocation(String buildSystemName, 
			String organization) throws IOException {
		final Properties props = getOrganizationConfigurations(buildSystemName, organization);
		return StringUtils.trim(props.getProperty(
				KEY_ERROR_LIB_CENTRAL_LOCATION));
	}

	/**
	 * Returns the service configuration. Configuration includes the types
	 * details required to create service from template WSDL. Some of them are
	 * the request base type, response type, other included types etc. This file
	 * is parsed in the create service flow and the parsed information and data
	 * is added to the WSDL. If anything goes wrong in this process the whole
	 * service creation flow is interrupted and that way it is very important to
	 * have this property correct.
	 * 
	 * @param buildSystemName -
	 *            the name of the folder which has the serivice_conf.properties
	 * @return
	 */
	public static SOAServiceConfiguration getServiceConfiguration(
			String buildSystemName, String organization) throws IOException {
		if (soaServiceConfiguration == null) {
			final Properties props = getOrganizationConfigurations(buildSystemName, organization);
			if (props != null) {
				soaServiceConfiguration = new SOAServiceConfiguration(
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_BASE_REQ_TYPE_NAME)),
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_BASE_RESP_TYPE_NAME)),
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_BASE_REQ_TYPE_NAMESPACE)),
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_BASE_RESP_TYPE_NAMESPACE)),
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_INCLUDED_TYPES_WSDL)), 
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_CLIENT_CONFIG_GROUP)),
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_SERVICE_CONFIG_GROUP)),
						StringUtils.trim(props.getProperty(SOAServiceConfiguration.KEY_ENV_MAPPER_IMPL)));
			}
		}
		return soaServiceConfiguration;

	}

}
