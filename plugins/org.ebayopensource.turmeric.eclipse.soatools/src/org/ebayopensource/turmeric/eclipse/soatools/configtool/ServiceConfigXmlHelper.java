/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.soatools.configtool;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.common.config.ServiceConfig;
import org.eclipse.core.resources.IFile;
import org.jdom.Element;

/**
 * @author yualiu
 *
 */
public class ServiceConfigXmlHelper {
	/**
	 * 
	 */
	public final static String SERVICE_NAME = "service-name";
	private static final String SERVICE_INTERFACE_CLASS_NAME = "service-interface-class-name";
	private static final String SERVICE_IMPL_CLASS_NAME = "service-impl-class-name";
	private static final String SERVICE_IMPL_FACTORY_CLASS_NAME = "service-impl-factory-class-name";

	
	public ServiceConfig parseServiceConfig(InputStream input) throws Exception {
		XMLHelper helper = new XMLHelper(input);
		
		ServiceConfig serviceConfig = new ServiceConfig();
		populateServiceName(helper, serviceConfig);
		populateServiceInterfaceName(helper, serviceConfig);
		populateServiceImplementationName(helper, serviceConfig);
		return serviceConfig;
	}

	/**
	 * @param helper
	 * @param serviceConfig
	 */
	private void populateServiceImplementationName(XMLHelper helper,
			ServiceConfig serviceConfig) {
		InvokeUnit iu = new InvokeUnit().setPathSplit(SERVICE_IMPL_CLASS_NAME);
		Element element = helper.getElement(iu);
		if (element == null) {
			serviceConfig.setServiceImplClassName(null);
		} else {
			String value = element.getText();
			serviceConfig.setServiceImplClassName(value);
		}
	}

	/**
	 * @param helper
	 * @param serviceConfig
	 */
	public void setServiceImplementationName(InputStream input, String svcImplName, IFile file) throws Exception {
		XMLHelper helper = new XMLHelper(input);
		IOUtils.closeQuietly(input);
		input = null;
		InvokeUnit iu = new InvokeUnit().setPathSplit(SERVICE_IMPL_CLASS_NAME);
		helper.replace(svcImplName, iu);
		helper.save(file);
	}

	
	/**
	 * @param helper
	 * @param serviceConfig
	 */
	private void populateServiceInterfaceName(XMLHelper helper,
			ServiceConfig serviceConfig) {
		InvokeUnit iu = new InvokeUnit().setPathSplit(SERVICE_INTERFACE_CLASS_NAME);
		String value = helper.getElement(iu).getText();
		serviceConfig.setServiceInterfaceClassName(value);
	}

	/**
	 * @param helper
	 * @param serviceConfig
	 */
	private void populateServiceName(XMLHelper helper,
			ServiceConfig serviceConfig) {
		InvokeUnit iu = new InvokeUnit();
		String value = helper.getElement(iu).getAttributeValue(SERVICE_NAME);
		serviceConfig.setServiceName(value);
	}
}
