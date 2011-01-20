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

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;


/**
 * @author yayu
 *
 */
public class SOAConsumerMetadata extends AbstractSOAMetadata{

//	private SOAIntfMetadata intfMetadata;
	private String consumerId;
	private String baseConsumerSrcDir;
	private String envMapper;
	//The name used for storing ClientConfig.xml files
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
	
	public static SOAConsumerMetadata create(String clientName, String baseConsumerSrcDir) {
		final SOAConsumerMetadata metadata = new SOAConsumerMetadata();
		metadata.setClientName(clientName);
		metadata.setBaseConsumerSrcDir(baseConsumerSrcDir);
		return metadata;
	}

	public static SOAConsumerMetadata create(ConsumerFromWsdlParamModel paramModel) {
		SOAConsumerMetadata metadata = new SOAConsumerMetadata();
//		metadata.setServiceImpl(paramModel.getServiceImpl());
		metadata.setClientName(paramModel.getClientName());
		metadata.setConsumerId(paramModel.getConsumerId());
		metadata.setBaseConsumerSrcDir(paramModel.getBaseConsumerSrcDir());
		metadata.setEnvironments(paramModel.getEnvironments());
		metadata.setServiceNames(ListUtil.arrayList(paramModel.getServiceName()));
		metadata.setSourceType(SOAProjectConstants.ConsumerSourceType.WSDL);
		return metadata;
	}
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
	
	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER;
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

	public String getBaseConsumerSrcDir() {
		return baseConsumerSrcDir;
	}

	public void setBaseConsumerSrcDir(String baseConsumerSrcDir) {
		this.baseConsumerSrcDir = baseConsumerSrcDir;
	}

	public List<String> getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	public List<String> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	public SOAProjectConstants.ConsumerSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOAProjectConstants.ConsumerSourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getEnvMapper() {
		return envMapper;
	}

	public void setEnvMapper(String envMapper) {
		this.envMapper = envMapper;
	}
}
