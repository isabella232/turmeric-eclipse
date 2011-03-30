/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceFromBlankWsdlTest;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceFromWsdlTest;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceSetupCleanupValidate;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.mavenapi.intf.IMavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class ServiceCreationFromExistingTypesTest1 extends
		AbstractTestCase {

	public static String PARENT_DIR = org.eclipse.core.runtime.Platform
			.getLocation().toOSString();
	/*public static String WSDL_FILE = EBoxServiceSetupCleanupValidate
			.getWsdlFilePath("TestSvc1V1.wsdl");*/
	static DialogMonitor monitor;
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUpBefore(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/xsd.zip",dataDirectory +"/extractedData");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUp() throws Exception {
	
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	}

	@Test
	@Ignore("failing")
	public void testCreateServiceFrmNewWsdl() throws IOException, CoreException {
		String eBoxServiceName = TypeLibSetUp.SVC_NAME1 + ServicesUtil.MAJOR_VERSION_PREFIX
		+ ServicesUtil.SERVICE_MAJOR_VERSION;  
		System.out.println(" --- eBox Service name : " + eBoxServiceName);

		try {
			// ProjectUtil.cleanUpWS();
			TypeLibSetUp.setupSvc(eBoxServiceName);
			TypeLibSetUp.setup();

			Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
					+ " -- TypeLibrary Creation failed", TLUtil
					.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_NAME1,
							"1.0.0", "COMMON",
							ServiceSetupCleanupValidate
							.getParentDir()));
			TLUtil.createType("monresType", TypeLibSetUp.TYPELIBRARY_NAME1,
					TypeLibSetUp.TYPELIBRARY_NAME1,
					SOAXSDTemplateSubType.SIMPLE,
					TstConstants.TEMPLATE_SIMPLE_TYPE,
					TstConstants.XSD_STRING);
			TLUtil.createType("hardwareType",
					TypeLibSetUp.TYPELIBRARY_NAME1,
					TypeLibSetUp.TYPELIBRARY_NAME1,
					SOAXSDTemplateSubType.SIMPLE,
					TstConstants.TEMPLATE_SIMPLE_TYPE,
					TstConstants.XSD_STRING);

			ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(
					eBoxServiceName, TypeLibSetUp.SVC_NAME1);
			
			String typeNS = TLUtil.getTargetNamespace(TLUtil.functionDomain);
			// Code to simulate Import action
			Assert.assertTrue("Import type in WSDL failed", TLUtil
					.importAction("WSDL", eBoxServiceName, eBoxServiceName
							+ ".wsdl", "monresType", typeNS));

			Assert.assertTrue("Import type in WSDL failed", TLUtil
					.importAction("WSDL", eBoxServiceName, eBoxServiceName
							+ ".wsdl", "hardwareType", typeNS));

			// Build Projects
			SimpleTestUtil.setAutoBuilding(true);
			WorkspaceUtil.getProject(eBoxServiceName).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(eBoxServiceName + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			Assert.assertTrue("Service Creation with Import action passed",
					true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue("Service Creation with Import action failed",
					false);
		}

		validateProjectArtifacts(eBoxServiceName);

		validatePOM(eBoxServiceName);
	}

	/*
	 * Assert Td.xml is generated with right dependency Assert monrestype and
	 * dependent types are added to the wsdl
	 */
	public void validateProjectArtifacts(String eBoxServiceName) throws IOException, CoreException {
		IProject project = WorkspaceUtil.getProject(eBoxServiceName);
		Assert.assertTrue(
				"TypeDepencies.xml didnot get generated",
				project.getFile(
						"meta-src" + File.separator + "META-INF"
								+ File.separator + eBoxServiceName
								+ File.separator + "TypeDependencies.xml")
						.exists());
		String sb = readContentsFromIFile(SOAServiceUtil
				.getWsdlFile(eBoxServiceName));

		Assert.assertTrue(
				"WSDL file is not inlined with imported type - monresType",
				sb.contains("monresType"));
		Assert.assertTrue(
				"WSDL file is not inlined with imported type - hardwareType",
				sb.contains("hardwareType"));

	}

	/*
	 * Assert pom.xml has library dependency - HardwareTypeLibrary
	 */
	public void validatePOM(String eBoxServiceName) throws CoreException, IOException {
		IProject project = WorkspaceUtil.getProject(eBoxServiceName);
		Assert.assertTrue(
				"Pom.xml doesnot contain library dependency HardwareTypeLibrary",
				readContentsFromIFile(project.getFile("pom.xml")).contains(
						"SOA21TestTL1"));
	}

	/*
	 * Copy the new wsdl with operation Copy the implementation class with
	 * business
	 */
	@Test
	//@Ignore("failed")
	public void testAddImpl() throws Exception {
		try {

			String eBoxServiceName = SOAServiceUtil.computeAdminName(TypeLibSetUp.SVC_NAME1, 
					TLUtil.functionDomain, SOAProjectConstants.DEFAULT_VERSION);
			
			ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(eBoxServiceName,TypeLibSetUp.SVC_NAME1);
			String srcFile1 = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
					"data/extractedData" + File.separator + eBoxServiceName);
			String srcFile2 = srcFile1;
			// Copy the wsdl with new operation
			srcFile1 = srcFile1 + File.separator + eBoxServiceName + ".wsdl";

			String destFile1 = ServiceSetupCleanupValidate.getParentDir() + File.separator
					+ eBoxServiceName + File.separator + "meta-src"
					+ File.separator + "META-INF" + File.separator + "soa"
					+ File.separator + "services" + File.separator + "wsdl"
					+ File.separator + eBoxServiceName + File.separator
					+ eBoxServiceName + ".wsdl";

			FileUtils.copyFile(new File(srcFile1), new File(destFile1));
			
			WorkspaceUtil.refresh(WorkspaceUtil.getProject(eBoxServiceName));

			// Copy the Implementation class with the new operation

			srcFile2 = srcFile2 + File.separator + eBoxServiceName
					+ "Impl.java";

			String destFile2 = TypeLibSetUp.SVC_LOCATION + File.separator
					+ eBoxServiceName + "Impl" + File.separator + "src\\org\\ebayopensource\\turmeric\\blogs\\v1\\services\\testsvc1\\impl\\"+ eBoxServiceName
					+ "Impl.java";

			System.out.println("destFile - " + destFile2);
			FileUtils.copyFile(new File(srcFile2), new File(destFile2));
			WorkspaceUtil.refresh(WorkspaceUtil.getProject(eBoxServiceName));
			SimpleTestUtil.setAutoBuilding(true);
			
			ActionUtil.cleanProject(WorkspaceUtil.getProject(eBoxServiceName), ProgressUtil.getDefaultMonitor(null));
			

			WorkspaceUtil.getProject(eBoxServiceName).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
		

			ActionUtil.cleanProject(WorkspaceUtil.getProject(eBoxServiceName + "Impl"), ProgressUtil.getDefaultMonitor(null));
			
			WorkspaceUtil.getProject(eBoxServiceName + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
		
			
			List<String> environment = new ArrayList<String>();
			environment.add("production");
			Assert.assertTrue("Consumer Creation failed", ServicesUtil
					.createConsumerFrmJava(eBoxServiceName,
							ServiceSetupCleanupValidate.getParentDir(),
							environment));

			IProject consProject = WorkspaceUtil.getProject(eBoxServiceName
					+ "Consumer");
			// ServicesUtil.modifyClientPrjTransport(consProject,
			// eBoxServiceName, SOAProjectConstants.Binding.LOCAL);
			String srcFile11 = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
					"data/extractedData" + File.separator + eBoxServiceName + File.separator
							+ "TestConsumer.java");

			String destFile11 = ServiceSetupCleanupValidate.getParentDir()
					+ File.separator + eBoxServiceName + "Consumer"
					+ File.separator + "src" + File.separator + "com"
					+ File.separator + "ebayopensource" +File.separator + "consumer"
					+ File.separator + "TestConsumer.java";

			FileUtils.copyFile(new File(srcFile11), new File(destFile11));
			
			WorkspaceUtil.refresh(consProject);
			
			ActionUtil.cleanProject(consProject, ProgressUtil.getDefaultMonitor(null));
			
			consProject.build(IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			IProject project =  WorkspaceUtil.getProject(eBoxServiceName + "Consumer");
			
			
			
			String configFileSrc = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
					"test-data" + File.separator + eBoxServiceName + File.separator
							+ "ClientConfig.xml");

			String configFileDest = ServiceSetupCleanupValidate.getParentDir()
					+ File.separator + eBoxServiceName + "Consumer"
					+ File.separator + "meta-src" + File.separator + "META-INF"
					+ File.separator + "soa" +File.separator + "client" +File.separator+"config"+File.separator+eBoxServiceName + "Consumer"+
					File.separator + "production" + File.separator +eBoxServiceName
					+ File.separator + "ClientConfig.xml";

			FileUtils.copyFile(new File(configFileSrc), new File(configFileDest));
			
			
			ActionUtil.generateGlobalClientConfig(WorkspaceUtil.getProject(eBoxServiceName + "Consumer"),
					ProgressUtil.getDefaultMonitor(null));
			
			ActionUtil.generateGlobalServiceConfig(WorkspaceUtil.getProject(eBoxServiceName + "Impl"),
					ProgressUtil.getDefaultMonitor(null));
			
			File pomImpl = new File(ServiceSetupCleanupValidate.getParentDir()+ File.separator + eBoxServiceName + "Impl"+File.separator+"pom.xml");
			MavenEclipseApi mavenapi = new MavenEclipseApi();
			Model modelImpl = mavenapi.parsePom(pomImpl);
			
			Dependency depImpl = new Dependency();
			depImpl.setArtifactId("slf4j-to-jul");
			depImpl.setGroupId("org.ebayopensource.turmeric");
			depImpl.setVersion("1.2-SNAPSHOT");
			depImpl.setScope("compile");
			depImpl.setType("jar");
			
			modelImpl.getDependencies().add(depImpl);
			mavenapi.writePom(project.getFile(project.getProjectRelativePath()+File.separator+"pom.xml"),modelImpl);
			
			
			File pom = new File(ServiceSetupCleanupValidate.getParentDir()+ File.separator + eBoxServiceName + "Consumer"+File.separator+"pom.xml");
			Model modelConsumer = mavenapi.parsePom(pom);
			
			Dependency depConsumer = new Dependency();
			depConsumer.setArtifactId("slf4j-to-jul");
			depConsumer.setGroupId("org.ebayopensource.turmeric");
			depConsumer.setVersion("1.2-SNAPSHOT");
			depConsumer.setScope("compile");
			depConsumer.setType("jar");
			
			
			modelConsumer.getDependencies().add(depConsumer);
			mavenapi.writePom(project.getFile(project.getProjectRelativePath()+File.separator+"pom.xml"),modelConsumer);
			mavenapi.refreshAllIndices();
			
			Thread.sleep(5000);
			
			String[] outSb = ServicesUtil.invokeConsumer(consProject);
				assertTrue("The consumer invocation failed with errors",
					(outSb[0].indexOf("EXCEPTION") < 0));
			assertTrue("The consumer invocation failed with errors",
					(outSb[0].indexOf("Exception") < 0));
			assertTrue("The consumer invocation failed with errors",
					(outSb[0].indexOf("ERROR") < 0));
			assertTrue("The consumer invocation failed with errors", (outSb[1]
					.toString().indexOf("EXCEPTION") < 0));
			assertTrue("The consumer invocation failed with errors", (outSb[1]
					.toString().indexOf("ERROR") < 0));
			assertTrue("The consumer invocation failed with errors", (outSb[1]
					.toString().indexOf("Exception") < 0));
			assertTrue("The consumer invocation failed with errors", (outSb[1]
					.toString().indexOf("The java class is not found") < 0));
			
			
			
			
			
			
			
			Assert.assertTrue("Build with new operation failed", true);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Assert.fail("Build with new operation failed->" + e.getLocalizedMessage());
		} catch (CoreException e) {
			System.err.println(e.getMessage());
			Assert.fail("Build with new operation failed->" + e.getLocalizedMessage());
		}
	}
	
	public void addDepToPom(){
		
	
		
		ServiceSetupCleanupValidate.getParentDir();
		
		
	}

	@Ignore("to be removed")
	@Test
	public void testConsumerCreation() throws Exception {
		String eBoxServiceName = SOAServiceUtil.computeAdminName(TypeLibSetUp.SVC_NAME1, 
				TLUtil.functionDomain, SOAProjectConstants.DEFAULT_VERSION); 

		ServiceSetupCleanupValidate.cleanup(eBoxServiceName);
		String wsdlFilePath = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"test-data" + File.separator + eBoxServiceName) + File.separator + eBoxServiceName + ".wsdl";
		String nsPart = TLUtil.functionDomain.toLowerCase(Locale.US);
		Assert.assertTrue(ServiceFromWsdlTest.createServiceFromWsdl(new File(wsdlFilePath).toURI().toURL(), nsPart));

		List<String> environment = new ArrayList<String>();
		environment.add("production");
		Assert.assertTrue("Consumer Creation failed", ServicesUtil
				.createConsumerFrmJava(eBoxServiceName,
						ServiceSetupCleanupValidate.getParentDir(),
						environment));

		IProject consProject = WorkspaceUtil.getProject(eBoxServiceName
				+ "Consumer");
		// ServicesUtil.modifyClientPrjTransport(consProject,
		// eBoxServiceName, SOAProjectConstants.Binding.LOCAL);
		String srcFile1 = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"test-data" + File.separator + eBoxServiceName + File.separator
						+ "TestConsumer.java");

		String destFile1 = ServiceSetupCleanupValidate.getParentDir()
				+ File.separator + eBoxServiceName + "Consumer"
				+ File.separator + "src" + File.separator + "com"
				+ File.separator + "ebay" + File.separator + "marketplace"
				+ File.separator + "services" + File.separator + "gen"
				+ File.separator + "Consumer.java";

		FileUtils.copyFile(new File(srcFile1), new File(destFile1));
		
		WorkspaceUtil.refresh(consProject);
		consProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		
		String[] outSb = ServicesUtil.invokeConsumer(consProject);
			assertTrue("The consumer invocation failed with errors",
				(outSb[0].indexOf("EXCEPTION") < 0));
		assertTrue("The consumer invocation failed with errors",
				(outSb[0].indexOf("Exception") < 0));
		assertTrue("The consumer invocation failed with errors",
				(outSb[0].indexOf("ERROR") < 0));
		assertTrue("The consumer invocation failed with errors", (outSb[1]
				.toString().indexOf("EXCEPTION") < 0));
		assertTrue("The consumer invocation failed with errors", (outSb[1]
				.toString().indexOf("ERROR") < 0));
		assertTrue("The consumer invocation failed with errors", (outSb[1]
				.toString().indexOf("Exception") < 0));
		assertTrue("The consumer invocation failed with errors", (outSb[1]
				.toString().indexOf("The java class is not found") < 0));
	}

	public static String readContentsFromIFile(IFile file)
			throws CoreException, IOException {
		InputStream is = file.getContents();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");

		}
		reader.close();
		return sb.toString();

	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
	
}
