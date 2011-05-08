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

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.ErrorLibraryUtil;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.SOAErrorLibraryConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * The Class NewPropertiesContentErrorWizardPage.
 *
 * @author yayu
 * @since 1.0.0
 */
public final class NewPropertiesContentErrorWizardPage extends
		AbstractNewErrorResourceWizardPage {
	private CCombo categoryCombo;
	private Text organizationText;
	private CCombo domainCombo;

	private CCombo severityCombo;
	private Text messageText;
	private Text resolutionText;
	private Text subdomainText;
	
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new new properties content error wizard page.
	 *
	 * @param selection A structured selection object
	 */
	public NewPropertiesContentErrorWizardPage(IStructuredSelection selection) {
		super("newErrorWizardPage", "New Error", "Create a new error",
				selection);

	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage#getDefaultResourceName()
	 */
	@Override
	public String getDefaultResourceName() {
		return PropertiesSOAConstants.DEFAULT_ERROR_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createControl(Composite parent) {
		try {
			final Composite container = super.createParentControl(parent, 4);

			createErrorLibrarySelector(container);

			final int width = 200;
			this.domainCombo = super.createCCombo(container,
					SOAMessages.TEXT_NAME_DOMAIN, false, new String[] {}, 
					SOAMessages.TOOLTIP_NAME_DOMAIN);
			((GridData) domainCombo.getLayoutData()).widthHint = width;
			domainCombo.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					setOrganization();
				}
			});
			
			this.organizationText = super.createLabelTextField(container,
					SOAMessages.TEXT_NAME_ORGANIZATION,
					SOAErrorLibraryConstants.DEFAULT_ORGANIZATION,
					modifyListener, false, SOAMessages.TOOLTIP_NAME_ORGANIZATION);
			((GridData) organizationText.getLayoutData()).widthHint = width;
			createContentStructure(container);
			errorLibraryChanged();
			setControl(container);
			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void errorLibraryChanged() {
		populateDomainCombo();
	}
	
	private void setOrganization() {
		String text = "";
		if (StringUtils.isNotBlank(getErrorLibrary())
				&& StringUtils.isNotBlank(getDomain())) {
			try {
				if (getDomain() != null) {
					ISOAErrDomain domain = TurmericErrorRegistry
							.getErrorDomainByName(getDomain());
					if (domain != null && domain.getOrganization() != null) {
						text = domain.getOrganization();
					}
				}
			} catch (Exception e) {
				RuntimeException rte = new RuntimeException(e);
				throw rte;
			}
		}
		this.organizationText.setText(text);
	}

	private void populateDomainCombo() {
		domainCombo.clearSelection();
		if (StringUtils.isNotBlank(getErrorLibrary())) {
			final List<String> names = new ArrayList<String>();
			try {
				names.addAll(TurmericErrorLibraryUtils
						.getAllErrorDomains(WorkspaceUtil
								.getProject(getErrorLibrary())));
			} catch (CoreException e) {
				RuntimeException rte = new RuntimeException(e);
				throw rte;
			}
			domainCombo.setItems(names.toArray(new String[0]));
			if (names.size() > 0) {
				domainCombo.select(0);
			}
		}

		setOrganization();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.wizards.AbstractNewErrorResourceWizardPage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false)
			return false;

		if (StringUtils.isEmpty(getDomain())) {
			updateStatus(domainCombo, SOAMessages.ERROR_NO_DOMAIN);
			return false;
		}
		
		if (StringUtils.isEmpty(getOrganization())) {
			updateStatus(organizationText, StringUtil.formatString(
					SOAMessages.ERROR_MISSING_DOMAIN_ORGANIZATION, getDomain()));
			return false;
		}
		
		try {
			if (TurmericErrorRegistry.containsError(getDomain(), getResourceName())) {
				super.updateStatus(
						getResourceNameText(),
						StringUtil.formatString(SOAMessages.ERROR_DUPLICATE_ERROR_TYPE,
								getResourceName()));
				return false;
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
		
		if (StringUtils.isBlank(getContentErrorMessage())) {
			super.updateStatus(messageText, SOAMessages.ERROR_EMPTY_ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}

	private void createContentStructure(Composite parent) {
		final Group container = new Group(parent, SWT.SHADOW_ETCHED_IN);
		container.setText(SOAMessages.GROUP_TITLE_CONTENT_STRUCTURE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		container.setLayoutData(data);
		GridLayout layout = new GridLayout(4, false);
		container.setLayout(layout);

		final ErrorParamModel model = new ErrorParamModel();

		super.createResourceNameControl(container,
				SOAMessages.TEXT_NAME_NAME, modifyListener, true, 
				SOAMessages.TOOLTIP_NAME_NAME);

		subdomainText = super.createLabelTextField(container,
				SOAMessages.TEXT_NAME_SUBDOMAIN, model.getSubdomain(),
				modifyListener, SOAMessages.TOOLTIP_NAME_SUBDOMAIN);

		messageText = super.createLabelTextField(container,
				SOAMessages.TEXT_NAME_MESSAGE, model.getMessage(),
				modifyListener, true, true, SWT.BORDER | SWT.MULTI | SWT.WRAP, 
				SOAMessages.TOOLTIP_NAME_MESSAGE);

		resolutionText = super.createLabelTextField(container,
				SOAMessages.TEXT_NAME_RESOLUTION, "", modifyListener, 
				SOAMessages.TOOLTIP_NAME_RESOLUTION);

		final int width = 250;
		categoryCombo = super.createCCombo(container,
				SOAMessages.TEXT_NAME_CATEGORY, false,
				SOAErrorLibraryConstants.ERROR_CATEGORIES, 
				SOAMessages.TOOLTIP_NAME_CATEGORY);
		((GridData) categoryCombo.getLayoutData()).widthHint = width;

		severityCombo = super.createCCombo(container,
				SOAMessages.TEXT_NAME_SEVERITY, false,
				SOAErrorLibraryConstants.ERROR_SEVERITY, 
				SOAMessages.TOOLTIP_NAME_SEVERITY);
		severityCombo.setText(model.getSeverity());
		((GridData) severityCombo.getLayoutData()).widthHint = width;

	}

	/**
	 * Gets the error id.
	 *
	 * @return  the Error id as a long
	 * @throws Exception the exception
	 */
	public long getErrorID() throws Exception {
		return ErrorParamModel.generateErrorID(
				ErrorLibraryUtil.getErrorLibraryCentralLocation(),
				getOrganization(), getDomain());
	}

	/**
	 * Gets the domain.
	 *
	 * @return the Domain name
	 */
	public String getDomain() {
		return getTextValue(domainCombo);
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return getTextValue(categoryCombo);
	}

	/**
	 * Gets the organization.
	 *
	 * @return the organizations
	 */
	public String getOrganization() {
		return getTextValue(organizationText);
	}

	/**
	 * Gets the severity.
	 *
	 * @return the severity
	 */
	public String getSeverity() {
		return getTextValue(severityCombo);
	}

	/**
	 * Gets the content error message.
	 *
	 * @return the error message
	 */
	public String getContentErrorMessage() {
		return getTextValue(messageText);
	}

	/**
	 * Gets the sub domain.
	 *
	 * @return the Sub Domain
	 */
	public String getSubDomain() {
		return getTextValue(subdomainText);
	}

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public String getResolution() {
		return getTextValue(resolutionText);
	}

	/**
	 * Gets the error name.
	 *
	 * @return the error name
	 */
	public String getErrorName() {
		return getTextValue(getResourceNameText());
	}
}
