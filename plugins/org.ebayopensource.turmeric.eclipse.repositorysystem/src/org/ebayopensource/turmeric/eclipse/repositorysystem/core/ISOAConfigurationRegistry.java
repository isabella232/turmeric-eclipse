/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

/**
 * The Interface ISOAConfigurationRegistry.
 *
 * @author smathew
 * 
 * This class provides build system specific configurations
 */
public interface ISOAConfigurationRegistry {
	
	/**
	 * The name of the organization this configuration represents.
	 *
	 * @return the organization name
	 */
	public String getOrganizationName();

	/**
	 * Returns the name of the type from which all request types should extend
	 * from It can be null and in that case there will be no base type.
	 *
	 * @return the base service request type
	 */
	public String getBaseServiceRequestType();

	/**
	 * Returns the name of the type from which all response types should extend
	 * from It can be null and in that case there will be no base type.
	 *
	 * @return the base service response type
	 */
	public String getBaseServiceResponseType();

	/**
	 * Returns the name space of the base request type.
	 *
	 * @return the base service request name space
	 */
	public String getBaseServiceRequestNameSpace();

	/**
	 * Returns the name space of the base response type.
	 *
	 * @return the base service response name space
	 */
	public String getBaseServiceResponseNameSpace();

	/**
	 * Returns the list of types to be included in the template wsdl.
	 *
	 * @return the types in wsdl
	 */
	public String getTypesInWsdl();
	
	/**
	 * Returns the client config group name for the underlying organization.
	 *
	 * @return the client config group
	 */
	public String getClientConfigGroup();
	
	/**
	 * Returns the service config group name for the underlying organization.
	 *
	 * @return the service config group
	 */
	public String getServiceConfigGroup();
	
	/**
	 * Gets the environment mapper impl.
	 *
	 * @return the environment mapper implementation class name
	 */
	public String getEnvironmentMapperImpl();

}
