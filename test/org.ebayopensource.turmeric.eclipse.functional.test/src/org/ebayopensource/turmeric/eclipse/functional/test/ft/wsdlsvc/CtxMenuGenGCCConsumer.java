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
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author shrao test for context menu option 'Generate Client Config'on
 *         consumer project
 */
public class CtxMenuGenGCCConsumer extends AbstractTestCase {
	final ActionUtil ctxMenuAction = new ActionUtil();
	static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("CalcService.wsdl");
	
	String adminName = "BlogsCalcServiceV1";
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/wsdl.zip",dataDirectory +"/extractedData");
		
	}

	// static IProject project = null;

	
	@Test
	public void testCtxMenuGCCConsumer() throws Exception {

		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();
		

		final String projectName = "BlogsCalcServiceV1Consumer";
		final IProject project = WorkspaceUtil.getProject(projectName);
		File fileGCC = new File(PARENT_DIR + File.separator + projectName
				+ File.separator + "meta-src" + File.separator + "META-INF"
				+ File.separator + "soa" + File.separator + "client"
				+ File.separator + "config" + File.separator
				+ "GlobalClientConfig.xml");
		long lastMod1 = 0;
		long lastMod2 = 0;


		try {
			
				Boolean b = ConsumerFromWsdlTest
						.createConsumerFromWsdl((new File(WSDL_FILE)).toURI()
								.toURL(),adminName);
				Assert.assertTrue(projectName
						+ "createEBoxConsumerFromWsdl() failed", b);
				
				ActionUtil.generateGlobalClientConfig(project,
						ProgressUtil.getDefaultMonitor(null));
				lastMod1 = fileGCC.lastModified();
				fileGCC.delete(); //to test assertTrue	
				
				ActionUtil.generateGlobalClientConfig(project,
						ProgressUtil.getDefaultMonitor(null));
			Assert.assertTrue("Assert failure: Expected file does not exist - "
					+ fileGCC.getName(), fileGCC.exists());
				lastMod2 = fileGCC.lastModified();
			// lastMod2=-1; //to test assertTrue
			System.out.println("Lastmod2: " + lastMod2);
			Assert.assertTrue(
					"Assert failure: Expected lastModified date on file to be newer",
					lastMod2 > lastMod1);
		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuGCC: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuGCC: " + ex.getLocalizedMessage());
		}
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
}
