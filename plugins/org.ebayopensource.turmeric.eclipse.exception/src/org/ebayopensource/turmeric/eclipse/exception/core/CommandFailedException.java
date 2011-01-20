/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.core;

/**
 * Base Exception thrown for any command failures. Typically this is thrown when
 * a SOA ICommand is failed.Clients should catch this exception and present to
 * the user with recovery messages.
 * 
 * @author smathew
 * 
 */
public class CommandFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommandFailedException() {
		super();
	}

	public CommandFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandFailedException(String message) {
		super(message);
	}

	public CommandFailedException(Throwable cause) {
		super(cause);
	}

}
