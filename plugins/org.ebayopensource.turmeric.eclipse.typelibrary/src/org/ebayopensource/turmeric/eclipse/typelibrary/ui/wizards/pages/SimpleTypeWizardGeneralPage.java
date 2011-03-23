/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages;

import java.net.URL;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.widgets.Composite;


/**
 * General Page for simple type wizard
 * @author ramurthy
 *
 */
public class SimpleTypeWizardGeneralPage extends AbstractNewTypeWizardPage {
	
	public SimpleTypeWizardGeneralPage(String typeLibName) {
		super("simpleTypeWizardGeneralPage", "Create Simple Type", "Create a new simple type", typeLibName);		
	}
	
	public void createControl(Composite parent) {
		try {
			super.createControl(parent, false);
			createTypeCombo(container, "&Restriction Type:");
			createDocumentationText(container);
			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}
	}
	
	protected Map<String, URL> getTemplateTypes() {
		return getTemplateTypeNames(SOAXSDTemplateSubType.SIMPLE);
	}
}
