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
package org.ebayopensource.turmeric.eclipse.repositorysystem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class BaseCodeGenModel.
 *
 * @author yayu
 */
public class BaseCodeGenModel {
	
	/** The Constant PARAM_GENTYPE. */
	public static final String PARAM_GENTYPE = "-genType";
	
	/** The Constant PARAM_NAMESPACE. */
	public static final String PARAM_NAMESPACE = "-namespace";
	
	/** The Constant PARAM_SERVICE_LAYER_FILE. */
	public static final String PARAM_SERVICE_LAYER_FILE = "-asl";
	
	/** The Constant PARAM_INTERFACE. */
	public static final String PARAM_INTERFACE = "-interface";
	
	/** The Constant PARAM_ADMIN_NAME. */
	public static final String PARAM_ADMIN_NAME = "-adminname";
	
	/** The Constant PARAM_SERVICE_NAME. */
	public static final String PARAM_SERVICE_NAME = "-serviceName";
	
	/** The Constant PARAM_SCV. */
	public static final String PARAM_SCV = "-scv";
	
	/** The Constant PARAM_SICN. */
	public static final String PARAM_SICN = "-sicn";
	
	/** The Constant PARAM_PR. */
	public static final String PARAM_PR = "-pr";
	
	/** The Constant PARAM_SLAYER. */
	public static final String PARAM_SLAYER = "-slayer";
	
	/** The Constant PARAM_SRC. */
	public static final String PARAM_SRC = "-src";
	
	/** The Constant PARAM_DEST. */
	public static final String PARAM_DEST = "-dest";
	
	/** The Constant PARAM_BIN. */
	public static final String PARAM_BIN = "-bin";
	
	/** The Constant PARAM_CN. */
	public static final String PARAM_CN = "-cn";
	
	/** The Constant PARAM_SL. */
	public static final String PARAM_SL = "-sl";
	
	/** The Constant PARAM_MDEST. */
	public static final String PARAM_MDEST = "-mdest";
	
	/** The Constant PARAM_WSDL. */
	public static final String PARAM_WSDL = "-wsdl";
	
	/** The Constant PARAM_GIP. */
	public static final String PARAM_GIP = "-gip";
	
	/** The Constant PARAM_GIN. */
	public static final String PARAM_GIN = "-gin";
	
	/** The Constant PARAM_AVI. */
	public static final String PARAM_AVI = "-avi";
	
	/** The Constant PARAM_JDEST. */
	public static final String PARAM_JDEST = "-jdest";
	
	/** The Constant PARAM_CCGN. */
	public static final String PARAM_CCGN = "-ccgn"; //Client Config Group Name
	
	/** The Constant PARAM_SCGN. */
	public static final String PARAM_SCGN = "-scgn"; //Service Config Group Name
	
	/** The Constant PARAM_GT. */
	public static final String PARAM_GT = "-gt"; //for generating the unit test of gentype serviceFromWSDLImpl
	
	/** The Constant PARAM_NS2PKG. */
	public static final String PARAM_NS2PKG = "-ns2pkg"; //namespace to pacakge mappings
	
	/** The Constant PARAM_GSS. */
	public static final String PARAM_GSS = "-gss"; //an indicator to ensure that the impl class will always be generated even already exist
	
	/** The Constant PARAM_OWIC. */
	public static final String PARAM_OWIC = "-owic"; //an indicator for whether to overwrite the impl class
	
	/** The Constant PARAM_CTNS. */
	public static final String PARAM_CTNS = "-ctns"; //common types Namespace
	
	/** The Constant PARAM_ENVIRONMENT. */
	public static final String PARAM_ENVIRONMENT = "-environment"; //Client config environment
	
	/** The Constant PARAM_ENV_MAPPER. */
	public static final String PARAM_ENV_MAPPER = "-envMapper"; //environment mapper impl class name
	
	/** The Constant PARAM_CONSUMER_ID. */
	public static final String PARAM_CONSUMER_ID = "-consumerid"; //consumer id
	
	/** The Constant PARAM_JAVA_HOME. */
	public static final String PARAM_JAVA_HOME = "-javahome"; //The full path of the JAVA HOME directory
	
	/** The Constant PARAM_JDK_HOME. */
	public static final String PARAM_JDK_HOME = "-jdkhome"; //The full path of the JDK HOME directory
	
