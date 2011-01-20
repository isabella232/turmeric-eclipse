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
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAErrorTypeCreationFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @author yayu
 * @since 1.0.0
 */
public final class NewErrorWizard extends SOABaseWizard {

	@Override
	public IStatus preValidate() throws ValidationInterruptedException {
		// checking for valid providers
		try {
			ErrorLibraryProviderFactory.getPreferredProvider();
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			return EclipseMessageUtils.createErrorStatus(e);
		}
		return Status.OK_STATUS;
	}

	private ISOAErrorLibraryWizardPageProvider wizardProvider;
	private static final SOALogger logger = SOALogger.getLogger();

	public NewErrorWizard() {
		super();
	}

	@Override
	public IWizardPage[] getContentPages() {
		final List<IWizardPage> pages = new ArrayList<IWizardPage>();
		IErrorLibraryProvider errorLibProvider;
		try {
			errorLibProvider = ErrorLibraryProviderFactory
					.getPreferredProvider();
			if (errorLibProvider != null
					&& errorLibProvider.getErrorWizardPageProvider() != null) {
				wizardProvider = errorLibProvider.getErrorWizardPageProvider();
				pages.addAll(wizardProvider.getWizardpages(getSelection()));
			}
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}

		return pages.toArray(new IWizardPage[0]);
	}

	@Override
	public boolean performFinish() {
		if (wizardProvider != null) {
			try {
				final BaseServiceParamModel uimodel = wizardProvider
						.performFinish();
				if ((uimodel instanceof ErrorParamModel) == false) {
					return false;
				}
				final ErrorParamModel model = (ErrorParamModel) uimodel;
				final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						final long startTime = System.currentTimeMillis();
						final int totalWork = ProgressUtil.PROGRESS_STEP * 50;
						monitor.beginTask(StringUtil.formatString(
								SOAMessages.CREATING_ERR, model.getErrorID()),
								totalWork);
						ProgressUtil.progressOneStep(monitor);
						try {
							ErrorTypeCreator.createErrorType(model, monitor);
							final TrackingEvent event = new TrackingEvent(
									SOAMessages.NEW_ERR, new Date(startTime),
									System.currentTimeMillis() - startTime);
							GlobalRepositorySystem.instanceOf()
									.getActiveRepositorySystem().trackingUsage(
											event);
						} catch (Exception e) {
							logger.error(e);
							throw new SOAErrorTypeCreationFailedException(
									"Failed to generate error->"
											+ model.getName(), e);
						} finally {
							monitor.done();
						}
					}

				};
				ErrorLibraryProviderFactory.getPreferredProvider()
						.getErrorTypeCreator().preCreation(model);
				getContainer().run(false, true, operation);
				changePerspective();
			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(SOAMessages.CREATE_ERR_ERR, e);
				if (SOALogger.DEBUG)
					logger.exiting(false);
				return false;
			}
		}
		return true;
	}

}
