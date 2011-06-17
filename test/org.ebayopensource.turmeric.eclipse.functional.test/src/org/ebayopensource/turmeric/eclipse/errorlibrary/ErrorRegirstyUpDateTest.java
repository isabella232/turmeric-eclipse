/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ErrorRegirstyUpDateTest extends AbstractTestCase {

	ErrorLibraryParamModel model = null;	
	DomainParamModel domainModel = null;
	ErrorParamModel errorModel = null;
	public static final String ERRORLIB_PROJECT_NAME = "TestErrorLibrary3";
	public static final String ERRORDOMAIN_NAME = "TestDomain1";
	public static final String ERROR_MESSAGE = "Test Message";
	public static final String ERROR_NAME = "TestError";
	public static final String ERROR_RESOLUTION = "Test Resolution";
	public static final String ERROR_SUBDOMAIN = "Buying";
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/ErrorLibraryTestData.zip",dataDirectory +"/extractedData");
		
	}
	

	
	@Before
	public void init() throws Exception{
		
		model = new ErrorLibraryParamModel();
		model.setWorkspaceRootDirectory(ErrorLibraryConstants.ERRORLIB_LOCATION);
		model.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		model.setProjectName(ERRORLIB_PROJECT_NAME);
		model.setVersion(ErrorLibraryConstants.ERRORLIB_VERSION);
		
		
		domainModel = new DomainParamModel();
		domainModel.setDomain(ERRORDOMAIN_NAME);
		domainModel.setErrorLibrary(ERRORLIB_PROJECT_NAME);
		domainModel.setLocale(ErrorLibraryConstants.ERRORLIB_LOCALE);
		domainModel.setOrganization(ErrorLibraryConstants.ERRORLIB_ORG);
		domainModel
				.setPackageName(ErrorLibraryConstants.ERRORLIB_STANDARD_PACKAGE);
		
		errorModel = new ErrorParamModel();
		errorModel.setCategory(ErrorLibraryConstants.ERROR_CATEGORY_APP);
		errorModel.setDomain(ERRORDOMAIN_NAME);
		errorModel
				.setErrorID(Integer.toString((new Random().nextInt(1000000))));
		errorModel.setErrorLibrary(ERRORLIB_PROJECT_NAME);
		errorModel.setMessage(ERROR_MESSAGE);
		errorModel.setName(ERROR_NAME);
		errorModel.setOrganization(ErrorLibraryConstants.ERRORLIB_ORG);
		errorModel.setResolution(ERROR_RESOLUTION);
		errorModel.setSubdomain(ERROR_SUBDOMAIN);
		errorModel.setSeverity(ErrorLibraryConstants.ERROR_SEVERITY_ERROR);
	
	}

	@Test
	public void testErrorRegirstryUpdateForDomain(){
		
		
		try{
		ErrorLibraryCreator.createErrorLibrary(model, ProgressUtil.getDefaultMonitor(null));
		
		assertEquals("Error library does not exist in registry",ERRORLIB_PROJECT_NAME,TurmericErrorRegistry.getErrorLibraryByName(ERRORLIB_PROJECT_NAME).getName());
		}
		catch(Exception e){
			fail("Exception with message" +e.getMessage()+ ", occured during "+
					"error library creation");
		}
	}
	
	@Test
	public void testErrorRegistryUpdateForDomain(){
		
		try{
			ErrorLibraryCreator.createErrorLibrary(model, ProgressUtil.getDefaultMonitor(null));
			
			}
			catch(Exception e){
				fail("Exception with message" +e.getMessage()+ ", occured during "+
						"error library creation");
			}
		
		try {
			ErrorDomainCreator.createErrorDomain(domainModel,
					ProgressUtil.getDefaultMonitor(null));
			assertEquals("Error Domain does not exist in registry",ERRORDOMAIN_NAME,TurmericErrorRegistry.getErrorDomainByName(ERRORDOMAIN_NAME).getName());
		} catch (Exception e) {
			fail("Exception with message" +e.getMessage()+ ", occured during "+
					"error library creation");
		}
		
	}
	

	//@Ignore ("Currently fails under Hudson CI Server")
	@Test
	public void testErrorRegirstyUpdateForError(){
		
		try{
			ErrorLibraryCreator.createErrorLibrary(model, ProgressUtil.getDefaultMonitor(null));
			
			}
			catch(Exception e){
				fail("Exception with message" +e.getMessage()+ ", occured during "+
						"error library creation");
			}
		
		try {
			ErrorDomainCreator.createErrorDomain(domainModel,
					ProgressUtil.getDefaultMonitor(null));
	
		} catch (Exception e) {
			fail("Exception with message" +e.getMessage()+ ", occured during "+
					"error library creation");
		}
		
		try {
			ErrorTypeCreator.createErrorType(errorModel,
					ProgressUtil.getDefaultMonitor(null));
			
			assertEquals("Error does not exist in registry",ERROR_NAME,TurmericErrorRegistry.getErrorByName(ERRORDOMAIN_NAME,ERROR_NAME).getName());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception with message" + e.getMessage()
					+ ", occured during " + "error creation");
		}
		
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
	
}
