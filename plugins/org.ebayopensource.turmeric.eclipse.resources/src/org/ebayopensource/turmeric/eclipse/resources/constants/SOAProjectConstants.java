/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.osgi.framework.Version;

/**
 * This class holds all the SOA Project and related constants
 * 
 * @author smathew
 */
public class SOAProjectConstants {
	public static final String REQUIRED_LIBRARIES = "requiredLibraries";
    public static final String DELIMITER_COMMA = ",";
    public static final String DELIMITER_SEMICOLON = ":";
    public static final String DELIMITER_PIPE = "|";
    public static final String DELIMITER_DOT = ".";
    public static final String DELIMITER_EQUALS = "=";
    public static final String DELIMITER_URL_SLASH = "/";
    public static final String EMPTY_STRING = "";
    public static final String REQUIRED_PROJECTS = "requiredProjects";
    public static final String REPOSITORY_PATH = "repositoryPath";
    public static final String CLASS_NAME_SEPARATOR = DELIMITER_DOT;
    public static final String IMPL_PROJECT_SUFFIX = "Impl";
    public static final String CLIENT_PROJECT_SUFFIX = "Consumer";
    public static final String SERVICE_CLIENT_SUFFIX = "_Client";
    public static final String CLIENT_NAME_SUFFIX_TEST = "_Test";
    public static final String BASE = "Base";
    public static final String MAJOR_VERSION_PREFIX = "V";
    public static final String MAJOR_VERSION_PREFIX_LOWERCASE = 
    	MAJOR_VERSION_PREFIX.toLowerCase(Locale.US);

    // The different types of creation possible for an intf project
    public static enum ConsumerSourceType {
        JAVA, WSDL
    }

    // The different types of creation possible for an intf project
    public static enum InterfaceSourceType {
        JAVA, WSDL
    }

    // The different types of creation possible for a wsdl project
    public static enum InterfaceWsdlSourceType {
        NEW, EXISTIING
    }

    public static enum SupportedProjectType {
        INTERFACE, IMPL, CONSUMER, TYPE_LIBRARY, ERROR_LIBRARY, UNKNOWN
    }

    public static enum Binding {
        LOCAL, HTTP10, HTTP11;
        public static Binding value(final String name) {
            return valueOf(name.toUpperCase());
        }
    }

    public static enum DataBinding {
        XML, NV, JSON, FAST_INFOSET;
        public static DataBinding value(final String name) {
            return valueOf(name.toUpperCase());
        }
    }

    public static enum ServiceLayer {
        UNKNOWN, COMMON, INTERMEDIATE, BUSINESS;
        public static ServiceLayer value(final String name) {
            return valueOf(name.toUpperCase());
        }

        public static List<ServiceLayer> getAllLayers() {
            return Arrays.asList(ServiceLayer.values());
        }
    }

    public static enum MessageProtocol {
        NONE, SOAP11, SOAP12;
        public static MessageProtocol value(final String name) {
            return valueOf(name.toUpperCase());
        }
    }
    
    public static enum SOAFrameworkLibrary {
    	 SOASERVER, SOACLIENT, SOATOOLS;
    	 public static SOAFrameworkLibrary value(final String name) {
             return valueOf(name.toUpperCase());
         }
    }

    public static final String PROTOCOL_HTTP = "http";

    public static final String DEFAULT_VERSION = "1.0.0";
    public static final String DEFAULT_SERVICE_VERSION = DEFAULT_VERSION;
    
    public static final String GEN = "gen";

    public static final String FOLDER_GEN = GEN;
    public static final String FOLDER_GEN_PREFIX = FOLDER_GEN + "-";
    public static final String FOLDER_SRC = "src";
    public static final String FOLDER_DOT = DELIMITER_DOT;

