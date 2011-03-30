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
package org.ebayopensource.turmeric.eclipse.core.model.consumer;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;

/**
 * @author yayu
 *
 */
public class ConsumerFromWsdlParamModel extends ServiceFromWsdlParamModel {

	private String clientName;
	private String consumerId;
	private List<String> environments = new ArrayList<String>();
	/**
	 * 
	 */
	public ConsumerFromWsdlParamModel() {
		super();
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
	 * @return a List of Environments
	 */
	public List<String> getEnvironments() {
		return environments;
	}
	
	/**
	 * 
	 * @param environments List environments 
	 */
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}
	
	/**
	 * 
	 * @param environment the environment
	 * @return whether the environement was succssefully wadded.
	 */
	public boolean addEnvironment(String environment) {
		return this.environments.add(environment);
	}
	
}
