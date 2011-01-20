/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper.getMavenModelManager;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactScopeEnum;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.ThrowableUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.maven.ide.eclipse.core.IMavenConstants;
import org.maven.ide.eclipse.index.IndexedArtifactFile;

/**
 * 
 * @author James Ervin</a>
 * 
 */
public final class MavenEclipseUtil {
	static final String ARTIFACT_METADATA_SEPARATOR = ":";

	public static boolean hasMavenNature(final IProject project)
			throws CoreException {
		if (project == null || !project.isAccessible())
			return false;
		return project.hasNature(IMavenConstants.NATURE_ID);
	}

	public static List<IProject> getAllMavenProjectsInWorkspace()
			throws CoreException {
		final List<IProject> projects = ListUtil.list();
		for (final IProject project : EclipseUtil.projects()) {
			if (project == null || !project.isAccessible()
					|| !project.hasNature(IMavenConstants.NATURE_ID))
				continue;
			projects.add(project);
		}
		return projects;
	}

	public static Model readPOM(final IProject project) throws CoreException {
		return getMavenModelManager().readMavenModel(getPomFile(project));
	}

	public static Map<ArtifactMetadata, IProject> getAllProjectArtifactsInWorkspace() {
		try {
			final Map<ArtifactMetadata, IProject> projects = new ConcurrentHashMap<ArtifactMetadata, IProject>();
			for (final IProject project : getAllMavenProjectsInWorkspace()) {
				if (project == null || !project.isAccessible())
					continue;
				final Model pom = readPOM(project);
				projects.put(MavenEclipseUtil.convertToArtifactMetadata(pom),
						project);
			}
			return projects;
		} catch (final CoreException e) {
			throw ThrowableUtil.adaptToRuntimeException(e);
		}
	}

	public static Map<String, IProject> getAllProjectArtifactIdsInWorkspace() {
		final Map<String, IProject> projects = new ConcurrentHashMap<String, IProject>();
		final Map<ArtifactMetadata, IProject> map = getAllProjectArtifactsInWorkspace();
		for (final ArtifactMetadata metadata : map.keySet()) {
			if (metadata == null)
				continue;
			projects.put(metadata.toString(), map.get(metadata));
		}
		return projects;
	}

	// -------------------------------------------------------------------------
	public static IFile getPomFile(final IProject project) {
		return project.getProject().getFile(IMavenConstants.POM_FILE_NAME);
	}

	// -------------------------------------------------------------------------
	public static Artifact artifact(final ArtifactMetadata metadata)
			throws MavenEclipseApiException {
		if (metadata == null)
			return null;
		if (isNotBlank(metadata.getScope()))
			return MavenApiHelper.getRepositorySystem().createArtifact(
					metadata.getGroupId(), metadata.getArtifactId(),
					metadata.getVersion(), metadata.getScope(),
					metadata.getType());
		return MavenApiHelper.getRepositorySystem()
				.createArtifactWithClassifier(metadata.getGroupId(),
						metadata.getArtifactId(), metadata.getVersion(),
						metadata.getType(), metadata.getClassifier());
	}

	public static ArtifactMetadata convertToArtifactMetadata(final Model pom) {
		return new EclipseArtifactMetadata(pom.getGroupId(),
				pom.getArtifactId(), pom.getVersion(), pom.getPackaging(),
				ArtifactScopeEnum.DEFAULT_SCOPE);
	}

	public static ArtifactMetadata artifactMetadata(final String library) {
		final String[] coordinates = StringUtils.split(library,
				ARTIFACT_METADATA_SEPARATOR);
		final ArtifactMetadata metadata;
		if (coordinates.length == 1 || coordinates.length == 0)
			metadata = new ArtifactMetadata(library);
		else if (coordinates.length == 2)
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], null, null, ArtifactScopeEnum.DEFAULT_SCOPE);
		else if (coordinates.length == 3)
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], null, coordinates[2],
					ArtifactScopeEnum.DEFAULT_SCOPE);
		else if (coordinates.length == 4)
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], coordinates[3], coordinates[2],
					ArtifactScopeEnum.DEFAULT_SCOPE);
		else
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], coordinates[3], coordinates[2],
					ArtifactScopeEnum.valueOf(coordinates[4]));
		return metadata;
	}

	public static ArtifactMetadata artifactMetadata(final Dependency dependency) {
		return new EclipseArtifactMetadata(dependency.getGroupId(),
				dependency.getArtifactId(), dependency.getVersion(),
				dependency.getType(), dependency.getScope(),
				dependency.getClassifier(), null, null, true, null);
	}

	public static ArtifactMetadata artifactMetadata(final String groupID,
			final String artifactID, final String version, final String type,
			final String scope, final String classifiler) {
		return new EclipseArtifactMetadata(groupID, artifactID, version, type,
				scope, classifiler, null, null, true, null);
	}

	public static ArtifactMetadata artifactMetadata(final String groupID,
			final String artifactID, final String version,
			final String packaging) {
		return new EclipseArtifactMetadata(groupID, artifactID, version,
				packaging);
	}

	public static ArtifactMetadata artifactMetadata(final Artifact artifact) {
		return new EclipseArtifactMetadata(artifact.getGroupId(),
				artifact.getArtifactId(), artifact.getVersion(),
				artifact.getType(), artifact.getScope(),
				artifact.getClassifier(), null, null, true, null);
	}

	public static ArtifactMetadata artifactMetadata(final Model model) {
		if (model == null)
			return null;
		return artifactMetadata(model.getId());
	}

	public static ArtifactMetadata artifactMetadata(
			final IndexedArtifactFile file, final String packaging) {
		return new EclipseArtifactMetadata(file.group, file.artifact,
				file.version, packaging, ArtifactScopeEnum.DEFAULT_SCOPE);
	}

	public static Dependency dependency(final ArtifactMetadata metadata) {
		final Dependency dependency = new Dependency();
		dependency.setGroupId(metadata.getGroupId());
		dependency.setArtifactId(metadata.getArtifactId());
		dependency.setVersion(metadata.getVersion());
		dependency.setClassifier(metadata.getClassifier());
		dependency.setType(metadata.getType());
		if (metadata.getScope() != null
				&& ArtifactScopeEnum.DEFAULT_SCOPE.getScope().equals(
						metadata.getScope()) == false)
			dependency.setScope(metadata.getScope());
		return dependency;
	}

	public static Dependency dependency(final Model model) {
		if (model == null)
			return null;
		return dependency(artifactMetadata(model));
	}

	public static File getArtifactPOMFile(final Artifact artifact) {
		if (artifact == null)
			return null;
		final StringBuffer loc = new StringBuffer();
		loc.append(artifact.getArtifactId());
		loc.append("-");
		loc.append(artifact.getVersion());
		loc.append(".pom");
		return new File(artifact.getFile().getParentFile(), loc.toString());
	}
}
