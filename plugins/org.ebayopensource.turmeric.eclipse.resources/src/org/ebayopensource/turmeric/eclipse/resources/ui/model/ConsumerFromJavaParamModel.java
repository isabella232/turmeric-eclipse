/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.ui.model;

import java.util.List;

/**
 * @author smathew
 * UI Model for Consumer from java FLow
 */
public class ConsumerFromJavaParamModel {
	
	private String parentDirectory;
	private String baseConsumerSrcDir;
	private String clientName;
	private String consumerId;
	private List<String> environments;
	private List<String> serviceNames; 
	private boolean convertingJavaProject = false;
	
	/**
	 * 
	 */
	public ConsumerFromJavaParamModel() {
		super();
	}

	public String getParentDirectory() {
		return parentDirectory;
	}

	public void setParentDirectory(String parentDirectory) {
		this.parentDirectory = parentDirectory;
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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<String> getEnvironments() {
		return environments;
	}
	
	public boolean addEnvironment(String environment) {
		return environments.add(environment);
	}

	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	public boolean isConvertingJavaProject() {
		return convertingJavaProject;
	}

	public void setConvertingJavaProject(boolean convertingJavaProject) {
		this.convertingJavaProject = convertingJavaProject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((baseConsumerSrcDir == null) ? 0 : baseConsumerSrcDir
						.hashCode());
		result = prime * result
				+ ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result
				+ ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result
				+ ((environments == null) ? 0 : environments.hashCode());
		result = prime * result
				+ ((parentDirectory == null) ? 0 : parentDirectory.hashCode());
		result = prime * result
				+ ((serviceNames == null) ? 0 : serviceNames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ConsumerFromJavaParamModel other = (ConsumerFromJavaParamModel) obj;
		if (baseConsumerSrcDir == null) {
			if (other.baseConsumerSrcDir != null)
				return false;
		} else if (!baseConsumerSrcDir.equals(other.baseConsumerSrcDir))
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		if (environments == null) {
			if (other.environments != null)
				return false;
		} else if (!environments.equals(other.environments))
			return false;
		if (parentDirectory == null) {
			if (other.parentDirectory != null)
				return false;
		} else if (!parentDirectory.equals(other.parentDirectory))
			return false;
		if (serviceNames == null) {
			if (other.serviceNames != null)
				return false;
		} else if (!serviceNames.equals(other.serviceNames))
			return false;
		return true;
	}

	
}
