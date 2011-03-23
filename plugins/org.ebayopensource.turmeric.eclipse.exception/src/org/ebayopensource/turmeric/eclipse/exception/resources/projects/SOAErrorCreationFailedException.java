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
 * Thrown when the error creation is interrupted or failed. Typically this is
 * due to an user error and needs user attention and for the same reason clients
 * should not swallow this exception.
 * 
 * @author smathew
 * 
 */
public class SOAErrorCreationFailedException extends CommandFailedException {

	private static final long serialVersionUID = 1L;

	public SOAErrorCreationFailedException(String message) {
		super(message);
	}

	public SOAErrorCreationFailedException(Exception exception) {
		super(exception);
	}

}
