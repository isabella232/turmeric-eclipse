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
package org.ebayopensource.turmeric.eclipse.ui;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.ServiceVersionValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 * 
 */
public abstract class AbstractSOAResourceWizardPage extends SOABasePage {
	private Label resourceNameLabel;
	private Text resourceNameText;
	protected ControlDecoration resourceNameControlDecoration;
	protected Text resourceVersionText;

	public AbstractSOAResourceWizardPage(String pageName, String title,
			String description) {
		super(pageName, title, description);
	}

	public AbstractSOAResourceWizardPage(String pageName) {
		super(pageName);
	}
	
	protected Text createResourceNameControl(final Composite composite,
			final String labelText, final ModifyListener modifyListener,
			boolean editable, final String tooltip) {
		return createResourceNameControl(composite, labelText, modifyListener, 
				editable, true, tooltip);
	}

	protected Text createResourceNameControl(final Composite composite,
			final String labelText, final ModifyListener modifyListener,
			boolean editable, final boolean needEmptyLabel, final String tooltip) {
		resourceNameLabel = new Label(composite, SWT.LEFT);
		resourceNameLabel.setText(labelText);
		resourceNameText = new Text(composite, SWT.BORDER);
		resourceNameText.setEditable(editable);
		if (editable == true) {
			//set a limit to the name max length
			resourceNameText.setTextLimit(50);
		}
		resourceNameText.setText(editable ? getDefaultValue(resourceNameText)
				: DEFAULT_TEXT_VALUE);
		resourceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		resourceNameControlDecoration = UIUtil.decorateControl(this, 
				resourceNameText, tooltip);
		if (modifyListener != null)
			resourceNameText.addModifyListener(modifyListener);
		if (needEmptyLabel)
			createEmptyLabel(composite, 1);
		return resourceNameText;
	}

	protected Text createResourceVersionControl(final Composite composite,
			final String labelText, ModifyListener modifyListener, 
			final String tooltip) {
		resourceVersionText = createLabelTextField(composite, labelText, null,
				modifyListener, tooltip);
		resourceVersionText.setTextLimit(50);
		resourceVersionText.setText(getDefaultValue(resourceVersionText));
		return resourceVersionText;
	}

	@Override
	public String getDefaultValue(Text text) {
		if (text == null)
			return DEFAULT_TEXT_VALUE;
		else if (text == this.resourceVersionText)
			return SOAProjectConstants.DEFAULT_VERSION;
		else if (text == this.resourceNameText)
			return getDefaultResourceName();
		return DEFAULT_TEXT_VALUE;
	}

	public abstract String getDefaultResourceName();

	public Text getResourceNameText() {
		return this.resourceNameText;
	}

	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false)
			return false;
		if (this.resourceNameText != null) {
			final String resourceName = getResourceName();
			if (StringUtils.isBlank(resourceName)) {
				String resourceLabel = StringUtils.replaceOnce(
						resourceNameLabel.getText(), "&",
						SOAProjectConstants.EMPTY_STRING);
				if (resourceLabel
						.endsWith(SOAProjectConstants.DELIMITER_SEMICOLON))
					resourceLabel = StringUtils.left(resourceLabel,
							resourceLabel.length() - 1);
				updateStatus(this.resourceNameText, 
						resourceLabel + " must not be empty");
				return false;
			}

			/*final InputObject inputObject = new InputObject(resourceName,
					RegExConstants.PROJECT_NAME_EXP,
					ErrorMessage.PROJECT_NAME_ERRORMSG);

			try {
				IStatus validationModel = NameValidator.getInstance().validate(
						inputObject);
				if (checkValidationResult(validationModel) == false)
					return false;
			} catch (ValidationInterruptedException e) {
				processException(e);
			}*/
			if (validateName(this.resourceNameText, resourceName,
					RegExConstants.PROJECT_NAME_EXP,
					ErrorMessage.PROJECT_NAME_ERRORMSG + 
					" The name [" + resourceName + "] is not valid against the pattern \"" 
					+ RegExConstants.PROJECT_NAME_EXP + "\"") == false) {
				return false;
			}

		}
		if (resourceVersionText != null) {
			if (StringUtils.isBlank(getResourceVersion())) {
				updateStatus(resourceVersionText, "Version must not be empty");
				return false;
			}
			ServiceVersionValidator serviceVersionValidator = ServiceVersionValidator
					.getInstance();

			try {
				IStatus validationModel = serviceVersionValidator
						.validate(getResourceVersion());
				if (checkValidationResult(this.resourceVersionText, 
						validationModel) == false)
					return false;
			} catch (ValidationInterruptedException e) {
				processException(e);
			}

		}
		return true;
	}
	
	protected boolean validateName(Control control, 
			String name, String pattern, String errorMessage) {
		final InputObject inputObject = new InputObject(name,
				pattern, errorMessage);

		try {
			IStatus validationModel = NameValidator.getInstance().validate(
					inputObject);
			if (checkValidationResult(control, validationModel) == false)
				return false;
		} catch (ValidationInterruptedException e) {
			processException(e);
		}
		return true;
	}

	public String getResourceName() {
		return getTextValue(this.resourceNameText);
	}

	public String getResourceVersion() {
		return getTextValue(this.resourceVersionText);
	}

}
