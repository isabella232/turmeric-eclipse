/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.internal.util;

import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.ThrowableUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestThrowableUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.ThrowableUtil#adaptToRuntimeException(java.lang.Throwable)}.
	 */
	@Test
	public void testAdaptToRuntimeException() {
		Assert.assertNotNull(ThrowableUtil.adaptToRuntimeException(null));
		Throwable throwable = new Exception("The new era of Android is coming!");
		RuntimeException rep = ThrowableUtil.adaptToRuntimeException(throwable);
		Assert.assertEquals(throwable.getClass().getName() + ": " 
				+ throwable.getLocalizedMessage(), rep.getLocalizedMessage());
	}

}
