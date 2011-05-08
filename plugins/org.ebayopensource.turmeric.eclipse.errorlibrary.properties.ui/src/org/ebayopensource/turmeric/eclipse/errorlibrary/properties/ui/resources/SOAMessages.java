/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages.
 * 
 * @author yayu
 * 
 */
public class SOAMessages extends NLS {

	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.resources.messages"; //$NON-NLS-1$
	
	//UI texts
	/** The TEX t_ nam e_ erro r_ library. */
	public static String TEXT_NAME_ERROR_LIBRARY;
	
	/** The TEX t_ nam e_ projec t_ nam e_ prefix. */
	public static String TEXT_NAME_PROJECT_NAME_PREFIX ;
	
	/** The TOOLTI p_ nam e_ projec t_ nam e_ prefix. */
	public static String TOOLTIP_NAME_PROJECT_NAME_PREFIX;
	
	/** The TEX t_ nam e_ projec t_ name. */
	public static String TEXT_NAME_PROJECT_NAME;
	
	/** The TOOLTI p_ nam e_ projec t_ name. */
	public static String TOOLTIP_NAME_PROJECT_NAME;
	
	/** The TEX t_ nam e_ conten t_ rep o_ root. */
	public static String TEXT_NAME_CONTENT_REPO_ROOT;
	
	/** The TOOLTI p_ nam e_ conten t_ rep o_ root. */
	public static String TOOLTIP_NAME_CONTENT_REPO_ROOT;
	
	/** The TEX t_ nam e_ locale. */
	public static String TEXT_NAME_LOCALE;
	
	/** The TOOLTI p_ nam e_ locale. */
	public static String TOOLTIP_NAME_LOCALE;
	
	/** The TEX t_ nam e_ version. */
	public static String TEXT_NAME_VERSION;
	
	/** The TOOLTI p_ nam e_ versio n_ errorlib. */
	public static String TOOLTIP_NAME_VERSION_ERRORLIB;
	
	/** The BUTTO n_ nam e_ browse. */
	public static String BUTTON_NAME_BROWSE;
	
	/** The DIALO g_ titl e_ selec t_ erro r_ library. */
	public static String DIALOG_TITLE_SELECT_ERROR_LIBRARY;
	
	/** The TEX t_ nam e_ domai n_ name. */
	public static String TEXT_NAME_DOMAIN_NAME;
	
	/** The PACKAG e_ nam e_ domai n_ name. */
	public static String PACKAGE_NAME_DOMAIN_NAME;
	
	/** The TOOLTI p_ nam e_ domai n_ name. */
	public static String TOOLTIP_NAME_DOMAIN_NAME;
	
	/** The TOOLTI p_ nam e_ packag e_ name. */
	public static String TOOLTIP_NAME_PACKAGE_NAME;
	
	/** The TEX t_ nam e_ domain. */
	public static String TEXT_NAME_DOMAIN;
	
	/** The TOOLTI p_ nam e_ domain. */
	public static String TOOLTIP_NAME_DOMAIN;
	
	/** The TEX t_ nam e_ subdomain. */
	public static String TEXT_NAME_SUBDOMAIN;
	
	/** The TOOLTI p_ nam e_ subdomain. */
	public static String TOOLTIP_NAME_SUBDOMAIN;
	
	/** The TEX t_ nam e_ organization. */
	public static String TEXT_NAME_ORGANIZATION;
	
	/** The TOOLTI p_ nam e_ organization. */
	public static String TOOLTIP_NAME_ORGANIZATION;
	
	/** The GROU p_ titl e_ conten t_ structure. */
	public static String GROUP_TITLE_CONTENT_STRUCTURE;
	
	/** The TEX t_ nam e_ name. */
	public static String TEXT_NAME_NAME;
	
	/** The TOOLTI p_ nam e_ name. */
	public static String TOOLTIP_NAME_NAME;
	
	/** The TEX t_ nam e_ message. */
	public static String TEXT_NAME_MESSAGE;
	
	/** The TOOLTI p_ nam e_ message. */
	public static String TOOLTIP_NAME_MESSAGE;
	
	/** The TEX t_ nam e_ resolution. */
	public static String TEXT_NAME_RESOLUTION;
	
	/** The TOOLTI p_ nam e_ resolution. */
	public static String TOOLTIP_NAME_RESOLUTION;
	
	/** The TEX t_ nam e_ category. */
	public static String TEXT_NAME_CATEGORY;
	
	/** The TOOLTI p_ nam e_ category. */
	public static String TOOLTIP_NAME_CATEGORY;
	
	/** The TEX t_ nam e_ severity. */
	public static String TEXT_NAME_SEVERITY;
	
	/** The TOOLTI p_ nam e_ severity. */
	public static String TOOLTIP_NAME_SEVERITY;
	
