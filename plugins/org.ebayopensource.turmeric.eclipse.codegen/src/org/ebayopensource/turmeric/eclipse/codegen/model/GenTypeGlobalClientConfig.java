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


// TODO: Auto-generated Javadoc
/**
 * The Class GenTypeGlobalClientConfig.
 *
 * @author yayu
 */
public class GenTypeGlobalClientConfig extends AbstractGenTypeGlobalConfig {

	/**
	 * Instantiates a new gen type global client config.
	 */
	public GenTypeGlobalClientConfig() {
		super();
		setGenType(GENTYPE_GLOBAL_CLIENT_CONFIG);
	}

	/**
	 * Instantiates a new gen type global client config.
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
	 * @param metadataDirectory the metadata directory
	 */
	public GenTypeGlobalClientConfig(String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory, String metadataDirectory) {
		super(GENTYPE_GLOBAL_CLIENT_CONFIG, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory, metadataDirectory);
	}
}
