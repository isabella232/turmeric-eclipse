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
package org.ebayopensource.turmeric.eclipse.ui.wizards.pages;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.ServiceImplType;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceReader;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The Class AbstractNewServiceWizardPage.
 *
 * @author yayu
 */
public abstract class AbstractNewServiceWizardPage extends
		AbstractSOAProjectWizardPage {
	// controls
	/** The service package text. */
	protected Text servicePackageText;
	
	/** The service implementation text. */
	protected Text serviceImplementationText;
	
	/** The type namespace text. */
	protected Text typeNamespaceText;
	protected Button typeFoldingButton;
	
	/** The admin name text. */
	protected Text adminNameText;
	
	/** The override admin name button. */
	protected Button overrideAdminNameButton;
	
	/** The override type ns button. */
	protected Button overrideTypeNSButton;
	
	/** The override service package button. */
	protected Button overrideServicePackageButton;
	
	/** The override service implementation button. */
	protected Button overrideServiceImplementationButton;
	private CCombo serviceLayer;
	private CCombo serviceImplType;

	/**
	 * Instantiates a new abstract new service wizard page.
	 *
	 * @param pageName the page name
	 * @param title the title
	 * @param description the description
	 */
	public AbstractNewServiceWizardPage(String pageName, String title,
			String description) {
		super(pageName, title, description);
	}

	/**
	 * Gets the default service impl name.
	 *
	 * @return the default service impl name
	 */
	public String getDefaultServiceImplName() {
		return getDefaultServiceImplName(getDefaultIMplPackageNamePrefix());
	}

	/**
	 * Gets the default service impl name.
	 *
	 * @param packageName the package name
	 * @return the default service impl name
	 */
	public String getDefaultServiceImplName(final String packageName) {

		return SOAServiceUtil.generateServiceImplPackageName(
				getPublicServiceName(), getAdminName(), packageName);
	}

	/**
	 * Gets the default i mpl package name prefix.
	 *
	 * @return the default i mpl package name prefix
	 */
	protected String getDefaultIMplPackageNamePrefix() {
		return "";
	}

	/**
	 * Gets the default service package name.
	 *
	 * @return the default service package name
	 */
	public String getDefaultServicePackageName() {
		return generateServicePackageName(getDefaultServicePackageNamePrefix());
	}
	
	/**
	 * Gets the default service package name prefix.
	 *
	 * @return the default service package name prefix
	 */
	protected String getDefaultServicePackageNamePrefix() {
		return "";
	}

	/**
	 * Gets the default service package name.
	 *
	 * @param packageName the package name
	 * @return the default service package name
	 */
	public String getDefaultServicePackageName(final String packageName) {
		return generateServicePackageName(packageName);
	}

	/**
	 * Generate service package name.
	 *
	 * @param packageName the package name
	 * @return the string
	 */
	public String generateServicePackageName(final String packageName) {
		return SOAServiceUtil.generateServicePackageName(
				getPublicServiceName(), packageName);
	}

	/**
	 * subclass could choose to turn off the validation for service existence
	 * checking.
	 * 
	 * @return true if requires service validation.
	 */
	protected boolean supportServiceValidation() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false)
			return false;

		final ISOARepositorySystem activeRepositorySystem = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem();
		ISOAValidator validator = null;

		IStatus validationModel = null;
		if (getResourceNameText() != null) {
			if (getResourceNameText().getEditable()) {
				final IStatus validServiceNameStatus = JDTUtil
						.validateIdentifier(getResourceName());
				if (checkValidationResult(getResourceNameText(),
						validServiceNameStatus) == false)
					return false;
			}

			if (StringUtils.isNotBlank(getResourceName())
					&& supportServiceValidation()) {
				validator = activeRepositorySystem.getServiceValidator();
				try {
					validationModel = validator.validate(new Path(
							getResourceName()));
				} catch (ValidationInterruptedException e) {
					processException(e);
				}
				if (checkValidationResult(getResourceNameText(),
						validationModel) == false)
					return false;
			}
		}

		if (this.servicePackageText != null) {
			if (StringUtils.isBlank(getServicePackage()) == true) {
				updateStatus("Please specify interface package name.",
						this.servicePackageText);
				return false;
			}
			final IStatus validatePacakgeNameStatus = JDTUtil
					.validatePacakgeName(getServicePackage());
			if (checkValidationResult(this.servicePackageText,
					validatePacakgeNameStatus) == false)
				return false;

			String intfClassName = getServicePackage().replace('.', '/');
			IPath path = new Path(getWorkspaceRoot()).append(getAdminName())
					.append(SOAProjectConstants.FOLDER_GEN_SRC_CLIENT)
					.append(intfClassName)
					.append(getAdminName() + SOAProjectConstants.JAVA_EXT);
			if (path.toString().length() > 230) {
				updateStatus(
						"The calculated interface class path is too long, please make appropriate changes for a shorter path.",
						this.adminNameText, getWorkspaceRootText(),
						this.servicePackageText);
				return false;
			}
		}

		if (this.typeNamespaceText != null
				&& this.typeNamespaceText.isEnabled()
				&& StringUtils.isBlank(getTypeNamespace())) {
			updateStatus(typeNamespaceText, "Type Namespace cannot be empty.");
			return false;
		}

		if (this.serviceImplementationText != null) {
			if (StringUtils.isBlank(getServiceImpl()) == true) {
				updateStatus("Please specify impl class name.",
						this.serviceImplementationText);
				return false;
			}
			final IStatus validImplNameStatus = JDTUtil
					.validateJavaTypeName(getServiceImpl());
			if (checkValidationResult(this.serviceImplementationText,
					validImplNameStatus) == false)
				return false;
		}

		return true;
	}

	/**
	 * this field is disabled by default.
	 *
	 * @param parent the parent
	 * @return the text
	 */
	protected Text addAdminName(final Composite parent) {
		return addAdminName(parent, false);
	}

	/**
	 * Adds the admin name.
	 *
	 * @param composite the composite
	 * @param editable the editable
	 * @return the text
	 */
	protected Text addAdminName(final Composite composite, boolean editable) {
		final String labelText = "&Admin Name:";
		final String tooltip = "the admin or interface project name of the new service";
		final ModifyListener adminNameListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (adminNameText == null || overrideAdminNameButton == null) {
					return;
				}

				if (serviceImplementationText == null
						|| overrideServiceImplementationButton == null
						|| overrideServiceImplementationButton.getSelection() == true) {
					return;
				}

				serviceImplementationText.setText(getDefaultServiceImplName());

			}
		};
		if (editable == false) {
			adminNameText = super.createResourceNameControl(composite,
					labelText, adminNameListener, editable, false, tooltip);
			overrideAdminNameButton = super.createOverrideButton(composite,
					adminNameText, null);
		} else {
			adminNameText = super.createResourceNameControl(composite,
					labelText, adminNameListener, true, tooltip);
		}

		adminNameText.addModifyListener(modifyListener);
		return adminNameText;
	}

	/**
	 * Adds the type namespace.
	 *
	 * @param composite the composite
	 * @return the text
	 */
	protected Text addTypeNamespace(final Composite composite) {
		typeNamespaceText = super.createLabelTextField(composite,
				"Common Type &NS:", getDefaultTypeNamespace(), modifyListener,
				false, false, "common type namespace");
		overrideTypeNSButton = super.createOverrideButton(composite,
				typeNamespaceText, null);
		return typeNamespaceText;
	}

	/**
	 * Adds the type folding.
	 *
	 * @param composite the composite
	 * @return the button
	 */
	protected Button addTypeFolding(final Composite composite) {
		typeFoldingButton = createButton(
				composite,
				"Enable namespace folding. (Check this option only if the WSDL is designed to have a single namespace)",
				"check to enable namespace folding if the WSDL is designed to have a single namespace");
		typeFoldingButton.setSelection(true);// we are making type folding to be
												// checked by default
		typeFoldingButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (typeNamespaceText != null) {
					typeNamespaceText.setEnabled(!typeFoldingButton
							.getSelection());
					dialogChanged();
				}
			}
		});
		return typeFoldingButton;
	}

	/**
	 * Adds the service package.
	 *
	 * @param composite the composite
	 * @return the text
	 */
	protected Text addServicePackage(final Composite composite) {
		servicePackageText = createLabelTextField(composite,
				"Interface &Package:", getDefaultServicePackageName(),
				modifyListener, false, false,
				"the package name of the interface class");
		overrideServicePackageButton = createOverrideButton(composite,
				servicePackageText, null);

		return servicePackageText;
	}

	/**
	 * Adds the service impl.
	 *
	 * @param composite the composite
	 */
	protected void addServiceImpl(final Composite composite) {
		serviceImplementationText = createLabelTextField(composite,
				"Impl &Class:", getDefaultServiceImplName(), modifyListener,
				false, false,
				"the fully qualified class name of the service implementation class");
		overrideServiceImplementationButton = createOverrideButton(composite,
				serviceImplementationText, null);
	}

	/**
	 * add service version line to wizard panel
	 * @param composite
	 * @return
	 */
	protected Text addServiceVersion(final Composite composite) {
		return super.createResourceVersionControl(composite,
				"Service &Version:", modifyListener,
				"the version of the service");
	}

	/**
	 * Adds the service layer.
	 *
	 * @param composite the composite
	 * @return the c combo
	 */
	protected CCombo addServiceLayer(final Composite composite) {

		final Label label = new Label(composite, SWT.NULL);
		label.setText("Service &Layer:");
		serviceLayer = new CCombo(composite, SWT.READ_ONLY | SWT.BORDER);
		serviceLayer.setBackground(UIUtil.display().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		serviceLayer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 3, 1));
		for (final String layer : PreferenceReader.getServiceLayer())
			serviceLayer.add(layer.toString());
		UIUtil.decorateControl(this, serviceLayer,
				"Select a service layer for the new service");
		serviceLayer.select(2);
		return serviceLayer;
	}
	
	/**
	 * create service impl type combo
	 * @param parent
	 */
	protected void createServiceImplTypeCombo(final Composite parent) {
		this.serviceImplType = super.createCCombo(parent, "Service Implementation T&ype:", 
				false, new String[]{"Service Impl", "Service Impl Factory"}, "the implementation type of the new service");
		this.serviceImplType.select(0);
	}
	
	/**
	 * Gets the service implementation type: Class|Factory.
	 *
	 * @return the service impl type
	 */
	public ServiceImplType getServiceImplType() {
		return ServiceImplType.value(this.serviceImplType.getText());
	}

	/**
	 * Gets the service package.
	 *
	 * @return the service package
	 */
	public String getServicePackage() {
		if (overrideServicePackageButton != null)
			return servicePackageText.getText();
		return getDefaultServicePackageName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultResourceName() {
		return "NewService";
	}

	/**
	 * Gets the default type namespace.
	 *
	 * @return the default type namespace
	 */
	public String getDefaultTypeNamespace() {
		return "";
	}

	/**
	 * Gets the service impl.
	 *
	 * @return the service impl
	 */
	protected String getServiceImpl() {
		if (overrideServiceImplementationButton != null)
			return serviceImplementationText.getText();
		if (serviceImplementationText == null
				|| StringUtils.isBlank(serviceImplementationText.getText()))
			return DEFAULT_TEXT_VALUE;
		return getDefaultServiceImplName();
	}

	/**
	 * get admin name from wizard.
	 * @return
	 */
	public String getAdminName() {
		return getTextValue(getResourceNameText());
	}

	/**
	 * Gets the public service name.
	 *
	 * @return the public service name
	 */
	public String getPublicServiceName() {
		return "";
	}

	/**
	 * Gets the service version.
	 *
	 * @return the service version
	 */
	public String getServiceVersion() {
		return getResourceVersion();
	}

	/**
	 * Gets the service layer.
	 *
	 * @return the service layer
	 */
	public String getServiceLayer() {
		String result = getTextValue(serviceLayer);
		if (StringUtils.isBlank(result)) {
			result = SOAProjectConstants.ServiceLayer.BUSINESS.name();
		}
		return result;
	}

	/**
	 * Sets the service implementation.
	 *
	 * @param serviceImpl the new service implementation
	 */
	protected void setServiceImplementation(final String serviceImpl) {
		if (serviceImplementationText != null && serviceImpl != null) {
			serviceImplementationText.setText(serviceImpl);
		}
	}

	/**
	 * Sets the service package.
	 *
	 * @param servicePacakge the new service package
	 */
	protected void setServicePackage(final String servicePacakge) {
		if (servicePackageText != null && servicePacakge != null && !overrideServicePackageButton.getSelection()) {
			servicePackageText.setText(servicePacakge);
		}
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the new service name
	 */
	protected void setServiceName(final String serviceName) {
		if (getResourceNameText() != null && serviceName != null) {
			getResourceNameText().setText(serviceName);
			dialogChanged();
		}
	}

	/**
	 * Sets the type namespace.
	 *
	 * @param typeNamespace the new type namespace
	 */
	protected void setTypeNamespace(final String typeNamespace) {
		if (typeNamespaceText != null && typeNamespace != null) {
			typeNamespaceText.setText(typeNamespace);
		}
	}

	/**
	 * Sets the type folding.
	 *
	 * @param typeFolding the new type folding
	 */
	protected void setTypeFolding(final boolean typeFolding) {
		if (typeFoldingButton != null) {
			typeFoldingButton.setSelection(typeFolding);
		}
	}

	/**
	 * Gets the type namespace.
	 *
	 * @return the type namespace
	 */
	public String getTypeNamespace() {
		return getTextValue(typeNamespaceText);
	}

	/**
	 * Gets the type folding.
	 *
	 * @return the type folding
	 */
	public boolean getTypeFolding() {
		if (typeFoldingButton != null) {
			return typeFoldingButton.getSelection();
		}
		// default is true
		return true;
	}

	/**
	 * Reset service name.
	 */
	public void resetServiceName() {
		if (getResourceNameText() != null)
			getResourceNameText().setText(DEFAULT_TEXT_VALUE);
		if (servicePackageText != null)
			servicePackageText.setText(DEFAULT_TEXT_VALUE);
		if (serviceImplementationText != null)
			serviceImplementationText.setText(DEFAULT_TEXT_VALUE);
	}

	/**
	 * Gets the fully qualified service implementation.
	 *
	 * @return the fully qualified service implementation
	 */
	public String getFullyQualifiedServiceImplementation() {
		return getServiceImpl();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		if (text == this.servicePackageText)
			return getDefaultServicePackageName();
		else if (text == this.serviceImplementationText)
			return getDefaultServiceImplName();
		else if (text == this.typeNamespaceText)
			return getDefaultTypeNamespace();
		else
			return super.getDefaultValue(text);
	}

	protected boolean getDefaultEnabledNSValue() {
		
		return true;
	}
}
