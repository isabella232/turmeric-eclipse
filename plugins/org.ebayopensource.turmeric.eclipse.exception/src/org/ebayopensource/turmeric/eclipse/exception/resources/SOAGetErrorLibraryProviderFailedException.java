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

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;

/**
 * The Class SOAGetErrorLibraryProviderFailedException.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOAGetErrorLibraryProviderFailedException 
extends AbstractSOAException{

	private static final long serialVersionUID = 211L;

	/**
	 * Instantiates a new sOA get error library provider failed exception.
	 */
	public SOAGetErrorLibraryProviderFailedException() {
		super();
	}

	/**
	 * Instantiates a new sOA get error library provider failed exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SOAGetErrorLibraryProviderFailedException(String message,
			Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new sOA get error library provider failed exception.
	 *
	 * @param message the message
	 */
	public SOAGetErrorLibraryProviderFailedException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new sOA get error library provider failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAGetErrorLibraryProviderFailedException(Throwable cause) {
		super(cause);
	}

	

}
