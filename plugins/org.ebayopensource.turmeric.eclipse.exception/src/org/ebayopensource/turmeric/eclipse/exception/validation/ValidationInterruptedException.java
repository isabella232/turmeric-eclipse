/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.validation;

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;

/**
 * 
 * Thrown when the validation logic is interrupted. A Class cast or a Null
 * argument or an invalid state all fall into this category
 * 
 * @author smathew
 * 
 */
public class ValidationInterruptedException extends AbstractSOAException {


	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new validation interrupted exception.
	 */
	public ValidationInterruptedException() {
		super();
	}

	/**
	 * Instantiates a new validation interrupted exception.
	 *
	 * @param message the message
	 */
	public ValidationInterruptedException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new validation interrupted exception.
	 *
	 * @param cause the cause
	 */
	public ValidationInterruptedException(Throwable cause) {
		super(cause);
	}
}
