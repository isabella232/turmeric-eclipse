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
	public static final String KEY_BASE_REQ_TYPE_NAME = "BASE_REQ_TYPE";
	public static final String KEY_BASE_REQ_TYPE_NAMESPACE = "BASE_REQ_TYPE_NAMESPACE";
	public static final String KEY_BASE_RESP_TYPE_NAME = "BASE_RESP_TYPE";
	public static final String KEY_BASE_RESP_TYPE_NAMESPACE = "BASE_RESP_TYPE_NAMESPACE";
	public static final String KEY_INCLUDED_TYPES_WSDL = "INCLUDED_TYPES_WSDL";
	public static final String KEY_CLIENT_CONFIG_GROUP = "CLIENT_CONFIG_GROUP";
	public static final String KEY_SERVICE_CONFIG_GROUP = "SERVICE_CONFIG_GROUP";
	public static final String KEY_ENV_MAPPER_IMPL = "ENV_MAPPER_IMPL";

	private String baseRequestTypeName;
	private String baseResponseTypeName;
	private String baseRequestTypeNameSpace;
	private String baseResponseTypeNameSpace;
	private String typesInWSDL;
	private String clientConfigGroup;
	private String serviceConfigGroup;
	private String envMapperImpl;

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
	 * All request types extend from this Base request type
	 * 
	 * @return
	 */
	public String getBaseRequestTypeName() {
		return baseRequestTypeName;
	}

	/**
	 * All response types extend from this base response type
	 * 
	 * @return
	 */
	public String getBaseResponseTypeName() {
		return baseResponseTypeName;
	}

	/**
	 * Name space of the base request type.
	 * 
	 * @return
	 */
	public String getBaseRequestTypeNameSpace() {
		return baseRequestTypeNameSpace;
	}

	/**
	 * Name space of the base response type.
	 * 
	 * @return
	 */
	public String getBaseResponseTypeNameSpace() {
		return baseResponseTypeNameSpace;
	}

	/**
	 * Types to be included in WSDL.
	 * 
	 * @return
	 */
	public String getTypesInWSDL() {
		return typesInWSDL;
	}

	/**
	 * The client config group name
	 * @return
	 */
	public String getClientConfigGroup() {
		return clientConfigGroup;
	}

	/**
	 * The service config group name
	 * @return
	 */
	public String getServiceConfigGroup() {
		return serviceConfigGroup;
	}

	/**
	 * The envrionment mapper implementation class name
	 * @return
	 */
	public String getEnvMapperImpl() {
		return envMapperImpl;
	}

}
