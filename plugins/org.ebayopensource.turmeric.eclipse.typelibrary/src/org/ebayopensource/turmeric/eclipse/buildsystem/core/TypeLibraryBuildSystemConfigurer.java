/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.core;

import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProject;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


/**
 * The Class TypeLibraryBuildSystemConfigurer.
 *
 * @author smathew
 * 
 * The build System configurer, This class glues normal project to build system .
 * adds the nature, java support etc
 */
public class TypeLibraryBuildSystemConfigurer {

	/**
	 * Configure the given source project with the java nature and typelibrary
	 * nature, Also it adds the classpath container as well.
	 *
	 * @param typeLibraryProject -
	 * the source project
	 * @param monitor -
	 * displayable monitor
	 * @throws CoreException if there is some problem. Very rare chance that there could
	 * be one as we are using all the jkdt apis here
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void configure(SOATypeLibraryProject typeLibraryProject,
			IProgressMonitor monitor) throws CoreException, IOException {
		// add java support
		JDTUtil.addJavaSupport(typeLibraryProject.getEclipseMetadata()
				.getProject(), typeLibraryProject.getSourceDirectoryNames(),
				SOAGlobalConfigAccessor.getDefaultCompilerLevel(), 
				SOAProjectConstants.FOLDER_OUTPUT_DIR, monitor);
		// add TypeLib support
		addTypeLibSupport(typeLibraryProject, monitor);
	}

	private static void addTypeLibSupport(
			SOATypeLibraryProject typeLibraryProject, IProgressMonitor monitor)
			throws CoreException {

		ProjectUtil.addNature(typeLibraryProject.getEclipseMetadata()
				.getProject(), monitor, TypeLibraryProjectNature.getTypeLibraryNatureId());

		final IJavaProject javaProject = (IJavaProject) typeLibraryProject
				.getEclipseMetadata().getProject()
				.getNature(JavaCore.NATURE_ID);
		
		BuildSystemUtil.appendSOAClassPath(javaProject, monitor);
	}
}
