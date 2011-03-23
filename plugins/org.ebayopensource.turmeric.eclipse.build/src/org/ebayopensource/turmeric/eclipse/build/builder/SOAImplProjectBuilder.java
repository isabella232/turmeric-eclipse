/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.build.builder;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.build.SOAFrameworkBuilderActivator;
import org.ebayopensource.turmeric.eclipse.build.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ModelTransformer;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * SOA Implementation Project Builder. Builds all the SOA Implementation
 * artifacts other than the one time generated ones. This calls only on genType -
 * ServiceFromWSDLImpl.
 * 
 * @author smathew
 * 
 */
public class SOAImplProjectBuilder extends AbstractSOAProjectBuilder {

	/**
	 * The Builder ID for the SOAImplProjectBuilder.
	 */
	public static final String BUILDER_ID = SOAFrameworkBuilderActivator.PLUGIN_ID
			+ ".SOAImplProjectBuilder";
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#shouldBuild(org.eclipse.core.resources.IResourceDelta, org.eclipse.core.resources.IProject)
	 */
	@Override
	protected boolean shouldBuild(IResourceDelta delta, IProject project)
			throws Exception {
		for (IProject reqProject : BuilderUtil.getRequiredProjects(
				getProject(), GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectNatureId(SupportedProjectType.INTERFACE))) {
			IResourceDelta deltaReqProj = getDelta(reqProject);
			if (BuilderUtil.shouldBuild(deltaReqProj, reqProject)) {
				return true;
			}
		}
		return BuilderUtil.shouldBuild(delta, project);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#doBuild(int, java.util.Map, org.eclipse.core.resources.IProject, org.eclipse.core.resources.IResourceDelta, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] doBuild(int kind, Map args, IProject project,
			IResourceDelta delta, IProgressMonitor monitor) throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		BaseCodeGenModel baseCodeGenModel = BuilderUtil
				.buildBaseCodeGenModel(project, monitor);
		if (StringUtils.isNotBlank(baseCodeGenModel.getServiceName())) {
			final IProject intfProject = WorkspaceUtil
					.getProject(baseCodeGenModel.getServiceName());
			if (intfProject.isAccessible()) {
				final String newNamespace = BuilderUtil
						.isWSDLTargetNamespaceChanged(
								getDelta(intfProject), baseCodeGenModel
										.getServiceName(), intfProject,
								project);
				if (newNamespace != null) {
					logger.warning(StringUtil.formatString(
							SOAMessages.TNS_CHANGED, baseCodeGenModel
									.getServiceName(), project
									.getName()));
					// update ServiceConfig.xml
					final IFile svcConfigFile = SOAImplUtil
							.getServiceConfigFile(project,
									baseCodeGenModel.getServiceName());
					if (svcConfigFile != null
							&& svcConfigFile.isAccessible() == true) {
						ConfigTool.modifyServiceConfigNamespace(
								newNamespace, svcConfigFile
										.getLocationURI().toURL());
					} else {
						logger.warning(StringUtil.formatString(
								SOAMessages.NO_SVC_CONFIG_FOUND,
								project.getName()));
					}
					// ClientConfig.xml of the Unit Test will be re
					// generated with
					// genType ServiceFromWSDLImpl
				}
			}
		}

		// Gen Type ServiceFromWSDLImpl
		BaseCodeGenModel codeGenModel = ModelTransformer
				.transformToGenTypeServiceFromWSDLImpl(
						baseCodeGenModel, project);
		codegenInvoker.execute(codeGenModel);
		return BuilderUtil.getRequiredProjects(project,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectNatureId(SupportedProjectType.INTERFACE));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#doClean(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doClean(IProject project, IProgressMonitor monitor)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
