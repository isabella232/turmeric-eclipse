/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.codegen.utils;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOACodegenTransformer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientEnvironment;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class SOACodegenTransformer.
 *
 * @author yayu
 */
public class SOACodegenTransformer implements ISOACodegenTransformer {

	/**
	 * Instantiates a new sOA codegen transformer.
	 */
	public SOACodegenTransformer() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BaseCodeGenModel transformModel(IProject project,
			final IProgressMonitor monitor) throws Exception {
		BaseCodeGenModel model = new BaseCodeGenModel();

		final boolean isConsumerProject = TurmericServiceUtils
				.isSOAConsumerProject(project);
		if (isConsumerProject) {
			model = new ConsumerCodeGenModel();
		}
		transform(model, project, monitor);

		if (TurmericServiceUtils.isSOAInterfaceProject(project)) {
			final SOAIntfMetadata intfMetadata = SOAIntfUtil.loadIntfMetadata(
					project.getLocation().makeAbsolute().toString(), project
							.getName());
			final Properties properties = SOAIntfUtil
					.loadIntfProjectPropFile(project);
			Object obj = properties
					.get(SOAProjectConstants.PROPS_KEY_NAMESPACE_TO_PACKAGE);
			if (obj != null) {
				final String ns2Pkg = StringUtils.replaceChars(String
						.valueOf(obj), '|', '=');
				model.setNs2pkg(ns2Pkg);
			}

			transform(model, intfMetadata, monitor);
		} else if (TurmericServiceUtils.isSOAImplProject(project)) {
			final ProjectInfo implProjectInfo = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getAssetRegistry().getProjectInfo(project.getName());
			final String serviceName = implProjectInfo
					.getInterfaceProjectName();
			final SOAImplMetadata implMetadata = SOAImplUtil.loadServiceConfig(
					project, serviceName);
			final String assetLocation = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getAssetLocation(serviceName);

			final SOAIntfMetadata intfMetadata = SOAIntfUtil.loadIntfMetadata(
					assetLocation, serviceName);
			if (intfMetadata == null)
				throw new CoreException(
						EclipseMessageUtils
								.createErrorStatus("Can not load metadata for service->"
										+ serviceName));
			transform(model, intfMetadata, monitor);
			model.setServiceImplClassName(implMetadata
					.getServiceImplClassName());
		}

		if (isConsumerProject) {
			final String clientName = SOAConsumerUtil.getClientName(project);
			((ConsumerCodeGenModel) model).setClientName(clientName);
			final Map<SOAClientEnvironment, IFile> configFiles = SOAConsumerUtil
					.getClientConfigFiles(project);
			/*
			 * final Set<String> svcNames = new LinkedHashSet<String>(); for
			 * (SOAClientEnvironment env : configFiles.keySet()) {
			 * svcNames.add(env.getServiceName()); }
			 * 
			 * final Map<String, String> svcClientMap = SOAConsumerUtil
			 * .getMappedServiceNamesFromPropsFile(project, svcNames
			 * .toArray(new String[0]));
			 */

			for (final SOAClientEnvironment env : configFiles.keySet()) {
				// each service should have a corresponding folder in here
				// using its service name
				final IFile clientConfigFile = configFiles.get(env);
				if (clientConfigFile.isAccessible()) {
					final String serviceName = env.getServiceName();
					final String assetLocation = GlobalRepositorySystem
							.instanceOf().getActiveRepositorySystem()
							.getAssetRegistry().getAssetLocation(serviceName);
					final SOAIntfMetadata intfMetadata = SOAIntfUtil
							.loadIntfMetadata(assetLocation, serviceName);
					if (intfMetadata == null)
						throw new CoreException(
								EclipseMessageUtils
										.createErrorStatus("Can not load metadata for service->"
												+ serviceName));
					final Map<String, String> data = new ConcurrentHashMap<String, String>();
					data.put(BaseCodeGenModel.PARAM_SERVICE_NAME, serviceName);
					data.put(BaseCodeGenModel.PARAM_INTERFACE, intfMetadata
							.getServiceInterface());
					data.put(BaseCodeGenModel.PARAM_SLAYER, intfMetadata
							.getServiceLayer());
					data.put(BaseCodeGenModel.PARAM_SCV, intfMetadata
							.getServiceVersion());
					data.put(BaseCodeGenModel.PARAM_NAMESPACE, intfMetadata
							.getTargetNamespace());
					data.put(BaseCodeGenModel.PARAM_SL, intfMetadata
							.getServiceLocation());
					((ConsumerCodeGenModel) model).getRequiredServices().put(
							serviceName, data);
				}
			}

		}

		return model;
	}

	private static BaseCodeGenModel transform(final BaseCodeGenModel model,
			final IProject project, final IProgressMonitor monitor) {
		final String projectLocation = project.getLocation().toString();
		model.setProjectName(project.getName());
		model.setDestination(projectLocation);
		model.setSourceDirectory(project.getFolder(
				SOAProjectConstants.FOLDER_SRC).getLocation().toString());
		model.setOutputDirectory(project.getFolder(
				SOAProjectConstants.CODEGEN_FOLDER_OUTPUT_DIR).getLocation()
				.toString());
		model.setProjectRoot(projectLocation);
		ProgressUtil.progressOneStep(monitor);
		return model;
	}

	private static BaseCodeGenModel transform(final BaseCodeGenModel model,
			final SOAIntfMetadata intfMetadata, final IProgressMonitor monitor)
			throws Exception {
		model.setAdminName(intfMetadata.getServiceName());
		model.setServiceName(intfMetadata.getPublicServiceName());
		model.setServiceVersion(intfMetadata.getServiceVersion());
		model.setServiceInterface(intfMetadata.getServiceInterface());
		model.setNamespace(intfMetadata.getTargetNamespace());
		model.setServiceLayer(intfMetadata.getServiceLayer());
		if (intfMetadata.getOriginalWSDLUrl() != null)
			model.setOriginalWsdlUrl(intfMetadata.getOriginalWSDLUrl()
					.toString());
		ProgressUtil.progressOneStep(monitor);
		return model;
	}

}
