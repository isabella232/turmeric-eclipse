/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.eclipse;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


/**
 * Visits the resource tree and decides whether Builder needs to run as far as
 * SOA andCodegen is concerned. Follows the visitor pattern and visits each node
 * and sets the build required to true is there are some interesting changes in
 * the delta fed.
 * 
 * @author smathew
 */
public class BuildResourceDeltaVisitor implements IResourceDeltaVisitor {
	private IJavaProject javaProject;
	private boolean buildRequired = false;
	private ArrayList<String> criteriaList = new ArrayList<String>();
	private String DEFAULT_CRITERIA = SOAProjectConstants.WSDL_EXT;

	/**
	 * This visitor takes the project and the criteria String eg
	 * SOAProjectConstants.WSDL_EXT.
	 * 
	 * @param project IProject
	 * @param criteriaStrs An array of Strings
	 */
	public BuildResourceDeltaVisitor(IProject project, String... criteriaStrs) {
		javaProject = JavaCore.create(project);
		for (String str : criteriaStrs) {
			criteriaList.add(str);
		}
	}

	/**
	 * Visits the code and if there are some changes in delta which we are
	 * interested in, then it will set the build required to true and
	 * immediately return.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	public boolean visit(IResourceDelta delta) throws CoreException {
		// we dont want to go inside the output location.
		// This is JDTs business
		if (StringUtils.containsIgnoreCase(delta.getFullPath().toString(),
				javaProject.getOutputLocation().toString())) {
			return false;
		}
		// this is the only place where SOA Algorithm gives green signal for a
		// build
		// use default string here
		if (criteriaList.size() == 0) {
			if (StringUtils.containsIgnoreCase(delta.getFullPath().toString(),
					DEFAULT_CRITERIA)) {
				setBuildRequired(true);
				return false;
			}
		} else {// use the provided string here
			for (String criteria : criteriaList) {
				if (StringUtils.containsIgnoreCase(delta.getFullPath()
						.toString(), criteria)) {
					setBuildRequired(true);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Return true if build is required.
	 * 
	 * @return boolean
	 */
	public boolean isBuildRequired() {
		return buildRequired;
	}

	/**
	 * @param isBuildRequired 
	 */
	public void setBuildRequired(boolean isBuildRequired) {
		this.buildRequired = isBuildRequired;
	}
}
