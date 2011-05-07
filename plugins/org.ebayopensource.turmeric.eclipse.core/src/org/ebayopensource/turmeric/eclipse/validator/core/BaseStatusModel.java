/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.core;

import org.eclipse.core.runtime.Status;

/**
 * The Class BaseStatusModel.
 *
 * @author smathew This class now serves as a convenience model This has been
 * introduced as a place holder for future enhancements
 */
public class BaseStatusModel extends Status {

	/**
	 * Instantiates a new base status model.
	 *
	 * @param severity the severity
	 * @param pluginId the plugin id
	 * @param code the code
	 * @param message the message
	 * @param exception the exception
	 */
	public BaseStatusModel(int severity, String pluginId, int code,
			String message, Throwable exception) {
		super(severity, pluginId, code, message, exception);
	}

}
