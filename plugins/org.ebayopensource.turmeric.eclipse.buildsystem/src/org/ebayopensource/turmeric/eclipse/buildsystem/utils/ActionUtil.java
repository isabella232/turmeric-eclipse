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
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAConstants;
import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator2;
import org.ebayopensource.turmeric.eclipse.registry.intf.IValidationStatus;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARootLocator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
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
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;

/**
 * All Action calls comes here
 * 
 * @author smathew
 */
public class ActionUtil {

	private static final SOALogger logger = SOALogger.getLogger();
	
	/** The Constant WST_FACET_NATURE_ID. */
	public static final String WST_FACET_NATURE_ID = "org.eclipse.wst.common.project.facet.core.nature";
	
	/** The Constant WST_MODULECORE_NATURE_ID. */
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
	 * @param selectedObject the selected object
	 * @param logger the logger
	 * @return the i project
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
	 * @param project the project
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
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
	 * @param project the project
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
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
	 * @param project the project
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
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
	 * @param project the project
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
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
	 * @param project the project
	 * @param overwriteImplClass the overwrite impl class
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
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
	 * @param project the project
	 * @param templates the templates
	 * @param templateLoadingClass the template loading class
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
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
	 * @param project the project
	 * @return the i status
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

		IFile projectFile = project.getFile(SOAProjectConstants.FILE_PROJECT);
		if (projectFile.exists() == true && projectFile.isReadOnly() == true) {
			return EclipseMessageUtils.createErrorStatus(StringUtil
					.formatString(SOAMessages.JAVA_CLASSPATH_READONLY,
							projectFile.getLocationURI().toString()));
		}

		IFile classpathFile = project
				.getFile(SOAProjectConstants.FILE_CLASSPATH);
		if (classpathFile.exists() == true
				&& classpathFile.isReadOnly() == true) {
			return EclipseMessageUtils.createErrorStatus(StringUtil
					.formatString(SOAMessages.JAVA_CLASSPATH_READONLY,
							classpathFile.getLocationURI().toString()));
		}

