/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway;

import java.net.MalformedURLException;
import java.net.URL;

import org.ebayopensource.turmeric.eclipse.registry.consumer.preferences.AssertionServicePreferenceInitializer;
import org.ebayopensource.turmeric.repository.v1.services.ApplyAssertionGroupsRequest;
import org.ebayopensource.turmeric.repository.v1.services.ApplyAssertionGroupsResponse;
import org.ebayopensource.turmeric.repository.v1.services.ArtifactContentTypes;
import org.ebayopensource.turmeric.repository.v1.services.AssertableArtifact;
import org.ebayopensource.turmeric.repository.v1.services.AssertionGroupAsset;
import org.ebayopensource.turmeric.repository.v1.services.AssertionGroupAssetTypes;
import org.ebayopensource.turmeric.repository.v1.services.BinaryContent;
import org.ebayopensource.turmeric.repository.v1.services.ExternalArtifact;
import org.ebayopensource.turmeric.repository.v1.services.ValidateArtifactRequest;
import org.ebayopensource.turmeric.repository.v1.services.ValidateArtifactResponse;
import org.ebayopensource.turmeric.repository.v1.services.assertionsservice.gen.SharedTurmericASV1Consumer;
import org.ebayopensource.turmeric.repository.v1.services.assertionsservice.impl.AsyncTurmericASV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.sif.service.RequestContext;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceInvokerOptions;

/**
 * @author asagarwal
 *
 */
public class AssertionsServiceConsumer extends SharedTurmericASV1Consumer {
	
	public final static String USERNAME = "logidex_adm";
	public final static String PASSWORD = "rm@ebay01";
	public final static String ASSERTIONS_LIB = "SystemAssets";
	
	public static String HOST_URL = "http://www.example.org/TrumericASV1"; 
	private static String ASSERTIONS_SERVICE = "TurmericASV1";
	public static final String AUTH_COOKIE = "X-TURMERIC-SECURITY-COOKIE";
	private static final String ASSERTIONS_SERVICE_CLIENT_NAME = ASSERTIONS_SERVICE + "Consumer";
	private static final String DEFAULT_ENVIRONMENT = "production";

	//handler for the single instance of the AssertionsServiceConsumer object
	private static AssertionsServiceConsumer fAssertionsServiceConsumer = null;
	
	private URL fServiceLocation = null;
	private AsyncTurmericASV1 fProxy = null;
	private String fTransportName = null;
	private String fUserName = "logidex_adm";;
	private String fPassword = "rm@ebay01";

	private static Service fService = null;
	private String fSecurityCookie = null;
	
	private AssertionsServiceConsumer(String url) throws MalformedURLException, ServiceException {
		super(ASSERTIONS_SERVICE_CLIENT_NAME, DEFAULT_ENVIRONMENT);
		this.fServiceLocation = new URL(url);
	}

	private AssertionsServiceConsumer(String transportName, String url) throws MalformedURLException, ServiceException {
		this(url);
		this.fTransportName = transportName;		
	}
	
	private AssertionsServiceConsumer(String url,String userName, String password) throws MalformedURLException, ServiceException {
		this(url);
		this.fUserName = userName;
		this.fPassword = password;
	}

	/**
	 * Static method to get the instance of the <code>AssertionsServiceConsumer</code>.
	 * @param url String URL of the server hosting the service, AssertionsService.
	 * @return AssertionsServiceConsumer Returns the reference to the service object.
	 * @throws MalformedURLException
	 */
	public static synchronized AssertionsServiceConsumer getInstance(String url) throws MalformedURLException, ServiceException
	{
		if (url == null) {
			url = AssertionServicePreferenceInitializer.getAssertionServiceEndpoint();
		}
		fAssertionsServiceConsumer = new AssertionsServiceConsumer(url);
		
		return fAssertionsServiceConsumer;
	}
	
