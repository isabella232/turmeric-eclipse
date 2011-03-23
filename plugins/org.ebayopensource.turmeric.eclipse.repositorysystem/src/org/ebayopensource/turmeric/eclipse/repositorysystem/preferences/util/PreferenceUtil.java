/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.util;

import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * @author smathew
 * General utilities for Pref Store
 */
public class PreferenceUtil {

	public static IPreferenceStore getPreferenceStore() {
		return RepositorySystemActivator.getDefault().getPreferenceStore();
	}

}
