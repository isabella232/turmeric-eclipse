/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.config.repo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.config.ConfigActivator;
import org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;


/**
 * @author yayu
 * @since 1.0.0
 */
public final class SOAConfigExtensionFactory {
	public static final String ID_SOA_CONFIG = 
		ConfigActivator.PLUGIN_ID + ".soaConfig";
	
	public static final String ATTR_NAME = "name";
	public static final String ATTR_ORGANIZATION = "organization";
	public static final String ATTR_RELATIVE_PATH = "relativePath";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_SUB_TYPE = "xsdSubType";
	
	private static final Map<String, List<SOAConfigTemplate>> wsdlTemplates = 
		new LinkedHashMap<String, List<SOAConfigTemplate>>();
	
	private static final Map<String, Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>>> xsdTemplates = 
		new ConcurrentHashMap<String, Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>>>();
	
	private static final Map<String, List<SOAConfigTemplate>> xmlTemplates = 
		new LinkedHashMap<String, List<SOAConfigTemplate>>();

	private SOAConfigExtensionFactory() {
		super();
	}
	
	public static URL getWSDLTemplate(String organization, String name) throws SOAConfigAreaCorruptedException {
		for (SOAConfigTemplate template : getWSDLTemplates(organization.toLowerCase(Locale.US))) {
			if (template.getName().equals(name)) {
				return template.getUrl();
			}
		}
		
		return null;
	}
	
	private static void init() throws SOAConfigAreaCorruptedException {
		if (wsdlTemplates.isEmpty() == true || xsdTemplates.isEmpty() == true) {
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
			.getExtensionPoint(ID_SOA_CONFIG);
			
			if (extensionPoint != null) {
				IExtension[] extensions = extensionPoint.getExtensions();
				for (final IExtension extension : extensions) {
					for (final IConfigurationElement element : extension
							.getConfigurationElements()) {
						final String name = element.getAttribute(ATTR_NAME).trim();
						final String org = element.getAttribute(ATTR_ORGANIZATION).trim().toLowerCase(Locale.US);
						final String relativePath = element.getAttribute(ATTR_RELATIVE_PATH).trim();
						final URL url = FileLocator.find(ConfigActivator.getDefault().getBundle(), new Path(relativePath), null);
						final String type = element.getAttribute(ATTR_TYPE).trim();
						if (type.equalsIgnoreCase("WSDL")) {
							List<SOAConfigTemplate> templates = wsdlTemplates.get(org);
							if (templates == null) {
								templates = new ArrayList<SOAConfigTemplate>();
								wsdlTemplates.put(org, templates);
							}
							try {
								if (url != null) {
									templates.add(new SOAConfigTemplate(name, org, relativePath, 
											FileLocator.toFileURL(url)));
								}
							} catch (IOException e) {
								throw new SOAConfigAreaCorruptedException(e);
							}
						} else if (type.equalsIgnoreCase("XSD")) {
							Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> templates = xsdTemplates.get(org);
							if (templates == null) {
								templates = new LinkedHashMap<SOAXSDTemplateSubType, List<SOAConfigTemplate>>();
								xsdTemplates.put(org, templates);
							}
							try {
								if (url != null) {
									String value = element.getAttribute(ATTR_SUB_TYPE);
									
									if (StringUtils.isNotBlank(value)) {
										SOAXSDTemplateSubType subType = SOAXSDTemplateSubType.valueOf(value);
										if (subType != null) {
											List<SOAConfigTemplate> temps = templates.get(subType);
											if (temps == null) {
												temps = new ArrayList<SOAConfigTemplate>();
												templates.put(subType, temps);
											}
											temps.add(new SOAConfigTemplate(name, org, relativePath, 
													FileLocator.toFileURL(url), subType));
										}
									}
								}
							} catch (IOException e) {
								throw new SOAConfigAreaCorruptedException(e);
							}
						} else if (type.equalsIgnoreCase("XML")) {
							List<SOAConfigTemplate> templates = xmlTemplates.get(org);
							if (templates == null) {
								templates = new ArrayList<SOAConfigTemplate>();
								xmlTemplates.put(org, templates);
							}
							try {
								if (url != null) {
									templates.add(new SOAConfigTemplate(name, org, relativePath, 
											FileLocator.toFileURL(url)));
								}
							} catch (IOException e) {
								throw new SOAConfigAreaCorruptedException(e);
							}
						}
							
					}
				}
			}
		}
	}
	
	public static Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> getXSDTemplates(String organization)
	throws SOAConfigAreaCorruptedException {
		organization = organization.toLowerCase(Locale.US);
		if (xsdTemplates.isEmpty() == true) {
			init();
		}
		
		return xsdTemplates.containsKey(organization) ? xsdTemplates.get(organization) 
				: new ConcurrentHashMap<SOAXSDTemplateSubType, List<SOAConfigTemplate>>(0);
	}
	
	public static List<SOAConfigTemplate> getWSDLTemplates(String organization) throws SOAConfigAreaCorruptedException {
		organization = organization.toLowerCase(Locale.US);
		if (wsdlTemplates.isEmpty() == true) {
			init();
		}
		
		return wsdlTemplates.containsKey(organization) ? wsdlTemplates.get(organization) 
				: new ArrayList<SOAConfigTemplate>(0);
	}
	
	public static List<SOAConfigTemplate> getXMLTemplates(String organization) throws SOAConfigAreaCorruptedException {
		organization = organization.toLowerCase(Locale.US);
		if (xmlTemplates.isEmpty() == true) {
			init();
		}
		
		return xmlTemplates.containsKey(organization) ? xmlTemplates.get(organization) 
				: new ArrayList<SOAConfigTemplate>(0);
	}
	
	public static URL getXMLTemplate(String organization, String name) throws SOAConfigAreaCorruptedException {
		for (SOAConfigTemplate template : getXMLTemplates(organization.toLowerCase(Locale.US))) {
			if (template.getName().equals(name)) {
				return template.getUrl();
			}
		}
		
		return null;
	}
	
	
	
	public static enum SOAXSDTemplateSubType {
		SIMPLE, COMPLEX, ENUM, COMPLEX_COMPLEXCONTENT, COMPLEX_SIMPLECONTENT
	}

	public static class SOAConfigTemplate {
		private String name;
		private String organization;
		private String relativePath;
		private URL url;
		private SOAXSDTemplateSubType subType;
		
		public SOAConfigTemplate(String name, String organization,
				String relativePath, URL url) {
			super();
			this.name = name;
			this.organization = organization;
			this.relativePath = relativePath;
			this.url = url;
		}
		
		public SOAConfigTemplate(String name, String organization,
				String relativePath, URL url, SOAXSDTemplateSubType subType) {
			this(name, organization, relativePath, url);
			this.subType = subType;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getOrganization() {
			return organization;
		}
		public void setOrganization(String organization) {
			this.organization = organization;
		}
		public String getRelativePath() {
			return relativePath;
		}
		public void setRelativePath(String relativePath) {
			this.relativePath = relativePath;
		}
		public URL getUrl() {
			return url;
		}
		public void setUrl(URL url) {
			this.url = url;
		}

		public SOAXSDTemplateSubType getSubType() {
			return subType;
		}

		public void setSubType(SOAXSDTemplateSubType subType) {
			this.subType = subType;
		}
		
	}

	
}
