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
 * Assertions Service Exception
 * All exceptions related to Assertions Service. The client can re-throw  
 * this exception as ServerException
 * @author ramurthy
 */

public class AssertionsServiceException extends AbstractRegistryException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public AssertionsServiceException() {
		super();
	}

	/**
	 * Instantiates a new assertions service exception.
	 *
	 * @param message the message
	 */
	public AssertionsServiceException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new assertions service exception.
	 *
	 * @param throwable the throwable
	 */
	public AssertionsServiceException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Instantiates a new assertions service exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public AssertionsServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
		
}
