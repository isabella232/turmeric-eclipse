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
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;


/**
 * The Class NewPropertiesContentErrorLibraryWizardPage.
 *
 * @author yayu
 * @since 1.0.0
 */
public class NewPropertiesContentErrorLibraryWizardPage extends
		AbstractSOAProjectWizardPage {

	/**
	 * The constructor.
	 */
	public NewPropertiesContentErrorLibraryWizardPage() {
		super("newErrorLibraryWizardPage", "New Error Library Project",
				"Create a new error library project");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultResourceName() {
		return PropertiesSOAConstants.DEFAULT_ERROR_LIBRARY_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		try {
			final Composite composite = super.createParentControl(parent, 4);
			super.addWorkspaceRootChooser(composite);

			super.createResourceNameControl(composite,
					SOAMessages.TEXT_NAME_PROJECT_NAME, modifyListener,
					true, SOAMessages.TOOLTIP_NAME_PROJECT_NAME);
			super.createResourceVersionControl(composite,
					SOAMessages.TEXT_NAME_VERSION, modifyListener,
					SOAMessages.TOOLTIP_NAME_VERSION_ERRORLIB);

			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}
	}

	/**
	 * Creates the locale combo.
	 *
	 * @param basePage the base SOA Page
	 * @param parent the parent composite
	 * @return A CCombo object with Locale information.
	 */
	protected static CCombo createLocaleCombo(SOABasePage basePage,
			Composite parent) {
		final List<String> items = new ArrayList<String>();
		items.add(toString(Locale.ENGLISH));
		items.add(toString(Locale.US));
		final CCombo combo = basePage.createCCombo(parent,
				SOAMessages.TEXT_NAME_LOCALE, false, items
						.toArray(new String[0]), SOAMessages.TOOLTIP_NAME_LOCALE);
		combo.select(0);
		return combo;
	}

	private static String toString(Locale locale) {
		final String language = locale.getLanguage();
		final String country = locale.getCountry();
		final String variant = locale.getVariant();
		boolean l = language.length() != 0;
		boolean c = country.length() != 0;
		boolean v = variant.length() != 0;
		StringBuffer result = new StringBuffer(language);
		/*
		 * ibm@118171 start The Serbian locale does not obey the convention of
		 * language_country_variant, so we have to special-case it here pending
		 * a properly thought-out fix
		 */
		if ((language.equals("sh") || language.equals("sr"))
				&& (country.equals("RS"))
				&& (variant.equals("Latn") || variant.equals("Cyrl"))) {
			return (language + "-" + variant + "-" + country);
		}
		/* ibm@118171 end */

		if (c || (l && v)) {
			result.append('-').append(country); // This may just append '-'
		}
		if (v && (l || c)) {
			result.append('-').append(variant);
		}
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false) {
			return false;
		}
		if (StringUtils.isBlank(getProjectNamePrefix())) {
			updateStatus(super.getResourceNameText(), 
					"Project name prefix must not be empty");
			return false;
		}
		
		return true;
	}

	/**
	 * Gets the project name prefix.
	 *
	 * @return the project name prefix
	 */
	public String getProjectNamePrefix() {
		return super.getResourceName();
	}

}
