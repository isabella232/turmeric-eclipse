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
 * @author yayu
 *
 */
public class SOAConsumerMetadata extends AbstractSOAMetadata{

	private String consumerId;
	private String baseConsumerSrcDir;
	private String envMapper;
	
	/**
	 * The name used for storing ClientConfig.xml files
	 */
	private String clientName;
	private SOAProjectConstants.ConsumerSourceType sourceType;
	private List<String> serviceNames = new ArrayList<String>();
	private List<String> environments = new ArrayList<String>();
	
	/**
	 * 
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
	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER;
	}

	/**
	 * 
	 * @return the client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * 
	 * @param clientName 
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * 
	 * @return the Consumer Id
	 */
	public String getConsumerId() {
		return consumerId;
	}

	/**
	 * 
	 * @param consumerId the consumer id
	 */
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	/**
	 * 
	 * @return the base consumer src directory
	 */
	public String getBaseConsumerSrcDir() {
		return baseConsumerSrcDir;
	}

	/**
	 * 
	 * @param baseConsumerSrcDir 
	 */
	public void setBaseConsumerSrcDir(String baseConsumerSrcDir) {
		this.baseConsumerSrcDir = baseConsumerSrcDir;
	}

	/**
	 * 
	 * @return a list of service names
	 */
	public List<String> getServiceNames() {
		return serviceNames;
	}

	/**
	 * 
	 * @param serviceNames 
	 */
	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	/**
	 * 
	 * @return A list of environements
	 */
	public List<String> getEnvironments() {
		return environments;
	}

	/**
	 * 
	 * @param environments 
	 */
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	/**
	 * 
	 * @return a Consumer Source Type
	 */
	public SOAProjectConstants.ConsumerSourceType getSourceType() {
		return sourceType;
	}

	/**
	 * 
	 * @param sourceType 
	 */
	public void setSourceType(SOAProjectConstants.ConsumerSourceType sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * 
	 * @return the environment mapper name
	 */
	public String getEnvMapper() {
		return envMapper;
	}

	/**
	 * 
	 * @param envMapper 
	 */
	public void setEnvMapper(String envMapper) {
		this.envMapper = envMapper;
	}
}
