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

/**
 * @author yayu
 *
 */
public class GenTypeGlobalServerConfig extends AbstractGenTypeGlobalConfig {

	
	/**
	 * 
	 */
	public GenTypeGlobalServerConfig() {
		super();
		setGenType(GENTYPE_GLOBAL_SERVER_CONFIG);
	}

	/**
	 * @param namespace
	 * @param serviceLayerFile
	 * @param serviceInterface
	 * @param serviceName
	 * @param serviceVersion
	 * @param serviceImpl
	 * @param projectRoot
	 * @param serviceLayer
	 * @param sourceDirectory
	 * @param destination
	 * @param outputDirectory
	 */
	public GenTypeGlobalServerConfig(String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory, String metadataDirectory) {
		super(GENTYPE_GLOBAL_SERVER_CONFIG, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory, metadataDirectory);
	}

	

}
