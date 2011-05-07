/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.config.core;

/**
 * 
 * Model object holding the type library related configuration that
 * organizations wants to inject in the service creation flow. Some of them are
 * the request base type, response type, other included types, name space etc.
 * This class just holds the name value pair, does not have any intelligence as
 * such.
 * 
 * @author smathew
 * 
 */
public class SOAServiceConfiguration {
	
	/** The Constant KEY_BASE_REQ_TYPE_NAME. */
	public static final String KEY_BASE_REQ_TYPE_NAME = "BASE_REQ_TYPE";
	
	/** The Constant KEY_BASE_REQ_TYPE_NAMESPACE. */
	public static final String KEY_BASE_REQ_TYPE_NAMESPACE = "BASE_REQ_TYPE_NAMESPACE";
	
	/** The Constant KEY_BASE_RESP_TYPE_NAME. */
	public static final String KEY_BASE_RESP_TYPE_NAME = "BASE_RESP_TYPE";
	
	/** The Constant KEY_BASE_RESP_TYPE_NAMESPACE. */
	public static final String KEY_BASE_RESP_TYPE_NAMESPACE = "BASE_RESP_TYPE_NAMESPACE";
	
	/** The Constant KEY_INCLUDED_TYPES_WSDL. */
	public static final String KEY_INCLUDED_TYPES_WSDL = "INCLUDED_TYPES_WSDL";
	
	/** The Constant KEY_CLIENT_CONFIG_GROUP. */
	public static final String KEY_CLIENT_CONFIG_GROUP = "CLIENT_CONFIG_GROUP";
	
	/** The Constant KEY_SERVICE_CONFIG_GROUP. */
	public static final String KEY_SERVICE_CONFIG_GROUP = "SERVICE_CONFIG_GROUP";
	
	/** The Constant KEY_ENV_MAPPER_IMPL. */
	public static final String KEY_ENV_MAPPER_IMPL = "ENV_MAPPER_IMPL";

	private String baseRequestTypeName;
	private String baseResponseTypeName;
	private String baseRequestTypeNameSpace;
	private String baseResponseTypeNameSpace;
	private String typesInWSDL;
	private String clientConfigGroup;
	private String serviceConfigGroup;
	private String envMapperImpl;

	/**
	 * Instantiates a new sOA service configuration.
	 *
	 * @param baseRequestTypeName the base request type name
	 * @param baseResponseTypeName the base response type name
	 * @param baseRequestTypeNameSpace the base request type name space
	 * @param baseResponseTypeNameSpace the base response type name space
	 * @param typesInWSDL the types in wsdl
	 * @param clientConfigGroup the client config group
	 * @param serviceConfigGroup the service config group
	 * @param envMapperImpl the env mapper impl
	 */
	public SOAServiceConfiguration(String baseRequestTypeName,
			String baseResponseTypeName, String baseRequestTypeNameSpace,
			String baseResponseTypeNameSpace, String typesInWSDL, 
			String clientConfigGroup, String serviceConfigGroup,
			String envMapperImpl) {
		super();
		this.baseRequestTypeName = baseRequestTypeName;
		this.baseResponseTypeName = baseResponseTypeName;
		this.baseRequestTypeNameSpace = baseRequestTypeNameSpace;
		this.baseResponseTypeNameSpace = baseResponseTypeNameSpace;
		this.typesInWSDL = typesInWSDL;
		this.clientConfigGroup = clientConfigGroup;
		this.serviceConfigGroup = serviceConfigGroup;
		this.envMapperImpl = envMapperImpl;
	}

	/**
	 * All request types extend from this Base request type.
	 *
	 * @return the base request type name
	 */
	public String getBaseRequestTypeName() {
		return baseRequestTypeName;
	}

	/**
	 * All response types extend from this base response type.
	 *
	 * @return the base response type name
	 */
	public String getBaseResponseTypeName() {
		return baseResponseTypeName;
	}

	/**
	 * Name space of the base request type.
	 *
	 * @return the base request type name space
	 */
	public String getBaseRequestTypeNameSpace() {
		return baseRequestTypeNameSpace;
	}

	/**
	 * Name space of the base response type.
	 *
	 * @return the base response type name space
	 */
	public String getBaseResponseTypeNameSpace() {
		return baseResponseTypeNameSpace;
	}

	/**
	 * Types to be included in WSDL.
	 *
	 * @return the types in wsdl
	 */
	public String getTypesInWSDL() {
		return typesInWSDL;
	}

	/**
	 * The client config group name.
	 *
	 * @return the client config group
	 */
	public String getClientConfigGroup() {
		return clientConfigGroup;
	}

	/**
	 * The service config group name.
	 *
	 * @return the service config group
	 */
	public String getServiceConfigGroup() {
		return serviceConfigGroup;
	}

	/**
	 * The envrionment mapper implementation class name.
	 *
	 * @return the env mapper impl
	 */
	public String getEnvMapperImpl() {
		return envMapperImpl;
	}

}
