/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.resources.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Version;

/**
 * This class holds all the SOA Project and related constants.
 * 
 * @author smathew
 */
public class SOAProjectConstants {
	
	/** The Constant REQUIRED_LIBRARIES. */
	public static final String REQUIRED_LIBRARIES = "requiredLibraries";
	
	/** The Constant DELIMITER_COMMA. */
    public static final String DELIMITER_COMMA = ",";
    
    /** The Constant DELIMITER_SEMICOLON. */
    public static final String DELIMITER_SEMICOLON = ":";
    
    /** The Constant DELIMITER_PIPE. */
    public static final String DELIMITER_PIPE = "|";
    
    /** The Constant DELIMITER_DOT. */
    public static final String DELIMITER_DOT = ".";
    
    /** The Constant DELIMITER_EQUALS. */
    public static final String DELIMITER_EQUALS = "=";
    
    /** The Constant DELIMITER_URL_SLASH. */
    public static final String DELIMITER_URL_SLASH = "/";
    
    /** The Constant EMPTY_STRING. */
    public static final String EMPTY_STRING = "";
    
    /** The Constant REQUIRED_PROJECTS. */
    public static final String REQUIRED_PROJECTS = "requiredProjects";
    
    /** The Constant REPOSITORY_PATH. */
    public static final String REPOSITORY_PATH = "repositoryPath";
    
    /** The Constant CLASS_NAME_SEPARATOR. */
    public static final String CLASS_NAME_SEPARATOR = DELIMITER_DOT;
    
    /** The Constant IMPL_PROJECT_SUFFIX. */
    public static final String IMPL_PROJECT_SUFFIX = "Impl";
    
    /** The Constant CLIENT_PROJECT_SUFFIX. */
    public static final String CLIENT_PROJECT_SUFFIX = "Consumer";
    
    /** The Constant SERVICE_CLIENT_SUFFIX. */
    public static final String SERVICE_CLIENT_SUFFIX = "_Client";
    
    /** The Constant CLIENT_NAME_SUFFIX_TEST. */
    public static final String CLIENT_NAME_SUFFIX_TEST = "_Test";
    
    /** The Constant BASE. */
    public static final String BASE = "Base";
    
    /**
     * The major version prefix.  It is set to V.
     */
    public static final String MAJOR_VERSION_PREFIX = "V";
    
    /**
     * Lowercase equivalent of the major version prefix, in US Local format.
     */
    public static final String MAJOR_VERSION_PREFIX_LOWERCASE = 
    	MAJOR_VERSION_PREFIX.toLowerCase(Locale.US);

    /**
     * The different types of creation possible for an intf project.
     * 
     */
    public static enum ConsumerSourceType {

    	/** The JAVA. */
    	JAVA, 
    	/** The WSDL. */
    	WSDL
    }

    /**
     *  The different types of creation possible for an intf project.
     */
    public static enum InterfaceSourceType {

    	/** The JAVA. */
    	JAVA, 
    	/** The WSDL. */
    	WSDL
    }

    /**
     *  The different types of creation possible for a wsdl project.
     */
    public static enum InterfaceWsdlSourceType {

    	/** The NEW. */
    	NEW, 
    	/** The EXISTIING. */
    	EXISTIING
    }

    /**
     * The different types of supported project types.
     * 
     * <ul>
     *    <li>Interface</li>
     *    <li>Impl</li>
     *    <li>Consumer</li>
     *    <li>Type Library</li>
     *    <li>Error Library</li>
     *    <li>Unknown</li>
     * </ul>
     *
     */
    public static enum SupportedProjectType {

    	/** The INTERFACE. */
    	INTERFACE, 
    	/** The IMPL. */
    	IMPL, 
    	/** The CONSUMER. */
    	CONSUMER, 
    	/** The TYP e_ library. */
    	TYPE_LIBRARY, 
    	/** The ERRO r_ library. */
    	ERROR_LIBRARY, 
    	/** The UNKNOWN. */
    	UNKNOWN
    }