	/** The Constant PARAM_DOMAIN. */
	public static final String PARAM_DOMAIN = "-domain"; //command separated list of error domains
	
	/** The Constant PARAM_ERROR_LIBRARY_NAME. */
	public static final String PARAM_ERROR_LIBRARY_NAME = "-errorlibname"; //the name of the error library
	
	/*All|Client|ClientNoConfig|Server|ServerNoConfig|Proxy|Dispatcher|ConfigAll|ClientConfig|ServerConfig|
    GlobalServerConfig|GlocalClientConfig|Wsdl|Interface|Schema|SISkeleton|TypeMappings|WebXml|UnitTest|
    TestClient|ServiceOpProps|SecurityPolicyConfig|ServiceMetadataProps|ServiceIntfProjectProps*/
	/** The Constant GENTYPE_ALL. */
	public static final String GENTYPE_ALL = "All";
	
	/** The Constant GENTYPE_CLIENT. */
	public static final String GENTYPE_CLIENT = "Client";
	
	/** The Constant GENTYPE_CONSUMER. */
	public static final String GENTYPE_CONSUMER = "Consumer";
	
	/** The Constant GENTYPE_CLIENT_NO_CONFIG. */
	public static final String GENTYPE_CLIENT_NO_CONFIG = "ClientNoConfig";
	
	/** The Constant GENTYPE_SERVER. */
	public static final String GENTYPE_SERVER = "Server";
	
	/** The Constant GENTYPE_SERVER_NO_CONFIG. */
	public static final String GENTYPE_SERVER_NO_CONFIG = "ServerNoConfig";
	
	/** The Constant GENTYPE_PROXY. */
	public static final String GENTYPE_PROXY = "Proxy";
	
	/** The Constant GENTYPE_DISPATCHER. */
	public static final String GENTYPE_DISPATCHER = "Dispatcher";
	
	/** The Constant GENTYPE_CONFIG_ALL. */
	public static final String GENTYPE_CONFIG_ALL = "ConfigAll";
	
	/** The Constant GENTYPE_CLIENT_CONFIG. */
	public static final String GENTYPE_CLIENT_CONFIG = "ClientConfig";
	
	/** The Constant GENTYPE_SERVER_CONFIG. */
	public static final String GENTYPE_SERVER_CONFIG = "ServerConfig";
	
	/** The Constant GENTYPE_GLOBAL_SERVER_CONFIG. */
	public static final String GENTYPE_GLOBAL_SERVER_CONFIG = "GlobalServerConfig";
	
	/** The Constant GENTYPE_GLOBAL_CLIENT_CONFIG. */
	public static final String GENTYPE_GLOBAL_CLIENT_CONFIG = "GlobalClientConfig";
	
	/** The Constant GENTYPE_WSDL. */
	public static final String GENTYPE_WSDL = "Wsdl";
	
	/** The Constant GENTYPE_INTERFACE. */
	public static final String GENTYPE_INTERFACE = "Interface";
	
	/** The Constant GENTYPE_SCHEMA. */
	public static final String GENTYPE_SCHEMA = "Schema";
	
	/** The Constant GENTYPE_SISKELETON. */
	public static final String GENTYPE_SISKELETON = "SISkeleton";
	
	/** The Constant GENTYPE_TYPE_MAPPINGS. */
	public static final String GENTYPE_TYPE_MAPPINGS = "TypeMappings";
	
	/** The Constant GENTYPE_WEB_XML. */
	public static final String GENTYPE_WEB_XML = "WebXml";
	
	/** The Constant GENTYPE_UNIT_TEST. */
	public static final String GENTYPE_UNIT_TEST = "UnitTest";
	
	/** The Constant GENTYPE_TEST_CLIENT. */
	public static final String GENTYPE_TEST_CLIENT = "TestClient";
	
	/** The Constant GENTYPE_SERVICE_OP_PROPS. */
	public static final String GENTYPE_SERVICE_OP_PROPS = "ServiceOpProps";
	
	/** The Constant GENTYPE_SECURITY_POLICY_CONFIG. */
	public static final String GENTYPE_SECURITY_POLICY_CONFIG = "SecurityPolicyConfig";
	
