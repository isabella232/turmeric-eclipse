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
 * @author smathew This class now serves as a convenience model This has been
 *         introduced as a place holder for future enhancements
 * 
 */
public class BaseStatusModel extends Status {

	/* TODO comment out for backward compatibility with Eclipse v3.2
	 * public BaseStatusModel(int severity, String pluginId, String message,
			Throwable exception) {
		super(severity, pluginId, message, exception);
	}

	public BaseStatusModel(int severity, String pluginId, String message) {
		super(severity, pluginId, message);
	}*/

	public BaseStatusModel(int severity, String pluginId, int code,
			String message, Throwable exception) {
		super(severity, pluginId, code, message, exception);
	}

}
