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

import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel.ParameterElement;
import org.ebayopensource.turmeric.eclipse.typelibrary.registry.TypeSelector;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author yayu
 *
 */
public class DataTypeManagementWizardPage extends AbstractElementManagementWizardPage {
	
	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public DataTypeManagementWizardPage(String title,  
			List<? extends IParameterElement> elements) {
		this(title, elements, 1);
	}
	
	public DataTypeManagementWizardPage(String title,
			List<? extends IParameterElement> elements,
			int minimumRequiredElementNum) {
		super("datatypeManagementWizardPage", title, 
				"manage elements for the given parameter", elements, minimumRequiredElementNum);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		dialogChanged();
	}

	public List<IParameterElement> getElements() {
		return this.elements;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}

	@Override
	protected void addPressed() {
		
	}

	@Override
	protected void removePrssed(Object object) {
		
	}

	@Override
	protected IParameterElement createNewElement(String name, String type) {
		return new ParameterElement(name, type);
	}
	
	protected Object openTypeSelectorDialog(Shell shell, LibraryType[] libTypes) {
		TypeSelector typeSelector = new TypeSelector(shell,
				"Select Type",	libTypes,
				"New Project");
		typeSelector.setMultipleSelection(false);
		if (typeSelector.open() == Window.OK)
			return typeSelector.getFirstResult();
		return null;
	}
	
	protected List<LibraryType> getSelectedLibraryTypes() throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
		.getAllTypes();
	}

}