    /**
     * Transport bindings.
     * <ul>
     *    <li>LOCAL</li>
     *    <li>HTTP10</li>
     *    <li>HTTP11</li>
     * </ul>
     *
     */
    public static enum Binding {

    	/** The LOCAL. */
    	LOCAL, 
    	/** The HTT p10. */
    	HTTP10, 
    	/** The HTT p11. */
    	HTTP11;

    	/**
    	 * The enumerated value of the binding.
    	 * @param name Name of transport bindings
    	 * @return returns the value of the named binding.
    	 */
    	public static Binding value(final String name) {
    		return valueOf(name.toUpperCase());
    	}
    }

    /**
     * Data Binding types.
     * 
     * <ul>
     *    <li>XML</li>
     *    <li>NV</li>
     *    <li>JSON</li>
     *    <li>FAST_INFOSET</li>
     * </ul>
     *
     */
    public static enum DataBinding {

    	/** The XML. */
    	XML, 
    	/** The NV. */
    	NV, 
    	/** The JSON. */
    	JSON, 
    	/** The FAS t_ infoset. */
    	FAST_INFOSET,
    	/** ProtoBuf protocol*/
    	PROTOBUF;


    	/**
    	 * The enumerated value of the binding.
    	 * @param name Name of data bindings
    	 * @return returns the value of the named binding.
    	 */
    	public static DataBinding value(final String name) {
    		return valueOf(name.toUpperCase());
    	}
    }

    /**
     * The service layer.
     * 
     * <ul>
     *   <li>UNKNOWN</li>
     *   <li>COMMON</li>
     *   <li>INTERMEDIATE</li>
     *   <li>BUSINESS</li>
     * </ul>
     * 
     *
     */
    public static enum ServiceLayer {

    	/** The UNKNOWN. */
    	UNKNOWN, 
    	/** The COMMON. */
    	COMMON, 
    	/** The INTERMEDIATE. */
    	INTERMEDIATE, 
    	/** The BUSINESS. */
    	BUSINESS;

    	/**
    	 * The enumerated value of the binding.
    	 * @param name Name of data bindings
    	 * @return returns the value of the named binding.
    	 */
    	public static ServiceLayer value(final String name) {
    		return valueOf(name.toUpperCase());
    	}

        /**
         * Gets all the service layers.
         * 
         * @return returns the service layers as a List of ServiceLayer
         */
        
        public static List<ServiceLayer> getAllLayers() {
            return Arrays.asList(ServiceLayer.values());
        }
    }

    /**
     * Supported Message Protocols.
     * 
     * <ul>
     *   <li>NONE</li>
     *   <li>SOAP11</li>
     *   <li>SOAP12</li>
     * </ul>
     *
     */
    public static enum MessageProtocol {

    	/** The NONE. */
    	NONE, 
    	/** The SOA p11. */
    	SOAP11, 
    	/** The SOA p12. */
    	SOAP12;

    	/**
    	 * The value of the protocol.  
    	 * @param name  Name of the protocol. The value is converted to upper case.
    	 * @return the value of the protocol.
    	 */
    	public static MessageProtocol value(final String name) {
    		return valueOf(name.toUpperCase());
    	}
    }
    
    /**
     * SOA Framework libraries.
     * <ul>
     * 	<li>SOASERVER</li>
     *  <li>SOACLIENT</li>
     *  <li>SOATOOLS</li>
     * </ul>
     *
     */
    public static enum SOAFrameworkLibrary {

    	/** The SOASERVER. */
    	SOASERVER, 
    	/** The SOACLIENT. */
    	SOACLIENT, 
    	/** The SOATOOLS. */
    	SOATOOLS;

    	/**
    	 * The value of the frameowork library enumeration.  
    	 * @param name  Name of the protocol. The name is converted to upper case.
    	 * @return the value of the protocol.
    	 */
    	public static SOAFrameworkLibrary value(final String name) {
    		return valueOf(name.toUpperCase());
    	}
    }

