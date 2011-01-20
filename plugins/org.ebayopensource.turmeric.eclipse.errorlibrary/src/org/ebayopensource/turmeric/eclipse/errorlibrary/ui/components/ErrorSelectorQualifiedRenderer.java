/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.components;

import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.eclipse.jface.viewers.LabelProvider;


/**
 * @author smathew
 * 
 * The qualified renderer for the error selector
 */
public class ErrorSelectorQualifiedRenderer extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof ISOAError) {
			return ((ISOAError) element).getDomain().getLibrary().getName()
					+ " - " + ((ISOAError) element).getDomain().getName();
		}
		return "";
	}

}
