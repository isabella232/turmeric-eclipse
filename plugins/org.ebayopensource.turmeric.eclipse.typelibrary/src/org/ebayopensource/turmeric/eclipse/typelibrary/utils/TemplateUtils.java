/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.wst.AddImportCommand;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.wst.xsd.ui.internal.common.util.XSDCommonUIUtils;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;

/**
 * @author smathew
 * 
 * Utility class for templating. Might move to core. if WSDL also needs this
 */
public class TemplateUtils {
	public static final int NO_OCCURS = -2;
	public static final int UNBOUND= -1;
	
	public static void formatChild(Element child) {
		if (child instanceof IDOMNode) {
			IDOMModel model = ((IDOMNode) child).getModel();
			try {
				model.aboutToChangeModel();
				IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
				formatProcessor.formatNode(child);
			} finally {
				model.changedModel();
			}
		}
	}

	public static String formatContents(String contents) throws IOException,
			CoreException {
		FormatProcessorXML formatProcessor = new FormatProcessorXML();
		return formatProcessor.formatContent(contents);
	}

	public static void addDocumentation(XSDConcreteComponent component,
			String docText) {
		XSDAnnotation xsdAnnotation = XSDCommonUIUtils.getInputXSDAnnotation(
				component, true);
		Element element = xsdAnnotation.getElement();

		List documentationList = xsdAnnotation.getUserInformation();
		Element documentationElement = null;
		boolean documentationExists = false;
		if (documentationList.size() > 0) {
			documentationExists = true;
			documentationElement = (Element) documentationList.get(0);
		}

		if (documentationElement == null) {
			documentationElement = xsdAnnotation.createUserInformation(null);
			element.appendChild(documentationElement);
			formatChild(documentationElement);
			xsdAnnotation.updateElement();
			xsdAnnotation.setElement(element);
		}

		if (documentationElement.hasChildNodes()) {
			if (documentationElement instanceof IDOMElement) {
				IDOMElement domElement = (IDOMElement) documentationElement;

				Node firstChild = documentationElement.getFirstChild();
				Node lastChild = documentationElement.getLastChild();
				int start = 0;
				int end = 0;

				IDOMNode first = null;
				if (firstChild instanceof IDOMNode) {
					first = (IDOMNode) firstChild;
					start = first.getStartOffset();
				}
				if (lastChild instanceof IDOMNode) {
					IDOMNode last = (IDOMNode) lastChild;
					end = last.getEndOffset();
				}

				if (domElement != null) {

					domElement.getModel().getStructuredDocument().replaceText(
							documentationElement, start, end - start, docText);
				}
			}
		} else {
			if (docText.length() > 0) {

				Node childNode = documentationElement.getOwnerDocument()
						.createTextNode(docText);
				documentationElement.appendChild(childNode);
			}
		}
		formatChild(xsdAnnotation.getElement());
	}

	public static XSDModelGroup getModelGroup(XSDComplexTypeDefinition cType) {
		XSDParticle particle = null;
		XSDComplexTypeContent xsdComplexTypeContent = cType.getContent();
		if (xsdComplexTypeContent instanceof XSDParticle) {
			particle = (XSDParticle) xsdComplexTypeContent;
		}

		if (particle == null) {
			return null;
		}

		Object particleContent = particle.getContent();
		XSDModelGroup group = null;

		if (particleContent instanceof XSDModelGroupDefinition) {
			group = ((XSDModelGroupDefinition) particleContent)
					.getResolvedModelGroupDefinition().getModelGroup();
		} else if (particleContent instanceof XSDModelGroup) {
			group = (XSDModelGroup) particleContent;
		}

		if (group == null) {
			return null;
		}

		return group;
	}

