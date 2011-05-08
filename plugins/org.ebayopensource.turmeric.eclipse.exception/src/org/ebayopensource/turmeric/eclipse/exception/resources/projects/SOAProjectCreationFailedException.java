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

import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceCreationException;

/**
 * The Class SOAProjectCreationFailedException.
 *
 * @author yayu
 */
public class SOAProjectCreationFailedException extends
SOAResourceCreationException {

	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA project creation failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAProjectCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA project creation failed exception.
	 *
	 * @param projectName the project name
	 * @param cause the cause
	 */
	public SOAProjectCreationFailedException(String projectName, Throwable cause) {
		this("SOA project", projectName, cause);
	}
	
	/**
	 * Instantiates a new sOA project creation failed exception.
	 *
	 * @param projectType the project type
	 * @param projectName the project name
	 * @param cause the cause
	 */
	protected SOAProjectCreationFailedException(String projectType, 
			String projectName, Throwable cause) {
		super("Failed to create " + projectType + "->" + projectName, cause);
	}

}
