/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.ui.UIConstants;


/**
 * The Class ServicesViewServiceModel.
 *
 * @author smathew
 * 
 * The Service Name and version-combined child model
 */
public class ServicesViewServiceModel extends ServicesViewBaseModel {

	private String serviceName;
	private String version;

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Instantiates a new services view service model.
	 *
	 * @param serviceName the service name
	 * @param version the version
	 */
	public ServicesViewServiceModel(String serviceName, String version) {
		this.serviceName = serviceName;
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String[] formattedArray = { UIConstants.SERVICES_VIEW_SERVICE,
				UIConstants.SERVICES_VIEW_COLON, " ", serviceName, " ",
				UIConstants.SERVICES_VIEW_VERSION, version };
		return StringUtils.join(formattedArray);
	}
	
	/**
	 * Gets the image name.
	 *
	 * @return the image name
	 */
	public static String getImageName() {
		return UIConstants.IMAGE_SCRIPT;
	}
}
