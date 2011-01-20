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

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.util.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericRepositorySystem;
import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author shrao
 * 
 */
public class JunitTestWsdlConsumerTest extends AbstractTestCase {

	public static String PARENT_DIR = org.eclipse.core.runtime.Platform
			.getLocation().toOSString();
	public static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("JunitEndTest.wsdl");
	static String publicServiceName = null;
	static String adminName = null;
	static IProject consProject = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUpBeforeClass() throws Exception {
		
		
	
		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		publicServiceName = ServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);
		adminName = ServicesUtil.getAdminName(publicServiceName);
		System.out.println(" --- WSDL FILE : " + WSDL_FILE);
		System.out.println(" ---Service Admin Name : " + adminName);

		ProjectUtil.cleanUpWS();

		ServiceSetupCleanupValidate.cleanup(adminName);
		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}
	
	

	
	@Test
	public void testJnitTestConsumerCreation() throws Exception {

		
		try {
		ConsumerFromWsdlTest.createConsumerFromWsdl(new File(WSDL_FILE)
				.toURI().toURL(),adminName);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		SimpleTestUtil.setAutoBuilding(true);
		// we know that the CC.xml or GCC.xml is the last artifact to be created
		// The project exists as
		consProject = WorkspaceUtil.getProject(adminName + "Consumer");

		boolean intfMatch = ServiceSetupCleanupValidate
				.validateIntfArtifacts(
						WorkspaceUtil.getProject(adminName),
						adminName);
		boolean consumerMatch = ServiceSetupCleanupValidate
				.validateConsumerArtifacts(consProject, adminName
						+ "Consumer");
		
		String failMessages = ProjectArtifactValidator.getErroredFileMessage().toString();
		ServiceSetupCleanupValidate.validateMatch = true;
		ProjectArtifactValidator.getErroredFileMessage().setLength(0);
		System.out.println(failMessages);
		assertTrue(" --- Service artifacts validation failed " + failMessages.toString(), intfMatch
				&& consumerMatch);
		
	}

}
