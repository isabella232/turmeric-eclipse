/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAConstants;
import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.exception.ProviderException;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator2;
import org.ebayopensource.turmeric.eclipse.registry.intf.IClientRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.intf.IRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.intf.IValidationStatus;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.registry.models.SubmitAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARootLocator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.osgi.framework.Version;


/**
 * All Action calls comes here
 * 
 * @author smathew
 */
public class ActionUtil {

	private static final SOALogger logger = SOALogger.getLogger();
	public static final String WST_FACET_NATURE_ID = "org.eclipse.wst.common.project.facet.core.nature";
	public static final String WST_MODULECORE_NATURE_ID = "org.eclipse.wst.common.modulecore.ModuleCoreNature";
	private static final String WSI_PREFIX = "WSI";
	private static final String AS_PREFIX = "AS";

	/**
	 * Validates the selected object before executing an action. Most of the
	 * context menu actions use the adaptable interface to find the project
	 * associated with the selection and this API checks if the selected object
	 * is adaptable. If it is not adaptable it returns null rather throwing an
	 * exception, because there are also other ways to find the project. if its
	 * adaptable this API will adapt the selected object to a project and
	 * returns it back
	 * 
	 * @param selectedObject
	 * @param logger
	 * @return
	 */
	public static IProject preValidateAction(final Object selectedObject,
			SOALogger logger) {
		if (!(selectedObject instanceof IAdaptable)) {
			logger.warning(StringUtil.formatString(SOAMessages.NOT_ADAPTABLE,
					selectedObject));
			return null;
		}
		final IProject project = (IProject) ((IAdaptable) selectedObject)
				.getAdapter(IProject.class);
		if (project == null || !project.isAccessible()) {
			logger.warning(StringUtil.formatString(SOAMessages.INVALIDPROJECT,
					project.getName()));
			return null;
		}
		return project;
	}

