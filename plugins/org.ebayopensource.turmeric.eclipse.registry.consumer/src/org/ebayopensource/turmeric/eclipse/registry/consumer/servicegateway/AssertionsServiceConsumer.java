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
		//if(null ==fAssertionsServiceConsumer)
		//{
			fAssertionsServiceConsumer = new AssertionsServiceConsumer(url);
		//}
		
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
//		System.out.println("\n***Starting testAssertionGroupsRequestWithMultipleGroupsAndExternalArtifact");
		ApplyAssertionGroupsRequest request = new ApplyAssertionGroupsRequest();

		AssertionGroupAsset reference = createAssertionGroup();
		request.getAssertionGroups().add(reference);
		
		AssertableArtifact artifact = createExternalArtifactWithBinaryContent(artifactContent);
		request.getArtifacts().add(artifact);
		
		ApplyAssertionGroupsResponse applyAssertionGroupsResponse = applyAssertionGroups(request);
		
		return applyAssertionGroupsResponse;
	}
	
//	public ApplyAssertionGroupsResponse applyAssertionGroups()
//	{
//		System.out.println("\n***Starting testAssertionGroupsRequestWithMultipleGroupsAndExternalArtifact");
//		ApplyAssertionGroupsRequest request = new ApplyAssertionGroupsRequest();
//
//		AssertionGroupAsset reference = createAssertionGroup();
//		request.getAssertionGroups().add(reference);
//		
//		AssertableArtifact artifact = createExternalArtifactWithPassingContent();
//		request.getArtifacts().add(artifact);
//		
//		ApplyAssertionGroupsResponse applyAssertionGroupsResponse = applyAssertionGroups(request);
//		
//		return applyAssertionGroupsResponse;
//	}
	
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
//		reference.setAssetName("soa_wsdl_assertionGroup"); 
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

//	private AssertableArtifact createExternalArtifactWithPassingContent() {		
//		AssertableArtifact artifact = new AssertableArtifact();
//		ExternalArtifact artifactExternal = new ExternalArtifact();
//		artifactExternal.setContentType(ArtifactContentTypes.WSDL);
//		artifactExternal.setBinaryContent(getBinaryContent("C:/FindingService.wsdl"));
//		artifact.setArtifactExternal(artifactExternal);
//		
//		return artifact;
//	}
//
//	public ApplyAssertionsResponse applyAssertions()
//	{
//		String assertionAssetName = "soa_attributesAreArrangedInRequiredOrder_assertion";
//		ApplyAssertionsRequest request = new ApplyAssertionsRequest();
//		Assertion assertion = createAssertion(assertionAssetName);
//		request.getAssertions().add(assertion);
//		
//		AssertableArtifact artifact = createArtifactForPositiveScenario(assertionAssetName);
//		request.getArtifacts().add(artifact);
//		
//		ApplyAssertionsResponse response = applyAssertions(request);
//		
//		return response;
//	}
	
//	private Assertion createAssertion(String assertionAssetName){
//		
//		Assertion assertion = new Assertion();
//		AssertionAsset reference = new AssertionAsset();
//		reference.setAssetName(assertionAssetName);
//		reference.setAssetType(AssertionAssetTypes.ASSERTION);
//		reference.setLibraryName(ASSERTIONS_LIB);
//		reference.setVersion("1.0.0");
//		assertion.setAssertionAsset(reference);
//		
//		return assertion;
//	}
	
//	private AssertableArtifact createArtifactForPositiveScenario(String assertionAssetName){
//		
//		AssertableArtifact artifact = new AssertableArtifact();
//		ExternalArtifact artifactExternal = new ExternalArtifact();
//		artifactExternal.setContentType(ArtifactContentTypes.WSDL);
//		artifactExternal.setBinaryContent(getBinaryContent("C:/ViewStore/OtherFiles/Sample_Pass.wsdl"));
//		artifact.setArtifactExternal(artifactExternal);
//		
//		return artifact;
//	}
	
//	private BinaryContent getBinaryContent(String resourceName) {		
//		BinaryContent content = new BinaryContent();
//		int count;
//	    int chunkSize = 10000;
//	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	    byte[] b = new byte[chunkSize];
//		InputStream in = null;
//		
//		try {
//			in = new FileInputStream(resourceName);
//			 while( ( count = in.read( b, 0, chunkSize ) ) > 0 )
//			        stream.write( b, 0, count );
//			      byte[] thebytes = stream.toByteArray();
//			      
//			      content.setContent(thebytes);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch(IOException ioe){
//	    	System.out.println("Error while reading"  + resourceName);
//			//TODO :  Handle correctly
//	    }
//	    finally {
//	    	try{
//	    		in.close();
//	    		//stream.close();
//	    		}
//	    	catch(IOException ioe){
//	    		System.out.println("Could not close stream properly" + ioe.getMessage());
//	    	}
//	    }
//	    
//	    return content;
//	}
	
	/**
	 * Entry Point
	 * @param args
	 */		
	public static void main(String[] args) {
//		try {
//			AssertionsServiceConsumer consumer = new AssertionsServiceConsumer(HOST_URL);
//			consumer.setUserName(USERNAME);
//			consumer.setPassword(PASSWORD);
//			
//			// Plugin will not use this 
////			ApplyAssertionsResponse applyAssertionResponse = consumer.applyAssertions();
////			AssertionReport report = applyAssertionResponse.getAssertionReport();
////			System.out.print("\n\nApplyAssertion============Ack value: "+applyAssertionResponse.getAck());
////			printErrorsInResponse(applyAssertionResponse.getErrorMessage());
//			
//			long startTime = System.currentTimeMillis();
//			System.out.println("Start Time = " + startTime);
//			ApplyAssertionGroupsResponse groupResponse = consumer.applyAssertionGroups();
//			long totalTime = System.currentTimeMillis() - startTime;
//			System.out.println("Total Time = " + totalTime);
//			
//			startTime = System.currentTimeMillis();
//			System.out.println("Start Time = " + startTime);
//			groupResponse = consumer.applyAssertionGroups();
//			totalTime = System.currentTimeMillis() - startTime;
//			System.out.println("Total Time = " + totalTime);
//			System.out.print("\n\nApplyAssertionGroups============Ack value: "+groupResponse.getAck());
//			
////			ErrorMessage errorMsg = groupResponse.getErrorMessage();
////			printErrorsInResponse(errorMsg);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * @param errorMsg
	 */
//	private static void printErrorsInResponse(ErrorMessage errorMsg) {
//		if(errorMsg != null)
//		{
//			System.out.print("\n\nError(s)\n");
//			List<ErrorData> errorList = errorMsg.getError();
//			for(Iterator<ErrorData> iterator = errorList.iterator(); iterator.hasNext();)
//			{
//				ErrorData errorData = (ErrorData) iterator.next();
//				System.out.print("======>errorData.getErrorId() = "+errorData.getErrorId());
//				System.out.print("======>errorData.getMessage() = "+errorData.getMessage());
//				System.out.print("\n\n");
//			}
//		}
//	}
}