	/** The Constant GENTYPE_SERVICE_METADATA_PROPS. */
	public static final String GENTYPE_SERVICE_METADATA_PROPS = "ServiceMetadataProps";
	
	/** The Constant GENTYPE_SERVICE_INTF_PROJECT_PROPS. */
	public static final String GENTYPE_SERVICE_INTF_PROJECT_PROPS = "ServiceIntfProjectProps";
	
	/** The Constant GENTYPE_SERVICE_FROM_WSDL_INTF. */
	public static final String GENTYPE_SERVICE_FROM_WSDL_INTF = "ServiceFromWSDLIntf";
	
	/** The Constant GENTYPE_SERVICE_FROM_WSDL_IMPL. */
	public static final String GENTYPE_SERVICE_FROM_WSDL_IMPL = "ServiceFromWSDLImpl";
	
	/** The Constant GENTYPE_ADDTYPE. */
	public static final String GENTYPE_ADDTYPE = "genTypeAddType";
	
	/** The Constant GENTYPE_DELETETYPE. */
	public static final String GENTYPE_DELETETYPE = "genTypeDeleteType";
	
	/** The Constant GENTYPE_CREATETYPELIBRARY. */
	public static final String GENTYPE_CREATETYPELIBRARY = "genTypeCreateTypeLibrary";
	
	/** The Constant GENTYPE_CLEANBUILDTYPELIBRARY. */
	public static final String GENTYPE_CLEANBUILDTYPELIBRARY = "genTypeCleanBuildTypeLibrary";
	
	/** The Constant GENTYPE_INCRBUILDTYPELIBRARY. */
	public static final String GENTYPE_INCRBUILDTYPELIBRARY = "genTypeIncrBuildTypeLibrary";
	
	/** The Constant GENTYPE_COMMAND_LINE_ALL. */
	public static final String GENTYPE_COMMAND_LINE_ALL = "genTypeCommandLineAll";
	
	/** The Constant GENTYPE_ERROR_LIB_ALL. */
	public static final String GENTYPE_ERROR_LIB_ALL = "genTypeErrorLibAll";
	
	/** The Constant SUPPORTED_GENTYPES. */
	public static final List<String> SUPPORTED_GENTYPES; 
	
	static {
		List<String> list = new ArrayList<String>();
		list.add(GENTYPE_ALL);
		list.add(GENTYPE_CLIENT);
		list.add(GENTYPE_CONSUMER);
		list.add(GENTYPE_CLIENT_NO_CONFIG);
		list.add(GENTYPE_SERVER);
		list.add(GENTYPE_SERVER_NO_CONFIG);
		list.add(GENTYPE_PROXY);
		list.add(GENTYPE_DISPATCHER);
		list.add(GENTYPE_CONFIG_ALL);
		list.add(GENTYPE_CLIENT_CONFIG);
		list.add(GENTYPE_SERVER_CONFIG);
		list.add(GENTYPE_GLOBAL_SERVER_CONFIG);
		list.add(GENTYPE_GLOBAL_CLIENT_CONFIG);
		list.add(GENTYPE_WSDL);
		list.add(GENTYPE_INTERFACE);
		list.add(GENTYPE_SCHEMA);
		list.add(GENTYPE_SISKELETON);
		list.add(GENTYPE_TYPE_MAPPINGS);
		list.add(GENTYPE_WEB_XML);
		list.add(GENTYPE_UNIT_TEST);
		list.add(GENTYPE_TEST_CLIENT);
		list.add(GENTYPE_SERVICE_OP_PROPS);
		list.add(GENTYPE_SECURITY_POLICY_CONFIG);
		list.add(GENTYPE_SERVICE_METADATA_PROPS);
		list.add(GENTYPE_SERVICE_INTF_PROJECT_PROPS);
		list.add(GENTYPE_SERVICE_FROM_WSDL_INTF);
		list.add(GENTYPE_SERVICE_FROM_WSDL_IMPL);
		list.add(GENTYPE_ADDTYPE);
		list.add(GENTYPE_CREATETYPELIBRARY);
		list.add(GENTYPE_CLEANBUILDTYPELIBRARY);
		list.add(GENTYPE_INCRBUILDTYPELIBRARY);
		list.add(GENTYPE_DELETETYPE);
		list.add(GENTYPE_COMMAND_LINE_ALL);
		list.add(GENTYPE_ERROR_LIB_ALL);
		SUPPORTED_GENTYPES = Collections.unmodifiableList(list);
	}