    /** The Constant PROTOCOL_HTTP. */
    public static final String PROTOCOL_HTTP = "http";

    /**
     * The default version to use. 1.0.0.
     */
    public static final String DEFAULT_VERSION = "1.0.0";
    
    /**
     * The default service version.  Currently set to the same value as DEFAULT_VERSION.
     */
    public static final String DEFAULT_SERVICE_VERSION = DEFAULT_VERSION;
    
    /** The Constant GEN. */
    public static final String GEN = "gen";

    /**
     * Legacy Folder structure for code generation.
     */
    public static final String FOLDER_GEN = GEN;
    
    /**
     * Prefix of legacy folder generation.  A combination of FOLDER_GEN plus a - .
     */
    public static final String FOLDER_GEN_PREFIX = FOLDER_GEN + "-";
    
    /**
     * Legacy source folder. Set to "src".
     */
    public static final String FOLDER_SRC = "src";
    
    /** The Constant FOLDER_DOT. */
    public static final String FOLDER_DOT = DELIMITER_DOT;
    
    /**
     * Legacy test folder prefix.  A combination of <code>FOLDER_GEN_PREFIX + "test"</code>.
     */
    public static final String FOLDER_GEN_TEST = FOLDER_GEN_PREFIX + "test";
    
    /**
     * Legacy source folder prefix.  A combination of <code>FOLDER_GEN_PREFIX + "src"</code>.
     */
    public static final String FOLDER_GEN_SRC = FOLDER_GEN_PREFIX + "src";
    
    /**
     * Legacy source folder for client code. <code>FOLDER_GEN_SRC + "/client".</code>
     */
    public static final String FOLDER_GEN_SRC_CLIENT = FOLDER_GEN_SRC
            + "/client";
    
    /**
     * Legacy source folder for service generated code. <code>FOLDER_GEN_SRC + "/service".</code>
     */
    public static final String FOLDER_GEN_SRC_SERVICE = FOLDER_GEN_SRC
            + "/service";
    
    /**
     * Legacy source folder for service generated. <code>FOLDER_GEN_SRC + "/test"</code>.
     */
    public static final String FOLDER_GEN_SRC_TEST = FOLDER_GEN_SRC + "/test";
    
    /** The Constant FOLDER_GEN_META_SRC. */
    public static final String FOLDER_GEN_META_SRC = FOLDER_GEN_PREFIX
            + "meta-src";
    
    /** The Constant FOLDER_META_INF. */
    public static final String FOLDER_META_INF = "META-INF";
    
    /** The Constant GEN_META_SRC_META_INF. */
    public static final String GEN_META_SRC_META_INF = FOLDER_GEN_META_SRC
            + "/" + FOLDER_META_INF;
    
    /** The Constant FOLDER_WEB_CONTENT. */
    public static final String FOLDER_WEB_CONTENT = FOLDER_GEN_PREFIX
            + "web-content";
    
    /** The Constant FOLDERS_TEST. */
    public static final List<String> FOLDERS_TEST = Arrays.asList(new String[] {
            FOLDER_GEN_SRC_TEST, FOLDER_GEN_TEST });
    
    /** Inside the jar wsdl goes inside meta inf. */
    public static final String META_INF_WSDL = "META-INF/soa/services/wsdl";
    
    /** protobuf file location  */
    public static final String META_PROTO_BUF = "META-INF/soa/services/proto";
    
    /** The Constant IMPL_SERVICE_CONFIG_DIR. */
    public static final String IMPL_SERVICE_CONFIG_DIR = "META-INF/soa/services/config";
    
    /** The Constant IMPL_SERVICE_CONFIG_XML. */
    public static final String IMPL_SERVICE_CONFIG_XML = "ServiceConfig.xml";

    /** The Constant METADATA_PROPS_LOCATION_JAR. */
    public static final String METADATA_PROPS_LOCATION_JAR = "META-INF/soa/common/config/";

