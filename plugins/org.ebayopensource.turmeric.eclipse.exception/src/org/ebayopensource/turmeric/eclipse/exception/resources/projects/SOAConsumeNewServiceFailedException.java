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
 * The Class SOAConsumeNewServiceFailedException.
 *
 * @author yayu
 */
public class SOAConsumeNewServiceFailedException extends
		SOAResourceModifyFailedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA consume new service failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAConsumeNewServiceFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA consume new service failed exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SOAConsumeNewServiceFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new sOA consume new service failed exception.
	 *
	 * @param project the implProject
	 * @param cause the cause
	 */
	public SOAConsumeNewServiceFailedException(IProject project,
			Throwable cause) {
		super(project, cause);
	}

}
