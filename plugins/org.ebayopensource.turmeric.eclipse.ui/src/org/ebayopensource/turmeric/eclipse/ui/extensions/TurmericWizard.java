/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public class TurmericWizard implements ITurmericWizard {

	private List<ITurmericWizardPage> pages = new ArrayList<ITurmericWizardPage>();
	private String type;
	
	public TurmericWizard(IConfigurationElement elem) {
		this.type = elem.getAttribute("type");
		IConfigurationElement[] pelems = elem.getChildren("page");
		for(IConfigurationElement pelem : pelems) {
			
		}
	}
	
	@Override
	public boolean isType(String type) {
		if (this.type == null) {
			return false;
		}
		return this.type.equals(type);
	}

	@Override
	public List<ITurmericWizardPage> allPages() {
		return pages;
	}

	@Override
	public boolean hasPages() {
		return pages.isEmpty();
	}

}
