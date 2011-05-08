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
 * The Class SOAInvocationException.
 *
 * @author yayu
 */
public class SOAInvocationException extends InvocationTargetException {

	private static final long serialVersionUID = 210L;

	/**
	 * Instantiates a new sOA invocation exception.
	 *
	 * @param target the target
	 */
	public SOAInvocationException(Throwable target) {
		super(target, target.getLocalizedMessage());
	}

	/**
	 * Instantiates a new sOA invocation exception.
	 *
	 * @param target the target
	 * @param s the s
	 */
	public SOAInvocationException(Throwable target, String s) {
		super(target, s);
	}

}
