/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * Type Selection Page
 * 
 * @author ramurthy
 * 
 */

public class TypeSelectionWizardPage extends SOABasePage {

	private Composite container;
	private List<Button> typeButtons = new ArrayList<Button>();
	private static Map<SOAXSDTemplateSubType, String> templateCategoryMap;


	public TypeSelectionWizardPage() {
		super("typeSelectionWizardPage", "Type Selection", "Select a type");
	}

	public String getSelectedTypeName() {
		for (int i = 0; i < typeButtons.size(); i++) {
			if (typeButtons.get(i).getSelection())
				return typeButtons.get(i).getText();
		}
		return null;
	}
	
	public Object getSelectedType() {
		for (int i = 0; i < typeButtons.size(); i++) {
			if (typeButtons.get(i).getSelection())
				return typeButtons.get(i).getData();
		}
		return null;
	}

	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		templateCategoryMap = TypeLibraryUtil.getTemplateCategoryMap();
		
		for (SOAXSDTemplateSubType subType: templateCategoryMap.keySet()) {
			Button button = new Button(container, SWT.RADIO);
			button.setText(templateCategoryMap.get(subType));
			button.setData(subType);
			typeButtons.add(button);
			if (subType.equals(SOAXSDTemplateSubType.COMPLEX)) {
				// select complex type as default if it exists
				button.setSelection(true);
			}
		}
		
		setControl(container);
		UIUtil.getHelpSystem().setHelp(container, getHelpContextID());
	}

	public boolean isSimpleType() {
		return SOAXSDTemplateSubType.SIMPLE.equals(getSelectedType());
	}

	public boolean isEnumType() {
		return SOAXSDTemplateSubType.ENUM.equals(getSelectedType());
	}

	public boolean isComplexType() {
		return SOAXSDTemplateSubType.COMPLEX.equals(getSelectedType());
	}

	public boolean isComplexSCType() {
		return SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT.equals(getSelectedType());
	}

	public boolean isComplexCCType() {
		return SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT.equals(getSelectedType());
	}

	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_SCHEMA_TYPE);
	}

	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}
}
