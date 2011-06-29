/*******************************************************************************
 * Copyright (c) 2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.sconfig;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * The Class TurmericStandardBuildParticipant.
 */
public class TurmericStandardBuildParticipant extends
		MojoExecutionBuildParticipant {

	private static final String GENERATED_SOURCES = "target/generated-sources";

	/**
	 * Instantiates a new turmeric standard build participant.
	 * 
	 * @param execution
	 *            the execution
	 */
	public TurmericStandardBuildParticipant(MojoExecution execution) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {
		
		Set<IProject> sproj = super.build(kind, monitor);
		
		BuildContext buildContext = getBuildContext();
		IMavenProjectFacade mproj = getMavenProjectFacade();

		IProject proj = mproj.getProject();
		
		proj.refreshLocal(IResource.DEPTH_INFINITE, monitor);

		IFile generatedSource = proj.getFile(GENERATED_SOURCES);
			File generatedSourceFolder = generatedSource.getFullPath().toFile();
			buildContext.refresh(generatedSourceFolder);

		IFile generatedResource = proj.getFile(GENERATED_SOURCES);
			File generatedResourceFolder = generatedResource.getFullPath()
					.toFile();
			buildContext.refresh(generatedResourceFolder);

		return sproj;

	}

}
