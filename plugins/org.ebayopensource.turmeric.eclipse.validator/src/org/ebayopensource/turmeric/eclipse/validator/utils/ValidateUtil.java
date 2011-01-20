/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils;

import org.eclipse.core.runtime.IStatus;

/**
 * @author smathew
 * Caters the util functions common for validators
 */
public class ValidateUtil {

	/**
	 * Formats the message with basic basic linear formatting
	 * Filters out all messages with severity not equal to the 
	 * aupplied severity
	 * @param model
	 * @param severity - the filter level of severity
	 * @return
	 */
	public static String getBasicFormattedUIErrorMessage(
			IStatus model, int severity) {

		StringBuffer formattedMsg = new StringBuffer();
		if (model.isMultiStatus()) {
			for (IStatus error : model.getChildren()) {
				if (error.getSeverity() == severity) {
					formattedMsg.append(error.getMessage());
					formattedMsg.append("\r\n");
				}
			}
		}  else if (model.getSeverity() == severity){
			formattedMsg.append(model.getMessage());
			formattedMsg.append("\r\n");
		}
		return formattedMsg.toString();

	}

	/**
	 * Simply formats the message without any filtering
	 * all erros are returned after formatting
	 * @param model
	 * @return
	 */
	public static String getBasicFormattedUIErrorMessage(
			IStatus model) {

		StringBuffer formattedMsg = new StringBuffer();
		if (model.isMultiStatus()) {
			for (IStatus status : model.getChildren()) {
				formattedMsg.append(status.getMessage());
				formattedMsg.append("\r\n");
			}
		} else {
			formattedMsg.append(model.getMessage());
			formattedMsg.append("\r\n");
		}
		return formattedMsg.toString();

	}
	
	/**
	 * @param status
	 * @return Null is the status is OK, or the error messages otherwise
	 */
	public static String getFormattedStatusMessagesForAction(IStatus status) {
		if (status.isOK() == false) {
			final StringBuffer buf = new StringBuffer();
			if (status.isMultiStatus()) {
				buf.append("Error Details:\n");
				buf.append(status.getMessage());
				buf.append("\n");
				for (final IStatus error : status.getChildren()) {
					buf.append("\n");
					buf.append(error.getMessage());
					buf.append("\n");
				}
			} else {
				buf.append("Errors:\n\n");
				buf.append(status.getMessage());
			}
			return buf.toString().trim();
		}
		return null;
	}

}
