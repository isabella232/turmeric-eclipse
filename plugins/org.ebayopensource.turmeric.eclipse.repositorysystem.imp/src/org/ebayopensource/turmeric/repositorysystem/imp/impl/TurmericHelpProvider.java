/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;

/**
 * @author yayu
 *
 */
public class TurmericHelpProvider implements ISOAHelpProvider {
	/**
	 * A static instance ofthe TurmericeHelpProvider.
	 */
	static final TurmericHelpProvider INSTANCE = new TurmericHelpProvider();

	/**
	 * 
	 */
	public TurmericHelpProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider#getHelpContextID(int)
	 */
	public String getHelpContextID(int helpID) {
		// TODO Auto-generated method stub
		return "";
	}

}