	public static void addElementDeclaration(
			XSDComplexTypeDefinition complexTypeDefinition,
			ComplexTypeParamModel model, String elementName,
			Object elementType, int minOccurs, int maxOccurs)
			throws CommandFailedException {
		String prefixedElementType = "";
		boolean isPrimitive = true;
		try {
			if (elementType instanceof String) {
				prefixedElementType = getPrefix(complexTypeDefinition
						.getSchema(), SOATypeLibraryConstants.W3C_NAMEPSACE)
						+ elementType;
			} else {
				isPrimitive = false;
				LibraryType libElementType = (LibraryType) elementType;
				prefixedElementType = getPrefix(complexTypeDefinition
						.getSchema(), TypeLibraryUtil
						.getNameSpace(libElementType))
						+ libElementType.getName();
			}
		} catch (Exception e) {
			throw new CommandFailedException(
					"Could not get the name space for the type "
							+ elementType
							+ " from the registry. Please fix this issue and try again.");
		}
		XSDParticle xsdParticle = createXSDElementDeclaration(elementName,
				prefixedElementType, minOccurs, maxOccurs);
		XSDModelGroup xsdModelGroup = getModelGroup(complexTypeDefinition);
		xsdModelGroup.getContents().add(0, xsdParticle);
		if (!isPrimitive)
			addImport((LibraryType) elementType, complexTypeDefinition, model
					.getTypeName(), model.getVersion(), model
					.getTypeLibraryName());
	}

	public static void addAttributeDeclarations(
			XSDComplexTypeDefinition complexTypeDefinition,
			ComplexTypeSCParamModel model, String attrName, Object attrType,
			String attrDoc) throws CommandFailedException {
		String prefixedAttrType = "";
		boolean isPrimitive = true;
		try {
			if (attrType instanceof String) {// this is a primitive type
				prefixedAttrType = getPrefix(complexTypeDefinition.getSchema(),
						SOATypeLibraryConstants.W3C_NAMEPSACE)
						+ attrType;
			} else {// this is a library Type
				isPrimitive = false;
				LibraryType libAttrType = (LibraryType) attrType;

				prefixedAttrType = getPrefix(complexTypeDefinition.getSchema(),
						TypeLibraryUtil.getNameSpace(libAttrType))
						+ libAttrType.getName();

			}
		} catch (Exception e) {
			throw new CommandFailedException(
					"Could not get the name space for the type "
							+ attrType
							+ " from the registry. Please fix this issue and try again.");
		}
		XSDAttributeUse attr = createXSDAttrDeclaration(attrName,
				prefixedAttrType);
		complexTypeDefinition.getAttributeContents().add(attr);
		if (!isPrimitive)
			addImport((LibraryType) attrType, complexTypeDefinition, model
					.getTypeName(), model.getVersion(), model
					.getTypeLibraryName());
		addDocumentation(attr.getAttributeDeclaration(), attrDoc);

	}

	public static String getPrefix(XSDSchema schema, String nameSpace) {		
		Map<String, String> qNamesMap = schema.getQNamePrefixToNamespaceMap();
		if (qNamesMap.containsValue(nameSpace)) {
			for (Entry<String, String> entry : qNamesMap.entrySet()) {
				if (StringUtils.equals(entry.getValue(), nameSpace)) {
					return entry.getKey() + SOATypeLibraryConstants.COLON;
				}
			}
		}

		int prefixInt = 0;
		while (true) {
			prefixInt++;
			if (!qNamesMap
					.containsKey(SOATypeLibraryConstants.DEFAULT_TNS_PREFIX
							+ prefixInt))
				break;
		}
		String prefix = SOATypeLibraryConstants.DEFAULT_TNS_PREFIX + prefixInt;
		qNamesMap.put(prefix, nameSpace);
		return prefix + SOATypeLibraryConstants.COLON;
	}

	public static XSDParticle createXSDElementDeclaration(String strName,
			String strType, int minOccurs, int maxOccurs) {

		XSDSimpleTypeDefinition type = XSDFactory.eINSTANCE
				.createXSDSimpleTypeDefinition();
		type.setName(strType);
		XSDElementDeclaration element = XSDFactory.eINSTANCE
				.createXSDElementDeclaration();

		element.setName(StringUtils.defaultString(strName)); //$NON-NLS-1$
		element.setTypeDefinition(type);

		XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
		particle.setContent(element);
		// -2 means no Occurs attribute
		if (minOccurs != NO_OCCURS) {
			particle.setMinOccurs(minOccurs);
		}
		if (maxOccurs != NO_OCCURS) {
			particle.setMaxOccurs(maxOccurs);
		}

		return particle;
	}

