/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib;

import java.net.URL;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewTypeWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.widgets.Composite;


/**
 * General Page for complex type wizard.
 *
 * @author ramurthy
 */
public class ComplexTypeWizardGeneralPage extends AbstractNewTypeWizardPage {

	/**
	 * Instantiates a new complex type wizard general page.
	 *
	 * @param typeLibName the type lib name
	 */
	public ComplexTypeWizardGeneralPage(String typeLibName) {
		super("complexTypeWizardGeneralPage", "Create Complex Type", "Create a new complex type", typeLibName);
	}
	
	/**
	 * Instantiates a new complex type wizard general page.
	 *
	 * @param typeLibName the type lib name
	 * @param wizardPageName the wizard page name
	 * @param title the title
	 * @param description the description
	 */
	public ComplexTypeWizardGeneralPage(String typeLibName, String wizardPageName, String title, String description) {
		super(typeLibName, wizardPageName, title, description);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		try {
			super.createControl(parent, false);
			//createTypeCombo(container, "&Extension Type:");	
			createDocumentationText(container);
			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}
	}
	
	/**
	 * Adds the components.
	 */
	protected void addComponents() {
		
	}
	
	/**
	 * Gets the base type.
	 *
	 * @return the base type
	 */
	protected String getBaseType() {
		return "&Extension Type:";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, URL> getTemplateTypes() {
		return getTemplateTypeNames(SOAXSDTemplateSubType.COMPLEX);
	}
}
