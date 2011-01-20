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
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.Map;

/**
 * This gentype is intended to be called during the service creation.
 * This is equivalent to gen types: Client + Proxy   
 * @author yayu
 *
 */
public class GenTypeServiceFromWSDLIntf extends GenTypeClient {

	/**
	 * 
	 */
	public GenTypeServiceFromWSDLIntf() {
		super();
		setGenType(GENTYPE_SERVICE_FROM_WSDL_INTF);
	}

	public GenTypeServiceFromWSDLIntf(String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory,
			boolean generateFromWsdl, String genFolder,
			String genInterfacePacakgeName, String genInterfaceClassName) {
		super(GENTYPE_SERVICE_FROM_WSDL_INTF, 
				namespace, serviceLayerFile, serviceInterface, serviceName,
				serviceVersion, serviceImpl, projectRoot, serviceLayer,
				sourceDirectory, destination, outputDirectory, generateFromWsdl,
				genFolder, genInterfacePacakgeName, genInterfaceClassName);
		
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> options = super.getCodeGenOptions();
		//options.remove(PARAM_PR);
		//options = GenTypeTypeMappings.addCommonTypesNSOption(options);
		return options;
	}
}
