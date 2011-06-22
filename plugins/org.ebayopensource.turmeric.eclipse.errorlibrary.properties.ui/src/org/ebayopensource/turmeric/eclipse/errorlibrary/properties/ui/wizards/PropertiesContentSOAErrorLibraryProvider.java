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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesContentErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesContentErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesContentErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesErrorRegistryViewProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorRegistryViewProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ISOAErrorLibraryWizardPageProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.resources.IProject;


/**
 * The Class PropertiesContentSOAErrorLibraryProvider.
 *
 * @author yayu
 */
public class PropertiesContentSOAErrorLibraryProvider implements
		IErrorLibraryProvider {
	
	/** The Constant PROVIDER_ID. */
	public static final String PROVIDER_ID;
	
	static {
		String id = "PropertiesContentErrorLibraryProvider";
		try {
			String data = JDTUtil.getPluginProperties(Activator.getDefault().getBundle())
			.getString("ErrorLib.Provider.ID.Properties");
			if (StringUtils.isNotBlank(data)) {
				id = data;
			}
		} catch (IOException e) {
			//ignore
		}
		PROVIDER_ID = id;
	}
		
	
	private ISOAErrorLibraryWizardPageProvider errorLibWizardProvider = null;
	private ISOAErrorLibraryWizardPageProvider errorDomainWizardProvider = null;
	private ISOAErrorLibraryWizardPageProvider errorWizardProvider = null;
	private IErrorLibraryCreator errorCreator = null;
	private IErrorDomainCreator domainCreator = null;
	private IErrorTypeCreator typeCreator = null;
	private IErrorRegistryViewProvider viewProvider = null;

	/**
	 * Instantiates a new properties content soa error library provider.
	 */
	public PropertiesContentSOAErrorLibraryProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISOAErrorLibraryWizardPageProvider getErrorLibraryWizardPageProvider() {
		if (errorLibWizardProvider == null) {
			errorLibWizardProvider = new PropertiesErrorLibraryWizardPageProvider();
		}
		return errorLibWizardProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISOAErrorLibraryWizardPageProvider getErrorWizardPageProvider() {
		if (errorWizardProvider == null) {
			errorWizardProvider = new PropertiesErrorWizardPageProvider();
		}
		return errorWizardProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISOAErrorLibraryWizardPageProvider getErrorDomainWizardPageProvider() {
		if (errorDomainWizardProvider == null) {
			errorDomainWizardProvider = new PropertiesDomainWizardPageProvider();
		}
		return errorDomainWizardProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProviderID() {
		return PROVIDER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IErrorLibraryCreator getErrorLibraryCreator() {
		if (errorCreator == null) {
			errorCreator = new PropertiesContentErrorLibraryCreator();
		}
		return errorCreator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IErrorDomainCreator getErrorDomainCreator() {
		if (domainCreator == null) {
			domainCreator = new PropertiesContentErrorDomainCreator();
		}
		return domainCreator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IErrorTypeCreator getErrorTypeCreator() {
		if (typeCreator == null) {
			typeCreator = new PropertiesContentErrorTypeCreator();
		}
		return typeCreator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IErrorRegistryViewProvider getErrorRegistryViewProvider() {
		if (viewProvider == null) {
			viewProvider = new PropertiesErrorRegistryViewProvider();
		}
		return viewProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getSourceSubFolders(ISOAProject project) {
		List<String> folders = new ArrayList<String>();
		folders.add(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN + "/" + project.getProjectName());
		return folders;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getImportErrorSource(IProject project, ISOAError error) {
		String retCode = PropertiesSOAConstants.IMPORT_CODE_ERROR_CONSTR;
		retCode = StringUtils.replace(retCode, "$error", error.getName().toUpperCase());
//		retCode = StringUtils.replace(retCode, "$domain", error.getDomain().getName().toUpperCase());
		return retCode;
	}

}
