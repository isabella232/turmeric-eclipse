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
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOATypeLibraryCreationFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.buildsystem.TypeLibraryCreator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.NewTypeLibraryWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.wizards.AbstractTypeLibraryWizard;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @author yayu
 * 
 */
public class NewTypeLibraryWizard extends AbstractTypeLibraryWizard {
	private static final SOALogger logger = SOALogger.getLogger();

	private NewTypeLibraryWizardPage typeLibWizardPage;

	public NewTypeLibraryWizardPage getTypeLibWizardPage() {
		return typeLibWizardPage;
	}

	public void setTypeLibWizardPage(NewTypeLibraryWizardPage typeLibWizardPage) {
		this.typeLibWizardPage = typeLibWizardPage;
	}

	/**
	 * 
	 */
	public NewTypeLibraryWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#getContentPages()
	 */
	@Override
	public IWizardPage[] getContentPages() {
		typeLibWizardPage = new NewTypeLibraryWizardPage();
		return new IWizardPage[] { typeLibWizardPage };
	}

	@Override
	public boolean performFinish() {
		try {
			final String typeLibraryName = typeLibWizardPage.getNameValue();
			final String typeLibraryVersion = typeLibWizardPage
					.getVersionValue();
			final String typeLibraryCategory = typeLibWizardPage
					.getCategoryValue();
			final String typeLibraryNamespace = typeLibWizardPage
					.getNamespaceValue();
			final String functionDomain = typeLibWizardPage.getServiceDomain();

			// saving the user selected project dir
			final boolean overrideWorkspaceRoot = typeLibWizardPage
					.isOverrideProjectRootDirectory();
			final String workspaceRootDirectory = typeLibWizardPage
					.getProjectRootDirectory();
			if (overrideWorkspaceRoot)
				SOABasePage.saveWorkspaceRoot(workspaceRootDirectory);

			// TODO create the UI model

			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					final long startTime = System.currentTimeMillis();
					final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
					monitor.beginTask("Creating type library project->"
							+ typeLibraryName, totalWork);
					ProgressUtil.progressOneStep(monitor);

					try {
						// create the new type library project
						TypeLibraryParamModel typeLibraryParamModel = new TypeLibraryParamModel();
						typeLibraryParamModel
								.setTypeLibraryName(typeLibraryName);
						typeLibraryParamModel.setFunctionDomain(functionDomain);
						typeLibraryParamModel.setCategory(typeLibraryCategory);
						typeLibraryParamModel.setVersion(typeLibraryVersion);
						typeLibraryParamModel
								.setWorkspaceRoot(workspaceRootDirectory);
						typeLibraryParamModel
								.setNamespace(typeLibraryNamespace);
						TypeLibraryCreator.createTypeLibrary(
								typeLibraryParamModel, monitor);
						final TrackingEvent event = new TrackingEvent(
								"NewTypeLibrary", new Date(startTime), System
										.currentTimeMillis()
										- startTime);
						GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().trackingUsage(
										event);
					} catch (Exception e) {
						logger.error(e);
						throw new SOATypeLibraryCreationFailedException(
								"Failed to create Type Library->"
										+ typeLibraryName, e);
					} finally {
						monitor.done();
					}
				}
			};

			getContainer().run(false, true, operation);
			changePerspective();
			return true;
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
			return false;
		}
	}
}
