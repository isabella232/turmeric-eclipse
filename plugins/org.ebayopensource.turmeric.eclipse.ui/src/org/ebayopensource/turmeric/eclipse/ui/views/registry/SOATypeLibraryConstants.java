/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;

// TODO: Auto-generated Javadoc
/**
 * Type Library related constants goes here. Most of them are used in UI and
 * Models. This is a typical constants class as you see in any soa plugin
 * project.
 * 
 * @author smathew
 * 
 */
public class SOATypeLibraryConstants {

	// The different types of categories possible for Types
	/**
	 * The Enum TypeCategory.
	 */
	public static enum TypeCategory {
		
		/** The SIMPLE. */
		SIMPLE, 
 /** The COMPLEX. */
 COMPLEX, 
 /** The ENUM. */
 ENUM
	}

	/** The Constant FOLDER_GEN_SRC. */
	public static final String FOLDER_GEN_SRC = "gen-src";
	
	/** The Constant FOLDER_GEN_META_SRC. */
	public static final String FOLDER_GEN_META_SRC = "gen-meta-src";
	
	/** The Constant FOLDER_GEN_META_SRC_META_INF. */
	public static final String FOLDER_GEN_META_SRC_META_INF = "gen-meta-src/META-INF";
	
	/** The Constant FOLDER_META_SRC. */
	public static final String FOLDER_META_SRC = "meta-src";
	
	/** The Constant FOLDER_META_SRC_META_INF. */
	public static final String FOLDER_META_SRC_META_INF = "meta-src/META-INF";
	
	/** The Constant FOLDER_META_SRC_TYPES. */
	public static final String FOLDER_META_SRC_TYPES = "meta-src/types";
	
	/** The Constant INFO_DEP_XML_PATH_IN_JAR. */
	public static final String INFO_DEP_XML_PATH_IN_JAR = "META-INF/";

	/** The Constant FILE_TYPE_DEP_XML. */
	public static final String FILE_TYPE_DEP_XML = "TypeDependencies.xml";
	
	/** The Constant FILE_TYPE_INFO_XML. */
	public static final String FILE_TYPE_INFO_XML = "TypeInformation.xml";
	
	/** The Constant DEFAULT_TYPE_LIB_NAME. */
	public static final String DEFAULT_TYPE_LIB_NAME = "NewTypeLibrary";
	
	/** The Constant DEFAULT_TYPE_NAME. */
	public static final String DEFAULT_TYPE_NAME = "NewType";
	
	/** The Constant DEFAULT_TYPE_LIB_VERSION. */
	public static final String DEFAULT_TYPE_LIB_VERSION = "1.0.0";
	
	/** The Constant DEFAULT_TYPE_VERSION. */
	public static final String DEFAULT_TYPE_VERSION = "1.0.0";
	
	/** The Constant DEFAULT_TYPE_DEP_VERSION. */
	public static final String DEFAULT_TYPE_DEP_VERSION = "1.1.1";

	/** The Constant XSD. */
	public static final String XSD = "xsd";
	
	/** The Constant EXT_XSD. */
	public static final String EXT_XSD = "." + XSD;
	
	/** The Constant DIR_TEMPLATES. */
	public static final String DIR_TEMPLATES = "templates";
	
	/** The Constant TEMPLATE_SIMPLE. */
	public static final String TEMPLATE_SIMPLE = "simple.xsd";
	
	/** The Constant TEMPLATE_COMPLEX. */
	public static final String TEMPLATE_COMPLEX = "complex.xsd";
	
	/** The Constant TEMPLATE_ENUM. */
	public static final String TEMPLATE_ENUM = "enum.xsd";

	/** The Constant TEMPLATE_NAME_REPLACE_STRING. */
	public static final String TEMPLATE_NAME_REPLACE_STRING = "TemplateTypeName";
	
	/** The Constant TEMPLATE_VERSION_REPLACE_STRING. */
	public static final String TEMPLATE_VERSION_REPLACE_STRING = "TemplateTypeVersion";

