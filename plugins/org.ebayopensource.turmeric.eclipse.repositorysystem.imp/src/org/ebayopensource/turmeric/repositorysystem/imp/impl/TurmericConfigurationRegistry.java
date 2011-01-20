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
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.config.core.SOAServiceConfiguration;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAConfigurationRegistry;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;


/**
 * @author yayu
 *
 */
public class TurmericConfigurationRegistry implements ISOAConfigurationRegistry {
	SOAServiceConfiguration serviceConfiguration = null;
	private String organizationName;

	/**
	 * 
	 */
	public TurmericConfigurationRegistry(String organizationName) {
		super();
		try {
			serviceConfiguration = SOAGlobalConfigAccessor
					.getServiceConfiguration(TurmericConstants.TURMERIC_SVC_CONFIG_FOLDER_NAME, 
							organizationName);
			this.organizationName = organizationName;
		} catch (IOException e) {
			SOALogger.getLogger().warning(e);
			// This exception is swallowed.
		}
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public String getBaseServiceRequestType() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getBaseRequestTypeName();
	}

	public String getBaseServiceResponseType() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getBaseResponseTypeName();
	}

	public String getBaseServiceRequestNameSpace() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getBaseRequestTypeNameSpace();
	}

	public String getBaseServiceResponseNameSpace() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getBaseResponseTypeNameSpace();
	}

	public String getTypesInWsdl() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getTypesInWSDL();
	}

	public String getClientConfigGroup() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getClientConfigGroup();
	}

	public String getServiceConfigGroup() {
		return serviceConfiguration == null ? null : serviceConfiguration
				.getServiceConfigGroup();
	}

	public String getEnvironmentMapperImpl() {
		return serviceConfiguration.getEnvMapperImpl();
	}

}
