/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.eclipse.jface.viewers.LabelProvider;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * The Class TypeSelectorQualifiedRenderer.
 *
 * @author smathew The qualified renderer for the type selector
 */
public class TypeSelectorQualifiedRenderer extends LabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof LibraryType
				&& ((LibraryType) element).getLibraryInfo() != null) {
			return ((LibraryType) element).getLibraryInfo()
					.getLibraryNamespace()
					+ " - "
					+ ((LibraryType) element).getLibraryInfo().getCategory();
		}
		return "";
	}

}