    /** The Constant FOLDER_META_SRC. */
    public static final String FOLDER_META_SRC = "meta-src";

    
    /** The Constant META_SRC_META_INF. */
    public static final String META_SRC_META_INF = FOLDER_META_SRC + "/"
            + FOLDER_META_INF;
    
    /** The Constant FOLDER_OUTPUT_DIR. */
    public static final String FOLDER_OUTPUT_DIR = "build/classes";
    
    /** The Constant FOLDER_GEN_WEB_CONTENT. */
    public static final String FOLDER_GEN_WEB_CONTENT = FOLDER_GEN_PREFIX
            + "web-content";
    
    /** The Constant FOLDER_SRC_TEST. */
    public static final String FOLDER_SRC_TEST = FOLDER_GEN_PREFIX + "test";
    
    /** The Constant SOURCE_DIRECTORIES. */
    public static final String[] SOURCE_DIRECTORIES = { FOLDER_SRC,
            FOLDER_GEN_SRC_TEST, FOLDER_META_SRC };

    /** The Constant FOLDER_WEB_INF. */
    public static final String FOLDER_WEB_INF = "WEB-INF";
    
    /** The Constant FOLDER_LIB. */
    public static final String FOLDER_LIB = "lib";
    
    /** The Constant FOLDER_CLASSES. */
    public static final String FOLDER_CLASSES = "classes";
    
    /** The Constant FOLDER_WEBINF_CLASSES. */
    public static final String FOLDER_WEBINF_CLASSES = FOLDER_WEB_INF + "/"
            + FOLDER_CLASSES;
    
    /** The Constant FOLDER_WEBINF_CLASSES_WITH_PATH_PREFIX. */
    public static final String FOLDER_WEBINF_CLASSES_WITH_PATH_PREFIX = "/"
            + FOLDER_WEBINF_CLASSES;
    
    /** The Constant FOLDER_WEBINF_LIB. */
    public static final String FOLDER_WEBINF_LIB = FOLDER_WEB_INF + "/"
            + FOLDER_LIB;
    
    /** The Constant FOLDER_WEBINF_LIB_WITH_PATH_PREFIX. */
    public static final String FOLDER_WEBINF_LIB_WITH_PATH_PREFIX = "/"
            + FOLDER_WEBINF_LIB;

    /** The Constant FILE_EXTENSION_JAR. */
    public static final String FILE_EXTENSION_JAR = "jar";

    /** The Constant WSDL. */
    public static final String WSDL = "wsdl";
    
    /** The Constant XSD. */
    public static final String XSD = "xsd";
    
    /** The Constant WSDL_EXT. */
    public static final String WSDL_EXT = "." + WSDL;
    
    /** The Constant JAR_EXT. */
    public static final String JAR_EXT = "." + FILE_EXTENSION_JAR;
    
    /** The Constant WAR_EXT. */
    public static final String WAR_EXT = ".war";
    
    /** The Constant XML_EXT. */
    public static final String XML_EXT = ".xml";
    
    /** The Constant JAVA_EXT. */
    public static final String JAVA_EXT = ".java";
    
    /** The Constant URL_FILE_PREFIX. */
    public static final String URL_FILE_PREFIX = "file://";

    /** The Constant CODEGEN_ALL_TYPE_SRC. */
    public static final String CODEGEN_ALL_TYPE_SRC = FOLDER_SRC;
    
    /** The Constant CODEGEN_FOLDER_OUTPUT_DIR. */
    public static final String CODEGEN_FOLDER_OUTPUT_DIR = FOLDER_OUTPUT_DIR;
    
    /** The Constant DEFAULT_BASE_CONSUMER_SOURCE_DIRECTORY. */
    public static final String DEFAULT_BASE_CONSUMER_SOURCE_DIRECTORY = FOLDER_SRC;

    /** The Constant FILE_PROJECT_XML. */
    public static final String FILE_PROJECT_XML = "project.xml";
    
    /** The Constant FILE_CLASSPATH. */
    public static final String FILE_CLASSPATH = ".classpath";
    
