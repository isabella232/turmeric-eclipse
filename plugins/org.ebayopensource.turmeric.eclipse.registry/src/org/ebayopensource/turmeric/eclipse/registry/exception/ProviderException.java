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
package org.ebayopensource.turmeric.eclipse.registry.exception;

/**
 * The Class ProviderException.
 *
 * @author yayu
 */
public class ProviderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 230L;

	/**
	 * Instantiates a new provider exception.
	 */
	public ProviderException() {
		super();
	}

	/**
	 * Instantiates a new provider exception.
	 *
	 * @param message the message
	 */
	public ProviderException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new provider exception.
	 *
	 * @param cause the cause
	 */
	public ProviderException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new provider exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ProviderException(String message, Throwable cause) {
		super(message, cause);
	}

}
