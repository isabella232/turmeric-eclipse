/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DeleteDomain extends AbstractTestCase {
	
	ErrorLibraryParamModel model = null;
	DomainParamModel domainModel = null;
	ErrorParamModel errorModel = null;
	
	public static final String ERRORLIB_PROJECT_NAME = "TestErrorLibrary2";
	public static final String ERRORLIB_DOMAIN = "TestDomain1";
	public static final String ERROR_MESSAGE = "Test Message";
	public static final String ERROR_NAME = "TestError";
	public static final String ERROR_RESOLUTION = "Test Resolution";
	public static final String ERROR_SUBDOMAIN = "Buying";
	
	public static final String ERROR_PROPS = WorkspaceUtil.getProjectDirPath(ERRORLIB_PROJECT_NAME,"Domain_list.properties");
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/ErrorLibraryTestData.zip",dataDirectory +"/extractedData");
		
	}
	
	@Before
	public void init(){
		
		model = new ErrorLibraryParamModel();
		model.setWorkspaceRootDirectory(ErrorLibraryConstants.ERRORLIB_LOCATION);
		model.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		model.setProjectName(ERRORLIB_PROJECT_NAME);
		model.setVersion(ErrorLibraryConstants.ERRORLIB_VERSION);
		try {
			ErrorLibraryCreator.createErrorLibrary(model,
					ProgressUtil.getDefaultMonitor(null));
			//TurmericErrorRegistry.refresh();
		} catch (Exception e) {
			Assert.fail("Exception with message " + e.getMessage()
					+ ", occured during " + "error library creation");
		}

		domainModel = new DomainParamModel();
		domainModel.setDomain(ERRORLIB_DOMAIN);
		domainModel.setErrorLibrary(ERRORLIB_PROJECT_NAME);
		domainModel.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		domainModel.setOrganization(ErrorLibraryConstants.ERRORLIB_ORG);
		domainModel
				.setPackageName(ErrorLibraryConstants.ERRORLIB_STANDARD_PACKAGE);

		try {
			ErrorDomainCreator.createErrorDomain(domainModel,
					ProgressUtil.getDefaultMonitor(null));
			//TurmericErrorRegistry.refresh();
		} catch (Exception e) {
			Assert.fail("Exception with message" + e.getMessage()
					+ ", occured during " + "domain creation");
		}
		errorModel = new ErrorParamModel();
		errorModel.setCategory(ErrorLibraryConstants.ERROR_CATEGORY_APP);
		errorModel.setDomain(ERRORLIB_DOMAIN);
		errorModel
				.setErrorID(Integer.toString((new Random().nextInt(1000000))));
		errorModel.setErrorLibrary(ERRORLIB_PROJECT_NAME);
		errorModel.setMessage(ERROR_MESSAGE);
		errorModel.setName(ERROR_NAME);
		errorModel.setOrganization(ErrorLibraryConstants.ERRORLIB_ORG);
		errorModel.setResolution(ERROR_RESOLUTION);
		errorModel.setSubdomain(ERROR_SUBDOMAIN);
		errorModel.setSeverity(ErrorLibraryConstants.ERROR_SEVERITY_ERROR);
		
		try {
			ErrorTypeCreator.createErrorType(errorModel,
					ProgressUtil.getDefaultMonitor(null));
			;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception with message" + e.getMessage()
					+ ", occured during " + "error creation");
		}
	}
	

	@Test
	public void deleteDomainTest() throws Exception{
		
		final ISOAErrDomain domain = TurmericErrorRegistry.getErrorDomainByName(ERRORLIB_DOMAIN);
		final IProject project = WorkspaceUtil.getProject(
				domain.getLibrary().getName());
		IFolder domainFolder = TurmericErrorLibraryUtils.getErrorDomainFolder(project, domain.getName());
		FileUtils.deleteDirectory(domainFolder.getLocation().toFile());
		WorkspaceUtil.refresh(domainFolder.getParent());
		TurmericErrorRegistry.removeErrorDomain(domain);
		//update library property file
		TurmericErrorLibraryUtils.removeDomainFromProps(project, domain.getName());
		
		IStatus  status  = EclipseMessageUtils.createErrorStatus(
				StringUtil.formatString(SOAMessages.ERROR_NO_ERRORDOMAIN_FOLDER, 
						domainFolder.getLocation()));
		
		Properties srcProp = new Properties();
		InputStream ins = null;
		try {
			ins = new FileInputStream(new File(ERROR_PROPS));
			srcProp.load(ins);
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			IOUtils.closeQuietly(ins);
		}
		Assert.assertNull(srcProp.get("listOfDomains"));
		
		System.out.println(status.getMessage());
		Assert.assertFalse(status.isOK());
		
		
	}
		
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
		

}
