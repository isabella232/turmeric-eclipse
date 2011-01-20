/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.eclipse;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeErrorLibAll;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;



/**
 * @author yayu
 *
 */
public class TurmericErrorLibraryProjectBuilder extends AbstractSOAProjectBuilder {
	public static final String BUILDER_ID = Activator.PLUGIN_ID
	+ ".TurmericErrorLibraryProjectBuilder";

	/**
	 * 
	 */
	public TurmericErrorLibraryProjectBuilder() {
		super();
	}
	
	/**
	 * Wrapper API for ErrorLibraryDeltaVisitor pattern. Returns all the error domains
	 * which has been modified according to the delta
	 * 
	 * @param delta -
	 *            resource delta
	 * @param project
	 * @return list of modified error domains.
	 * @throws CoreException
	 */
	public static List<IFile> getModifiedErrorDomains(IResourceDelta delta,
			IProject project) throws CoreException {
		ErrorLibraryDeltaVisitor errorLibraryDeltaVisitor = new ErrorLibraryDeltaVisitor(
				project);
		delta.accept(errorLibraryDeltaVisitor);
		return errorLibraryDeltaVisitor.getModifiedErrorDomains();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder#shouldBuild(org.eclipse.core.resources.IResourceDelta, org.eclipse.core.resources.IProject)
	 */
	@Override
	protected boolean shouldBuild(IResourceDelta delta, IProject project)
			throws Exception {
		return BuilderUtil.shouldBuild(delta, project, SOAProjectConstants.XML_EXT);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder#doBuild(int, java.util.Map, org.eclipse.core.resources.IProject, org.eclipse.core.resources.IResourceDelta, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] doBuild(int kind, Map args, IProject project,
			IResourceDelta delta, IProgressMonitor monitor) throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		final String projectLocation = project.getLocation().toString();
		GenTypeErrorLibAll codeGenModel = new GenTypeErrorLibAll(
				projectLocation, projectLocation + "/gen-src");
		codeGenModel.setErrorLibraryName(project.getName());
		final Collection<String> domains = new LinkedHashSet<String>();
		if (delta == null || kind == FULL_BUILD || kind == CLEAN_BUILD) {
			domains.addAll(TurmericErrorLibraryUtils.getAllErrorDomains(project));
		} else {
			for (IFile file : getModifiedErrorDomains(delta, project)) {
				domains.add(file.getParent().getName());
			}
		}
		
		if (domains.isEmpty() == false) {
			codeGenModel.addDomains(domains);
			codegenInvoker.execute(codeGenModel);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder#doClean(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doClean(IProject project, IProgressMonitor monitor)
			throws Exception {
		
	}

}
