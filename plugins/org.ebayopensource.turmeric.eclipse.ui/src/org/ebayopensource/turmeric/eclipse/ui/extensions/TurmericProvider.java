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
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * The provider object from the extension point.
 * 
 * @author dcarver
 *
 */
public class TurmericProvider implements ITurmericProvider {
	
	private Collection<ITurmericWizard> wizards = new ArrayList<ITurmericWizard>();
	private IConfigurationElement providerElement;
	
	/**
	 * Instantiates a new turmeric provider.
	 *
	 * @param configElement provider configuration element
	 */
	public TurmericProvider(IConfigurationElement configElement) {
		this.providerElement = configElement;
		IConfigurationElement[] children = providerElement.getChildren("wizard");

		for(IConfigurationElement wizElm : children) {
			ITurmericWizard wizard = new TurmericWizard(wizElm);
		    wizards.add(wizard);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#providerName()
	 */
	@Override
	public String providerName() {
		return providerElement.getAttribute("name");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#hasWizards()
	 */
	@Override
	public boolean hasWizards() {
		return !wizards.isEmpty();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#hasServiceWizard()
	 */
	@Override
	public boolean hasServiceWizard() {
		return findWizardType("Service");
	}

	private boolean findWizardType(String type) {
		for(ITurmericWizard wizard : wizards) {
			if (wizard.isType(type)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#hasTypeLibWizard()
	 */
	@Override
	public boolean hasTypeLibWizard() {
		return findWizardType("Type Library");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#hasErrorLibWizard()
	 */
	@Override
	public boolean hasErrorLibWizard() {
		return findWizardType("Error Library");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#hasConsumerWSDLWizard()
	 */
	@Override
	public boolean hasConsumerWSDLWizard() {
		return findWizardType("Consumer WSDL");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider#findWizard(java.lang.String)
	 */
	@Override
	public ITurmericWizard findWizard(String type) {
		for(ITurmericWizard wizard : wizards) {
			if (wizard.isType(type)) {
				return wizard;
			}
		}
		return new NullTurmericWizard();
	}

}
