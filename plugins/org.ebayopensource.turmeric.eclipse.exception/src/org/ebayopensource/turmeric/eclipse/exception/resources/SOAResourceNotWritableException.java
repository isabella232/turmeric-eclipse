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

import org.eclipse.core.resources.IResource;

/**
 * The Class SOAResourceNotWritableException.
 *
 * @author yayu
 */
public class SOAResourceNotWritableException extends
		AbstractSOAResourceException {

	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA resource not writable exception.
	 *
	 * @param message the message
	 * @param resource the resource
	 */
	public SOAResourceNotWritableException(String message, IResource resource) {
		super(message, resource);
	}

	/**
	 * Instantiates a new sOA resource not writable exception.
	 *
	 * @param resource the resource
	 * @param cause the cause
	 */
	public SOAResourceNotWritableException(IResource resource, Throwable cause) {
		super(resource, cause);
	}

	/**
	 * Instantiates a new sOA resource not writable exception.
	 *
	 * @param message the message
	 * @param resource the resource
	 * @param cause the cause
	 */
	public SOAResourceNotWritableException(String message, IResource resource,
			Throwable cause) {
		super(message, resource, cause);
	}

}
