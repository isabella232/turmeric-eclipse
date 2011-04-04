/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.template.wsdl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.TemplateBinding;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.template.wsdl.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.WTPTypeLibUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.internal.generator.extension.ContentGeneratorExtensionFactoryRegistry;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPartCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.XSDComponentHelper;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * The Utility class to serve the Service template processors and other template
 * classes. This class has methods to create the WSDL, manipulate it i.e add
 * operation, elements etc. Basically this class manipulates the WSDL using the
 * WTP EMF model and expose the manipulating functionalities SOA require. It
 * also has getters for accessing the the WSDL properties like the port,
 * operations, elements etc. Has the binding generation responsibility also.
 * 
 * @author smathew
 * 
 */
public class ServiceTemplateUtil {

	/**
	 * Gets the default port in a definition object. The default is the first
	 * port type available in the WSDL. If the definition does not have any port
	 * types then it creates a new one, add it to the definition and returns it.
	 * 
	 * @param definition
	 * @return
	 */
	public static PortType getDefaultPort(Definition definition) {
		PortType portType = null;
		if (definition.getPortTypes() != null) {
			Iterator<?> iterator = definition.getPortTypes().values().iterator();
			while (iterator.hasNext()) {
				Object objPortType = iterator.next();
				if (objPortType != null && objPortType instanceof PortType) {
					portType = (PortType) objPortType;
					break;
				}
			}
		}
		if (portType == null) {
			portType = WSDLFactory.eINSTANCE.createPortType();
			portType.setQName(new QName(definition.getTargetNamespace(),
					ServiceTemplateConstants.DEFAULT_PORT_NAME));
			portType.setEnclosingDefinition(definition);
			definition.addPortType(portType);
		}
		return portType;
	}

