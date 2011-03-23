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
 * GenType - cleanBuildTypeLibrary Model. From Codegen point of view this is a
 * clean build and it tries to build everything form the scratch. This class
 * does not have any attributes other than the inherited ones
 * 
 * @author smathew
 * 
 */
public class GenTypeCleanBuildTypeLibrary extends BaseTypeLibCodegenModel {

	public GenTypeCleanBuildTypeLibrary() {
		super();
		setGenType(GENTYPE_CLEANBUILDTYPELIBRARY);
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		// happy with BaseTypeLibCodegenModels functionality
		return super.getCodeGenOptions();
	}
}
