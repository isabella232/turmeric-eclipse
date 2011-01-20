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
 * @author yayu
 *
 */
public class BaseCodeGenModel {
	public static final String PARAM_GENTYPE = "-genType";
	public static final String PARAM_NAMESPACE = "-namespace";
	public static final String PARAM_SERVICE_LAYER_FILE = "-asl";
	public static final String PARAM_INTERFACE = "-interface";
	public static final String PARAM_ADMIN_NAME = "-adminname";
	public static final String PARAM_SERVICE_NAME = "-serviceName";
	public static final String PARAM_SCV = "-scv";
	public static final String PARAM_SICN = "-sicn";
	public static final String PARAM_PR = "-pr";
	public static final String PARAM_SLAYER = "-slayer";
	public static final String PARAM_SRC = "-src";
	public static final String PARAM_DEST = "-dest";
	public static final String PARAM_BIN = "-bin";
	public static final String PARAM_CN = "-cn";
	public static final String PARAM_SL = "-sl";
	public static final String PARAM_MDEST = "-mdest";
	public static final String PARAM_WSDL = "-wsdl";
	public static final String PARAM_GIP = "-gip";
	public static final String PARAM_GIN = "-gin";
	public static final String PARAM_AVI = "-avi";
	public static final String PARAM_JDEST = "-jdest";
	public static final String PARAM_CCGN = "-ccgn"; //Client Config Group Name
	public static final String PARAM_SCGN = "-scgn"; //Service Config Group Name
	public static final String PARAM_GT = "-gt"; //for generating the unit test of gentype serviceFromWSDLImpl
	public static final String PARAM_NS2PKG = "-ns2pkg"; //namespace to pacakge mappings
	public static final String PARAM_GSS = "-gss"; //an indicator to ensure that the impl class will always be generated even already exist
	public static final String PARAM_OWIC = "-owic"; //an indicator for whether to overwrite the impl class
	public static final String PARAM_CTNS = "-ctns"; //common types Namespace
	public static final String PARAM_ENVIRONMENT = "-environment"; //Client config environment
	public static final String PARAM_ENV_MAPPER = "-envMapper"; //environment mapper impl class name
	public static final String PARAM_CONSUMER_ID = "-consumerid"; //consumer id
	public static final String PARAM_JAVA_HOME = "-javahome"; //The full path of the JAVA HOME directory
	public static final String PARAM_JDK_HOME = "-jdkhome"; //The full path of the JDK HOME directory
	public static final String PARAM_DOMAIN = "-domain"; //command separated list of error domains
	public static final String PARAM_ERROR_LIBRARY_NAME = "-errorlibname"; //the name of the error library
	
	/*All|Client|ClientNoConfig|Server|ServerNoConfig|Proxy|Dispatcher|ConfigAll|ClientConfig|ServerConfig|
    GlobalServerConfig|GlocalClientConfig|Wsdl|Interface|Schema|SISkeleton|TypeMappings|WebXml|UnitTest|
    TestClient|ServiceOpProps|SecurityPolicyConfig|ServiceMetadataProps|ServiceIntfProjectProps*/
	public static final String GENTYPE_ALL = "All";
	public static final String GENTYPE_CLIENT = "Client";
	public static final String GENTYPE_CONSUMER = "Consumer";
	public static final String GENTYPE_CLIENT_NO_CONFIG = "ClientNoConfig";
	public static final String GENTYPE_SERVER = "Server";
	public static final String GENTYPE_SERVER_NO_CONFIG = "ServerNoConfig";
	public static final String GENTYPE_PROXY = "Proxy";
	public static final String GENTYPE_DISPATCHER = "Dispatcher";
	public static final String GENTYPE_CONFIG_ALL = "ConfigAll";
	public static final String GENTYPE_CLIENT_CONFIG = "ClientConfig";
	public static final String GENTYPE_SERVER_CONFIG = "ServerConfig";
	public static final String GENTYPE_GLOBAL_SERVER_CONFIG = "GlobalServerConfig";
	public static final String GENTYPE_GLOBAL_CLIENT_CONFIG = "GlobalClientConfig";
	public static final String GENTYPE_WSDL = "Wsdl";
	public static final String GENTYPE_INTERFACE = "Interface";
	public static final String GENTYPE_SCHEMA = "Schema";
	public static final String GENTYPE_SISKELETON = "SISkeleton";
	public static final String GENTYPE_TYPE_MAPPINGS = "TypeMappings";
	public static final String GENTYPE_WEB_XML = "WebXml";
	public static final String GENTYPE_UNIT_TEST = "UnitTest";
	public static final String GENTYPE_TEST_CLIENT = "TestClient";
	public static final String GENTYPE_SERVICE_OP_PROPS = "ServiceOpProps";
	public static final String GENTYPE_SECURITY_POLICY_CONFIG = "SecurityPolicyConfig";
	public static final String GENTYPE_SERVICE_METADATA_PROPS = "ServiceMetadataProps";
	public static final String GENTYPE_SERVICE_INTF_PROJECT_PROPS = "ServiceIntfProjectProps";
	public static final String GENTYPE_SERVICE_FROM_WSDL_INTF = "ServiceFromWSDLIntf";
	public static final String GENTYPE_SERVICE_FROM_WSDL_IMPL = "ServiceFromWSDLImpl";
	public static final String GENTYPE_ADDTYPE = "genTypeAddType";
	public static final String GENTYPE_DELETETYPE = "genTypeDeleteType";
	public static final String GENTYPE_CREATETYPELIBRARY = "genTypeCreateTypeLibrary";
	public static final String GENTYPE_CLEANBUILDTYPELIBRARY = "genTypeCleanBuildTypeLibrary";
	public static final String GENTYPE_INCRBUILDTYPELIBRARY = "genTypeIncrBuildTypeLibrary";
	public static final String GENTYPE_COMMAND_LINE_ALL = "genTypeCommandLineAll";
	public static final String GENTYPE_ERROR_LIB_ALL = "genTypeErrorLibAll";
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
	 * 
	 */
	public BaseCodeGenModel() {
		super();
	}

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

	public String getGenType() {
		return genType;
	}

	public void setGenType(String genType) {
		if (SUPPORTED_GENTYPES.contains(genType) == false)
			throw new IllegalArgumentException("Unsupported gentype->" + genType);
		this.genType = genType;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getNamespace(){
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getServiceLayerFile() {
		return serviceLayerFile;
	}

	public void setServiceLayerFile(String serviceLayerFile) {
		this.serviceLayerFile = serviceLayerFile;
	}

	public String getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getServiceImplClassName() {
		return serviceImplClassName;
	}

	public void setServiceImplClassName(String serviceImpl) {
		this.serviceImplClassName = serviceImpl;
	}

	public String getProjectRoot() {
		return projectRoot;
	}

	public void setProjectRoot(String projectRoot) {
		this.projectRoot = projectRoot;
	}

	public String getServiceLayer() {
		return serviceLayer;
	}

	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public String getOriginalWsdlUrl() {
		return originalWsdlUrl;
	}

	public void setOriginalWsdlUrl(String originalWsdlUrl) {
		this.originalWsdlUrl = originalWsdlUrl;
	}

	public String getNs2pkg() {
		return ns2pkg;
	}

	public void setNs2pkg(String ns2pkg) {
		this.ns2pkg = ns2pkg;
	}
	
	public String getGenFolder() {
		return genFolder;
	}

	public void setGenFolder(String genFolder) {
		this.genFolder = genFolder;
	}

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

	@Override
	public String toString() {
		return toString(this.genType, getCodeGenOptions());
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
