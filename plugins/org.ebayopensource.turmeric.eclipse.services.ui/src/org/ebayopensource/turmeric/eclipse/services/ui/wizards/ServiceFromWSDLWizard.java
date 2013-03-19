/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAServiceCreationFailedException;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator;
import org.ebayopensource.turmeric.eclipse.registry.intf.IRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.intf.IValidationStatus;
import org.ebayopensource.turmeric.eclipse.registry.models.SimpleAssetModel;
import org.ebayopensource.turmeric.eclipse.registry.util.RegistryStatusUtil;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ChooseWSDLSourcePage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.DependenciesWizardPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceFromExistingWSDLWizardPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceFromNewWSDLAddBindingWizardPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceFromNewWSDLAddOperationWizardPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceFromNewWSDLPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceProtocolSelectionWizardPage;
import org.ebayopensource.turmeric.eclipse.template.wsdl.processors.WSDLTemplateProcessor;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOADomainWizard;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.actions.SOAStatusReportingRunnable;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;

/**
 * The Wizard represents service from new and from existing WSDL both. It stages
 * all the wizard pages and finally formulates the UI model and pass it to the
 * back end for processing. It has the wizard page navigation logic as the next
 * wizard is mostly controlled by the current pages inputs.Even if there are
 * some code in the UI component, it does not have many SOA specific logic
 * except for some inevitable UI validations and selection responses. The
 * principle here is to make the UI as dumb as possible to enable non UI testing
 * possible to the maximum extent.
 * 
 * @author smathew
 */
public class ServiceFromWSDLWizard extends AbstractSOADomainWizard {
	private static final SOALogger logger = SOALogger.getLogger();

	private ChooseWSDLSourcePage chooseWsdlSource = null;
	private ServiceFromNewWSDLPage serviceFromNewWSDL = null;
	private DependenciesWizardPage intfDependenciesPage = null;
	private DependenciesWizardPage implDependenciesPage = null;
	private ServiceFromExistingWSDLWizardPage serviceFromExsitingWSDL = null;
	private ServiceFromNewWSDLAddOperationWizardPage addOperationPage = null;
	private ServiceFromNewWSDLAddBindingWizardPage addBindingPage = null;
	private ServiceProtocolSelectionWizardPage protocolPage = null;

