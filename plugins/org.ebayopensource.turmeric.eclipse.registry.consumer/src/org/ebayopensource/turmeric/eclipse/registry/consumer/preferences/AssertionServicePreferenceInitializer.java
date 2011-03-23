/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.preferences;

import org.ebayopensource.turmeric.eclipse.registry.consumer.Activator;
import org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway.AssertionsServiceConsumer;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 * @author yayu
 * @since 1.0.0
 */
public class AssertionServicePreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(AssertionServicePreferenceConstants.ENABLE_ASSERTION_SERVICE,
				Boolean.FALSE);
		store.setDefault(AssertionServicePreferenceConstants.OVERWRITE_AS_ENDPOINT, 
				false);
		store.setDefault(AssertionServicePreferenceConstants.URL_AS_ENDPOINT,
				AssertionsServiceConsumer.HOST_URL);
	}
	
	public static String getAssertionServiceEndpoint() {
		return Activator.getDefault().getPreferenceStore().getString(
				AssertionServicePreferenceConstants.URL_AS_ENDPOINT);
	}

}
