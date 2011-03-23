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
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * General Page for enum type wizard
 * 
 * @author ramurthy
 * 
 */

public class EnumTypeWizardGeneralPage extends AbstractNewTypeWizardPage {

	public EnumTypeWizardGeneralPage(String typeLibName) {
		super("enumTypeWizardGeneralPage", "Create Enum Type",
				"Create a new enum type", typeLibName);
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
		return getTemplateTypeNames(SOAXSDTemplateSubType.ENUM);
	}

	protected void createTypeCombo(Composite parent, String typeLabel) {
		baseTypeComp = createCCombo(parent, typeLabel, false,
				SOATypeLibraryConstants.SCHEMA_DATA_TYPES, 
				"select an existing type for the new schema type to be based on");
		baseTypeComp.remove(SOATypeLibraryConstants.BOOLEAN);
		baseTypeComp.select(0);
		baseTypeComp.setBackground(UIUtil.display().getSystemColor(
				SWT.COLOR_WHITE));
	}

}
