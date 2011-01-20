/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.exception.core.SOANullParameterException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceNotAccessibleException;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestWorkspaceUtil {
	private static IProject project;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject(TestWorkspaceUtil.class.getSimpleName() + "Project", 
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
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getWorkspace()}.
	 */
	@Test
	public void testGetWorkspace() {
		Assert.assertSame(ResourcesPlugin.getWorkspace(), WorkspaceUtil.getWorkspace());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getWorkspaceRoot()}.
	 */
	@Test
	public void testGetWorkspaceRoot() {
		Assert.assertSame(ResourcesPlugin.getWorkspace().getRoot(), WorkspaceUtil.getWorkspaceRoot());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#projectExistsInWorkSpace(java.lang.String)}.
	 */
	@Test
	public void testProjectExistsInWorkSpace() {
		Assert.assertFalse(WorkspaceUtil.projectExistsInWorkSpace("DummpProject"));
		Assert.assertTrue(WorkspaceUtil.projectExistsInWorkSpace(project.getName()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#directoryExistsInFileSystem(java.lang.String)}.
	 */
	@Test
	public void testDirectoryExistsInFileSystem() {
		String filePath = System.getProperty("user.home");
		Assert.assertTrue(WorkspaceUtil.directoryExistsInFileSystem(filePath));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getProjectDirPath(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetProjectDirPath() {
		String filePath = System.getProperty("user.dir");
		Assert.assertEquals(filePath + File.separator + "Hello", 
				WorkspaceUtil.getProjectDirPath(filePath, "Hello"));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getAllProjectsInWorkSpace()}.
	 */
	@Test
	public void testGetAllProjectsInWorkSpace() {
		IProject[] projects = WorkspaceUtil.getAllProjectsInWorkSpace();
		Assert.assertNotNull(projects);
		Assert.assertTrue(projects.length > 0);
		Assert.assertTrue(Arrays.asList(projects).contains(project));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#resolveProjectPath(java.lang.String, org.eclipse.core.runtime.IPath)}.
	 */
	@Test
	public void testResolveProjectPath() {
		String projectName = project.getName();
		IPath path = new Path("bin");
		Assert.assertNull(WorkspaceUtil.resolveProjectPath(projectName, null));
		Assert.assertEquals(path, WorkspaceUtil.resolveProjectPath(null, path));
		Assert.assertEquals(project.getLocation(), 
				WorkspaceUtil.resolveProjectPath(projectName, project.getLocation()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#resolveProjectRoot(org.eclipse.core.runtime.IPath, java.lang.String[])}.
	 */
	@Test
	public void testResolveProjectRoot() {
		String projectName = project.getName();
		Assert.assertEquals(WorkspaceUtil.getWorkspaceRoot().getLocation(), 
				WorkspaceUtil.resolveProjectRoot(project.getLocation(), projectName));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#createProject(java.lang.String, org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IProgressMonitor)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#deleteProject(java.lang.String)}.
	 * @throws CoreException 
	 */
	@Test
	public void testCreateProject() throws CoreException {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		String projectName = "TestCreateProject";
		IProject testProject = WorkspaceUtil.createProject(projectName, 
				workspaceRoot, monitor);
		Assert.assertTrue(testProject.isAccessible());
		
		WorkspaceUtil.deleteProject(projectName);
		Assert.assertFalse(WorkspaceUtil.projectExistsInWorkSpace(projectName));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getProject(java.lang.String)}.
	 */
	@Test
	public void testGetProjectString() {
		Assert.assertSame(project, WorkspaceUtil.getProject(project.getName()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getProject(org.eclipse.core.runtime.IPath)}.
	 */
	@Test
	public void testGetProjectIPath() {
		Assert.assertSame(project, WorkspaceUtil.getProject(project.getFullPath()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#openProject(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 */
	@Test
	public void testOpenProject() throws CoreException {
		Assert.assertSame(project, WorkspaceUtil.openProject(project, 
				ProgressUtil.getDefaultMonitor(null)));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#createFolders(org.eclipse.core.resources.IProject, java.util.List, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 */
	@Test
	public void testCreateFolders() throws CoreException {
		List<String> dirs = new ArrayList<String>();
		dirs.add("nikon/d80");
		dirs.add("panasonic/gf1");
		WorkspaceUtil.createFolders(project, dirs, new NullProgressMonitor());
		for (String dir : dirs) {
			Assert.assertTrue(project.getFolder(dir).exists());
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#refresh(org.eclipse.core.resources.IResource[])}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#refresh(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.resources.IResource[])}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#refresh(org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 */
	@Test
	public void testRefreshIResourceArray() throws CoreException {
		if (project.getFolder("nikon").exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList("nikon"), new NullProgressMonitor());
		}
		IResource res = project.getFolder("nikon");
		WorkspaceUtil.refresh(res);
		WorkspaceUtil.refresh(ProgressUtil.getDefaultMonitor(null), res);
		WorkspaceUtil.refresh(res, ProgressUtil.getDefaultMonitor(null));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getLocation(org.eclipse.core.runtime.IPath)}.
	 */
	@Test
	public void testGetLocation() {
		Assert.assertNull(WorkspaceUtil.getLocation(null));
		Assert.assertEquals(project.getLocation(), WorkspaceUtil.getLocation(project.getFullPath()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#writeToFile(java.lang.String, org.eclipse.core.resources.IFile, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test
	public void testWriteToFileStringIFileIProgressMonitor() throws CoreException, IOException {
		IFile file = project.getFile("d700.properties");
		WorkspaceUtil.writeToFile("nikon", file, 
				ProgressUtil.getDefaultMonitor(null));
		InputStream input = null;
		try {
			input = file.getContents();
			String data = IOUtils.toString(input);
			Assert.assertNotNull(data);
			Assert.assertEquals("nikon", data);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#writeToFile(java.io.InputStream, org.eclipse.core.resources.IFile, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test
	public void testWriteToFileInputStreamIFileIProgressMonitor() throws CoreException, IOException {
		IFile file = project.getFile("d70.properties");
		WorkspaceUtil.writeToFile(IOUtils.toInputStream("nikon"), file, 
				ProgressUtil.getDefaultMonitor(null));
		InputStream input = null;
		try {
			input = file.getContents();
			String data = IOUtils.toString(input);
			Assert.assertNotNull(data);
			Assert.assertEquals("nikon", data);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#loadProperties(org.eclipse.core.resources.IFile)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test
	public void testLoadProperties() throws CoreException, IOException {
		IFile file = project.getFile("d7000.properties");
		InputStream input = null;
		try {
			input = IOUtils.toInputStream("nikon=d3x");
			file.create(input, true, 
					ProgressUtil.getDefaultMonitor(null));
		} finally {
			IOUtils.closeQuietly(input);
		}
		
		Properties props = WorkspaceUtil.loadProperties(file);
		Assert.assertNotNull(props);
		Assert.assertTrue(props.containsKey("nikon"));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#addPathSeperators(java.lang.String[])}.
	 */
	@Test
	public void testAddPathSeperators() {
		String[] data = {"nikon", "d80"};
		Assert.assertEquals("nikon" + WorkspaceUtil.PATH_SEPERATOR + "d80" + WorkspaceUtil.PATH_SEPERATOR, 
				WorkspaceUtil.addPathSeperators(data));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#delete(org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 */
	@Test
	public void testDelete() throws CoreException {
		String folderName = "canon";
		if (project.getFolder(folderName).exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList(folderName), new NullProgressMonitor());
		}
		IResource res = project.getFolder(folderName);
		WorkspaceUtil.delete(res, new NullProgressMonitor());
		Assert.assertFalse(res.exists());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#setBuildAutomatically(boolean)}.
	 * @throws CoreException 
	 */
	@Test
	public void testSetBuildAutomatically() throws CoreException {
		WorkspaceUtil.setBuildAutomatically(false);
		Assert.assertFalse(WorkspaceUtil.getWorkspace().isAutoBuilding());
		WorkspaceUtil.setBuildAutomatically(true);
		Assert.assertTrue(WorkspaceUtil.getWorkspace().isAutoBuilding());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#isResourceWritable(org.eclipse.core.resources.IResource)}.
	 * @throws CoreException 
	 */
	@Test
	public void testIsResourceWritable() throws CoreException {
		String folderName = "nikon/d7000";
		if (project.getFolder(folderName).exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList(folderName), new NullProgressMonitor());
		}
		IResource res = project.getFolder(folderName);
		Assert.assertTrue(WorkspaceUtil.isResourceWritable(res));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#isResourceModifiable(org.eclipse.core.resources.IResource)}.
	 * @throws CoreException 
	 */
	@Test
	public void testIsResourceModifiable() throws CoreException {
		String folderName = "nikon/d7000";
		if (project.getFolder(folderName).exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList(folderName), new NullProgressMonitor());
		}
		IResource res = project.getFolder(folderName);
		Assert.assertTrue(WorkspaceUtil.isResourceModifiable(res));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#isResourceReadable(org.eclipse.core.resources.IResource)}.
	 * @throws CoreException 
	 */
	@Test
	public void testIsResourceReadable() throws CoreException {
		String folderName = "nikon/d7000";
		if (project.getFolder(folderName).exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList(folderName), new NullProgressMonitor());
		}
		IResource res = project.getFolder(folderName);
		Assert.assertTrue(WorkspaceUtil.isResourceReadable(res));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getOpenProjectsInWorkSpace()}.
	 */
	@Test
	public void testGetOpenProjectsInWorkSpace() {
		IProject[] projects = WorkspaceUtil.getOpenProjectsInWorkSpace();
		Assert.assertNotNull(projects);
		Assert.assertTrue(projects.length > 0);
		Assert.assertTrue(Arrays.asList(projects).contains(project));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getProjectsByNature(java.lang.String[])}.
	 * @throws CoreException 
	 */
	@Test
	public void testGetProjectsByNature() throws CoreException {
		Assert.assertTrue(WorkspaceUtil.getProjectsByNature(JavaCore.NATURE_ID).contains(project));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#createEmptyFile(org.eclipse.core.resources.IProject, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws CoreException 
	 */
	@Test
	public void testCreateEmptyFile() throws CoreException {
		String fileName = "d700.nikon";
		IFile file = 
			WorkspaceUtil.createEmptyFile(project, fileName, ProgressUtil.getDefaultMonitor(null));
		Assert.assertTrue("file not accessible", file.isAccessible());
		//file.delete(true, ProgressUtil.getDefaultMonitor(null));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#getFilesWithExtensions(org.eclipse.core.resources.IFolder, boolean, java.lang.String)}.
	 * @throws CoreException 
	 */
	@Test
	public void testGetFilesWithExtensions() throws CoreException {
		String folderName = "nikon/d7000";
		if (project.getFolder(folderName).exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList(folderName), 
					ProgressUtil.getDefaultMonitor(null));
		}
		IFolder folder = project.getFolder(folderName);
		IFile file = folder.getFile("d7000.nikon");
		file.create(IOUtils.toInputStream(" "), true, 
				ProgressUtil.getDefaultMonitor(null));
		List<IFile> files = WorkspaceUtil.getFilesWithExtensions(folder, false, "nikon");
		Assert.assertFalse("file not accessible", files.isEmpty());
		Assert.assertTrue(files.contains(file));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#deleteContents(org.eclipse.core.resources.IFolder, boolean)}.
	 * @throws CoreException 
	 * @throws SOAResourceNotAccessibleException 
	 * @throws SOANullParameterException 
	 */
	@Test
	public void testDeleteContents() throws CoreException, SOANullParameterException, SOAResourceNotAccessibleException {
		String folderName = "pentax";
		if (project.getFolder(folderName).exists() == false) {
			WorkspaceUtil.createFolders(project, ListUtil.arrayList(folderName), 
					ProgressUtil.getDefaultMonitor(null));
		}
		IFolder folder = project.getFolder(folderName);
		WorkspaceUtil.deleteContents(folder, true);
		Assert.assertTrue(folder.members().length == 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil#isDotDirectory(java.lang.String)}.
	 */
	@Test
	public void testIsDotDirectory() {
		Assert.assertTrue(WorkspaceUtil.isDotDirectory("."));
	}

}
