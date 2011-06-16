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
	
	public static String DEFAULT_ORGANIZATION = "Turmeric";
	
	public static final String TAG_ERRORS = "errors";

	public static final String[] ERRORLIB_ORGANIZATION
	= {"Marketplace", "Skype", "Paypal", "AdCommerce"};
	
	public static final String DEFAULT_ERROR_DOMAIN_NAME = "NewErrorDomain";
	public static final String DEFAULT_ERROR_LIBRARY_NAME = "NewErrorLibrary";
	public static final String DEFAULT_ERROR_NAME = "NewError";
	
	public static final String FILE_ERROR_DATA = "ErrorData.xml";
	public static final String FOLDER_TEMPLATES = "templates";
	public static final String FOLDER_ERRORLIBRARY = "errorlibrary";
	public static final String FOLDER_ERROR_DOMAIN_IN_JAR = SOAProjectConstants.FOLDER_META_INF + WorkspaceUtil.PATH_SEPERATOR + 
	FOLDER_ERRORLIBRARY;
	public static final String FOLDER_ERROR_DOMAIN = 
		SOAProjectConstants.FOLDER_META_SRC + WorkspaceUtil.PATH_SEPERATOR + FOLDER_ERROR_DOMAIN_IN_JAR;
	
	public static final String FILE_TEMPLATE = "ErrorDataTemplate.ftl";
	public static final String PATH_TEMPLATE = FOLDER_TEMPLATES + "/" + FILE_TEMPLATE;
	public static final String PROPS_FILE_ERROR_LIBRARY_PROJECT = "domain_list.properties";
	public static final String PROPS_LIST_OF_DOMAINS = "listOfDomains";
	public static final String PROPS_FILE_DEFAULT_ERROR_PROPERTIES = "Errors_{0}.properties";
	public static final String DEFAULT_DOMAIN_PACKAGE = "org.ebayopensource.turmeric.errorlibrary";
	public static final String DEFAULT_LOCALE = "en";
	public static final String PROPS_FILE_NO_LOCALE = "Errors.properties";
	public static final String PROPS_KEY_MESSAGE = "message";
	public static final String PROPS_KEY_RESOLUTION = "resolution";
	
	public static final String IMPORT_CODE_ERROR_CONSTR = "ErrorDataFactory.createErrorData(ErrorConstants.$error,ErrorConstants.ERRORDOMAIN);";;//"ErrorDataProvider.ErrorDataKey NewErrorDataKey = new ErrorDataProvider.ErrorDataKey(\"library\",\"bundle\",\"error\");";
}
