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
 * The Class AbstractSOAException.
 *
 * @author yayu
 */
public abstract class AbstractSOAException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5961214511800810906L;

	/**
	 * Instantiates a new abstract soa exception.
	 */
	public AbstractSOAException() {
		super();
	}

	/**
	 * Instantiates a new abstract soa exception.
	 *
	 * @param message the message
	 */
	public AbstractSOAException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new abstract soa exception.
	 *
	 * @param cause the cause
	 */
	public AbstractSOAException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new abstract soa exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AbstractSOAException(String message, Throwable cause) {
		super(message, cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		//We dont want to see "Null" in any dialog
		return super.getMessage() == null ? "" : super.getMessage();
	}
}
