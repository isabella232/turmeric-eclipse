/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.impl;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.AbstractProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.maveneclipseapi.test.request.TestProjectMavenizationRequestRaw;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestMavenEclipseUtil {
	
	private static MavenEclipseApi api;
	private static final String MAVEN_GROUP_ID = "org.ebayopensource.turmeric.project";
	private static IProject mavenProject;

	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = (MavenEclipseApi)MavenApiPlugin.getDefault().getMavenEclipseApi();
		api.refreshAllIndices();
		int retry = 0;
		
		try {
			while (retry < 10 && api.findGroup("junit").isEmpty()) {
				Thread.sleep(1000);
				retry++;
			}
		} catch (Exception ex) {
			// Swallow the exception
		}
		
		String projctName = "TestProjectUtil";
		
		if (WorkspaceUtil.getProject(projctName).isAccessible()) {
			mavenProject = WorkspaceUtil.getProject(projctName);
		} else {
			IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
			IProgressMonitor monitor = new NullProgressMonitor();
			IProject project = WorkspaceUtil.createProject(projctName, workspaceRoot, monitor);
			final IProjectDescription description = project.getDescription();
			final List<String> natureIDs = ListUtil.array(description
					.getNatureIds());
			natureIDs.add(JavaCore.NATURE_ID);
			description.setNatureIds(natureIDs.toArray(new String[0]));
			project.setDescription(description, monitor);
			
			ProjectMavenizationRequest request = ProjectMavenizationRequest.createRequest(project);
			final ArtifactMetadata artifact = new ArtifactMetadata( MAVEN_GROUP_ID, project.getName(), "1.0.0", "jar" );
			request.setArtifact(artifact);
			final Collection<ArtifactMetadata> dependencies = new ArrayList<ArtifactMetadata>();
			ArtifactMetadata metadata = new EclipseArtifactMetadata("junit", "junit", "4.4");
			metadata.setType("jar");
			dependencies.add(metadata);
			request.setDependencies(dependencies);
			
			//setup SOA app dir structure
			request.setSourcePath("src");
			request.setTestSourcePath("test");
			/*request.setTestOutputPath("bin");
			request.setOutputPath("bin");*/
			api.mavenizeProject((AbstractProjectMavenizationRequest)request, new NullProgressMonitor());
			mavenProject = project;
		}
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#hasMavenNature(org.eclipse.core.resources.IProject)}.
	 * @throws CoreException 
	 */
	@Test
	public void testHasMavenNature() throws CoreException {
		Assert.assertTrue(MavenEclipseUtil.hasMavenNature(mavenProject));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#getAllMavenProjectsInWorkspace()}.
	 * @throws CoreException 
	 */
	@Test
	public void testGetAllMavenProjectsInWorkspace() throws CoreException {
		Assert.assertTrue("Can not find the TestProject", MavenEclipseUtil.getAllMavenProjectsInWorkspace().contains(mavenProject));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#readPOM(org.eclipse.core.resources.IProject)}.
	 * @throws Exception 
	 */
	@Test
	public void testReadPOM() throws Exception {
		Model model = MavenEclipseUtil.readPOM(mavenProject);
		Assert.assertNotNull(model);
		Assert.assertEquals(MAVEN_GROUP_ID, model.getGroupId());
		Assert.assertEquals(mavenProject.getName(), model.getArtifactId());
		
		Model pModel = api.parsePom(mavenProject);
		Assert.assertNotNull(pModel);
		Assert.assertEquals(MAVEN_GROUP_ID, pModel.getGroupId());
		Assert.assertEquals(mavenProject.getName(), pModel.getArtifactId());
		
		IFile file = mavenProject.getFile("pom.xml");
		pModel = api.parsePom(file);
		Assert.assertNotNull(pModel);
		Assert.assertEquals(MAVEN_GROUP_ID, pModel.getGroupId());
		Assert.assertEquals(mavenProject.getName(), pModel.getArtifactId());
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#writePom(org.eclipse.core.resources.IFile, org.apache.maven.model.Model)}.
	 * @throws Exception
	 */
	@Test
	public void testWritePom() throws Exception {
		URL url = TestProjectMavenizationRequestRaw.class.getResource("pom.xml");
		Assert.assertNotNull(url);
		InputStream ins = null;
		try {
			ins = url.openStream();
			Model model = api.parsePom(ins);
			Assert.assertNotNull(model);
			IFile file = mavenProject.getFile("new-pom.xml");
			api.writePom(file, model);
		} finally {
			IOUtils.closeQuietly(ins);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#getAllProjectArtifactsInWorkspace()}.
	 */
	@Test
	public void testGetAllProjectArtifactsInWorkspace() {
		boolean found = false;
		for(IProject project : MavenEclipseUtil.getAllProjectArtifactsInWorkspace().values()) {
			if (project.getName().equals(mavenProject.getName())) {
				found = true;
				break;
			}
		}
		Assert.assertTrue("Could not find TestProject", found);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#getAllProjectArtifactIdsInWorkspace()}.
	 */
	@Test
	public void testGetAllProjectArtifactIdsInWorkspace() {
		Map<String, IProject> data = MavenEclipseUtil.getAllProjectArtifactIdsInWorkspace();
		Assert.assertTrue(data.values().contains(mavenProject));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#getPomFile(org.eclipse.core.resources.IProject)}.
	 */
	@Test
	public void testGetPomFile() {
		Assert.assertNotNull(MavenEclipseUtil.getPomFile(mavenProject));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifact(org.apache.maven.repository.metadata.ArtifactMetadata)}.
	 * @throws MavenEclipseApiException 
	 */
	@Test
	public void testArtifact() throws MavenEclipseApiException {
		ArtifactMetadata metadata = new EclipseArtifactMetadata("android", "junit", "4.4");
		metadata.setType("jar");
		Artifact artifact = MavenEclipseUtil.artifact(metadata);
		Assert.assertNotNull(artifact);
		Assert.assertEquals(metadata.getGroupId(), artifact.getGroupId());
		Assert.assertEquals(metadata.getArtifactId(), artifact.getArtifactId());
		Assert.assertEquals(metadata.getVersion(), artifact.getVersion());
		Assert.assertEquals(metadata.getType(), artifact.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#convertToArtifactMetadata(org.apache.maven.model.Model)}.
	 * @throws CoreException 
	 */
	@Test
	public void testConvertToArtifactMetadata() throws CoreException {
		Model model = MavenEclipseUtil.readPOM(mavenProject);
		Assert.assertNotNull(model);
		ArtifactMetadata metadata = MavenEclipseUtil.convertToArtifactMetadata(model);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(model.getGroupId(), metadata.getGroupId());
		Assert.assertEquals(model.getArtifactId(), metadata.getArtifactId());
		Assert.assertEquals(model.getVersion(), metadata.getVersion());
		Assert.assertEquals(model.getPackaging(), metadata.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(java.lang.String)}.
	 */
	@Test
	public void testArtifactMetadataString() {
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata("android:rocks");
		Assert.assertNotNull(metadata);
		Assert.assertEquals("android", metadata.getGroupId());
		Assert.assertEquals("rocks", metadata.getArtifactId());
		Assert.assertNull(metadata.getVersion());
		
		metadata = MavenEclipseUtil.artifactMetadata("android:rocks:jar:4.4");
		Assert.assertNotNull(metadata);
		Assert.assertEquals("android", metadata.getGroupId());
		Assert.assertEquals("rocks", metadata.getArtifactId());
		Assert.assertEquals("4.4", metadata.getVersion());
		Assert.assertEquals("jar", metadata.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(org.apache.maven.model.Dependency)}.
	 */
	@Test
	public void testArtifactMetadataDependency() {
		Dependency dependency = new Dependency();
		dependency.setGroupId("android");
		dependency.setArtifactId("rocks");
		dependency.setVersion("1.0");
		dependency.setType("jar");
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata(dependency);
		
		Assert.assertNotNull(metadata);
		Assert.assertEquals(dependency.getGroupId(), metadata.getGroupId());
		Assert.assertEquals(dependency.getArtifactId(), metadata.getArtifactId());
		Assert.assertEquals(dependency.getVersion(), metadata.getVersion());
		Assert.assertEquals(dependency.getType(), metadata.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testArtifactMetadataStringStringStringStringStringString() {
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata("android", "rocks", "1.0", "jar", "compile", "clssifier");
		Assert.assertNotNull(metadata);
		Assert.assertEquals("android", metadata.getGroupId());
		Assert.assertEquals("rocks", metadata.getArtifactId());
		Assert.assertEquals("1.0", metadata.getVersion());
		Assert.assertEquals("jar", metadata.getType());
		Assert.assertEquals("compile", metadata.getScope());
		Assert.assertEquals("clssifier", metadata.getClassifier());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testArtifactMetadataStringStringStringString() {
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata("android", "rocks", "1.0", "jar");
		Assert.assertNotNull(metadata);
		Assert.assertEquals("android", metadata.getGroupId());
		Assert.assertEquals("rocks", metadata.getArtifactId());
		Assert.assertEquals("1.0", metadata.getVersion());
		Assert.assertEquals("jar", metadata.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(org.apache.maven.artifact.Artifact)}.
	 * @throws MavenEclipseApiException 
	 */
	@Test
	public void testArtifactMetadataArtifact() throws MavenEclipseApiException {
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata("android", "rocks", "1.0", "jar");
		Artifact artifact = MavenEclipseUtil.artifact(metadata);
		Assert.assertNotNull(artifact);
		Assert.assertEquals(metadata.getGroupId(), artifact.getGroupId());
		Assert.assertEquals(metadata.getArtifactId(), artifact.getArtifactId());
		Assert.assertEquals(metadata.getVersion(), artifact.getVersion());
		Assert.assertEquals(metadata.getType(), artifact.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(org.apache.maven.model.Model)}.
	 * @throws CoreException 
	 */
	@Test
	public void testArtifactMetadataModel() throws CoreException {
		Model model = MavenEclipseUtil.readPOM(mavenProject);
		Assert.assertNotNull(model);
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata(model);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(model.getGroupId(), metadata.getGroupId());
		Assert.assertEquals(model.getArtifactId(), metadata.getArtifactId());
		Assert.assertEquals(model.getVersion(), metadata.getVersion());
		Assert.assertEquals(model.getPackaging(), metadata.getType());
		
		Assert.assertNull(MavenEclipseUtil.artifactMetadata((Model)null));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#artifactMetadata(org.maven.ide.eclipse.index.IndexedArtifactFile, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testArtifactMetadataIndexedArtifactFileString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#dependency(org.apache.maven.repository.metadata.ArtifactMetadata)}.
	 */
	@Test
	public void testDependencyArtifactMetadata() {
		ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata("android", "rocks", "1.0", "jar");
		Dependency dependency = MavenEclipseUtil.dependency(metadata);
		Assert.assertNotNull(dependency);
		Assert.assertEquals(metadata.getGroupId(), dependency.getGroupId());
		Assert.assertEquals(metadata.getArtifactId(), dependency.getArtifactId());
		Assert.assertEquals(metadata.getVersion(), dependency.getVersion());
		Assert.assertEquals(metadata.getType(), dependency.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#dependency(org.apache.maven.model.Model)}.
	 */
	@Test
	public void testDependencyModel() {
		Assert.assertNull(MavenEclipseUtil.dependency((Model)null));
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil#getArtifactPOMFile(org.apache.maven.artifact.Artifact)}.
	 * @throws MavenEclipseApiException 
	 */
	@Test
	public void testGetArtifactPOMFile() throws MavenEclipseApiException {
		Collection<Artifact> data = MavenApiPlugin.getDefault().getMavenEclipseApi()
		.findArtifactByNameAndGroup("junit", "junit");
		Assert.assertNotNull(data);
		if (data.isEmpty() == false) {
			Assert.assertFalse("could not find the artifact", data.isEmpty());
			Artifact artifact = data.iterator().next();
			MavenApiPlugin.getDefault().getMavenEclipseApi().resolveArtifact(artifact);
			File file = MavenEclipseUtil.getArtifactPOMFile(artifact);
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			Assert.assertNull(MavenEclipseUtil.getArtifactPOMFile(null));
		}
	}

}
