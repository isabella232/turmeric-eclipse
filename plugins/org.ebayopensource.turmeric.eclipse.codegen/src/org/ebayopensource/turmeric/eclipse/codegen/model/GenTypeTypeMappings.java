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

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * Simple Model class for type mappings generation. Clients might want to
 * generate the type mappings file multiple times, since changes in wsdl(related to types) might
 * make it invalid. Gentype used is "TypeMappings"
 * 
 * @author smathew
 * 
 */
public class GenTypeTypeMappings extends BaseCodeGenModel {

	public GenTypeTypeMappings() {
		super();
		setGenType(GENTYPE_TYPE_MAPPINGS);
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		Map<String, String> options = super.getCodeGenOptions();
		return options;
	}

}
