/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.internal.util;

import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestEclipseUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#workspace()}.
	 */
	@Test
	public void testWorkspace() {
		Assert.assertSame(ResourcesPlugin.getWorkspace(), 
				EclipseUtil.workspace());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#workspaceRoot()}.
	 */
	@Test
	public void testWorkspaceRoot() {
		Assert.assertSame(ResourcesPlugin.getWorkspace().getRoot(), 
				EclipseUtil.workspaceRoot());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#projects()}.
	 */
	@Test
	public void testProjects() {
		IProject[] projects = EclipseUtil.projects();
		Assert.assertNotNull(projects);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#getProject(java.lang.String)}.
	 */
	@Test
	public void testGetProject() {
		IProject project = EclipseUtil.getProject("AndroidProject");
		Assert.assertNotNull(project);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#project(java.lang.String)}.
	 */
	@Test
	public void testProject() {
		IProject project = EclipseUtil.project("AndroidProject");
		Assert.assertNotNull(project);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#getEclipseVersion()}.
	 */
	@Test
	public void testGetEclipseVersion() {
		Assert.assertNotNull(EclipseUtil.getEclipseVersion());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#coreException(java.lang.Throwable)}.
	 */
	@Test
	public void testCoreException() {
		Throwable t = new RuntimeException("Android is far better than iOS");
		Assert.assertSame(t, EclipseUtil.coreException(t).getCause());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#refresh(org.eclipse.core.resources.IResource[])}.
	 */
	@Test
	public void testRefreshIResourceArray() {
		try {
			EclipseUtil.refresh();
			EclipseUtil.refresh(ResourcesPlugin.getWorkspace().getRoot());
		} catch (Exception e) {
			Assert.fail("Refresh test failed:" + e);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#refresh(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.resources.IResource[])}.
	 */
	@Test
	public void testRefreshIProgressMonitorIResourceArray() {
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			EclipseUtil.refresh(monitor);
			EclipseUtil.refresh(monitor, ResourcesPlugin.getWorkspace().getRoot());
		} catch (Exception e) {
			Assert.fail("Refresh test failed:" + e);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil#refresh(org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)}.
	 */
	@Test
	public void testRefreshIResourceIProgressMonitor() {
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			EclipseUtil.refresh(ResourcesPlugin.getWorkspace().getRoot(), monitor);
		} catch (Exception e) {
			Assert.fail("Refresh test failed:" + e);
		}
	}

}
