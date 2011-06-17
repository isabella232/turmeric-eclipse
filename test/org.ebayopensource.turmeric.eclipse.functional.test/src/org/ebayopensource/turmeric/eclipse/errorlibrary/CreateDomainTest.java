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

import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CreateDomainTest extends AbstractTestCase {

	ErrorLibraryParamModel model = null;
	DomainParamModel domainModel = null;
	public static final String ERRORLIB_PROJECT_NAME = "TestErrorLibrary2";
	public static final String ERRORDOMAIN_NAME = "TestDomain1";

	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/ErrorLibraryTestData.zip",dataDirectory +"/extractedData");
		
	}	
	@Before
	public void init() throws Exception {

		model = new ErrorLibraryParamModel();
		model.setWorkspaceRootDirectory(ErrorLibraryConstants.ERRORLIB_LOCATION);
		model.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		model.setProjectName(ERRORLIB_PROJECT_NAME);
		model.setVersion(ErrorLibraryConstants.ERRORLIB_VERSION);
		try {
			ErrorLibraryCreator.createErrorLibrary(model,
					ProgressUtil.getDefaultMonitor(null));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception with message" + e.getMessage()
					+ ", occured during " + "error library creation");
		}

		domainModel = new DomainParamModel();
		domainModel.setDomain(ERRORDOMAIN_NAME);
		domainModel.setErrorLibrary(ERRORLIB_PROJECT_NAME);
		domainModel.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		domainModel.setOrganization(ErrorLibraryConstants.ERRORLIB_ORG);
		domainModel
				.setPackageName(ErrorLibraryConstants.ERRORLIB_STANDARD_PACKAGE);

	}

	@Test
	public void testCreatingDomaininErrorLibrary() throws Exception {

		try {
			ErrorDomainCreator.createErrorDomain(domainModel,
					ProgressUtil.getDefaultMonitor(null));
			Assert.assertNotNull(
					"Error Domain ["
							+ ERRORDOMAIN_NAME
							+ "] is missing in the error registry",
					TurmericErrorRegistry
							.getErrorDomainByName(ERRORDOMAIN_NAME));
			ErrorLibraryCleanValidate.validateErrorLibraryArtifacts(
					WorkspaceUtil.getProject(ERRORLIB_PROJECT_NAME),
					ERRORLIB_PROJECT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception with message" + e.getMessage()
					+ ", occured during " + "error library creation");
		}
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
