/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.containers;

import java.util.Comparator;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;


/**
 * Dynamically finds the class path from the build system and contribute to the
 * project class path. Also projects hold precedence over libraries. Queries the
 * build system for the class path entries. This class swallows exception for
 * the time being. This behavior will be changed soon.
 * 
 * @author smathew
 * 
 */
public class SOAClassPathContainer implements IClasspathContainer {
	/**
	 * The list of class path entries to be added for the SOA Project.
	 */
	IClasspathEntry entries[];
	
	/**
	 * The project or container path.
	 */
	IPath containerPath;

	/**
	 * Pre-populates the entries to avoid multiple queries to the build system.
	 * The entries are stored in an instance variable and will be returned from
	 * the subsequent call to getClasspathEntries.
	 * 
	 * @param containerPath -
	 *            container's path representation
	 * @param project -
	 *            the project on which this container to be stitched
	 */
	public SOAClassPathContainer(IPath containerPath, IJavaProject project) {
		populateClasspath(containerPath, project);
		this.containerPath = containerPath;
	}

	/**
	 * 
	 * This is the same as the above constructor except for the fact that this
	 * one honors the existing parameter class path values.
	 * 
	 * @param containerPath
	 *            container's path representation
	 * @param project -
	 *            the project on which this container to be stitched
	 * @param entries
	 *            The classpath entries - can be null
	 */
	public SOAClassPathContainer(IPath containerPath, IJavaProject project,
			IClasspathEntry entries[]) {
		if (entries != null) {
			this.entries = entries;
			TreeSet<IClasspathEntry> treeSet = new TreeSet<IClasspathEntry>(
					new ClassPathComparator());
			for (IClasspathEntry entry : entries) {
				treeSet.add(entry);
			}
			entries = treeSet.toArray(new IClasspathEntry[0]);

		} else {
			populateClasspath(containerPath, project);
		}
		this.containerPath = containerPath;
	}

	/**
	 * {@inheritDoc}
	 */
	public IClasspathEntry[] getClasspathEntries() {
		return entries;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
	 */
	public String getDescription() {
		return "SOA Library Dependencies";
	}

	/**
	 * This kind is required so that the Run configuration can fetch our class
	 * path. Most other kinds are ignored by the run configuration.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jdt.core.IClasspathContainer#getKind()
	 */
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	/**
	 * {@inheritDoc}
	 */
	public IPath getPath() {
		return containerPath;
	}

	/**
	 * Populates the class path from the build system. It maintains the
	 * uniqueness but not by itself but the API it uses guarantee this. The
	 * entries are stored in an instance variable and will be returned from the
	 * subsequent call to getClasspathEntries.
	 * 
	 * @param containerPath
	 * @param project
	 */
	private synchronized void populateClasspath(IPath containerPath,
			IJavaProject project) {
		try {
			entries = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getClassPathContainer(
							containerPath, project).getUniqueClasspathEntries();
			TreeSet<IClasspathEntry> treeSet = new TreeSet<IClasspathEntry>(
					new ClassPathComparator());
			for (IClasspathEntry entry : entries) {
				treeSet.add(entry);
			}
			entries = treeSet.toArray(new IClasspathEntry[0]);

		} catch (Exception e) {
			SOAExceptionHandler.silentHandleException(e);
			// TODO: Show some error dialog. Not sure how should we proceed.
		}
	}

	/**
	 * This is to make sure that projects are above jar entries Reordering the
	 * class path entries thats all. Especially for debugging purpose SOA
	 * developers wants Project entries to be always above libraries.
	 * 
	 * @author smathew
	 * 
	 */
	class ClassPathComparator implements Comparator<IClasspathEntry> {
		/**
		 * {@inheritDoc}
		 */
		public int compare(IClasspathEntry o1, IClasspathEntry o2) {
			if (o1 != null && o2 != null) {
				if (o1.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					return -1;
				}
				if (o2.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					return 1;
				}
			}
			return -1;
		}
	}
}
