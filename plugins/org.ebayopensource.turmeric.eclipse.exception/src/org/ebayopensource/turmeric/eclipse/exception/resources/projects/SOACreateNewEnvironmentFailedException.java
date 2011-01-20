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
 * @since 1.0.0
 */
public class SOACreateNewEnvironmentFailedException extends
SOAResourceCreationException {

	private static final long serialVersionUID = 230L;

	public SOACreateNewEnvironmentFailedException(String message,
			Throwable cause) {
		super(message, cause);
	}

	public SOACreateNewEnvironmentFailedException(Throwable cause) {
		super(cause);
	}

}
