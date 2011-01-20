/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class CreateErrorLibraryTest extends AbstractTestCase{
	
	ErrorLibraryParamModel model = null;

	@Before
	public void init() {
		model = new ErrorLibraryParamModel();
		model.setWorkspaceRootDirectory(ErrorLibraryConstants.ERRORLIB_LOCATION);
		model.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		model.setProjectName(ErrorLibraryConstants.ERRORLIB_PROJECT_NAME);
		model.setVersion(ErrorLibraryConstants.ERRORLIB_VERSION);

	}

	//@Ignore("Test is failing under Linux") 
	@Test
	public void createErrorLibrary() {

		try {
			ErrorLibraryCreator.createErrorLibrary(model,
					ProgressUtil.getDefaultMonitor(null));
			Assert.assertNotNull(
					"Error library ["
							+ ErrorLibraryConstants.ERRORLIB_PROJECT_NAME
							+ "] is missing in the error registry",
					TurmericErrorRegistry
							.getErrorLibraryByName(ErrorLibraryConstants.ERRORLIB_PROJECT_NAME));
			Assert.assertTrue(ErrorLibraryCleanValidate.validateErrorLibraryArtifacts(
					WorkspaceUtil
							.getProject(ErrorLibraryConstants.ERRORLIB_PROJECT_NAME),
					ErrorLibraryConstants.ERRORLIB_PROJECT_NAME));

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception with message" + e.getMessage()
					+ ", occured during " + "error library creation");
		}

	}

	

}
