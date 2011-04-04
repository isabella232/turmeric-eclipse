/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.build.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.build.SOAFrameworkBuilderActivator;
import org.ebayopensource.turmeric.eclipse.buildsystem.SynchronizeWsdlAndDepXML;
import org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ModelTransformer;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


/**
 * The interface builder builds the SOA interface projects. From codegen
 * perspective it use the gentype ServiceFromWSDLIntf. One additional step is
 * synchronizing the type libraries entries in Type dependency.xml.
 * 
 * @author smathew
 */
public class SOAInterfaceProjectBuilder extends AbstractSOAProjectBuilder {

	/**
	 * The Builder ID for the SOAInterfaceProjectBuilder.
	 */
	public static final String BUILDER_ID = SOAFrameworkBuilderActivator.PLUGIN_ID
			+ ".SOAInterfaceProjectBuilder";

	private static final SOALogger logger = SOALogger.getLogger();
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#shouldBuild(org.eclipse.core.resources.IResourceDelta, org.eclipse.core.resources.IProject)
	 */
	@Override
	protected boolean shouldBuild(IResourceDelta delta, IProject project)
			throws Exception {
		return super.shouldBuild(delta, project) || BuilderUtil
		.isWSDLFileChanged(delta, project);
	}


	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#doBuild(int, java.util.Map, org.eclipse.core.resources.IProject, org.eclipse.core.resources.IResourceDelta, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] doBuild(int kind, Map args, IProject project,
			IResourceDelta delta, IProgressMonitor monitor) throws Exception {
		if (kind == CLEAN_BUILD || kind == FULL_BUILD) {
			try {
				SynchronizeWsdlAndDepXML synch = new SynchronizeWsdlAndDepXML(project);
 				synch.syncronizeWsdlandDepXml();
				synch.synchronizeTypeDepandProjectDep(monitor);
			} catch (Exception e) {
				SOAExceptionHandler.silentHandleException(e);
				// Silently ignore. This is just an attempt
			}
		}

		// validate service WSDL when WSDL file is modified.
		try {
			IFile wsdlFile = SOAServiceUtil.getWsdlFile(project, project
					.getName());
			List<IStatus> wtpStatus = new ArrayList<IStatus>();
			ActionUtil.validateUsingWTP(wsdlFile, wsdlFile.getLocationURI()
					.toURL(), wtpStatus, true, monitor);
		} catch (Exception e) {
			SOAExceptionHandler.silentHandleException(e);
		}
		final IFile oldMetadataFile = SOAIntfUtil.getOldMetadataFile(project, project.getName());
		final IFile newMetadataFile = SOAIntfUtil.getNewMetadataFile(project, project.getName());

		if (oldMetadataFile.exists() == true) {
			String message = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getProjectHealthChecker()
					.getWarningMessageIntfProjectStructureOld();

			MarkerUtil.createSOAProblemMarker(EclipseMessageUtils
					.createErrorStatus(message), oldMetadataFile);
		} else if (newMetadataFile.exists() == false) {
			logger.warning("The service_metadata.properties file is missing, re-genreate it");
		}

		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		BaseCodeGenModel baseCodeGenModel = BuilderUtil
		.buildBaseCodeGenModel(project, monitor);

		BaseCodeGenModel codeGenModel = ModelTransformer
		.transformToGenTypeServiceFromWSDLIntf(
				baseCodeGenModel, project);
		codegenInvoker.execute(codeGenModel);		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#doClean(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doClean(IProject project, IProgressMonitor monitor)
			throws Exception {
		
	}
}
