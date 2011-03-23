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
	 * Constructor
	 */
	public NoDataReturnedException() {
		super();
	}

	/**
	 * @param message
	 */
	public NoDataReturnedException(String message) {
		super(message);
	}

	/**
	 * @param throwable
	 */
	public NoDataReturnedException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public NoDataReturnedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
