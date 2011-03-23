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
package org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.ebayopensource.turmeric.repository.v1.services.ArtifactContentTypes;
import org.ebayopensource.turmeric.repository.v1.services.BinaryContent;
import org.ebayopensource.turmeric.repository.v1.services.ExternalArtifact;
import org.ebayopensource.turmeric.repository.v1.services.ValidateArtifactRequest;
import org.ebayopensource.turmeric.repository.v1.services.assertionsservice.impl.AsyncTurmericASV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceInvocationRuntimeException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceRuntimeException;
import org.ebayopensource.turmeric.runtime.common.pipeline.Message;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContextAccessor;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;



/**
 * @author yayu
 *
 */
public class ServiceConsumerFactory<K> {
	private static final String EIDP_TOKEN = "X-EBAY-EIDP-TOKEN";
	private static final String EIDP_IDENTITY = "X-EBAY-EIDP-IDENTITY";
	protected void copyToken(Service service) throws ServiceException {
		MessageContext ctx = MessageContextAccessor.getContext();
		Message msg = ctx.getRequestMessage();
		String identity = msg.getTransportHeader(EIDP_IDENTITY);
		String token = msg.getTransportHeader(EIDP_TOKEN);
		service.setSessionTransportHeader(EIDP_IDENTITY, identity);
		service.setSessionTransportHeader(EIDP_TOKEN, token);
	}
	protected K getProxy(String serviceName) throws ServiceException {
		Service service = getService(serviceName);
		// we should expose CallBack to set headers and token
		copyToken(service);
		return service.getProxy();
	}
	/**
	 * Method returns an instance of Service which has been initilized for this
	 * Consumer
	 * 
	 */
	public Service getService(String serviceName) throws ServiceException { 
		//ClientServiceDesc serviceDesc = new ClientServiceDesc(id, serviceQName, config, requestPipeline, responsePipeline, requestDispatcher, responseDispatcher, operations, protocols, bindings, transports, typeMappings, classLoader, g11nOptions, loggingHandlers, serviceInterfaceClass, defRequestDataBinding, defResponseDataBinding, defTransportName, defTransport, defServiceLocationURL, serviceVersion, retryHandler, customErrorResponseAdapter, errorDataProviderClass, cacheProviderClass, autoMarkdownStateFactory, defRestRequestDataBinding, defRestResponseDataBinding, requestHeaderMappings, responseHeaderMappings, serviceLayers, urlPathInfo)
		/*Service service = new Service(serviceDesc, new URL("https://asset-repository.corp.ebay.com/AssertionsService/AssertionsService"), null,
				null);*/
		return ServiceFactory.create(serviceName, serviceName, null);
		//return service;
	}
	@SuppressWarnings("unchecked")
	public K getConsumer(Class<K> clazz, final String serviceName) {
		return (K) Proxy.newProxyInstance(
				clazz.getClassLoader(), 
				new Class[] { clazz }, 
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						K internal = null;
						try {
							internal = getProxy(serviceName);
							return method.invoke(internal, args);
						} catch (ServiceInvocationRuntimeException ex) {
							ex.printStackTrace();
							throw ex;
						} catch (ServiceException serviceException) {
							serviceException.printStackTrace();
							throw ServiceRuntimeException.wrap(serviceException);
						} catch (Exception ex) {
							ex.printStackTrace();
							throw ex;
						}
					}
				}
		);
	}
	
	public static void main(String[] args) {
		ServiceConsumerFactory<AsyncTurmericASV1> factory = new ServiceConsumerFactory<AsyncTurmericASV1>();
		AsyncTurmericASV1 consumer = factory.getConsumer(AsyncTurmericASV1.class, "TurmericASV1");
		final byte[] artifactContent = "Hello WOrld".getBytes();
		ExternalArtifact externalArtifact = new ExternalArtifact();
		BinaryContent binaryContent = new BinaryContent();
		binaryContent.setContent(artifactContent);
		
		externalArtifact.setBinaryContent(binaryContent);
		externalArtifact.setContentType(ArtifactContentTypes.valueOf("WSDL"));
		
		ValidateArtifactRequest validateArtifactRequest = new ValidateArtifactRequest();
		validateArtifactRequest.setArtifactContent(externalArtifact);
		consumer.validateArtifact(validateArtifactRequest);
		/*FindApplicationsForUserResponse response2 = consumer.findApplicationsForUser(new FindApplicationsForUserRequest());
		System.out.println("sync response:" + response2.getAck());
		ServiceConsumerFactory<AsyncApplicationCatalogService> factory2 = new ServiceConsumerFactory<AsyncApplicationCatalogService>();
		AsyncApplicationCatalogService consumer2 = factory2.getConsumer(AsyncApplicationCatalogService.class, "ApplicationCatalogService");
		FindApplicationsForUserResponse response3 = consumer2.findApplicationsForUser(new FindApplicationsForUserRequest());
		System.out.println("async response:" + response3.getAck());*/

	}

}