	/**
	 * Returns true if the operation with with given name exists in the given
	 * port type. This is a case sensitive API. Remember an operation with the
	 * same name can exist in the WSDL under a different port type and clients
	 * are supposed to use this API with different port types to achieve a full
	 * length uniqueness.
	 * 
	 * @param portType
	 * @param opName
	 * @return
	 */
	public static boolean operationExists(PortType portType, String opName) {
		for (Object operationObject : portType.getOperations()) {
			if (operationObject instanceof Operation) {
				if (StringUtils.equals(((Operation) operationObject).getName(),
						opName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Adds an operation with the given name to the port type. Does not check
	 * for existing operations. In that case clients can use
	 * 
	 * @see ServiceTemplateUtil#operationExists(PortType, String) API before
	 *      calling this API.
	 * 
	 * @param portType
	 * @param operationName
	 * @return
	 */
	public static Operation addOperation(PortType portType, String operationName) {
		Operation operation = WSDLFactory.eINSTANCE.createOperation();
		operation.setName(operationName);
		operation.setEnclosingDefinition(portType.getEnclosingDefinition());
		if (portType.getElement() != null && portType.getElement().getOwnerDocument() != null) {
			//adding the wsdl:documentation tag to the PortType->Operation
			final Document ownerDoc = portType.getElement().getOwnerDocument();
			final Element element = ownerDoc.createElement(
					ServiceTemplateConstants.TAG_DOCUMENTATION_ELEMENT);
			element.appendChild(ownerDoc.createTextNode(
					ServiceTemplateConstants.DEFAULT_DOCUMENT_TEXT));
			operation.setDocumentationElement(element);
		}
		
		portType.addOperation(operation);
		return operation;
	}

	/**
	 * Adds the specified input parameter to the operation, creates a message
	 * for it and return the part. Remember for setting the input parameter we
	 * need the message and part to be added as well.
	 * 
	 * @param operation
	 * @param inputParamName
	 * @return
	 * @throws CommandFailedException
	 */
	public static Part addInput(Operation operation, String inputParamName)
			throws CommandFailedException {
		MessageReference messageReference = WSDLFactory.eINSTANCE.createInput();
		messageReference.setEnclosingDefinition(operation
				.getEnclosingDefinition());
		operation.setInput((Input) messageReference);
		return createMessage(operation, inputParamName, messageReference);

	}

	/**
	 * Adds the specified output parameter to the operation, creates a message
	 * for it and return the part. Remember for setting the output parameter we
	 * need the message and part to be added as well.
	 * 
	 * @param operation
	 * @param outputParamName
	 * @return
	 * @throws CommandFailedException
	 */
	public static Part addOutput(Operation operation, String outputParamName)
			throws CommandFailedException {
		MessageReference messageReference = WSDLFactory.eINSTANCE
				.createOutput();
		messageReference.setEnclosingDefinition(operation
				.getEnclosingDefinition());
		operation.setOutput((Output) messageReference);
		return createMessage(operation, outputParamName, messageReference);
	}

	/**
	 * Creates the message with the given message name. The WSDL definition is
	 * automatically discovered from the operation passed. Finally the part is
	 * also created with this message and returned to the client.
	 * 
	 * @param operation
	 * @param messageName
	 * @param messageReference
	 * @return
	 * @throws CommandFailedException
	 */
	public static Part createMessage(Operation operation, String messageName,
			MessageReference messageReference) throws CommandFailedException {
		Definition definition = operation.getEnclosingDefinition();
		AddMessageCommand command = new AddMessageCommand(definition,
				messageName, false);
		command.run();
		messageReference.setEMessage((Message) command.getWSDLElement());
		return createPart((Message) command.getWSDLElement());
	}

	/**
	 * Creates the part for the message, the name of the part is the default
	 * name and the name space and name are discovered from the message
	 * parameter. It used the WTP EMF based command internally.
	 * 
	 * @param message
	 * @return
	 */
	public static Part createPart(Message message) {
		AddPartCommand command = new AddPartCommand(message,
				ServiceTemplateConstants.DEFAULT_PART_NAME, message.getQName()
						.getNamespaceURI(), message.getQName().getLocalPart(),
				false);
		command.run();
		return (Part) command.getWSDLElement();
	}

	/**
	 * Wrapper over the method
	 * 
	 * @see ServiceTemplateUtil#createXSDElementDefinition(String, Part, String,
	 *      String).
	 * 
	 * The input parameter element name is computed from the part name using the
	 * WTP API and the operation parameter. The base service request type name
	 * and name space are parsed from the "service_config.properties" and passed
	 * to the API
	 * 
	 * @param part
	 * @param operation
	 * @return
	 * @throws CommandFailedException
	 */
	public static XSDComplexTypeDefinition createInputComplexType(Part part,
			Operation operation, boolean typeFolding)
			throws CommandFailedException {
		return createXSDElementDefinition(NameUtil.getPartName(operation
				.getEInput()), part, GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getConfigurationRegistry()
				.getBaseServiceRequestType(), GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry().getBaseServiceRequestNameSpace(),
				typeFolding, operation.getEnclosingDefinition()
						.getTargetNamespace());

	}

	/**
	 * Wrapper over the method
	 * 
	 * @see ServiceTemplateUtil#createXSDElementDefinition(String, Part, String,
	 *      String).
	 * 
	 * The output parameter element name is computed from the part name using
	 * the WTP API and the operation parameter. The base service response type
	 * name and name space are parsed from the "service_config.properties" and
	 * passed to the API
	 * 
	 * @param part
	 * @param operation
	 * @return
	 * @throws CommandFailedException
	 */
	public static XSDComplexTypeDefinition createOutputComplexType(Part part,
			Operation operation, boolean typeFolding)
			throws CommandFailedException {
		return createXSDElementDefinition(NameUtil.getPartName(operation
				.getEOutput()), part, GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getConfigurationRegistry()
				.getBaseServiceResponseType(), GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry().getBaseServiceResponseNameSpace(),
				typeFolding, operation.getEnclosingDefinition()
						.getTargetNamespace());
	}

	/**
	 * Add the element declaration based on the parameter element passed. The
	 * default w3c data types will carry the prefix from corresponding to w3c
	 * name space and rest all will be computed and if the prefix does not exist
	 * then it is added before using it.
	 * 
	 * @param parameterElement
	 * @param complexTypeDefinition
	 * @throws CommandFailedException
	 */
	public static void addElementDeclaration(
			IParameterElement parameterElement,
			XSDComplexTypeDefinition complexTypeDefinition, boolean typeFolding)
			throws CommandFailedException {
		String prefixedElementType = "";
		if (parameterElement.getDatatype() instanceof String) {
			prefixedElementType = TypeLibraryUIActivator.getPrefix(complexTypeDefinition
					.getSchema(), SOATypeLibraryConstants.W3C_NAMEPSACE)
					+ (String) parameterElement.getDatatype();
		} else {
			try {
				LibraryType libElementType = (LibraryType) parameterElement
						.getDatatype();
				String elementName = libElementType.getName();
				String elementNamespace = typeFolding ? complexTypeDefinition
						.getTargetNamespace() : TypeLibraryUtil
						.getNameSpace(libElementType);
				prefixedElementType = TypeLibraryUIActivator.getPrefix(
						complexTypeDefinition.getSchema(), elementNamespace)
						+ elementName;
			} catch (Exception e) {
				throw new CommandFailedException(StringUtil.formatString(
						SOAMessages.TYPE_NOT_FOUND, parameterElement
								.getDatatype()));
			}
		}
		XSDParticle xsdParticle = TypeLibraryUIActivator.createXSDElementDeclaration(
				parameterElement.getName(), prefixedElementType,
				parameterElement.getMinOccurs(), parameterElement
						.getMaxOccurs());
		XSDModelGroup xsdModelGroup = TypeLibraryUIActivator
				.getModelGroup(complexTypeDefinition);
		xsdModelGroup.getContents().add(xsdParticle);

	}

	/**
	 * Adds a binding to the definition model being used. Here we have both SOAP
	 * and HTTP bindings generation logic embedded. First we parse all the
	 * bindings to see if we have an existing binding. If we don't have one the
	 * we will add it. Note there is no hard coding for HTTP or SOAP here, but
	 * is there in the UI and we just consult the WTP model before adding it.
	 * 
	 * @param definition
	 * @param bindings
	 * @return
	 */
	public static Binding addBinding(Definition definition,
			Set<TemplateBinding> bindings) {
		Binding binding = null;

		if (definition.getBindings() != null) {
			Iterator iterator = definition.getBindings().values().iterator();
			while (iterator.hasNext()) {
				Object objBinding = iterator.next();
				if (objBinding != null && objBinding instanceof Binding) {
					binding = (Binding) objBinding;
					BindingGenerator bindingGenerator = new BindingGenerator(
							definition, binding);
					for (TemplateBinding templBinding : bindings) {
						// TODO:This is bad we are identifying the type
						// of binding (ie if it is http or soap)
						// by the name
						if (StringUtils.containsIgnoreCase(binding.getQName()
								.getLocalPart(), templBinding.name())) {
							if (bindingGenerator.getContentGenerator() == null) {
								bindingGenerator.setOverwrite(true);
								bindingGenerator
										.setContentGenerator(ContentGeneratorExtensionFactoryRegistry
												.getInstance()
												.getGeneratorClassFromName(
														templBinding.name()));

							}
							bindingGenerator.generateBinding();

						}
					}

				}
			}
		}

		if (binding == null) {
			binding = WSDLFactory.eINSTANCE.createBinding();
			binding.setQName(new QName(definition.getTargetNamespace(),
					ServiceTemplateConstants.DEFAULT_BINDING_NAME));
			binding.setEnclosingDefinition(definition);
			definition.addBinding(binding);
			BindingGenerator bindingGenerator = new BindingGenerator(
					definition, binding);
			bindingGenerator.generateBinding();
		}
		return binding;
	}

	/**
	 * Creates an element definition with the given element name. The parent
	 * type of this definition would be the base element name parameter and the
	 * name space is the base element name space. THe definition is derived from
	 * the part and the anonymous type is also created based on the part. This
	 * API also makes sure that the parent type is imported before usage.
	 * 
	 * @param elementName
	 * @param part
	 * @param baseElementName
	 * @param baseElementNameSpace
	 * @return
	 */
	public static XSDComplexTypeDefinition createXSDElementDefinition(
			String elementName, Part part, String baseElementName,
			String baseElementNameSpace, boolean typeFolding,
			String targetNameSpace) {
		XSDElementDeclaration elementDef = XSDComponentHelper
				.createAnonymousXSDElementDefinition(elementName, part);

		XSDComplexTypeDefinition complexType = XSDFactory.eINSTANCE
				.createXSDComplexTypeDefinition();
		complexType.setName(StringUtils.capitalize(elementName));
		complexType.setTargetNamespace(part.getEnclosingDefinition()
				.getTargetNamespace());

		XSDParticle newXSDParticle = XSDFactory.eINSTANCE.createXSDParticle();
		XSDModelGroup newXSDModelGroup = XSDFactory.eINSTANCE
				.createXSDModelGroup();
		newXSDModelGroup.setCompositor(XSDCompositor.SEQUENCE_LITERAL);
		newXSDParticle.setContent(newXSDModelGroup);
		complexType.setContent(newXSDParticle);

		elementDef.setTypeDefinition(complexType);
		XSDComplexTypeDefinition complexTypeDefition = XSDComponentHelper
				.createXSDComplexTypeDefiniion(complexType.getName(), part);

		XSDComplexTypeDefinition restriction = XSDFactory.eINSTANCE
				.createXSDComplexTypeDefinition();

		if (!StringUtils.isEmpty(baseElementName)
				&& !StringUtils.isEmpty(baseElementNameSpace)) {
			if (!typeFolding)
				restriction.setName(TypeLibraryUIActivator.getPrefix(complexTypeDefition
						.getSchema(), baseElementNameSpace)
						+ baseElementName);
			else
				restriction.setName(TypeLibraryUIActivator.getPrefix(complexTypeDefition
						.getSchema(), targetNameSpace)
						+ baseElementName);
			complexTypeDefition
					.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
			complexTypeDefition.setBaseTypeDefinition(restriction);
		}
		TypeLibraryUIActivator.addDocumentation(complexTypeDefition, "Document goes here");
		return complexTypeDefition;
	}

	/**
	 * Computes the dependency of the parameter library type and in line the
	 * WSDL with the computed types. Duplicate types will be ignored. This is
	 * more a Wrapper over the method
	 * 
	 * @see WTPTypeLibUtil#inlineType(LibraryType, Definition, boolean, boolean)
	 * 
	 * @param libraryType
	 * @param definition
	 * @param typeFolding
	 * @throws Exception
	 */
	public static void inlineTypesInWSDL(LibraryType libraryType,
			Definition definition, boolean typeFolding) throws Exception {
		ArrayList<LibraryType> librarylist = new ArrayList<LibraryType>();
		librarylist.add(libraryType);
		for (LibraryType loopLibraryType : SOAGlobalRegistryAdapter.getInstance()
				.getGlobalRegistry().getDependentParentTypeFiles(libraryType)) {
			librarylist.add(loopLibraryType);
		}
		for (LibraryType selectedType : librarylist) {
			WTPTypeLibUtil.inlineType(selectedType, definition, true,
					typeFolding);
		}
	}

	private static void addImportDirective(XSDTypeDefinition typeDefinition,
			String libElementNamespace) {
		if (!StringUtil.broadEquals(typeDefinition.getSchema()
				.getTargetNamespace(), libElementNamespace)
				&& !importExists(typeDefinition.getSchema(),
						libElementNamespace)) {
			XSDImport xsdSchemaDirective = XSDFactory.eINSTANCE
					.createXSDImport();
			xsdSchemaDirective.setNamespace(libElementNamespace);
			typeDefinition.getSchema().getContents().add(0, xsdSchemaDirective);
		}
	}

	private static boolean importExists(XSDSchema schema,
			String libElementNamespace) {
		for (Iterator i = schema.getContents().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof XSDImport) {
				if (StringUtil.broadEquals(((XSDImport) o).getNamespace(),
						libElementNamespace)) {
					return true;
				}
			}
		}
		return false;
	}

}
