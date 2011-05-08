/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.resources.projects;

import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;

/**
 * Throw this exception if the operation is not available for the given object.
 * Some operations cannot be performed on the given input parameter and in such
 * situations clients can make use of this exception.
 * 
 * @author smathew
 */
public class SOAOperationNotAvailableException extends CommandFailedException {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new sOA operation not available exception.
	 *
	 * @param msg the msg
	 */
	public SOAOperationNotAvailableException(String msg) {
		super(msg);
	}

}
