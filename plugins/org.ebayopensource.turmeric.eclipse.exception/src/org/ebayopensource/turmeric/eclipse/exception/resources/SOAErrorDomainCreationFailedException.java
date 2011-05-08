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
package org.ebayopensource.turmeric.eclipse.exception.resources;

/**
 * The Class SOAErrorDomainCreationFailedException.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOAErrorDomainCreationFailedException extends
		SOAResourceCreationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 22L;

	/**
	 * Instantiates a new sOA error domain creation failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAErrorDomainCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA error domain creation failed exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SOAErrorDomainCreationFailedException(String message, Throwable cause) {
		super("Failed to create SOA error domain->" + message, cause);
	}

}
