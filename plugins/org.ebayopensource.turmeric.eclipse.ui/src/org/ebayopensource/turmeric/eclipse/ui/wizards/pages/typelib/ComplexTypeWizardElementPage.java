/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.views.registry.TypeSelector;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Element Page for complex type wizard.
 *
 * @author ramurthy
 */

public class ComplexTypeWizardElementPage extends
		AbstractElementManagementWizardPage {

	/**
	 * Instantiates a new complex type wizard element page.
	 *
	 * @param elements the elements
	 */
	public ComplexTypeWizardElementPage(
			List<? extends IParameterElement> elements) {
		super("complexTypeWizardElementPage",
				"Add Element for the Complex Type",
				"Add Element Name, Type, Min Occurs, Max Occurs", elements, 0);
	}

	/**
	 * Gets the element table model.
	 *
	 * @return the element table model
	 */
	public ElementTableModel[] getElementTableModel() {
		return elements.toArray(new ElementTableModel[0]);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		dialogChanged();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#dialogChanged()
	 */
	@Override
	public boolean dialogChanged() {
		if (super.dialogChanged() == false)
			return false;

		boolean addButtonEnabled = true;
		if (getPreviousPage() instanceof ComplexTypeCCWizardGeneralPage) {
			final ComplexTypeCCWizardGeneralPage ccPage = (ComplexTypeCCWizardGeneralPage) getPreviousPage();
			if (ccPage.getRawBaseType() == null
					|| StringUtils.isBlank(ccPage.getRawBaseType().toString())) {
				updatePageStatus(
						null,
						EclipseMessageUtils
								.createStatus(
										"The extension type of the previous page need to be selected in order to add elements",
										IStatus.WARNING));
				addButtonEnabled = false;
			}
		}

		if (super.typesViewer != null) {
			super.typesViewer.setAddButtonsEnabled(addButtonEnabled);

			if (addButtonEnabled == false)
				return false;
		}

		final Set<String> elementNames = new HashSet<String>();
		for (final IParameterElement elementModel : elements) {
			if (elementModel instanceof ElementTableModel) {
				final String elementValue = ((ElementTableModel) elementModel)
						.getElementName();
				final int minOccursValue = ((ElementTableModel) elementModel)
						.getMinOccurs();
				final int maxOccursValue = ((ElementTableModel) elementModel)
						.getMaxOccurs();
				if (!StringUtils.isAlphanumeric(elementValue)) {
					updateStatus(super.typesViewer.getControl(),
							"Element name should be alphanumeric");
					return false;
				}
				if (minOccursValue < 0) {
					updateStatus(super.typesViewer.getControl(),
							"The min occurs must be greater than zero");
					return false;
				}
				if (maxOccursValue != -1) {
					if (maxOccursValue >= 0 && minOccursValue > maxOccursValue) {
						updateStatus(super.typesViewer.getControl(),
								"The min occurs " + minOccursValue
										+ " cannot be greater than max occurs "
										+ maxOccursValue);
						return false;
					}
				}
				if (elementNames.contains(elementValue)) {
					updateStatus(super.typesViewer.getControl(),
							"Duplicate element name - " + elementValue);
					return false;
				} else {
					updateStatus(null);
				}
				elementNames.add(elementValue);
			}
		}
		return true;
	}

	/**
	 * Element Table Model.
	 */
	public static class ElementTableModel implements IParameterElement {

		private String elementName;
		private Object elementType;
		private int minOccurs = 0;
		private int maxOccurs = 1;

		/**
		 * Instantiates a new element table model.
		 *
		 * @param elementName the element name
		 * @param elementType the element type
		 */
		public ElementTableModel(String elementName, String elementType) {
			this.elementName = elementName;
			this.elementType = elementType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getMaxOccurs() {
			return maxOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setMaxOccurs(int maxOccurs) {
			this.maxOccurs = maxOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getMinOccurs() {
			return minOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setMinOccurs(int minOccurs) {
			this.minOccurs = minOccurs;
		}

		/**
		 * Gets the element type.
		 *
		 * @return the element type
		 */
		public String getElementType() {
			if (elementType instanceof LibraryType) {
				return ((LibraryType) elementType).getName();
			} else if (elementType != null) {
				return elementType.toString();
			}
			return null;
		}

		/**
		 * Gets the raw element type.
		 *
		 * @return the raw element type
		 */
		public Object getRawElementType() {
			return elementType;
		}

		/**
		 * Gets the element name.
		 *
		 * @return the element name
		 */
		public String getElementName() {
			return elementName;
		}

		/**
		 * Sets the element type.
		 *
		 * @param elementType the new element type
		 */
		public void setElementType(Object elementType) {
			this.elementType = elementType;
		}

		/**
		 * Sets the element name.
		 *
		 * @param name the new element name
		 */
		public void setElementName(String name) {
			this.elementName = name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getDatatype() {
			return elementType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return elementName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDatatype(Object datatype) {
			setElementType(datatype);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setName(String name) {
			this.elementName = name;
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#getDefaultValue(org.eclipse.swt.widgets.Text)
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
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#createNewElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected IParameterElement createNewElement(String name, String type) {
		ElementTableModel model = new ElementTableModel(name, type);
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object openTypeSelectorDialog(Shell shell, LibraryType[] libTypes) {
		TypeSelector typeSelector = new TypeSelector(shell, "Select Type",
				libTypes, "New Project");
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
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().getAllTypes();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage#removePrssed(java.lang.Object)
	 */
	@Override
	protected void removePrssed(Object object) {

	}
}
