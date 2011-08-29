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

// TODO: Auto-generated Javadoc
/**
 * The Class ChooseServiceSourceWizardPage.
 *
 * @author yayu
 * @deprecated Creating service from existing Java interface is no longer supported.
 */
public class ChooseServiceSourceWizardPage extends
		AbstractSelectionSourceWizardPage {

	/**
	 * Instantiates a new choose service source wizard page.
	 *
	 */
	public ChooseServiceSourceWizardPage() {
		super("chooseServiceSourceWizardPage", "Choose Service Source", 
				"a good description goes here");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractSelectionSourceWizardPage#getFirstChoiceName()
	 */
	@Override
	protected String getFirstChoiceName() {
		return "New Interface";
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractSelectionSourceWizardPage#getSecondChoiceName()
	 */
	@Override
	protected String getSecondChoiceName() {
		return "Existing Interface";
	}
	
	/**
	 * Checks if is creates the from new interface.
	 *
	 * @return true, if is creates the from new interface
	 */
	public boolean isCreateFromNewInterface() {
		return super.isFirstChoiceSelected();
	}
	
	/**
	 * Checks if is creates the from existing interface.
	 *
	 * @return true, if is creates the from existing interface
	 */
	public boolean isCreateFromExistingInterface() {
		return super.isSecondChoiceSelected();
	}
}
