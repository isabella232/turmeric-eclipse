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
package org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemCodeGen;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author shrao menu on Impl project Impl\gen-web-content\WEB-INF\web.xml file
 *         should be created
 */

public class CtxMenuGenWebAppDesc extends AbstractTestCase {
	final ActionUtil ctxMenuAction = new ActionUtil();

	static final String SERVICE_NAME = "CalcServiceV1";
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/wsdl.zip",dataDirectory +"/extractedData");
		
	}


	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public  void setUpBeforeClass() throws Exception {
	
		FunctionalTestHelper.ensureM2EcipseBeingInited();
		ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(SERVICE_NAME,
				"CalcService");
		Thread.sleep(10000);
	}
	
	/**
	 * @throws java.lang.Exception
	 */

	
	@Test
	public void testCtxMenuWebAppDesc() throws Exception {
		// final ServiceFromWsdlParamModel model = new
		// ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		// model.setServiceName("BlogsCalcV1");
		// model.setWorkspaceRootDirectory(PARENT_DIR);

		final String projectName = SERVICE_NAME + "Impl";
		final IProject project = WorkspaceUtil.getProject(projectName);
		File fileWebAppDesc = new File(PARENT_DIR + File.separator
				+ projectName + File.separator + "gen-web-content"
				+ File.separator + "WEB-INF" + File.separator + "web.xml");
		long lastMod1 = 0;
		long lastMod2 = 0;

		if (fileWebAppDesc.exists())
			lastMod1 = fileWebAppDesc.lastModified();

		System.out.println("Lastmod1: " + lastMod1);

		try {
			if (!WorkspaceUtil.projectExistsInWorkSpace(projectName)) {
				System.out.println("Project does not exist in workspace.");
				WorkspaceUtil.openProject(project,
						ProgressUtil.getDefaultMonitor(null));
			} else
				System.out.println("Project exists in work space - "
						+ projectName);

			final Class<?> templateLoadingClass;
			final Map<String, String> templates = new ConcurrentHashMap<String, String>(1);
			if (PreferenceConstants._PREF_DEFAULT_REPOSITORY_SYSTEM
					.equals(GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().getId())) {
				// we should generate the Geronimo specific deployment file in
				// V3 mode
				templates.put("WEB-INF/geronimo-web.xml",
						"geronimo-web.xml.ftl");
				templateLoadingClass = BuildSystemCodeGen.class;
			} else {
				templateLoadingClass = null;
			}

			boolean genSuccess = false;
			genSuccess = ActionUtil.generateWebXml(project, templates,
					templateLoadingClass, ProgressUtil.getDefaultMonitor(null));
			Assert.assertTrue("Assert Failure: generateWebXml() returned false",
					genSuccess == true);
			// fileWebAppDesc.delete(); //to test assertTrue
			Assert.assertTrue("Assert failure: Expected file does not exist - "
					+ fileWebAppDesc.getName(), fileWebAppDesc.exists());
			lastMod2 = fileWebAppDesc.lastModified();
			// lastMod2=-1; //to test assertTrue
			System.out.println("Lastmod2: " + lastMod2);
			Assert.assertTrue(
					"Assert failure: Expected lastModified date on file to be newer",
					lastMod2 > lastMod1);
		} catch (CoreException cEx) {
			System.out.println("Exception in testCtxMenuWebAppDesc: "
					+ cEx.getLocalizedMessage());
			Assert.fail("Core Exception in testCtxMenuWebAppDesc: "
					+ cEx.getLocalizedMessage());
		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuWebAppDesc: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuWebAppDesc: "
					+ ex.getLocalizedMessage());
		}
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
