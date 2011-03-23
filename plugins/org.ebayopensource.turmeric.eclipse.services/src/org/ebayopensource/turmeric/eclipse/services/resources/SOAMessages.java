/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author smathew
 * 
 */
public class SOAMessages extends NLS {
private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.services.resources.messages"; //$NON-NLS-1$

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}

	public static String INITIALIZE_ERR;

	public static String SVC_CREATE;;

	public static String SVC_INTF;;
	public static String ERR_SVC_CREATE;

	public static String SVC_CREATE_FAILED;

	public static String NEW_SVC;
	
	public static String NEW_SVC_TITLE;
	public static String NEW_SVC_DESC;
	
	public static String NMSPACE_ERR;
	public static String TEMPLATE_ERR;
	public static String SVCNAME_ERR;
	
	public static String ERR_WRONG_NAMESPACEPART;
	
	public static String TMPLT_WSDL;
	
	public static String WARNING_ADMIN_NAME_MANUAL_OVERRIDE;
	
	public static String ERR_CHANGE_MAJOR_VERSION_NOT_ALLOWED;
	
	public static String ERR_EMPTY_SELECTION;
	public static String ERR_INVALID_PROJECT;
	
	public static String DIFFERENT_SERVICE_VERSION_WITH_WSDL;
	
	//AR Integration
	public static String ASSET_SUMISSION_SUCCEEDED_TITLE;
	public static String ASSET_SUMISSION_SUCCEEDED_MESSAGE;;
	public static String ASSET_SUMISSION_FAILED_TITLE;
	public static String WARNING_MISSING_REGISTRY_PROVIDER;
	public static String WARNING_MISSING_ARTIFACT_VALIDATOR;
	public static String WARNING_MISSING_CLIENT_REGISTRY_PROVIDER;
	public static String WARNING_TYPE_FOLDING_DISABLED_VALIDATION;
	public static String CLIENT_SUMISSION_SUCCEEDED_MESSAGE;
	public static String ERROR_SERVICE_WSDL_VALIDATION_FAILED;
	public static String ERROR_ASSET_REPOSITORY;
	public static String ISSUES_DIALOG_OPENING;
	public static String ISSUES_DIALOG_MUST_FIX;
	public static String ISSUES_DIALOG_SHOULD_FIX;
	public static String ISSUES_DIALOG_MAY_FIX;
	public static String ISSUES_DIALOG_FIX_IN_DETAILS_PAGE;
	public static String ISSUES_DIALOG_OK_OR_CONTINUE;
	public static String ISSUES_DIALOG_COPY_TO_CLIPBOARD;
	
	// Change Service Version dialog
	public static String CHANGE_SERVICE_VERSION_DIALOG_TITLE;
	public static String CHANGE_SERVICE_VERSION_DIALOG_MESSAGE;
	
	
	public static String CHANGE_MAINTENANCE_VERSION_LABEL;
	public static String CHANGE_MAINTENANCE_VERSION_DECORATION;

	public static String CHANGE_MINOR_VERSION_LABEL;
	public static String CHANGE_MINOR_VERSION_DECORATION;


	public static String CHANGE_MAJOR_VERSION_LABEL;
	public static String CHANGE_MAJOR_VERSION_DECORATION;

	public static String EXISTING_SERVICE_VERSION;
	public static String NEW_SERVICE_VERSION;
	
	public static String NEW_MAJOR_SERVICE_VERSION_NOTIFICATION;
	
	public static String NEW_VERSION_SMALLER_ERROR_MESSAGE;
	public static String SHOULD_ONLY_CHANGE_MAINTENANCE_VERSION_ERROR_MESSAGE;
	public static String SHOULD_ONLY_CHANGE_MAINTENANCE_VERSION_OR_MINIOR_VERSION_ERROR_MESSAGE;

	public static String USE_EXTERNAL_SERVICE_FACTORY_IS_TRUE_TITLE;
	public static String USE_EXTERNAL_SERVICE_FACTORY_IS_TRUE_MSG;
	
}
