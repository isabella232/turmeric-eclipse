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
 * GenType - IncrBuildType. From Codegen point, it expect a list of modified
 * xsds, and builds only the list passed
 * 
 * @author smathew
 * 
 */
public class GenTypeIncrBuildTypeLibrary extends BaseTypeLibCodegenModel {

	public GenTypeIncrBuildTypeLibrary() {
		super();
		setGenType(GENTYPE_INCRBUILDTYPELIBRARY);
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		// happy with BaseTypeLibCodegenModels functionality
		return super.getCodeGenOptions();
	}
}
