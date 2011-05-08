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
 * The Class ArtifactValidationException.
 *
 * @author yayu
 * @since 1.0.0
 */
public class ArtifactValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 230L;

	/**
	 * Instantiates a new artifact validation exception.
	 */
	public ArtifactValidationException() {
		super();
	}

	/**
	 * Instantiates a new artifact validation exception.
	 *
	 * @param message the message
	 */
	public ArtifactValidationException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new artifact validation exception.
	 *
	 * @param cause the cause
	 */
	public ArtifactValidationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new artifact validation exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ArtifactValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
