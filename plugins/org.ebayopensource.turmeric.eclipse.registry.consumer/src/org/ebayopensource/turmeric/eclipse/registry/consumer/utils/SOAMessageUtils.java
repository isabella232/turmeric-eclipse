/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.utils;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.registry.consumer.Activator;
import org.ebayopensource.turmeric.eclipse.registry.intf.AssertionStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;


/**
 * SOA Message Utils.
 *
 * @author ramurthy
 */

public class SOAMessageUtils {
	
	/** The Constant UNKNOWN_ERROR. */
	public static final String UNKNOWN_ERROR = "Unknown error";

	/**
	 * Creates the multi status.
	 *
	 * @param pluginID the plugin id
	 * @param code the code
	 * @param children the children
	 * @param description the description
	 * @param throwable the throwable
	 * @return the i status
	 */
	public static IStatus createMultiStatus(String pluginID, int code,
			IStatus[] children, String description, Throwable throwable) {
		if (description == null) {
			description = (throwable == null)
					|| StringUtils.isNotBlank(throwable.getLocalizedMessage()) ? UNKNOWN_ERROR
					: throwable.getLocalizedMessage();
		}
		if (pluginID == null)
			pluginID = Activator.PLUGIN_ID;

		if (children == null || children.length == 0) {
			return new MultiStatus(pluginID, code, description, throwable);
		} else {
			return new MultiStatus(pluginID, code, children, description,
					throwable);
		}
	}
	
	/**
	 * Creates the may assertion status.
	 *
	 * @param message the message
	 * @param exception the exception
	 * @return the status
	 */
	public static Status createMayAssertionStatus(String message, Throwable exception) {
		return new AssertionStatus(message, null);
	}
	
	/**
	 * Creates the status.
	 *
	 * @param severity the severity
	 * @param pluginID the plugin id
	 * @param code the code
	 * @param message the message
	 * @param exception the exception
	 * @param lineNumber the line number
	 * @return the assertion status
	 */
	public static AssertionStatus createStatus(int severity, String pluginID, int code, String message, 
			Throwable exception, int lineNumber) {
		if (lineNumber <= 0) {
			//set line number to 1 if not line number
			lineNumber = 1;
		}
		return new AssertionStatus(severity, pluginID, code, message, exception, lineNumber);
	}
	
}
