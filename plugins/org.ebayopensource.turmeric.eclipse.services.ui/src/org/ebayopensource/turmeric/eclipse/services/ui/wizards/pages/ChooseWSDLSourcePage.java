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
 * @author smathew
 * 
 * Wizard for choosing between WSDL from existing or from blank wsdl
 * 
 */
public class ChooseWSDLSourcePage extends AbstractSelectionSourceWizardPage {

	public ChooseWSDLSourcePage() {
		super("Choose WSDL Source", "Choose WSDL Source", 
		"Choose whether to use a previously existing WSDL or start with a template.");
	}

	@Override
	protected String getFirstChoiceName() {
		return "Existing WSDL";
	}

	@Override
	protected String getSecondChoiceName() {
		return "Template WSDL";
	}

	public boolean isStartFromExistingWSDL() {
		return isFirstChoiceSelected();
	}

	public boolean isStartFromNewWSDL() {
		return isSecondChoiceSelected();
	}
	

	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getHelpProvider().getHelpContextID(
				ISOAHelpProvider.PAGE_CHOOSE_WSDL_SOURCE);
	}


}
