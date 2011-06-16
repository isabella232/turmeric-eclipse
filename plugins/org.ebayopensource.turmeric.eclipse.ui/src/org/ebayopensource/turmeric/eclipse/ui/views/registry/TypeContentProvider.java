/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import java.util.List;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;

/**
 * @author smathew
 * 
 */
public class TypeContentProvider extends ArrayContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SOATypeRegistry) {
			SOATypeRegistry typeRegistry = (SOATypeRegistry) inputElement;
			try {
				List<LibraryType> types = typeRegistry.getAllTypes();
				return types.toArray();
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
		}
		return null;

	}

}
