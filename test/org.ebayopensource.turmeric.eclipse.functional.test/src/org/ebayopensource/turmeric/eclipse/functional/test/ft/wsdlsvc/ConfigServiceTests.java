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

import java.io.File;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.ui.util.PropertiesPageUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.Ignore;
import org.junit.Test;
//import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author shrao config service tests
 */
public class ConfigServiceTests extends AbstractTestCase {

	static String WSDL_FILE = ServiceSetupCleanupValidate
	.getWsdlFilePath("CSUpdateMACActivityAddAttachments.wsdl");

	@Test
//	@Ignore
	public void testConfigSvcProps() throws Exception {
		final ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		model.setServiceName("CSAPIInterfaceServiceV1");
		model.setWorkspaceRootDirectory(PARENT_DIR);
		AttachmentWsdlConsumerTest test = new AttachmentWsdlConsumerTest();
		test.createConsumerFromWsdl(new File(WSDL_FILE).toURI().toURL());

		final String projectName = model.getServiceName() + "Consumer";
		final IProject project = WorkspaceUtil.getProject(projectName);
	
		String envName = "production";
		String[] requiredServices = null;
		try {
			PropertiesPageUtil.modifyServiceDependencies(project, envName,
					model.getServiceName()+" Impl",
					model.getServiceName() + "Consumer",
					"http://localhost/ws/spf", "LOCAL", "SOAP12", "NV", "NV",
					requiredServices, ProgressUtil.getDefaultMonitor(null));
	
		} catch (Exception ex) {
			System.out.println("Exception in testConfigSvcProps: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testConfigSvcProps: " + ex.getLocalizedMessage());

		}

	}

}
