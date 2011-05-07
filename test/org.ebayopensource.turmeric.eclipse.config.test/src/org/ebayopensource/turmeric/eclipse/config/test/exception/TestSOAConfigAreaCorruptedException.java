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
package org.ebayopensource.turmeric.eclipse.config.test.exception;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException;
import org.junit.Test;


/**
 * The Class TestSOAConfigAreaCorruptedException.
 *
 * @author yayu
 */
public class TestSOAConfigAreaCorruptedException {
	 private static final String OOPS_BUG = "Oops. Bug";

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException#SOAConfigAreaCorruptedException(java.lang.Throwable)}.
	 */
	@Test
	public void testSOAConfigAreaCorruptedExceptionThrowable() {
		Throwable t = new RuntimeException(OOPS_BUG);
		SOAConfigAreaCorruptedException exception = new SOAConfigAreaCorruptedException(t);
        Assert.assertSame(t, exception.getCause());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException#SOAConfigAreaCorruptedException(java.lang.String)}.
	 */
	@Test
	public void testSOAConfigAreaCorruptedExceptionString() {
		SOAConfigAreaCorruptedException exception = new SOAConfigAreaCorruptedException(OOPS_BUG);
        Assert.assertNotNull(exception.getLocalizedMessage());
        Assert.assertTrue(exception.getLocalizedMessage().contains(OOPS_BUG));
	}

}
