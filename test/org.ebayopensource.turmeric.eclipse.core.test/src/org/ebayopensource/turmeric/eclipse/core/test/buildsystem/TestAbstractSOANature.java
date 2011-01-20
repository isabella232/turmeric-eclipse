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
package org.ebayopensource.turmeric.eclipse.core.test.buildsystem;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestAbstractSOANature {
	private static final String TEST_BUILDER_NAME = "org.ebayopensource.turmeric.eclipse.core.test.TestSOAProjectBuilder";
	private static final String PROJECT_NAME = "TestProject";
	private static IProject project;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject(PROJECT_NAME, workspaceRoot, monitor);
		
		final IProjectDescription description = project.getDescription();
		final List<String> natureIDs = ListUtil.array(description
				.getNatureIds());
		natureIDs.add(JavaCore.NATURE_ID);
		natureIDs.add(TestSOANature.NATURE_ID);
		description.setNatureIds(natureIDs.toArray(new String[0]));
		project.setDescription(description, monitor);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//remove the proejct
		project.delete(true, new NullProgressMonitor());
		project = null;
	}
	
	@Test
	public void testHasNature() throws Exception {
		Assert.assertTrue(project.hasNature(TestSOANature.NATURE_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature#configure()}.
	 */
	@Test
	public void testConfigure() throws Exception {
		IProjectNature nature = project.getNature(TestSOANature.NATURE_ID);
		Assert.assertNotNull(nature);
		nature.configure();
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature#deconfigure()}.
	 */
	@Test
	public void testDeconfigure() throws Exception {
		IProjectNature nature = project.getNature(TestSOANature.NATURE_ID);
		Assert.assertNotNull(nature);
		nature.deconfigure();
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature#getProject()}.
	 */
	@Test
	public void testGetProject() throws Exception {
		IProjectNature nature = project.getNature(TestSOANature.NATURE_ID);
		Assert.assertNotNull(nature);
		Assert.assertSame(project, nature.getProject());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature#setProject(org.eclipse.core.resources.IProject)}.
	 */
	@Test
	public void testSetProject() throws Exception {
		IProjectNature nature = project.getNature(TestSOANature.NATURE_ID);
		Assert.assertNotNull(nature);
		nature.setProject(project);
		Assert.assertSame(project, nature.getProject());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature#getBuilderName()}.
	 */
	@Test
	public void testGetBuilderName() throws Exception {
		AbstractSOANature nature = (AbstractSOANature)project.getNature(TestSOANature.NATURE_ID);
		Assert.assertNotNull(nature);
		Assert.assertEquals(TEST_BUILDER_NAME, nature.getBuilderName());
	}
	
	public static class TestSOANature extends AbstractSOANature {
		public static final String NATURE_ID = "org.ebayopensource.turmeric.eclipse.core.test.TestSOANature";

		@Override
		public String getBuilderName() {
			return TEST_BUILDER_NAME;
		}
		
	}
}
