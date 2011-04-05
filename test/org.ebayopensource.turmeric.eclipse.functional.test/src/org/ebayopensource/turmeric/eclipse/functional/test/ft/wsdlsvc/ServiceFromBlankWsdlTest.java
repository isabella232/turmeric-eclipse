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

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.Binding;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.Operation;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
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
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericRepositorySystem;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author shrao
 * 
 */

public class ServiceFromBlankWsdlTest extends AbstractTestCase {

	final static String TARGET_NAMESPACE = "http://www.ebayopensource.org/turmeric/blogs/v1/services";

	public static String PARENT_DIR = ServiceSetupCleanupValidate
			.getParentDir();
	static String publicServiceName = null;
	static String adminName = null;
	static final String namespacePart = "blogs";
	static final String  domainClassifier ="Blogs";
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/BlankWsdlServiceConsumerTest.zip",dataDirectory +"/extractedData");
		
	}
	
	@Before
	public  void setUpBeforeClass() throws Exception {
	
		
		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		publicServiceName = "Service";
		adminName = ServicesUtil.getAdminName(publicServiceName);
		// eBoxServiceName = ServicesUtil.getAdminName(eBoxServiceName,
		// SoaTestConstants.DOMAIN_CLASSIFIER);
		System.out.println(" ---Service name : " + publicServiceName);

		ProjectUtil.cleanUpWS();
		ServiceSetupCleanupValidate.cleanupWSConsumer(adminName+"Consumer");
		ServiceSetupCleanupValidate.cleanup(adminName);


		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}
	
	/*@Override
	public void setUp() throws Exception {

		super.setUp();
		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		eBoxServiceName = "BlogsServiceV1";
		// eBoxServiceName = ServicesUtil.getAdminName(eBoxServiceName,
		// SoaTestConstants.DOMAIN_CLASSIFIER);
		System.out.println(" --- eBox Service name : " + eBoxServiceName);

		ProjectUtil.cleanUpWS();
		EBoxServiceSetupCleanupValidate.cleanupWSConsumer(eBoxServiceName);
		EBoxServiceSetupCleanupValidate.cleanup(eBoxServiceName);
		SimpleTestUtil.setAutoBuilding(true);

		EBoxFunctionalTestHelper.ensureM2EcipseBeingInited();
	}*/

	public static boolean createServiceFromBlankWsdl(String adminNameService,String publicService) throws Exception {

		try {
			Thread.sleep(5000);
			final ServiceFromTemplateWsdlParamModel model = new ServiceFromTemplateWsdlParamModel();
			final File templateFile = new File(
					WsdlUtilTest
							.getPluginOSPath(
									"org.ebayopensource.turmeric.eclipse.config.imp",
									"templates" + File.separator + "wsdl" + File.separator + "turmeric" + File.separator + "Turmeric_NoOperationTemplate.wsdl"));
			// String publicServiceName =
			// ServicesUtil.getPublicServiceName(serviceName, domainClassifier);
			// String nsPart = StringUtils.lowerCase(domainClassifier);

			String interfacePackage = ServicesUtil.getInterfacePackage(
					publicService, TARGET_NAMESPACE);
			String implClass = SOAServiceUtil.generateServiceImplClassName(
					publicService, adminNameService, TARGET_NAMESPACE);
			List<Operation> operations = new ArrayList<Operation>();
			final Operation op = ServiceFromTemplateWsdlParamModel
					.createOperation("getVersion");
			
			op.getOutputParameter().getElements().get(0).setName("version");
			
			
			operations.add(op);
			final Set<Binding> bindings = new LinkedHashSet<Binding>();
			final Binding binding0 = new Binding(
					SOAProjectConstants.TemplateBinding.values()[0]);
			final Binding binding1 = new Binding(
					SOAProjectConstants.TemplateBinding.values()[1]);
			bindings.add(binding0);
			bindings.add(binding1);
			model.setTemplateFile(templateFile.toURL());
			model.setTargetNamespace(TARGET_NAMESPACE);
			model.setServiceName(adminNameService);
			model.setServiceInterface(interfacePackage);
			model.setWorkspaceRootDirectory(PARENT_DIR);
			model.setServiceImpl(implClass);
			model.setServiceVersion("1.0.0");
			model.setImplName(adminNameService + "Impl");
			model.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.NEW);
			model.setPublicServiceName(publicService);
			model.setServiceLayer("COMMON");
			model.setServiceDomain(domainClassifier);
			model.setNamespacePart(namespacePart);
			model.setOperations(operations);
			model.setBindings(bindings);
			model.setTypeFolding(true);
			model.setTypeNamespace(TARGET_NAMESPACE);
			SimpleTestUtil.setAutoBuilding(false);

			ServiceCreator.createServiceFromBlankWSDL(model,
					ProgressUtil.getDefaultMonitor(null));
			
			SimpleTestUtil.setAutoBuilding(true);
			Thread.sleep(5000);


			WorkspaceUtil.getProject(model.getServiceName()).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(model.getImplName()).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ----" + e);
			return false;
		}
	}

	@Test
	@Ignore("failing")
	public void testEBoxCreateServiceFrmBlankWsdl() throws Exception {

		boolean b = false;
		try {
			b = createServiceFromBlankWsdl(adminName,publicServiceName);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		SimpleTestUtil.setAutoBuilding(true);
		WorkspaceUtil.getProject(adminName).refreshLocal(IResource.DEPTH_INFINITE, null);
		WorkspaceUtil.getProject(adminName).build(IncrementalProjectBuilder.FULL_BUILD, null);
		WorkspaceUtil.getProject(adminName + "Impl").refreshLocal(IResource.DEPTH_INFINITE, null);
		WorkspaceUtil.getProject(adminName + "Impl").build(IncrementalProjectBuilder.FULL_BUILD, null);
		assertTrue("Service creation failed ", b);

		// validate artifacts
		boolean intfMatch = ServiceSetupCleanupValidate
				.validateIntfArtifacts(
						WorkspaceUtil.getProject(adminName),
						adminName);
		boolean implMatch = ServiceSetupCleanupValidate
				.validateImplArtifacts(
						WorkspaceUtil.getProject(adminName + "Impl"),
						adminName + "Impl");
		String failMessages = ProjectArtifactValidator.getErroredFileMessage().toString();
		
		ProjectArtifactValidator.getErroredFileMessage().setLength(0);
		System.out.println(failMessages);
		ServiceSetupCleanupValidate.validateMatch = true;
		
		assertTrue(" --- Service artifacts validation failed " +failMessages.toString() , intfMatch
				&& implMatch);
		
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
}