    /** The Constant FILE_PROJECT. */
    public static final String FILE_PROJECT = ".project";


    /** The Constant FILE_TYPE_MAPPINGS. */
    public static final String FILE_TYPE_MAPPINGS = "TypeMappings.xml";
    
    /** The Constant FILE_WEB_XML. */
    public static final String FILE_WEB_XML = "web.xml";
    
    /** The Constant FILE_GLOBAL_CLIENT_CONFIG. */
    public static final String FILE_GLOBAL_CLIENT_CONFIG = "GlobalClientConfig.xml";
    
    /** The Constant FILE_GLOBAL_SERVICE_CONFIG. */
    public static final String FILE_GLOBAL_SERVICE_CONFIG = "GlobalServiceConfig.xml";
    
    /** The Constant DEFAULT_CLIENT_CONFIG_ENVIRONMENT. */
    public static final String DEFAULT_CLIENT_CONFIG_ENVIRONMENT = "production";
    
    /** The Constant EBAY_POOL_TYPES. */
    public static final String[] EBAY_POOL_TYPES = {
            DEFAULT_CLIENT_CONFIG_ENVIRONMENT, "staging", "feature", "dev",
            "sandbox" };


    //Props File Constants
    /** The Constant PROPS_FILE_SERVICE_METADATA. */
    public static final String PROPS_FILE_SERVICE_METADATA = "service_metadata.properties";
    
    /** The Constant PROPS_FILE_SERVICE_INTERFACE. */
    public static final String PROPS_FILE_SERVICE_INTERFACE = "service_intf_project.properties";
    
    /** The Constant PROPS_FILE_SERVICE_IMPL. */
    public static final String PROPS_FILE_SERVICE_IMPL = "service_impl_project.properties";
    
    /** The Constant PROPS_FILE_SERVICE_CONSUMER. */
    public static final String PROPS_FILE_SERVICE_CONSUMER = "service_consumer_project.properties";
    
    /** The Constant PROPS_FILE_TYPE_LIBRARY. */
    public static final String PROPS_FILE_TYPE_LIBRARY = "type_library_project.properties";
    
    /** The Constant PROPS_INTF_SOURCE_TYPE. */
    public static final String PROPS_INTF_SOURCE_TYPE = "interface_source_type";
    
    /** The Constant PROPS_SERVICE_DOMAIN_NAME. */
    public static final String PROPS_SERVICE_DOMAIN_NAME = "domainName";
    
    /** The Constant PROPS_SERVICE_NAMESPACE_PART. */
    public static final String PROPS_SERVICE_NAMESPACE_PART = "service_namespace_part";
    
    /** The Constant PROPS_KEY_NAMESPACE_TO_PACKAGE. */
    public static final String PROPS_KEY_NAMESPACE_TO_PACKAGE = "ns2pkg";
    
    /** The Constant PROPS_KEY_TYPE_NAMESPACE. */
    public static final String PROPS_KEY_TYPE_NAMESPACE = "ctns";
    
    /** The Constant PROPS_KEY_TYPE_FOLDING. */
    public static final String PROPS_KEY_TYPE_FOLDING = "enabledNamespaceFolding";
    
    /** The Constant PROP_KEY_SERVICE_LAYER. */
    public static final String PROP_KEY_SERVICE_LAYER = "service_layer";
    
    /** The Constant PROP_KEY_SERVICE_VERSION. */
    public static final String PROP_KEY_SERVICE_VERSION = "service_version";
    
    /** The Constant PROP_KEY_ORIGINAL_WSDL_URI. */
    public static final String PROP_KEY_ORIGINAL_WSDL_URI = "original_wsdl_uri";
    
    /** The Constant PROP_KEY_IMPL_PROJECT_NAME. */
    public static final String PROP_KEY_IMPL_PROJECT_NAME = "impl_project_name";
    
    /** The Constant PROP_KEY_SERVICE_NAME. */
    public static final String PROP_KEY_SERVICE_NAME = "service_name";
    
