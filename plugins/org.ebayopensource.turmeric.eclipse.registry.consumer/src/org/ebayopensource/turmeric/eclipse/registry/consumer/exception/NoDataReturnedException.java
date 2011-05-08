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
 * No Data Returned Exception
 * Thrown when no data is returned from server. The client can re-throw  
 * this exception as ServerException
 * @author ramurthy
 */

public class NoDataReturnedException extends AbstractRegistryException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public NoDataReturnedException() {
		super();
	}

	/**
	 * Instantiates a new no data returned exception.
	 *
	 * @param message the message
	 */
	public NoDataReturnedException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new no data returned exception.
	 *
	 * @param throwable the throwable
	 */
	public NoDataReturnedException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Instantiates a new no data returned exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public NoDataReturnedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
