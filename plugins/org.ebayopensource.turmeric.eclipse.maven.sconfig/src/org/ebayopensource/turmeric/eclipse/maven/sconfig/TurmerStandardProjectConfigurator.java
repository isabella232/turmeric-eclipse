/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.sconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.maven.ide.eclipse.project.configurator.AbstractProjectConfigurator;
import org.maven.ide.eclipse.project.configurator.ProjectConfigurationRequest;

public class TurmerStandardProjectConfigurator extends
		AbstractProjectConfigurator {

	private static final String GEN_TYPELIBRARY = "gen-typelibrary";
	private static final String GEN_ERRORLIBRARY = "gen-errorlibrary";
	private static final String GEN_IMPLEMENTATION = "gen-implementation";
	private static final String GEN_INTERFACE_WSDL = "gen-interface-wsdl";
	private static final String TURMERIC_MAVEN_PLUGIN = "turmeric-maven-plugin";

	public TurmerStandardProjectConfigurator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure(ProjectConfigurationRequest projRequest,
			IProgressMonitor monitor) throws CoreException {

		if (projRequest == null) {
			return;
		}

		SupportedProjectType projectType = null;
		IProject project = projRequest.getProject();
		if (isInterfaceProject(projRequest)) {
			projectType = SupportedProjectType.INTERFACE;
		} else if (isImplementationProject(projRequest)) {
			projectType = SupportedProjectType.IMPL;
		} else if (isErrorLibProject(projRequest)) {
			projectType = SupportedProjectType.ERROR_LIBRARY;
		} else if (isTypeLibProject(projRequest)) {
			projectType = SupportedProjectType.TYPE_LIBRARY;
		} else if (isConsumerLibProject(projRequest)) {
			projectType = SupportedProjectType.CONSUMER;
		} else {
			return;
		}

		String natureId = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(projectType);

		JDTUtil.addNatures(project, monitor, natureId);

		List<IPath> additionalSrcDirs = new ArrayList<IPath>();
		additionalSrcDirs.add(new Path("target/generated-sources/codegen"));
		additionalSrcDirs.add(new Path("target/generated-resources/codegen"));

		final IJavaProject javaProject = JavaCore.create(project);

		final List<IClasspathEntry> entries = ListUtil.arrayList(javaProject
				.readRawClasspath());
		for (IPath path : additionalSrcDirs) {
			IFolder folder = project.getFolder(path);
			if (folder.exists()) {
				IPath srcPath = project.getFolder(path).getFullPath();
				if (containsSourcePath(entries, srcPath) == false) {
					entries.add(JavaCore.newSourceEntry(srcPath, new IPath[0]));
				}
			}
		}
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[0]), monitor);

	}

	private boolean containsSourcePath(List<IClasspathEntry> entries,
			IPath srcPath) {
		for (IClasspathEntry entry : entries) {
			if (entry.getPath().equals(srcPath))
				return true;
		}
		return false;
	}

	public boolean isInterfaceProject(ProjectConfigurationRequest projRequest) {

		return isProjectType(GEN_INTERFACE_WSDL, projRequest);
	}

	private boolean isProjectType(String goalType,
			ProjectConfigurationRequest projRequest) {
		MavenProject mproj = projRequest.getMavenProject();
		List<Plugin> buildPlugins = mproj.getBuildPlugins();
		for (Plugin mplug : buildPlugins) {
			if (TURMERIC_MAVEN_PLUGIN.equals(mplug.getArtifactId())) {
				List<PluginExecution> exList = mplug.getExecutions();

				for (PluginExecution pexec : exList) {
					List<String> goals = pexec.getGoals();
					for (String goal : goals) {
						if (goalType.equals(goal)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isImplementationProject(
			ProjectConfigurationRequest projRequest) {
		return isProjectType(GEN_IMPLEMENTATION, projRequest);
	}

	public boolean isErrorLibProject(ProjectConfigurationRequest projRequest) {
		return isProjectType(GEN_ERRORLIBRARY, projRequest);
	}

	public boolean isTypeLibProject(ProjectConfigurationRequest projRequest) {
		return isProjectType(GEN_TYPELIBRARY, projRequest);
	}

	public boolean isConsumerLibProject(ProjectConfigurationRequest projRequest) {
		return isFileAccessible(projRequest.getProject(),
				SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER);
	}

	private static boolean isFileAccessible(IProject project,
			String fileRelativePath) {
		if (project.isAccessible()) {
			return project.getFile(fileRelativePath).isAccessible();
		}
		return false;
	}

}
