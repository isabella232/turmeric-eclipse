/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.SOAErrorLibraryConstants;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;


/**
 * The Class PropertiesSOAConstants.
 *
 * @author yayu, haozhou
 * 
 * The constants file for properties based error library
 */
public final class PropertiesSOAConstants {

	private PropertiesSOAConstants() {
		super();
	}

	/**
	 * The properties in this map will be hidden in the editor.
	 */
	public static String[] hiddenProperties = { SOAErrorLibraryConstants.ERR_PROP_NID };
	
	/** The DEFAUL t_ organization. */
	public static String DEFAULT_ORGANIZATION = "Turmeric";
	
	/** The Constant TAG_ERRORS. */
	public static final String TAG_ERRORS = "errors";

	/** The Constant ERRORLIB_ORGANIZATION. */
	public static final String[] ERRORLIB_ORGANIZATION
	= {"Marketplace", "Skype", "Paypal", "AdCommerce"};
	
	/** The Constant DEFAULT_ERROR_DOMAIN_NAME. */
	public static final String DEFAULT_ERROR_DOMAIN_NAME = "NewErrorDomain";
	
	/** The Constant DEFAULT_ERROR_LIBRARY_NAME. */
	public static final String DEFAULT_ERROR_LIBRARY_NAME = "NewErrorLibrary";
	
	/** The Constant DEFAULT_ERROR_NAME. */
	public static final String DEFAULT_ERROR_NAME = "NewError";
	
	/** The Constant FILE_ERROR_DATA. */
	public static final String FILE_ERROR_DATA = "ErrorData.xml";
	
	/** The Constant FOLDER_TEMPLATES. */
	public static final String FOLDER_TEMPLATES = "templates";
	
	/** The Constant FOLDER_ERRORLIBRARY. */
	public static final String FOLDER_ERRORLIBRARY = "errorlibrary";
	
	/** The Constant FOLDER_ERROR_DOMAIN_IN_JAR. */
	public static final String FOLDER_ERROR_DOMAIN_IN_JAR = SOAProjectConstants.FOLDER_META_INF + WorkspaceUtil.PATH_SEPERATOR + 
	FOLDER_ERRORLIBRARY;
	
	/** The Constant FOLDER_ERROR_DOMAIN. */
	public static final String FOLDER_ERROR_DOMAIN = 
		SOAProjectConstants.FOLDER_META_SRC + WorkspaceUtil.PATH_SEPERATOR + FOLDER_ERROR_DOMAIN_IN_JAR;
	
	/** The Constant FILE_TEMPLATE. */
	public static final String FILE_TEMPLATE = "ErrorDataTemplate.ftl";
	
	/** The Constant PATH_TEMPLATE. */
	public static final String PATH_TEMPLATE = FOLDER_TEMPLATES + "/" + FILE_TEMPLATE;
	
	/** The Constant PROPS_FILE_ERROR_LIBRARY_PROJECT. */
	public static final String PROPS_FILE_ERROR_LIBRARY_PROJECT = "domain_list.properties";
	
	/** The Constant PROPS_LIST_OF_DOMAINS. */
	public static final String PROPS_LIST_OF_DOMAINS = "listOfDomains";
	
	/** The Constant PROPS_FILE_DEFAULT_ERROR_PROPERTIES. */
	public static final String PROPS_FILE_DEFAULT_ERROR_PROPERTIES = "Errors_{0}.properties";
	
	/** The Constant DEFAULT_DOMAIN_PACKAGE. */
	public static final String DEFAULT_DOMAIN_PACKAGE = "org.ebayopensource.turmeric.errorlibrary";
	
	/** The Constant DEFAULT_LOCALE. */
	public static final String DEFAULT_LOCALE = "en";
	
	/** The Constant PROPS_FILE_NO_LOCALE. */
	public static final String PROPS_FILE_NO_LOCALE = "Errors.properties";
	
	/** The Constant PROPS_KEY_MESSAGE. */
	public static final String PROPS_KEY_MESSAGE = "message";
	
	/** The Constant PROPS_KEY_RESOLUTION. */
	public static final String PROPS_KEY_RESOLUTION = "resolution";
	
	/** The Constant IMPORT_CODE_ERROR_CONSTR. */
	public static final String IMPORT_CODE_ERROR_CONSTR = "ErrorDataFactory.createErrorData(ErrorConstants.$error,ErrorConstants.ERRORDOMAIN);";;//"ErrorDataProvider.ErrorDataKey NewErrorDataKey = new ErrorDataProvider.ErrorDataKey(\"library\",\"bundle\",\"error\");";
}
