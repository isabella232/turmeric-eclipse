/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.plugin;

import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
public class TestProjectUtil {
	
	private static IProject project;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject("TestProject", workspaceRoot, monitor);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		project.delete(true, new NullProgressMonitor());
		project = null;
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil#addNature(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor, java.lang.String[])}.
	 * @throws CoreException 
	 */
	@Test
	public void testAddNature() throws CoreException {
		ProjectUtil.addNature(project, ProgressUtil.getDefaultMonitor(null), JavaCore.NATURE_ID);
		Assert.assertTrue(project.hasNature(JavaCore.NATURE_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil#removeNatures(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor, java.lang.String[])}.
	 * @throws CoreException 
	 */
	@Test
	public void testRemoveNatures() throws CoreException {
		if (project.hasNature(JavaCore.NATURE_ID) == false) {
			ProjectUtil.addNature(project, ProgressUtil.getDefaultMonitor(null), JavaCore.NATURE_ID);
		}
		ProjectUtil.removeNatures(project, ProgressUtil.getDefaultMonitor(null), JavaCore.NATURE_ID);
		Assert.assertFalse(project.hasNature(JavaCore.NATURE_ID));
	}

}
