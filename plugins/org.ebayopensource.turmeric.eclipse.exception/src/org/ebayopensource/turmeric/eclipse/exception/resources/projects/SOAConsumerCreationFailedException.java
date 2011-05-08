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
 * The Class SOAConsumerCreationFailedException.
 *
 * @author yayu
 */
public class SOAConsumerCreationFailedException extends
		SOAProjectCreationFailedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA consumer creation failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAConsumerCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA consumer creation failed exception.
	 *
	 * @param projectName the project name
	 * @param cause the cause
	 */
	public SOAConsumerCreationFailedException(String projectName,
			Throwable cause) {
		super("SOA consumer project", projectName, cause);
	}

}
