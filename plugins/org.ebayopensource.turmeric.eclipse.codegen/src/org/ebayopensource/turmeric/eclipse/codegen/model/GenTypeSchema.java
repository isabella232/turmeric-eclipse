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

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * The Class GenTypeSchema.
 *
 * @author yayu
 */
public class GenTypeSchema extends BaseCodeGenModel {

	private String metadataDirectory; //-mdest Destination location for generated configuration and other XML files
	
	/**
	 * Instantiates a new gen type schema.
	 */
	public GenTypeSchema() {
		super();
		setGenType(GENTYPE_SCHEMA);
	}

	/**
	 * Instantiates a new gen type schema.
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
	public GenTypeSchema(String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory, String metadataDirectory) {
		super(GENTYPE_SCHEMA, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
		this.metadataDirectory = metadataDirectory;
	}

	/**
	 * Gets the metadata directory.
	 *
	 * @return the metadata directory
	 */
	public String getMetadataDirectory() {
		return metadataDirectory;
	}

	/**
	 * Sets the metadata directory.
	 *
	 * @param metadataDirectory the new metadata directory
	 */
	public void setMetadataDirectory(String metadataDirectory) {
		this.metadataDirectory = metadataDirectory;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = super.getCodeGenOptions();
		if (this.metadataDirectory != null)
			result.put(PARAM_MDEST, this.metadataDirectory);
		return result;
	}
}
