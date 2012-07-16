/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.util.Map;
import java.util.Set;

import javax.wsdl.WSDLException;

import org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeConsumer;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeGlobalClientConfig;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeGlobalClientConfigConsumer;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeGlobalServerConfig;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeSISkeleton;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceConfig;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceFromWSDLImpl;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceFromWSDLIntf;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeTypeMappings;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeWebXml;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodeGenUtil;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAConfigurationRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Transforms the projects available at run time to callable code generation
 * models. This has some critical functionalities. It parse a projects and finds
 * as much information as it can and create a model based on the information
 * with all the code generation parameters and its values. This Model can be
 * passed to the code generation invoker engine to perform code generation
 * operations. Most of the cases, these APIS are called with a project or a base
 * model or both.
 * 
 * @author smathew
 * 
 */
public class ModelTransformer {

	/**
	 * Generates the base code generation model from a given project. It
	 * transforms the given project to a code generation model and this base
	 * model contains all the standard extracted information from a project. For
	 * instance in the case of an interface project the properties file will be
	 * parsed and the base codegen model parameters will be set.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return the base code gen model
	 * @throws Exception the exception
	 */
	public static BaseCodeGenModel generateCodeGenModel(final IProject project,
			final IProgressMonitor monitor) throws Exception {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getCodegenTranformer().transformModel(project, monitor);
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "ServiceFromWSDLIntf". Uses the available values from
	 * the base model and the rest of the values are populated after parsing the
	 * project data and the base model data. Meaning the rest of the data is
	 * inferred from the both of them.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type service from wsdl intf
	 */
	public static GenTypeServiceFromWSDLIntf transformToGenTypeServiceFromWSDLIntf(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeServiceFromWSDLIntf genTypeServiceFromWsdlIntf = new GenTypeServiceFromWSDLIntf();
		genTypeServiceFromWsdlIntf.setAdminName(model.getAdminName());
		genTypeServiceFromWsdlIntf.setClientName(model.getClientName());
		genTypeServiceFromWsdlIntf.setNamespace(model.getNamespace());
		genTypeServiceFromWsdlIntf.setServiceInterface(model
				.getServiceInterface());
		String genFolder = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getSOACodegenProvider()
				.getGenFolderForIntf();
		genTypeServiceFromWsdlIntf.setGenFolder(project.getFolder(genFolder)
				.getLocation().toString());
		genTypeServiceFromWsdlIntf.setProjectRoot(model.getProjectRoot());
		genTypeServiceFromWsdlIntf.setServiceName(model.getServiceName());
		genTypeServiceFromWsdlIntf.setServiceVersion(model.getServiceVersion());
		genTypeServiceFromWsdlIntf.setServiceLayer(model.getServiceLayer());
		genTypeServiceFromWsdlIntf.setSourceDirectory(model
				.getSourceDirectory());
		genTypeServiceFromWsdlIntf.setDestination(model.getDestination());
		genTypeServiceFromWsdlIntf.setOutputDirectory(model
				.getOutputDirectory());
		genTypeServiceFromWsdlIntf.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeServiceFromWsdlIntf.setGenInterfaceClassName(CodeGenUtil
				.getGINFromInterface(model.getServiceInterface()));
		genTypeServiceFromWsdlIntf.setGenInterfacePacakgeName(CodeGenUtil
				.getGIPFromInterface(model.getServiceInterface()));
		genTypeServiceFromWsdlIntf.setGenerateFromWsdl(true);

		genTypeServiceFromWsdlIntf.setMetaDir(project.getLocation().toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_GEN_META_SRC);
		genTypeServiceFromWsdlIntf.setNonXSDFormats(model.getNonXSDFormats());

		return genTypeServiceFromWsdlIntf;
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "ServiceFromWSDLImpl". Uses the available values from
	 * the base model and the rest of the values are populated after parsing the
	 * project data and the base model data. Meaning the rest of the data is
	 * inferred from the both of them.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type service from wsdl impl
	 * @throws WSDLException the wSDL exception
	 */
	public static GenTypeServiceFromWSDLImpl transformToGenTypeServiceFromWSDLImpl(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeServiceFromWSDLImpl genTypeServiceFromWsdlImpl = new GenTypeServiceFromWSDLImpl();
		genTypeServiceFromWsdlImpl.setNamespace(model.getNamespace());
		genTypeServiceFromWsdlImpl.setAdminName(model.getAdminName());
		genTypeServiceFromWsdlImpl.setServiceName(model.getServiceName());
		genTypeServiceFromWsdlImpl.setOriginalWsdlUrl(model
				.getOriginalWsdlUrl());
		genTypeServiceFromWsdlImpl.setServiceInterface(model
				.getServiceInterface());
		genTypeServiceFromWsdlImpl.setProjectRoot(model.getProjectRoot());
		genTypeServiceFromWsdlImpl.setServiceVersion(model.getServiceVersion());
		genTypeServiceFromWsdlImpl.setServiceLayer(model.getServiceLayer());
		genTypeServiceFromWsdlImpl.setSourceDirectory(model
				.getSourceDirectory());
		genTypeServiceFromWsdlImpl.setDestination(model.getDestination());
		genTypeServiceFromWsdlImpl.setOutputDirectory(model
				.getOutputDirectory());
		genTypeServiceFromWsdlImpl.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeServiceFromWsdlImpl.setClientName(model.getAdminName()
				+ SOAProjectConstants.CLIENT_NAME_SUFFIX_TEST);
		genTypeServiceFromWsdlImpl.setMetaDir(project.getLocation().toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);
		String genFolder = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getSOACodegenProvider()
				.getGenFolderForImpl();
		genTypeServiceFromWsdlImpl.setGenFolder(project.getLocation()
				.toString() + WorkspaceUtil.PATH_SEPERATOR + genFolder);
		final ISOAConfigurationRegistry config = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry();
		genTypeServiceFromWsdlImpl.setServiceConfigGroup(config
				.getServiceConfigGroup());
		genTypeServiceFromWsdlImpl.setUseExternalServiceFactory(model
				.useExternalServiceFactory());
		return genTypeServiceFromWsdlImpl;
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "Consumer". Uses the available values from the
	 * consumer model and the rest of the values are populated after parsing the
	 * project data and the consumer model data. Meaning the rest of the data is
	 * inferred from the both of them.
	 *
	 * @param model - The consumer model has the consumer information from a
	 * project already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type consumer
	 * @throws Exception the exception
	 */
	public static GenTypeConsumer transformToGenTypeConsumer(
			ConsumerCodeGenModel model, IProject project) throws Exception {
		GenTypeConsumer genTypeConsumer = new GenTypeConsumer();
		Map<String, Map<String, String>> allServiceData = model
				.getRequiredServices();
		Set<String> dependencies = allServiceData.keySet();
		boolean isImplProject = false;
		if (TurmericServiceUtils.isSOAImplProject(project)) {
			isImplProject = true;
			final ProjectInfo implProjectInfo = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getAssetRegistry().getProjectInfo(project.getName());
			if (implProjectInfo != null
					&& implProjectInfo.getInterfaceProjectName() != null) {
				// we do NOT need to generate base consumer for the
				// corresponding interface project
				dependencies.remove(implProjectInfo.getInterfaceProjectName());
			}
		}

		BuildSystemCodeGen.addEnvMapperToGenTypeConsumer(genTypeConsumer,
				project);
		genTypeConsumer.setProjectRoot(model.getProjectRoot());
		final String clientName = SOAConsumerUtil.getClientName(project);
		genTypeConsumer.setClientName(clientName);

		for (String dependency : dependencies) {
			Map<String, String> serviceData = allServiceData.get(dependency);
			String serviceName = serviceData
					.get(BaseCodeGenModel.PARAM_SERVICE_NAME);
			String serviceInterface = serviceData
					.get(BaseCodeGenModel.PARAM_INTERFACE);
			String serviceVersion = serviceData.get(BaseCodeGenModel.PARAM_SCV);
			String serviceLayer = serviceData
					.get(BaseCodeGenModel.PARAM_SLAYER);
			genTypeConsumer.setNamespace(serviceData
					.get(BaseCodeGenModel.PARAM_NAMESPACE));
			genTypeConsumer.setServiceInterface(serviceInterface);
			genTypeConsumer.setServiceName(serviceName);
			genTypeConsumer.setServiceVersion(serviceVersion);
			genTypeConsumer.setServiceLayer(serviceLayer);
			genTypeConsumer.setDestination(model.getDestination());
			genTypeConsumer.setOutputDirectory(model.getOutputDirectory());
			final IFolder genFolder = SOAServiceUtil.getBaseConsumerFolder(
					project, isImplProject ? SupportedProjectType.IMPL
							: SupportedProjectType.CONSUMER);
			genTypeConsumer.setGenFolder(genFolder.getLocation().toString());
			genTypeConsumer.setSourceDirectory(genTypeConsumer.getGenFolder());
			genTypeConsumer.setServiceLocation(serviceData
					.get(BaseCodeGenModel.PARAM_SL));
		}
		genTypeConsumer.setRequiredServices(((ConsumerCodeGenModel) model)
				.getRequiredServices());
		return genTypeConsumer;
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "SISkeleton". Uses the available values from the base
	 * model and the rest of the values are populated after parsing the project
	 * data and the base model data. Meaning the rest of the data is inferred
	 * from the both of them.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type si skeleton
	 */
	public static GenTypeSISkeleton transformToGenTypeSISkelton(
			BaseCodeGenModel model, IProject project) {
		GenTypeSISkeleton genTypeSkeleton = new GenTypeSISkeleton();
		genTypeSkeleton.setNamespace(model.getNamespace());
		genTypeSkeleton.setServiceInterface(model.getServiceInterface());
		genTypeSkeleton.setServiceName(model.getServiceName());
		genTypeSkeleton.setServiceVersion(model.getServiceVersion());
		genTypeSkeleton.setSourceDirectory(model.getSourceDirectory());
		genTypeSkeleton.setDestination(model.getDestination());
		genTypeSkeleton.setOutputDirectory(model.getOutputDirectory());
		genTypeSkeleton
				.setServiceImplClassName(model.getServiceImplClassName());
		return genTypeSkeleton;
	}

	/**
	 * Wrapper on the
	 * 
	 * @see {@link ModelTransformer}
	 *      {@link #transformToGenTypeSISkelton(BaseCodeGenModel, IProject)}
	 * 
	 *      method, additionally it has the overwrite implementation class flag
	 *      set to true. Setting it to true will over write the implementation
	 *      java class. The reason for having an additional flag is because
	 *      there is a high chance that there might be some additional business
	 *      logic added to the implementation class and for the same reason we
	 *      don't want to overwrite it without a confirmation.
	 * 
	 * @param model
	 * @param project
	 * @return
	 */
	public static GenTypeSISkeleton transformToGenTypeSISkeltonOverwriteImplClass(
			BaseCodeGenModel model, IProject project) {
		final GenTypeSISkeleton genTypeSkeleton = transformToGenTypeSISkelton(
				model, project);
		genTypeSkeleton.setOverwriteImplClass(true);
		return genTypeSkeleton;
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "GlobalServerConfig". Uses the available values from
	 * the base model and the rest of the values are populated after parsing the
	 * project data and the base model data. Meaning the rest of the data is
	 * inferred from the both of them. This is not generated in all the
	 * projects, but is generated on demand.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type global server config
	 * @throws WSDLException the wSDL exception
	 */
	public static GenTypeGlobalServerConfig transformToGenTypeGlobalServerConfig(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeGlobalServerConfig genTypeGlobalServerConfig = new GenTypeGlobalServerConfig();
		genTypeGlobalServerConfig.setNamespace(model.getNamespace());
		genTypeGlobalServerConfig.setServiceInterface(model
				.getServiceInterface());
		genTypeGlobalServerConfig.setServiceName(model.getServiceName());
		genTypeGlobalServerConfig.setServiceVersion(model.getServiceVersion());
		genTypeGlobalServerConfig.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeGlobalServerConfig
				.setSourceDirectory(model.getSourceDirectory());
		genTypeGlobalServerConfig.setDestination(model.getDestination());
		genTypeGlobalServerConfig
				.setOutputDirectory(model.getOutputDirectory());
		genTypeGlobalServerConfig.setMetadataDirectory(project.getLocation()
				.toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);
		return genTypeGlobalServerConfig;
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "GlobalClientConfig". Uses the available values from
	 * the base model and the rest of the values are populated after parsing the
	 * project data and the base model data. Meaning the rest of the data is
	 * inferred from the both of them. This is not generated in all the
	 * projects, but is generated on demand.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type global client config
	 * @throws WSDLException the wSDL exception
	 */
	public static GenTypeGlobalClientConfig transformToGenTypeGlobalClientConfig(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeGlobalClientConfig genTypeGlobalClientConfig = new GenTypeGlobalClientConfig();
		genTypeGlobalClientConfig.setNamespace(model.getNamespace());
		genTypeGlobalClientConfig.setServiceName(model.getServiceName());
		genTypeGlobalClientConfig.setServiceInterface(model
				.getServiceInterface());
		genTypeGlobalClientConfig.setServiceVersion(model.getServiceVersion());
		genTypeGlobalClientConfig.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeGlobalClientConfig
				.setSourceDirectory(model.getSourceDirectory());
		genTypeGlobalClientConfig.setDestination(model.getDestination());
		genTypeGlobalClientConfig
				.setOutputDirectory(model.getOutputDirectory());
		genTypeGlobalClientConfig.setMetadataDirectory(project.getLocation()
				.toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);
		return genTypeGlobalClientConfig;
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "GlobalClientConfig". Uses the available values from
	 * the base model and the rest of the values are populated after parsing the
	 * project data and the base model data. Meaning the rest of the data is
	 * inferred from the both of them. This is not generated in all the
	 * projects, but is generated on demand. Additional SOA Tools bug fixes are
	 * added here.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type global client config consumer
	 * @throws WSDLException the wSDL exception
	 */
	public static GenTypeGlobalClientConfigConsumer transformToGenTypeGlobalClientConfigConsumer(
			ConsumerCodeGenModel model, IProject project) throws WSDLException {
		GenTypeGlobalClientConfigConsumer genTypeGlobalClientConfigConsumer = new GenTypeGlobalClientConfigConsumer();
		// The next two lines are because of a bug in SOA Tools that checks for
		// unnecessary
		// input arguments
		genTypeGlobalClientConfigConsumer.setServiceName("FoobarService");

		genTypeGlobalClientConfigConsumer
				.setServiceInterface("org.ebayopensource.turmeric.foobar.FoobarService");
		genTypeGlobalClientConfigConsumer.setServiceVersion(model
				.getServiceVersion());
		genTypeGlobalClientConfigConsumer.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeGlobalClientConfigConsumer.setSourceDirectory(model
				.getSourceDirectory());
		genTypeGlobalClientConfigConsumer
				.setDestination(model.getDestination());
		genTypeGlobalClientConfigConsumer.setOutputDirectory(model
				.getOutputDirectory());
		genTypeGlobalClientConfigConsumer.setMetadataDirectory(project
				.getLocation().toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);

		genTypeGlobalClientConfigConsumer.setRequiredServices(model
				.getRequiredServices());

		return genTypeGlobalClientConfigConsumer;
	}

	/**
	 * Model for Web Xml generation. Transforms the base model and creates a
	 * specialized model to invoke the codegen gen type - "WebXml". Uses the
	 * available values from the base model and the rest of the values are
	 * populated after parsing the project data and the base model data. Meaning
	 * the rest of the data is inferred from the both of them. This is not
	 * generated in all the projects, but is generated on demand. Additional SOA
	 * Tools bug fixes are added here.
	 *
	 * @param model - The base model has the general information from a project
	 * already parsed and fed into
	 * @param project - the project will be again used to get the rest of the
	 * codegen parameter values.
	 * @return the gen type web xml
	 * @throws WSDLException the wSDL exception
	 */
	public static GenTypeWebXml transformToGenTypeWebXml(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeWebXml genTypeWebXml = new GenTypeWebXml();
		genTypeWebXml.setNamespace(model.getNamespace());
		genTypeWebXml.setServiceInterface(model.getServiceInterface());
		genTypeWebXml.setServiceName(model.getServiceName());
		genTypeWebXml.setServiceVersion(model.getServiceVersion());
		genTypeWebXml.setServiceImplClassName(model.getServiceImplClassName());
		genTypeWebXml.setSourceDirectory(model.getSourceDirectory());
		genTypeWebXml.setDestination(model.getDestination());
		genTypeWebXml.setOutputDirectory(model.getOutputDirectory());
		genTypeWebXml.setMetaDir(project.getLocation().toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_GEN_WEB_CONTENT);
		return genTypeWebXml;
	}

	/**
	 * Model for Service Config xml generation. Transforms the base model and
	 * creates a specialized model to invoke the codegen gen type -
	 * "ServerConfig". Uses the available values from the base model and the
	 * rest of the values are populated after parsing the project data and the
	 * base model data. Meaning the rest of the data is inferred from the both
	 * of them. This is not generated in all the projects, but is generated on
	 * demand. Additional SOA Tools bug fixes are added here.
	 * 
	 * @param model
	 *            - The base model has the general information from a project
	 *            already parsed and fed into
	 * @param project
	 *            - the project will be again used to get the rest of the
	 *            codegen parameter values.
	 * @return
	 * @throws WSDLException
	 */
	public static GenTypeServiceConfig transformToGenTypeServiceConfig(
			BaseCodeGenModel model, IProject project) throws Exception {
		GenTypeServiceConfig genTypeServiceConfig = new GenTypeServiceConfig();
		genTypeServiceConfig.setNamespace(model.getNamespace());
		genTypeServiceConfig.setServiceInterface(model.getServiceInterface());
		genTypeServiceConfig.setServiceName(model.getServiceName());
		genTypeServiceConfig.setServiceVersion(model.getServiceVersion());
		genTypeServiceConfig.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeServiceConfig.setOutputDirectory(project
				.getFolder(SOAProjectConstants.CODEGEN_FOLDER_OUTPUT_DIR)
				.getLocation().toString());
		genTypeServiceConfig.setSourceDirectory(project
				.getFolder(SOAProjectConstants.FOLDER_SRC).getLocation()
				.toString());
		genTypeServiceConfig.setDestination(project.getLocation().toString());
		genTypeServiceConfig.setMetadataDirectory(project.getLocation()
				.toString()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOAProjectConstants.FOLDER_META_SRC);
		final ISOAConfigurationRegistry config = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry();
		genTypeServiceConfig.setServiceConfigGroup(config
				.getServiceConfigGroup());

		// get the useExternalServiceFactory property value and set it to gen
		// model.
		IFile svcImplProperties = SOAImplUtil
				.getServiceImplPropertiesFile(project);
		if (svcImplProperties.isAccessible() == true) {
			String useExternalFac = PropertiesFileUtil.getPropertyValueByKey(
					svcImplProperties.getContents(),
					SOAProjectConstants.PROPS_KEY_USE_EXTERNAL_SERVICE_FACTORY);
			genTypeServiceConfig.setUseExternalServiceFactory(Boolean
					.valueOf(useExternalFac));

		}

		genTypeServiceConfig.setProjectRoot(model.getProjectRoot());

		return genTypeServiceConfig;
	}

	/**
	 * This is frequently used to generate the type mappings file. User can use
	 * this action whenever he thinks that he needs to regenerate the type
	 * mappings due to any change in the wsdl. Transforms the base model and
	 * creates a specialized model to invoke the codegen gen type -
	 * "TypeMappings". Uses the available values from the base model and the
	 * rest of the values are populated after parsing the project data and the
	 * base model data. Meaning the rest of the data is inferred from the both
	 * of them. This is not generated in all the projects, but is generated on
	 * demand. Additional SOA Tools bug fixes are added here.
	 * 
	 * @param model
	 *            - The base model has the general information from a project
	 *            already parsed and fed into
	 * @param project
	 *            - the project will be again used to get the rest of the
	 *            codegen parameter values.
	 * @return
	 * @throws WSDLException
	 */
	public static GenTypeTypeMappings transformToGenTypeTypeMappings(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeTypeMappings genTypeTypeMappings = new GenTypeTypeMappings();
		genTypeTypeMappings.setProjectRoot(project.getLocation().toString());
		genTypeTypeMappings.setNamespace(model.getNamespace());
		genTypeTypeMappings.setServiceInterface(model.getServiceInterface());
		genTypeTypeMappings.setServiceName(model.getServiceName());
		genTypeTypeMappings.setServiceVersion(model.getServiceVersion());
		genTypeTypeMappings.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeTypeMappings.setSourceDirectory(model.getSourceDirectory());
		genTypeTypeMappings.setDestination(model.getDestination());
		genTypeTypeMappings.setOutputDirectory(model.getOutputDirectory());
		return genTypeTypeMappings;
	}
}
