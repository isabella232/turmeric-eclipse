/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.buildsystem;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;


/**
 * Extending classes holds the class path for both V3 and Maven respectively.
 * This is the common point where the corresponding class path entries are
 * contributed to the framework. Here we abstract the Maven and SOA container
 * class path entries and give it to the SOA container in an uniform fashion.
 * Typically for Maven this is managed by the Maven container, but for V3 we are
 * using this as the sole container. From a SOA project functionality this is
 * one of the key classes.
 * 
 * @author smathew
 * 
 */
public abstract class AbstractSOAClassPathContainer {

	/** The path. */
	protected IPath path;
	
	/** The project. */
	protected IJavaProject project;

	/**
	 * Instantiates a new abstract soa class path container.
	 *
	 * @param path the path
	 * @param javaProject the java project
	 */
	public AbstractSOAClassPathContainer(IPath path, IJavaProject javaProject) {
		this.path = path;
		this.project = javaProject;
	}

	/**
	 * Gets the classpath entries.
	 *
	 * @return the classpath entries
	 */
	public abstract IClasspathEntry[] getClasspathEntries();

	/**
	 * Gets the unique class path entries. This is to make sure that in case if
	 * the jar or project is in the class path, some where outside the container
	 * also, we will not add this to the container. So Ideally wit this fix
	 * users should never face the duplicate class path issue dues to an entry
	 * in this class path. This is a bad fix for now. V3 interacts with the
	 * plugin and add some project dependency during the V3 build. And this ends
	 * in duplicate class path entry. Avoiding that here
	 *
	 * @return the unique classpath entries
	 * @throws JavaModelException the java model exception
	 */
	public IClasspathEntry[] getUniqueClasspathEntries()
			throws JavaModelException {
		IClasspathEntry[] containerClassPathEntriesArr = getClasspathEntries();
		ArrayList<IClasspathEntry> uniqueList = new ArrayList<IClasspathEntry>();
		List<IClasspathEntry> rawClassPathList = JDTUtil.rawClasspath(project
				.getProject(), true);
		boolean unique = true;
		for (IClasspathEntry classpathEntry : containerClassPathEntriesArr) {
			unique = true;
			for (IClasspathEntry rawClasspathEntry : rawClassPathList) {
				if (classpathEntry.getPath()
						.equals(rawClasspathEntry.getPath())) {
					unique = false;
					break;
				}
			}
			// add this because its not present in the projects class path
			if (unique) {
				uniqueList.add(classpathEntry);
			}
		}

		return uniqueList.toArray(new IClasspathEntry[0]);
	}

}
