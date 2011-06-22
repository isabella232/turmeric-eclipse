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
 * Input object used for validation.
 *
 * @author ramurthy
 */

public class InputObject {
	
	private String value;
	
	private String errorMsg;
	
	private String pattern;
	
	/**
	 * Instantiates a new input object.
	 *
	 * @param value the value
	 * @param pattern the pattern
	 * @param errorMsg the error msg
	 */
	public InputObject(String value, String pattern, String errorMsg) {
		this.value = value;
		this.errorMsg = errorMsg;
		this.pattern = pattern;
	}	

	/**
	 * Gets the error msg.
	 *
	 * @return the error msg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}	
	
	/**
	 * Gets the pattern.
	 *
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
}
