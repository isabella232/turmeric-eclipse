/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.model.SOAErrorLibraryProject;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject.SOAProjectSourceDirectory;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;


/**
 * @author yayu
 * @since 1.0.0
 */
public class ErrorLibraryBuildSystemConfigurer {

	public static void configure(SOAErrorLibraryProject errorLibraryProject,
			IProgressMonitor monitor) throws CoreException {
		// add java support
		addJavaSupport(errorLibraryProject, errorLibraryProject
				.getRawOutputFolder(), monitor);
		addSOASupport(errorLibraryProject.getProject(), monitor);
	}

	public static void reorderClasspath(IProject project,
			IProgressMonitor monitor) throws JavaModelException {
		final IJavaProject javaProject = JavaCore.create(project);

		final List<IClasspathEntry> entries = JDTUtil.rawClasspath(javaProject,
				true);
		Collections.sort(entries, ClasspathComparator.INSTANCE);

		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[0]),
				monitor);
	}

	private static class ClasspathComparator implements
			Comparator<IClasspathEntry> {
		private static final ClasspathComparator INSTANCE = new ClasspathComparator();

		public int compare(IClasspathEntry e1, IClasspathEntry e2) {

			if (e1.getEntryKind() == e2.getEntryKind())
				return 0;
			else if (e1.getEntryKind() == IClasspathEntry.CPE_SOURCE)
				return -1;
			else if (e2.getEntryKind() == IClasspathEntry.CPE_SOURCE)
				return 1;
			else if (e1.getEntryKind() == IClasspathEntry.CPE_CONTAINER)
				return -1;
			return 0;
		}

	}

	public static void addJavaSupport(
			SOAErrorLibraryProject errorLibraryProject, String outputLocation,
			IProgressMonitor monitor) throws CoreException {
		final IProject project = errorLibraryProject.getProject();
		boolean changedClasspath = false;
		if (JDTUtil.addJavaNature(project, monitor)) {
			changedClasspath = true;
		}
		// Configuring the Java Project
		final IJavaProject javaProject = JavaCore.create(project);
		final List<IClasspathEntry> classpath = JDTUtil.rawClasspath(
				javaProject, true);
		final List<IClasspathEntry> classpathContainers = new ArrayList<IClasspathEntry>();
		// TODO Lets see if we need this
		if (outputLocation.equals(javaProject.getOutputLocation()) == false) {
			final IFolder outputDirClasses = project.getFolder(outputLocation);
			javaProject.setOutputLocation(outputDirClasses.getFullPath(),
					monitor);
			changedClasspath = true;
		}

		// Dealing with the case where the root of the project is set to be the
		// src and bin destinations... bad... bad...
		for (final Iterator<IClasspathEntry> iterator = classpath.iterator(); iterator
				.hasNext();) {
			final IClasspathEntry entry = iterator.next();
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				classpathContainers.add(entry);
			}
			if (entry.getEntryKind() != IClasspathEntry.CPE_SOURCE
					|| !entry.getPath().equals(
							new Path("/" + project.getName())))
				continue;
			iterator.remove();
			changedClasspath |= true;
		}
		for (final SOAProjectSourceDirectory dir : errorLibraryProject
				.getSourceDirectories()) {
			if (!WorkspaceUtil.isDotDirectory(dir.getLocation())) {
				final IFolder source = project.getFolder(dir.getLocation());
				// If the Java project existed previously, checking if
				// directories
				// already exist in
				// its classpath.
				boolean found = false;
				for (final IClasspathEntry entry : classpath) {
					if (!entry.getPath().equals(source.getFullPath()))
						continue;
					found = true;
					break;
				}
				if (found)
					continue;
				changedClasspath |= true;
				IPath[] excludePatterns = ClasspathEntry.EXCLUDE_NONE;
				if (dir.getExcludePatterns() != null) {
					int length = dir.getExcludePatterns().length;
					excludePatterns = new Path[length];
					for (int i = 0; i < length; i++) {
						excludePatterns[i] = new Path(
								dir.getExcludePatterns()[i]);
					}
				}
				IPath outputPath = dir.getOutputLocation() != null ? project
						.getFolder(dir.getOutputLocation()).getFullPath()
						: null;
				final IClasspathEntry entry = JavaCore.newSourceEntry(source
						.getFullPath(), excludePatterns, outputPath);
				classpath.add(entry);
			}
		}
		ProgressUtil.progressOneStep(monitor);
		// Adding the runtime library
		boolean found = false;
		for (final IClasspathEntry entry : classpath) {
			// All JRE Containers should have a prefix of
			// org.eclipse.jdt.launching.JRE_CONTAINER
			if (JavaRuntime.newDefaultJREContainerPath().isPrefixOf(
					entry.getPath())
					&& JavaRuntime.newDefaultJREContainerPath().equals(
							entry.getPath())) {
				found = true;
				break;
			}
		}
		if (!found) {
			changedClasspath = true;
			classpath.add(JavaRuntime.getDefaultJREContainerEntry());
		}
		// we want all classpath containers to be the end of .classpath file
		classpath.removeAll(classpathContainers);
		classpath.addAll(classpathContainers);

		ProgressUtil.progressOneStep(monitor);
		// Configuring the classpath of the Java Project
		if (changedClasspath) {
			javaProject.setRawClasspath(classpath
					.toArray(new IClasspathEntry[0]), null);
		}
	}

	public static void addSOASupport(IProject errorLibProject,
			IProgressMonitor monitor) throws CoreException {
		ProjectUtil.addNature(errorLibProject, monitor,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectNatureId(SupportedProjectType.ERROR_LIBRARY));

		final IJavaProject javaProject = (IJavaProject) errorLibProject
				.getNature(JavaCore.NATURE_ID);

		BuildSystemUtil.appendSOAClassPath(javaProject, monitor);
	}
}
