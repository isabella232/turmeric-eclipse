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
 * @author yayu
 * @since 1.0.0
 */
public class SOAGetErrorLibraryProviderFailedException 
extends AbstractSOAException{

	private static final long serialVersionUID = 211L;

	public SOAGetErrorLibraryProviderFailedException() {
		super();
	}

	public SOAGetErrorLibraryProviderFailedException(String message,
			Throwable cause) {
		super(message, cause);
	}

	public SOAGetErrorLibraryProviderFailedException(String message) {
		super(message);
	}

	public SOAGetErrorLibraryProviderFailedException(Throwable cause) {
		super(cause);
	}

	

}
