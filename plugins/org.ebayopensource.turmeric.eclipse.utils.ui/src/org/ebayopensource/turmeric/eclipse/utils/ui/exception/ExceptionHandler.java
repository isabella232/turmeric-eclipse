package org.ebayopensource.turmeric.eclipse.utils.ui.exception;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;

/**
 * The Class ExceptionHandler.
 */
public class ExceptionHandler {
	
	/**
	 * Default handling is always showing to the user. Clients who ends up in an
	 * exception that cannot be recovered or that needs user attention should
	 * handle the exception this way. Or in a rare scenario in which clients
	 * does not know how to handle it also should use this error handling.
	 * Remember this is the default exception handling.
	 *
	 * @param throwable the throwable
	 */
	public static void handleException(Throwable throwable) {
		SOALogger.getLogger().error(throwable);
		UIUtil.showErrorDialog(throwable);
	}
	

}
