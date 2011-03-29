/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;

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
	public static enum TypeCategory {
		SIMPLE, COMPLEX, ENUM
	}

	public static final String FOLDER_GEN_SRC = "gen-src";
	public static final String FOLDER_GEN_META_SRC = "gen-meta-src";
	public static final String FOLDER_GEN_META_SRC_META_INF = "gen-meta-src/META-INF";
	public static final String FOLDER_META_SRC = "meta-src";
	public static final String FOLDER_META_SRC_META_INF = "meta-src/META-INF";
	public static final String FOLDER_META_SRC_TYPES = "meta-src/types";
	public static final String INFO_DEP_XML_PATH_IN_JAR = "META-INF/";

	public static final String FILE_TYPE_DEP_XML = "TypeDependencies.xml";
	public static final String FILE_TYPE_INFO_XML = "TypeInformation.xml";
	public static final String DEFAULT_TYPE_LIB_NAME = "NewTypeLibrary";
	public static final String DEFAULT_TYPE_NAME = "NewType";
	public static final String DEFAULT_TYPE_LIB_VERSION = "1.0.0";
	public static final String DEFAULT_TYPE_VERSION = "1.0.0";
	public static final String DEFAULT_TYPE_DEP_VERSION = "1.1.1";

	public static final String XSD = "xsd";
	public static final String EXT_XSD = "." + XSD;
	public static final String DIR_TEMPLATES = "templates";
	public static final String TEMPLATE_SIMPLE = "simple.xsd";
	public static final String TEMPLATE_COMPLEX = "complex.xsd";
	public static final String TEMPLATE_ENUM = "enum.xsd";

	public static final String TEMPLATE_NAME_REPLACE_STRING = "TemplateTypeName";
	public static final String TEMPLATE_VERSION_REPLACE_STRING = "TemplateTypeVersion";

	public static final String REGISTRY_VIEW_COLUMN_NAME = "Name";
	public static final String REGISTRY_VIEW_COLUMN_VERSION = "Version";

	public static final String STAGING_PROJECT_NAME = "typelibstagingarea";

	public static final String TURMERIC_XSD_FILE_PROTOCOL = "typelib";

	public static final String PROTOCOL_DELIMITER_START = "://";

	public static final String PROTOCOL_DELIMITER = "//";

	public static final String DND_PREFIX = "<xs:include schemaLocation=\"";

	public static final String DND_SUFFIX = "\"></xs:include>";
	
	public static final String DND_PREFIX_IMPORT = "<xs:import schemaLocation=\"";

	public static final String DND_SUFFIX_IMPORT = "\"></xs:import>";

	public static final String TYPES_LOCATION_IN_JAR = "types";


	public static final String DOT_XSD = ".xsd";
	public static final String TEMPLATE_TYPE_REPLACE_STRING = "string";
	public static final String TEMPLATE_DOC_REPLACE_STRING = "Type Documentation";

	public static final String SIMPLE_TYPE_NAME = "Simple Type";
	public static final String COMPLEX_TYPE_NAME = "Complex Type";
	public static final String ENUM_TYPE_NAME = "Enum Type";
	public static final String COMPLEXSC_TYPE_NAME = "Complex Type (Simple Content)";
	public static final String COMPLEXCC_TYPE_NAME = "Complex Type (Complex Content)";

	public static final String[] SCHEMA_DATA_TYPES = SOAProjectConstants.DEFAULT_DATA_TYPES;
	public static final String BOOLEAN = "boolean";
	public static final String DEFAULT_TNS_PREFIX = "tns";
	public static final String COLON = ":";
	public static final String W3C_NAMEPSACE = "http://www.w3.org/2001/XMLSchema";

	public static final String TAG_TYPE_LIB = "typeLibrarySource";
	public static final String ATTR_LIB = "library";
	public static final String ATTR_NMSPC = "namespace";

	public static final String ATTR_TYPE_INFO_LIBNAME = "libraryName";

}
