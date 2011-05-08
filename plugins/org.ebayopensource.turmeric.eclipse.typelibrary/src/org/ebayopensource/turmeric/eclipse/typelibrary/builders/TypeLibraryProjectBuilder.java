/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.builders;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.buildsystem.SynchronizeWsdlAndDepXML;
import org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model.BaseTypeLibCodegenModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model.TypeLibModelTransformer;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


/**
 * Type Library builder. This builder calls codegen(internally xjc) for xsd to
 * java code generation
 * 
 * @author smathew
 */
public class TypeLibraryProjectBuilder extends AbstractSOAProjectBuilder {
	
	/** The Constant BUILDER_ID. */
	public static final String BUILDER_ID = TypeLibraryActivator.PLUGIN_ID
			+ ".TypeLibraryProjectBuilder";
	//private static final SOALogger logger = SOALogger.getLogger();

	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder#shouldBuild(org.eclipse.core.resources.IResourceDelta, org.eclipse.core.resources.IProject)
	 */
	@Override
	protected boolean shouldBuild(IResourceDelta delta, IProject project)
			throws Exception {
		return BuilderUtil.shouldBuild(delta, project,
				SOATypeLibraryConstants.EXT_XSD);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder#checkProjectHealth(org.eclipse.core.resources.IProject)
	 */
	@Override
	protected IStatus checkProjectHealth(IProject project) throws Exception {
		IStatus status = super.checkProjectHealth(project);
		if (status.isOK() == false)
			return status;
		return TypeLibraryBuilderUtils.checkProjectHealth(project);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder#doBuild(int, java.util.Map, org.eclipse.core.resources.IProject, org.eclipse.core.resources.IResourceDelta, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] doBuild(int kind, Map args, IProject project,
			IResourceDelta delta, IProgressMonitor monitor) throws Exception {
		IStatus status = TypeLibraryBuilderUtils.validateXSDS(project, false);
		if (status.isOK() == false) {
			MarkerUtil.createSOAProblemMarkerRecursive(status, project);
			//return null;
		}
		// Only for clean or full build we need the synchronize
		// dependency to pitch in
		if (kind == CLEAN_BUILD || kind == FULL_BUILD) {
			try {
				SynchronizeWsdlAndDepXML synch = new SynchronizeWsdlAndDepXML(project);
				synch.syncronizeAllXSDsandDepXml();
				synch.synchronizeTypeDepandProjectDep(monitor);
				} catch (Exception e) {
				// Silently ignore. This is just an attempt
			}
		} else {
			final List<IFile> modifiedXsds = TypeLibraryBuilderUtils.getModifiedXsds(delta, project);
			if (modifiedXsds.isEmpty() == false) {
				try {
					SynchronizeWsdlAndDepXML synch = new SynchronizeWsdlAndDepXML(project);
					synch.syncronizeAllXSDsandDepXml(TypeLibraryUtil.getAllXsdFiles(project, true));
					synch.synchronizeTypeDepandProjectDep(monitor);
				} catch (Exception e) {
					// Silently ignore. This is just an attempt
				}
			}
		}
		CodegenInvoker codegenInvoker = TypeLibraryBuilderUtils
				.initForTypeLib(project);
		BaseTypeLibCodegenModel codeGenModel = TypeLibModelTransformer
				.buildBaseTypeLibModel(project);
		codeGenModel = TypeLibModelTransformer
				.transformToGenTypeIncrOrCleanBuildTypeLibrary(
						codeGenModel, project, delta);
		codegenInvoker.execute(codeGenModel);
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