    public static final String FOLDER_GEN_TEST = FOLDER_GEN_PREFIX + "test";
    public static final String FOLDER_GEN_SRC = FOLDER_GEN_PREFIX + "src";
    public static final String FOLDER_GEN_SRC_CLIENT = FOLDER_GEN_SRC
            + "/client";
    public static final String FOLDER_GEN_SRC_SERVICE = FOLDER_GEN_SRC
            + "/service";
    public static final String FOLDER_GEN_SRC_TEST = FOLDER_GEN_SRC + "/test";
    public static final String FOLDER_GEN_META_SRC = FOLDER_GEN_PREFIX
            + "meta-src";
    public static final String FOLDER_META_INF = "META-INF";
    public static final String GEN_META_SRC_META_INF = FOLDER_GEN_META_SRC
            + "/" + FOLDER_META_INF;
    public static final String FOLDER_WEB_CONTENT = FOLDER_GEN_PREFIX
            + "web-content";
    public static final List<String> FOLDERS_TEST = Arrays.asList(new String[] {
            FOLDER_GEN_SRC_TEST, FOLDER_GEN_TEST });
    // inside the jar wsdl goes inside meta inf
    public static final String META_INF_WSDL = "META-INF/soa/services/wsdl";
    public static final String IMPL_SERVICE_CONFIG_DIR = "META-INF/soa/services/config";
    public static final String IMPL_SERVICE_CONFIG_XML = "ServiceConfig.xml";

    public static final String METADATA_PROPS_LOCATION_JAR = "META-INF/soa/common/config/";

    public static final String FOLDER_META_SRC = "meta-src";

    public static final String META_SRC_META_INF = FOLDER_META_SRC + "/"
            + FOLDER_META_INF;
    public static final String FOLDER_OUTPUT_DIR = "build/classes";
    public static final String FOLDER_GEN_WEB_CONTENT = FOLDER_GEN_PREFIX
            + "web-content";
    public static final String FOLDER_SRC_TEST = FOLDER_GEN_PREFIX + "test";
    public static final String[] SOURCE_DIRECTORIES = { FOLDER_SRC,
            FOLDER_GEN_SRC_TEST, FOLDER_META_SRC };

    public static final String FOLDER_WEB_INF = "WEB-INF";
    public static final String FOLDER_LIB = "lib";
    public static final String FOLDER_CLASSES = "classes";
    public static final String FOLDER_WEBINF_CLASSES = FOLDER_WEB_INF + "/"
            + FOLDER_CLASSES;
    public static final String FOLDER_WEBINF_CLASSES_WITH_PATH_PREFIX = "/"
            + FOLDER_WEBINF_CLASSES;
    public static final String FOLDER_WEBINF_LIB = FOLDER_WEB_INF + "/"
            + FOLDER_LIB;
    public static final String FOLDER_WEBINF_LIB_WITH_PATH_PREFIX = "/"
            + FOLDER_WEBINF_LIB;

    public static final String FILE_EXTENSION_JAR = "jar";

    public static final String WSDL = "wsdl";
    public static final String XSD = "xsd";
    public static final String WSDL_EXT = "." + WSDL;
    public static final String JAR_EXT = "." + FILE_EXTENSION_JAR;
    public static final String WAR_EXT = ".war";
    public static final String XML_EXT = ".xml";
    public static final String JAVA_EXT = ".java";
    public static final String URL_FILE_PREFIX = "file://";

    public static final String CODEGEN_ALL_TYPE_SRC = FOLDER_SRC;
    public static final String CODEGEN_FOLDER_OUTPUT_DIR = FOLDER_OUTPUT_DIR;
    public static final String DEFAULT_BASE_CONSUMER_SOURCE_DIRECTORY = FOLDER_SRC;

    public static final String FILE_PROJECT_XML = "project.xml";
    public static final String FILE_TYPE_MAPPINGS = "TypeMappings.xml";
    public static final String FILE_WEB_XML = "web.xml";
    public static final String FILE_GLOBAL_CLIENT_CONFIG = "GlobalClientConfig.xml";
    public static final String FILE_GLOBAL_SERVICE_CONFIG = "GlobalServiceConfig.xml";
    public static final String DEFAULT_CLIENT_CONFIG_ENVIRONMENT = "production";
    public static final String[] EBAY_POOL_TYPES = {
            DEFAULT_CLIENT_CONFIG_ENVIRONMENT, "staging", "feature", "dev",
            "sandbox" };


