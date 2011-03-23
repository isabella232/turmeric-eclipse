/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.exception;

import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;


/**
 * Generic exception handling logic. In most cases SOA wants to either silently
 * handle an exception( there are exceptions that can be ignored or in some
 * cases SOA knows how to recover from some exceptions) or wants to show it to
 * the user so that user can correct it. In short User errors should be shown to
 * the user, rest all which soa can handle is not shown.
 * 
 * @author smathew
 * 
 */
public class SOAExceptionHandler {

	/**
	 * Default handling is always showing to the user. Clients who ends up in an
	 * exception that cannot be recovered or that needs user attention should
	 * handle the exception this way. Or in a rare scenario in which clients
	 * does not know how to handle it also should use this error handling.
	 * Remember this is the default exception handling.
	 * 
	 * @param throwable-
	 *            the exception that needs the handling.
	 */
	public static void handleException(Throwable throwable) {
		SOALogger.getLogger().error(throwable);
		UIUtil.showErrorDialog(throwable);
	}

	/**
	 * This is special error handling. ONLY clients who are sure an error can be
	 * ignored should use this API. Remember this is not the default handling.
	 * If you are not sure which handling should be used should always use the
	 * above handling. This is not shown to the user and will be just logged.
	 * 
	 * @param throwable -
	 *            the exception that needs the handling.
	 */
	public static void silentHandleException(Throwable throwable) {
		SOALogger.getLogger().error(throwable);
	}
}
