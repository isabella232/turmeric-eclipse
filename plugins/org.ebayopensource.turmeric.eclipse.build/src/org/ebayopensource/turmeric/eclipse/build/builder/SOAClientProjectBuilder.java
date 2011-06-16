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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.build.SOAFrameworkBuilderActivator;
import org.ebayopensource.turmeric.eclipse.build.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.buildsystem.eclipse.AbstractSOAProjectBuilder;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ModelTransformer;
import org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.tools.codegen.util.CodeGenUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


/**
 * SOA Client project builder. This builder is supposed to be a no operation.
 * But since there are some issues in creating the one time generated files
 * outside this builder, we build the one time generated files in this builder.
 * 
 * @author smathew
 */
public class SOAClientProjectBuilder extends AbstractSOAProjectBuilder {
	/**
	 * The Build Id for the SOAClientProjectBuilder.
	 */
	public static final String BUILDER_ID = SOAFrameworkBuilderActivator.PLUGIN_ID
			+ ".SOAClientProjectBuilder";
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#shouldBuild(org.eclipse.core.resources.IResourceDelta, org.eclipse.core.resources.IProject)
	 */
	@Override
	protected boolean shouldBuild(IResourceDelta delta, IProject project)
			throws Exception {
		if (SOAConsumerUtil.isOldClientConfigDirStructure(project)) {
			// warn the user that they would need to do migration.
			String message = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getProjectHealthChecker()
					.getWarningMessageConsumeProjectStructureOld();

			final IStatus status = EclipseMessageUtils
					.createSOAResourceWarnStatus(project, message, null);
			MarkerUtil.createSOAProblemMarker(status, project);
		}
		return super.shouldBuild(delta, project);
	}

	/**
	 * Checks whether the base consumers for each required service has already
	 * been generated. Those services that already has base consumers will be
	 * removed from the list.
	 * 
	 * @param project
	 * @param baseConsumerModel
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	private boolean shouldGenerateConsumers(final IProject project,
			final ConsumerCodeGenModel baseConsumerModel,
			final IProgressMonitor monitor) throws Exception {
		// TODO lets generate the consumer as long as its required services are
		// not empty
		// TODO remove this logic since it is always regenerating consumers.
		/*
		 * if (baseConsumerModel.getRequiredServices().isEmpty() == false)
		 * return true;
		 */
		// check whether the consumer has already been generated
		boolean shouldGenerate = false;
		monitor.internalWorked(10);
		final Properties props = SOAConsumerUtil.loadConsumerProperties(project);
		List<String> post24Services = new ArrayList<String>();
		if (props.containsKey(SOAProjectConstants.PROPS_NOT_GENERATE_BASE_CONSUMER)) {
			String serviceList = StringUtils.trim(props.getProperty(
					SOAProjectConstants.PROPS_NOT_GENERATE_BASE_CONSUMER, ""));
			post24Services = ListUtil.arrayList(
					StringUtils.split(serviceList, SOAProjectConstants.DELIMITER_COMMA));
		}
		//final String clientName = SOAConsumerUtil.getClientName(project);
		for (Iterator<String> it = baseConsumerModel.getRequiredServices()
				.keySet().iterator(); it.hasNext();) {
			final String serviceName = it.next();
			if (post24Services.contains(serviceName)) {
				logger.info(
						"Service is on the list of not_generate_base_consumer, skip the generation for ->", 
						serviceName);
				it.remove();
				continue;
			}
			final Map<String, String> serviceData = baseConsumerModel
					.getRequiredServices().get(serviceName);
			final String serviceInterface = serviceData
					.get(BaseCodeGenModel.PARAM_INTERFACE);

			String svcConsumerClassName = SOAProjectConstants.BASE
					+ CodeGenUtil.makeFirstLetterUpper(serviceName)
					+ SOAProjectConstants.CLIENT_PROJECT_SUFFIX;
			//TODO we might need to convert the clientname to lowercase
			svcConsumerClassName = JDTUtil
					.generateQualifiedClassNameUsingPathSeperator(
							serviceInterface, 
							SOAProjectConstants.FOLDER_GEN,
							svcConsumerClassName);
			;
			final IFolder srcFolder = SOAServiceUtil
					.getBaseConsumerFolder(project, 
							GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().getProjectType(project));
			srcFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			final IFile consumerFile = srcFolder.getFile(
					svcConsumerClassName + SOAProjectConstants.JAVA_EXT);
			if (consumerFile.exists() == false) {
				logger.info(
						"The base consumer for service->", 
						serviceName, " is missing, need to re-regenerate it. The expected baseconsumer source file location->", 
						consumerFile.getLocation());
				shouldGenerate = true;
			} else {
				it.remove();
			}
		}

