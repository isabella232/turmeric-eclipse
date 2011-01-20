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

/**
 * Gen Type - addType Model. This needs nothing extra than the
 * BaseTypeLibCodegenModel attributes
 * 
 * @author smathew
 * 
 */
public class GenTypeAddType extends BaseTypeLibCodegenModel {

	public GenTypeAddType() {
		super();
		setGenType(GENTYPE_ADDTYPE);
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		// happy with BaseTypeLibCodegenModels functionality
		return super.getCodeGenOptions();
	}

}