	/** The Constant REGISTRY_VIEW_COLUMN_NAME. */
	public static final String REGISTRY_VIEW_COLUMN_NAME = "Name";
	
	/** The Constant REGISTRY_VIEW_COLUMN_VERSION. */
	public static final String REGISTRY_VIEW_COLUMN_VERSION = "Version";

	/** The Constant STAGING_PROJECT_NAME. */
	public static final String STAGING_PROJECT_NAME = "typelibstagingarea";

	/** The Constant TURMERIC_XSD_FILE_PROTOCOL. */
	public static final String TURMERIC_XSD_FILE_PROTOCOL = "typelib";

	/** The Constant PROTOCOL_DELIMITER_START. */
	public static final String PROTOCOL_DELIMITER_START = "://";

	/** The Constant PROTOCOL_DELIMITER. */
	public static final String PROTOCOL_DELIMITER = "//";

	/** The Constant DND_PREFIX. */
	public static final String DND_PREFIX = "<xs:include schemaLocation=\"";

	/** The Constant DND_SUFFIX. */
	public static final String DND_SUFFIX = "\"></xs:include>";
	
	/** The Constant DND_PREFIX_IMPORT. */
	public static final String DND_PREFIX_IMPORT = "<xs:import schemaLocation=\"";

	/** The Constant DND_SUFFIX_IMPORT. */
	public static final String DND_SUFFIX_IMPORT = "\"></xs:import>";

	/** The Constant TYPES_LOCATION_IN_JAR. */
	public static final String TYPES_LOCATION_IN_JAR = "types";


	/** The Constant DOT_XSD. */
	public static final String DOT_XSD = ".xsd";
	
	/** The Constant TEMPLATE_TYPE_REPLACE_STRING. */
	public static final String TEMPLATE_TYPE_REPLACE_STRING = "string";
	
	/** The Constant TEMPLATE_DOC_REPLACE_STRING. */
	public static final String TEMPLATE_DOC_REPLACE_STRING = "Type Documentation";

	/** The Constant SIMPLE_TYPE_NAME. */
	public static final String SIMPLE_TYPE_NAME = "Simple Type";
	
	/** The Constant COMPLEX_TYPE_NAME. */
	public static final String COMPLEX_TYPE_NAME = "Complex Type";
	
	/** The Constant ENUM_TYPE_NAME. */
	public static final String ENUM_TYPE_NAME = "Enum Type";
	
	/** The Constant COMPLEXSC_TYPE_NAME. */
	public static final String COMPLEXSC_TYPE_NAME = "Complex Type (Simple Content)";
	
	/** The Constant COMPLEXCC_TYPE_NAME. */
	public static final String COMPLEXCC_TYPE_NAME = "Complex Type (Complex Content)";

	/** The Constant SCHEMA_DATA_TYPES. */
	public static final String[] SCHEMA_DATA_TYPES = SOAProjectConstants.DEFAULT_DATA_TYPES;
	
	/** The Constant BOOLEAN. */
	public static final String BOOLEAN = "boolean";
	
	/** The Constant DEFAULT_TNS_PREFIX. */
	public static final String DEFAULT_TNS_PREFIX = "tns";
	
	/** The Constant COLON. */
	public static final String COLON = ":";
	
	/** The Constant W3C_NAMEPSACE. */
	public static final String W3C_NAMEPSACE = "http://www.w3.org/2001/XMLSchema";

	/** The Constant TAG_TYPE_LIB. */
	public static final String TAG_TYPE_LIB = "typeLibrarySource";
	
	/** The Constant ATTR_LIB. */
	public static final String ATTR_LIB = "library";
	
	/** The Constant ATTR_NMSPC. */
	public static final String ATTR_NMSPC = "namespace";

	/** The Constant ATTR_TYPE_INFO_LIBNAME. */
	public static final String ATTR_TYPE_INFO_LIBNAME = "libraryName";

}
