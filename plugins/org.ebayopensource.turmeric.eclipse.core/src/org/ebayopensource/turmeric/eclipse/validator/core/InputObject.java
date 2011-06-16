/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.core;

/**
 * Input object used for validation
 * @author ramurthy
 *
 */

public class InputObject {
	
	private String value;
	
	private String errorMsg;
	
	private String pattern;
	
	public InputObject(String value, String pattern, String errorMsg) {
		this.value = value;
		this.errorMsg = errorMsg;
		this.pattern = pattern;
	}	

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getValue() {
		return value;
	}	
	
	public String getPattern() {
		return pattern;
	}
}
