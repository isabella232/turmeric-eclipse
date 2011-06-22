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
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAErrorTypeCreationFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
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
<<<<<<< HEAD
 * The Class NewErrorDomainWizard.
 *
 * @author yayu
=======
 * @author yayu
 * 
>>>>>>> TURMERIC-1351
 */
public class NewErrorDomainWizard extends SOABaseWizard {
	private ISOAErrorLibraryWizardPageProvider wizardProvider;
	private static final SOALogger logger = SOALogger.getLogger();
	private ErrorLibraryProviderFactory factory = ErrorLibraryProviderFactory.getInstance();

	/**
	 * Instantiates a new new error domain wizard.
	 */
	public NewErrorDomainWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#preValidate()
	 */
	@Override
	public IStatus preValidate() {
		// checking if there is a provider
		try {
			ErrorLibraryProviderFactory.getPreferredProvider();
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			return EclipseMessageUtils.createErrorStatus(e);
		}
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#getContentPages()
	 */
	@Override
	public IWizardPage[] getContentPages() {
		final List<IWizardPage> pages = new ArrayList<IWizardPage>();
		IErrorLibraryProvider errorLibProvider;
		try {
			errorLibProvider = ErrorLibraryProviderFactory
					.getPreferredProvider();
			if (errorLibProvider != null
					&& errorLibProvider.getErrorDomainWizardPageProvider() != null) {
				wizardProvider = errorLibProvider
						.getErrorDomainWizardPageProvider();
				pages.addAll(wizardProvider.getWizardpages(getSelection()));
			}
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}

		return pages.toArray(new IWizardPage[0]);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (wizardProvider != null) {
			try {
				final BaseServiceParamModel uimodel = wizardProvider
						.performFinish();
				if ((uimodel instanceof DomainParamModel) == false) {
					return false;
				}
				final DomainParamModel model = (DomainParamModel) uimodel;
				final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						final long startTime = System.currentTimeMillis();
						final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
						monitor.beginTask(
								StringUtil.formatString(
										SOAMessages.CREATING_DOMAIN, model
												.getDomain()), totalWork);
						ProgressUtil.progressOneStep(monitor);
						try {
							ErrorDomainCreator
									.createErrorDomain(model, monitor);
							final TrackingEvent event = new TrackingEvent(
									SOAMessages.NEW_ERR_DOMAIN, new Date(
											startTime), System
											.currentTimeMillis()
											- startTime);
							GlobalRepositorySystem.instanceOf()
									.getActiveRepositorySystem().trackingUsage(
											event);
						} catch (Exception e) {
							logger.error(e);
							throw new SOAErrorTypeCreationFailedException(
									"Failed to create new error domain->"
											+ model.getDomain(), e);
						} finally {
							monitor.done();
						}
					}
				};
				ErrorLibraryProviderFactory.getPreferredProvider()
						.getErrorDomainCreator().preCreation(model);
				getContainer().run(false, true, operation);
				// http://www.nbweekly.com/Print/Page/844,62.shtml
				// No change if just resource created inside a project
				// changePerspective();
			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(SOAMessages.CREATE_DOMAIN_ERR, e);
				if (SOALogger.DEBUG)
					logger.exiting(false);
				return false;
			}
		}
		return true;
	}

}
