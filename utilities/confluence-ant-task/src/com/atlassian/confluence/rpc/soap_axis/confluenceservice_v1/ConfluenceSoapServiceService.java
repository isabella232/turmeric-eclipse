/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * ConfluenceSoapServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1;

public interface ConfluenceSoapServiceService extends javax.xml.rpc.Service {
	public java.lang.String getConfluenceserviceV1Address();

	public com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1.ConfluenceSoapService getConfluenceserviceV1()
			throws javax.xml.rpc.ServiceException;

	public com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1.ConfluenceSoapService getConfluenceserviceV1(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
