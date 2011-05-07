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
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * Simple Model class for web xml generation. Gentype used is "WebXml"
 * 
 * @author smathew
 * 
 */
public class GenTypeWebXml extends BaseCodeGenModel {

	private String metaDir;

	/**
	 * Instantiates a new gen type web xml.
	 */
	public GenTypeWebXml() {
		super();
		setGenType(GENTYPE_WEB_XML);
	}

	/**
	 * Gets the meta dir.
	 *
	 * @return the meta dir
	 */
	public String getMetaDir() {
		return metaDir;
	}

	/**
	 * Sets the meta dir.
	 *
	 * @param metaDir the new meta dir
	 */
	public void setMetaDir(String metaDir) {
		this.metaDir = metaDir;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		Map<String, String> result = super.getCodeGenOptions();
		if (!StringUtils.isEmpty(getMetaDir()))
			result.put(PARAM_MDEST, getMetaDir());
		return result;
	}

}
