/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.extensions;

import java.util.List;


/**
 * The Interface ITurmericWizard.
 */
public interface ITurmericWizard {

	/**
	 * Checks if is type.
	 *
	 * @param type the type
	 * @return true, if is type
	 */
	public boolean isType(String type);
	
	/**
	 * All pages.
	 *
	 * @return the list
	 */
	public List<ITurmericWizardPage> allPages();
	
	/**
	 * Checks for pages.
	 *
	 * @return true, if successful
	 */
	public boolean hasPages();
	
}