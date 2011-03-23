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
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.wizards.pages.NewPropertiesContentErrorDomainWizardPage;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author yayu
 *
 */
public class PropertiesDomainWizardPageProvider implements
		ISOAErrorLibraryWizardPageProvider {
	private NewPropertiesContentErrorDomainWizardPage propPage = null;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 */
	public PropertiesDomainWizardPageProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider#getWizardpages(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public List<SOABasePage> getWizardpages(IStructuredSelection selection) {
		final List<SOABasePage> pages = new ArrayList<SOABasePage>(1);
		propPage = new NewPropertiesContentErrorDomainWizardPage(selection);
		pages.add(propPage);
		return pages;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider#performFinish()
	 */
	public BaseServiceParamModel performFinish() {
		if (SOALogger.DEBUG)
			logger.entering();
		
		final DomainParamModel model = new DomainParamModel();
		model.setPackageName(propPage.getPackageName());
		model.setDomain(propPage.getDomainName());
		model.setOrganization(propPage.getOrganization());
		model.setErrorLibrary(propPage.getErrorLibrary());
		model.setLocale(propPage.getLocale());
		
		if (SOALogger.DEBUG)
			logger.exiting(model);
		return model;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider#preValidate()
	 */
	public IStatus preValidate() {
		return Status.OK_STATUS;
	}

}
