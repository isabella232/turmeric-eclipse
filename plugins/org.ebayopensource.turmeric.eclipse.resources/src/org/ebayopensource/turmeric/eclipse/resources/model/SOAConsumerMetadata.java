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
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.model.consumer.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;


/**
 * The Class SOAConsumerMetadata.
 *
 * @author yayu
 */
public class SOAConsumerMetadata extends AbstractSOAMetadata{

	private String consumerId;
	private String baseConsumerSrcDir;
	private String envMapper;
	private boolean isZeroConfig = false;
	private String superVersion;
	
	/**
	 * The name used for storing ClientConfig.xml files
	 */
	private String clientName;
	private SOAProjectConstants.ConsumerSourceType sourceType;
	private List<String> serviceNames = new ArrayList<String>();
	private List<String> environments = new ArrayList<String>();
	
	/**
	 * Instantiates a new sOA consumer metadata.
	 */
	public SOAConsumerMetadata() {
		super();
	}
	
	/**
	 * 
	 * @param clientName 
	 * @param baseConsumerSrcDir 
	 * @return the created SOAConsumerMetadata instance
	 */
	
	public static SOAConsumerMetadata create(String clientName, String baseConsumerSrcDir) {
		final SOAConsumerMetadata metadata = new SOAConsumerMetadata();
		metadata.setClientName(clientName);
		metadata.setBaseConsumerSrcDir(baseConsumerSrcDir);
		return metadata;
	}

	/**
	 * 
	 * @param paramModel 
	 * @return the created SOAConsumerMetaData instance
	 */
	public static SOAConsumerMetadata create(ConsumerFromWsdlParamModel paramModel) {
		SOAConsumerMetadata metadata = new SOAConsumerMetadata();
		metadata.setClientName(paramModel.getClientName());
		metadata.setConsumerId(paramModel.getConsumerId());
		metadata.setBaseConsumerSrcDir(paramModel.getBaseConsumerSrcDir());
		metadata.setEnvironments(paramModel.getEnvironments());
		metadata.setServiceNames(ListUtil.arrayList(paramModel.getServiceName()));
		metadata.setSourceType(SOAProjectConstants.ConsumerSourceType.WSDL);
		metadata.setZeroConfig(paramModel.getEnvironments().isEmpty());
		return metadata;
	}
	
	/**
	 * 
	 * @param paramModel 
	 * @return the created SOAConsumerMetadata instance
	 */
	public static SOAConsumerMetadata create(ConsumerFromJavaParamModel paramModel) {
		SOAConsumerMetadata metadata = new SOAConsumerMetadata();
		metadata.setClientName(paramModel.getClientName());
		metadata.setConsumerId(paramModel.getConsumerId());
		metadata.setBaseConsumerSrcDir(paramModel.getBaseConsumerSrcDir());
		metadata.setServiceNames(paramModel.getServiceNames());
		metadata.setEnvironments(paramModel.getEnvironments());
		metadata.setSourceType(SOAProjectConstants.ConsumerSourceType.JAVA);
		return metadata;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isZeroConfig() {
		return isZeroConfig;
	}

	public void setZeroConfig(boolean isZeroConfig) {
		this.isZeroConfig = isZeroConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER;
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
	 * @return the Consumer Id
	 */
	public String getConsumerId() {
		return consumerId;
	}

	/**
	 * Sets the consumer id.
	 *
	 * @param consumerId the consumer id
	 */
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	/**
	 * Gets the base consumer src dir.
	 *
	 * @return the base consumer src directory
	 */
	public String getBaseConsumerSrcDir() {
		return baseConsumerSrcDir;
	}

	/**
	 * Sets the base consumer src dir.
	 *
	 * @param baseConsumerSrcDir the new base consumer src dir
	 */
	public void setBaseConsumerSrcDir(String baseConsumerSrcDir) {
		this.baseConsumerSrcDir = baseConsumerSrcDir;
	}

	/**
	 * Gets the service names.
	 *
	 * @return a list of service names
	 */
	public List<String> getServiceNames() {
		return serviceNames;
	}

	/**
	 * Sets the service names.
	 *
	 * @param serviceNames the new service names
	 */
	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	/**
	 * Gets the environments.
	 *
	 * @return A list of environements
	 */
	public List<String> getEnvironments() {
		return environments;
	}

	/**
	 * Sets the environments.
	 *
	 * @param environments the new environments
	 */
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	/**
	 * Gets the source type.
	 *
	 * @return a Consumer Source Type
	 */
	public SOAProjectConstants.ConsumerSourceType getSourceType() {
		return sourceType;
	}

	/**
	 * Sets the source type.
	 *
	 * @param sourceType the new source type
	 */
	public void setSourceType(SOAProjectConstants.ConsumerSourceType sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * Gets the env mapper.
	 *
	 * @return the environment mapper name
	 */
	public String getEnvMapper() {
		return envMapper;
	}

	/**
	 * Sets the env mapper.
	 *
	 * @param envMapper the new env mapper
	 */
	public void setEnvMapper(String envMapper) {
		this.envMapper = envMapper;
	}

	@Override
	public String getSuperVersion() {
		// TODO Auto-generated method stub
		return superVersion;
	}
	public void setSuperVersion(String superVersion){
		this.superVersion=superVersion;
	}
}
