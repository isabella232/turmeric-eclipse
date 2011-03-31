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
package org.ebayopensource.turmeric.eclipse.services.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemCodeGen;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectPropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAConsumeNewServiceFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOACreateNewEnvironmentFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOARemoveConsumedServiceFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOARemoveEnvironmentFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientConfig;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAClientConfigUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.ModifyConsumerIDResult;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ConsumeNewServiceWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.osgi.framework.Version;

/**
 * @author yayu
 * 
 */
public class ConsumeNewServiceWizard extends SOABaseWizard {
	private ConsumeNewServiceWizardPage consumeNewServicePage;
	private IProject consumerProject;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 */
	public ConsumeNewServiceWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#getContentPages()
	 */
	@Override
	public IWizardPage[] getContentPages() {
		try {
			if (getSelection().isEmpty() == false
					&& getSelection().getFirstElement() instanceof IAdaptable) {
				final Object obj = ((IAdaptable) getSelection()
						.getFirstElement()).getAdapter(IProject.class);
				if (obj instanceof IProject) {
					consumerProject = (IProject) obj;
					consumeNewServicePage = new ConsumeNewServiceWizardPage(
							consumerProject);
				}
			}
		} catch (Exception exception) {
			logger.error(exception);
			throw new RuntimeException(exception);
		}

		return new IWizardPage[] { consumeNewServicePage };
	}

	@Override
	public IStatus preValidate() {
		// bypass the pre validation
		return Status.OK_STATUS;
	}

