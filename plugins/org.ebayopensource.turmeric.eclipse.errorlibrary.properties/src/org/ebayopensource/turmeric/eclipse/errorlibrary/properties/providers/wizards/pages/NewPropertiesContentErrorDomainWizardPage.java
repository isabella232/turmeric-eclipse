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
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.wizards.pages;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 *
 */
public class NewPropertiesContentErrorDomainWizardPage extends
AbstractNewErrorResourceWizardPage {
	private Text domainText;
	private CCombo organizationCombo;
	private CCombo localeCombo;
	private Text packageText;
	private Button overridePackage;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public NewPropertiesContentErrorDomainWizardPage(IStructuredSelection selection) {
		super("newErrorDomainWizardPage", "New Error Domain", 
		"Create a new error domain", selection);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage#getDefaultResourceName()
	 */
	@Override
	public String getDefaultResourceName() {
		return PropertiesSOAConstants.DEFAULT_ERROR_DOMAIN_NAME;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		try {
			final Composite container = super.createParentControl(parent, 4);
			
			createErrorLibrarySelector(container);
			final ModifyListener domainModifyListener = new ModifyListener() {
				
				public void modifyText(ModifyEvent e) {
					if (packageText != null && overridePackage.getSelection() == false) {
						packageText.setText(getDefaultValue(packageText));
					}
					modifyListener.modifyText(e);
				}
			};
			domainText = super.createResourceNameControl(container, SOAMessages.TEXT_NAME_DOMAIN_NAME, 
					domainModifyListener, true, SOAMessages.TOOLTIP_NAME_DOMAIN_NAME);
			packageText = super.createLabelTextField(container,
					SOAMessages.PACKAGE_NAME_DOMAIN_NAME,
					PropertiesSOAConstants.DEFAULT_DOMAIN_PACKAGE, 
					modifyListener, false, false,
					SOAMessages.TOOLTIP_NAME_PACKAGE_NAME);
			packageText.setText(getDefaultValue(packageText));
			overridePackage = super.createOverrideButton(container, packageText, null);
			organizationCombo = super
			.createCCombo(container, SOAMessages.TEXT_NAME_ORGANIZATION, true, 
					new String[]{PropertiesSOAConstants.DEFAULT_ORGANIZATION}, 
					SOAMessages.TOOLTIP_NAME_ORGANIZATION);
			localeCombo = NewPropertiesContentErrorLibraryWizardPage
			.createLocaleCombo(this, container);
			localeCombo.setEditable(false);
			localeCombo.setEnabled(false);
			setControl(container);
			dialogChanged();
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
	}	
	
	
	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false) {
			return false;
		}
		
		if (StringUtils.isEmpty(getDomainName())) {
			super.updateStatus(this.domainText, SOAMessages.ERROR_EMPTY_DOMAIN_NAME);
			return false;
		}
		
		if (StringUtils.isEmpty(getPackageName())) {
			super.updateStatus(this.packageText, SOAMessages.ERROR_EMPTY_DOMAIN_PACKAGE);
			return false;
		}
		
		try {
			if (TurmericErrorRegistry.containsErrorDomain(getDomainName())) {
				super.updateStatus(this.domainText, MessageFormat.format(
						SOAMessages.ERROR_DUPLICATE_DOMAIN_NAME,
						new Object[] { getDomainName() }));
				return false;
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
		
		//final String errorLibrary = getErrorLibrary();
		
		return true;
	}

	@Override
	protected void errorLibraryChanged() {
		
	}
	
	public String getPackageName() {
		return getTextValue(packageText);
	}

	public String getDomainName() {
		return getTextValue(domainText);
	}
	
	public String getOrganization() {
		return getTextValue(organizationCombo);
	}
	
	public String getLocale() {
		return getTextValue(localeCombo);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		if (text == this.packageText) {
			StringBuffer buf = new StringBuffer();
			buf.append(PropertiesSOAConstants.DEFAULT_DOMAIN_PACKAGE);
			if (StringUtils.isNotBlank(getDomainName())) {
				buf.append(SOAProjectConstants.DELIMITER_DOT);
				buf.append(getDomainName().toLowerCase(Locale.US));
			}
			return buf.toString();
		}
		return super.getDefaultValue(text);
	}

}
