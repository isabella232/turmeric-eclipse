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

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;

/**
 * This gentype is intended to be called during the service creation. This is
 * equivalent to gen types: Client + Proxy
 * 
 * @author yayu
 * 
 */
public class GenTypeServiceFromWSDLIntf extends GenTypeClient {
	
	private static final SOALogger logger = SOALogger.getLogger();

	private String metaDir;

	/**
	 * 
	 */
	public GenTypeServiceFromWSDLIntf() {
		super();
		setGenType(GENTYPE_SERVICE_FROM_WSDL_INTF);
	}

	/**
	 * Instantiates a new gen type service from wsdl intf.
	 *
	 * @param namespace the namespace
	 * @param serviceLayerFile the service layer file
	 * @param serviceInterface the service interface
	 * @param serviceName the service name
	 * @param serviceVersion the service version
	 * @param serviceImpl the service impl
	 * @param projectRoot the project root
	 * @param serviceLayer the service layer
	 * @param sourceDirectory the source directory
	 * @param destination the destination
	 * @param outputDirectory the output directory
	 * @param generateFromWsdl the generate from wsdl
	 * @param genFolder the gen folder
	 * @param genInterfacePacakgeName the gen interface pacakge name
	 * @param genInterfaceClassName the gen interface class name
	 */
	public GenTypeServiceFromWSDLIntf(String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory,
			boolean generateFromWsdl, String genFolder,
			String genInterfacePacakgeName, String genInterfaceClassName) {
		super(GENTYPE_SERVICE_FROM_WSDL_INTF, namespace, serviceLayerFile,
				serviceInterface, serviceName, serviceVersion, serviceImpl,
				projectRoot, serviceLayer, sourceDirectory, destination,
				outputDirectory, generateFromWsdl, genFolder,
				genInterfacePacakgeName, genInterfaceClassName);

	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> options = super.getCodeGenOptions();
		
		String nonXSDFormat = this.getNonXSDFormats();
		// if (metaDir != null) {
		// options.put(PARAM_MDEST, metaDir);
		// }
		if (StringUtils.isNotBlank(nonXSDFormat) == true) {
			options.put(PARAM_NON_XSD_FORMATS, nonXSDFormat);
			logger.info("Calling codegen with option -nonXSDFormats="
					+ nonXSDFormat);
		} else {
			logger.info("Calling codegen without option -nonXSDFormats");
		}
		return options;
	}

	public String getMetaDir() {
		return metaDir;
	}

	public void setMetaDir(String metaDir) {
		this.metaDir = metaDir;
	}
}
