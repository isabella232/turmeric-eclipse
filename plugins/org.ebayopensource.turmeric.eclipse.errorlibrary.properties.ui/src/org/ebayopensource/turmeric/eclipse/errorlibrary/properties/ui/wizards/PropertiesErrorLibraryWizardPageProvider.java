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
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.eclipse.TurmericErrorLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * The Class PropertiesErrorLibraryWizardPageProvider.
 *
 * @author yayu
 * @since 1.0.0
 */
public class PropertiesErrorLibraryWizardPageProvider implements
		ISOAErrorLibraryWizardPageProvider {
	private NewPropertiesContentErrorLibraryWizardPage propPage = null;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new properties error library wizard page provider.
	 */
	public PropertiesErrorLibraryWizardPageProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStatus preValidate() {
		return Status.OK_STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SOABasePage> getWizardpages(IStructuredSelection selection) {
		final List<SOABasePage> pages = new ArrayList<SOABasePage>(1);
		propPage = new NewPropertiesContentErrorLibraryWizardPage();
		pages.add(propPage);
		return pages;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ErrorLibraryParamModel performFinish() {
		if (SOALogger.DEBUG)
			logger.entering();
		
		final boolean overrideWorkspaceRoot = propPage
				.isOverrideProjectRootDirectory();
		final String workspaceRootDirectory = propPage
				.getProjectRootDirectory();
		if (overrideWorkspaceRoot) {
			SOABasePage.saveWorkspaceRoot(workspaceRootDirectory);
		}
		
		final ErrorLibraryParamModel model = new ErrorLibraryParamModel();
		model.setProjectName(propPage.getResourceName());
		model.setVersion(propPage.getResourceVersion());
		model.setOverrideWorkspaceRoot(overrideWorkspaceRoot);
		model.setWorkspaceRootDirectory(workspaceRootDirectory);
		model.addEclipseNature(TurmericErrorLibraryProjectNature.getNatureId());
		
		if (SOALogger.DEBUG)
			logger.exiting();
		return model;
	}

}
