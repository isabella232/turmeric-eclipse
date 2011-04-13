/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.extensions;

import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;

public class TurmericProvider implements ITurmericProvider {
	
	private Collection<ITurmericWizard> wizards;
	private IConfigurationElement providerElement;
	
	public TurmericProvider(IConfigurationElement configElement) {
		this.providerElement = configElement;
	}

	@Override
	public String providerName() {
		return providerElement.getAttribute("name");
	}

	@Override
	public boolean hasWizards() {
		return wizards.isEmpty();
	}

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

	@Override
	public boolean hasTypeLibWizard() {
		return findWizardType("Type Library");
	}

	@Override
	public boolean hasErrorLibWizard() {
		return findWizardType("Error Library");
	}

	@Override
	public boolean hasConsumerWSDLWizard() {
		return findWizardType("Consumer WSDL");
	}

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
