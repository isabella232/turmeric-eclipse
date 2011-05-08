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

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

/**
 * An implementation of the ITurmericWizard interface for holding information about the
 * provided wizard pages.
 * 
 * @since 1.0.0
 * @author dcarver
 */
public class TurmericWizard implements ITurmericWizard {

	private List<ITurmericWizardPage> pages = new ArrayList<ITurmericWizardPage>();
	private String type;
	
	/**
	 * Constructor.
	 * 
	 * @param elem a configuration element representing the wizard xml entry
	 */
	public TurmericWizard(IConfigurationElement elem) {
		this.type = elem.getAttribute("type");
		IConfigurationElement[] pelems = elem.getChildren("page");
		for(IConfigurationElement pelem : pelems) {
			try {
				pages.add(new TurmericWizardPage(pelem));
			} catch (InvalidRegistryObjectException e) {
				SOALogger.getLogger().error(e);
			} catch (CoreException e) {
				SOALogger.getLogger().error(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard#isType(java.lang.String)
	 */
	@Override
	public boolean isType(String type) {
		if (this.type == null) {
			return false;
		}
		return this.type.equals(type);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard#allPages()
	 */
	@Override
	public List<ITurmericWizardPage> allPages() {
		return pages;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard#hasPages()
	 */
	@Override
	public boolean hasPages() {
		return !pages.isEmpty();
	}

}
