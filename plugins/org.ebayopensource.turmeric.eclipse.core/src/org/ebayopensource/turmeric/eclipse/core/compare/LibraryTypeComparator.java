/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.compare;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author smathew
 * 
 */
public class LibraryTypeComparator implements Comparator<LibraryType>,
		Serializable {

	private static final long serialVersionUID = 201L;

	public int compare(LibraryType o1, LibraryType o2) {
		if (o1 == null && o2 == null)
			return 0;
		if (o1 == null && o2 != null)
			return -1;
		if (o2 == null && o1 != null)
			return 1;
		if (StringUtils.equals(o1.getName(), o2.getName())
				&& StringUtils.equals(o1.getNamespace(), o2.getNamespace())) {
			return 0;
		} else {
			if (StringUtils.equals(o1.getName(), o2.getName())) {
				return o1.getNamespace().compareTo(o2.getNamespace());
			} else {
				return o1.getName().compareTo(o2.getName());
			}
		}
	}	

}