	/**
	 * Generates the type mappings file for a given project. Gentype used is
	 * TypeMappings. Mainly called from actions, because we have grouped, impl,
	 * intf gentypes and users might still want to regenerate their type
	 * mappings just in case.
	 * 
	 * @param project
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static boolean generateTypeMappings(IProject project,
			IProgressMonitor monitor) throws Exception {

		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		ProgressUtil.progressOneStep(monitor);

		BaseCodeGenModel codeGenModel = BuilderUtil.buildBaseCodeGenModel(
				project, monitor);
		ProgressUtil.progressOneStep(monitor);

		// Gen Type Mappings
		codeGenModel = ModelTransformer.transformToGenTypeTypeMappings(
				codeGenModel, project);
		ProgressUtil.progressOneStep(monitor);

		codegenInvoker.execute(codeGenModel);
		ProgressUtil.progressOneStep(monitor);
		return true;
	}

	/**
	 * Generates the Global Service Config File. This file used to get generated
	 * automatically before and now we have made it an on demand file. Reason is
	 * obvious, Not all projects are supposed to have this file. And for the
	 * same reason we have now given a context menu to generate it on demand.
	 * The gen-type used is GlobalServerConfig.
	 * 
	 * @param project
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static boolean generateGlobalServiceConfig(IProject project,
			IProgressMonitor monitor) throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		ProgressUtil.progressOneStep(monitor);

		BaseCodeGenModel codeGenModel = BuilderUtil.buildBaseCodeGenModel(
				project, monitor);
		ProgressUtil.progressOneStep(monitor);

		codeGenModel = ModelTransformer.transformToGenTypeGlobalServerConfig(
				codeGenModel, project);
		ProgressUtil.progressOneStep(monitor);

		codegenInvoker.execute(codeGenModel);
		ProgressUtil.progressOneStep(monitor);
		return true;
	}

	/**
	 * Generates the Global Client Config File. This file used to get generated
	 * automatically before and now we have made it an on demand file. Reason is
	 * obvious, Not all projects are supposed to have this file. And for the
	 * same reason we have now given a context menu to generate it on demand.
	 * The gen-type used is GlobalClientConfig.
	 * 
	 * @param project
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static boolean generateGlobalClientConfig(IProject project,
			IProgressMonitor monitor) throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		ProgressUtil.progressOneStep(monitor);

		BaseCodeGenModel codeGenModel = BuilderUtil.buildBaseCodeGenModel(
				project, monitor);
		ProgressUtil.progressOneStep(monitor);

		if (codeGenModel instanceof ConsumerCodeGenModel) {
			// This call is required here to set the required services and other
			// fetch steps GenType Consumer
			codeGenModel = ModelTransformer.transformToGenTypeConsumer(
					(ConsumerCodeGenModel) codeGenModel, project);
			ProgressUtil.progressOneStep(monitor);

			codeGenModel = ModelTransformer
					.transformToGenTypeGlobalClientConfigConsumer(
							(ConsumerCodeGenModel) codeGenModel, project);
			ProgressUtil.progressOneStep(monitor);
		} else {
			codeGenModel = ModelTransformer
					.transformToGenTypeGlobalClientConfig(codeGenModel, project);
			ProgressUtil.progressOneStep(monitor);
		}

		codegenInvoker.execute(codeGenModel);
		ProgressUtil.progressOneStep(monitor);
		return true;

	}

	/**
	 * Generates the Skeleton for the given implementation project. Skeleton
	 * means everything except implementation class. Again everything means all
	 * generated files :).
	 * 
	 * @param project
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static boolean generateServiceImplSkeleton(IProject project,
			IProgressMonitor monitor) throws Exception {
		return generateServiceImplSkeleton(project, false, monitor);
	}

	/**
	 * Generates the Skeleton for the given implementation project. Additionally
	 * if the overwriteImplClass is true it will overwrite the implementation
	 * java class as well. The reason why we have a separate variable to govern
	 * it, is because in most cases people will have domain logic in the
	 * implementation class and we do not want to overwrite it withour a
	 * confirmation from the user.
	 * 
	 * @param project
	 * @param overwriteImplClass
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static boolean generateServiceImplSkeleton(IProject project,
			boolean overwriteImplClass, IProgressMonitor monitor)
			throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		ProgressUtil.progressOneStep(monitor);

		BaseCodeGenModel codeGenModel = BuilderUtil.buildBaseCodeGenModel(
				project, monitor);
		ProgressUtil.progressOneStep(monitor);
		if (overwriteImplClass) {
			codeGenModel = ModelTransformer
					.transformToGenTypeSISkeltonOverwriteImplClass(
							codeGenModel, project);
		} else {
			codeGenModel = ModelTransformer.transformToGenTypeSISkelton(
					codeGenModel, project);
		}
		ProgressUtil.progressOneStep(monitor);

		codegenInvoker.execute(codeGenModel);
		ProgressUtil.progressOneStep(monitor);
		return true;
	}

	/**
	 * Generates the web xml. This is web application descriptor.
	 * 
	 * @param project
	 * @param templates
	 * @param templateLoadingClass
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static boolean generateWebXml(IProject project,
			final Map<String, String> templates,
			final Class<?> templateLoadingClass, IProgressMonitor monitor)
			throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		ProgressUtil.progressOneStep(monitor);

		BaseCodeGenModel codeGenModel = BuilderUtil.buildBaseCodeGenModel(
				project, monitor);
		ProgressUtil.progressOneStep(monitor);

		codeGenModel = ModelTransformer.transformToGenTypeWebXml(codeGenModel,
				project);
		ProgressUtil.progressOneStep(monitor);

		codegenInvoker.execute(codeGenModel);
		ProgressUtil.progressOneStep(monitor);

		if (templates != null && templateLoadingClass != null) {
			final Map<String, Object> data = new ConcurrentHashMap<String, Object>();
			data.put(SOAConstants.SVC_NAME, codeGenModel.getServiceName());
			BuildSystemCodeGen
					.generateArtifactsFromTemplates(
							templateLoadingClass,
							templates,
							data,
							project
									.getFolder(SOAProjectConstants.FOLDER_GEN_WEB_CONTENT),
							monitor);
		}
		return true;
	}

	/**
	 * Validates the given project to make sure that it can consume a service.
	 * we have to make sure that the source directory is not the the root
	 * directory, it does not have more than one source directory,
	 * 
	 * @param project
	 * @return
	 */
	public static IStatus validateJavaProjectForConvertingToConsumer(
			final IProject project) {
		final ISOARootLocator locator = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getSOARootLocator();
		if (locator.getRoot().isPrefixOf(project.getLocation()) == false) {
			return EclipseMessageUtils.createErrorStatus(StringUtil
					.formatString(SOAMessages.ERR_LOC_ROOT_INCORRECT, locator
							.getRoot()));
		}

		return Status.OK_STATUS;
	}

