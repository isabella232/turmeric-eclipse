/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.internal.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactScopeEnum;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.apache.maven.repository.metadata.MetadataResolutionRequest;
import org.apache.maven.repository.metadata.MetadataResolutionResult;
import org.apache.maven.repository.metadata.MetadataTreeNode;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;

/**
 * @author bishen
 */
public final class MavenApiUtil {

	private MavenApiUtil() {
	}

	/**
	 * Tries to resolve the given {@code ArtifactMetadata}s in the given
	 * local/remote repositories and returns the resolved {@code Artifact}s.
	 * 
	 * @param embedder
	 * @param mdCollection
	 * @param localRepository
	 * @param remoteRepositories
	 * @return
	 * @throws MavenEclipseApiException 
	 */
	public static List<Artifact> resolveArtifacts(RepositorySystem embedder, List<ArtifactMetadata> mdCollection,
			ArtifactRepository localRepository, List<ArtifactRepository> remoteRepositories) throws MavenEclipseApiException {
		if (mdCollection == null || mdCollection.isEmpty()) {
			return null;
		}

		List<Artifact> res = new ArrayList<Artifact>(mdCollection.size());
		Artifact artifact = null;
		for (Iterator<ArtifactMetadata> i$ = mdCollection.iterator(); i$.hasNext(); res
				.add(artifact)) {
			ArtifactMetadata md = i$.next();
			artifact = embedder.createArtifact(
					md.getGroupId(), md.getArtifactId(), md.getVersion(),
					md.getScope(), md.getType() != null ? md.getType() : "jar");

			resolveArtifact(embedder, artifact, localRepository,
					remoteRepositories);
		}

		return res;
	}

	/**
	 * Tries to resolve the given artifact in the given local/remote
	 * repositories.
	 * 
	 * @param embedder
	 * @param artifact
	 * @param localRepository
	 * @param remoteRepositories
	 * @return
	 * @throws MavenEclipseApiException 
	 */
	public static ArtifactResolutionResult resolveArtifact(RepositorySystem embedder,
			Artifact artifact, ArtifactRepository localRepository,
			List<ArtifactRepository> remoteRepositories) throws MavenEclipseApiException {
		return resolveArtifact(embedder, artifact, localRepository,
				remoteRepositories, false);
	}

	/**
	 * Tries to resolve the given artifact in the given local/remote
	 * repositories.
	 * 
	 * @param embedder
	 * @param artifact
	 * @param localRepository
	 * @param remoteRepositories
	 * @param resolveDependencies
	 * @return
	 * @throws MavenEclipseApiException 
	 */
	public static ArtifactResolutionResult resolveArtifact(RepositorySystem embedder,
			Artifact artifact, ArtifactRepository localRepository,
			List<ArtifactRepository> remoteRepositories,
			boolean resolveDependencies) throws MavenEclipseApiException {
		if (embedder == null) {
			return null;
		}
		
		ArtifactResolutionRequest request = new ArtifactResolutionRequest();
		request.setArtifact(artifact);
		request.setLocalRepository(localRepository);
		request.setRemoteRepositories(remoteRepositories);
		request.setResolveTransitively(resolveDependencies);
		
		return embedder.resolve(request);
	}

	/**
	 * Tries to resolve a metadata tree based on the given
	 * {@code MetadataResolutionRequest} and embed it in a
	 * {@code MetadataResolutionResult}.
	 * 
	 * @param embedder
	 * @param req
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public static MetadataResolutionResult resolveMetadata(RepositorySystem embedder,
			MetadataResolutionRequest req) throws MavenEclipseApiException {
		MetadataResolutionResult res = new MetadataResolutionResult();
		MetadataTreeNode tree = resolveMetadataTree(embedder, req.getQuery(),
				null, req.getLocalRepository(), req.getRemoteRepositories());
		res.setTree(tree);

		return res;
	}

	private static MetadataTreeNode resolveMetadataTree(RepositorySystem embedder,
			ArtifactMetadata query, MetadataTreeNode parent,
			ArtifactRepository localRepository, List remoteRepositories)
			throws MavenEclipseApiException {
		try {
			Artifact pomArtifact = embedder
					.createArtifact(query.getGroupId(), query.getArtifactId(),
							query.getVersion(), query.getScope(),
							query.getType() != null ? query.getType() : "jar");
			//Artifact art = MavenApiHelper.getMavenEmbedder().resolve(groupId, artifactId, version, type, classifier, remoteRepositories, monitor);
			
			ArtifactResolutionResult result = resolveArtifact(embedder,
					pomArtifact, localRepository, remoteRepositories, false);

			if (pomArtifact.isResolved()) {
				MetadataTreeNode node = new MetadataTreeNode(query, parent,
						true, query.getScopeAsEnum());
				Set<Artifact> artifacts = result.getArtifacts();
				if (artifacts.size() > 1) {
					List<Artifact> dependencies = new ArrayList<Artifact>(
							artifacts);
					// remove the first one, which is the source artifact itself
					dependencies.remove(0);

					int nKids = dependencies.size();
					node.setNChildren(nKids);
					int kidNo = 0;
					MetadataTreeNode kidNode;
					for (Artifact artifact : dependencies) {
						ArtifactMetadata amd = new ArtifactMetadata(
								artifact.getGroupId(),
								artifact.getArtifactId(),
								artifact.getVersion(), artifact.getType(),
								ArtifactScopeEnum.valueOf(artifact.getScope()));
						// 20100514#emac: no need to resolve dependencies
						// recursively
						// kidNode = resolveMetadataTree(embedder, amd, node,
						// localRepository, remoteRepositories);
						kidNode = new MetadataTreeNode(amd, node,
								artifact.isResolved(), amd.getScopeAsEnum());
						node.addChild(kidNo++, kidNode);
					}
				}

				return node;
			}

			return new MetadataTreeNode(pomArtifact, parent, false,
					query.getArtifactScope());
		} catch (Exception anyEx) {
			throw new MavenEclipseApiException(anyEx);
		}
	}

}
