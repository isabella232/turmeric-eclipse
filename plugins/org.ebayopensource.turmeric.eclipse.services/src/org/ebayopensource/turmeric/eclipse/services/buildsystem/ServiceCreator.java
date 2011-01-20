/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.buildsystem;

import java.util.ArrayList;
import java.util.Arrays;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemCodeGen;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAConsumeNewServiceFailedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.template.wsdl.processors.WSDLTemplateProcessor;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author smathew All create wizards are going to hit here after validation
 */
public class ServiceCreator {
	private static final SOALogger logger = SOALogger.getLogger();

	public static void createServiceFromBlankWSDL(
			ServiceFromTemplateWsdlParamModel paramModel,
			IProgressMonitor monitor) throws Exception {
		WSDLTemplateProcessor wsdlTemplateProcessor = new WSDLTemplateProcessor(
				paramModel);
		SOAIntfProject intfProject = InterfaceCreator
				.createIntfModelFromBlankWsdl(paramModel, monitor);
		SOAImplProject implProject = ImplementationCreator
				.createImplModelFromBlankWsdl(paramModel, intfProject, monitor);
		InterfaceCreator.createIntfProjectFromBlankWsdl(intfProject,
				implProject, paramModel.getTargetNamespace(),
				wsdlTemplateProcessor, monitor);
		ImplementationCreator.createImplProjectFromBlankWsdl(implProject,
				intfProject, monitor);

	}

	public static void createServiceFromExistingWSDL(
			final ServiceFromWsdlParamModel paramModel,
			final IProgressMonitor monitor) throws Exception {
		SOAIntfProject intfProject = InterfaceCreator
				.createIntfModelFromExistingWsdl(paramModel, monitor);
		SOAImplProject implProject = ImplementationCreator
				.createImplModelFromExistingWsdl(paramModel, intfProject,
						monitor);
		InterfaceCreator.createIntfProjectFromExistingWsdl(intfProject,
				implProject, paramModel.getTargetNamespace(), monitor);
		ImplementationCreator.createImplProjectFromExistingWsdl(implProject,
				intfProject, monitor);

	}

	public static void createConsumerFromExistingWSDL(
			final ConsumerFromWsdlParamModel paramModel,
			final IProgressMonitor monitor) throws Exception {
		SOAIntfProject intfProject = InterfaceCreator
				.createIntfModelFromExistingWsdl(paramModel, monitor);

		SOAConsumerProject consumerProject = ConsumerCreator
				.createConsumerModelFromExistingWsdl(paramModel, intfProject,
						monitor);
		InterfaceCreator
				.createIntfProjectFromExistingWsdl(intfProject, monitor);

		ConsumerCreator.createConsumerProjectFromExistingWsdl(consumerProject,
				intfProject, monitor);
	}

	public static void addServiceToConsumerFromWSDL(
			final ConsumerFromWsdlParamModel paramModel,
			SOAConsumerProject consumerProject, final IProgressMonitor monitor)
			throws Exception {
		// create interface project staff
		SOAIntfProject intfProject = InterfaceCreator
				.createIntfModelFromExistingWsdl(paramModel, monitor);

		InterfaceCreator
				.createIntfProjectFromExistingWsdl(intfProject, monitor);

		SOAIntfMetadata metadata = intfProject.getMetadata();

		// add service to required services
		consumerProject.getRequiredServices().put(metadata.getServiceName(),
				metadata);

		// services configuration
		ISOAProjectConfigurer configurer = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectConfigurer();

		AssetInfo assertObj = (AssetInfo) GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry().getAsset(
						intfProject.getProject());

		try {
			logger.info("Consuming new services->", intfProject
					.getProjectName());
			
			// generate client configure for newly created service
			BuildSystemCodeGen.generateClientConfigXmlForAddedService(
					consumerProject, Arrays.asList(metadata), paramModel
							.getEnvironments(), monitor);
			ProgressUtil.progressOneStep(monitor);

			// newly create service is always shared consumer, add this service
			// to the not_genreate_base_consumer list
			SOAConsumerUtil.modifyNotGenerateBaseConsumers(consumerProject
					.getProject(), Arrays.asList(metadata.getServiceName()),
					new ArrayList<String>(), monitor);
			ProgressUtil.progressOneStep(monitor);

//			BuildSystemConfigurer.configure(consumerProject, monitor);
			// add service to consumer dependency list
			configurer.addDependencies(consumerProject.getProjectName(),
					ListUtil.arrayList(assertObj), monitor);
			ProgressUtil.progressOneStep(monitor);
			// update consumer project classpath
			BuildSystemUtil.updateSOAClasspathContainer(consumerProject
					.getProject());
			ProgressUtil.progressOneStep(monitor);

		} catch (Exception e) {
			logger.error(e);
			throw new SOAConsumeNewServiceFailedException(
					"Failed to add new service to consumer project->"
							+ consumerProject.getProjectName(), e);
		}

	}

	public static void createConsumerFromJava(
			final ConsumerFromJavaParamModel uiModel,
			final IProgressMonitor monitor) throws Exception {
		SOAConsumerProject consumerProject = ConsumerCreator
				.createConsumerModelFromJava(uiModel, monitor);
		ConsumerCreator.createConsumerProjectFromJava(consumerProject, 
				uiModel.isConvertingJavaProject(), monitor);
	}

}
