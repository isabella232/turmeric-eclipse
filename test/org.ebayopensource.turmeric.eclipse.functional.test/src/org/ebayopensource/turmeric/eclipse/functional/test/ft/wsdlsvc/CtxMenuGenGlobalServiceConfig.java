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
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author shrao
 * 
 */
public class CtxMenuGenGlobalServiceConfig extends AbstractTestCase {

	final ActionUtil ctxMenuAction = new ActionUtil();
	static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("CalcService.wsdl");
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/wsdl.zip",dataDirectory +"/extractedData");
		
	}

	
	@Test
	public void testCtxMenuGSC() throws Exception {
		final ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		model.setServiceName("BlogsCalcServiceV1");
		model.setWorkspaceRootDirectory(PARENT_DIR);

		final String projectName = model.getServiceName() + "Impl";
		final IProject project = WorkspaceUtil.getProject(projectName);
		File fileGSC = new File(PARENT_DIR + File.separator + projectName
				+ File.separator + "meta-src" + File.separator + "META-INF"
				+ File.separator + "soa" + File.separator + "services"
				+ File.separator + "config" + File.separator
				+ "GlobalServiceConfig.xml");
		long lastMod1 = 0;
		long lastMod2 = 0;

		if (fileGSC.exists())
			lastMod1 = fileGSC.lastModified();

		System.out.println("Lastmod1: " + lastMod1);

		try {
			if (!WorkspaceUtil.projectExistsInWorkSpace(projectName)) {
				System.out.println("Project does not exist in workspace.");
				ProjectUtil.cleanUpWS();
				ServiceSetupCleanupValidate.cleanup(model.getServiceName());
				Boolean b = ServiceFromWsdlTest
						.createServiceFromWsdl((new File(WSDL_FILE)).toURI()
								.toURL(),null);
				Assert.assertTrue(model.getServiceName()
						+ " Service creation failed", b);
				WorkspaceUtil.openProject(project,
						ProgressUtil.getDefaultMonitor(null));
			} else
				System.out.println("Project exists in work space - "
						+ projectName);

			fileGSC.delete(); // to test assertTrue
			ActionUtil.generateGlobalServiceConfig(project,
					ProgressUtil.getDefaultMonitor(null));
			Assert.assertTrue("Assert failure: Expected file does not exist - "
					+ fileGSC.getName(), fileGSC.exists());
			lastMod2 = fileGSC.lastModified();
			// lastMod2=-1; //to test assertTrue
			System.out.println("Lastmod2: " + lastMod2);
			Assert.assertTrue(
					"Assert failure: Expected lastModified date on file to be newer",
					lastMod2 > lastMod1);
		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuGSC: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuGSC: " + ex.getLocalizedMessage());
		}
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
