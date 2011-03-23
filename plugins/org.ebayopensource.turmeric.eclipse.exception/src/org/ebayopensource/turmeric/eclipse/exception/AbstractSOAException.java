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
package org.ebayopensource.turmeric.eclipse.exception;

/**
 * @author yayu
 * 
 */
public abstract class AbstractSOAException extends Exception {

	/**
	 * 
	 */
	public AbstractSOAException() {
		super();
	}

	/**
	 * @param message
	 */
	public AbstractSOAException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AbstractSOAException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AbstractSOAException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getMessage() {
		//We dont want to see "Null" in any dialog
		return super.getMessage() == null ? "" : super.getMessage();
	}
}
