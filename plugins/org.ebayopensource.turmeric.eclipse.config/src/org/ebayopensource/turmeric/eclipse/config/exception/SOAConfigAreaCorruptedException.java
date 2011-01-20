/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.config.exception;

import org.ebayopensource.turmeric.eclipse.config.ConfigActivator;
import org.ebayopensource.turmeric.eclipse.config.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;


/**
 * This exception is thrown by the framework if the configuration area is
 * corrupted. Typically this will happen only if the user corrupts the area by
 * deleting some of the files or if its made hidden or not accessible. There
 * should not be any errors even if the file is read only because this is just a
 * read operation.
 * 
 * @author smathew
 * 
 */
public class SOAConfigAreaCorruptedException extends AbstractSOAException {

	private static final long serialVersionUID = 1L;
	

	public SOAConfigAreaCorruptedException(Throwable cause) {
		super(cause);
	}

	public SOAConfigAreaCorruptedException(String message) {
		super(StringUtil.formatString(SOAMessages.CORRUPTED_CONFIF, ConfigActivator
						.getDefault().getBundle().getLocation(), message));
	}
}