		return Status.OK_STATUS;
	}

	private static IStatus validateUsingAS(IFile wsdlWorkspaceFile,
			URL wsdlFile, List<IStatus> statuses, boolean needDowngrade,
			IProgressMonitor monitor) throws Exception {
		final List<IArtifactValidator> validators = ExtensionPointFactory
				.getArtifactValidators();

		final List<IArtifactValidator> wsdlValidators = new ArrayList<IArtifactValidator>();

		final String upperWSDL = SOAProjectConstants.WSDL
				.toUpperCase(Locale.US);

		for (IArtifactValidator validator : validators) {
			if (validator instanceof IArtifactValidator2) {
				if (((IArtifactValidator2) validator)
						.isAssertionServiceEnabled() == false) {
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

			@Override
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
						// if need downgrade, only care about error status and
						// change it to warning.
						if (needDowngrade == true) {
							if (wsdlValidationStatus.getSeverity() == IStatus.ERROR) {
								wsdlValidationStatus = EclipseMessageUtils
										.createStatus(wsdlValidationStatus
												.getMessage(), IStatus.WARNING);
							} else {
								continue;
							}
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
					IStatus statusDump = status;
					if (needDowngrade == true) {
						if (statusDump.getSeverity() == IStatus.ERROR) {
							statusDump = EclipseMessageUtils.createStatus(
									statusDump.getMessage(), IStatus.WARNING);
						} else {
							statusDump = null;
						}
					}
					if (statusDump != null) {
						// Add markers to wsdl file
						if (wsdlWorkspaceFile != null) {
							MarkerUtil.createWSDLMarker(wsdlWorkspaceFile,
									AS_PREFIX, statusDump, -1);
							logger.warning(AS_PREFIX + ":"
									+ statusDump.getMessage(), statusDump
									.getException());
						}
						statuses.add(statusDump);
					}
				}
			} else {
				logger.info("Validating WSDL with AS validator finsihed."
						+ " No AS violations found.");
			}
			return status;
		} finally {
			IOUtils.closeQuietly(is);
			ProgressUtil.progressOneStep(monitor);
		}
	}

	/**
	 * Validate using wtp.
	 *
	 * @param wsdlWorkspaceFile the wsdl workspace file
	 * @param wsdlFile the wsdl file
	 * @param statuses the statuses
	 * @param needDowngrade the need downgrade
	 * @param monitor the monitor
	 * @throws ValidationInterruptedException the validation interrupted exception
	 * @throws CoreException the core exception
	 * @throws MalformedURLException the malformed url exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("restriction")
	public static void validateUsingWTP(IFile wsdlWorkspaceFile, URL wsdlFile,
			List<IStatus> statuses, boolean needDowngrade,
			IProgressMonitor monitor) throws ValidationInterruptedException,
			CoreException, MalformedURLException, IOException {

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
				if (needDowngrade == true) {
					// downgrade error to warning, ignore warning.
					if (validationMessage.getSeverity() == IValidationMessage.SEV_ERROR) {
						status = EclipseMessageUtils
								.createStatus(validationMessage.getMessage(),
										IStatus.WARNING);
					} else {
						continue;
					}
				} else {
					if (validationMessage.getSeverity() == IValidationMessage.SEV_ERROR) {
						status = EclipseMessageUtils.createStatus(
								validationMessage.getMessage(), IStatus.ERROR);
					} else if (validationMessage.getSeverity() == IValidationMessage.SEV_WARNING) {
						status = EclipseMessageUtils
								.createStatus(validationMessage.getMessage(),
										IStatus.WARNING);
					} else {
						continue;
					}
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

	/**
	 * Validate service wsdl.
	 *
	 * @param wsdlFile the wsdl file
	 * @param wsdlFileURL the wsdl file url
	 * @param needASValidation the need as validation
	 * @param needDowngrade the need downgrade
	 * @param monitor the monitor
	 * @return the i status
	 * @throws Exception the exception
	 */
	public static IStatus validateServiceWSDL(IFile wsdlFile, URL wsdlFileURL,
			boolean needASValidation, boolean needDowngrade,
			IProgressMonitor monitor) throws Exception {
		final List<IStatus> statuses = new ArrayList<IStatus>();

		// Clean all markers before validation
		if (wsdlFile != null) {
			MarkerUtil.cleanWSDLMarkers(wsdlFile);
		}

		validateUsingWTP(wsdlFile, wsdlFileURL, statuses, needDowngrade,
				monitor);

		if (needASValidation == true) {
			try {
				validateUsingAS(wsdlFile, wsdlFileURL, statuses, needDowngrade,
						monitor);
			} catch (Exception e) {
				logger.error(e);
				IStatus status = EclipseMessageUtils.createStatus(
						"Exception occures while Running Assertion Service Validation: "
								+ e.getMessage(), IStatus.WARNING);
				statuses.add(status);
				// UIUtil.showErrorDialog(
				// SOAMessages.ERROR_SERVICE_RS_SERVICE_FAILED_TITLE,
				// SOAMessages.ERROR_SERVICE_RS_SERVICE_FAILED, e);
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

	/**
	 * Clean project.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return the i status
	 * @throws CoreException the core exception
	 */
	public static IStatus cleanProject(IProject project,
			IProgressMonitor monitor) throws CoreException {

		try {
			final Collection<IFolder> resources = new HashSet<IFolder>();
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_META_SRC));
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_TEST));
			IFolder genClient = project
					.getFolder(SOAProjectConstants.FOLDER_GEN_SRC_CLIENT);
			IFolder genService = project
					.getFolder(SOAProjectConstants.FOLDER_GEN_SRC_SERVICE);
			if (genClient.isAccessible() == false
					&& genService.isAccessible() == false) {
				resources.add(project
						.getFolder(SOAProjectConstants.FOLDER_GEN_SRC));

			}
			resources.add(genClient);
			resources.add(genService);
			resources.add(project
					.getFolder(SOAProjectConstants.FOLDER_GEN_WEB_CONTENT));
			logger.info("Start to clean project " + project.getName() + "...");
			for (final IResource resource : resources) {
				if (resource.isAccessible()) {
					try {
						logger.info("Cleaning directory  "
								+ resource.getLocation() + "...");
						FileUtils.cleanDirectory(resource.getLocation()
								.toFile());
						ProgressUtil.progressOneStep(monitor);
					} catch (Exception e) {
						logger.error(e);
						throw new SOAActionExecutionFailedException(e);
					}
				}
			}
			logger.info("Clean project " + project.getName() + " finished.");
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);
			ProgressUtil.progressOneStep(monitor);
		} finally {
			monitor.done();
			WorkspaceUtil.refresh(monitor, project);
		}
		return Status.OK_STATUS;

	}
	
	public static IStatus generateConfigs(IProject consumerProject, IProgressMonitor monitor) throws Exception {
		final List<AssetInfo> services = new ArrayList<AssetInfo>();
		for (AssetInfo asset : GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getAssetRegistry().getDependencies(consumerProject.getName())) {
			if (IAssetInfo.TYPE_SERVICE_LIBRARY.equals(asset.getType())) {
				services.add(asset);
			}
		}
		logger.info("Generating configs for consumer project-> ", consumerProject.getName(), 
				" with services ->", services );
		final String clientName = SOAConsumerUtil.getClientName(consumerProject);
		BuildSystemCodeGen
		.generateArtifactsForAddedService(
				consumerProject,
				clientName,
				ListUtil
						.arrayList(SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT),
						services, monitor);
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
