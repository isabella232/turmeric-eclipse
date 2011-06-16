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
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractSelectionSourceWizardPage;

/**
 * @author yayu
 * @deprecated Creating service from existing Java interface is no longer supported.
 */
public class ChooseServiceSourceWizardPage extends
		AbstractSelectionSourceWizardPage {

	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public ChooseServiceSourceWizardPage() {
		super("chooseServiceSourceWizardPage", "Choose Service Source", 
				"a good description goes here");
	}

	@Override
	protected String getFirstChoiceName() {
		return "New Interface";
	}

	@Override
	protected String getSecondChoiceName() {
		return "Existing Interface";
	}
	
	public boolean isCreateFromNewInterface() {
		return super.isFirstChoiceSelected();
	}
	
	public boolean isCreateFromExistingInterface() {
		return super.isSecondChoiceSelected();
	}
}
