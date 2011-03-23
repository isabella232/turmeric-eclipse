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
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.DeleteErrorNodeAction;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
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


public class DeleteError extends AbstractTestCase {
	
	ErrorLibraryParamModel model = null;
	DomainParamModel domainModel = null;
	ErrorParamModel errorModel = null;
	
	public  final String ERRORLIB_PROJECT_NAME = "TestErrorLibrary2";
	public  final String ERRORLIB_DOMAIN = "TestDomain1";
	public  final String ERROR_MESSAGE = "Test Message";
	public  final String ERROR_NAME = "TestError";
	public  final String ERROR_RESOLUTION = "Test Resolution";
	public  final String ERROR_SUBDOMAIN = "Buying";
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
	public void deleteErrorTest() throws Exception{
		
		Thread.sleep(10000);
		final ISOAError error = TurmericErrorRegistry.getErrorByName(ERRORLIB_DOMAIN,ERROR_NAME);
		final ISOAErrDomain domain = error.getDomain();
		final IProject project = WorkspaceUtil.getProject(
				domain.getLibrary().getName());
		IFolder domainFolder = TurmericErrorLibraryUtils.getErrorDomainFolder(
				project, domain.getName());
		WorkspaceUtil.refresh(project);
		TurmericErrorLibraryUtils.removeErrorFromXmlData(domainFolder, error); //delete from xml data
		Thread.sleep(3000);
		WorkspaceUtil.refresh(project);
		TurmericErrorLibraryUtils.removeErrorFromPropsFile(domainFolder, error); //delete from property file
		WorkspaceUtil.refresh(domainFolder.getParent());
		TurmericErrorRegistry.removeError(domain.getName(), error);
	
		
		
	}
	
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
