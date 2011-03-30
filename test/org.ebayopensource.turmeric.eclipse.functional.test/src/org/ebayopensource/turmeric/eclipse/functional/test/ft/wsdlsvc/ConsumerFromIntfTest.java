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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericOrganizationProvider;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericRepositorySystem;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assume.*;


/**
 * @author shrao
 * 
 */
public class ConsumerFromIntfTest extends AbstractTestCase {

	public static String PARENT_DIR = org.eclipse.core.runtime.Platform
			.getLocation().toOSString();
	static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("CalcService.wsdl");
	static String publicServiceName = null;
	static String adminName = null;
	final String namespacePart = "blogs";
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/ConsumerFromIntfTest.zip",dataDirectory +"/extractedData");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUpBeforeClass() throws Exception {
	
		
		// Thread.sleep(600000);
		SimpleTestUtil.setAutoBuilding(false);
		// Thread.sleep(60000);
		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		publicServiceName = ServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);
		adminName = ServicesUtil.getAdminName(publicServiceName);
		System.out.println(" --- WSDL FILE : " + WSDL_FILE);
		System.out.println(" ---  Service Admin Name : " + adminName);

		ProjectUtil.cleanUpWS();
		// EBoxServiceSetupCleanupValidate.cleanupWSConsumer(eBoxServiceName);
		ServiceSetupCleanupValidate.cleanup(adminName);
		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}
	
	/*@Override
	public void setUp() throws Exception {
		super.setUp();
		// Thread.sleep(600000);
		SimpleTestUtil.setAutoBuilding(false);
		// Thread.sleep(60000);
		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		eBoxServiceName = EBoxServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);
		eBoxServiceName = ServicesUtil.getAdminName(eBoxServiceName);
		System.out.println(" --- WSDL FILE : " + WSDL_FILE);
		System.out.println(" --- eBox Service name : " + eBoxServiceName);

		ProjectUtil.cleanUpWS();
		// EBoxServiceSetupCleanupValidate.cleanupWSConsumer(eBoxServiceName);
		EBoxServiceSetupCleanupValidate.cleanup(eBoxServiceName);
		EBoxFunctionalTestHelper.ensureM2EcipseBeingInited();
	}*/

	
	@Test
	@Ignore("failing")
	public void testConsumeCalculatorSvc() throws Exception {

		// Turn on the auto-build for the builders to kick-in
		SimpleTestUtil.setAutoBuilding(true);

		try {
			Boolean b = createServiceFromWsdl(new File(
					WSDL_FILE).toURI().toURL(),namespacePart);
			
		
			Assert.assertTrue(adminName + "  creation failed", b);
			
			StringBuffer failMessages = ProjectArtifactValidator.getErroredFileMessage();
			
			ProjectArtifactValidator.getErroredFileMessage().setLength(0);
			// validate artifacts
			boolean intfMatch = ServiceSetupCleanupValidate
					.validateIntfArtifacts(
							WorkspaceUtil.getProject(adminName),
							adminName);
			Assert.assertTrue(" --- Service artifacts validation failed for "+ failMessages,
					intfMatch);

			
			String consumerId = "CalcConsumer_Id";
			List<String> environment = new ArrayList<String>();
			environment.add("production");
			ConsumerFromJavaParamModel model = new ConsumerFromJavaParamModel();
			model.setBaseConsumerSrcDir("src");
			model.setClientName(adminName + "Consumer");
			ArrayList<String> list = new ArrayList<String>();
			list.add(adminName);
			model.setServiceNames(list);
			model.setParentDirectory(PARENT_DIR);
			model.setConsumerId(consumerId);
			model.setEnvironments(environment);
	
			SimpleTestUtil.setAutoBuilding(false);

			ServiceCreator.createConsumerFromJava(model,
					ProgressUtil.getDefaultMonitor(null));
			SimpleTestUtil.setAutoBuilding(true);
			// Add validation for the expected artifacts and contents..
			boolean consumerMatch = ServiceSetupCleanupValidate
					.validateConsumerArtifacts(
							WorkspaceUtil.getProject(adminName
									+ "Consumer"), adminName + "Consumer");
			
			
			
			System.out.println(failMessages.toString());
			Assert.assertTrue(" --- Service artifacts validation failed for " + failMessages.toString(),
					consumerMatch);
			ProjectArtifactValidator.getErroredFileMessage().setLength(0);
			
			

			// we know that the CC.xml or GCC.xml is the last artifact to be
			// created
			// The project exists as
			IProject consProject = WorkspaceUtil.getProject(model
					.getClientName());

			consProject.build(IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			// if project build goes through, its a successful test
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		catch (Exception ex) {
			System.out.println("--- Exception in consumeCalculatorSvc() - "
					+ ex.getMessage());
			System.out
					.println("--- Exception in consumeCalculatorSvc() toString - "
							+ ex.toString());
			System.out
					.println("--- Exception in consumeCalculatorSvc() getCause - "
							+ ex.getCause().getMessage());
			Assert.fail("Exception in consumeCalculatorSvc() - "
					+ ex.getLocalizedMessage());
		}
	}
	
	
	public static boolean createServiceFromWsdl(URL wsdlUrl, String nsPart) throws Exception {
		try {
			final ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
			// final String nsPart = functionalDomain.toLowerCase();

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
			SimpleTestUtil.setAutoBuilding(true);
			WorkspaceUtil.getProject(model.getServiceName()).build(
					IncrementalProjectBuilder.FULL_BUILD, monitor);
			WorkspaceUtil.getProject(model.getImplName()).build(
					IncrementalProjectBuilder.FULL_BUILD, monitor);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
