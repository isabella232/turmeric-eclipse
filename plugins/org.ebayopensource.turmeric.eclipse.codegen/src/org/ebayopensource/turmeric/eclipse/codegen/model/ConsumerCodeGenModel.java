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
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;


/**
 * @author yayu
 *
 */
public class ConsumerCodeGenModel extends BaseCodeGenModel{
	private String clientName;
	private String consumerId;
	private String clientConfigGroup;
	private Map<String, Map<String, String>> requiredServices = new TreeMap<String, Map<String, String>>();
	
	/**
	 * 
	 */
	public ConsumerCodeGenModel() {
		super();
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
	public ConsumerCodeGenModel(String genType, String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory) {
		super(genType, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
	}

	public Map<String, Map<String, String>> getRequiredServices() {
		return requiredServices;
	}

	public void setRequiredServices(Map<String, Map<String, String>> requiredServices) {
		this.requiredServices = requiredServices;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getClientConfigGroup() {
		return clientConfigGroup;
	}

	public void setClientConfigGroup(String clientConfigGroup) {
		this.clientConfigGroup = clientConfigGroup;
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = super.getCodeGenOptions();
		if (this.clientName != null) {
			result.put(PARAM_CN, this.clientName);
		}
		if (StringUtils.isNotBlank(this.consumerId)) {
			result.put(PARAM_CONSUMER_ID, this.consumerId);
		}
		if (StringUtils.isNotBlank(this.clientConfigGroup)) {
			result.put(PARAM_CCGN, this.clientConfigGroup);
		}
		return result;
	}

	/**
	 * the key is the service name
	 * @param requiredServices
	 */
	public void setRequiredServicesMetadata(Map<String, SOAIntfMetadata> requiredServices) {
		for (SOAIntfMetadata metadata : requiredServices.values()) {
			final Map<String, String> data = new ConcurrentHashMap<String, String>();
			data.put(BaseCodeGenModel.PARAM_SERVICE_NAME,
					metadata.getServiceName());
			data.put(BaseCodeGenModel.PARAM_INTERFACE, 
					metadata.getServiceInterface());
			data.put(BaseCodeGenModel.PARAM_SLAYER, 
					metadata.getServiceLayer());
			data.put(BaseCodeGenModel.PARAM_SCV, 
					metadata.getServiceVersion());
			data.put(BaseCodeGenModel.PARAM_NAMESPACE,
					metadata.getTargetNamespace());
			if (metadata.getServiceLocation() != null)
				data.put(BaseCodeGenModel.PARAM_SL, metadata.getServiceLocation());
			getRequiredServices().put(metadata.getServiceName(), data);
		}
		
	}

}
