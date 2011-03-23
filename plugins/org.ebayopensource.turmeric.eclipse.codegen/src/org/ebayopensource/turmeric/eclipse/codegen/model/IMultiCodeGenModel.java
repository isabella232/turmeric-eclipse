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
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.Map;

/**
 * A code gen model that should be executed more than once
 * @author yayu
 *
 */
public interface IMultiCodeGenModel {
	public IMultiCodeGenModelIterator iterator();
	
	public static interface IMultiCodeGenModelIterator {
		public boolean hasNext();
		
		public Map<String, String> nextInputOptions();
	}
}
