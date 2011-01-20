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
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author shrao test for context menu option 'ReGenerate Service Impl' on Impl
 *         project overwriteImplClass=true for this option
 */
public class CtxMenuRegenServiceImpl extends AbstractTestCase {

	final ActionUtil ctxMenuAction = new ActionUtil();

	static final String SERVICE_NAME = "CalcServiceV1";

	/**
	 * @throws java.lang.Exception
	 */
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


	@SuppressWarnings("unchecked")
	@Test
//	@Ignore("failing")
	public void testCtxMenuServiceImpl() throws Exception {
		final ServiceFromWsdlParamModel model = new
		 ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		 model.setServiceName("CalcService");
		 model.setWorkspaceRootDirectory(PARENT_DIR);

		String implProjectName = SERVICE_NAME + "Impl";
		IProject serviceImplPrj = WorkspaceUtil.getProject(implProjectName);
		NameFileFilter fileFilter = new NameFileFilter(implProjectName
				+ ".java");
		Collection<File> files = FileUtils.listFiles(serviceImplPrj
				.getLocation().toFile(), fileFilter, TrueFileFilter.INSTANCE);

		File fileServiceImpl = null;
		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			if (file.getAbsolutePath().indexOf("src") > 0) {
				fileServiceImpl = file;
			}
		}
		// File fileServiceImpl = new File(PARENT_DIR +"\\" +projectName +"\\"
		// +"src\\com\\ebay\\marketplace\\services\\eboxcalcservice\\impl\\EBoxCalcServiceImpl.java");
		long lastMod1 = 0;
		long lastMod2 = 0;

		if (fileServiceImpl.exists())
			lastMod1 = fileServiceImpl.lastModified();

		System.out.println("Lastmod1: " + lastMod1);

		try {
			if (!WorkspaceUtil.projectExistsInWorkSpace(implProjectName)) {
				System.out.println("Project does not exist in workspace.");
				WorkspaceUtil.openProject(serviceImplPrj,
						ProgressUtil.getDefaultMonitor(null));
			} else
				System.out.println("Project exists in work space - "
						+ implProjectName);
			fileServiceImpl.delete(); // to test assertTrue
			ActionUtil.generateServiceImplSkeleton(serviceImplPrj, true,
					ProgressUtil.getDefaultMonitor(null));

			Assert.assertTrue("Assert failure: Expected file does not exist - "
					+ fileServiceImpl.getName(), fileServiceImpl.exists());
			lastMod2 = fileServiceImpl.lastModified();

			System.out.println("Lastmod2: " + lastMod2);
			Assert.assertTrue(
					"Assert failure: Expected lastModified date on file to be newer",
					lastMod2 > lastMod1);
		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuServiceImpl: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuServiceImpl: "
					+ ex.getLocalizedMessage());
		}
	}
	
}
