/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeClientConfig;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeConsumer;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceConfig;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceMetadataProps;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAConfigurationRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Code Generation Engine. Most of the Code generation calls hit here this class
 * and generates the the model required for calling codegen. Codegen invoker
 * understands the intermediate model of these APIs. The Codegen invoker is
 * finally called for real code generation.
 * 
 * @author smathew
 * 
 * @see CodegenInvoker
 * 
 */
public class BuildSystemCodeGen {
	//private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Generates the interface metadata properties file.
	 *
	 * @param intfProject the intf project
	 * @param nameSpace the name space
	 * @throws Exception the exception
	 */
	public static void generateServiceMetadataProperties(
			SOAIntfProject intfProject, String nameSpace) throws Exception {
		GenTypeServiceMetadataProps codeGenModel = new GenTypeServiceMetadataProps();
		final SOAIntfMetadata metadata = intfProject.getMetadata();
		if (metadata.getOriginalWSDLUrl() != null) {
			codeGenModel.setOriginalWsdlUrl(metadata
					.getOriginalWSDLUrl().toString());
		}
		codeGenModel.setAdminName(metadata.getServiceName());
		codeGenModel.setNamespace(nameSpace);
		codeGenModel.setProjectRoot(intfProject.getProject()
				.getLocation().toString());
		codeGenModel.setServiceName(metadata.getServiceName());
		codeGenModel.setServiceVersion(metadata
				.getServiceVersion());
		codeGenModel.setServiceLayer(metadata
				.getServiceLayer());
		codeGenModel.setServiceInterface(metadata
				.getServiceInterface());
		
		CodegenInvoker codegenInvoker = CodegenInvoker.init(intfProject
				.getProject());
		codegenInvoker.execute(codeGenModel);
		intfProject.getProject().getFolder(SOAProjectConstants.FOLDER_GEN_META_SRC).refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	/**
	 * -scgn Service config group name -wsdl to get the QName of the service (
	 * iff -servicename is not used ) -sicn service impl class name -namespace
	 * -servicename -scv service version.
	 *
	 * @param implProject the impl project
	 * @throws Exception the exception
	 */
	public static void generateServiceConfigXml(final SOAImplProject implProject)
			throws Exception {
		GenTypeServiceConfig genTypeServiceConfig = new GenTypeServiceConfig();
		final SOAIntfMetadata intfMetadata = implProject.getMetadata()
				.getIntfMetadata();
		genTypeServiceConfig.setNamespace(intfMetadata.getTargetNamespace());
		genTypeServiceConfig.setServiceInterface(intfMetadata
				.getServiceInterface());
		genTypeServiceConfig.setServiceName(intfMetadata.getServiceName());
		genTypeServiceConfig
				.setServiceVersion(intfMetadata.getServiceVersion());
		genTypeServiceConfig.setServiceImplClassName(implProject.getMetadata()
				.getServiceImplClassName());
		final IProject project = implProject.getProject();
		genTypeServiceConfig.setSourceDirectory(project.getFolder(
				SOAProjectConstants.FOLDER_SRC).getLocation().toString());
		genTypeServiceConfig.setDestination(project.getLocation().toString());
		genTypeServiceConfig.setOutputDirectory(project.getFolder(
				SOAProjectConstants.CODEGEN_FOLDER_OUTPUT_DIR).getLocation()
				.toString());
		genTypeServiceConfig.setMetadataDirectory(project.getLocation()
				.toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);
		final ISOAConfigurationRegistry config = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getConfigurationRegistry();
		genTypeServiceConfig.setServiceConfigGroup(config.getServiceConfigGroup());
		
		// get the useExternalServiceFactory property value and set it to gen model.
		IFile svcImplProperties = SOAImplUtil
				.getServiceImplPropertiesFile(project);
		if (svcImplProperties.isAccessible() == true) {
			String useExternalFac = PropertiesFileUtil
					.getPropertyValueByKey(
							svcImplProperties.getContents(),
							SOAProjectConstants.PROPS_KEY_USE_EXTERNAL_SERVICE_FACTORY);
			genTypeServiceConfig.setUseExternalServiceFactory(Boolean
					.valueOf(useExternalFac));

		}
		
		genTypeServiceConfig.setProjectRoot(implProject.getProject().getLocation().toFile().toString());

		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		codegenInvoker.execute(genTypeServiceConfig);
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	/**
	 * Generates the Client Config xml.
	 *
	 * @param consumerProject the consumer project
	 * @param clientName the client name
	 * @param environments the environments
	 * @param addedServices the added services
	 * @return the gen type client config
	 * @throws Exception the exception
	 */
	public static GenTypeClientConfig generateGenTypeClientConfigXml(
			final IProject consumerProject, final String clientName, 
			final List<String> environments, 
			final Collection<SOAIntfMetadata> addedServices) throws Exception {
		final GenTypeClientConfig genTypeClientConfig = new GenTypeClientConfig();
		// these are all the service related data
		genTypeClientConfig.setSourceDirectory(consumerProject.getFolder(
				SOAProjectConstants.FOLDER_SRC).getLocation().toString());
		genTypeClientConfig.setDestination(consumerProject.getLocation()
				.toString());
		genTypeClientConfig.setOutputDirectory(consumerProject.getFolder(
				SOAProjectConstants.CODEGEN_FOLDER_OUTPUT_DIR).getLocation()
				.toString());
		genTypeClientConfig.setMetadataDirectory(consumerProject.getLocation()
				.toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);
		final ISOAConfigurationRegistry config = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getConfigurationRegistry();
		genTypeClientConfig.setClientConfigGroup(config.getClientConfigGroup());
		// these are all the service related data
		final Map<String, Map<String, String>> requiredServices = new LinkedHashMap<String, Map<String, String>>();
		for (final SOAIntfMetadata addedService : addedServices) {
			final String serviceName = addedService.getServiceName();
			final String serviceInterface = addedService.getServiceInterface();
			final String namespace = addedService.getTargetNamespace();
			final String serviceLocation = addedService.getServiceLocation();

			final Map<String, String> data = new ConcurrentHashMap<String, String>();
			data.put(BaseCodeGenModel.PARAM_SERVICE_NAME, serviceName);
			data.put(BaseCodeGenModel.PARAM_INTERFACE, serviceInterface);
			//data.put(BaseCodeGenModel.PARAM_CN, serviceName);
			data.put(BaseCodeGenModel.PARAM_NAMESPACE, namespace);
			data.put(BaseCodeGenModel.PARAM_SL, serviceLocation);
			requiredServices.put(serviceName, data);
		}
		genTypeClientConfig.setClientName(clientName);
		final Properties props = SOAConsumerUtil.loadConsumerProperties(consumerProject);
		if (props.containsKey(SOAProjectConstants.PROPS_KEY_CONSUMER_ID)) {
			genTypeClientConfig.setConsumerId(StringUtils.trim(props.getProperty(
					SOAProjectConstants.PROPS_KEY_CONSUMER_ID)));
		}
		genTypeClientConfig.setRequiredServices(requiredServices);
		genTypeClientConfig.setEnvironments(environments);
		return genTypeClientConfig;
	}

	/**
	 * Generate client config xml.
	 *
	 * @param consumerProject the consumer project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void generateClientConfigXml(
			SOAConsumerProject consumerProject, IProgressMonitor monitor) throws Exception {
		final SOAConsumerMetadata metadata = consumerProject.getMetadata();
		GenTypeClientConfig genTypeClientConfig = generateGenTypeClientConfigXml(
				consumerProject.getProject(), metadata.getClientName(), 
				metadata.getEnvironments(), 
				consumerProject.getRequiredServices().values());
		CodegenInvoker codegenInvoker = CodegenInvoker.init(consumerProject
				.getProject());
		codegenInvoker.execute(genTypeClientConfig);
		
		consumerProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
				monitor);
	}

	/**
	 * generate client configure for newly created service.
	 *
	 * @param consumerProject the consumer project
	 * @param addServices the add services
	 * @param consumerEnvs the consumer envs
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void generateClientConfigXmlForAddedService(
			SOAConsumerProject consumerProject,
			Collection<SOAIntfMetadata> addServices, List<String> consumerEnvs,
			IProgressMonitor monitor) throws Exception {

		final SOAConsumerMetadata metadata = consumerProject.getMetadata();
		GenTypeClientConfig genTypeClientConfig = generateGenTypeClientConfigXml(
				consumerProject.getProject(), metadata.getClientName(),
				consumerEnvs, addServices);
		CodegenInvoker codegenInvoker = CodegenInvoker.init(consumerProject
				.getProject());
		codegenInvoker.execute(genTypeClientConfig);

		consumerProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
				monitor);
	}

	/**
	 * Generates the ClientConfig.xml and Consumer code for the given newly
	 * created consumer project.
	 *
	 * @param consumerProject the consumer project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void generateArtifactsForConsumerProject(
			final SOAConsumerProject consumerProject,
			final IProgressMonitor monitor) throws Exception {
		final IProject project = consumerProject.getProject();
		final GenTypeConsumer gentypeConsumer = generateGenTypeConsumer(
				project, consumerProject.getMetadata().getEnvironments().get(0), 
				consumerProject.getRequiredServices().values());
		ProgressUtil.progressOneStep(monitor);
		
		final SOAConsumerMetadata metadata = consumerProject.getMetadata();
		final GenTypeClientConfig gentypeClientConfig = generateGenTypeClientConfigXml(
				project, metadata.getClientName(), metadata.getEnvironments(), 
				consumerProject.getRequiredServices().values());
		ProgressUtil.progressOneStep(monitor);
		final CodegenInvoker codegenInvoker = CodegenInvoker.init(project);

		codegenInvoker.execute(gentypeClientConfig);
		ProgressUtil.progressOneStep(monitor);
		codegenInvoker.execute(gentypeConsumer);
		ProgressUtil.progressOneStep(monitor);
		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	/**
	 * generate the ClientConfig.xml and Consumer code for the given list of
	 * newly added services.
	 *
	 * @param consumerProject the consumer project
	 * @param clientName the client name
	 * @param environments the environments
	 * @param addedServices the added services
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void generateArtifactsForAddedService(
			final IProject consumerProject,
			final String clientName,
			final List<String> environments, 
			final Collection<AssetInfo> addedServices,
			final IProgressMonitor monitor) throws Exception {
		final ISOAAssetRegistry assetRegistry = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem().getAssetRegistry();

		final Collection<SOAIntfMetadata> metadatas = new LinkedHashSet<SOAIntfMetadata>(
				addedServices.size());
		for (final AssetInfo addedService : addedServices) {
			final String assetLocation = assetRegistry
					.getAssetLocation(addedService);
			final SOAIntfMetadata metadata = SOAIntfUtil.loadIntfMetadata(
					assetLocation, addedService.getName());
			metadatas.add(metadata);
		}
		ProgressUtil.progressOneStep(monitor);
		final GenTypeConsumer gentypeConsumer = generateGenTypeConsumer(
				consumerProject, environments.get(0), metadatas);
		ProgressUtil.progressOneStep(monitor);
		final GenTypeClientConfig gentypeClientConfig = generateGenTypeClientConfigXml(
				consumerProject, clientName, environments, metadatas);
		ProgressUtil.progressOneStep(monitor);
		final CodegenInvoker codegenInvoker = CodegenInvoker
				.init(consumerProject.getProject());

		codegenInvoker.execute(gentypeClientConfig);
		ProgressUtil.progressOneStep(monitor);
		codegenInvoker.execute(gentypeConsumer);
		ProgressUtil.progressOneStep(monitor);
		consumerProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
				monitor);
	}
	
	/**
	 * Adds the env mapper to gen type consumer.
	 *
	 * @param genTypeConsumer the gen type consumer
	 * @param project the project
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void addEnvMapperToGenTypeConsumer(GenTypeConsumer genTypeConsumer, 
			IProject project) throws CoreException, IOException {
		final IFile conFile = SOAConsumerUtil.getConsumerPropertiesFile(project);
		if (conFile.isAccessible()) {
			final String envMapper = SOAConsumerUtil.getEnvMapperFromConsumerProperties(project);
			if (StringUtils.isNotBlank(envMapper)) {
				genTypeConsumer.setEnvMapper(envMapper);
			}
		}
	}

	/**
	 * Generates a consumer from the scratch. This is a one time generation and
	 * in builders it is mostly a no operation.
	 *
	 * @param consumerProject the consumer project
	 * @param defaultEnvName the default env name
	 * @param addedServices the added services
	 * @return the gen type consumer
	 * @throws Exception the exception
	 */
	public static GenTypeConsumer generateGenTypeConsumer(
			final IProject consumerProject, final String defaultEnvName, 
			final Collection<SOAIntfMetadata> addedServices) throws Exception {
		final IProject project = consumerProject.getProject();
		final GenTypeConsumer genTypeConsumer = new GenTypeConsumer();
		genTypeConsumer.setSourceDirectory(project.getFolder(
				SOAProjectConstants.FOLDER_SRC).getLocation().toString());
		genTypeConsumer.setDestination(project.getLocation().toString());
		genTypeConsumer.setOutputDirectory(project.getFolder(
				SOAProjectConstants.CODEGEN_FOLDER_OUTPUT_DIR).getLocation()
				.toString());
		genTypeConsumer.setProjectRoot(project.getLocation().toString());
		final IFolder genFolder = SOAServiceUtil.getBaseConsumerFolder(project, 
				GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectType(project));
		genTypeConsumer.setGenFolder(genFolder.getLocation().toString());
		genTypeConsumer.setDefaultEnvironmentName(defaultEnvName);
		final String clientName = SOAConsumerUtil.getClientName(project);
		genTypeConsumer.setClientName(clientName);
		addEnvMapperToGenTypeConsumer(genTypeConsumer, project);
		// these are all the service related data
		final Map<String, Map<String, String>> requiredServices = new LinkedHashMap<String, Map<String, String>>();
		for (final SOAIntfMetadata intfMetadata : addedServices) {
			final Map<String, String> serviceData = new ConcurrentHashMap<String, String>();
			serviceData.put(BaseCodeGenModel.PARAM_SERVICE_NAME, intfMetadata
					.getServiceName());
			serviceData.put(BaseCodeGenModel.PARAM_INTERFACE, intfMetadata
					.getServiceInterface());
			serviceData.put(BaseCodeGenModel.PARAM_SCV, intfMetadata
					.getServiceVersion());
			serviceData.put(BaseCodeGenModel.PARAM_SLAYER, intfMetadata
					.getServiceLayer());
			serviceData.put(BaseCodeGenModel.PARAM_NAMESPACE, intfMetadata
					.getTargetNamespace());
			serviceData.put(BaseCodeGenModel.PARAM_SL, intfMetadata
					.getServiceLocation());
			requiredServices.put(intfMetadata.getServiceName(), serviceData);
		}
		genTypeConsumer.setRequiredServices(requiredServices);
		return genTypeConsumer;
	}

	/**
	 * Returns the test client name for a given service name. Simply adds a
	 * suffix.
	 *
	 * @param serviceName the service name
	 * @return the test client name
	 */
	public static String getTestClientName(final String serviceName) {
		return serviceName + SOAProjectConstants.CLIENT_NAME_SUFFIX_TEST;
	}

	/**
	 * Generates the consumer artifacts.
	 *
	 * @param consumerProject the consumer project
	 * @throws Exception the exception
	 */
	public static void generateConsumer(SOAConsumerProject consumerProject)
			throws Exception {
		final IProject project = consumerProject.getProject();
		final GenTypeConsumer genTypeConsumer = generateGenTypeConsumer(
				project, consumerProject.getMetadata().getEnvironments().get(0), 
				consumerProject.getRequiredServices().values());
		CodegenInvoker codegenInvoker = CodegenInvoker.init(consumerProject
				.getProject());
		codegenInvoker.execute(genTypeConsumer);
		consumerProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
				null);
	}

	/**
	 * Generate artifacts from templates.
	 *
	 * @param templateLoadingClass the template loading class
	 * @param templates the templates
	 * @param templateData the template data
	 * @param destinationFolder the destination folder
	 * @param monitor the monitor
	 * @return the list
	 * @throws CoreException the core exception
	 */
	public static List<IFile> generateArtifactsFromTemplates(
			final Class<?> templateLoadingClass,
			final Map<String, String> templates,
			final Map<String, Object> templateData,
			final IFolder destinationFolder, final IProgressMonitor monitor)
			throws CoreException {
		final List<IFile> result = new ArrayList<IFile>();
		if (templates.isEmpty() == false && templateLoadingClass != null) {// index
			// page
			try {
				for (final String fileName : templates.keySet()) {
					final String templateFileName = templates.get(fileName);
					final File file = destinationFolder.getFile(fileName)
							.getLocation().toFile();
					if (file.exists()) {
						FileUtils.forceDelete(file);
						WorkspaceUtil.refresh(monitor, destinationFolder);
					}

					FreeMarkerUtil.generate(templateData, templateLoadingClass,
							templateFileName, new FileOutputStream(file));
					if (file.exists())
						result.add(destinationFolder.getFile(fileName));
				}

			} catch (Exception e) {
				throw new CoreException(EclipseMessageUtils
						.createErrorStatus(e));
			}
		}
		return result;
	}

}
