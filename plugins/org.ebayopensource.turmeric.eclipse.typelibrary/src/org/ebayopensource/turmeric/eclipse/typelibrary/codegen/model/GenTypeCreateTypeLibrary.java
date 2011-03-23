/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model;

/**
 * GenType - Create New Type Library. From Codegen point, this creates a new
 * type information xml.
 * No other attributes other than inherited ones
 * @author smathew
 * 
 */
public class GenTypeCreateTypeLibrary extends BaseTypeLibCodegenModel {

	public GenTypeCreateTypeLibrary() {
		super();
		setGenType(GENTYPE_CREATETYPELIBRARY);
	}
}
