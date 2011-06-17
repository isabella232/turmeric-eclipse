/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.plugin;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.test.Activator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestJDTUtil {
	private static IProject project;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject(TestJDTUtil.class.getSimpleName() + "Project", 
				workspaceRoot, monitor);
		final IProjectDescription description = project.getDescription();
		final List<String> natureIDs = ListUtil.array(description
				.getNatureIds());
		natureIDs.add(JavaCore.NATURE_ID);
		description.setNatureIds(natureIDs.toArray(new String[0]));
		project.setDescription(description, monitor);
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
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#validateIdentifier(java.lang.String)}.
	 */
	@Test
	public void testValidateIdentifier() {
		Assert.assertFalse(JDTUtil.validateIdentifier("**xxx").isOK());
		Assert.assertTrue(JDTUtil.validateIdentifier("NikonClass").isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#validateJavaTypeName(java.lang.String)}.
	 */
	@Test
	public void testValidateJavaTypeName() {
		Assert.assertFalse(JDTUtil.validateJavaTypeName("**xxx").isOK());
		Assert.assertTrue(JDTUtil.validateJavaTypeName("NikonClass").isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#validateMethodName(java.lang.String)}.
	 */
	@Test
	public void testValidateMethodName() {
		Assert.assertFalse(JDTUtil.validateMethodName("**xxx").isOK());
		Assert.assertTrue(JDTUtil.validateMethodName("isNikonClass").isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#validatePacakgeName(java.lang.String)}.
	 */
	@Test
	public void testValidatePacakgeName() {
		Assert.assertFalse(JDTUtil.validatePacakgeName("**xxx").isOK());
		Assert.assertTrue(JDTUtil.validatePacakgeName("com.nikon.dslr").isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#addJavaSupport(org.eclipse.core.resources.IProject, java.util.List, java.lang.String, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 */
	@Test
	public void testAddJavaSupport() throws CoreException {
		JDTUtil.addJavaSupport(project, ListUtil.arrayList("src"), "1.6", 
				"build/classes", ProgressUtil.getDefaultMonitor(null));
		Assert.assertTrue(project.hasNature(JavaCore.NATURE_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#resolveClasspathToURLs(org.eclipse.core.resources.IProject)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#resolveClasspathToURLs(org.osgi.framework.Bundle, org.eclipse.core.resources.IProject)}.
	 * @throws Exception 
	 */
	@Test
	public void testResolveClasspathToURLsIProject() throws Exception {
		Set<URL> urls = JDTUtil.resolveClasspathToURLs(Activator.getDefault().getBundle(), project);
		Assert.assertNotNull(urls);
		Assert.assertFalse(urls.isEmpty());
		
		urls = JDTUtil.resolveClasspathToURLs(project);
		Assert.assertNotNull(urls);
		Assert.assertFalse(urls.isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#addNatures(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor, java.lang.String[])}.
	 * @throws CoreException 
	 */
	@Test
	public void testAddNatures() throws CoreException {
		IProgressMonitor monitor = ProgressUtil.getDefaultMonitor(null);
		if (project.hasNature(JavaCore.NATURE_ID)) {
			ProjectUtil.removeNatures(project, monitor, JavaCore.NATURE_ID);
		}
		JDTUtil.addJavaNature(project, monitor);
		Assert.assertTrue(project.hasNature(JavaCore.NATURE_ID));
		ProjectUtil.removeNatures(project, monitor, JavaCore.NATURE_ID);
		JDTUtil.addNatures(project, monitor, JavaCore.NATURE_ID);
		Assert.assertTrue(project.hasNature(JavaCore.NATURE_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#rawClasspath(org.eclipse.core.resources.IProject, boolean)}.
	 * @throws JavaModelException 
	 */
	@Test
	public void testRawClasspathIProjectBoolean() throws JavaModelException {
		List<IClasspathEntry> entries = JDTUtil.rawClasspath(project, true);
		Assert.assertNotNull(entries);
		Assert.assertFalse(entries.isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#rawClasspath(org.eclipse.jdt.core.IJavaProject, boolean)}.
	 * @throws JavaModelException 
	 */
	@Test
	public void testRawClasspathIJavaProjectBoolean() throws JavaModelException {
		List<IClasspathEntry> entries = JDTUtil.rawClasspath(JavaCore.create(project), true);
		Assert.assertNotNull(entries);
		Assert.assertFalse(entries.isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#isClasspathContainer(org.eclipse.jdt.core.IClasspathEntry, java.lang.String)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#isJREClasspathContainer(org.eclipse.jdt.core.IClasspathEntry)}.
	 * @throws JavaModelException 
	 */
	@Test
	public void testIsClasspathContainer() throws JavaModelException {
		for (IClasspathEntry entry: JDTUtil.rawClasspath(project, true)) {
			switch(entry.getContentKind()) {
			case IClasspathEntry.CPE_CONTAINER:
				Assert.assertTrue(JDTUtil.isClasspathContainer(entry, entry.getPath().toString()));
				break;
			default:
				//Assert.assertFalse(JDTUtil.isClasspathContainer(entry, entry.getPath().toString()));
				break;
			}
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#getBundleInfo(org.osgi.framework.Bundle, boolean)}.
	 */
	@Test
	public void testGetBundleInfo() {
		String data = JDTUtil.getBundleInfo(Activator.getDefault().getBundle(), true);
		Assert.assertNotNull(data);
		Assert.assertTrue(data.length() > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#generateQualifiedClassNameUsingPathSeperator(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGenerateQualifiedClassNameUsingPathSeperator() {
		String serviceInterface = "org.ebayopensource.turmeric.service.Service";
		
		String name = JDTUtil.generateQualifiedClassNameUsingPathSeperator(serviceInterface, 
							"gen",
							"BaseServiceConsumer");
		Assert.assertTrue("name should not be empty", StringUtils.isNotBlank(name));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#getPluginProperties(org.osgi.framework.Bundle, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetPluginPropertiesBundleString() throws IOException {
		PropertyResourceBundle bundle = JDTUtil.getPluginProperties(Activator.getDefault().getBundle(), 
				"plugin.properties");
		Assert.assertNotNull(bundle);
		Assert.assertFalse(bundle.keySet().isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#getPluginProperties(org.osgi.framework.Bundle)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetPluginPropertiesBundle() throws IOException {
		PropertyResourceBundle bundle = JDTUtil.getPluginProperties(Activator.getDefault().getBundle());
		Assert.assertNotNull(bundle);
		Assert.assertFalse(bundle.keySet().isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#getSourceDirectories(org.eclipse.core.resources.IProject)}.
	 */
	@Test
	public void testGetSourceDirectories() {
		List<IPath> srcDirs = JDTUtil.getSourceDirectories(project);
		Assert.assertNotNull(srcDirs);
		Assert.assertFalse(srcDirs.isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil#convertClassNameToFilePath(java.lang.String)}.
	 */
	@Test
	public void testConvertClassNameToFilePath() {
		String javaName = "com.nikon.DSLR";
		Assert.assertEquals(new Path("com/nikon/DSLR.java"), JDTUtil.convertClassNameToFilePath(javaName));
	}

}
