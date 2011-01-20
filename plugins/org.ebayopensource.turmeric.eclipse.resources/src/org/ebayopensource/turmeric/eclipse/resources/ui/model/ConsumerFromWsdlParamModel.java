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
package org.ebayopensource.turmeric.eclipse.resources.ui.model;

import java.util.ArrayList;
import java.util.List;

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
	
	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
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
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}
	public boolean addEnvironment(String environment) {
		return this.environments.add(environment);
	}
	
}