	/** The ACTIO n_ tex t_ delete. */
	public static String ACTION_TEXT_DELETE;
	
	/** The ACTIO n_ tex t_ refresh. */
	public static String ACTION_TEXT_REFRESH;
	
	/** The TITL e_ validatio n_ falied. */
	public static String TITLE_VALIDATION_FALIED;
	
	/** The TITL e_ executio n_ falied. */
	public static String TITLE_EXECUTION_FALIED;
	
	//Validation Errors
	/** The ERRO r_ n o_ erro r_ library. */
	public static String ERROR_NO_ERROR_LIBRARY;
	
	/** The ERRO r_ empt y_ domai n_ name. */
	public static String ERROR_EMPTY_DOMAIN_NAME;
	
	/** The ERRO r_ empt y_ domai n_ package. */
	public static String ERROR_EMPTY_DOMAIN_PACKAGE;
	
	/** The ERRO r_ duplicat e_ domai n_ name. */
	public static String ERROR_DUPLICATE_DOMAIN_NAME;
	
	/** The ERRO r_ duplicat e_ erro r_ type. */
	public static String ERROR_DUPLICATE_ERROR_TYPE;
	
	/** The ERRO r_ empt y_ erro r_ message. */
	public static String ERROR_EMPTY_ERROR_MESSAGE;
	
	/** The ERRO r_ n o_ domain. */
	public static String ERROR_NO_DOMAIN;
	
	/** The ERRO r_ nosuppor t_ errorlib. */
	public static String ERROR_NOSUPPORT_ERRORLIB;
	
	/** The ERRO r_ n o_ paren t_ errorlib. */
	public static String ERROR_NO_PARENT_ERRORLIB;
	
	/** The ERRO r_ n o_ errorli b_ i n_ workspace. */
	public static String ERROR_NO_ERRORLIB_IN_WORKSPACE;
	
	/** The ERRO r_ n o_ errordomai n_ folder. */
	public static String ERROR_NO_ERRORDOMAIN_FOLDER;
	
	/** The ERRO r_ missin g_ errordomain. */
	public static String ERROR_MISSING_ERRORDOMAIN;
	
	/** The ERRO r_ missin g_ domai n_ organization. */
	public static String ERROR_MISSING_DOMAIN_ORGANIZATION;
	
	//LOGGING
	/** The LO g_ conten t_ alread y_ exist. */
	public static String LOG_CONTENT_ALREADY_EXIST;
	
	/** The LO g_ conten t_ registr y_ vie w_ no t_ available. */
	public static String LOG_CONTENT_REGISTRY_VIEW_NOT_AVAILABLE;
	
	/** The LO g_ conten t_ contrac t_ alread y_ exist. */
	public static String LOG_CONTENT_CONTRACT_ALREADY_EXIST;
	
	/** The LO g_ conten t_ folde r_ alread y_ exist. */
	public static String LOG_CONTENT_FOLDER_ALREADY_EXIST;
	
	/** The EXCEPTIO n_ invali d_4 c b_ file. */
	public static String EXCEPTION_INVALID_4CB_FILE;
	
	//Property View
	/** The PRO p_ ke y_ name. */
	public static String PROP_KEY_NAME;
	
	/** The PRO p_ ke y_ locale. */
	public static String PROP_KEY_LOCALE;
	
	/** The PRO p_ ke y_ version. */
	public static String PROP_KEY_VERSION;
	
	/** The PRO p_ ke y_ org. */
	public static String PROP_KEY_ORG;
	
	/** The PRO p_ ke y_ packagename. */
	public static String PROP_KEY_PACKAGENAME;
	
	/** The PRO p_ ke y_ erro r_ id. */
	public static String PROP_KEY_ERROR_ID;
	
	/** The PRO p_ ke y_ subdomain. */
	public static String PROP_KEY_SUBDOMAIN;
	
	/** The PRO p_ ke y_ message. */
	public static String PROP_KEY_MESSAGE;
	
	/** The PRO p_ ke y_ resolution. */
	public static String PROP_KEY_RESOLUTION;
	
	/** The PRO p_ ke y_ category. */
	public static String PROP_KEY_CATEGORY;
	
	/** The PRO p_ ke y_ severity. */
	public static String PROP_KEY_SEVERITY;
	
	//Preference page
	/** The PREFERENC e_ desc. */
	public static String PREFERENCE_DESC;
	
	/** The US e_ loca l_ conf. */
	public static String USE_LOCAL_CONF;
	
	/** The US e_ remot e_ host. */
	public static String USE_REMOTE_HOST;
	
	/** The SELEC t_ host. */
	public static String SELECT_HOST;
	
	/** The LOCA l_ con f_ folder. */
	public static String LOCAL_CONF_FOLDER;
	
	/** The REMOT e_ endpoint. */
	public static String REMOTE_ENDPOINT;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
}
