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
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAConsumerCreationFailedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ConsumerFromExistingWSDLWizardPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceFromNewWSDLPage;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOADomainWizard;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;

/**
 * @author yayu
 * 
 */
public class ConsumerFromWSDLWizard extends AbstractSOADomainWizard {
	private ConsumerFromExistingWSDLWizardPage consumerFromWsdl = null;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 */
	public ConsumerFromWSDLWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#getContentPages()
	 */
	@Override
	public IWizardPage[] getContentPages() {
		consumerFromWsdl = new ConsumerFromExistingWSDLWizardPage(
				getSelection());
		return new IWizardPage[] { consumerFromWsdl };
	}

	@Override
	protected Object getCreatingType() {
		return ISOAPreValidator.CONSUMER_FROM_WSDL;
	}

	@Override
	public boolean performFinish() {
		// saving the user selected project dir
		if (SOALogger.DEBUG)
			logger.entering();
		final boolean overrideWorkspaceRoot = consumerFromWsdl
				.isOverrideProjectRootDirectory();
		final String workspaceRootDirectory = consumerFromWsdl
				.getProjectRootDirectory();
		if (overrideWorkspaceRoot)
			ServiceFromNewWSDLPage.saveWorkspaceRoot(workspaceRootDirectory);

		final String serviceName = consumerFromWsdl.getAdminName();
		final String servicePackage = consumerFromWsdl.getServicePackage();
		final String serviceInterface = StringUtils.isBlank(servicePackage) ? serviceName
				: servicePackage + SOAProjectConstants.CLASS_NAME_SEPARATOR
						+ serviceName;

		final ConsumerFromWsdlParamModel uiModel = new ConsumerFromWsdlParamModel();
		uiModel.setServiceName(serviceName);
		uiModel.setPublicServiceName(consumerFromWsdl.getPublicServiceName());
		uiModel.setServiceInterface(serviceInterface);
		uiModel.setOverrideWorkspaceRoot(overrideWorkspaceRoot);
		uiModel.setWorkspaceRootDirectory(workspaceRootDirectory);
		uiModel.setServiceImpl(consumerFromWsdl
				.getFullyQualifiedServiceImplementation());
		uiModel.setServiceVersion(consumerFromWsdl.getServiceVersion());
		uiModel.setServiceLayer(consumerFromWsdl.getServiceLayer());
		final String clientName = consumerFromWsdl.getClientName();
		uiModel
				.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.EXISTIING);
		uiModel.setBaseConsumerSrcDir(consumerFromWsdl.getBaseConsumerSrcDir());
		uiModel.setNamespaceToPacakgeMappings(consumerFromWsdl
				.getNamespaceToPackageMappings());
		uiModel.setClientName(clientName);
		if (StringUtils.isNotBlank(consumerFromWsdl.getConsumerId()))
			uiModel.setConsumerId(consumerFromWsdl.getConsumerId());
		uiModel.setEnvironments(consumerFromWsdl.getEnvironments());
		uiModel.setServiceDomain(consumerFromWsdl.getServiceDomain());
		uiModel.setNamespacePart(consumerFromWsdl.getDomainClassifier());
		uiModel.setTypeFolding(consumerFromWsdl.getTypeFolding());
		try {
			uiModel.setOriginalWsdlUrl(new URL(consumerFromWsdl.getWSDLURL()));
			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					final long startTime = System.currentTimeMillis();
					final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
					monitor.beginTask("Creating consumer from WSDL->"
							+ clientName, totalWork);
					ProgressUtil.progressOneStep(monitor);

					try {
						ServiceCreator.createConsumerFromExistingWSDL(uiModel,
								monitor);
						// we should open the wsdl file for any successful
						// creation of an interface project.
						final IFile wsdlFile = SOAServiceUtil
								.getWsdlFile(serviceName);
						WorkspaceUtil.refresh(wsdlFile, monitor);
						if (wsdlFile.exists()) {
							IDE.openEditor(
									UIUtil.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage(), wsdlFile);
						}
						final TrackingEvent event = new TrackingEvent(
								"NewConsumerFromWSDL", new Date(startTime),
								System.currentTimeMillis() - startTime);
						GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().trackingUsage(
										event);
					} catch (Exception e) {
						logger.error(e);
						throw new SOAConsumerCreationFailedException(
								"Failed to create consumer from WSDL->"
										+ clientName, e);
					} finally {
						monitor.done();
					}
				}

			};
			getContainer().run(false, true, operation);
			changePerspective();
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(getShell(),
					"Error Occured During Consumer Creation", null, e);
			if (SOALogger.DEBUG)
				logger.exiting(false);
			return false;
		}
		if (SOALogger.DEBUG)
			logger.exiting(true);
		return true;
	}

	public int getMinimumHeight() {
		return super.getMinimumHeight() + 150;
	}
}
