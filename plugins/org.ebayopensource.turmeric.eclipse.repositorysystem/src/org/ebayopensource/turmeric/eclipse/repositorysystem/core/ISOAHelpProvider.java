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

/**
 * @author yayu
 *
 */
public interface ISOAHelpProvider {
	public static final int SOA_HELP_ROOT = 0;
	public static final int SOA_TUTORIAL = -1;
	//Wizard Pages
	//Wizard Pages - Service Creation
	public static final int PAGE_CHOOSE_WSDL_SOURCE = 1;
	public static final int PAGE_CREATE_SERVICE_FROM_EXISTING_WSDL = 2;
	public static final int PAGE_CREATE_SERVICE_FROM_TEMPLATE_WSDL = 3;
	public static final int PAGE_SERVICE_DEPENDENCIES = 4;
	public static final int PAGE_SERVICE_INTERFACE_DEPENDENCIES = 5;
	public static final int PAGE_SERVICE_IMPLEMENTATION_DEPENDENCIES = 6;
	
	//Wizard Pages - Consumer Creation
	public static final int PAGE_CREATE_CONSUMER = 11;
	public static final int PAGE_CREATE_CONSUMER_FROM_WSDL = 12;
	public static final int PAGE_PROBLEM = 13;
	public static final int PAGE_CONSUME_NEW_SERVICE = 14;
	public static final int PAGE_ADD_REMOVE_REQUIRED_SERVICES = 15;
	
	//Wizard Pages - Type Library
	public static final int PAGE_CREATE_TYPE_LIBRARY = 200;
	public static final int PAGE_CREATE_SCHEMA_TYPE = 201;
	public static final int DIALOG_SELECT_TYPE_LIBRARY = 202;
	
	//Dialog Windows
	public static final int WINDOW_DEPENDENCIES = 100;
	public static final int WINDOW_SELECT_PROJECT = 101;
	public static final int WINDOW_SELECT_LIBRARY = 102;
	public static final int WINDOW_SELECT_SERVICE = 103;
	public static final int HELPID_CHECK_MARKETPLACE_COMPLIANCE_ACTION_ID = 104;
	public static final int HELPID_SCHEMA_TYPES_IMPORTEXPORT_WIZARD_MARKETPLACE_ID = 105;
	
	public static final int HELPID_NEW_ERRORERROR_LIBRARY_WIZARD_ID = 106;
	public static final int HELPID_NEW_ERROR_DOMAINERROR_LIBRARY_WIZARD_ID = 107;
	public static final int HELPID_NEW_ERROR_LIBRARY_PROJECT_WIZARD_ID = 108;

	public static final int HELPID_CONSUME_SERVICE_FROM_WSDL = 109;

	
	public static final String HELPID_PREFIX = org.ebayopensource.turmeric.eclipse.core.Activator.PLUGIN_ID_PREFIX + ".help";
	public static final String HELPID_SOAHELP = HELPID_PREFIX + "." + "soahelp";
	
	/**
	 * @param pageID
	 * @return The context help ID for the specified help ID.
	 */
	public String getHelpContextID(int helpID);

}
