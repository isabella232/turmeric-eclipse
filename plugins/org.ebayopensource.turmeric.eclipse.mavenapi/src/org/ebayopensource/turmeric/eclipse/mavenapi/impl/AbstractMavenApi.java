/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactScopeEnum;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.apache.maven.repository.metadata.MetadataResolutionRequest;
import org.apache.maven.repository.metadata.MetadataResolutionResult;
import org.apache.maven.repository.metadata.MetadataTreeNode;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.SettingsUtils;
import org.codehaus.plexus.PlexusContainer;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.Messages;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.MavenApiUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.intf.IMavenEclipseApi;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.internal.embedder.MavenImpl;

/**
 * The Class AbstractMavenApi.
 *
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 */
public abstract class AbstractMavenApi implements IMavenEclipseApi {
	
	/** The Constant PACKAGING_TYPE_MAVENPLUGIN. */
	public static final String PACKAGING_TYPE_MAVENPLUGIN = "maven-plugin";
	
	/** The Constant PACKAGING_TYPE_POM. */
	public static final String PACKAGING_TYPE_POM = "pom";

	/**
	 * _get known repositories.
	 *
	 * @param embedder the maven implementation to use
	 * @param packagingType the packaging type to filter by
	 * @return A list of known repositories filtered by packaging type
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	protected List<ArtifactRepository> _getKnownRepositories(
			MavenImpl embedder, String packagingType)
			throws MavenEclipseApiException {
		if (embedder == null)
			throw new MavenEclipseApiException();
		
		try {
			Settings settings = embedder.getSettings();
			if (settings == null) {
				throw new MavenEclipseApiException(Messages.ERROR_NULL_SETTINGS);
			}
			return _getKnownRepositories(embedder.getPlexusContainer(),
					settings, packagingType);
		} catch (Exception anyEx) {
			anyEx.printStackTrace();
			throw new MavenEclipseApiException(anyEx);
		}
	}

	/**
	 * Return the Maven remote repositories defined in the Maven settings.xml
	 * file. The returned list of repositories depend on the pacakgeType.
	 * 
	 * For <code>maven-plugin</code> package type, it will return
	 * pluginRepositories, and all other types will return Mirrors and
	 * Repositories.
	 *
	 * @param plexus the plexus
	 * @param settings the settings.xml file to use
	 * @param packagingType the pom packaging type to return.
	 * @return the remote repositories defined in the Maven settings.xml file.
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	protected List<ArtifactRepository> _getKnownRepositories(
			PlexusContainer plexus, Settings settings, String packagingType)
			throws MavenEclipseApiException {
		final boolean needPluginRepo = PACKAGING_TYPE_MAVENPLUGIN
				.equalsIgnoreCase(packagingType);
		if (settings == null) {
			throw new MavenEclipseApiException(Messages.ERROR_NULL_SETTINGS);
		}

		List<String> activeProfiles = settings
				.getActiveProfiles();
		if (activeProfiles == null || activeProfiles.size() < 1) {
			throw new MavenEclipseApiException(
					Messages.ERROR_NO_ACTIVE_PROFILES);
		}

		List<ArtifactRepository> repositories = new ArrayList<ArtifactRepository>(
				8);
		try {
			// MavenTools mtools = (MavenTools) plexus
			// .lookup(org.apache.maven.MavenTools.class);
			RepositorySystem rs = plexus.lookup(RepositorySystem.class);
			if (needPluginRepo == false) {
				for (Mirror mirror : settings.getMirrors()) {
					final org.apache.maven.model.Repository repo = new org.apache.maven.model.Repository();
					repo.setId(mirror.getId());
					// repo.setModelEncoding(mirror.getModelEncoding());
					repo.setUrl(mirror.getUrl());
					final RepositoryPolicy snapshotPolicty = new RepositoryPolicy();
					snapshotPolicty.setEnabled(false);
					// snapshotPolicty.setModelEncoding(mirror.getModelEncoding());
					repo.setSnapshots(snapshotPolicty);
					final RepositoryPolicy releasedPolicty = new RepositoryPolicy();
					releasedPolicty.setEnabled(true);
					// releasedPolicty.setModelEncoding(mirror.getModelEncoding());
					repo.setReleases(releasedPolicty);
					// repositories.add(mtools.buildArtifactRepository(repo));
					repositories.add(rs.buildArtifactRepository(repo));
				}
			}

			List<Profile> profiles = settings.getProfiles();
			for (Profile p : profiles) {
				if (activeProfiles.contains(p.getId())) {
					org.apache.maven.model.Profile mp = SettingsUtils
							.convertFromSettingsProfile(p);
					if (needPluginRepo == true) {
						// requires plugin repositories
						final List<org.apache.maven.model.Repository> pluginRepos = mp
								.getPluginRepositories();
						// repositories
						// .addAll((List<ArtifactRepository>) mtools
						// .buildArtifactRepositories(pluginRepos));
						for (org.apache.maven.model.Repository repo : pluginRepos) {
							repositories.add(rs.buildArtifactRepository(repo));
						}
					} else {
						final List<org.apache.maven.model.Repository> repos = mp
								.getRepositories();
						// repositories
						// .addAll((List<ArtifactRepository>) mtools
						// .buildArtifactRepositories(repos));
						for (org.apache.maven.model.Repository repo : repos) {
							repositories.add(rs.buildArtifactRepository(repo));
						}
					}
				}
			}
			return repositories;

		} catch (Exception anyEx) {
			anyEx.printStackTrace();
			throw new MavenEclipseApiException(anyEx);
		}
	}


	/**
	 * Resolve an artifact using a particular maven implementation and repository.
	 *
	 * @param embedder the embedded version of maven to use
	 * @param repoSystem the repository to resolve against
	 * @param md the artifact metadata to search
	 * @return the resolved artifact
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Artifact resolveArtifact(MavenImpl embedder, RepositorySystem repoSystem, ArtifactMetadata md)
			throws MavenEclipseApiException {
		if (embedder == null)
			throw new MavenEclipseApiException();

		try {
			List<ArtifactRepository> repos = _getKnownRepositories(embedder,
					md.getType());
			if (repos == null || repos.size() < 1) {
				throw new MavenEclipseApiException(
						Messages.ERROR_NO_REPOSITORIES);
			}

			Artifact artifact = null;
			
			if (md.getClassifier() == null)
				artifact = repoSystem.createArtifact(
						md.getGroupId(), md.getArtifactId(), md.getVersion(),
						md.getScope(), md.getType());
			else
				artifact = repoSystem
						.createArtifactWithClassifier(md.getGroupId(),
								md.getArtifactId(), md.getVersion(),
								md.getType(), md.getClassifier());

			if (artifact == null) {
				throw new MavenEclipseApiException(Messages.ERROR_NULL_ARTIFACT);
			}

			// embedder.resolve(artifact, repos, embedder.getLocalRepository());
			MavenApiUtil.resolveArtifact(repoSystem, artifact,
					embedder.getLocalRepository(), repos);

			return artifact;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MavenEclipseApiException(e);
		}
	}

	private static void resolveArtifactAsClasspath(
			final MetadataTreeNode treeNode, final ArtifactScopeEnum scope,
			final Map<String, ArtifactMetadata> metadatas) {
		final ArtifactMetadata md = treeNode.getMd();
		if (metadatas.containsKey(md.toString()) == false
				&& scope.equals(md.getScopeAsEnum()) && md.getGroupId() != null) {
			metadatas.put(md.toString(), md);
			if (treeNode.getChildren() != null) {
				for (final MetadataTreeNode child : treeNode.getChildren()) {
					resolveArtifactAsClasspath(child, scope, metadatas);
				}
			}
		}
	}
	
	private void resolveArtifactDependencies(MavenImpl embedder, RepositorySystem repoSystem, 
			Artifact artifact, Set<Artifact> result) throws MavenEclipseApiException {
		try {
			MavenProject mProject = resolveArtifactAsProject(artifact);
			for (Dependency dep : mProject.getDependencies()) {
				ArtifactScopeEnum scope = ArtifactScopeEnum.valueOf(dep.getScope());
				if (ArtifactScopeEnum.test.equals(scope) == false) {
					Artifact art = resolveArtifact(embedder, repoSystem, new EclipseArtifactMetadata(
							dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType(), 
							scope, dep.getClassifier()));
					if (result.contains(art) == false) {
						result.add(art);
						resolveArtifactDependencies(embedder, repoSystem, art, result);
					}
				}
			}
		} catch (Exception e) {
			//ignore this one
		}
	}

	/**
	 * Returns a list of Artifacts.
	 *
	 * @param embedder the maven implementation to use.
	 * @param md the artifact meta data.
	 * @return A list of Artifacts that were resolved.
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public List<Artifact> resolveArtifactAsClasspath(MavenImpl embedder,
			ArtifactMetadata md) throws MavenEclipseApiException {
		List<Artifact> result = new ArrayList<Artifact>();
		try {
			if (embedder == null)
				throw new MavenEclipseApiException("null embedder supplied");

			if (md == null)
				throw new MavenEclipseApiException(
						"null metadata object supplied");
			RepositorySystem repoSystem = MavenApiHelper.getRepositorySystem();
			Set<Artifact> data = new HashSet<Artifact>();
			Artifact artifact = resolveArtifact(embedder, repoSystem, md);
			data.add(artifact);
			resolveArtifactDependencies(embedder, repoSystem, artifact, data);
			result.addAll(ListUtil.array(data));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MavenEclipseApiException(e);
		} 
		return result;

	}

	/**
	 * Resolves the artifact Metadata from the repository.
	 *
	 * @param embedder the maven implementation to use
	 * @param md the artifact metadata
	 * @return the MetadataResolutionResult
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	protected MetadataResolutionResult resolveArtifactMetadata(
			MavenImpl embedder, ArtifactMetadata md)
			throws MavenEclipseApiException {
		if (embedder == null)
			throw new MavenEclipseApiException("null embedder supplied");

		if (md == null)
			throw new MavenEclipseApiException("null metadata object supplied");

		try {
			List<ArtifactRepository> repos = _getKnownRepositories(embedder,
					md.getType());
			if (repos == null || repos.size() < 1) {
				throw new MavenEclipseApiException(
						Messages.ERROR_NO_REPOSITORIES);
			}

			//PlexusContainer plexus = embedder.getPlexusContainer();
			ArtifactRepository localRepository = MavenPlugin.getDefault().getMaven().getLocalRepository();
			List<ArtifactRepository> remoteRepositories = _getKnownRepositories(
					embedder, md.getType());

			MetadataResolutionRequest req = new MetadataResolutionRequest(md,
					localRepository, remoteRepositories);
			MetadataResolutionResult res = MavenApiUtil.resolveMetadata(
					MavenApiHelper.getRepositorySystem(), req);

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			throw new MavenEclipseApiException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model parsePom(final File file)
			throws MavenEclipseApiException {
		try {
			return MavenPlugin.getDefault().getMaven().readModel(file);
		} catch (Exception ex) {
			throw new MavenEclipseApiException(ex);
		}
	}

}