	public static IStatus submitNewClientToSOARegistry(IProject project)
			throws Exception {
		final IClientRegistryProvider regProvider = ExtensionPointFactory
				.getSOAClientRegistryProvider();
		if (regProvider == null) {
			return EclipseMessageUtils.createStatus(
					"Could not find a valid client registry provider",
					IStatus.WARNING);
		}

		final ClientAssetModel clientModel = new ClientAssetModel();
		final String clientName = SOAConsumerUtil.getClientName(project);
		clientModel.setClientName(clientName);
		final Properties props = SOAConsumerUtil
				.loadConsumerProperties(project);
		if (props.containsKey(SOAProjectConstants.PROPS_KEY_CONSUMER_ID)) {
			final String consumerID = StringUtils.trim(props
					.getProperty(SOAProjectConstants.PROPS_KEY_CONSUMER_ID));
			clientModel.setConsumerId(consumerID);
		}
		// submit the model
		return regProvider.submitNewClientAsset(clientModel);
	}

	/**
	 * submit new service from menu item. it will submit a service version to
	 * AR. This is the start of submit version to AR
	 * 
	 * @param project
	 * @return
	 */
	public static IStatus submitNewAssetToSOARegistry(IProject project) {
		/*
		 * //validate the service for (IArtifactValidator validator :
		 * ExtensionPointFactory.getArtifactValidators()) { if
		 * (validator.getAllSupportedValidators
		 * ().contains(SOAProjectConstants.WSDL)) { //as of now we only use WSDL
		 * Validator final IFile wsdlFile = SOAServiceUtil.getWsdlFile(project,
		 * serviceName); InputStream is = null; try { is =
		 * wsdlFile.getContents(); byte[] contents = IOUtils.toByteArray(is);
		 * IStatus status = validator.validateArtifact(contents,
		 * wsdlFile.getFileExtension(), monitor); if (status.isOK() == false) {
		 * throw new CoreException(status); } } finally {
		 * IOUtils.closeQuietly(is); } } }
		 */

		IRegistryProvider regProvider;
		try {
			regProvider = ExtensionPointFactory.getSOARegistryProvider();
			if (regProvider == null)
				throw new IllegalArgumentException(
						"Could not find a valid SOA Registry Provider");

			final SubmitAssetModel model = getAssetModel(project);
			// ProgressUtil.progressOneStep(monitor);
			// submit the model
			return regProvider.submitNewAssetForGovernance(model);
		} catch (CoreException e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		} catch (ProviderException e) {
			return handleExceptionFromAR(e);
		} catch (Exception e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		}

	}

