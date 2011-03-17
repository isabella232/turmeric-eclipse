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
import java.net.URL;
import java.util.Map;

import javax.wsdl.Definition;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.util.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericOrganizationProvider;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericRepositorySystem;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assume.*;
import static org.junit.Assert.*;


/**
 * @author shrao
 * 
 */

public class ServiceFromWsdlTest extends AbstractTestCase {

	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	public static String PARENT_DIR = ServiceSetupCleanupValidate
			.getParentDir();
	public static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("JunitEndTest.wsdl");
	static String publicServiceName = null;
	static String adminName = null;
	public final String namespacePart = "blogs";

	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/JunitTestWsdlConsumerTest.zip",dataDirectory +"/extractedData");
		
	}	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUpBeforeClass() throws Exception {
	
		//ZipExtractor zip = new ZipExtractor();
		//zip.extract(dataDirectory+"/test-data.zip",dataDirectory);
		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		publicServiceName = ServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);
		adminName = ServicesUtil.getAdminName(publicServiceName);
		System.out.println(" --- WSDL FILE : " + WSDL_FILE);
		System.out.println(" ---  Service Admin Name : " + adminName);

	
		ProjectUtil.cleanUpWS();
		ServiceSetupCleanupValidate.cleanup(adminName);

		FunctionalTestHelper.ensureM2EcipseBeingInited();
		
		
	}
	
	
	

	public static boolean createServiceFromWsdl(URL wsdlUrl, String nsPart) throws Exception {
		try {
			final ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
			

			Definition wsdl = WSDLUtil.readWSDL(wsdlUrl.getFile());
			final String publicService = WSDLUtil
					.getServiceNameFromWSDL(wsdl);
			final String serviceVersion = "1.0.0";
			final String targetNS = WSDLUtil.getTargetNamespace(wsdl);
			
			if (StringUtils.isBlank(nsPart)) {
				nsPart = TurmericOrganizationProvider.INSTANCE.getNamespacePartFromTargetNamespace(targetNS);
			}
			String serviceName = SOAServiceUtil.computeAdminName(
					publicService, nsPart, serviceVersion);
			
			
			model.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.EXISTIING);
			model.setOriginalWsdlUrl(wsdlUrl);
			model.setWorkspaceRootDirectory(PARENT_DIR);
			model.setServiceVersion(serviceVersion);
			model.setServiceName(serviceName);
			model.setPublicServiceName(publicService);
			if (StringUtils.isNotBlank(nsPart)) {
				model.setServiceDomain(StringUtils.capitalize(nsPart));
				model.setNamespacePart(nsPart);
			}
			
			model.setTargetNamespace(targetNS);
			
			model.setBaseConsumerSrcDir(SOAProjectConstants.DEFAULT_BASE_CONSUMER_SOURCE_DIRECTORY);
			model.setTypeFolding(true);

			model.setServiceInterface(ServicesUtil.getInterfacePackage(
					publicService, targetNS));
			System.out.println("model.....Intf" + model.getServiceInterface());
			String implClass = SOAServiceUtil.generateServiceImplClassName(
					publicService, serviceName, targetNS);
			model.setServiceImpl(implClass);
			System.out.println("model.....Impl" + model.getServiceImpl());

			model.setImplName(serviceName + "Impl");

			model.setServiceLayer(SOAProjectConstants.ServiceLayer.COMMON
					.toString());
			model.setTypeNamespace(targetNS);
			Map<String, String> namespaceToPacakgeMappings = ServicesUtil
					.getNamespaceToPackage(wsdlUrl.getFile());
			model.setNamespaceToPacakgeMappings(namespaceToPacakgeMappings);

			SimpleTestUtil.setAutoBuilding(false);

			IProgressMonitor monitor = ProgressUtil.getDefaultMonitor(null);

			ServiceCreator.createServiceFromExistingWSDL(model, monitor);
		
			WorkspaceUtil.getProject(model.getServiceName()).build(
					IncrementalProjectBuilder.FULL_BUILD, monitor);
			WorkspaceUtil.getProject(model.getImplName()).build(
					IncrementalProjectBuilder.FULL_BUILD, monitor);

			return true;
		} catch (Exception e) {
			return false;
		}
	}


	@Test

	public void testCreateSvcFrmExistingWsdl() throws Exception {
		
		boolean b = false;
		try {
		 b = createServiceFromWsdl((new File(WSDL_FILE)).toURI().toURL(),namespacePart);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		
		SimpleTestUtil.setAutoBuilding(true);
		assertTrue(adminName + " Service creation failed", b);
		// validate artifacts
		boolean implMatch = ServiceSetupCleanupValidate
		.validateImplArtifacts(
				WorkspaceUtil.getProject(adminName + "Impl"),
				adminName + "Impl");
		boolean intfMatch = ServiceSetupCleanupValidate
				.validateIntfArtifacts(
						WorkspaceUtil.getProject(adminName),
						adminName);
		StringBuffer failMessages = ProjectArtifactValidator.getErroredFileMessage();
		
		System.out.println(failMessages.toString());
		assertTrue(" --- Service artifacts validation failed " +failMessages.toString(), intfMatch
				&& implMatch);
		ProjectArtifactValidator.getErroredFileMessage().setLength(0);
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}