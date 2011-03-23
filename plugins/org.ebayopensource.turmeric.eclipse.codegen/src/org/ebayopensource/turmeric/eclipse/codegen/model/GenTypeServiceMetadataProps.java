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
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * Simple Model class for type mappings generation. Clients might want to
 * generate the type mappings file multiple times, since changes in wsdl(related to types) might
 * make it invalid. Gentype used is "TypeMappings"
 * 
 * @author smathew
 * 
 */
public class GenTypeServiceMetadataProps extends BaseCodeGenModel {

	public GenTypeServiceMetadataProps() {
		super();
		setGenType(GENTYPE_SERVICE_METADATA_PROPS);
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		Map<String, String> result = new TreeMap<String, String>();
		if (getAdminName() != null)
			result.put(PARAM_ADMIN_NAME, getAdminName());
		if (getNamespace() != null)
			result.put(PARAM_NAMESPACE, getNamespace());
		if (getServiceLayerFile() != null)
			result.put(PARAM_SERVICE_LAYER_FILE, getServiceLayer());
		if (getProjectRoot() != null)
			result.put(PARAM_PR, getProjectRoot());
		if (getServiceName() != null)
			result.put(PARAM_SERVICE_NAME, getServiceName());
		if (getServiceImplClassName() != null){
			result.put(PARAM_SICN, getServiceImplClassName());
		}
		if (getServiceVersion() != null)
			result.put(PARAM_SCV, getServiceVersion());
		if (getServiceLayer() != null)
			result.put(PARAM_SLAYER, getServiceLayer());
		if (StringUtils.isNotBlank(getOriginalWsdlUrl())) {
			result.put(PARAM_WSDL, getOriginalWsdlUrl());
			if (getServiceInterface().contains("."))
				result.put(PARAM_GIP, StringUtils.substringBeforeLast(
						getServiceInterface(), "."));
			result.put(PARAM_GIN, StringUtils.substringAfterLast(
					getServiceInterface(), "."));

		} else {
			result.put(PARAM_INTERFACE, getServiceInterface());
		}
		result.put(PARAM_GENTYPE, getGenType());
		return result;

	}
}