	@Override
	public boolean performFinish() {
		try {
			if (SOALogger.DEBUG)
				logger.entering();
			if (consumeNewServicePage != null) {
				final String clientName = consumeNewServicePage.getClientName();
				final String consumerId = consumeNewServicePage.getConsumerId();
				final Set<AssetInfo> fullServiceList = consumeNewServicePage
						.getServices();
				final Set<AssetInfo> addedServices = consumeNewServicePage
						.getAddedServices();
				final Set<AssetInfo> removedServices = consumeNewServicePage
						.getRemovedServices();
				final Map<String, EnvironmentItem> addedEnvironments = consumeNewServicePage
						.getAddedEnvironments();
				final Set<String> removedEnvironments = consumeNewServicePage
						.getRemovedEnvironments();
				final Set<String> environments = consumeNewServicePage
						.getEnvironments();
				// checking if the intf project is built
				final Collection<String> addedServiceList = new ArrayList<String>(
						addedServices.size());
				for (AssetInfo asset : addedServices) {
					addedServiceList.add(asset.getName());
				}
				if (ProjectUtils.isProjectGoodForConsumption(addedServiceList
						.toArray(new String[0])) == false) {
					return false;
				}

				final boolean allowModifyConsumerID = consumeNewServicePage
						.isConsumerIdEditable();
				final String projectName = consumerProject.getName();

				WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						long startTime = System.currentTimeMillis();
						final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
						monitor.beginTask(
								"Modifying service consumption configurations for project->"
										+ consumerProject.getName(), totalWork);

						ProgressUtil.progressOneStep(monitor);

						try {
							if (TurmericServiceUtils
									.isSOAConsumerProject(consumerProject) == false) {
								// does not have consumer nature yet, add it now
								BuildSystemUtil
										.addSOAConsumerSupport(
												consumerProject,
												GlobalRepositorySystem
														.instanceOf()
														.getActiveRepositorySystem()
														.getProjectNatureId(
																SupportedProjectType.CONSUMER),
												monitor);
								logger
										.warning("Added SOA consumer project nature to the impl project->"
												+ projectName);
								SOAResourceCreator
										.createConsumerPropertiesFileForImplProjects(
												consumerProject, clientName,
												consumerId, monitor);
								logger
										.warning("Created service_consumer_project.properties to the impl project->"
												+ projectName);
							}
							ProgressUtil.progressOneStep(monitor);

							// add environments
							if (addedEnvironments.isEmpty() == false) {
								logger.info("Creating new environments->",
										addedEnvironments);
								List<String> clonedEnvs = new ArrayList<String>();
								for (String newEnvName : addedEnvironments
										.keySet()) {
									final EnvironmentItem cloneEnv = addedEnvironments
											.get(newEnvName);
									if (cloneEnv != null) {
										logger
												.info(
														"Cloning existing environment ->",
														cloneEnv.getName(),
														" to new environment->",
														newEnvName);
										SOAConsumerUtil.cloneEnvironment(
												consumerProject, cloneEnv
														.getName(), newEnvName,
												monitor);
										clonedEnvs.add(newEnvName);
									}
								}

								addedEnvironments.keySet()
										.removeAll(clonedEnvs);

							}
							ProgressUtil.progressOneStep(monitor);
						} catch (Exception e) {
							logger.error(e);
							throw new SOACreateNewEnvironmentFailedException(
									"Failed to create new environment for project->"
											+ consumerProject.getName(), e);
						}

						// remove environments
						try {
							if (removedEnvironments.isEmpty() == false) {
								logger.info("Removing environments->",
										removedEnvironments);
								SOAConsumerUtil.removeEnvironments(
										consumerProject, monitor,
										removedEnvironments
												.toArray(new String[0]));
								ProgressUtil.progressOneStep(monitor);
							}
						} catch (Exception e) {
							logger.error(e);
							throw new SOARemoveEnvironmentFailedException(
									"Failed to remove existing environment from project->"
											+ consumerProject.getName(), e);
						}

						// services configuration
						ISOAProjectConfigurer configurer = GlobalRepositorySystem
								.instanceOf().getActiveRepositorySystem()
								.getProjectConfigurer();

						try {
							// removing existing services
							if (removedServices.isEmpty() == false) {
								logger.info("Removing services->",
										removedServices);
								configurer.removeDependencies(projectName,
										ListUtil.arrayList(removedServices),
										monitor);

								Collection<String> serviceNames = new ArrayList<String>();
								for (AssetInfo removedService : removedServices) {
									serviceNames.add(removedService.getName());
								}
								SOAConsumerUtil.removeClientConfigFiles(
										consumerProject, monitor, serviceNames
												.toArray(new String[0]));
								ProgressUtil.progressOneStep(monitor);
							}
						} catch (Exception e) {
							logger.error(e);
							throw new SOARemoveConsumedServiceFailedException(
									"Failed to remove service from project->"
											+ consumerProject.getName(), e);
						}

						try {
							// manage the not_generate_base_consumer property
							final Collection<String> removedServiceList = new ArrayList<String>(
									removedServices.size());
							for (AssetInfo asset : removedServices) {
								removedServiceList.add(asset.getName());
							}
							final Collection<String> addedServiceNames = new ArrayList<String>(
									addedServiceList.size());
							final ISOAAssetRegistry assetRegistry = GlobalRepositorySystem
									.instanceOf().getActiveRepositorySystem()
									.getAssetRegistry();
							for (AssetInfo asset : addedServices) {
								final String assetLocation = assetRegistry
										.getAssetLocation(asset);
								final Version version = SOAIntfUtil
										.getServiceMetadataVersion(asset
												.getName(), assetLocation);
								if (version
										.compareTo(SOAProjectConstants.DEFAULT_PROPERTY_VERSION) >= 0) {
									// the project is post 2.4
									addedServiceNames.add(asset.getName());
								}
							}
							SOAConsumerUtil.modifyNotGenerateBaseConsumers(
									consumerProject, addedServiceNames,
									removedServiceList, monitor);
						} catch (Exception e) {
							logger.error(e);
							throw new SOAResourceModifyFailedException(
									"Failed to modify the not_generate_base_consumer property->"
											+ consumerProject.getName(), e);
						}

						// add services
						try {
							if (addedServices.isEmpty() == false) {
								logger.info("Consuming new services->",
										addedServices);
								configurer.addDependencies(projectName,
										ListUtil.arrayList(addedServices),
										monitor);
								ProgressUtil.progressOneStep(monitor);
								BuildSystemUtil
										.updateSOAClasspathContainer(consumerProject);
								ProgressUtil.progressOneStep(monitor);
							}
						} catch (Exception e) {
							logger.error(e);
							throw new SOAConsumeNewServiceFailedException(
									"Failed to add new service to project->"
											+ consumerProject.getName(), e);
						}

						try {
							if (fullServiceList.isEmpty() == true
									&& TurmericServiceUtils
											.isSOAImplProject(consumerProject)) {
								// no services remained
								logger
										.info(
												"All consumed services by the underlying implementation project have been removed->",
												consumerProject);
								ProjectUtil
										.removeNatures(
												consumerProject,
												monitor,
												GlobalRepositorySystem
														.instanceOf()
														.getActiveRepositorySystem()
														.getProjectNatureId(
																SupportedProjectType.CONSUMER));
								IFile file = SOAConsumerUtil
										.getConsumerPropertiesFile(consumerProject);
								if (file.exists() == true) {
									FileUtils.forceDelete(file.getLocation()
											.toFile());
									logger
											.info(
													"Removed service_consumer_project.properties file->",
													file.getLocation());
								}
								final IFolder folder = consumerProject
										.getFolder(SOAConsumerProject.FOLDER_META_SRC_ClIENT);
								if (folder.exists() == true) {
									FileUtils.forceDelete(folder.getLocation()
											.toFile());
									logger.info(
											"Removed client config folder->",
											folder.getLocation());
								}
							}
						} catch (Exception e) {
							logger.error(e);
							throw new SOAResourceModifyFailedException(
									"Failed to remove consumer natures from the underlying implementation projects->"
											+ consumerProject.getName(), e);
						}

						WorkspaceUtil.refresh(consumerProject);
						final TrackingEvent event = new TrackingEvent(
								"SetupServiceConsumptions",
								new Date(startTime), System.currentTimeMillis()
										- startTime);
						GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().trackingUsage(
										event);
						monitor.done();
					}
				};
				if (addedServices.isEmpty() == false
						|| removedServices.isEmpty() == false
						|| addedEnvironments.isEmpty() == false
						|| removedEnvironments.isEmpty() == false) {
					getContainer().run(false, true, operation);

					operation = new WorkspaceModifyOperation() {

						@Override
						protected void execute(IProgressMonitor monitor)
								throws CoreException,
								InvocationTargetException, InterruptedException {
							// add services
							try {
								if (addedServices.isEmpty() == false) {
									logger
											.info(
													"Generating artifacts for new services->",
													addedServices);
									ProgressUtil.progressOneStep(monitor);
									if (addedServices.isEmpty() == false) {
										BuildSystemCodeGen
												.generateArtifactsForAddedService(
														consumerProject,
														clientName,
														ListUtil
																.arrayList(environments),
														addedServices, monitor);
									}
								}
							} catch (Exception e) {
								logger.error(e);
								throw new SOAConsumeNewServiceFailedException(
										"Failed to generate artifacts for new service to project->"
												+ consumerProject.getName(), e);
							}

							if (removedServices.isEmpty() == false
									&& addedServices.isEmpty() == true) {
								// need to udpate the classpath container
								BuildSystemUtil
										.updateSOAClasspathContainer(consumerProject);
							}

							try {
								// the remaining new environments that have to
								// be added
								// after new services being added
								if (addedEnvironments.isEmpty() == false) {
									logger.info(
											"Generating new environments->",
											addedEnvironments);
									BuildSystemCodeGen
											.generateArtifactsForAddedService(
													consumerProject,
													clientName,
													ListUtil
															.arrayList(addedEnvironments
																	.keySet()),
													fullServiceList, monitor);
								}

							} catch (Exception e) {
								logger.error(e);
								throw new SOACreateNewEnvironmentFailedException(
										"Failed to create new environment for project->"
												+ consumerProject.getName(), e);
							}

						}

					};
					getContainer().run(false, true, operation);
				}

				if (allowModifyConsumerID == true
						&& StringUtils.isNotBlank(consumerId)) {
					final ModifyConsumerIDResult result = SOAConsumerUtil
							.updateConsumerId(consumerId, consumerProject);
					if (result != null) {
						// the consumerID has been updated
						if (result.getOldClientConfigs().isEmpty() == false) {
							// some CC.xml contains <invocation-use-case>
							StringBuffer msg = new StringBuffer();
							msg
									.append("The following ClientConfig.xml files contain the deprecated element <invocation-use-case>, ");
							msg
									.append("which will be removed. Backups of the ClientConfig.xml files will be created with \".bak\" suffix.\n");

							for (SOAClientConfig config : result
									.getOldClientConfigs()) {
								msg.append("\n");
								msg.append(config.getFile().getLocation());
							}
							MessageDialog.openWarning(getShell(), "Warnings",
									msg.toString());
						}

						final WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

							@Override
							protected void execute(IProgressMonitor monitor)
									throws CoreException,
									InvocationTargetException,
									InterruptedException {
								logger
										.warning(
												"The consumer ID has been changed, re-generate the service_consumer_project.properties and "
														+ "modify all ClientConfig.xml files for project->",
												consumerProject);
								try {
									ProjectPropertiesFileUtil
											.createPropsFileForImplProjects(
													consumerProject,
													clientName,
													consumerId,
													StringUtils
															.trim(result
																	.getProperties()
																	.getProperty(
																			SOAProjectConstants.PROPS_IMPL_BASE_CONSUMER_SRC_DIR)),
													monitor);
									
									for (SOAClientConfig clientConfig : result
											.getClientConfigs()) {
										String protocalProcessorClassName = GlobalRepositorySystem
												.instanceOf()
												.getActiveRepositorySystem()
												.getActiveOrganizationProvider()
												.getSOAPProtocolProcessorClassName();
										SOAClientConfigUtil
												.save(clientConfig, result
														.getOldClientConfigs()
														.contains(clientConfig), protocalProcessorClassName);
									}

								} catch (Exception e) {
									logger.error(e);
									throw new SOAResourceModifyFailedException(
											"Failed to modify consumerID for the underlying consumer projects->"
													+ consumerProject.getName(),
											e);
								} finally {
									consumerProject.refreshLocal(
											IProject.DEPTH_INFINITE, monitor);
								}
							}
						};
						getContainer().run(false, true, op);
						changePerspective();
					}
				}

				if (removedServices.isEmpty() == false) {
					MessageDialog
							.openInformation(
									UIUtil.getActiveShell(),
									"Services Removed",
									"Services have been successfully removed, please either backup or remove the corresponding base consumer classes if applicable.");
				}
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
			if (SOALogger.DEBUG)
				logger.exiting(false);
			return false;
		}
		if (SOALogger.DEBUG)
			logger.exiting(true);
		return true;
	}

}
