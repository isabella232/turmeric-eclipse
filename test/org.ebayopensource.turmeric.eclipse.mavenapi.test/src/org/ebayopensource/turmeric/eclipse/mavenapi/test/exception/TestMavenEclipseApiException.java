/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.exception;

import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestMavenEclipseApiException {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException#MavenEclipseApiException()}.
	 */
	@Test
	public void testMavenEclipseApiException() {
		MavenEclipseApiException exp = new MavenEclipseApiException();
		Assert.assertNotNull(exp);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException#MavenEclipseApiException(java.lang.String)}.
	 */
	@Test
	public void testMavenEclipseApiExceptionString() {
		String msg = "Error";
		MavenEclipseApiException exp = new MavenEclipseApiException(msg);
		Assert.assertNotNull(exp);
		Assert.assertEquals(msg, exp.getLocalizedMessage());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException#MavenEclipseApiException(java.lang.Throwable)}.
	 */
	@Test
	public void testMavenEclipseApiExceptionThrowable() {
		Throwable t = new RuntimeException("iPhone is for Dummies, Nexus One is for Genius");
		MavenEclipseApiException exp = new MavenEclipseApiException(t);
		Assert.assertNotNull(exp);
		Assert.assertEquals(t, exp.getCause());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException#MavenEclipseApiException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testMavenEclipseApiExceptionStringThrowable() {
		String msg = "iPhone is for Dummies";
		Throwable t = new RuntimeException("Nexus One is for Genius");
		MavenEclipseApiException exp = new MavenEclipseApiException(msg, t);
		Assert.assertNotNull(exp);
		Assert.assertEquals(t, exp.getCause());
		Assert.assertEquals(msg, exp.getLocalizedMessage());
	}

}
