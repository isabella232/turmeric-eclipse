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
package org.ebayopensource.turmeric.eclipse.exception.resources.projects;

import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.eclipse.core.resources.IResource;


/**
 * The Class SOARemoveEnvironmentFailedException.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOARemoveEnvironmentFailedException extends
		SOAResourceModifyFailedException {

	private static final long serialVersionUID = 230L;

	/**
	 * Instantiates a new sOA remove environment failed exception.
	 *
	 * @param cause the cause
	 */
	public SOARemoveEnvironmentFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA remove environment failed exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SOARemoveEnvironmentFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new sOA remove environment failed exception.
	 *
	 * @param resource the resource
	 * @param cause the cause
	 */
	public SOARemoveEnvironmentFailedException(IResource resource,
			Throwable cause) {
		super(resource, cause);
	}

}
