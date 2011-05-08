/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.model.typelib;

import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.EnumTypeWizardDetailsPage;

/**
 * UI Model for Enum Type creation.
 *
 * @author ramurthy
 */

public class EnumTypeParamModel extends SimpleTypeParamModel {

	private EnumTypeWizardDetailsPage.EnumTableModel[] enumTableModel;

	/**
	 * Gets the enum table model.
	 *
	 * @return the enum table model
	 */
	public EnumTypeWizardDetailsPage.EnumTableModel[] getEnumTableModel() {
		return enumTableModel;
	}

	/**
	 * Sets the enum table model.
	 *
	 * @param enumTableModel the new enum table model
	 */
	public void setEnumTableModel(EnumTypeWizardDetailsPage.EnumTableModel[] enumTableModel) {
		this.enumTableModel = enumTableModel;
	}
}
