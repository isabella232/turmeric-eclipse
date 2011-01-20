/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import org.apache.maven.repository.RepositorySystem;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.Messages;
import org.maven.ide.eclipse.MavenPlugin;
import org.maven.ide.eclipse.embedder.IMaven;
import org.maven.ide.eclipse.embedder.MavenModelManager;
import org.maven.ide.eclipse.index.IndexManager;
import org.maven.ide.eclipse.internal.embedder.MavenImpl;
import org.maven.ide.eclipse.project.MavenProjectManager;

/**
 * 
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 * 
 */
public class MavenApiHelper {

	public static IndexManager getMavenIndexManager() {
		return MavenPlugin.getDefault().getIndexManager();
	}

	// -------------------------------------------------------------------------------------
	public static MavenProjectManager getMavenProjectManager() {
		return MavenPlugin.getDefault().getMavenProjectManager();
	}

	// -------------------------------------------------------------------------------------
	public static MavenModelManager getMavenModelManager() {
		return MavenPlugin.getDefault().getMavenModelManager();
	}

	// -------------------------------------------------------------------------------------
	// public static MavenEmbedder getMavenEmbedder()
	// throws MavenEclipseApiException
	// {
	// return getMavenEmbedder(
	// MavenPlugin.getDefault().getMavenEmbedderManager() );
	// }
	// -------------------------------------------------------------------------------------
	// public static MavenEmbedder getMavenEmbedder( MavenEmbedderManager
	// embedderManager )
	// throws MavenEclipseApiException
	// {
	// if(DEBUG)
	// System.out.println("request for MavenEmbedder");
	//
	// MavenEmbedder embedder = null;
	// try {
	// embedder = embedderManager.createEmbedder(
	// EmbedderFactory.createExecutionCustomizer() );
	// // MavenEmbedder embedder = embedderManager.getWorkspaceEmbedder(); TODO
	// this would come later
	// if( embedder == null )
	// throw new MavenEclipseApiException(
	// Language.getMsg("configure.command.error.noMavenEmbedder") );
	// } catch (CoreException e) {
	// throw new MavenEclipseApiException(e);
	// }
	//
	// if(DEBUG)
	// System.out.println("request for MavenEmbedder: return "+embedder);
	// return embedder;
	// }
	// -------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------
	public static MavenImpl getMavenEmbedder() throws MavenEclipseApiException {
		IMaven embedder = MavenPlugin.getDefault().getMaven();
		if (!(embedder instanceof MavenImpl)) {
			throw new MavenEclipseApiException(Messages.ERROR_NO_MAVEN_IMPL);
		}
		return (MavenImpl) embedder;
	}
	
	public static RepositorySystem getRepositorySystem() throws MavenEclipseApiException {
		try {
			return getMavenEmbedder().getPlexusContainer().lookup(RepositorySystem.class);
		} catch (Exception e) {
			throw new MavenEclipseApiException(e);
		}
	}
	

}
