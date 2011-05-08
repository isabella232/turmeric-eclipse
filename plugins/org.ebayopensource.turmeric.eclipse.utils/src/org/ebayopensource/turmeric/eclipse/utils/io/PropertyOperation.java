/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.io;

/**
 * The Class PropertyOperation.
 *
 * @author haozhou
 */
public abstract class PropertyOperation {
	
	/**
	 * Process.
	 *
	 * @param input the input
	 * @param key the key
	 * @param value the value
	 * @return the string
	 */
	public abstract String process (String input, String key, String value);
}
