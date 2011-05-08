/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.properties;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.eclipse.core.expressions.PropertyTester;


/**
 * The Class SOAPropertyTester.
 *
 * @author smathew
 */
public class SOAPropertyTester extends PropertyTester {
	
	/** The Constant IS_SERVICES_VIEW_ENABLED. */
	public static final String IS_SERVICES_VIEW_ENABLED = "isServicesViewEnabled";
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public boolean test(final Object receiver, final String property,
			final Object[] args, final Object expectedValue) {
		try {
			if (StringUtils.equals(property, IS_SERVICES_VIEW_ENABLED))
				return true;
			
		} catch (Exception exception) {
			logger.error(exception);
		}

		return false;
	}
}
