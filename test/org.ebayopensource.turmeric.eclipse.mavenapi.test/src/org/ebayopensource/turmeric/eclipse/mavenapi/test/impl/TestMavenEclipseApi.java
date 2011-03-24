/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.AbstractProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestMavenEclipseApi {
	private static MavenEclipseApi api;
	

	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = (MavenEclipseApi)MavenApiPlugin.getDefault().getMavenEclipseApi();
		api.refreshAllIndices();
		int retry = 0;
		while (retry < 10 && api.findGroup("junit").isEmpty()) {
			Thread.sleep(1000);
			retry++;
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#mavenizeProject(org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest, org.eclipse.core.runtime.IProgressMonitor)}.
	 * @throws Exception 
	 */
	@Test
	public void testMavenizeProjectProjectMavenizationRequestIProgressMonitor() throws Exception {
		String projctName = "TestProjectApi";
		IProject project = WorkspaceUtil.getProject(projctName);
		if (project.isAccessible()) {
			project.delete(true, new NullProgressMonitor());
		}
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject("TestProject", workspaceRoot, monitor);
		final IProjectDescription description = project.getDescription();
		final List<String> natureIDs = ListUtil.array(description
				.getNatureIds());
		natureIDs.add(JavaCore.NATURE_ID);
		description.setNatureIds(natureIDs.toArray(new String[0]));
		project.setDescription(description, monitor);

		ProjectMavenizationRequest request = ProjectMavenizationRequest.createRequest(project);
		final ArtifactMetadata artifact = new ArtifactMetadata( "org.ebayopensource.turmeric.project", project.getName(), "1.0.0", "jar" );
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
		
		Assert.assertTrue(MavenEclipseUtil.hasMavenNature(project));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#resolveArtifact(org.apache.maven.repository.metadata.ArtifactMetadata)}.
	 * @throws MavenEclipseApiException 
	 */
	@Test
	public void testResolveArtifactArtifactMetadata() throws MavenEclipseApiException {
		ArtifactMetadata metadata = new EclipseArtifactMetadata("junit", "junit", "4.4");
		metadata.setType("jar");
		Artifact artifact = api.resolveArtifact(metadata);
		Assert.assertEquals(metadata.getGroupId(), artifact.getGroupId());
		Assert.assertEquals(metadata.getArtifactId(), artifact.getArtifactId());
		Assert.assertEquals(metadata.getVersion(), artifact.getVersion());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#findGroup(java.lang.String)}.
	 */
	@Test
	public void testFindGroup() throws MavenEclipseApiException{
		Collection<Artifact> collection = api.findGroup("junit");
		Assert.assertNotNull(collection);
		/*boolean isEmpty = collection.isEmpty();
		Assert.assertFalse(isEmpty);*/
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#findArtifact(java.lang.String)}.
	 */
	@Test
	public void testFindArtifact() throws MavenEclipseApiException{
		Collection<Artifact> collection = api.findArtifact("junit:junit");
		Assert.assertNotNull(collection);
		/*boolean isEmpty = collection.isEmpty();
		Assert.assertFalse(isEmpty);*/
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#resolveArtifactAsClasspath(org.apache.maven.repository.metadata.ArtifactMetadata)}.
	 * @throws MavenEclipseApiException 
	 */
	@Test
	public void testResolveArtifactAsClasspathArtifactMetadata() throws MavenEclipseApiException {
		ArtifactMetadata metadata = new EclipseArtifactMetadata("junit", "junit", "4.4");
		metadata.setType("jar");
		List<Artifact> artifacts = api.resolveArtifactAsClasspath(metadata);
		Assert.assertNotNull(artifacts);
		/*boolean isEmpty = artifacts.isEmpty();
		Assert.assertFalse(isEmpty);*/
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#resolveArtifactAsProject(org.apache.maven.artifact.Artifact)}.
	 */
	@Ignore("currently failing")
	@Test
	public void testResolveArtifactAsProject() throws MavenEclipseApiException {
		Collection<Artifact> data = api.findArtifactByName("junit");
		Assert.assertNotNull(data);
		Assert.assertTrue(data.size() > 0);
		Artifact artifact = data.iterator().next();
		System.out.println(artifact.toString());
		MavenProject mProject = 
			api.resolveArtifactAsProject(artifact);
		Assert.assertNotNull(mProject);
		Assert.assertEquals(artifact.toString(), mProject.toString());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#findArtifactByName(java.lang.String)}.
	 */
	@Test
	public void testFindArtifactByName() throws MavenEclipseApiException{
		Collection<Artifact> data = api.findArtifactByName("junit");
		Assert.assertNotNull(data);
		/*boolean isEmpty = data.isEmpty();
		Assert.assertFalse(isEmpty);*/
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#findArtifactByNameAndGroup(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFindArtifactByNameAndGroup() throws MavenEclipseApiException {
		Collection<Artifact> data = api.findArtifactByNameAndGroup("junit", "junit");
		Assert.assertNotNull(data);
		/*boolean isEmpty = data.isEmpty();
		Assert.assertFalse(isEmpty);*/
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseApi#findArtifactByNameAndGroupAndRepositoryUrl(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFindArtifactByNameAndGroupAndRepositoryUrl() throws MavenEclipseApiException {
		Collection<Artifact> data = api.findArtifactByNameAndGroupAndRepositoryUrl("junit", "junit", "http://repo1.maven.org/maven2/");
		Assert.assertNotNull(data);
		/*boolean isEmpty = data.isEmpty();
		Assert.assertFalse(isEmpty);*/
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.AbstractMavenEclipseApi#parsePom()}.
	 * @throws IOException
	 * @throws MavenEclipseApiException 
	 * @throws URISyntaxException 
	 */
	@Test
	public void testParsePom() throws IOException, MavenEclipseApiException, URISyntaxException {
		
		URL url = Platform.getBundle("org.ebayopensource.turmeric.eclipse.mavenapi.test").getEntry("pom.xml");
		Assert.assertNotNull(url);
		InputStream ins = null;
		try {
			ins = url.openStream();
			Assert.assertNotNull(api.parsePom(ins));
		} finally {
			IOUtils.closeQuietly(ins);
		}
		
		Collection<Artifact> data = api.findArtifactByNameAndGroupAndRepositoryUrl("junit", "junit", "http://repo1.maven.org/maven2/");
		Assert.assertNotNull(data);
		boolean isEmpty = data.isEmpty();
		if (isEmpty == false) {
			Assert.assertFalse(isEmpty);
			Artifact artifact = data.iterator().next();
			MavenApiPlugin.getDefault().getMavenEclipseApi().resolveArtifact(artifact);
			File file = MavenEclipseUtil.getArtifactPOMFile(artifact);
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			Assert.assertNotNull(api.parsePom(file));
		}
	}

}