    /** The Constant PROP_KEY_SERVICE_INTERFACE_CLASS_NAME. */
    public static final String PROP_KEY_SERVICE_INTERFACE_CLASS_NAME = "service_interface_class_name";
    
    /** The Constant PROP_KEY_ADMIN_NAME. */
    public static final String PROP_KEY_ADMIN_NAME = "admin_name";
    
    /** The Constant PROP_KEY_NON_XSD_FORMATS. */
    public static final String PROP_KEY_NON_XSD_FORMATS = "nonXSDFormats";
    
    /** The Constant PROPS_INTF_SOURCE_TYPE_WSDL. */
    public static final String PROPS_INTF_SOURCE_TYPE_WSDL = "WSDL";
    
    /** The Constant PROPS_INTF_SOURCE_TYPE_JAVA. */
    public static final String PROPS_INTF_SOURCE_TYPE_JAVA = "JAVA";
    
    /** The Constant PROPS_COMMENTS. */
    public static final String PROPS_COMMENTS = "Generated Properties File";
    
    /** The Constant PROPS_IMPL_BASE_CONSUMER_SRC_DIR. */
    public static final String PROPS_IMPL_BASE_CONSUMER_SRC_DIR = "baseconsumer-srcdir";
    
    /** The Constant PROPS_SUPPORT_ZERO_CONFIG. */
    public static final String PROPS_SUPPORT_ZERO_CONFIG = "support_zero_config";
    
    /** The Constant PROPS_IMPL_BASE_CONSUMER_SRC_DIR_DEFAULT. */
    public static final String PROPS_IMPL_BASE_CONSUMER_SRC_DIR_DEFAULT = FOLDER_SRC;
    
    /** The Constant PROPS_ENV_MAPPER. */
    public static final String PROPS_ENV_MAPPER = "envMapper";
    
    /** The Constant PROPS_NOT_GENERATE_BASE_CONSUMER. */
    public static final String PROPS_NOT_GENERATE_BASE_CONSUMER = "not_generate_base_consumer";

    
    /** The Constant PROPS_KEY_CONSUMER_SVC_CLIENT_NAME_MAPPING. */
    public static final String PROPS_KEY_CONSUMER_SVC_CLIENT_NAME_MAPPING = "service_client_name_mapping";
    
    /** The Constant PROPS_KEY_CLIENT_NAME. */
    public static final String PROPS_KEY_CLIENT_NAME = "client_name";
    
    /** The Constant PROPS_KEY_CONSUMER_ID. */
    public static final String PROPS_KEY_CONSUMER_ID = "consumer_id";
    
    /** The Constant PROPS_KEY_SMP_VERSION. The version of service_metadata.properties file */
    public static final String PROPS_KEY_SMP_VERSION = "smp_version";
    
    /** The Constant PROPS_KEY_SIMP_VERSION. */
    public static final String PROPS_KEY_SIMP_VERSION = "simp_version";
    
    /** The Constant PROPS_KEY_USE_EXTERNAL_SERVICE_FACTORY. */
    public static final String PROPS_KEY_USE_EXTERNAL_SERVICE_FACTORY = "useExternalServiceFactory";
    
    /** The Constant PROPS_KEY_SERVICE_FACTORY_CLASS_NAME. */
    public static final String PROPS_KEY_SERVICE_FACTORY_CLASS_NAME = "serviceImplFactoryClassName";
    
    /** The Constant PROPS_DEFAULT_VALUE_SERVICE_FACTORY_CLASS_NAME_POSTFIX. */
    public static final String PROPS_DEFAULT_VALUE_SERVICE_FACTORY_CLASS_NAME_POSTFIX = "Factory";
    // The version of service_intf_project.properties file
    /** The Constant PROPS_KEY_SIPP_VERSION. */
    public static final String PROPS_KEY_SIPP_VERSION = "sipp_version";
    