	private String adminName; //-adminname
	private String genType; //-genType
	private String namespace; //-namespace
	private String serviceLayerFile; //-asl
	private String serviceInterface; //-interface
	private String serviceName; //-serviceName
	private String serviceVersion; //-scv
	private String serviceImplClassName; //-sicn
	private String projectRoot; //-pr
	private String serviceLayer; //-slayer
	private String sourceDirectory; //-src
	private String destination; //-dest This is the project location in most cases
	private String outputDirectory; //-bin
	private String projectName;
	private String originalWsdlUrl; //-wsdl
	private String ns2pkg; //-ns2pkg
	private String genFolder; //jdesk
	
	/**
	 * Instantiates a new base code gen model.
	 */
	public BaseCodeGenModel() {
		super();
	}

	/**
	 * Instantiates a new base code gen model.
	 *
	 * @param genType the gen type
	 * @param namespace the namespace
	 * @param serviceLayerFile the service layer file
	 * @param serviceInterface the service interface
	 * @param serviceName the service name
	 * @param serviceVersion the service version
	 * @param serviceImpl the service impl
	 * @param projectRoot the project root
	 * @param serviceLayer the service layer
	 * @param sourceDirectory the source directory
	 * @param destination the destination
	 * @param outputDirectory the output directory
	 */
	public BaseCodeGenModel(String genType, String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory) {
		super();
		setGenType(genType);
		this.namespace = namespace;
		this.serviceLayerFile = serviceLayerFile;
		this.serviceInterface = serviceInterface;
		this.serviceName = serviceName;
		this.serviceVersion = serviceVersion;
		this.serviceImplClassName = serviceImpl;
		this.projectRoot = projectRoot;
		this.serviceLayer = serviceLayer;
		this.sourceDirectory = sourceDirectory;
		this.destination = destination;
		this.outputDirectory = outputDirectory;
	}

	/**
	 * Gets the gen type.
	 *
	 * @return the gen type
	 */
	public String getGenType() {
		return genType;
	}

	/**
	 * Sets the gen type.
	 *
	 * @param genType the new gen type
	 */
	public void setGenType(String genType) {
		if (SUPPORTED_GENTYPES.contains(genType) == false)
			throw new IllegalArgumentException("Unsupported gentype->" + genType);
		this.genType = genType;
	}

	/**
	 * Gets the admin name.
	 *
	 * @return the admin name
	 */
	public String getAdminName() {
		return adminName;
	}

	/**
	 * Sets the admin name.
	 *
	 * @param adminName the new admin name
	 */
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace(){
		return namespace;
	}

	/**
	 * Sets the namespace.
	 *
	 * @param namespace the new namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Gets the service layer file.
	 *
	 * @return the service layer file
	 */
	public String getServiceLayerFile() {
		return serviceLayerFile;
	}

	/**
	 * Sets the service layer file.
	 *
	 * @param serviceLayerFile the new service layer file
	 */
	public void setServiceLayerFile(String serviceLayerFile) {
		this.serviceLayerFile = serviceLayerFile;
	}

	/**
	 * Gets the service interface.
	 *
	 * @return the service interface
	 */
	public String getServiceInterface() {
		return serviceInterface;
	}

