/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author smathew
 * 
 */
public class SOAMessages extends NLS {
private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.services.ui.messages"; //$NON-NLS-1$

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}

	/** The INITIALIZ e_ err. */
	public static String INITIALIZE_ERR;

	/** The SV c_ create. */
	public static String SVC_CREATE;;

	/** The SV c_ intf. */
	public static String SVC_INTF;;
	
	/** The ER r_ sv c_ create. */
	public static String ERR_SVC_CREATE;

	/** The SV c_ creat e_ failed. */
	public static String SVC_CREATE_FAILED;

	/** The NE w_ svc. */
	public static String NEW_SVC;
	
	/** The NE w_ sv c_ title. */
	public static String NEW_SVC_TITLE;
	
	/** The NE w_ sv c_ desc. */
	public static String NEW_SVC_DESC;
	
	/** The NMSPAC e_ err. */
	public static String NMSPACE_ERR;
	
	/** The TEMPLAT e_ err. */
	public static String TEMPLATE_ERR;
	
	/** The SVCNAM e_ err. */
	public static String SVCNAME_ERR;
	
	/** The ER r_ wron g_ namespacepart. */
	public static String ERR_WRONG_NAMESPACEPART;
	
	/** The TMPL t_ wsdl. */
	public static String TMPLT_WSDL;
	
	/** The WARNIN g_ admi n_ nam e_ manua l_ override. */
	public static String WARNING_ADMIN_NAME_MANUAL_OVERRIDE;
	
	/** The ER r_ chang e_ majo r_ versio n_ no t_ allowed. */
	public static String ERR_CHANGE_MAJOR_VERSION_NOT_ALLOWED;
	
	/** The ER r_ empt y_ selection. */
	public static String ERR_EMPTY_SELECTION;
	
	/** The ER r_ invali d_ project. */
	public static String ERR_INVALID_PROJECT;
	
	/** The DIFFEREN t_ servic e_ versio n_ wit h_ wsdl. */
	public static String DIFFERENT_SERVICE_VERSION_WITH_WSDL;
	
	//AR Integration
	/** The ASSE t_ sumissio n_ succeede d_ title. */
	public static String ASSET_SUMISSION_SUCCEEDED_TITLE;
	
	/** The ASSE t_ sumissio n_ succeede d_ message. */
	public static String ASSET_SUMISSION_SUCCEEDED_MESSAGE;;
	
	/** The ASSE t_ sumissio n_ faile d_ title. */
	public static String ASSET_SUMISSION_FAILED_TITLE;
	
	/** The WARNIN g_ missin g_ registr y_ provider. */
	public static String WARNING_MISSING_REGISTRY_PROVIDER;
	
	/** The WARNIN g_ missin g_ artifac t_ validator. */
	public static String WARNING_MISSING_ARTIFACT_VALIDATOR;
	
	/** The WARNIN g_ missin g_ clien t_ registr y_ provider. */
	public static String WARNING_MISSING_CLIENT_REGISTRY_PROVIDER;
	
	/** The WARNIN g_ typ e_ foldin g_ disable d_ validation. */
	public static String WARNING_TYPE_FOLDING_DISABLED_VALIDATION;
	
	/** The CLIEN t_ sumissio n_ succeede d_ message. */
	public static String CLIENT_SUMISSION_SUCCEEDED_MESSAGE;
	
	/** The ERRO r_ servic e_ wsd l_ validatio n_ failed. */
	public static String ERROR_SERVICE_WSDL_VALIDATION_FAILED;
	
	/** The ERRO r_ asse t_ repository. */
	public static String ERROR_ASSET_REPOSITORY;
	
	/** The ISSUE s_ dialo g_ opening. */
	public static String ISSUES_DIALOG_OPENING;
	
	/** The ISSUE s_ dialo g_ mus t_ fix. */
	public static String ISSUES_DIALOG_MUST_FIX;
	
	/** The ISSUE s_ dialo g_ shoul d_ fix. */
	public static String ISSUES_DIALOG_SHOULD_FIX;
	
	/** The ISSUE s_ dialo g_ ma y_ fix. */
	public static String ISSUES_DIALOG_MAY_FIX;
	
	/** The ISSUE s_ dialo g_ fi x_ i n_ detail s_ page. */
	public static String ISSUES_DIALOG_FIX_IN_DETAILS_PAGE;
	
	/** The ISSUE s_ dialo g_ o k_ o r_ continue. */
	public static String ISSUES_DIALOG_OK_OR_CONTINUE;
	
	/** The ISSUE s_ dialo g_ cop y_ t o_ clipboard. */
	public static String ISSUES_DIALOG_COPY_TO_CLIPBOARD;
	
	// Change Service Version dialog
	/** The CHANG e_ servic e_ versio n_ dialo g_ title. */
	public static String CHANGE_SERVICE_VERSION_DIALOG_TITLE;
	
	/** The CHANG e_ servic e_ versio n_ dialo g_ message. */
	public static String CHANGE_SERVICE_VERSION_DIALOG_MESSAGE;
	
	
	/** The CHANG e_ maintenanc e_ versio n_ label. */
	public static String CHANGE_MAINTENANCE_VERSION_LABEL;
	
	/** The CHANG e_ maintenanc e_ versio n_ decoration. */
	public static String CHANGE_MAINTENANCE_VERSION_DECORATION;

	/** The CHANG e_ mino r_ versio n_ label. */
	public static String CHANGE_MINOR_VERSION_LABEL;
	
	/** The CHANG e_ mino r_ versio n_ decoration. */
	public static String CHANGE_MINOR_VERSION_DECORATION;


	/** The CHANG e_ majo r_ versio n_ label. */
	public static String CHANGE_MAJOR_VERSION_LABEL;
	
	/** The CHANG e_ majo r_ versio n_ decoration. */
	public static String CHANGE_MAJOR_VERSION_DECORATION;

	/** The EXISTIN g_ servic e_ version. */
	public static String EXISTING_SERVICE_VERSION;
	
	/** The NE w_ servic e_ version. */
	public static String NEW_SERVICE_VERSION;
	
	/** The NE w_ majo r_ servic e_ versio n_ notification. */
	public static String NEW_MAJOR_SERVICE_VERSION_NOTIFICATION;
	
	/** The NE w_ versio n_ smalle r_ erro r_ message. */
	public static String NEW_VERSION_SMALLER_ERROR_MESSAGE;
	
	/** The SHOUL d_ onl y_ chang e_ maintenanc e_ versio n_ erro r_ message. */
	public static String SHOULD_ONLY_CHANGE_MAINTENANCE_VERSION_ERROR_MESSAGE;
	
	/** The SHOUL d_ onl y_ chang e_ maintenanc e_ versio n_ o r_ minio r_ versio n_ erro r_ message. */
	public static String SHOULD_ONLY_CHANGE_MAINTENANCE_VERSION_OR_MINIOR_VERSION_ERROR_MESSAGE;

	/** The US e_ externa l_ servic e_ factor y_ i s_ tru e_ title. */
	public static String USE_EXTERNAL_SERVICE_FACTORY_IS_TRUE_TITLE;
	
	/** The US e_ externa l_ servic e_ factor y_ i s_ tru e_ msg. */
	public static String USE_EXTERNAL_SERVICE_FACTORY_IS_TRUE_MSG;
	
}
