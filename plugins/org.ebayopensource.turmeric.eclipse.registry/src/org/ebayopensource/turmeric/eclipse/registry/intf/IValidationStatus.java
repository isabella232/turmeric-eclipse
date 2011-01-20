/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.intf;

/**
 * Addition interface for WSDL validation status. Add line number to indicate
 * which line causes this problem.
 * 
 * @author mzang
 * 
 */
public interface IValidationStatus {
	
	/**
	 * indicates a suggestion to the user
	 */
	public static final int CODE_MAY = 100;
	
	/**
	 * if AR login failed, the returned status should be IStatus.WARNING and the
	 * getCode method should return CODE_LOGIN_FAILURE
	 */
	public static final int CODE_LOGIN_FAILURE = 102;
	
	public int getLineNumber();

	public void setLineNumber(int lineNumber);

}
