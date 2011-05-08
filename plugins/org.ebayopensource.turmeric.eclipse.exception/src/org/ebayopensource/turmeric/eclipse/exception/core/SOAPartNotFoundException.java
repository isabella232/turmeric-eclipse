/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.core;

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;
import org.ebayopensource.turmeric.eclipse.exception.messages.SOAMessages;

/**
 * This should be thrown when one of the expected UI part is not found or
 * missing. One example would be missing active editor or incompatible editor
 * 
 * @author smathew
 * 
 */
public class SOAPartNotFoundException extends AbstractSOAException{

	private static final long serialVersionUID = 1L;
	
	/** The default msg. */
	public String defaultMsg = SOAMessages.PART_NOT_FOUND;
	
	/**
	 * Instantiates a new sOA part not found exception.
	 *
	 * @param message the message
	 */
	public SOAPartNotFoundException(String message) {
		super(message);		
	}

	
}
