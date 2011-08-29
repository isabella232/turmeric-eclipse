/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.MessageProtocol;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAFileNotWritableException;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientConfig;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;


// TODO: Auto-generated Javadoc
/**
 * The Class SOAClientConfigUtil.
 *
 * @author smathew
 */
public class SOAClientConfigUtil {

	/** The Constant CLIENT_CONFIG. */
	private static final String CLIENT_CONFIG = "client-config";
	
	/** The Constant SERVICE_NAME. */
	private static final String SERVICE_NAME = "service-name";
	
	/** The Constant SERVICE_INTERFACE_CLASS_NAME. */
	private static final String SERVICE_INTERFACE_CLASS_NAME = "service-interface-class-name";
	
	/** The Constant SERVICE_LOCATION. */
	private static final String SERVICE_LOCATION = "service-location";
	
	/** The Constant CLIENT_INSTANCE_CONFIG. */
	private static final String CLIENT_INSTANCE_CONFIG = "client-instance-config";
	
	/** The Constant INVOCATION_OPTIONS. */
	private static final String INVOCATION_OPTIONS = "invocation-options";
	
	/** The Constant PREFERRED_TRANSPORT. */
	private static final String PREFERRED_TRANSPORT = "preferred-transport";
	
	/** The Constant REQUEST_DATA_BINDING. */
	private static final String REQUEST_DATA_BINDING = "request-data-binding";
	
	/** The Constant RESPONSE_DATA_BINDING. */
	private static final String RESPONSE_DATA_BINDING = "response-data-binding";
	
	/** The Constant INVOCATION_USE_CASE. */
	private static final String INVOCATION_USE_CASE = "invocation-use-case";
	
	/** The Constant MESSAGE_PROTOCOL. */
	private static final String MESSAGE_PROTOCOL = "message-protocol";
	
	/** The Constant NAME. */
	private static final String NAME = "name";
	
	/** The Constant PROTOCOL_PROCESSOR. */
	private static final String PROTOCOL_PROCESSOR = "protocol-processor";
	
	/** The Constant VERSION. */
	private static final String VERSION = "version";
	
	/** The Constant INDICATOR. */
	private static final String INDICATOR = "indicator";
	
	/** The Constant URL_PATTERN. */
	private static final String URL_PATTERN = "URL-pattern";
	
	/** The Constant TRANSPORT_HEADER. */
	private static final String TRANSPORT_HEADER = "transport-header";
	
	/** The Constant CONSUMER_ID. */
	private static final String CONSUMER_ID = "consumer-id";
	
	/** The Constant TRANSPORT_HEADER_NAME. */
	private static final String TRANSPORT_HEADER_NAME = "X-TURMERIC-MESSAGE-PROTOCOL";
	
	/** The Constant SOAP12. */
	private static final String SOAP12 = "SOAP12";
	
	/** The Constant SOAP11. */
	private static final String SOAP11 = "SOAP11";
	
	/** The Constant SOAP11_VERSION. */
	private static final String SOAP11_VERSION = "1.1";
	
	/** The Constant SOAP12_VERSION. */
	private static final String SOAP12_VERSION = "1.2";
	
	/** The Constant CLASS_NAME. */
	private static final String CLASS_NAME = "class-name";

	// private static final String CLASS_NAME_VALUE =
	// "org.ebayopensource.turmeric.sif.impl.protocolprocessor.soap.ClientSOAPProtocolProcessor";
	// //FIXME use the new turmeric name

