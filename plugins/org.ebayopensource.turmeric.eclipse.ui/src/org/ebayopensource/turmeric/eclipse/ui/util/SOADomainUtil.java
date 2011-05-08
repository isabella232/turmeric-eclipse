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
package org.ebayopensource.turmeric.eclipse.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.models.NameValuePair;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.eclipse.core.runtime.Platform;


/**
 * Reading the domain names.
 * 
 * The logics are:
 * 1) AR plugin not installed
 * SOA plugin always read from the locally cached properties file
 * 
 * 2) AR plugin installed
 * If connection to AR established, then always get the latest from AR server, 
 * then persist the retrieved data into a Configuration scoped preference store as a cache.
 * If no connection established, then always use the data in the cache.
 * 
 * @author yayu
 * @since 1.0.0
 */
public final class SOADomainUtil {
	private static final SOALogger logger = SOALogger.getLogger();
	
	/** The Constant SOA_DOMAIN_PREFERENCE_ID. */
	public static final String SOA_DOMAIN_PREFERENCE_ID = "org.ebayopensource.turmeric.eclipse.domains";
	
	/** The Constant SOA_DOMAIN_PREFERENCE_NODE. */
	public static final String SOA_DOMAIN_PREFERENCE_NODE = "domainNames";
	private static final URL SOA_DOMAIN_PREF_LOCATION;
	
	static {
		URL url = null;
		try {
			url = new URL(Platform.getInstallLocation().getURL()
					+ "/configuration/" + SOA_DOMAIN_PREFERENCE_ID + 
					"/" + SOA_DOMAIN_PREFERENCE_NODE + ".prefs");
			logger.info("Configuration location->", url);
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		SOA_DOMAIN_PREF_LOCATION = url;
	}

	/**
	 * 
	 */
	private SOADomainUtil() {
		super();
	}
	
	/**
	 * Key is the domain name, value is the list of namespace parts.
	 *
	 * @return the registered domains
	 * @throws Exception the exception
	 */
	public static Map<String, List<String>> getRegisteredDomains() throws Exception {
		final Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
		try {
			if (ExtensionPointFactory.getSOARegistryProvider() != null) {
				final List<NameValuePair> domains = ExtensionPointFactory.getSOARegistryProvider().getDomainNamespaceValues();
				if (domains != null && domains.isEmpty() == false) {
					StringBuffer buf = new StringBuffer();
					for (NameValuePair nameValuePair: domains) {
						result.put(nameValuePair.getDomainName(), nameValuePair.getClassifiers());
						buf.append(nameValuePair.getDomainName() + "=" + StringUtils.join(nameValuePair.getClassifiers(), ":") + ", ");
					}
					logger.info("Domain list retrieved from AR: ", buf.toString());
					if (result.isEmpty() == false) {
						saveDomainsToCache(result);
						return result;
					}
				}
			}
		} catch (Exception e) {
			logger.warning(
					"Failed to retrieve data from the Asset Repository, use local cache instead.", 
					e);
		}
		
		//could not read from the AR, use the local cache instead.
		result.putAll(loadDomainsFromCache());
		if (result.isEmpty() == false)
			return result;
		
		//use the properties file
		final String buildSystemID = 
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getId();
		final String organization =  GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getActiveOrganizationProvider().getName();
		logger.info("Loading functional domain list from the static file...");
		result.putAll(SOADomainAccessor.getDomains(buildSystemID, organization));
		
		saveDomainsToCache(result);
		return result;
	}
	
	private static Map<String, List<String>> loadDomainsFromCache() {
		final Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
		if (SOA_DOMAIN_PREF_LOCATION != null) {
			if (FileUtils.toFile(SOA_DOMAIN_PREF_LOCATION).canRead() == false) {
				logger.warning("The cache file does not exist->", SOA_DOMAIN_PREF_LOCATION);
				return result;
			}
			InputStream input = null;
			try {
				input = SOA_DOMAIN_PREF_LOCATION.openStream();
				final Properties props = new Properties();
				props.load(input);
				final String data = props.getProperty(SOA_DOMAIN_PREFERENCE_NODE, "");
				logger.info("Domain names loaded from cache->", data);
				return SOADomainAccessor.parseStringToDomainMap(data);
			} catch (Exception e) {
				logger.warning(e);
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		return result;
	}
	
	private static void saveDomainsToCache(Map<String, List<String>> domains)  {
		if (SOA_DOMAIN_PREF_LOCATION != null) {
			InputStream input = null;
			OutputStream output = null;
			try {
				File file = FileUtils.toFile(SOA_DOMAIN_PREF_LOCATION);
				if (file.exists() == false) {
					if (file.getParentFile().exists() == false) {
						FileUtils.forceMkdir(file.getParentFile());
					}
					file.createNewFile();
				} else if (file.canWrite() == false) {
					logger.warning("The SOA domain cache file is not writable->", file);
					return;
				}
				
				final String data = SOADomainAccessor.paseDomainMapToString(domains);
				final Properties props = new Properties();
				output = new FileOutputStream(file);
				props.setProperty(SOA_DOMAIN_PREFERENCE_NODE, data);
				props.store(output, "SOA Domain names.\n");
				logger.info("Storing domain names->", data);
			} catch (Exception e) {
				logger.warning(e);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
