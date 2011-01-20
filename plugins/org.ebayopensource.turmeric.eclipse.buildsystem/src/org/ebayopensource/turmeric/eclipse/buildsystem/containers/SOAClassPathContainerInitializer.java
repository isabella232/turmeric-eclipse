/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.containers;

import java.io.File;

import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * SOA Initializer for SOA Container. Associates a given project with the SOA
 * container. Additionally It allows users to attach their sources for debug
 * purposes. Most of the APIs are call backs and are called once in life cycle.
 * 
 * @author smathew
 * 
 */
public class SOAClassPathContainerInitializer extends
		ClasspathContainerInitializer {

	/*
	 * This function associates a project with our container and yes we need it
	 * as there is no way other than this to bind it.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse.core.runtime.IPath,
	 *      org.eclipse.jdt.core.IJavaProject)
	 */
	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		if (isValidContainer(containerPath, project) == false) {
			return;
		}
		SOAClassPathContainer container = new SOAClassPathContainer(
				containerPath, project);
		setClassPathContainer(containerPath, project, container);
	}

	/*
	 * This API is called whenever there is an update request. From SOA
	 * perspective we are handling the source attachment here. we want our users
	 * to be able to attach the source to the library entries
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ClasspathContainerInitializer#requestClasspathContainerUpdate(org.eclipse.core.runtime.IPath,
	 *      org.eclipse.jdt.core.IJavaProject,
	 *      org.eclipse.jdt.core.IClasspathContainer)
	 */
	@Override
	public void requestClasspathContainerUpdate(IPath containerPath,
			IJavaProject project, IClasspathContainer containerSuggestion)
			throws CoreException {
		if (isValidContainer(containerPath, project) == false) {
			return;
		}
		IClasspathEntry[] entries = containerSuggestion.getClasspathEntries();
		// validate the locations
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				IPath path = entry.getPath();
				File lib = path.toFile();
				if (lib.exists() == false || lib.isFile() == false) {
					throw new CoreException(
							EclipseMessageUtils
									.createErrorStatus("The Classpath entry does not refer to an existing library->"
											+ entry.getPath()));

				} else {
					IPath srcPath = entry.getSourceAttachmentPath();
					if (srcPath != null && srcPath.toFile().exists() == false) {
						// the specified source attachment file/folder does not
						// exist
						throw new CoreException(
								EclipseMessageUtils
										.createErrorStatus("The specified source attachment file/folder does not exist->"
												+ srcPath));
					}
				}
			} else {
				// ignore none library classpath entry
			}
		}

		// creating a new instance of SOAClassPathContainer, which would
		// cause
		// the container to be updated properly.
		SOAClassPathContainer soaContainer = new SOAClassPathContainer(
				containerPath, project, entries);
		setClassPathContainer(containerPath, project, soaContainer);
	}

	/*
	 * Returns true to make sure that users could attach java doc and source to
	 * a particular library. It is true that this flag has some additional
	 * meaning but from SOA perspective this is the prime usage
	 */
	@Override
	public boolean canUpdateClasspathContainer(IPath containerPath,
			IJavaProject project) {
		return project != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.ClasspathContainerInitializer#
	 * canUpdateClasspathContainer(org.eclipse.core.runtime.IPath,
	 * org.eclipse.jdt.core.IJavaProject)
	 */
	@Override
	public Object getComparisonID(IPath containerPath, IJavaProject project) {
		if (containerPath != null && project != null)
			return containerPath.segment(0) + "/"
					+ project.getPath().segment(0);
		return super.getComparisonID(containerPath, project);
	}

	private void setClassPathContainer(IPath containerPath,
			IJavaProject project, IClasspathContainer soaContainer) {
		try {
			JavaCore.setClasspathContainer(containerPath,
					new IJavaProject[] { project },
					new IClasspathContainer[] { soaContainer }, null);
		} catch (Exception e) {
			// There could be a chance that the java model has this container,
			// In that case, silently die
			// Another way would be to check if the container exists.
			// That is much more expensive that hitting this exception
			SOAExceptionHandler.silentHandleException(e);
		}
	}

	private boolean isValidContainer(IPath containerPath, IJavaProject project)
			throws JavaModelException {
		if (!project.isOpen())
			project.open(null);
		if (containerPath == null
				|| (!containerPath.segment(0).equals(
						SOAProjectConstants.SOA_CLASSPATH_CONTAINER_ID) && !containerPath
						.lastSegment().equals(
								SOAProjectConstants.SOA_CLASSPATH_CONTAINER_ID)))
			return false;
		return true;
	}

}
