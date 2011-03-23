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
package org.ebayopensource.turmeric.eclipse.config.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException;


/**
 * @author yayu
 * @since 1.0.0
 */
public final class SOADomainAccessor {
	
	public static final String PROP_KEY_DOMAIN_REQUIRED = "IsDomainRequired";
	public static final String PROP_KEY_DOMAINS= "Domains";
	
	private static final Map<String, Boolean> IS_DOMAIN_REQUIRED 
	= new ConcurrentHashMap<String, Boolean>(5);
	
	/**
	 * The key of first map is build system ID, whereas the key of the inner
	 * map is domain name with classifier as the corresponding value.
	 */
	private static final Map<String, Map<String, List<String>>> DOMAINS
	= new ConcurrentHashMap<String, Map<String, List<String>>>(5);
	
	/**
	 * 
	 */
	private SOADomainAccessor() {
		super();
	}
	
	private static void init(String buildSystemID, String organization) 
	throws SOAConfigAreaCorruptedException, IOException {
		final Properties props = SOAGlobalConfigAccessor.getOrganizationConfigurations(
				buildSystemID, organization);
		if (props != null) {
			final String key = getKey(buildSystemID, organization);
			final String domainNames = StringUtils.trim(props.getProperty(PROP_KEY_DOMAINS));
			final String isDomainRequired = StringUtils.trim(props.getProperty(PROP_KEY_DOMAIN_REQUIRED));
			IS_DOMAIN_REQUIRED.put(key, Boolean.valueOf(isDomainRequired));
			Map<String, List<String>> data = DOMAINS.get(key);
			if (data == null) {
				data = new LinkedHashMap<String, List<String>>(2);
			}
			if (data.containsKey(buildSystemID) == false) {
				for (String val : StringUtils.split(domainNames, ",")) {
					String domain = StringUtils.substringBefore(val, "=").trim();
					String classifiers = StringUtils.substringAfter(val, "=").trim();
					List<String> classifierList = data.get(domain);
					if (classifierList == null)
						classifierList = new ArrayList<String>(5);
					for (String classifier : StringUtils.split(classifiers, ":")) {
						final String clas = classifier.trim();
						if (classifierList.contains(clas) == false)
							classifierList.add(clas);
					}

					data.put(domain, classifierList);
				}
			}
			DOMAINS.put(key, data);
		}
	}
	
	public static Map<String, List<String>> parseStringToDomainMap(String value) {
		final Map<String, List<String>> data = new LinkedHashMap<String, List<String>>();
		for (String val : StringUtils.split(value, ",")) {
			String domain = StringUtils.substringBefore(val, "=").trim();
			String classifiers = StringUtils.substringAfter(val, "=").trim();
			List<String> classifierList = data.get(domain);
			if (classifierList == null)
				classifierList = new ArrayList<String>(5);
			for (String classifier : StringUtils.split(classifiers, ":")) {
				final String clas = classifier.trim();
				if (classifierList.contains(clas) == false)
					classifierList.add(clas);
			}
			
			data.put(domain, classifierList);
		}
		return data;
	}
	
	public static String paseDomainMapToString(Map<String, List<String>> domains) {
		final StringBuffer result = new StringBuffer();
		int index = 0;
		for (String key : domains.keySet()) {
			if (index > 0)
				result.append(",");
			result.append(key);
			result.append("=");
			for (int i = 0; i < domains.get(key).size(); i++) {
				if (i > 0)
					result.append(":");
				
				result.append(domains.get(key).get(i));
			}
			index++;
		}
		return result.toString();
	}
	
	public static boolean isDomainRequired(String buildSystemID, String organization) throws Exception {
		if (IS_DOMAIN_REQUIRED.isEmpty()) {
			init(buildSystemID, organization);
		}
		return IS_DOMAIN_REQUIRED.get(getKey(buildSystemID, organization));
	}
	
	public static Map<String, List<String>> getDomains(String buildSystemID, String organization) throws Exception {
		if (DOMAINS.isEmpty()) {
			init(buildSystemID, organization);
		}
		return DOMAINS.get(getKey(buildSystemID, organization));
	}
	
	private static String getKey(String buildSystemID, String organization) {
		buildSystemID = buildSystemID.toLowerCase(Locale.US);
		organization = organization.toLowerCase(Locale.US);
		return buildSystemID + "-" + organization;
	}

}
