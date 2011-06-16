/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * Label Provider for Type Library selector. This provider is designed to hack
 * the unique label restriction posed by the Renderer framework by appending the
 * type library name to the type name
 * 
 * @author smathew
 */
public class TypeSelectorElementRenderer extends LabelProvider {

	@Override
	public String getText(Object element) {
		String retStr = "";
		if (element instanceof LibraryType) {
			LibraryType libType = (LibraryType) element;
			retStr = libType.getName();
			if (libType.getLibraryInfo() != null)
				retStr += " - " + libType.getLibraryInfo().getLibraryName();
		}
		return retStr;
	}
}
