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
package org.ebayopensource.turmeric.eclipse.config.test.core;

/**
 * Constants for testing the config plugin.
 *
 * @author yayu
 */
public final class ConfigTestConstants {

	/**
	 * 
	 */
	private ConfigTestConstants() {
		super();
	}
	
	/** The Constant CONFIG_TEST_REPO_ID. */
	public static final String CONFIG_TEST_REPO_ID = "config_test";
	
	/** The Constant CONFIG_TEST_ORG_ID. */
	public static final String CONFIG_TEST_ORG_ID = CONFIG_TEST_REPO_ID;

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
