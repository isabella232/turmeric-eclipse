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
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.widgets.Composite;


/**
 * General Page for complex type with simple content wizard.
 *
 * @author ramurthy
 */

public class ComplexTypeSCWizardGeneralPage extends ComplexTypeWizardGeneralPage {

	/**
	 * Instantiates a new complex type sc wizard general page.
	 *
	 * @param typeLibName the type lib name
	 */
	public ComplexTypeSCWizardGeneralPage(String typeLibName) {
		super("complexTypeSCWizardGeneralPage", "Create Complex Type (Simple Content)", "Create a new complex type with simple content", typeLibName);	
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardGeneralPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		try {
			super.createControl(parent, false);
			createTypeCombo(container, "&Extension Type:");	
			createDocumentationText(container);
			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected Map<String, URL> getTemplateTypes() {
		return getTemplateTypeNames(SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT);
	}
}
