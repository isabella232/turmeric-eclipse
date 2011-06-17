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
package org.ebayopensource.turmeric.eclipse.ui.wizards;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOADomainWizard;
import org.ebayopensource.turmeric.eclipse.ui.UIConstants;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * The Class AbstractTypeLibraryWizard.
 *
 * @author yayu
 */
public abstract class AbstractTypeLibraryWizard extends AbstractSOADomainWizard {

	/** The Constant logger. */
	protected static final SOALogger logger = SOALogger.getLogger();
	
	/** The type lib name. */
	protected String typeLibName = "";
	

	/**
	 * Instantiates a new abstract type library wizard.
	 */
	public AbstractTypeLibraryWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#preValidate()
	 */
	@Override
	public IStatus preValidate() throws ValidationInterruptedException {
		// initialize the Global type Registry
		try {
			SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
		} catch (Exception exception) {
			return EclipseMessageUtils
					.createErrorStatus("The Type Registry could not be initialized. This is probably due to a bad repository system. Please make sure the repository(V3 or Maven) is configured:"
							+ StringUtils.defaultString(exception.getMessage()));
		}
		ISOARepositorySystem activeRepositorySystem = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem();
		ISOAPreValidator validator = activeRepositorySystem.getPreValidator();
		return validator.validate(ISOAPreValidator.TYPE_LIBRARY);

	}

	/**
	 * Sets the selected project name.
	 *
	 * @param selection the new selected project name
	 * @throws CoreException the core exception
	 */
	protected void setSelectedProjectName(IStructuredSelection selection)
			throws CoreException {
		if (selection != null) {
			if (selection.getFirstElement() != null) {
				if (selection.getFirstElement() instanceof IJavaElement) {
					IProject project = ((IJavaElement) selection
							.getFirstElement()).getJavaProject().getProject();
					if (project.hasNature(UIConstants.TYPELIB_NATURE_ID)) {
						typeLibName = project.getName();
					}
				}
			}
		}
	}
}
