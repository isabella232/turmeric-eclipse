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
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;


/**
 * A factory for creating SOAConfigExtension objects.
 *
 * @author yayu
 * @since 1.0.0
 */
public final class SOAConfigExtensionFactory {
	
	/** The Constant ID_SOA_CONFIG. */
	public static final String ID_SOA_CONFIG = 
		ConfigActivator.PLUGIN_ID + ".soaConfig";
	
	/** The Constant ATTR_NAME. */
	public static final String ATTR_NAME = "name";
	
	/** The Constant ATTR_ORGANIZATION. */
	public static final String ATTR_ORGANIZATION = "organization";
	
	/** The Constant ATTR_RELATIVE_PATH. */
	public static final String ATTR_RELATIVE_PATH = "relativePath";
	
	/** The Constant ATTR_TYPE. */
	public static final String ATTR_TYPE = "type";
	
	/** The Constant ATTR_SUB_TYPE. */
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
	
	/**
	 * Gets the wSDL template.
	 *
	 * @param organization the organization
	 * @param name the name
	 * @return the wSDL template
	 * @throws SOAConfigAreaCorruptedException the sOA config area corrupted exception
	 */
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
	
	/**
	 * Gets the xSD templates.
	 *
	 * @param organization the organization
	 * @return the xSD templates
	 * @throws SOAConfigAreaCorruptedException the sOA config area corrupted exception
	 */
	public static Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> getXSDTemplates(String organization)
	throws SOAConfigAreaCorruptedException {
		organization = organization.toLowerCase(Locale.US);
		if (xsdTemplates.isEmpty() == true) {
			init();
		}
		
		return xsdTemplates.containsKey(organization) ? xsdTemplates.get(organization) 
				: new ConcurrentHashMap<SOAXSDTemplateSubType, List<SOAConfigTemplate>>(0);
	}
	
	/**
	 * Gets the wSDL templates.
	 *
	 * @param organization the organization
	 * @return the wSDL templates
	 * @throws SOAConfigAreaCorruptedException the sOA config area corrupted exception
	 */
	public static List<SOAConfigTemplate> getWSDLTemplates(String organization) throws SOAConfigAreaCorruptedException {
		organization = organization.toLowerCase(Locale.US);
		if (wsdlTemplates.isEmpty() == true) {
			init();
		}
		
		return wsdlTemplates.containsKey(organization) ? wsdlTemplates.get(organization) 
				: new ArrayList<SOAConfigTemplate>(0);
	}
	
	/**
	 * Gets the xML templates.
	 *
	 * @param organization the organization
	 * @return the xML templates
	 * @throws SOAConfigAreaCorruptedException the sOA config area corrupted exception
	 */
	public static List<SOAConfigTemplate> getXMLTemplates(String organization) throws SOAConfigAreaCorruptedException {
		organization = organization.toLowerCase(Locale.US);
		if (xmlTemplates.isEmpty() == true) {
			init();
		}
		
		return xmlTemplates.containsKey(organization) ? xmlTemplates.get(organization) 
				: new ArrayList<SOAConfigTemplate>(0);
	}
	
	/**
	 * Gets the xML template.
	 *
	 * @param organization the organization
	 * @param name the name
	 * @return the xML template
	 * @throws SOAConfigAreaCorruptedException the sOA config area corrupted exception
	 */
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

	/**
	 * The Class SOAConfigTemplate.
	 */
	public static class SOAConfigTemplate {
		private String name;
		private String organization;
		private String relativePath;
		private URL url;
		private SOAXSDTemplateSubType subType;
		
		/**
		 * Instantiates a new sOA config template.
		 *
		 * @param name the name
		 * @param organization the organization
		 * @param relativePath the relative path
		 * @param url the url
		 */
		public SOAConfigTemplate(String name, String organization,
				String relativePath, URL url) {
			super();
			this.name = name;
			this.organization = organization;
			this.relativePath = relativePath;
			this.url = url;
		}
		
		/**
		 * Instantiates a new sOA config template.
		 *
		 * @param name the name
		 * @param organization the organization
		 * @param relativePath the relative path
		 * @param url the url
		 * @param subType the sub type
		 */
		public SOAConfigTemplate(String name, String organization,
				String relativePath, URL url, SOAXSDTemplateSubType subType) {
			this(name, organization, relativePath, url);
			this.subType = subType;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the name.
		 *
		 * @param name the new name
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the organization.
		 *
		 * @return the organization
		 */
		public String getOrganization() {
			return organization;
		}
		
		/**
		 * Sets the organization.
		 *
		 * @param organization the new organization
		 */
		public void setOrganization(String organization) {
			this.organization = organization;
		}
		
		/**
		 * Gets the relative path.
		 *
		 * @return the relative path
		 */
		public String getRelativePath() {
			return relativePath;
		}
		
		/**
		 * Sets the relative path.
		 *
		 * @param relativePath the new relative path
		 */
		public void setRelativePath(String relativePath) {
			this.relativePath = relativePath;
		}
		
		/**
		 * Gets the url.
		 *
		 * @return the url
		 */
		public URL getUrl() {
			return url;
		}
		
		/**
		 * Sets the url.
		 *
		 * @param url the new url
		 */
		public void setUrl(URL url) {
			this.url = url;
		}

		/**
		 * Gets the sub type.
		 *
		 * @return the sub type
		 */
		public SOAXSDTemplateSubType getSubType() {
			return subType;
		}

		/**
		 * Sets the sub type.
		 *
		 * @param subType the new sub type
		 */
		public void setSubType(SOAXSDTemplateSubType subType) {
			this.subType = subType;
		}
		
	}

	
}
