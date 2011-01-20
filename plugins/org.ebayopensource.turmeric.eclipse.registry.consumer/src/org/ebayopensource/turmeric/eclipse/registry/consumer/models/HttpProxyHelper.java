/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.models;


public class HttpProxyHelper {
	private static String REPOSITORY_SERVICE = "RepositoryService";
	private static String REPOSITORYMETADATA_SERVICE = "RepositoryMetadataService";

	public static void setHttpProxy(String proxyHost, String proxyPort) {
		//set proxy settings in Gateway.
		setHttpProxySettings(proxyHost, proxyPort, true);
	}
	
	private static void setHttpProxySettings(String proxyHost, String proxyPort, boolean proxyEnabled) {
//		try {
//			ClientMessageProcessor.getInstance();
//
//			ClientServiceDescFactory.getInstance();
//
//			ClientConfigHolder clientConfigHolder = ClientConfigManager
//					.getInstance().getConfigForUpdate(REPOSITORY_SERVICE,
//							REPOSITORY_SERVICE);
//
//			MessageProcessorConfigHolder mpConfHolder = clientConfigHolder
//					.getMessageProcessorConfig();
//			Map<String, TransportOptions> transOptions = mpConfHolder
//					.getTransportOptions();
//			TransportOptions proxyTransPortOptions = transOptions
//					.get(SOAConstants.TRANSPORT_HTTP_11);
//
//			Map<String, String> options = proxyTransPortOptions.getProperties();
//			options.put(HTTPClientTransportConfig.PROXY_HOST, proxyHost);
//			options.put(HTTPClientTransportConfig.PROXY_PORT, proxyPort);
//			options.put(HTTPClientTransportConfig.PROXY_ENABLED, Boolean.toString(proxyEnabled));
//
//			// update
//			ClientConfigManager.getInstance().updateConfig(REPOSITORY_SERVICE,
//					REPOSITORY_SERVICE, clientConfigHolder);
//
//			// Initialize
//
//			/*
//			 * ClientServiceDescFactory.getInstance().reloadServiceDesc("OpeneBayParticipantInterfaceService",
//			 * "OpeneBayParticipantInterfaceService");
//			 */
//
//			ClientServiceConfigBeanManager.initConfigBean(clientConfigHolder);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
