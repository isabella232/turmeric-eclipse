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

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAErrorTypeCreationFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @author yayu
 * @since 1.0.0
 */
public class NewErrorLibraryWizard extends SOABaseWizard {
	private ISOAErrorLibraryWizardPageProvider wizardProvider;
	private static final SOALogger logger = SOALogger.getLogger();

	public NewErrorLibraryWizard() {
		super();
	}

	@Override
	public IStatus preValidate() throws ValidationInterruptedException {
		final IStatus result = super.preValidate();
		if (result.isOK() == false) {
			return result;
		}
		try {
			return ErrorLibraryProviderFactory.getPreferredProvider()
					.getErrorLibraryWizardPageProvider().preValidate();
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			return EclipseMessageUtils.createErrorStatus(e);
		}
	}

	@Override
	protected Object getCreatingType() {
		return ISOAPreValidator.ERROR_LIBRARY;
	}

	@Override
	public IWizardPage[] getContentPages() {
		final List<IWizardPage> pages = new ArrayList<IWizardPage>();
		IErrorLibraryProvider errorLibProvider;
		try {

			errorLibProvider = ErrorLibraryProviderFactory
					.getPreferredProvider();
			if (errorLibProvider != null
					&& errorLibProvider.getErrorLibraryWizardPageProvider() != null) {
				wizardProvider = errorLibProvider
						.getErrorLibraryWizardPageProvider();
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
				if ((uimodel instanceof ErrorLibraryParamModel) == false) {
					return false;
				}
				final ErrorLibraryParamModel model = (ErrorLibraryParamModel) uimodel;
				final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						final long startTime = System.currentTimeMillis();
						final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
						monitor.beginTask(StringUtil.formatString(
								SOAMessages.CREATING_ERRLIB, model
										.getProjectName()), totalWork);
						ProgressUtil.progressOneStep(monitor);
						try {
							ErrorLibraryCreator.createErrorLibrary(model,
									monitor);
							final TrackingEvent event = new TrackingEvent(
									SOAMessages.NEW_ERR_LIB,
									new Date(startTime), System
											.currentTimeMillis()
											- startTime);
							GlobalRepositorySystem.instanceOf()
									.getActiveRepositorySystem().trackingUsage(
											event);
							changePerspective();
						} catch (Exception e) {
							logger.error(e);
							throw new SOAErrorTypeCreationFailedException(
									"Failed to create new error library->"
											+ model.getProjectName(), e);
						} finally {
							monitor.done();
						}
					}

				};
				ErrorLibraryProviderFactory.getPreferredProvider()
						.getErrorLibraryCreator().preCreation(model);
				getContainer().run(false, true, operation);
			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(SOAMessages.CREATE_ERRLIB_ERR, e);
				if (SOALogger.DEBUG)
					logger.exiting(false);
				return false;
			}
		}
		return true;
	}

}
