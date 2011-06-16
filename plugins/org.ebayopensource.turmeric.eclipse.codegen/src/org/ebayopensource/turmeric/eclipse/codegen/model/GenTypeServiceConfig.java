/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author smathew
 * 
 * 
 */
public class GenTypeServiceConfig extends AbstractGenTypeGlobalConfig {
	private String serviceConfigGroup;

	public GenTypeServiceConfig() {
		super();
		setGenType(GENTYPE_SERVER_CONFIG);
	}

	public String getServiceConfigGroup() {
		return serviceConfigGroup;
	}

	public void setServiceConfigGroup(String serviceConfigGroup) {
		this.serviceConfigGroup = serviceConfigGroup;
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = super.getCodeGenOptions();
		if (StringUtils.isNotBlank(this.serviceConfigGroup))
			result.put(PARAM_SCGN, this.serviceConfigGroup);
		if (this.useExternalServiceFactory() == true) {
			result.remove(PARAM_SICN);
		}
		return result;
	}
}
