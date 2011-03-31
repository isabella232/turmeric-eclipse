/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;


/**
 * Following the visitor pattern, this class is responsible for build trigger
 * algorithm. It will kick in (ideally :)) ) only if there is a change in XSD
 * 
 * @author smathew
 * 
 */
public class TypeLibraryDeltaVisitor implements IResourceDeltaVisitor {

	private IProject project;
	private List<IProject> deletedTypeLibProjects = new ArrayList<IProject>();
	private ArrayList<IFile> modifiedXSdList = new ArrayList<IFile>();
	private ArrayList<IFile> deletedXSDList = new ArrayList<IFile>();

	public TypeLibraryDeltaVisitor(IProject project) {
		this.project = project;
	}

	public TypeLibraryDeltaVisitor() {
	}

	/*
	 * Visits the delta, finds out if there are modified XSDs and returns true
	 * if delta is not over. In addition it populates the deleted xsd files and
	 * modified xsd files list. (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	public boolean visit(IResourceDelta delta) throws CoreException {
		if (!StringUtils.isEmpty(delta.getProjectRelativePath().toString())
				&& delta.getProjectRelativePath().toString().endsWith(
						SOATypeLibraryConstants.EXT_XSD)
				&& delta.getProjectRelativePath().toString().startsWith(
						SOATypeLibraryConstants.FOLDER_META_SRC_TYPES)) {
			if (project != null) {
				if (project.getFile(delta.getProjectRelativePath())
						.isAccessible()) {
					modifiedXSdList.add(project.getFile(delta
							.getProjectRelativePath()));
				}
			} else {
				if (TypeLibraryUtil.isValidXSDFileDeleted(delta.getResource())) {
					deletedXSDList.add((IFile) delta.getResource());
				} else if (TypeLibraryUtil.isValidXSDFileModified(delta
						.getResource())) {
					modifiedXSdList.add((IFile) delta.getResource());
				}
			}
		}
		if (delta.getKind() == IResourceDelta.REMOVED && 
				delta.getResource() instanceof IProject) {
			final IProject typeLibProject = (IProject)delta.getResource();
			if (deletedTypeLibProjects.contains(typeLibProject) == false
					&& typeLibProject.getName().contains("TypeLibrary")) {
				//TODO this is a dirty fix to check the name, 
				//but the project already been deleted so no way to 
				//check the project nature
				deletedTypeLibProjects.add(typeLibProject);
			}
		}
		return true;
	}
	
	public List<IProject> getDeletedProject() {
		return deletedTypeLibProjects;
	}

	/**
	 * @return list of deleted XSDs. See visit method
	 * @see {@link TypeLibraryDeltaVisitor#visit(IResourceDelta)} to see how
	 *      this is populated
	 * @return
	 */
	public ArrayList<IFile> getModifiedXsds() {
		return modifiedXSdList;
	}

	/**
	 * @return list of deleted XSDs. See visit method
	 * @see {@link TypeLibraryDeltaVisitor#visit(IResourceDelta)}to see how
	 *      this is populated
	 */
	public ArrayList<IFile> getDeletedXSDList() {
		return deletedXSDList;
	}

}