    // Props File Constants
    public static final String PROPS_FILE_SERVICE_METADATA = "service_metadata.properties";
    public static final String PROPS_FILE_SERVICE_INTERFACE = "service_intf_project.properties";
    public static final String PROPS_FILE_SERVICE_IMPL = "service_impl_project.properties";
    public static final String PROPS_FILE_SERVICE_CONSUMER = "service_consumer_project.properties";
    public static final String PROPS_FILE_TYPE_LIBRARY = "type_library_project.properties";
    public static final String PROPS_INTF_SOURCE_TYPE = "interface_source_type";
    public static final String PROPS_SERVICE_DOMAIN_NAME = "domainName";
    public static final String PROPS_SERVICE_NAMESPACE_PART = "service_namespace_part";
    public static final String PROPS_KEY_NAMESPACE_TO_PACKAGE = "ns2pkg";
    public static final String PROPS_KEY_TYPE_NAMESPACE = "ctns";
    public static final String PROPS_KEY_TYPE_FOLDING = "enabledNamespaceFolding";
    public static final String PROP_KEY_SERVICE_LAYER = "service_layer";
    public static final String PROP_KEY_SERVICE_VERSION = "service_version";
    public static final String PROP_KEY_ORIGINAL_WSDL_URI = "original_wsdl_uri";
    public static final String PROP_KEY_IMPL_PROJECT_NAME = "impl_project_name";
    public static final String PROP_KEY_SERVICE_NAME = "service_name";
    public static final String PROP_KEY_SERVICE_INTERFACE_CLASS_NAME = "service_interface_class_name";
    public static final String PROP_KEY_ADMIN_NAME = "admin_name";
    public static final String PROPS_INTF_SOURCE_TYPE_WSDL = "WSDL";
    public static final String PROPS_INTF_SOURCE_TYPE_JAVA = "JAVA";
    public static final String PROPS_COMMENTS = "Generated Properties File";
    
    public static final String PROPS_IMPL_BASE_CONSUMER_SRC_DIR = "baseconsumer-srcdir";
    public static final String PROPS_IMPL_BASE_CONSUMER_SRC_DIR_DEFAULT = FOLDER_SRC;
    public static final String PROPS_ENV_MAPPER = "envMapper";
    public static final String PROPS_NOT_GENERATE_BASE_CONSUMER = "not_generate_base_consumer";

    public static final String PROPS_KEY_CONSUMER_SVC_CLIENT_NAME_MAPPING = "service_client_name_mapping";
    public static final String PROPS_KEY_CLIENT_NAME = "client_name";
    public static final String PROPS_KEY_CONSUMER_ID = "consumer_id";
    // The version of service_metadata.properties file
    public static final String PROPS_KEY_SMP_VERSION = "smp_version";
    public static final String PROPS_KEY_SIMP_VERSION = "simp_version";
    // The version of service_intf_project.properties file
    public static final String PROPS_KEY_SIPP_VERSION = "sipp_version";
    // To make sure sharedConsumer path is shortened and existing consumers are also supported, Codegen can rely on a new property (which needs to be put by plugin in service_intf_project.properties in cases path is too long)
    public static final String PROPS_KEY_SHORT_PATH_FOR_SHARED_CONSUMER = "short_path_for_shared_consumer";
    // The version of service_consumer_project.properties file
    public static final String PROPS_KEY_SCPP_VERSION = "scpp_version";
    public static final String PROPS_DEFAULT_PROPERTY_VERSION = "1.1";
    public static final Version DEFAULT_PROPERTY_VERSION = new Version(
            PROPS_DEFAULT_PROPERTY_VERSION);
    public static final String PROPS_DEFAULT_SIPP_VERSION = PROPS_DEFAULT_PROPERTY_VERSION;
    public static final String PROPS_DEFAULT_SCPP_VERSION = PROPS_DEFAULT_PROPERTY_VERSION;
    public static final String PROPS_DEFAULT_SIMP_VERSION = PROPS_DEFAULT_PROPERTY_VERSION;

