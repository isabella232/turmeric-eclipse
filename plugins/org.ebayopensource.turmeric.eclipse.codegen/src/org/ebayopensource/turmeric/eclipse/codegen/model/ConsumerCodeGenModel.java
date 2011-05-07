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
 * The Class ConsumerCodeGenModel.
 *
 * @author yayu
 */
public class ConsumerCodeGenModel extends BaseCodeGenModel{
	private String clientName;
	private String consumerId;
	private String clientConfigGroup;
	private Map<String, Map<String, String>> requiredServices = new TreeMap<String, Map<String, String>>();
	
	/**
	 * Instantiates a new consumer code gen model.
	 */
	public ConsumerCodeGenModel() {
		super();
	}

	/**
	 * Instantiates a new consumer code gen model.
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
	public ConsumerCodeGenModel(String genType, String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory) {
		super(genType, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
	}

	/**
	 * Gets the required services.
	 *
	 * @return the required services
	 */
	public Map<String, Map<String, String>> getRequiredServices() {
		return requiredServices;
	}

	/**
	 * Sets the required services.
	 *
	 * @param requiredServices the required services
	 */
	public void setRequiredServices(Map<String, Map<String, String>> requiredServices) {
		this.requiredServices = requiredServices;
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
	 * Gets the consumer id.
	 *
	 * @return the consumer id
	 */
	public String getConsumerId() {
		return consumerId;
	}

	/**
	 * Sets the consumer id.
	 *
	 * @param consumerId the new consumer id
	 */
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	/**
	 * Gets the client config group.
	 *
	 * @return the client config group
	 */
	public String getClientConfigGroup() {
		return clientConfigGroup;
	}

	/**
	 * Sets the client config group.
	 *
	 * @param clientConfigGroup the new client config group
	 */
	public void setClientConfigGroup(String clientConfigGroup) {
		this.clientConfigGroup = clientConfigGroup;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
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
	 * the key is the service name.
	 *
	 * @param requiredServices the required services
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
