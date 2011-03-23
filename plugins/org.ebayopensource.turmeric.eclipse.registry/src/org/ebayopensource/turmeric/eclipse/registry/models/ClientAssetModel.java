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
package org.ebayopensource.turmeric.eclipse.registry.models;

/**
 * the model for client asset
 * @author yayu
 * @since 1.0.0
 */
public class ClientAssetModel {
	private String clientName;
	private String consumerId;

	/**
	 * 
	 */
	public ClientAssetModel() {
		super();
	}

	public ClientAssetModel(String clientName, String consumerId) {
		super();
		this.clientName = clientName;
		this.consumerId = consumerId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result
				+ ((consumerId == null) ? 0 : consumerId.hashCode());
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
		final ClientAssetModel other = (ClientAssetModel) obj;
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
		return true;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Client Name: ");
		buf.append(this.clientName);
		buf.append("\n");
		buf.append("Consumer ID: ");
		buf.append(this.consumerId);
		return buf.toString();
	}

}
