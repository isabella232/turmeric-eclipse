/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * A wizard page object.  The wizard pages are contributed to the existing SOA wizards.
 * 
 * @author dcarver
 *
 */
public class TurmericWizardPage implements ITurmericWizardPage {
	
	private IWizardPage page = null;
	private String id;

	/**
	 * Constructor.
	 * 
	 * @param elem the page element 
	 * @throws InvalidRegistryObjectException no item was found.
	 * @throws CoreException couldn't instantiate the page
	 */
	public TurmericWizardPage(IConfigurationElement elem) throws InvalidRegistryObjectException, CoreException {
		page = (IWizardPage) elem.createExecutableExtension("class");
		id = elem.getAttribute("id");
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizardPage#createWizardPage()
	 */
	@Override
	public IWizardPage createWizardPage() {
		return page;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizardPage#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

}
