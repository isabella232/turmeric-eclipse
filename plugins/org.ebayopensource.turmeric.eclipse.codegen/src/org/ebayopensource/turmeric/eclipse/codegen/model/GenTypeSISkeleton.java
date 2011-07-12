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


import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * The Class GenTypeSISkeleton.
 * 
 * @author yayu
 * 
 */
public class GenTypeSISkeleton extends BaseCodeGenModel {
	private boolean overwriteImplClass = false;

	/**
	 * Instantiates a new gen type si skeleton.
	 */
	public GenTypeSISkeleton() {
		super();
		setGenType(GENTYPE_SISKELETON);
	}

	/**
	 * Instantiates a new gen type si skeleton.
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
	 */
	public GenTypeSISkeleton(String namespace, String serviceLayerFile,
			String serviceInterface, String serviceName, String serviceVersion,
			String serviceImpl, String projectRoot, String serviceLayer,
			String sourceDirectory, String destination, String outputDirectory) {
		super(GENTYPE_SISKELETON, namespace, serviceLayerFile,
				serviceInterface, serviceName, serviceVersion, serviceImpl,
				projectRoot, serviceLayer, sourceDirectory, destination,
				outputDirectory);
	}
	

	/**
	 * Checks if is overwrite impl class.
	 *
	 * @return true, if is overwrite impl class
	 */
	public boolean isOverwriteImplClass() {
		return overwriteImplClass;
	}

	/**
	 * Sets the overwrite impl class.
	 *
	 * @param overwriteImplClass the new overwrite impl class
	 */
	public void setOverwriteImplClass(boolean overwriteImplClass) {
		this.overwriteImplClass = overwriteImplClass;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = new HashMap<String, String>();
		result.put(PARAM_GENTYPE, getGenType());
		result.put(PARAM_NAMESPACE, getNamespace());
		result.put(PARAM_INTERFACE, getServiceInterface());
		result.put(PARAM_SERVICE_NAME, getServiceName());
		result.put(PARAM_SCV, getServiceVersion());
		result.put(PARAM_SRC, getSourceDirectory());
		result.put(PARAM_DEST, getDestination());
		result.put(PARAM_BIN, getOutputDirectory());
		if (getServiceImplClassName() != null) {
			result.put(PARAM_SICN, getServiceImplClassName());
		}
		result.put(PARAM_GSS, null); //This would ensure the impl class will always ben re-generated
		if (overwriteImplClass)
			result.put(PARAM_OWIC, null);
		return result;
	}

}