    // preference properties
    public static final String PROP_REQUIRED_SERVICES = "requiredServices";
    public static final String REQUIRED_SERVICES_DELIMITER = ",";

    // Metadata Props
    public static final String SERVICE_METADATA_PROPS_SERVICE_NAME = PROP_KEY_ADMIN_NAME;
    public static final String SERVICE_METADATA_PROPS_SERVICE_INTERFACE_CLASS_NAME = PROP_KEY_SERVICE_INTERFACE_CLASS_NAME;
    public static final String SERVICE_METADATA_PROPS_SERVICE_IMPLEMENTATION_CLASS_NAME = "service_implementation_class_name";
    public static final String SERVICE_METADATA_PROPS_SERVICE_LAYER = PROP_KEY_SERVICE_LAYER;
    public static final String SERVICE_METADATA_PROPS_SERVICE_VERSION = PROP_KEY_SERVICE_VERSION;

    // Property Page
    public static final String PROP_PAGE_ID_SOA_PROJ = "org.ebayopensource.turmeric.eclipse.services.ui.properties.soaProjectPropertyPage";
    // mzang 2010-4-20 id for property page.
    public static final String PROP_PAGE_ID_TYPELIBRARA_PROJ = "org.ebayopensource.turmeric.eclipse.services.ui.properties.typelibraryProjectPropertyPage";

    public static final String TEMP_PREFIX = "__temp_soa_";

    public static final String BUILD = "build";

    /**
     * The ID for the SOA library classpath container
     */
    public static final String SOA_CLASSPATH_CONTAINER_ID = "org.ebayopensource.turmeric.eclipse.build.classpath.SOALibrary";
    

    public static final String[] DEFAULT_DATA_TYPES;

    public static final String XML_NAMESPACE_2001 = "http://www.w3.org/2001/XMLSchema";

    
    static {
        final List<String> types = new ArrayList<String>();
        types.add("string");
        types.add("boolean");
        types.add("byte");
        types.add("date");
        types.add("dateTime");
        types.add("decimal");
        types.add("float");
        types.add("double");
        types.add("duration");
        types.add("int");
        types.add("integer");
        types.add("long");
        types.add("short");
        types.add("time");
        types.add("anySimpleType");
        types.add("anyType");
        types.add("anyURI");
        types.add("language");
        types.add("ENTITY");
        types.add("ENTITIES");
        types.add("gDay");
        types.add("gMonth");
        types.add("gMonthDay");
        types.add("gYear");
        types.add("gYearMonth");
        types.add("base64Binary");
        types.add("hexBinary");
        types.add("ID");
        types.add("IDREF");
        types.add("IDREFS");
        types.add("NMTOKEN");
        types.add("NMTOKENS");
        types.add("nonNegativeInteger");
        types.add("nonPositiveInteger");
        types.add("negativeInteger");
        types.add("positiveInteger");
        types.add("normalizedString");
        types.add("unsignedByte");
        types.add("unsignedInt");
        types.add("unsignedLong");
        types.add("unsignedShort");
        types.add("NOTATION");
        types.add("Name");
        types.add("NCName");
        types.add("QName");
        types.add("token");

        DEFAULT_DATA_TYPES = types.toArray(new String[0]);
    }

    public static final String PARAMETER_INPUT_SUFFIX = "Request";
    public static final String PARAMETER_OUTPUT_SUFFIX = "Response";

    public static enum TemplateBinding {
        HTTP, SOAP;

        public static TemplateBinding value(final String name) {
            return valueOf(name.toUpperCase());
        }

        public static List<TemplateBinding> getAllBindings() {
            return Arrays.asList(TemplateBinding.values());
        }

        public static List<String> getAllBindingNames() {
            final List<String> result = new ArrayList<String>();
            for (TemplateBinding binding : TemplateBinding.values()) {
                result.add(binding.name());
            }
            return result;
        }
    }

}
