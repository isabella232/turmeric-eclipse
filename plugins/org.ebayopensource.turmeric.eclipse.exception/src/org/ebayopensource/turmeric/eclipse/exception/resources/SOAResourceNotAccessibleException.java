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
 * The Class SOAResourceNotAccessibleException.
 *
 * @author yayu
 */
public class SOAResourceNotAccessibleException extends
		AbstractSOAResourceException {

	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA resource not accessible exception.
	 *
	 * @param message the message
	 * @param resource the resource
	 */
	public SOAResourceNotAccessibleException(String message, IResource resource) {
		super(message, resource);
	}

	/**
	 * Instantiates a new sOA resource not accessible exception.
	 *
	 * @param resource the resource
	 * @param cause the cause
	 */
	public SOAResourceNotAccessibleException(IResource resource, Throwable cause) {
		super(resource, cause);
	}

	/**
	 * Instantiates a new sOA resource not accessible exception.
	 *
	 * @param message the message
	 * @param resource the resource
	 * @param cause the cause
	 */
	public SOAResourceNotAccessibleException(String message,
			IResource resource, Throwable cause) {
		super(message, resource, cause);
	}

}
