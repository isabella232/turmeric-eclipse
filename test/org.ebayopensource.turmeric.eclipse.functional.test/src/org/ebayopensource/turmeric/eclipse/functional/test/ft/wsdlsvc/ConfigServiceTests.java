/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/*
 *
 */
package org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.ui.util.PropertiesPageUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
//import org.eclipse.core.runtime.IProgressMonitor;




/**
 * @author shrao config service tests
 */
public class ConfigServiceTests extends AbstractTestCase {

	// final PropertiesPageUtil configSvcProps = new PropertiesPageUtil();
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/wsdl.zip",dataDirectory +"/extractedData");
		
	}

	@Test
	@Ignore
	public void testConfigSvcProps() throws Exception {
		final ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		model.setServiceName("BlogsServiceV1");
		model.setWorkspaceRootDirectory(PARENT_DIR);

		final String projectName = model.getServiceName() + "Client67";
		final IProject project = WorkspaceUtil.getProject(projectName);
	
		String envName = "production";
		String[] requiredServices = null;
		try {
			PropertiesPageUtil.modifyServiceDependencies(project, envName,
					model.getServiceName(),
					model.getServiceName() + "Client67",
					"http://localhost/ws/spf", "LOCAL", "SOAP12", "NV", "NV",
					requiredServices, ProgressUtil.getDefaultMonitor(null));
	
		} catch (Exception ex) {
			System.out.println("Exception in testConfigSvcProps: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testConfigSvcProps: " + ex.getLocalizedMessage());

		}

	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
