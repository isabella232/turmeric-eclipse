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
 * Abstract Registry Exception
 * This is the base class for all exceptions.
 *
 * @author ramurthy
 */

public abstract class AbstractRegistryException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AbstractRegistryException() {
		super();
	}

	/**
	 * Instantiates a new abstract registry exception.
	 *
	 * @param message the message
	 */
	public AbstractRegistryException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new abstract registry exception.
	 *
	 * @param throwable the throwable
	 */
	public AbstractRegistryException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Instantiates a new abstract registry exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public AbstractRegistryException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
