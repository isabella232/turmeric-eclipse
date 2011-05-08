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
 * The Class SOATypeCreationFailedException.
 *
 * @author yayu
 */
public class SOATypeCreationFailedException extends
		SOAResourceCreationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA type creation failed exception.
	 *
	 * @param cause the cause
	 */
	public SOATypeCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA type creation failed exception.
	 *
	 * @param typeName the type name
	 * @param cause the cause
	 */
	public SOATypeCreationFailedException(String typeName, Throwable cause) {
		super("Failed to create SOA type->" + typeName, cause);
	}

}
