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
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;


/**
 * This gentype is intended to be called during the service creation.
 * This is equivalent to gen types: SISkeleton + UnitTest + WebXml + Dispatcher + ServiceOpProps
 * @author yayu
 *
 */
public class GenTypeServiceFromWSDLImpl extends BaseCodeGenModel {
	// Java source files
	private String clientName;
	private String serviceConfigGroup;
	private String metaDir;
	
	/**
	 * Instantiates a new gen type service from wsdl impl.
	 */
	public GenTypeServiceFromWSDLImpl() {
		super();
		setGenType(GENTYPE_SERVICE_FROM_WSDL_IMPL);
	}

	/**
	 * Instantiates a new gen type service from wsdl impl.
	 *
	 * @param genType the gen type
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
	public GenTypeServiceFromWSDLImpl(String genType, String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory) {
		super(GENTYPE_SERVICE_FROM_WSDL_IMPL, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
	}

	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Sets the client name.
	 *
	 * @param clientName the new client name
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	/**
	 * Gets the meta dir.
	 *
	 * @return the meta dir
	 */
	public String getMetaDir() {
		return metaDir;
	}

	/**
	 * Sets the meta dir.
	 *
	 * @param metaDir the new meta dir
	 */
	public void setMetaDir(String metaDir) {
		this.metaDir = metaDir;
	}

	/**
	 * Gets the service config group.
	 *
	 * @return the service config group
	 */
	public String getServiceConfigGroup() {
		return serviceConfigGroup;
	}

	/**
	 * Sets the service config group.
	 *
	 * @param serviceConfigGroup the new service config group
	 */
	public void setServiceConfigGroup(String serviceConfigGroup) {
		this.serviceConfigGroup = serviceConfigGroup;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = new HashMap<String, String>();
		//SI Skeleton
		result.put(PARAM_GENTYPE, getGenType());
		result.put(PARAM_NAMESPACE, getNamespace());
		result.put(PARAM_PR, getProjectRoot());
		/*if (getOriginalWsdlUrl() != null) {
			result.put(PARAM_WSDL, getOriginalWsdlUrl());
			if (getServiceInterface().contains("."))
				result.put(PARAM_GIP, StringUtils.substringBeforeLast(
						getServiceInterface(), "."));
		} else */
		result.put(PARAM_INTERFACE, getServiceInterface());
		if (getAdminName() != null) {
			result.put(PARAM_ADMIN_NAME, getAdminName());
			if (getAdminName().equals(getServiceName()) == false) {
				//we only set environment if admin name is not same as service name, 
				//which means it is not pre-2.4 projects
				result.put(PARAM_ENVIRONMENT, 
						SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT);
			}
		}
		result.put(PARAM_SERVICE_NAME, getServiceName());
		result.put(PARAM_SCV, getServiceVersion());
		result.put(PARAM_SRC, getSourceDirectory());
		result.put(PARAM_DEST, getDestination());
		result.put(PARAM_BIN, getOutputDirectory());
		if (getServiceImplClassName() != null && this.useExternalServiceFactory() == false) {
			result.put(PARAM_SICN, getServiceImplClassName());
		}
		
		if (StringUtils.isNotBlank(this.serviceConfigGroup))
			result.put(PARAM_SCGN, this.serviceConfigGroup);
		
		//Unit Test		
		result.put(PARAM_CN, getClientName());
		result.put(PARAM_GT, null);

		if (StringUtils.isNotBlank(getGenFolder()))
			result.put(PARAM_JDEST, getGenFolder());
		
		//Web Xml
		/*if(!StringUtils.isEmpty(getMetaDir()))
			result.put(PARAM_MDEST,getMetaDir());*/
		return result;
	}

}
