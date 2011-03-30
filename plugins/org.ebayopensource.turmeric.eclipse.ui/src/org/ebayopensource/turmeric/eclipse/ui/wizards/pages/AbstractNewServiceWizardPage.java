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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceReader;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author yayu
 * 
 */
public abstract class AbstractNewServiceWizardPage extends
		AbstractSOAProjectWizardPage {
	// controls
	protected Text servicePackageText;
	protected Text serviceImplementationText;
	protected Text typeNamespaceText;
	private Button typeFoldingButton;
	private Text baseConsumerSrcText;
	protected Text adminNameText;
	protected Button overrideAdminNameButton;
	protected Button overrideTypeNSButton;
	protected Button overrideServicePackageButton;
	protected Button overrideServiceImplementationButton;
	private CCombo serviceLayer;

	/**
	 * @param pageName
	 */
	public AbstractNewServiceWizardPage(String pageName, String title,
			String description) {
		super(pageName, title, description);
	}

	public String getDefaultServiceImplName() {
		return getDefaultServiceImplName(getDefaultIMplPackageNamePrefix());
	}

	public String getDefaultServiceImplName(final String packageName) {

		return SOAServiceUtil.generateServiceImplPackageName(
				getPublicServiceName(), getAdminName(), packageName);
	}

	protected String getDefaultIMplPackageNamePrefix() {
		return "";
	}

	public String getDefaultServicePackageName() {
		return generateServicePackageName(getDefaultServicePackageNamePrefix());
	}

	protected String getDefaultServicePackageNamePrefix() {
		return "";
	}

	public String getDefaultServicePackageName(final String packageName) {
		return generateServicePackageName(packageName);
	}

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

		if (getBaseConsumerSrcControl() != null) {
			if (StringUtils.isBlank(getBaseConsumerSrcDir())) {
				updateStatus(getBaseConsumerSrcControl(),
						"BaseConsumer Source Dir cannot be empty.");
				return false;
			}
			IStatus valModel = null;
			try {
				valModel = NameValidator.getInstance().validate(
						getBaseConsumerSrcDir());
			} catch (ValidationInterruptedException e) {
				processException(e);
			}
			if (checkValidationResult(getBaseConsumerSrcControl(), valModel) == false)
				return false;

			final IPath baseConsumerSrcPath = new Path(getBaseConsumerSrcDir());
			for (final String srcDir : SOAProjectConstants.SOURCE_DIRECTORIES) {
				final IPath srcPath = new Path(srcDir);
				if (srcPath.isPrefixOf(baseConsumerSrcPath)
						&& !ObjectUtils.equals(srcPath, baseConsumerSrcPath)) {
					updateStatus(getBaseConsumerSrcControl(),
							"BaseConsumer Source Dir: " + baseConsumerSrcPath
									+ " is a subdirectory of " + srcPath);
					return false;
				}
				if (baseConsumerSrcPath.isPrefixOf(srcPath)
						&& !ObjectUtils.equals(srcPath, baseConsumerSrcPath)) {
					updateStatus(getBaseConsumerSrcControl(),
							"BaseConsumer Source Dir: " + baseConsumerSrcPath
									+ " is a subdirectory of " + srcPath);
					return false;
				}
			}
		}

		return true;
	}

	protected Control getBaseConsumerSrcControl() {
		return this.baseConsumerSrcText;
	}

	/**
	 * this field is disabled by default
	 * 
	 * @param parent
	 * @return
	 */
	protected Text addAdminName(final Composite parent) {
		return addAdminName(parent, false);
	}

	protected Text addAdminName(final Composite composite, boolean editable) {
		final String labelText = "&Admin Name:";
		final String tooltip = "the admin or interface project name of the new service";
		final ModifyListener adminNameListener = new ModifyListener() {
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

	protected Text addTypeNamespace(final Composite composite) {
		typeNamespaceText = super.createLabelTextField(composite,
				"Common Type &NS:", getDefaultTypeNamespace(), modifyListener,
				false, false, "common type namespace");
		overrideTypeNSButton = super.createOverrideButton(composite,
				typeNamespaceText, null);
		return typeNamespaceText;
	}

	protected Button addTypeFolding(final Composite composite) {
		typeFoldingButton = createButton(
				composite,
				"Enable namespace folding. (Check this option only if the WSDL is designed to have a single namespace)",
				"check to enable namespace folding if the WSDL is designed to have a single namespace");
		typeFoldingButton.setSelection(true);// we are making type folding to be
												// checked by default
		typeFoldingButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

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

	protected Text addServicePackage(final Composite composite) {
		servicePackageText = createLabelTextField(composite,
				"Interface &Package:", getDefaultServicePackageName(),
				modifyListener, false, false,
				"the package name of the interface class");
		overrideServicePackageButton = createOverrideButton(composite,
				servicePackageText, null);

		return servicePackageText;
	}

	protected void addServiceImpl(final Composite composite) {
		serviceImplementationText = createLabelTextField(composite,
				"Impl &Class:", getDefaultServiceImplName(), modifyListener,
				false, false,
				"the fully qualified class name of the service implementation class");
		overrideServiceImplementationButton = createOverrideButton(composite,
				serviceImplementationText, null);
	}

	protected Text createBaseConsumerSource(final Composite parent) {
		baseConsumerSrcText = super.createLabelTextField(parent,
				"BaseCons&umer Source Dir:", SOAProjectConstants.FOLDER_SRC,
				modifyListener,
				"the source directory of the base consumer class");
		return baseConsumerSrcText;
	}

	protected Text addServiceVersion(final Composite composite) {
		return super.createResourceVersionControl(composite,
				"Service &Version:", modifyListener,
				"the version of the service");
	}

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

	public String getServicePackage() {
		if (overrideServicePackageButton != null)
			return servicePackageText.getText();
		return getDefaultServicePackageName();
	}

	public String getDefaultResourceName() {
		return "NewService";
	}

	public String getDefaultTypeNamespace() {
		return "";
	}

	protected String getServiceImpl() {
		if (overrideServiceImplementationButton != null)
			return serviceImplementationText.getText();
		if (serviceImplementationText == null
				|| StringUtils.isBlank(serviceImplementationText.getText()))
			return DEFAULT_TEXT_VALUE;
		return getDefaultServiceImplName();
	}

	public String getBaseConsumerSrcDir() {
		return getTextValue(baseConsumerSrcText);
	}

	public String getAdminName() {
		return getTextValue(getResourceNameText());
	}

	public String getPublicServiceName() {
		return "";
	}

	public String getServiceVersion() {
		return getResourceVersion();
	}

	public String getServiceLayer() {
		return getTextValue(serviceLayer);
	}

	protected void setServiceImplementation(final String serviceImpl) {
		if (serviceImplementationText != null && serviceImpl != null) {
			serviceImplementationText.setText(serviceImpl);
		}
	}

	protected void setServicePackage(final String servicePacakge) {
		if (servicePackageText != null && servicePacakge != null) {
			servicePackageText.setText(servicePacakge);
		}
	}

	protected void setServiceName(final String serviceName) {
		if (getResourceNameText() != null && serviceName != null) {
			getResourceNameText().setText(serviceName);
			dialogChanged();
		}
	}

	protected void setBaseConsumerDir(final String baseConsumerDir) {
		if (baseConsumerSrcText != null && baseConsumerDir != null) {
			baseConsumerSrcText.setText(baseConsumerDir);
		}
	}

	protected void setTypeNamespace(final String typeNamespace) {
		if (typeNamespaceText != null && typeNamespace != null) {
			typeNamespaceText.setText(typeNamespace);
		}
	}

	protected void setTypeFolding(final boolean typeFolding) {
		if (typeFoldingButton != null) {
			typeFoldingButton.setSelection(typeFolding);
		}
	}

	protected void setBaseConsumerDirTextEditable(final boolean editable) {
		if (baseConsumerSrcText != null)
			baseConsumerSrcText.setEditable(editable);
	}

	public String getTypeNamespace() {
		return getTextValue(typeNamespaceText);
	}

	public boolean getTypeFolding() {
		if (typeFoldingButton != null) {
			return typeFoldingButton.getSelection();
		}
		// default is true
		return true;
	}

	public void resetServiceName() {
		if (getResourceNameText() != null)
			getResourceNameText().setText(DEFAULT_TEXT_VALUE);
		if (servicePackageText != null)
			servicePackageText.setText(DEFAULT_TEXT_VALUE);
		if (serviceImplementationText != null)
			serviceImplementationText.setText(DEFAULT_TEXT_VALUE);
	}

	public String getFullyQualifiedServiceImplementation() {
		return getServiceImpl();
	}

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
}
