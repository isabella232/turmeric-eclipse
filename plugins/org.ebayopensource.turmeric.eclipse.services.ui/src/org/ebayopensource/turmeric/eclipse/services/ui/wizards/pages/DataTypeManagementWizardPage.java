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

import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.ParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.views.registry.TypeSelector;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * The Class DataTypeManagementWizardPage.
 *
 * @author yayu
 */
public class DataTypeManagementWizardPage extends AbstractElementManagementWizardPage {
	
	/**
	 * Instantiates a new data type management wizard page.
	 *
	 * @param title the title
	 * @param elements the elements
	 */
	public DataTypeManagementWizardPage(String title,  
			List<? extends IParameterElement> elements) {
		this(title, elements, 1);
	}
	
	/**
	 * Instantiates a new data type management wizard page.
	 *
	 * @param title the title
	 * @param elements the elements
	 * @param minimumRequiredElementNum the minimum required element num
	 */
	public DataTypeManagementWizardPage(String title,
			List<? extends IParameterElement> elements,
			int minimumRequiredElementNum) {
		super("datatypeManagementWizardPage", title, 
				"manage elements for the given parameter", elements, minimumRequiredElementNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		dialogChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<IParameterElement> getElements() {
		return this.elements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#addPressed()
	 */
	@Override
	protected void addPressed() {
		
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#removePrssed(java.lang.Object)
	 */
	@Override
	protected void removePrssed(Object object) {
		
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#createNewElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected IParameterElement createNewElement(String name, String type) {
		return new ParameterElement(name, type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object openTypeSelectorDialog(Shell shell, LibraryType[] libTypes) {
		TypeSelector typeSelector = new TypeSelector(shell,
				"Select Type",	libTypes,
				"New Project");
		typeSelector.setMultipleSelection(false);
		if (typeSelector.open() == Window.OK)
			return typeSelector.getFirstResult();
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<LibraryType> getSelectedLibraryTypes() throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
		.getAllTypes();
	}

}
