/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maveneclipseapi.test.request;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequestRaw;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestProjectMavenizationRequestRaw {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequestRaw#ProjectMavenizationRequestRaw()}.
	 */
	@Test
	public void testProjectMavenizationRequestRaw() {
		ProjectMavenizationRequestRaw request = new ProjectMavenizationRequestRaw();
		Assert.assertNotNull(request);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequestRaw#getMavenModel()}.
	 */
	@Test
	public void testSetGetMavenModel() {
		InputStream ins = null;
		try {
			ins = TestProjectMavenizationRequestRaw.class.getResourceAsStream("pom.xml");
			Model model = MavenApiHelper.getMavenModelManager().readMavenModel(ins);
			Assert.assertNotNull(model);
			ProjectMavenizationRequestRaw request = new ProjectMavenizationRequestRaw();
			request.setMavenModel(model);
			Assert.assertSame(model, request.getMavenModel());
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(ins);
		}
	}


}
