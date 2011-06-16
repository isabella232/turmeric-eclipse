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
 * @author smathew
 * 
 * The Service Name and version-combined child model
 */
public class ServicesViewServiceModel extends ServicesViewBaseModel {

	private String serviceName;
	private String version;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ServicesViewServiceModel(String serviceName, String version) {
		this.serviceName = serviceName;
		this.version = version;
	}

	@Override
	public String toString() {
		String[] formattedArray = { UIConstants.SERVICES_VIEW_SERVICE,
				UIConstants.SERVICES_VIEW_COLON, " ", serviceName, " ",
				UIConstants.SERVICES_VIEW_VERSION, version };
		return StringUtils.join(formattedArray);
	}
	
	public static String getImageName() {
		return UIConstants.IMAGE_SCRIPT;
	}
}
