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
package org.ebayopensource.turmeric.eclipse.exception.resources;

import java.lang.reflect.InvocationTargetException;

/**
 * @author yayu
 *
 */
public abstract class AbstractSOAResourceModifyException extends
InvocationTargetException {
	
	public AbstractSOAResourceModifyException(Throwable cause) {
		super(cause, cause.getLocalizedMessage());
	}
	
	public AbstractSOAResourceModifyException(String message, Throwable cause) {
		super(cause, message);
	}
}
