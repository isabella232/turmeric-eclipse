/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;

/**
 * The Interface ISOAHelpProvider.
 *
 * @author yayu
 */
public interface ISOAHelpProvider {
	
	/** The Constant SOA_HELP_ROOT. */
	public static final int SOA_HELP_ROOT = 0;
	
	/** The Constant SOA_TUTORIAL. */
	public static final int SOA_TUTORIAL = -1;
	//Wizard Pages
	//Wizard Pages - Service Creation
	/** The Constant PAGE_CHOOSE_WSDL_SOURCE. */
	public static final int PAGE_CHOOSE_WSDL_SOURCE = 1;
	
	/** The Constant PAGE_CREATE_SERVICE_FROM_EXISTING_WSDL. */
	public static final int PAGE_CREATE_SERVICE_FROM_EXISTING_WSDL = 2;
	
	/** The Constant PAGE_CREATE_SERVICE_FROM_TEMPLATE_WSDL. */
	public static final int PAGE_CREATE_SERVICE_FROM_TEMPLATE_WSDL = 3;
	
	/** The Constant PAGE_SERVICE_DEPENDENCIES. */
	public static final int PAGE_SERVICE_DEPENDENCIES = 4;
	
	/** The Constant PAGE_SERVICE_INTERFACE_DEPENDENCIES. */
	public static final int PAGE_SERVICE_INTERFACE_DEPENDENCIES = 5;
	
	/** The Constant PAGE_SERVICE_IMPLEMENTATION_DEPENDENCIES. */
	public static final int PAGE_SERVICE_IMPLEMENTATION_DEPENDENCIES = 6;
	
	//Wizard Pages - Consumer Creation
	/** The Constant PAGE_CREATE_CONSUMER. */
	public static final int PAGE_CREATE_CONSUMER = 11;
	
	/** The Constant PAGE_CREATE_CONSUMER_FROM_WSDL. */
	public static final int PAGE_CREATE_CONSUMER_FROM_WSDL = 12;
	
	/** The Constant PAGE_PROBLEM. */
	public static final int PAGE_PROBLEM = 13;
	
	/** The Constant PAGE_CONSUME_NEW_SERVICE. */
	public static final int PAGE_CONSUME_NEW_SERVICE = 14;
	
	/** The Constant PAGE_ADD_REMOVE_REQUIRED_SERVICES. */
	public static final int PAGE_ADD_REMOVE_REQUIRED_SERVICES = 15;
	
	/** The Constant PAGE_ADDITIONAL_CONFIG. */
	public static final int PAGE_ADDITIONAL_CONFIG = 16;
	
	//Wizard Pages - Type Library
	/** The Constant PAGE_CREATE_TYPE_LIBRARY. */
	public static final int PAGE_CREATE_TYPE_LIBRARY = 200;
	
	/** The Constant PAGE_CREATE_SCHEMA_TYPE. */
	public static final int PAGE_CREATE_SCHEMA_TYPE = 201;
	
	/** The Constant DIALOG_SELECT_TYPE_LIBRARY. */
	public static final int DIALOG_SELECT_TYPE_LIBRARY = 202;
	
	//Dialog Windows
	/** The Constant WINDOW_DEPENDENCIES. */
	public static final int WINDOW_DEPENDENCIES = 100;
	
	/** The Constant WINDOW_SELECT_PROJECT. */
	public static final int WINDOW_SELECT_PROJECT = 101;
	
	/** The Constant WINDOW_SELECT_LIBRARY. */
	public static final int WINDOW_SELECT_LIBRARY = 102;
	
	/** The Constant WINDOW_SELECT_SERVICE. */
	public static final int WINDOW_SELECT_SERVICE = 103;
	
	/** The Constant HELPID_CHECK_MARKETPLACE_COMPLIANCE_ACTION_ID. */
	public static final int HELPID_CHECK_MARKETPLACE_COMPLIANCE_ACTION_ID = 104;
	
	/** The Constant HELPID_SCHEMA_TYPES_IMPORTEXPORT_WIZARD_MARKETPLACE_ID. */
	public static final int HELPID_SCHEMA_TYPES_IMPORTEXPORT_WIZARD_MARKETPLACE_ID = 105;
	
	/** The Constant HELPID_NEW_ERRORERROR_LIBRARY_WIZARD_ID. */
	public static final int HELPID_NEW_ERRORERROR_LIBRARY_WIZARD_ID = 106;
	
	/** The Constant HELPID_NEW_ERROR_DOMAINERROR_LIBRARY_WIZARD_ID. */
	public static final int HELPID_NEW_ERROR_DOMAINERROR_LIBRARY_WIZARD_ID = 107;
	
	/** The Constant HELPID_NEW_ERROR_LIBRARY_PROJECT_WIZARD_ID. */
	public static final int HELPID_NEW_ERROR_LIBRARY_PROJECT_WIZARD_ID = 108;

	/** The Constant HELPID_CONSUME_SERVICE_FROM_WSDL. */
	public static final int HELPID_CONSUME_SERVICE_FROM_WSDL = 109;

	
	/** The Constant HELPID_PREFIX. */
	public static final String HELPID_PREFIX = TurmericCoreActivator.PLUGIN_ID_PREFIX + ".help";
	
	/** The Constant HELPID_SOAHELP. */
	public static final String HELPID_SOAHELP = HELPID_PREFIX + "." + "soahelp";
	
	/**
	 * Gets the help context id.
	 *
	 * @param helpID the help id
	 * @return The context help ID for the specified help ID.
	 */
	public String getHelpContextID(int helpID);

}
