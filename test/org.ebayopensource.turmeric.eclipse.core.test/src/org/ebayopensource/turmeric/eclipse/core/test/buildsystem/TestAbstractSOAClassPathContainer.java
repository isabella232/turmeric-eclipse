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

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOAClassPathContainer;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestAbstractSOAClassPathContainer {
	private static IProject project;
	private static TestSOAClassPathContainer container;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject("TestProject", workspaceRoot, monitor);
		final IProjectDescription description = project.getDescription();
		final List<String> natureIDs = ListUtil.array(description
				.getNatureIds());
		natureIDs.add(JavaCore.NATURE_ID);
		description.setNatureIds(natureIDs.toArray(new String[0]));
		project.setDescription(description, monitor);
		container = new TestSOAClassPathContainer(null, JavaCore.create(project));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container = null;
		project.delete(true, new NullProgressMonitor());
		project = null;
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOAClassPathContainer#getClasspathEntries()}.
	 */
	@Test
	public void testGetClasspathEntries() {
		Assert.assertNotNull(container);
		Assert.assertNotNull(container.getClasspathEntries());
		Assert.assertTrue(container.getClasspathEntries().length > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOAClassPathContainer#getUniqueClasspathEntries()}.
	 */
	@Test
	public void testGetUniqueClasspathEntries() throws Exception {
		Assert.assertNotNull(container);
		Assert.assertNotNull(container.getUniqueClasspathEntries());
		Assert.assertTrue(container.getUniqueClasspathEntries().length > 0);
	}
	
	public static class TestSOAClassPathContainer extends AbstractSOAClassPathContainer {

		public TestSOAClassPathContainer(IPath path, IJavaProject javaProject) {
			super(path, javaProject);
		}

		@Override
		public IClasspathEntry[] getClasspathEntries() {
			return new IClasspathEntry[]{JavaCore.newSourceEntry(project.getPath().append("src"))
					, JavaCore.newSourceEntry(project.getPath())};
		}
		
	}

}