		return shouldGenerate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#doBuild(int, java.util.Map, org.eclipse.core.resources.IProject, org.eclipse.core.resources.IResourceDelta, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] doBuild(int kind, Map args, IProject project,
			IResourceDelta delta, IProgressMonitor monitor) throws Exception {
		BaseCodeGenModel baseCodeGenModel = BuilderUtil
		.buildBaseCodeGenModel(project, monitor);
		// Gen Type ClientConfigs
		if (baseCodeGenModel instanceof ConsumerCodeGenModel) {
			ConsumerCodeGenModel consumerModel = null;
			final ConsumerCodeGenModel baseConsumerModel = (ConsumerCodeGenModel) baseCodeGenModel;
			CodegenInvoker codegenInvoker = null;
			// Only if the files are not generated we generate it.
			// Otherwise its just a no operation
			if (shouldGenerateConsumers(project, baseConsumerModel,
					monitor)) {
				// Common init steps
				codegenInvoker = CodegenInvoker.init(project);
				// GenType Consumer
				consumerModel = ModelTransformer
				.transformToGenTypeConsumer(baseConsumerModel,
						project);
				codegenInvoker.execute(consumerModel);
			} else {
				logger.warning("No need to re-generate the base consumer for the consumer project->", project.getName());
			}
		}
		return BuilderUtil.getRequiredProjects(project,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectNatureId(SupportedProjectType.CONSUMER));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.build.builder.AbstractSOAProjectBuilder#doClean(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doClean(IProject project, IProgressMonitor monitor)
			throws Exception {
		IResourceDelta delta = getDelta(project);

		if (BuilderUtil.shouldBuild(delta, project)) {
			BaseCodeGenModel baseCodeGenModel = BuilderUtil
			.buildBaseCodeGenModel(project, monitor);
			// Gen Type ClientConfigs
			if (baseCodeGenModel instanceof ConsumerCodeGenModel) {
				final ConsumerCodeGenModel baseConsumerModel = (ConsumerCodeGenModel) baseCodeGenModel;
				for (final String serviceName : baseConsumerModel
						.getRequiredServices().keySet()) {
					final IProject intfProject = WorkspaceUtil
					.getProject(serviceName);
					if (intfProject.isAccessible()) {
						final String newNamespace = BuilderUtil
						.isWSDLTargetNamespaceChangedInConsumerProject(
								serviceName, intfProject, project);
						if (newNamespace != null) {
							logger.warning(StringUtil.formatString(
									SOAMessages.WSDL_NAMESPACE_CHANGED,
									serviceName));
							for (String envName : SOAConsumerUtil
									.getClientEnvironmentList(project, monitor)) {
								final IFile clientConfigFile = SOAConsumerUtil
								.getClientConfig(project, envName, serviceName);
								if (clientConfigFile != null
										&& clientConfigFile.isAccessible()) {
									ConfigTool.modifyClientConfigNamespace(
											newNamespace, clientConfigFile
											.getLocationURI().toURL());
								} else {
									logger
									.warning(StringUtil
											.formatString(
													SOAMessages.CLIENT_CONFIG_NOT_FOUND,
													serviceName,
													project.getName()));
								}
							}

						}
					}
				}
			}

			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		}
	}

}