	/**
	 * Instantiates a new service from wsdl wizard.
	 */
	public ServiceFromWSDLWizard() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canFinish() {
		
		
			
			
		
		if (			
				serviceFromExsitingWSDL == null
			
				) {
			logger.warning(StringUtil.formatString(SOAMessages.INITIALIZE_ERR));
			return false;
		}
		if(getContainer().getCurrentPage() == serviceFromExsitingWSDL){
			return serviceFromExsitingWSDL.isPageComplete()&&(serviceFromExsitingWSDL.customCanFlipToNextPage()==false);
		}
			return serviceFromExsitingWSDL.isPageComplete()
					&& getContainer().getCurrentPage() != chooseWsdlSource;

	}
	public boolean doCheck(){
		final String serviceName = serviceFromExsitingWSDL.getAdminName();
		final String servicePackage = serviceFromExsitingWSDL.getServicePackage();
		Map<String,String> allNSToPackMappings= serviceFromExsitingWSDL.getNamespaceToPackageMappings();
		allNSToPackMappings.put(servicePackage.toLowerCase(),servicePackage.toLowerCase());
		allNSToPackMappings.put(servicePackage.toLowerCase()+".gen",servicePackage.toLowerCase()+".gen");
		final String adminName = serviceFromExsitingWSDL.getAdminName();
		final String serviceInterface = SOAServiceUtil
				.generateInterfaceClassName(adminName, servicePackage);

		//During creation service Name is hardcoded
		String fullServiceName = "com.ebay.soa.interface."+serviceName;
		
		Set<String> mappedPackages = new HashSet<String>();
		mappedPackages.addAll(allNSToPackMappings.values());
		
		
		if(!callSplitPackageService(fullServiceName,mappedPackages,false))
			//Cancel has been Pressed on the dialog box
			return false;
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performFinish() {
		if (SOALogger.DEBUG)
			logger.entering();
		final boolean fromExistingWsdl = true;
		final AbstractNewServiceFromWSDLWizardPage wizardPage = fromExistingWsdl ? serviceFromExsitingWSDL
				: serviceFromNewWSDL;
		// saving the user selected project dir
		final boolean overrideWorkspaceRoot = wizardPage
				.isOverrideProjectRootDirectory();
		final String workspaceRootDirectory = wizardPage
				.getProjectRootDirectory();
		if (overrideWorkspaceRoot)
			SOABasePage.saveWorkspaceRoot(workspaceRootDirectory);
		final String serviceName = wizardPage.getAdminName();
		final String servicePackage = wizardPage.getServicePackage();
		final String adminName = wizardPage.getAdminName();
		final String serviceInterface = SOAServiceUtil
				.generateInterfaceClassName(adminName, servicePackage);
		if(! doCheck())
			return false;
		intfDependenciesPage.finished();		

		final String implProjectName = adminName
				+ SOAProjectConstants.IMPL_PROJECT_SUFFIX;

		final ServiceFromWsdlParamModel uiModel = fromExistingWsdl ? new ServiceFromWsdlParamModel()
				: new ServiceFromTemplateWsdlParamModel();
		uiModel.setServiceName(adminName);
		uiModel.setPublicServiceName(wizardPage.getPublicServiceName());
		uiModel.setServiceInterface(serviceInterface);
		uiModel.setOverrideWorkspaceRoot(overrideWorkspaceRoot);
		uiModel.setWorkspaceRootDirectory(workspaceRootDirectory);
		uiModel.setServiceImpl(wizardPage
				.getFullyQualifiedServiceImplementation());
		uiModel.setServiceVersion(wizardPage.getServiceVersion());
		uiModel.setServiceImplType(wizardPage.getServiceImplType());
		if (!wizardPage.getTypeFolding())
			uiModel.setTypeNamespace(wizardPage.getTypeNamespace());
		uiModel.setTypeFolding(wizardPage.getTypeFolding());
		uiModel.setServiceLayer(wizardPage.getServiceLayer());
		uiModel.setInterfaceLibs(intfDependenciesPage.getLibraries());
		//uiModel.setInterfaceProjects(intfDependenciesPage.getProjects());
		//uiModel.setImplLibs(implDependenciesPage.getLibraries());
		//uiModel.setImplProjects(implDependenciesPage.getProjects());
		uiModel.setImplName(implProjectName);
		uiModel.setServiceDomain(wizardPage.getServiceDomain());
		uiModel.setNamespacePart(wizardPage.getDomainClassifier());
		uiModel.setServiceNonXSDProtocols(protocolPage.getNonXSDProtocolString());
		uiModel.setNamespaceToPacakgeMappings(wizardPage
				.getNamespaceToPackageMappings());
		uiModel.setRaptorSvcImpl(true);
		try {
			if (fromExistingWsdl) {
				uiModel
						.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.EXISTIING);
				uiModel.setOriginalWsdlUrl(new URL(serviceFromExsitingWSDL
						.getWSDLURL()));
			} else {
				final ServiceFromTemplateWsdlParamModel tempModel = (ServiceFromTemplateWsdlParamModel) uiModel;
				tempModel.setTemplateFile(serviceFromNewWSDL.getTemplateFile());
				uiModel.setTargetNamespace(serviceFromNewWSDL
						.getTargetNamespace());
				uiModel
						.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.NEW);
				tempModel.setOperations(addOperationPage.getOperations());
				tempModel.setBindings(addBindingPage.getBindings());
			}
			final ISOAOrganizationProvider provider = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getActiveOrganizationProvider();

			// registry validation
			SOAStatusReportingRunnable validationRunnable = new SOAStatusReportingRunnable() {

				@Override
				public IStatus execute(IProgressMonitor monitor)
						throws Exception {
					monitor.beginTask("Checking existence for service->"
							+ uiModel.getServiceName(),
							ProgressUtil.PROGRESS_STEP * 30);
					final List<IStatus> statuses = new ArrayList<IStatus>();

					IRegistryProvider regProvider = ExtensionPointFactory
							.getSOARegistryProvider();
					if (regProvider == null) {
						// could not find a reg provider
						logger
								.warning(SOAMessages.WARNING_MISSING_REGISTRY_PROVIDER);
					} else {
						ProgressUtil.progressOneStep(monitor);

						final SimpleAssetModel assetModel = new SimpleAssetModel();
						assetModel.setAssetAdminName(uiModel.getServiceName());
						assetModel.setAssetName(uiModel.getPublicServiceName());
						assetModel.setAssetVersion(uiModel.getServiceVersion());
						assetModel.setDomainName(uiModel.getServiceDomain());
						assetModel.setNamespacePart(uiModel.getNamespacePart());
						logger.info("Checking asset name existence ->",
								assetModel);
						IStatus nameCheckStatus = regProvider.assetNameExists(
								assetModel, monitor);

						logger.info("Asset name existence checking result->",
								nameCheckStatus);

						if (RegistryStatusUtil
								.isLoginFailStatus(nameCheckStatus)) {
							if (SOALogger.DEBUG)
								logger.debug("Asset Repository login failed.");
						} else if (nameCheckStatus.getSeverity() == IStatus.CANCEL) {
							if (SOALogger.DEBUG)
								logger
										.debug("Asset Repository login canceled. Service Name duplication check is skipped.");
						} else if (nameCheckStatus.isOK() == false) {
							statuses.add(nameCheckStatus);
						}
						ProgressUtil.progressOneStep(monitor);
					}

					final List<IArtifactValidator> validators = ExtensionPointFactory
							.getArtifactValidators();
					if (validators.isEmpty() == true) {
						logger
								.warning(SOAMessages.WARNING_MISSING_ARTIFACT_VALIDATOR);
					}
					ProgressUtil.progressOneStep(monitor);
					URL wsdlURL = null;
					if (fromExistingWsdl == true) {
						wsdlURL = uiModel.getOriginalWsdlUrl();
					} else {
						WSDLTemplateProcessor wsdlTemplateProcessor = new WSDLTemplateProcessor(
								(ServiceFromTemplateWsdlParamModel) uiModel);
						wsdlURL = wsdlTemplateProcessor.getWSDLFileURL(monitor);
					}
					IStatus validationStatuses = ActionUtil
							.validateServiceWSDL(null, wsdlURL, provider
									.supportAssertionServiceIntegration(),
									false, monitor);
					if (validationStatuses != null) {
						if (validationStatuses.isMultiStatus() == true) {
							Collections.addAll(statuses, validationStatuses
									.getChildren());
						} else {
							statuses.add(validationStatuses);
						}
					}

					if (statuses.isEmpty() == false) {
						return EclipseMessageUtils
								.createErrorMultiStatus(
										statuses,
										SOAMessages.ERROR_SERVICE_WSDL_VALIDATION_FAILED);
					}
					monitor.done();
					return Status.OK_STATUS;
				}

			};

			// we only do the validation if and only if the underlying
			// organization support it.
			if (provider != null
					&& (provider.supportAssetRepositoryIntegration() || provider
							.supportAssertionServiceIntegration())) {
				try {
					getContainer().run(false, false, validationRunnable);
				} catch (Exception arExp) {
					logger.error(arExp);
					IStatus status = UIUtil
							.getDetailedExceptionStackTrace(arExp);
					if (UIUtil.showErrorDialog(getShell(), "Error occured",
							SOAMessages.ERROR_ASSET_REPOSITORY, status, false,
							false, true, true) == Window.CANCEL) {
						if (SOALogger.DEBUG)
							logger.exiting(false);

						return false;
					}
				}

				IStatus regStatus = validationRunnable.getStatus();
				if (regStatus.isOK() == false) {

					int errorCount = 0, warningCount = 0, mayCount = 0;
					final StringBuffer msg = new StringBuffer();
					if ((regStatus instanceof NameValidationStatus)
							&& regStatus.getSeverity() == IStatus.WARNING) {
						// could be connection issue
						msg.append("Problems occured.");
					} else {
						msg.append(SOAMessages.ISSUES_DIALOG_OPENING);
					}

					if (regStatus.isMultiStatus() == true) {

						for (IStatus status : regStatus.getChildren()) {
							switch (status.getSeverity()) {
							case IStatus.WARNING:
								if (status.getCode() == IValidationStatus.CODE_MAY)
									mayCount++;
								else
									warningCount++;
								break;
							case IStatus.ERROR:
								errorCount++;
								break;
							}
						}
					}

					if (errorCount > 0) {
						msg.append("\n");
						msg.append(SOAMessages.ISSUES_DIALOG_MUST_FIX);
						msg.append(errorCount);
					}
					if (warningCount > 0) {
						msg.append("\n");
						msg.append(SOAMessages.ISSUES_DIALOG_SHOULD_FIX);
						msg.append(warningCount);
					}
					if (mayCount > 0) {
						msg.append("\n");
						msg.append(SOAMessages.ISSUES_DIALOG_MAY_FIX);
						msg.append(mayCount);
					}

					boolean isNameValidationError = (regStatus instanceof NameValidationStatus)
							&& regStatus.getSeverity() == IStatus.ERROR;
					if (isNameValidationError == true) {
						regStatus = ((NameValidationStatus) regStatus)
								.getNestedStatus();
						msg.append("\n\n");
						msg
								.append(SOAMessages.ISSUES_DIALOG_FIX_IN_DETAILS_PAGE);
					} else {
						msg.append("\n\n");
						msg.append(SOAMessages.ISSUES_DIALOG_OK_OR_CONTINUE);
					}
					if (regStatus.getException() != null
							|| regStatus.isMultiStatus() == true) {
						msg.append("\n\n");
						msg.append(SOAMessages.ISSUES_DIALOG_COPY_TO_CLIPBOARD);
					}
					String title = regStatus.getSeverity() == IStatus.WARNING ? "Problems Occured"
							: "Validation Failed";
					logger.warning(msg);
					logger.warning(regStatus);
					int ret = UIUtil.showErrorDialog(getShell(), title, msg
							.toString(), regStatus, true, true,
							isNameValidationError == false,
							isNameValidationError == false);
					if (ret == Window.CANCEL || isNameValidationError == true) {
						if (SOALogger.DEBUG)
							logger.exiting(false);
						return false;
					}
				}
			}

			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					final long startTime = System.currentTimeMillis();
					final int totalWork = ProgressUtil.PROGRESS_STEP * 50;
					monitor.beginTask(StringUtil.formatString(
							SOAMessages.SVC_CREATE, adminName), totalWork);
					ProgressUtil.progressOneStep(monitor);
					try {
						String wsdlSource = "Existing";
						if (fromExistingWsdl) {
							ServiceCreator.createServiceFromExistingWSDL(
									uiModel, monitor);
						} else {
							wsdlSource = "Blank";
							ServiceCreator
									.createServiceFromBlankWSDL(
											(ServiceFromTemplateWsdlParamModel) uiModel,
											monitor);
						}
						// we should open the wsdl file for any successful
						// creation of an interface project.
						final IFile wsdlFile = SOAServiceUtil
								.getWsdlFile(adminName);
						WorkspaceUtil.refresh(wsdlFile, monitor);
						if (wsdlFile.exists()) {
							IDE.openEditor(
									UIUtil.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage(), wsdlFile);
						}
						final TrackingEvent event = new TrackingEvent(
								StringUtil.formatString(SOAMessages.NEW_SVC,
										wsdlSource), new Date(startTime),
								System.currentTimeMillis() - startTime);
						GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().trackingUsage(
										event);
					} catch (Exception e) {
						logger.error(e);
						throw new SOAServiceCreationFailedException(StringUtil
								.formatString(SOAMessages.SVC_CREATE_FAILED,
										adminName), e);
					} finally {
						monitor.done();
					}
				}

			};
			getContainer().run(false, true, operation);
			changePerspective();
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(getShell(), SOAMessages.ERR_SVC_CREATE,
					null, e);
			if (SOALogger.DEBUG)
				logger.exiting(false);
			return false;
		}
		if (SOALogger.DEBUG)
			logger.exiting(true);
		return true;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof ChooseWSDLSourcePage) {
			if (((ChooseWSDLSourcePage) page).isStartFromNewWSDL()) {
				protocolPage.setWizardPage(serviceFromNewWSDL);
				return serviceFromNewWSDL;
			} else {
				protocolPage.setWizardPage(serviceFromExsitingWSDL);
				return serviceFromExsitingWSDL;
			}
		}
		// avoiding service from wsdl page coming up two times
		else if (page == serviceFromNewWSDL) {
			return addOperationPage;
		} else if (page == serviceFromExsitingWSDL) {
			protocolPage.setWizardPage(serviceFromExsitingWSDL);
			
			if (serviceFromExsitingWSDL.getTypeFolding()&&serviceFromExsitingWSDL.canFlipTOTL())
			return intfDependenciesPage;
			return protocolPage;
		} else if (page == addOperationPage) {
			return addBindingPage;
		} else if (page == addBindingPage) {
			return intfDependenciesPage;
		} else if (page == intfDependenciesPage) {
			return protocolPage;
		} else if (page == implDependenciesPage) {
			return protocolPage;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IWizardPage[] getContentPages() {
		//chooseWsdlSource = new ChooseWSDLSourcePage();
		//serviceFromNewWSDL = new ServiceFromNewWSDLPage();
		serviceFromExsitingWSDL = new ServiceFromExistingWSDLWizardPage();
		intfDependenciesPage = new DependenciesWizardPage(SOAMessages.SVC_INTF);
		//implDependenciesPage = new DependenciesWizardPage(
		//		"Service Implementation");
		//addOperationPage = new ServiceFromNewWSDLAddOperationWizardPage();
		//addBindingPage = new ServiceFromNewWSDLAddBindingWizardPage();

		protocolPage = new ServiceProtocolSelectionWizardPage();

		List<IWizardPage> pages = new ArrayList<IWizardPage>();
		//pages.add(chooseWsdlSource);
		//pages.add(serviceFromNewWSDL);
		pages.add(serviceFromExsitingWSDL);
		//pages.add(addOperationPage);
		//pages.add(addBindingPage);
		pages.add(intfDependenciesPage);
	//	pages.add(implDependenciesPage);
		pages.add(protocolPage);
		return pages.toArray(new IWizardPage[pages.size()]);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getCreatingType() {
		return ISOAPreValidator.SERVICE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinimumHeight() {
		return super.getMinimumHeight() + 50;
	}
	
	
	private static class NameValidationStatus extends Status {
		private IStatus nestedStatus;

		public NameValidationStatus(IStatus status) {
			super(status.getSeverity(), status.getPlugin(), status.getCode(),
					status.getMessage(), status.getException());
			this.nestedStatus = status;
		}

		public IStatus getNestedStatus() {
			return nestedStatus;
		}
	}

}
