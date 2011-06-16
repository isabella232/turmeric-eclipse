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
package org.ebayopensource.turmeric.eclipse.core.test.exception;

import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOAExceptionHandler {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler#handleException(java.lang.Throwable)}.
	 */
	@Test
	public void testHandleException() {
		Throwable throwable = new RuntimeException("Error");
		//SOAExceptionHandler.handleException(throwable);
		//TODO handle the popuped dialog.
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler#silentHandleException(java.lang.Throwable)}.
	 */
	@Test
	public void testSilentHandleException() {
		Throwable throwable = new RuntimeException("Error");
		SOAExceptionHandler.silentHandleException(throwable);
	}

}
