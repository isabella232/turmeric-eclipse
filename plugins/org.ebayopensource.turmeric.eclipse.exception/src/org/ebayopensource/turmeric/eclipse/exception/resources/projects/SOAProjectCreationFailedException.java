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
 * @author yayu
 *
 */
public class SOAProjectCreationFailedException extends
SOAResourceCreationException {

	private static final long serialVersionUID = 201L;

	/**
	 * @param cause
	 */
	public SOAProjectCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param serviceName
	 * @param cause
	 */
	public SOAProjectCreationFailedException(String projectName, Throwable cause) {
		this("SOA project", projectName, cause);
	}
	
	protected SOAProjectCreationFailedException(String projectType, 
			String projectName, Throwable cause) {
		super("Failed to create " + projectType + "->" + projectName, cause);
	}

}
