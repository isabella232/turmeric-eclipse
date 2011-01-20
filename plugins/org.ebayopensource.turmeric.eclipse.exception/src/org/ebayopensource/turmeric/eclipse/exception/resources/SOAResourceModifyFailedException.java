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
public class SOAResourceModifyFailedException extends
		AbstractSOAResourceModifyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201L;

	/**
	 * @param cause
	 */
	public SOAResourceModifyFailedException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public SOAResourceModifyFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public SOAResourceModifyFailedException(IResource resource, Throwable cause) {
		super("Failed to modify resource->" + resource.getLocation(), cause);
	}

}
