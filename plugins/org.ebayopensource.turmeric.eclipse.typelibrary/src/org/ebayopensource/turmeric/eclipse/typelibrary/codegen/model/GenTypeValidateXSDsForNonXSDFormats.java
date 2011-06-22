/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * Gen Type - validate if one or more xsd file are validate for non schema
 * protocols, such as protobuf
 * 
 * @author mzang
 * 
 */
public class GenTypeValidateXSDsForNonXSDFormats extends BaseCodeGenModel {

	private String xsdsPath = null;

	public String getXsdsPath() {
		return xsdsPath;
	}

	public void setXsdsPath(String xsdsPath) {
		this.xsdsPath = xsdsPath;
	}

	public GenTypeValidateXSDsForNonXSDFormats() {
		super();
		setGenType(GENTYPE_VALIDATEXSDSFORNONXSDFORMATES);
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		Map<String, String> options = super.getCodeGenOptions();
		options.put(BaseCodeGenModel.PARAM_XSD_PATHS_FOR_VALIDATION, xsdsPath);
		return options;
	}

}
