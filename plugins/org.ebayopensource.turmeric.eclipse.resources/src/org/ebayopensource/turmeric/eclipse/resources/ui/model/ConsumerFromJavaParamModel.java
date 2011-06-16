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

	/**
	 * 
	 * @return the parent directory
	 */
	public String getParentDirectory() {
		return parentDirectory;
	}

	/**
	 * 
	 * @param parentDirectory parent directory
	 */
	public void setParentDirectory(String parentDirectory) {
		this.parentDirectory = parentDirectory;
	}

	/**
	 * 
	 * @return the consumer id
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
	 * @return the base consumer source directory
	 */
	public String getBaseConsumerSrcDir() {
		return baseConsumerSrcDir;
	}

	/**
	 * 
	 * @param baseConsumerSrcDir the base consumer source directory
	 */
	public void setBaseConsumerSrcDir(String baseConsumerSrcDir) {
		this.baseConsumerSrcDir = baseConsumerSrcDir;
	}
	
	/**
	 * 
	 * @return the service name
	 */

	public List<String> getServiceNames() {
		return serviceNames;
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
	 * @param clientName the client name
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * 
	 * @return the deployment environments
	 */
	public List<String> getEnvironments() {
		return environments;
	}
	
	/**
	 * 
	 * @param environment a deployment environment
	 * @return whether the enviroment was added successfully
	 */
	public boolean addEnvironment(String environment) {
		return environments.add(environment);
	}

	/**
	 * 
	 * @param environments a List of deployment environments
	 */
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	/**
	 * 
	 * @param serviceNames a List of service names
	 */
	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	/**
	 * 
	 * @return whether the project is being converted or not.
	 */
	public boolean isConvertingJavaProject() {
		return convertingJavaProject;
	}

	/**
	 * 
	 * @param convertingJavaProject sets the status of the conversion
	 */
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
