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

import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 *
 */
public abstract class AbstractSelectionSourceWizardPage extends SOABasePage {
	private Button firstChoice = null;
	private Button secondChoice = null;
	
	/**
	 * @param pageName
	 */
	public AbstractSelectionSourceWizardPage(String pageName, String title, String description) {
		super(pageName);
		setTitle(title);
		setDescription(description);
	}

	/**
	 * {@inheritDoc}
	 */
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		firstChoice = new Button(container, SWT.RADIO);
		firstChoice.setText(getFirstChoiceName());
		firstChoice.setSelection(true);

		secondChoice = new Button(container, SWT.RADIO);
		secondChoice.setText(getSecondChoiceName());
		secondChoice.setSelection(false);

		setControl(container);
		UIUtil.getHelpSystem().setHelp(
						container,
						getHelpContextID());
	}
	
	protected String getFirstChoiceName() {
		return "First Choice";
	}
	
	protected String getSecondChoiceName() {
		return "Second Choice";
	}
	
	public boolean isFirstChoiceSelected() {
		return firstChoice.getSelection();
	}
	
	public boolean isSecondChoiceSelected() {
		return secondChoice.getSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDefaultValue(Text text) {
		return "";
	}
}
