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
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;


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
	 * 
	 */
	public GenTypeServiceFromWSDLImpl() {
		super();
		setGenType(GENTYPE_SERVICE_FROM_WSDL_IMPL);
	}

	/**
	 * @param genType
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
	public GenTypeServiceFromWSDLImpl(String genType, String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory) {
		super(GENTYPE_SERVICE_FROM_WSDL_IMPL, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getMetaDir() {
		return metaDir;
	}

	public void setMetaDir(String metaDir) {
		this.metaDir = metaDir;
	}

	public String getServiceConfigGroup() {
		return serviceConfigGroup;
	}

	public void setServiceConfigGroup(String serviceConfigGroup) {
		this.serviceConfigGroup = serviceConfigGroup;
	}

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
		if (getServiceImplClassName() != null) {
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
