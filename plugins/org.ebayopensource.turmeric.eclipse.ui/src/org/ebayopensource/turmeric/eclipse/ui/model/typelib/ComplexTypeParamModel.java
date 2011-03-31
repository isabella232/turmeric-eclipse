/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.model.typelib;

import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardElementPage;

/**
 * UI Model for Complex Type creation. Currently it derives all parameters from 
 * TypeParamModel. In future, additional parameters may be added here.
 * @author ramurthy
 *
 */

public class ComplexTypeParamModel extends TypeParamModel {
	private ComplexTypeWizardElementPage.ElementTableModel[] elementTableModel;

	public ComplexTypeWizardElementPage.ElementTableModel[] getElementTableModel() {
		return elementTableModel;
	}

	public void setElementTableModel(ComplexTypeWizardElementPage.ElementTableModel[] elementTableModel) {
		this.elementTableModel = elementTableModel;
	}
}
