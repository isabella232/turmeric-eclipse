/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.impl;

import org.apache.maven.artifact.ArtifactScopeEnum;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestEclipseArtifactMetadata {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(org.apache.maven.artifact.Artifact)}.
	 */
	@Test
	public void testEclipseArtifactMetadataArtifact() {
		//EclipseArtifactMetadata metadata = new EclipseArtifactMetadata();
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.maven.artifact.ArtifactScopeEnum, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringStringStringArtifactScopeEnumStringStringStringBooleanString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		String type = "type";
		ArtifactScopeEnum artifactScope = ArtifactScopeEnum.compile;
		String classifier = "classifier";
		String artifactUri = "artifactUri";
		String why = "why";
		boolean resolved = true;
		String error = "error";
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version, type, artifactScope, classifier, artifactUri, why,
				resolved, error);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
		Assert.assertEquals(type, metadata.getType());
		Assert.assertEquals(artifactScope, metadata.getArtifactScope());
		Assert.assertEquals(classifier, metadata.getClassifier());
		Assert.assertEquals(artifactUri, metadata.getArtifactUri());
		Assert.assertEquals(why, metadata.getWhy());
		Assert.assertEquals(resolved, metadata.isResolved());
		Assert.assertEquals(error, metadata.getError());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.maven.artifact.ArtifactScopeEnum, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringStringStringArtifactScopeEnumStringString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		String type = "type";
		ArtifactScopeEnum artifactScope = ArtifactScopeEnum.compile;
		String classifier = "classifier";
		String artifactUri = "artifactUri";
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version, type, artifactScope, classifier, artifactUri);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
		Assert.assertEquals(type, metadata.getType());
		Assert.assertEquals(artifactScope, metadata.getArtifactScope());
		Assert.assertEquals(classifier, metadata.getClassifier());
		Assert.assertEquals(artifactUri, metadata.getArtifactUri());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.maven.artifact.ArtifactScopeEnum, java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringStringStringArtifactScopeEnumString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		String type = "type";
		ArtifactScopeEnum artifactScope = ArtifactScopeEnum.compile;
		String classifier = "classifier";
		
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version, type, artifactScope, classifier);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
		Assert.assertEquals(type, metadata.getType());
		Assert.assertEquals(artifactScope, metadata.getArtifactScope());
		Assert.assertEquals(classifier, metadata.getClassifier());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.maven.artifact.ArtifactScopeEnum)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringStringStringArtifactScopeEnum() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		String type = "type";
		ArtifactScopeEnum artifactScope = ArtifactScopeEnum.compile;
		
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version, type, artifactScope);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
		Assert.assertEquals(type, metadata.getType());
		Assert.assertEquals(artifactScope, metadata.getArtifactScope());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringStringStringStringStringStringStringBooleanString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		String type = "type";
		String artifactScope = ArtifactScopeEnum.compile.name();
		String classifier = "classifier";
		String artifactUri = "artifactUri";
		String why = "why";
		boolean resolved = true;
		String error = "error";
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version, type, artifactScope, classifier, artifactUri, why,
				resolved, error);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
		Assert.assertEquals(type, metadata.getType());
		Assert.assertEquals(artifactScope, metadata.getArtifactScope().name());
		Assert.assertEquals(classifier, metadata.getClassifier());
		Assert.assertEquals(artifactUri, metadata.getArtifactUri());
		Assert.assertEquals(why, metadata.getWhy());
		Assert.assertEquals(resolved, metadata.isResolved());
		Assert.assertEquals(error, metadata.getError());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringStringString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		String type = "type";
		
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version, type);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
		Assert.assertEquals(type, metadata.getType());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataStringStringString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version);
		Assert.assertNotNull(metadata);
		Assert.assertEquals(groupId, metadata.getGroupId());
		Assert.assertEquals(name, metadata.getArtifactId());
		Assert.assertEquals(version, metadata.getVersion());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#EclipseArtifactMetadata(java.lang.String)}.
	 */
	@Test
	public void testEclipseArtifactMetadataString() {
		String name = "name";
		
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(name);
		Assert.assertNotNull(metadata);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata#toString()}.
	 */
	@Test
	public void testToString() {
		String groupId = "groupId"; 
		String name = "name";
		String version = "version";
		
		EclipseArtifactMetadata metadata = new EclipseArtifactMetadata(groupId, name,
				version);
		metadata.setType("jar");
		Assert.assertNotNull(metadata);
		Assert.assertNotNull(metadata.toString());
	}

}
