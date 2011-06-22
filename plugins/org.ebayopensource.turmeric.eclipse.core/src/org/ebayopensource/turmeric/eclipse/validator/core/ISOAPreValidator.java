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
package org.ebayopensource.turmeric.eclipse.validator.core;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.eclipse.core.runtime.IStatus;


/**
 * The Interface ISOAPreValidator.
 *
 * @author yayu
 */
public interface ISOAPreValidator extends ISOAValidator {
	
	/** The Constant SERVICE. */
	public static final String SERVICE = "service";
	
	/** The Constant CONSUMER. */
	public static final String CONSUMER = "consumer";
	
	/** The Constant CONSUMER_FROM_WSDL. */
	public static final String CONSUMER_FROM_WSDL = "consumer_from_wsdl";
	
	/** The Constant OTHERS. */
	public static final String OTHERS = "others";
	
	/** The Constant TYPE_LIBRARY. */
	public static final String TYPE_LIBRARY = "type_library";
	
	/** The Constant ERROR_LIBRARY. */
	public static final String ERROR_LIBRARY = "error_library";

	/**
	 * Pre-validation for project creation.
	 *
	 * @param obj the obj
	 * @return the i status
	 * @throws ValidationInterruptedException the validation interrupted exception
	 */
	public IStatus validateProjectCreation(Object obj) throws ValidationInterruptedException ;
}