	public static SubmitAssetModel getAssetModel(IProject project)
			throws Exception {
		if (project == null
				|| TurmericServiceUtils.isSOAInterfaceProject(project) == false) {
			throw new IllegalArgumentException(
					"Not a valid SOA interface project ->" + project);
		}
		// we trust the service name should be same as the project name
		final String serviceName = project.getName();
		// construct the model
		final SubmitAssetModel model = new SubmitAssetModel();
		String natureId = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getTurmericProjectNatureId(project);
		ISOAProject soaProject = SOAServiceUtil.loadSOAProject(project, natureId);
		SOAIntfMetadata metadata = (SOAIntfMetadata) soaProject.getMetadata();
		if (StringUtils.isNotBlank(metadata.getPublicServiceName())) {
			// post 2.4 services
			model.setServiceName(metadata.getPublicServiceName());
			model.setAdminName(metadata.getServiceName());
		} else {
			// pre 2.4 services, adminName=serviceName
			model.setServiceName(metadata.getServiceName());
			model.setAdminName(metadata.getServiceName());
		}
		model.setNamespacePart(metadata.getServiceNamespacePart());
		model.setInterfaceProjectPath(project.getLocation().toString());
		model.setServiceLayer(metadata.getServiceLayer());
		model.setServiceNamespace(metadata.getTargetNamespace());
		model.setServiceVersion(metadata.getServiceVersion());
		model.setServiceWsdlLocation(SOAServiceUtil.getWsdlFile(project,
				serviceName).getLocation().toString());
		if (StringUtils.isBlank(metadata.getServiceDomainName())) {
			logger
					.warning(
							"Service domain name is missing. Please check service_intf_project.properties of the project and make sure 'domainName' propery is set.",
							"\n\nIf the service is created before installing AR plugin, then please either re-create the service or manually add 'domainName={DomainName}' to service_intf_project.properties and substitute the {DomainName}.");
		}
		model.setServiceDomain(metadata.getServiceDomainName());
		return model;
	}

	public static IStatus updateExistingAssetVersionToSOARegistry(
			IProject project) {
		IRegistryProvider regProvider;
		try {
			regProvider = ExtensionPointFactory.getSOARegistryProvider();

			if (regProvider == null)
				throw new IllegalArgumentException(
						"Could not find a valid SOA Registry Provider");

			final SubmitAssetModel model = getAssetModel(project);
			// ProgressUtil.progressOneStep(monitor);
			// submit the model
			return regProvider.updateExistingVersionForGovernance(model);
		} catch (CoreException e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		} catch (ProviderException e) {
			return handleExceptionFromAR(e);
		} catch (Exception e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		}
	}

	/**
	 * submit a new minor version to AR.
	 * 
	 * @param project
	 * @param newVersion
	 * @return
	 */
	public static IStatus submitNewVersionAssetToSOARegistry(IProject project,
			String newVersion) {
		IRegistryProvider regProvider;
		try {
			regProvider = ExtensionPointFactory.getSOARegistryProvider();
			if (regProvider == null) {
				return EclipseMessageUtils.createStatus(
						SOAMessages.WARNING_AR_NOT_AVAILABLE, IStatus.WARNING);
			}

			final SubmitAssetModel model = getAssetModel(project);
			if (newVersion != null && newVersion.trim().length() > 0) {
				model.setServiceVersion(newVersion);
			}
			// ProgressUtil.progressOneStep(monitor);
			// submit the model
			return regProvider.submitNewVersionForGovernance(model);
		} catch (CoreException e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		} catch (ProviderException e) {
			return handleExceptionFromAR(e);
		} catch (Exception e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		}

	}

