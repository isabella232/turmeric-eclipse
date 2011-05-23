/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.utils;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;


/**
 * The Class ErrLibComparator.
 *
 * @author smathew
 * 
 * This is being used the in the sort action in the Error View.
 */
public class ErrLibComparator implements Comparator<ISOAError> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(ISOAError o1, ISOAError o2) {
		return StringUtils.defaultString(o1.getName()).compareTo(
				StringUtils.defaultString(o2.getName()));
	}

}