    /**
     * To make sure sharedConsumer path is shortened and existing consumers are also supported, Codegen can rely on a new property (which needs to be put by plugin in service_intf_project.properties in cases path is too long).
     */
    
    public static final String PROPS_KEY_SHORT_PATH_FOR_SHARED_CONSUMER = "short_path_for_shared_consumer";
    
    /**
     * The version of service_consumer_project.properties file.
     */
    public static final String PROPS_KEY_SCPP_VERSION = "scpp_version";
    
    /**
     * Default version. 1.1 
     */
    public static final String PROPS_DEFAULT_PROPERTY_VERSION = "1.1";
    
    /**
     * version for interface prop file is bumped up to 1.2 in SOA2.9
     */
    public static final String PROPS_INTERFACE_PROPERTY_VERSION = "1.2";

    
    /**
     * The default property version as a Version class.
     */
    public static final Version DEFAULT_PROPERTY_VERSION = new Version(
            PROPS_DEFAULT_PROPERTY_VERSION);
    
    /**
     * The property version as a Version class for interface project
     */
    public static final Version INTERFACE_PROPERTY_VERSION = new Version(
    		PROPS_INTERFACE_PROPERTY_VERSION);
    
    
    /**
     * version 1.2
     */
    public static final String PROPS_DEFAULT_SIPP_VERSION = PROPS_INTERFACE_PROPERTY_VERSION;
    
    /**
     * Default version. 1.1 
     */
    public static final String PROPS_DEFAULT_SCPP_VERSION = PROPS_DEFAULT_PROPERTY_VERSION;
    /**
     * Default version. 1.1 
     */
    public static final String PROPS_DEFAULT_SIMP_VERSION = PROPS_DEFAULT_PROPERTY_VERSION;
    /*
     * Property for enabling raptor consumer generation
     */
    public static final String PROPS_KEY_RAPTOR_CONSUMER_GENERATION = "raptorConsumerGeneration";
    
    /*
     * Default value for enabling raptor consumer generation in RIDE
     */
    public static final String PROPS_DEFAULT_RAPTOR_CONSUMER_GENERATION = "false";
    /*
     * Property for enabling deletion of object factory and package info in RIDE
     */
    public static final String PROPS_OBJECT_FACT_PACK_INFO_DEL = "deleteObjectFactoryPackinfo";
    /*
     * Property for enabling bindings generation in RIDE
     */
    public static final String PROPS_GENERATE_BINDING = "generateBinding";
    /*
     * Property for denoting an interface created via service impl flow.
     */
    public static final String PROPS_GEN_SHARED_CONSUMER = "generateSharedConsumer";
    /*
     * Default value for property for enabling bindings generation in RIDE
     */
    public static final String PROPS_DEFAULT_GENERATE_BINDING = "false";
    /*
     * Property for enabling deletion of object factory and package info in RIDE
     */
    public static final String PROPS_DEFAULT_OBJECT_FACT_PACK_INFO_DEL = "true";
    
    /*
     * Default preference file name in RIDE
     */
    public static final String PREFS_FILE = "org.eclipse.pde.core.prefs";
    public static final String RIDE_PREFS_KEY_BUNDLE="BUNDLE_ROOT_PATH";
    public static final String RIDE_PREFS_VALUE_BUNDLE="src/main/resources";
    
    
    // preference properties
    /** The Constant PROP_REQUIRED_SERVICES. */
    public static final String PROP_REQUIRED_SERVICES = "requiredServices";
    
    /** The Constant REQUIRED_SERVICES_DELIMITER. */
    public static final String REQUIRED_SERVICES_DELIMITER = ",";

    // Metadata Props
    /** The Constant SERVICE_METADATA_PROPS_SERVICE_NAME. */
    public static final String SERVICE_METADATA_PROPS_SERVICE_NAME = PROP_KEY_ADMIN_NAME;
    
    /** The Constant SERVICE_METADATA_PROPS_SERVICE_INTERFACE_CLASS_NAME. */
    public static final String SERVICE_METADATA_PROPS_SERVICE_INTERFACE_CLASS_NAME = PROP_KEY_SERVICE_INTERFACE_CLASS_NAME;
    
