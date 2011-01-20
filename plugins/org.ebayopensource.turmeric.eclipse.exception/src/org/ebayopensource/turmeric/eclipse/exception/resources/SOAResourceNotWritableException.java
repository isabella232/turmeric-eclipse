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
 * @author yayu
 *
 */
public class SOAResourceNotWritableException extends
		AbstractSOAResourceException {

	private static final long serialVersionUID = 201L;

	/**
	 * @param message
	 * @param resource
	 */
	public SOAResourceNotWritableException(String message, IResource resource) {
		super(message, resource);
	}

	/**
	 * @param resource
	 * @param cause
	 */
	public SOAResourceNotWritableException(IResource resource, Throwable cause) {
		super(resource, cause);
	}

	/**
	 * @param message
	 * @param resource
	 * @param cause
	 */
	public SOAResourceNotWritableException(String message, IResource resource,
			Throwable cause) {
		super(message, resource, cause);
	}

}
