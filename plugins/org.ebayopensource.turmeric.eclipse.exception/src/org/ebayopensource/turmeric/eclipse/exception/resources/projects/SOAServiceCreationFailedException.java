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
 * The Class SOAServiceCreationFailedException.
 *
 * @author yayu
 */
public class SOAServiceCreationFailedException extends
		SOAProjectCreationFailedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201L;

	/**
	 * Instantiates a new sOA service creation failed exception.
	 *
	 * @param cause the cause
	 */
	public SOAServiceCreationFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sOA service creation failed exception.
	 *
	 * @param serviceName the service name
	 * @param cause the cause
	 */
	public SOAServiceCreationFailedException(String serviceName, Throwable cause) {
		super("SOA service", serviceName, cause);
	}

}