    /** The Constant SERVICE_METADATA_PROPS_SERVICE_IMPLEMENTATION_CLASS_NAME. */
    public static final String SERVICE_METADATA_PROPS_SERVICE_IMPLEMENTATION_CLASS_NAME = "service_implementation_class_name";
    
    /** The Constant SERVICE_METADATA_PROPS_SERVICE_LAYER. */
    public static final String SERVICE_METADATA_PROPS_SERVICE_LAYER = PROP_KEY_SERVICE_LAYER;
    
    /** The Constant SERVICE_METADATA_PROPS_SERVICE_VERSION. */
    public static final String SERVICE_METADATA_PROPS_SERVICE_VERSION = PROP_KEY_SERVICE_VERSION;

    // Property Page
    /** The Constant PROP_PAGE_ID_SOA_PROJ. */
    public static final String PROP_PAGE_ID_SOA_PROJ = "org.ebayopensource.turmeric.eclipse.services.ui.properties.soaProjectPropertyPage";
    // mzang 2010-4-20 id for property page.
    /** The Constant PROP_PAGE_ID_TYPELIBRARA_PROJ. */
    public static final String PROP_PAGE_ID_TYPELIBRARA_PROJ = "org.ebayopensource.turmeric.eclipse.services.ui.properties.typelibraryProjectPropertyPage";

    /** The Constant TEMP_PREFIX. */
    public static final String TEMP_PREFIX = "__temp_soa_";

    /** The Constant BUILD. */
    public static final String BUILD = "build";

    /** The Constant DEFAULT_DATA_TYPES. */
    public static final String[] DEFAULT_DATA_TYPES;

    /**
     * XML Schema Namespace.
     */
    public static final String XML_NAMESPACE_2001 = "http://www.w3.org/2001/XMLSchema";

    /**
     * string for protobuf
     */
	public static final String SVC_PROTOCOL_BUF = "protobuf";
    
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

    /** The Constant PARAMETER_INPUT_SUFFIX. */
    public static final String PARAMETER_INPUT_SUFFIX = "Request";
    
    /** The Constant PARAMETER_OUTPUT_SUFFIX. */
    public static final String PARAMETER_OUTPUT_SUFFIX = "Response";

    /**
     * Template Bindings.
     * 
     * <ul>
     *   <li>HTTP</li>
     *   <li>SOAP</li>
     * </ul>
     * 
     *
     */
    public static enum TemplateBinding {

    	/** The HTTP. */
    	HTTP, 
    	/** The SOAP. */
    	SOAP;

    	/**
    	 * Value.
    	 *
    	 * @param name the name
    	 * @return value of the binding
    	 */
    	public static TemplateBinding value(final String name) {
    		return valueOf(name.toUpperCase());
    	}

        /**
         * Gets the all bindings.
         *
         * @return A List of TemplateBinding objects
         */
        public static List<TemplateBinding> getAllBindings() {
            return Arrays.asList(TemplateBinding.values());
        }

        /**
         * Gets the all binding names.
         *
         * @return A List of String names for the bindings.
         */
        public static List<String> getAllBindingNames() {
            final List<String> result = new ArrayList<String>();
            for (TemplateBinding binding : TemplateBinding.values()) {
                result.add(binding.name());
            }
            return result;
        }
    }
    
    /**
     * the type of the service implementation
     *
     */
    public static enum ServiceImplType {
    	SERVICE_IMPL, SERVICE_IMPL_FACTORY;
    	
    	 /**
         * 
         * @param name 
         * @return value of the binding
         */
        public static ServiceImplType value(final String name) {
        	String upperName = name.toUpperCase(Locale.US);
            return StringUtils.isNotBlank(upperName) && upperName.endsWith("FACTORY") 
            ? SERVICE_IMPL_FACTORY : SERVICE_IMPL;
        }
    }

}
