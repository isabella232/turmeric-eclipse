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
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAConsumerCreationFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ConsumerFromJavaWizardPage;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ServiceFromNewWSDLPage;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @author yayu
 * 
 */
public class ConsumerFromJavaWizard extends SOABaseWizard {
	private ConsumerFromJavaWizardPage newConsumerPage;
	private SOALogger logger = SOALogger.getLogger();
	// whether for converting an existing Java project into a Consumer project
	private boolean convertExistingJavaProject = false;

	/**
	 * 
	 */
	public ConsumerFromJavaWizard() {
		super();
	}

	public ConsumerFromJavaWizard(boolean convertExistingJavaProject) {
		this();
		this.convertExistingJavaProject = convertExistingJavaProject;
	}

	@Override
	public IWizardPage[] getContentPages() {
		newConsumerPage = new ConsumerFromJavaWizardPage(getSelection(),
				convertExistingJavaProject);
		return new IWizardPage[] { newConsumerPage };
	}

	@Override
	protected Object getCreatingType() {
		return ISOAPreValidator.CONSUMER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			if (SOALogger.DEBUG)
				logger.entering();
			if (!ProjectUtils.isProjectGoodForConsumption(newConsumerPage
					.getServiceList().toArray(new String[0]))) {
				return false;

			}
			final String clientProjectName = newConsumerPage.getClientName();
			// saving the user selected project dir
			final boolean overrideWorkspaceRoot = newConsumerPage
					.isOverrideProjectRootDirectory();
			final String workspaceRootDirectory = newConsumerPage
					.getProjectRootDirectory();
			if (overrideWorkspaceRoot)
				ServiceFromNewWSDLPage
						.saveWorkspaceRoot(workspaceRootDirectory);

			final ConsumerFromJavaParamModel uiModel = new ConsumerFromJavaParamModel();
			uiModel.setBaseConsumerSrcDir(newConsumerPage
					.getBaseConsumerSrcDir());
			uiModel.setParentDirectory(newConsumerPage
					.getProjectRootDirectory());
			uiModel.setServiceNames(newConsumerPage.getServiceList());
			uiModel.setClientName(clientProjectName);
			uiModel.setEnvironments(newConsumerPage.getEnvironments());
			uiModel.setConvertingJavaProject(convertExistingJavaProject);
			if (StringUtils.isNotBlank(newConsumerPage.getConsumerID()))
				uiModel.setConsumerId(newConsumerPage.getConsumerID());
			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					final long startTime = System.currentTimeMillis();
					final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
					monitor.beginTask("Creating SOA Consumer->"
							+ clientProjectName, totalWork);
					try {
						ProgressUtil.progressOneStep(monitor);

						ServiceCreator.createConsumerFromJava(uiModel, monitor);
						final TrackingEvent event = new TrackingEvent(
								"NewConsumerFromJAVA", new Date(startTime),
								System.currentTimeMillis() - startTime);
						GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().trackingUsage(
										event);
					} catch (Exception e) {
						logger.error(e);
						throw new SOAConsumerCreationFailedException(
								"Failed to create consumer ->"
										+ clientProjectName, e);
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
}
