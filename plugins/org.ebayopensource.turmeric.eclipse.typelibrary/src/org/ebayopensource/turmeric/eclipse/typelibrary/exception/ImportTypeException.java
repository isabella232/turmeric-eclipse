/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.exception;

/**
 * The Class ImportTypeException.
 *
 * @author mzang
 * @since 1.0.0
 */
public class ImportTypeException extends Exception {

	private static final long serialVersionUID = 260L;

	/**
	 * Instantiates a new import type exception.
	 *
	 * @param message the message
	 */
	public ImportTypeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new import type exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ImportTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new import type exception.
	 *
	 * @param cause the cause
	 */
	public ImportTypeException(Throwable cause) {
		super(cause);
	}
	
	

}