	/**
	 * when exception thrown from AR, it is not friendly. This method gets the
	 * root cause of the exception and create an error status for the exception,
	 * using a description
	 * SOAMessages.ERROR_FAIL_TO_SUBMIT_SERVICE_VERSION_TO_AR
	 * 
	 * @param e
	 * @return
	 */
	private static IStatus handleExceptionFromAR(Throwable e) {
		logger.error(e);
		Throwable rootCause = e.getCause();
		if (rootCause == null) {
			return EclipseMessageUtils.createErrorStatus(e);
		} else {
			while (rootCause.getCause() != null) {
				rootCause = rootCause.getCause();
			}
			if (rootCause instanceof LinkageError) {
				logger.error(rootCause);
				return EclipseMessageUtils.createErrorStatus(
						SOAMessages.ERROR_AR_OUT_OF_DATE, null);
			} else {
				return EclipseMessageUtils.createErrorStatus(
						SOAMessages.ERROR_FAIL_TO_SUBMIT_SERVICE_VERSION_TO_AR,
						rootCause);
			}
		}
	}

	/**
	 * Submite a service maintenance version. For Backward Compatibility, using
	 * reflect to call new added method. Show error message if method not found
	 * and notify users to update to latest version.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static IStatus submitServiceMaintenanceVersion(IProject project,
			String newVersion) throws Exception {
		final IRegistryProvider regProvider = ExtensionPointFactory
				.getSOARegistryProvider();
		if (regProvider == null) {
			return EclipseMessageUtils.createStatus(
					SOAMessages.WARNING_AR_NOT_AVAILABLE, IStatus.WARNING);
		}

		// use reflect for backward compatible.
		try {
			Method submitNewMaintenanceVersionMethod = regProvider.getClass()
					.getMethod("submitNewMaintenanceVersion",
							SubmitAssetModel.class);
			SubmitAssetModel model = getAssetModel(project);
			if (newVersion != null && newVersion.trim().length() > 0) {
				model.setServiceVersion(newVersion);
			}
			Object retStatus = submitNewMaintenanceVersionMethod.invoke(
					regProvider, model);
			return (IStatus) retStatus;
		} catch (NoSuchMethodException ex) {
			logger.error(ex);
			return EclipseMessageUtils.createErrorStatus(
					SOAMessages.ERROR_AR_OUT_OF_DATE, null);
		} catch (SecurityException ex) {
			return EclipseMessageUtils.createErrorStatus(ex);
		} catch (InvocationTargetException ex) {
			return handleExceptionFromAR(ex);
		}
	}

	public static void buildService(IProject project, Properties intfProps,
			String serviceName, Version newVersion, IProgressMonitor monitor)
			throws Exception {
		{

			// modifying the service version
			// intfProps.setProperty(SOAProjectConstants.PROP_KEY_SERVICE_VERSION,
			// newVersion.toString());
			// final IFile oldMetadataFile = SOAIntfUtil.getOldMetadataFile(
			// project, serviceName);
			// if (oldMetadataFile.exists() == true) {
			// // pre 2.4
			// SOAIntfUtil.saveMetadataProps(intfProps, project);
			// oldMetadataFile.refreshLocal(IResource.DEPTH_ONE, monitor);
			// } else {
			// final IFile intfProjFile = SOAIntfUtil
			// .getIntfProjectPropFile(project);
			// final Properties props = SOAIntfUtil
			// .loadIntfProjectPropFile(project);
			// props.setProperty(SOAProjectConstants.PROP_KEY_SERVICE_VERSION,
			// newVersion.toString());
			// PropertiesFileUtil.writeToFile(props, intfProjFile,
			// SOAProjectConstants.PROPS_COMMENTS);
			// project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);
			// }
			ISOAProject soaProject = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getSOAProject(project);

			final SOAIntfMetadata intfMetadata = ((SOAIntfProject) soaProject)
					.getMetadata();
			intfMetadata.setServiceVersion(newVersion.toString());

			ISOAProjectConfigurer configurer = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getProjectConfigurer();
			ProgressUtil.progressOneStep(monitor);

			// All metadata version will be updated, SIPP, WSDL, Project.xml and
			// so on.
			configurer.updateProject(soaProject, false, monitor);

			logger.info("Saved new version->", newVersion, " for service->",
					serviceName);
			ProgressUtil.progressOneStep(monitor);
		}

		// {
		// // modifying the WSDL file
		// final String wsdlVersion = newVersion.getMajor()
		// + SOAProjectConstants.DELIMITER_DOT + newVersion.getMinor();
		// SOAIntfUtil.modifyWsdlAppInfoVersion(project, wsdlVersion, monitor);
		// logger.info("Saved new version->", wsdlVersion,
		// " into service WSDL file->", serviceName);
		// ProgressUtil.progressOneStep(monitor);
		// }

	}

	private static IStatus validateUsingAS(IFile wsdlWorkspaceFile,
			URL wsdlFile, IProgressMonitor monitor, List<IStatus> statuses)
			throws Exception {
		final List<IArtifactValidator> validators = ExtensionPointFactory
				.getArtifactValidators();

		final List<IArtifactValidator> wsdlValidators = new ArrayList<IArtifactValidator>();

		final String upperWSDL = SOAProjectConstants.WSDL
				.toUpperCase(Locale.US);

		for (IArtifactValidator validator : validators) {
			if (validator instanceof IArtifactValidator2 ) {
				if (((IArtifactValidator2)validator).isAssertionServiceEnabled() == false) {
					continue;
				}
			}
			if (validator.getAllSupportedValidators().contains(upperWSDL) == true) {
				wsdlValidators.add(validator);
			}
		}

		if (wsdlValidators.isEmpty() == true) {
			// IStatus status = EclipseMessageUtils
			// .createErrorStatus(SOAMessages.WARNING_MISSING_ARTIFACT_VALIDATOR);
			// statuses.add(status);
			logger.warning(SOAMessages.WARNING_MISSING_ARTIFACT_VALIDATOR);
			return Status.CANCEL_STATUS;
		}

		
		Collections.sort(wsdlValidators, new Comparator<IArtifactValidator>() {

			public int compare(IArtifactValidator v1, IArtifactValidator v2) {
				if (v1 instanceof IArtifactValidator2
						&& v2 instanceof IArtifactValidator2) {
					return VersionUtil.compare(((IArtifactValidator2) v1)
							.getVersion(), ((IArtifactValidator2) v2)
							.getVersion());
				} else if (v1 instanceof IArtifactValidator2) {
					return -1;
				} else if (v2 instanceof IArtifactValidator2) {
					return 1;
				}
				return 0;
			}
		});
		ProgressUtil.progressOneStep(monitor);

		
		// sorted in ascending order, so the last one is the most matched one.
		IArtifactValidator validator = wsdlValidators.get(0);
		logger.info("Validating WSDL with assertion service ->", wsdlFile
				.toString()
				+ " using validator->" + validator.getClass().getName());
		InputStream is = null;
		try {
			is = wsdlFile.openStream();
			byte[] contents = IOUtils.toByteArray(is);
			logger.info("Validating WSDL with assertion service->", wsdlFile
					.toString());
			
			IStatus status = validator.validateArtifact(contents, upperWSDL,
					monitor);
			ProgressUtil.progressOneStep(monitor);
			logger.info("Validating WSDL result->", status);
			if (status.isOK() == false) {
				if (status.isMultiStatus() == true) {
					// Add markers to wsdl file
					for (IStatus wsdlValidationStatus : status.getChildren()) {
						int lineNumber = -1;
						if (wsdlValidationStatus instanceof IValidationStatus) {
							lineNumber = ((IValidationStatus) wsdlValidationStatus)
									.getLineNumber();
						}
						if (wsdlWorkspaceFile != null) {
							MarkerUtil
									.createWSDLMarker(wsdlWorkspaceFile,
											AS_PREFIX, wsdlValidationStatus,
											lineNumber);
							logger.warning(AS_PREFIX + ":"
									+ wsdlValidationStatus.getMessage(),
									wsdlValidationStatus.getException());
						}
						statuses.add(wsdlValidationStatus);
					}
				} else {
					// Add markers to wsdl file
					if (wsdlWorkspaceFile != null) {
						MarkerUtil.createWSDLMarker(wsdlWorkspaceFile,
								AS_PREFIX, status, -1);
						logger.warning(AS_PREFIX + ":" + status.getMessage(),
								status.getException());
					}
					statuses.add(status);
				}
			}
			if (status.isOK() == true) {
				logger.info("Validating WSDL with AS validator finsihed."
						+ " No AS violations found.");
			}
			return status;
		} finally {
			IOUtils.closeQuietly(is);
			ProgressUtil.progressOneStep(monitor);
		}
	}

	@SuppressWarnings("restriction")
	public static void validateUsingWTP(IFile wsdlWorkspaceFile, URL wsdlFile,
			IProgressMonitor monitor, List<IStatus> statuses)
			throws ValidationInterruptedException, CoreException,
			MalformedURLException, IOException {

		// TODO This WSDL validation is different with the one used in in
		// codeGen.
		logger
				.info(
						"Validating WSDL with WTP validator->",
						wsdlFile.toExternalForm()
								+ " using validator->"
								+ org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator
										.getInstance().getClass().getName());
		IValidationMessage[] validationMessages = org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator
				.getInstance().validate(wsdlFile.toExternalForm())
				.getValidationMessages();

		if (validationMessages != null && validationMessages.length > 0) {
			for (IValidationMessage validationMessage : validationMessages) {
				IStatus status = null;
				if (validationMessage.getSeverity() == IValidationMessage.SEV_ERROR) {
					status = EclipseMessageUtils.createStatus(validationMessage
							.getMessage(), IStatus.ERROR);
				} else {
					status = EclipseMessageUtils.createStatus(validationMessage
							.getMessage(), IStatus.WARNING);
				}
				statuses.add(status);
				if (wsdlWorkspaceFile != null) {
					MarkerUtil.createWSDLMarker(wsdlWorkspaceFile, WSI_PREFIX,
							status, validationMessage.getLine());
					logger.warning(WSI_PREFIX + ":" + status.getMessage(),
							status.getException());
				}
			}
		} else {
			logger.info("Validating WSDL with WTP validator finsihed."
					+ " No WTP violations found.");
		}
	}

	public static IStatus validateServiceWSDL(IFile wsdlFile, URL wsdlFileURL,
			boolean needASValidation, IProgressMonitor monitor)
			throws Exception {
		final List<IStatus> statuses = new ArrayList<IStatus>();

		// Clean all markers before validation
		if (wsdlFile != null) {
			MarkerUtil.cleanWSDLMarkers(wsdlFile);
		}

		validateUsingWTP(wsdlFile, wsdlFileURL, monitor, statuses);

		if (needASValidation == true) {
			try {
				validateUsingAS(wsdlFile, wsdlFileURL, monitor, statuses);
			} catch (Exception e) {
				logger.error(e);
				IStatus status = EclipseMessageUtils.createStatus(
						"Exception occures while Running Assertion Service Validation: "
								+ e.getMessage(), IStatus.WARNING);
				statuses.add(status);
				UIUtil.showErrorDialog(
						SOAMessages.ERROR_SERVICE_RS_SERVICE_FAILED_TITLE,
						SOAMessages.ERROR_SERVICE_RS_SERVICE_FAILED, e);

			}
		} else {
			logger.info("AS validation skipped.");
		}

		if (statuses.isEmpty() == false) {
			logger.warning(SOAMessages.ERROR_SERVICE_WSDL_VALIDATION_FAILED);
			return EclipseMessageUtils.createErrorMultiStatus(statuses,
					SOAMessages.ERROR_SERVICE_WSDL_VALIDATION_FAILED);
		}

		return Status.OK_STATUS;
	}

	public static IStatus submitVersionToAssetRepository(Object newVersion,
			Object oldVersion, String svcIntfName, IProgressMonitor monitor)
			throws Exception {
		Version newVer = VersionUtil.getVersion(newVersion);
		Version oldVer = VersionUtil.getVersion(oldVersion);

		monitor.setTaskName("Synchronize service version with "
				+ "Asset Repository for project->" + svcIntfName
				+ ", from version [" + oldVer + "], to version [" + newVer
				+ "].");

		// no version format check here because it is checked when version
		// changed.
		// here we use not equal but not bigger than to decide if a version is
		// changed.
		// Only show when service version is changed this time.
		boolean miniorChanged = newVer.getMinor() != oldVer.getMinor();
		boolean mantanChanged = newVer.getMicro() != oldVer.getMicro();
		final boolean syncVersion;

		if (miniorChanged || mantanChanged) {
			syncVersion = UIUtil
					.openChoiceDialog(
							"Synchronize Service Version",
							"Service version has been updated to ["
									+ newVer
									+ "] in local metadata. "
									+ "Would you like to synchronize service version change to the Asset Repository?",
							MessageDialog.QUESTION_WITH_CANCEL);

		} else {
			logger.info("Service Version not changed:" + newVer
					+ ". Exist submitVersionToAssetRepository");
			return Status.CANCEL_STATUS;
		}

		if (syncVersion == false) {
			return Status.CANCEL_STATUS;
		}

		try {
			IStatus result = null;
			IProject intfProj = WorkspaceUtil.getProject(svcIntfName);
			if (miniorChanged == true) {
				result = ActionUtil.submitNewVersionAssetToSOARegistry(
						intfProj, newVer.toString());
			} else if (mantanChanged == true) {
				result = ActionUtil.submitServiceMaintenanceVersion(intfProj,
						newVer.toString());

			} else {
				logger.info("Service Version not changed:" + newVer
						+ ". Exist submitVersionToAssitionRepository");
			}
			if (result != null) {
				if (result.isOK() == true) {
					GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().trackingUsage(
									new TrackingEvent(ActionUtil.class
											.getName(),
											TrackingEvent.TRACKING_ACTION));
				} else {
					UIUtil
							.showErrorDialog(
									(Shell) null,
									"Service Version Synchronize Failed",
									"Failed to synchronize service version with Asset Repository.",
									result);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new SOAResourceModifyFailedException(
					"Failed to synchronize service version for project->"
							+ svcIntfName + ", from version [" + oldVer
							+ "] to version [" + newVer + "].", e);
		} finally {
			monitor.done();
		}

		return null;
	}

	public static IStatus cleanProject(IProject project,
			IProgressMonitor monitor) throws CoreException {

		try {
			final Collection<IFolder> resources = new HashSet<IFolder>();
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_META_SRC));
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_TEST));
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_SRC_CLIENT));
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_SRC_SERVICE));
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_WEB_CONTENT));

			for (final IResource resource : resources) {
				if (resource.isAccessible()) {
					FileUtils.cleanDirectory(resource.getLocation().toFile());
					ProgressUtil.progressOneStep(monitor);
				}
			}
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			ProgressUtil.progressOneStep(monitor);
			project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);
		} catch (Exception e) {
			logger.error(e);
			throw new SOAActionExecutionFailedException(e);
		} finally {
			monitor.done();
			WorkspaceUtil.refresh(monitor, project);
		}
		return Status.OK_STATUS;

	}

	/**
	 * Wrapper to execute the job and schedule and show a message in the UI if
	 * required
	 * 
	 */
	public static interface ActionJob {
		/**
		 * Gets the target platform
		 * 
		 * @return
		 */
		public String getTargetPlatform();

		/**
		 * Schedules the job for execution
		 */
		public void schedule();

		/**
		 * Setting the sub job for the parent job
		 * 
		 * @param job
		 */
		public void setSubJob(Job job);

		/**
		 * Shows the message box in the UI
		 * 
		 * @param title
		 * @param message
		 */
		public void showMessage(String title, String message);
	}
}