	private static XSDAttributeUse createXSDAttrDeclaration(String strName,
			String strType) {

		XSDSimpleTypeDefinition type = XSDFactory.eINSTANCE
				.createXSDSimpleTypeDefinition();
		type.setName(strType);
		XSDAttributeDeclaration attribute = XSDFactory.eINSTANCE
				.createXSDAttributeDeclaration();

		attribute.setName(StringUtils.defaultString(strName)); //$NON-NLS-1$
		attribute.setTypeDefinition(type);

		XSDAttributeUse attributeUse = XSDFactory.eINSTANCE
				.createXSDAttributeUse();
		attributeUse.setAttributeDeclaration(attribute);
		attributeUse.setContent(attribute);
		return attributeUse;
	}

	public static void setBaseTypeForComplexTypes(
			XSDComplexTypeDefinition complexTypeDefinition, Object baseType,
			String typeName, String typeLibraryName, String version)
			throws CommandFailedException {
		XSDComplexTypeDefinition restriction = XSDFactory.eINSTANCE
				.createXSDComplexTypeDefinition();
		boolean isPrimitive = true;
		try {
			if (baseType instanceof String)
				restriction.setName(getPrefix(
						complexTypeDefinition.getSchema(),
						SOATypeLibraryConstants.W3C_NAMEPSACE)
						+ baseType);
			else {
				isPrimitive = false;
				LibraryType libBaseType = (LibraryType) baseType;
				restriction.setName(getPrefix(
						complexTypeDefinition.getSchema(), TypeLibraryUtil
								.getNameSpace(libBaseType))
						+ libBaseType.getName());

			}
		} catch (Exception e) {
			throw new CommandFailedException(
					"Could not get the name space for the type "
							+ baseType
							+ " from the registry. Please fix this issue and try again.");
		}
		complexTypeDefinition.setBaseTypeDefinition(restriction);

		if (!isPrimitive) {
			addImport((LibraryType) baseType, complexTypeDefinition, typeName,
					version, typeLibraryName);
		}

	}

	private static void addImport(LibraryType importType,
			XSDTypeDefinition typeDefinition, String typeName, String version,
			String typeLibraryName) throws CommandFailedException {
		try {
			XSDSchema importSchema = TypeLibraryUtil
					.parseSchema(TypeLibraryUtil
							.getXSD(SOAGlobalRegistryAdapter.getInstance()
									.getGlobalRegistry()
									.getType(
											TypeLibraryUtil.toQName(importType))));
			AddImportCommand addImportCommand = new AddImportCommand(
					typeDefinition.getSchema(), TypeLibraryUtil
							.getProtocolString(importType),
					importSchema);
			addImportCommand.run();

			TypeLibraryType typeLibInfo = SOAGlobalRegistryAdapter.getInstance()
					.getGlobalRegistry().getTypeLibrary(typeLibraryName);
			LibraryType libraryType = TypeLibraryUtil.getLibraryType(typeName,
					version, typeLibInfo);

			SOAGlobalRegistryAdapter.getInstance().addTypeToRegistry(libraryType);
			IProject project = WorkspaceUtil.getProject(typeLibraryName);
			TypeLibSynhcronizer.syncronizeXSDandDepXml(typeDefinition
					.getSchema(), project, TypeLibraryUtil.toQName(libraryType));
			TypeLibSynhcronizer.synchronizeTypeDepandProjectDep(project, 
					ProgressUtil.getDefaultMonitor(null));
		} catch (Exception e) {
			throw new CommandFailedException(e);
		}
	}

	public static TemplateModel processTemplateModel(InputStream inputStream)
			throws CoreException, IOException {
		TemplateModel templateModel = new TemplateModel();
		XSDSchema schema = TypeLibraryUtil.parseSchema(inputStream);
		if (schema.getTypeDefinitions() != null
				&& schema.getTypeDefinitions().size() > 0
				&& schema.getTypeDefinitions().get(0) != null
				&& schema.getTypeDefinitions().get(0) instanceof XSDTypeDefinition) {
			XSDAnnotation xsdAnnotation = XSDCommonUIUtils
					.getInputXSDAnnotation((XSDTypeDefinition) schema
							.getTypeDefinitions().get(0), false);
			if (xsdAnnotation != null) {
				List documentationList = xsdAnnotation.getUserInformation();
				if (documentationList.size() > 0) {
					Element documentationElement = (Element) documentationList
							.get(0);
					templateModel.setDocumentation(documentationElement
							.getTextContent());
				}
			}
		}
		return templateModel;

	}

}
