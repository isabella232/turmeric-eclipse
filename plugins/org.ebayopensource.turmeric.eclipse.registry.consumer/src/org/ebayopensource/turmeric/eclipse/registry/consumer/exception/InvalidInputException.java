/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.exception;


/**
 * Invalid Input Exception.
 *
 * @author ramurthy
 */

public class InvalidInputException extends AbstractRegistryException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public InvalidInputException() {
		super();
	}

	/**
	 * Instantiates a new invalid input exception.
	 *
	 * @param message the message
	 */
	public InvalidInputException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid input exception.
	 *
	 * @param throwable the throwable
	 */
	public InvalidInputException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Instantiates a new invalid input exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public InvalidInputException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
