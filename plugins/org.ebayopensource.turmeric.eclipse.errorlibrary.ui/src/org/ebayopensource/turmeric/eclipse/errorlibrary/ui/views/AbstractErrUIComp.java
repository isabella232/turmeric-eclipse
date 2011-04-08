/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.views;

import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp;

/**
 * @author smathew
 * 
 * This is a tricky class. We have some empty implementation for UI Comp
 * interface. At the same time, We are re abstracting equals and hashcode so
 * that The UI components can be ordered and sorted and comapared.
 * 
 * Implementors are strongly recommended to subclass this class and override
 * equals meaningfully.
 */
public abstract class AbstractErrUIComp implements ISOAErrUIComp {

	public String getName() {
		return "";
	}

	public String getVersion() {
		return "";
	}

	@Override
	public abstract boolean equals(Object object);

	@Override
	public abstract int hashCode();
}
