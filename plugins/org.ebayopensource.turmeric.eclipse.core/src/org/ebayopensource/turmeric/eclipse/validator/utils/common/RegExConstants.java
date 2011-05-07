/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

/**
 * Constants for validation against regular expression.
 *
 * @author ramurthy
 */
public interface RegExConstants {

	/** The Constant PROJECT_NAME_EXP. */
	public static final String PROJECT_NAME_EXP = "[A-Za-z]+[A-Za-z0-9_]*";
	
	/** The Constant SERVICE_NAME_EXP. */
	public static final String SERVICE_NAME_EXP = "[A-Z]+[A-Za-z0-9_]*";
	
	/** The Constant DOMAIN_NAME_EXP. */
	public static final String DOMAIN_NAME_EXP = "[A-Za-z]+[A-Za-z _]*";

}
