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

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * Type Selection Page.
 *
 * @author ramurthy
 */

public class TypeSelectionWizardPage extends SOABasePage {

	private Composite container;
	private List<Button> typeButtons = new ArrayList<Button>();
	private static Map<SOAXSDTemplateSubType, String> templateCategoryMap;


	/**
	 * Instantiates a new type selection wizard page.
	 */
	public TypeSelectionWizardPage() {
		super("typeSelectionWizardPage", "Type Selection", "Select a type");
	}

	/**
	 * Gets the selected type name.
	 *
	 * @return the selected type name
	 */
	public String getSelectedTypeName() {
		for (int i = 0; i < typeButtons.size(); i++) {
			if (typeButtons.get(i).getSelection())
				return typeButtons.get(i).getText();
		}
		return null;
	}
	
	/**
	 * Gets the selected type.
	 *
	 * @return the selected type
	 */
	public Object getSelectedType() {
		for (int i = 0; i < typeButtons.size(); i++) {
			if (typeButtons.get(i).getSelection())
				return typeButtons.get(i).getData();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Checks if is simple type.
	 *
	 * @return true, if is simple type
	 */
	public boolean isSimpleType() {
		return SOAXSDTemplateSubType.SIMPLE.equals(getSelectedType());
	}

	/**
	 * Checks if is enum type.
	 *
	 * @return true, if is enum type
	 */
	public boolean isEnumType() {
		return SOAXSDTemplateSubType.ENUM.equals(getSelectedType());
	}

	/**
	 * Checks if is complex type.
	 *
	 * @return true, if is complex type
	 */
	public boolean isComplexType() {
		return SOAXSDTemplateSubType.COMPLEX.equals(getSelectedType());
	}

	/**
	 * Checks if is complex sc type.
	 *
	 * @return true, if is complex sc type
	 */
	public boolean isComplexSCType() {
		return SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT.equals(getSelectedType());
	}

	/**
	 * Checks if is complex cc type.
	 *
	 * @return true, if is complex cc type
	 */
	public boolean isComplexCCType() {
		return SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT.equals(getSelectedType());
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_SCHEMA_TYPE);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}
}