	@Override
	protected AsyncTurmericASV1 getProxy() throws ServiceException {
		if (fService == null) {
			fService = ServiceFactory.create(ASSERTIONS_SERVICE, DEFAULT_ENVIRONMENT, ASSERTIONS_SERVICE_CLIENT_NAME, fServiceLocation);
		}

		ServiceInvokerOptions options = fService.getInvokerOptions();
		options.setTransportName(fTransportName);

		// get security cookie after first successful login
		if (fSecurityCookie == null) {
			fSecurityCookie = fService.getResponseContext().getTransportHeader(AUTH_COOKIE);
		}

		// Use security cookie if present or use userid/password
		RequestContext requestContext = fService.getRequestContext();

		if (fSecurityCookie != null) {
			requestContext.setTransportHeader(AUTH_COOKIE,
					fSecurityCookie);
		} else {
			requestContext.setTransportHeader(SOAHeaders.AUTH_USERID, 
					fUserName);
			requestContext.setTransportHeader(SOAHeaders.AUTH_PASSWORD,
					fPassword);
		}

		fProxy = fService.getProxy();
		return fProxy;
	}

	/**
	 * Sets the service handler to null.
	 */
	public static void invalidateService()
	{
		fService=null;
	}

	public String getUserName() {
		return fUserName;
	}

	public void setUserName(String name) {
		fUserName = name;
	}

	public String getPassword() {
		return fPassword;
	}

	public void setPassword(String password) {
		this.fPassword = password;
	}
	
	public URL getServiceLocation() {
		return this.fServiceLocation;
	}
	
	public ApplyAssertionGroupsResponse applyAssertionGroups(byte[] artifactContent, String artifactType)
	{
		ApplyAssertionGroupsRequest request = new ApplyAssertionGroupsRequest();

		AssertionGroupAsset reference = createAssertionGroup();
		request.getAssertionGroups().add(reference);
		
		AssertableArtifact artifact = createExternalArtifactWithBinaryContent(artifactContent);
		request.getArtifacts().add(artifact);
		
		ApplyAssertionGroupsResponse applyAssertionGroupsResponse = applyAssertionGroups(request);
		
		return applyAssertionGroupsResponse;
	}
	
	
	/**
	 * Validate external artifact.
	 * 
	 * @param artifactContent
	 * @param artifactType
	 * @return
	 */
	public ValidateArtifactResponse validateArtifact(byte[] artifactContent, String artifactType)
	{
		ExternalArtifact externalArtifact = new ExternalArtifact();
		BinaryContent binaryContent = new BinaryContent();
		binaryContent.setContent(artifactContent);
		
		externalArtifact.setBinaryContent(binaryContent);
		externalArtifact.setContentType(ArtifactContentTypes.valueOf(artifactType));
		
		ValidateArtifactRequest validateArtifactRequest = new ValidateArtifactRequest();
		validateArtifactRequest.setArtifactContent(externalArtifact);
		
		return validateArtifact(validateArtifactRequest);
	}
	
	private AssertionGroupAsset createAssertionGroup(){
		
		AssertionGroupAsset reference = new AssertionGroupAsset();
		reference.setAssetName("soa_wsdlAttributeRules_assertionGroup");
		reference.setLibraryName(ASSERTIONS_LIB); 
		reference.setVersion("1.0.0");
		reference.setAssetType(AssertionGroupAssetTypes.ASSERTION_GROUP);
		
		return reference;
	}
	
	private AssertableArtifact createExternalArtifactWithBinaryContent(byte[] artifactContent) {		
		AssertableArtifact artifact = new AssertableArtifact();
		ExternalArtifact artifactExternal = new ExternalArtifact();
		artifactExternal.setContentType(ArtifactContentTypes.WSDL);
		BinaryContent binaryContent = new BinaryContent();
		binaryContent.setContent(artifactContent);
		artifactExternal.setBinaryContent(binaryContent);
		artifact.setArtifactExternal(artifactExternal);
		
		return artifact;
	}
}
