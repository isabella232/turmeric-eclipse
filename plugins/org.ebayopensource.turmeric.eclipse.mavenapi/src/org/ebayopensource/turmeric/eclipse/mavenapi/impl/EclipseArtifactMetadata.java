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

public class EclipseArtifactMetadata extends ArtifactMetadata {

	public EclipseArtifactMetadata(Artifact af) {
		super(af);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope, String classifier,
			String artifactUri, String why, boolean resolved, String error) {
		super(groupId, name, version, type, artifactScope, classifier,
				artifactUri, why, resolved, error);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope, String classifier,
			String artifactUri) {
		super(groupId, name, version, type, artifactScope, classifier,
				artifactUri);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope, String classifier) {
		super(groupId, name, version, type, artifactScope, classifier);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, ArtifactScopeEnum artifactScope) {
		super(groupId, name, version, type, artifactScope);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type, String scopeString, String classifier,
			String artifactUri, String why, boolean resolved, String error) {
		super(groupId, name, version, type, scopeString, classifier,
				artifactUri, why, resolved, error);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version,
			String type) {
		super(groupId, name, version, type);
	}

	public EclipseArtifactMetadata(String groupId, String name, String version) {
		super(groupId, name, version);
	}

	public EclipseArtifactMetadata(String name) {
		super(name);
	}

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