/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.impl;

import org.junit.Assert;

import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestMavenApiHelper {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper#getMavenIndexManager()}.
	 */
	@Test
	public void testGetMavenIndexManager() {
		Assert.assertNotNull(MavenApiHelper.getMavenIndexManager());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper#getMavenProjectManager()}.
	 */
	@Test
	public void testGetMavenProjectManager() {
		Assert.assertNotNull(MavenApiHelper.getMavenProjectManager());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper#getMavenModelManager()}.
	 */
	@Test
	public void testGetMavenModelManager() {
		Assert.assertNotNull(MavenApiHelper.getMavenModelManager());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper#getMavenEmbedder()}.
	 */
	@Test
	public void testGetMavenEmbedder() {
		try {
			Assert.assertNotNull(MavenApiHelper.getMavenEmbedder());
		} catch (MavenEclipseApiException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

}