	/**
	 * Sets the service interface.
	 *
	 * @param serviceInterface the new service interface
	 */
	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the service version.
	 *
	 * @return the service version
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}

	/**
	 * Sets the service version.
	 *
	 * @param serviceVersion the new service version
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	/**
	 * Gets the service impl class name.
	 *
	 * @return the service impl class name
	 */
	public String getServiceImplClassName() {
		return serviceImplClassName;
	}

	/**
	 * Sets the service impl class name.
	 *
	 * @param serviceImpl the new service impl class name
	 */
	public void setServiceImplClassName(String serviceImpl) {
		this.serviceImplClassName = serviceImpl;
	}

	/**
	 * Gets the project root.
	 *
	 * @return the project root
	 */
	public String getProjectRoot() {
		return projectRoot;
	}

	/**
	 * Sets the project root.
	 *
	 * @param projectRoot the new project root
	 */
	public void setProjectRoot(String projectRoot) {
		this.projectRoot = projectRoot;
	}

	/**
	 * Gets the service layer.
	 *
	 * @return the service layer
	 */
	public String getServiceLayer() {
		return serviceLayer;
	}

	/**
	 * Sets the service layer.
	 *
	 * @param serviceLayer the new service layer
	 */
	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	/**
	 * Gets the source directory.
	 *
	 * @return the source directory
	 */
	public String getSourceDirectory() {
		return sourceDirectory;
	}

	/**
	 * Sets the source directory.
	 *
	 * @param sourceDirectory the new source directory
	 */
	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination the new destination
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Gets the output directory.
	 *
	 * @return the output directory
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Sets the output directory.
	 *
	 * @param outputDirectory the new output directory
	 */
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	/**
	 * Gets the original wsdl url.
	 *
	 * @return the original wsdl url
	 */
	public String getOriginalWsdlUrl() {
		return originalWsdlUrl;
	}

	/**
	 * Sets the original wsdl url.
	 *
	 * @param originalWsdlUrl the new original wsdl url
	 */
	public void setOriginalWsdlUrl(String originalWsdlUrl) {
		this.originalWsdlUrl = originalWsdlUrl;
	}

	/**
	 * Gets the ns2pkg.
	 *
	 * @return the ns2pkg
	 */
	public String getNs2pkg() {
		return ns2pkg;
	}

	/**
	 * Sets the ns2pkg.
	 *
	 * @param ns2pkg the new ns2pkg
	 */
	public void setNs2pkg(String ns2pkg) {
		this.ns2pkg = ns2pkg;
	}
	
	/**
	 * Gets the gen folder.
	 *
	 * @return the gen folder
	 */
	public String getGenFolder() {
		return genFolder;
	}

	/**
	 * Sets the gen folder.
	 *
	 * @param genFolder the new gen folder
	 */
	public void setGenFolder(String genFolder) {
		this.genFolder = genFolder;
	}

	/**
	 * Gets the code gen options.
	 *
	 * @return the code gen options
	 */
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = new HashMap<String, String>();
		result.put(PARAM_GENTYPE, this.genType);
		if (this.adminName != null)
			result.put(PARAM_ADMIN_NAME, this.adminName);
		if (this.namespace != null)
			result.put(PARAM_NAMESPACE, this.namespace);
		if (this.serviceLayerFile != null)
			result.put(PARAM_SERVICE_LAYER_FILE, this.serviceLayerFile);
		if (this.serviceInterface != null)
			result.put(PARAM_INTERFACE, this.serviceInterface);
		if (this.serviceName != null)
			result.put(PARAM_SERVICE_NAME, this.serviceName);
		if (this.serviceVersion != null)
			result.put(PARAM_SCV, this.serviceVersion);
		if (this.serviceImplClassName != null)
			result.put(PARAM_SICN, this.serviceImplClassName);
		if (this.projectRoot != null)
			result.put(PARAM_PR, this.projectRoot);
		if (this.serviceLayer != null)
			result.put(PARAM_SLAYER, this.serviceLayer);
		if (this.sourceDirectory != null)
			result.put(PARAM_SRC, this.sourceDirectory);
		if (this.destination != null)
			result.put(PARAM_DEST, this.destination);
		if (this.outputDirectory != null)
			result.put(PARAM_BIN, this.outputDirectory);
		if (this.ns2pkg != null)
			result.put(PARAM_NS2PKG, this.ns2pkg);
		if (this.genFolder != null)
			result.put(PARAM_JDEST, this.genFolder);
		return result;
	}
	
	/**
	 * To string.
	 *
	 * @param genType the gen type
	 * @param inputParams the input params
	 * @return the string
	 */
	public static String toString(final String genType, final Map<String, String> inputParams) {
		final StringBuffer result = new StringBuffer();
		
		result.append("Generating ");
		result.append(genType);
		result.append(" input parameters: ");
		for ( String key : inputParams.keySet()) {
			result.append(" ");
			result.append(key);
			result.append(" ");
			result.append(inputParams.get(key));
		}
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(this.genType, getCodeGenOptions());
	}

	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Sets the project name.
	 *
	 * @param projectName the new project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
