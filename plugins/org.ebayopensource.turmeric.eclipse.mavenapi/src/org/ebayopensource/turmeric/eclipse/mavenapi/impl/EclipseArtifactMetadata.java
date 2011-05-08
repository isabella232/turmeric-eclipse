/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactScopeEnum;
import org.apache.maven.repository.metadata.ArtifactMetadata;

/**
 * The Class EclipseArtifactMetadata.
 */
public class EclipseArtifactMetadata extends ArtifactMetadata {

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param af the Artifact
	 */
	public EclipseArtifactMetadata(Artifact af) {
		super(af);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId groupid
	 * @param name name
	 * @param version version
	 * @param type type
	 * @param artifactScope scope
	 * @param classifier classifier
	 * @param artifactUri location of the artifact
	 * @param why the why
	 * @param resolved resolved or not
	 * @param error error state
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope, String classifier,
			String artifactUri, String why, boolean resolved, String error) {
		super(groupId, name, version, type, artifactScope, classifier,
				artifactUri, why, resolved, error);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId gropid
	 * @param name name
	 * @param version version
	 * @param type type
	 * @param artifactScope artifact scope
	 * @param classifier maven classifier
	 * @param artifactUri artifact location
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope, String classifier,
			String artifactUri) {
		super(groupId, name, version, type, artifactScope, classifier,
				artifactUri);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId groupid
	 * @param name name
	 * @param version version
	 * @param type type
	 * @param artifactScope artifact scope
	 * @param classifier the maven classifer
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope, String classifier) {
		super(groupId, name, version, type, artifactScope, classifier);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId groupid
	 * @param name name
	 * @param version version
	 * @param type type
	 * @param artifactScope artifact scope
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope) {
		super(groupId, name, version, type, artifactScope);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId groupid
	 * @param name name
	 * @param version version
	 * @param type type
	 * @param scopeString artifact scope
	 * @param classifier the maven classifer
	 * @param artifactUri the artificat location
	 * @param why the why
	 * @param resolved whether the items is to be resolved or not
	 * @param error error status
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, String scopeString, String classifier,
			String artifactUri, String why, boolean resolved, String error) {
		super(groupId, name, version, type, scopeString, classifier,
				artifactUri, why, resolved, error);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId group id
	 * @param name name
	 * @param version version
	 * @param type type
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type) {
		super(groupId, name, version, type);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param groupId group id
	 * @param name name
	 * @param version version
	 */
	public EclipseArtifactMetadata(String groupId, String name, String version) {
		super(groupId, name, version);
	}

	/**
	 * Instantiates a new eclipse artifact metadata.
	 *
	 * @param name the artifact name
	 */
	public EclipseArtifactMetadata(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.apache.maven.repository.metadata.ArtifactMetadata#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(toDomainString());
		buf.append(MavenEclipseUtil.ARTIFACT_METADATA_SEPARATOR);
		if (getType() != null) {
			buf.append(getType());
			buf.append(MavenEclipseUtil.ARTIFACT_METADATA_SEPARATOR);
		}
		buf.append(getVersion());
		buf.append(MavenEclipseUtil.ARTIFACT_METADATA_SEPARATOR);
		buf.append(getScope());
		return buf.toString();
	}

}