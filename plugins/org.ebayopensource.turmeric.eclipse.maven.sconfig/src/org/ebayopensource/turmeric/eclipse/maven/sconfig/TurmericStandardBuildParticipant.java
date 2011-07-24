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
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
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
		
		MojoExecution exec = getMojoExecution();
		if (exec.getGoal().equals(TurmerStandardProjectConfigurator.GEN_INTERFACE_WSDL)) {		
			if (!serviceNeedsGeneration()) {
				return null;
			}
		}
		
		Set<IProject> sproj = super.build(kind, monitor);
		
		IMavenProjectFacade mproj = getMavenProjectFacade();
		BuildContext buildContext = getBuildContext();
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

	private boolean serviceNeedsGeneration() {
		String source = "/META-INF/soa/services/wsdl";
		
		IMavenProjectFacade mproj = getMavenProjectFacade();
		String basedir = mproj.getMavenProject().getBasedir().getPath();
		BuildContext buildContext = getBuildContext();
		
		IPath[] rsrcPaths = mproj.getResourceLocations();
		for (IPath path : rsrcPaths) {
			if (path.toString().contains("src/main/resources")) {
				IPath wsdlPath = new Path(basedir + path.makeAbsolute().toString() + source);
				Scanner ds = buildContext.newScanner(wsdlPath.toFile());
				ds.scan();
			    String[] includedFiles = ds.getIncludedFiles();
			    if (includedFiles != null && includedFiles.length > 0 ) {
			           return true;
			    }				
			}
		}
		return false;
	}

}
