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

/**
 * This exception is supposed to be used when the creation of SOA Error Library failed.
 * @author yayu
 * @since 1.0.0
 *
 */
public class SOAErrorLibraryCreationFailedException extends
		SOAProjectCreationFailedException {
	private static final String PROJECT_TYPE_ERROR_LIBRARY = "SPA Error Library";

	/**
	 * 
	 */
	private static final long serialVersionUID = 211L;

	/**
	 * Instantiates a new sOA error library creation failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAErrorLibraryCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA error library creation failed exception.
	 *
	 * @param projectName the project name
	 * @param cause the cause
	 */
	public SOAErrorLibraryCreationFailedException(String projectName,
			Throwable cause) {
		super(PROJECT_TYPE_ERROR_LIBRARY, projectName, cause);
	}

}
