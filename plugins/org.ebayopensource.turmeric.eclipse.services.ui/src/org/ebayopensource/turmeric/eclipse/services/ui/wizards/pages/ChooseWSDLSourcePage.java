/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractSelectionSourceWizardPage;

/**
 * The Class ChooseWSDLSourcePage.
 *
 * @author smathew
 * 
 * Wizard for choosing between WSDL from existing or from blank wsdl
 */
public class ChooseWSDLSourcePage extends AbstractSelectionSourceWizardPage {

	/**
	 * Instantiates a new choose wsdl source page.
	 */
	public ChooseWSDLSourcePage() {
		super("Choose WSDL Source", "Choose WSDL Source", 
		"Choose whether to use a previously existing WSDL or start with a template.");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractSelectionSourceWizardPage#getFirstChoiceName()
	 */
	@Override
	protected String getFirstChoiceName() {
		return "Existing WSDL";
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractSelectionSourceWizardPage#getSecondChoiceName()
	 */
	@Override
	protected String getSecondChoiceName() {
		return "Template WSDL";
	}

	/**
	 * Checks if is start from existing wsdl.
	 *
	 * @return true, if is start from existing wsdl
	 */
	public boolean isStartFromExistingWSDL() {
		return isFirstChoiceSelected();
	}

	/**
	 * Checks if is start from new wsdl.
	 *
	 * @return true, if is start from new wsdl
	 */
	public boolean isStartFromNewWSDL() {
		return isSecondChoiceSelected();
	}
	

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getHelpProvider().getHelpContextID(
				ISOAHelpProvider.PAGE_CHOOSE_WSDL_SOURCE);
	}


}
