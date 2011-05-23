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

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author shrao
 * 
 */
public class CtxMenuGenTypeMappings extends AbstractTestCase {

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
	
	
	@Test
	public void testCtxMenuTypeMapping() throws Exception {
		 final ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		 model.setServiceName("CalcService");
		 model.setWorkspaceRootDirectory(PARENT_DIR);

		final String projectName = SERVICE_NAME;
		final IProject project = WorkspaceUtil.getProject(projectName);
		File fileTypeMapping = new File(PARENT_DIR + File.separator
				+ projectName + File.separator + "gen-meta-src"
				+ File.separator + "META-INF" + File.separator + "soa"
				+ File.separator + "common" + File.separator + "config"
				+ File.separator + SERVICE_NAME + File.separator
				+ "TypeMappings.xml");
		long lastMod1 = 0;
		long lastMod2 = 0;

		if (fileTypeMapping.exists())
			lastMod1 = fileTypeMapping.lastModified();

		System.out.println("Lastmod1: " + lastMod1);

		try {
			if (!WorkspaceUtil.projectExistsInWorkSpace(projectName)) {
				System.out.println("Project does not exist in workspace.");
				WorkspaceUtil.openProject(project,
						ProgressUtil.getDefaultMonitor(null));
			} else
				System.out.println("Project exists in work space - "
						+ projectName);

			fileTypeMapping.delete(); // to test assertTrue
			ActionUtil.generateTypeMappings(project,
					ProgressUtil.getDefaultMonitor(null));
			Assert.assertTrue("Assert failure: Expected file does not exist - "
					+ fileTypeMapping.getName(), fileTypeMapping.exists());
			lastMod2 = fileTypeMapping.lastModified();
			// lastMod2=-1; //to test assertTrue
			System.out.println("Lastmod2: " + lastMod2);
			Assert.assertTrue(
					"Assert failure: Expected lastModified date on file to be newer",
					lastMod2 > lastMod1);
		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuTypeMapping: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuTypeMapping: "
					+ ex.getLocalizedMessage());
		}
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
	
}