	/**
	 * Parses the client config.
	 *
	 * @param iFile the i file
	 * @return the sOA client config
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	public static SOAClientConfig parseClientConfig(IFile iFile)
	throws IOException, JDOMException {
		SOAClientConfig soaClientConfig = new SOAClientConfig();

		File soaConfFile = iFile.getLocation().toFile();
		Document document = JDOMUtil.readXML(soaConfFile);
		Element clientConfigList = document.getRootElement();
		Namespace nameSpace = clientConfigList.getNamespace();
		if (clientConfigList != null) {
			Element clientConfig = clientConfigList.getChild(CLIENT_CONFIG,
					nameSpace);

			if (clientConfig != null) {
				final String fullServiceName = clientConfig
				.getAttributeValue(SERVICE_NAME);
				soaClientConfig.setFullyQualifiedServiceName(fullServiceName);
				final String serviceName = getServiceNameFromClientConfigServiceName(fullServiceName);
				soaClientConfig.setServiceName(serviceName);
				soaClientConfig.setTargetNamespace(StringUtils
						.substringBetween(fullServiceName, "{", "}"));
				Element serviceInterfaceClassName = clientConfig.getChild(
						SERVICE_INTERFACE_CLASS_NAME, nameSpace);
				if (serviceInterfaceClassName != null) {
					soaClientConfig
					.setServiceInterfaceClassName(serviceInterfaceClassName
							.getValue());
				}
				Element serviceLocation = clientConfig.getChild(
						SERVICE_LOCATION, nameSpace);
				if (serviceLocation != null) {
					soaClientConfig.setServiceLocation(serviceLocation
							.getValue());
				}
				Element clientInstanceConfig = clientConfig.getChild(
						CLIENT_INSTANCE_CONFIG, nameSpace);
				if (clientInstanceConfig != null) {
					Element invocationOptions = clientInstanceConfig.getChild(
							INVOCATION_OPTIONS, nameSpace);
					if (invocationOptions != null) {
						Element preferedTransport = invocationOptions.getChild(
								PREFERRED_TRANSPORT, nameSpace);
						if (preferedTransport != null) {
							soaClientConfig.setServiceBinding(preferedTransport
									.getAttributeValue(NAME));
						}
						Element requestDataBinding = invocationOptions
						.getChild(REQUEST_DATA_BINDING, nameSpace);
						if (requestDataBinding != null) {
							soaClientConfig
							.setRequestDataBinding(requestDataBinding
									.getValue());
						}
						Element responseDataBinding = invocationOptions
						.getChild(RESPONSE_DATA_BINDING, nameSpace);
						if (responseDataBinding != null) {
							soaClientConfig
							.setResponseDataBinding(responseDataBinding
									.getValue());
						}
						Element invocationUseCase = invocationOptions
						.getChild(INVOCATION_USE_CASE, nameSpace);
						if (invocationUseCase != null) {
							soaClientConfig
							.setInvocationUseCase(invocationUseCase
									.getValue());
						}
						Element messageProtocol = invocationOptions.getChild(
								MESSAGE_PROTOCOL, nameSpace);
						if (messageProtocol != null) {
							soaClientConfig.setMessageProtocol(messageProtocol
									.getValue());
						} else {
							soaClientConfig
							.setMessageProtocol(MessageProtocol.NONE
									.name());
						}
						Element consumerId = invocationOptions.getChild(
								CONSUMER_ID, nameSpace);
						if (consumerId != null) {
							soaClientConfig.setConsumerId(consumerId
									.getValue());
						}
					}
				}
			}
		}
		soaClientConfig.setDocument(document);
		soaClientConfig.setFile(iFile);
		return soaClientConfig;
	}

	/**
	 * Gets the service name from client config service name.
	 *
	 * @param serviceName the service name
	 * @return the service name from client config service name
	 */
	private static String getServiceNameFromClientConfigServiceName(
			String serviceName) {
		// this has bracket {namespace}serviceName
		if (StringUtils.lastIndexOf(serviceName, "}") != -1) {
			return StringUtils.substringAfterLast(serviceName, "}");
		}
		return serviceName;
	}
	
	/**
	 * Save.
	 *
	 * @param config the config
	 * @param protocalProcessorClassName the protocal processor class name
	 * @throws Exception the exception
	 */
	public static void save(SOAClientConfig config, String protocalProcessorClassName) throws Exception {
		save(config, false, protocalProcessorClassName);
	}

