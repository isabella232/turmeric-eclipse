/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


/**
 * @author smathew
 * 
 */
public class ErrorLibViewLabelProvider implements ILabelProvider {

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		if (element instanceof ISOAErrUIComp) {
			return ((ISOAErrUIComp) element).getName();
		}
		return SOAProjectConstants.EMPTY_STRING;
	}
}
