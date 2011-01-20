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
 * @author yayu
 *
 */
public interface ISOAPreValidator extends ISOAValidator {
	public static final String SERVICE = "service";
	public static final String CONSUMER = "consumer";
	public static final String CONSUMER_FROM_WSDL = "consumer_from_wsdl";
	public static final String OTHERS = "others";
	public static final String TYPE_LIBRARY = "type_library";

	/**
	 * Pre-validation for project creation.
	 * @param obj
	 * @return
	 */
	public IStatus validateProjectCreation(Object obj) throws ValidationInterruptedException ;
}