	/**
	 * Save.
	 *
	 * @param config the config
	 * @param removeDeprecatedElements the remove deprecated elements
	 * @param protocalProcessorClassName the protocal processor class name
	 * @throws Exception the exception
	 */
	public static void save(SOAClientConfig config, 
			boolean removeDeprecatedElements, String protocalProcessorClassName) throws Exception {
		if (!WorkspaceUtil.isResourceWritable(config.getFile())) {
			throw new SOAFileNotWritableException(config.getFile()
					.getLocation().toFile());
		}
		if (removeDeprecatedElements == true) {
			final File srcFile = config.getFile().getLocation().toFile();
			FileUtils.copyFile(srcFile, new File(srcFile.getCanonicalPath() + ".bak"), true);
		}

		Document document = config.getDocument();
		String serviceLocation = config.getServiceLocation();
		String serviceBinding = config.getServiceBinding();
		String requestDataBinding = config.getRequestDataBinding();
		String responseDataBinding = config.getResponseDataBinding();
		String consumerId = config.getConsumerId();
		String messageProtocol = config.getMessageProtocol();
		// We could add a check here.. This is one of the reason to avoid
		// the config tool parsing
		// service location
		Element clientConfigList = document.getRootElement();
		Namespace nameSpace = clientConfigList.getNamespace();
		if (clientConfigList != null) {
			Element clientConfig = clientConfigList.getChild(CLIENT_CONFIG,
					nameSpace);
			if (clientConfig != null) {
				Element serviceLocationElement = clientConfig.getChild(
						SERVICE_LOCATION, nameSpace);
				if (serviceLocationElement != null) {
					serviceLocationElement.setText(serviceLocation);
				}
				Element clientInstanceConfig = clientConfig.getChild(
						CLIENT_INSTANCE_CONFIG, nameSpace);
				if (clientInstanceConfig != null) {
					Element invocationOptions = clientInstanceConfig.getChild(
							INVOCATION_OPTIONS, nameSpace);
					if (invocationOptions != null) {
						Element preferedTransport = invocationOptions.getChild(
								PREFERRED_TRANSPORT, nameSpace);
						if (preferedTransport != null) {
							preferedTransport.getAttribute(NAME).setValue(
									serviceBinding);
						}

						Element requestDataBindingElement = invocationOptions
						.getChild(REQUEST_DATA_BINDING, nameSpace);
						if (requestDataBindingElement != null) {
							requestDataBindingElement
							.setText(requestDataBinding);
						}
						Element responseDataBindingElement = invocationOptions
						.getChild(RESPONSE_DATA_BINDING, nameSpace);
						if (responseDataBindingElement != null) {
							responseDataBindingElement
							.setText(responseDataBinding);

						}
						if (StringUtils.isNotBlank(consumerId)) {
							Element consumerIdElement = invocationOptions.getChild(
									CONSUMER_ID, nameSpace);
							if (consumerIdElement != null) {
								consumerIdElement.setText(consumerId);
							} else {
								Element elem = new Element(CONSUMER_ID, nameSpace);
								elem.setText(consumerId);
								invocationOptions.addContent(elem);
							}
						}

						if (removeDeprecatedElements == true) {
							invocationOptions.removeChild(INVOCATION_USE_CASE, nameSpace);
						}

						// if this is none
						// then we have to remove the protocol processor and the
						// message protocol element under invocation options
						if (StringUtils.equals(MessageProtocol.NONE.name(),
								messageProtocol)) {
							Element messageProtocolElement = invocationOptions
							.getChild(MESSAGE_PROTOCOL, nameSpace);
							if (messageProtocolElement != null) {
								invocationOptions
								.removeContent(messageProtocolElement);
							}
							removeProtocolProcessor(clientInstanceConfig,
									document);
						} else {
							Element messageProtocolElement = invocationOptions
							.getChild(MESSAGE_PROTOCOL, nameSpace);
							if (messageProtocolElement == null) {
								messageProtocolElement = new Element(
										MESSAGE_PROTOCOL, nameSpace);
								invocationOptions
								.addContent(messageProtocolElement);
							}
							messageProtocolElement.setText(messageProtocol);
							removeProtocolProcessor(clientInstanceConfig,
									document);
							String version = "";
							if (StringUtils.equals(messageProtocol,
									MessageProtocol.SOAP11.name())) {
								version = SOAP11_VERSION;
							} else {
								version = SOAP12_VERSION;
							}
							addProtocolProcessor(messageProtocol, version,
									clientInstanceConfig, document, protocalProcessorClassName);
						}
					}

				}
			}
		}
		JDOMUtil.outputDocument(document, config.getFile());
	}

	/**
	 * Removes the protocol processor.
	 *
	 * @param parentElement the parent element
	 * @param document the document
	 */
	private static void removeProtocolProcessor(Element parentElement,
			Document document) {
		Element protocolProcessorElement = parentElement.getChild(
				PROTOCOL_PROCESSOR, document.getRootElement().getNamespace());
		parentElement.removeContent(protocolProcessorElement);
	}

	/**
	 * Adds the protocol processor.
	 *
	 * @param name the name
	 * @param version the version
	 * @param parentElement the parent element
	 * @param document the document
	 * @param protocalProcessorClassName the protocal processor class name
	 */
	private static void addProtocolProcessor(String name, String version,
			Element parentElement, Document document, String protocalProcessorClassName) {

		Namespace nameSpace = document.getRootElement().getNamespace();
		Element protocolProcessorElement = new Element(PROTOCOL_PROCESSOR,
				nameSpace);
		protocolProcessorElement.setAttribute(VERSION, version);
		protocolProcessorElement.setAttribute(NAME, name);
		parentElement.addContent(protocolProcessorElement);

		Element indicator = new Element(INDICATOR, nameSpace);
		protocolProcessorElement.addContent(indicator);

		Element transportHeader = new Element(TRANSPORT_HEADER, nameSpace);
		transportHeader.setAttribute(NAME, TRANSPORT_HEADER_NAME);
		transportHeader.setText(name);
		indicator.addContent(transportHeader);

		Element classNameElement = new Element(CLASS_NAME, nameSpace);
		classNameElement.setText(protocalProcessorClassName);

		protocolProcessorElement.addContent(classNameElement);
	}
}
