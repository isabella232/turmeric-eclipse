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
 * the model for client asset.
 *
 * @author yayu
 * @since 1.0.0
 */
public class ClientAssetModel {
	private String clientName;
	private String consumerId;

	/**
	 * Instantiates a new client asset model.
	 */
	public ClientAssetModel() {
		super();
	}

	/**
	 * Instantiates a new client asset model.
	 *
	 * @param clientName the client name
	 * @param consumerId the consumer id
	 */
	public ClientAssetModel(String clientName, String consumerId) {
		super();
		this.clientName = clientName;
		this.consumerId = consumerId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
