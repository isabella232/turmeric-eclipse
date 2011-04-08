/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.views;

import java.util.Collections;

import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrRegistry;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author smathew
 * 
 */
public class ErrorLibViewContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		// this is the root.
		if (parentElement instanceof ISOAErrLibrary) {
			final ISOAErrLibrary parentNode = (ISOAErrLibrary) parentElement;
			// this is second level
			if (parentNode.getDomains() != null) {
				return parentNode.getDomains().toArray();
			}
		} else if (parentElement instanceof ISOAErrDomain) {
			final ISOAErrDomain parentNode = (ISOAErrDomain) parentElement;
			// this is second level
			if (parentNode.getErrors() != null) {
				return parentNode.getErrors().toArray();
			}
		}
		return Collections.EMPTY_LIST.toArray();
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return element instanceof ISOAErrLibrary || element instanceof ISOAErrDomain;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ISOAErrRegistry) {
			final ISOAErrRegistry registry = (ISOAErrRegistry)inputElement;
			return registry.getLibraries().toArray();
		}
		return Collections.EMPTY_LIST.toArray();
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
