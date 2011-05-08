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
import org.eclipse.core.resources.IProject;


/**
 * The Class SOARemoveConsumedServiceFailedException.
 *
 * @author yayu
 */
public class SOARemoveConsumedServiceFailedException extends
		SOAResourceModifyFailedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA remove consumed service failed exception.
	 *
	 * @param cause the cause
	 */
	public SOARemoveConsumedServiceFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA remove consumed service failed exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SOARemoveConsumedServiceFailedException(String message,
			Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new sOA remove consumed service failed exception.
	 *
	 * @param project the project
	 * @param cause the cause
	 */
	public SOARemoveConsumedServiceFailedException(IProject project,
			Throwable cause) {
		super(project, cause);
	}

}
