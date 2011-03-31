/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.ImportTypeModel;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.typelibrary.exception.ImportTypeException;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.CommonTypeProp;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeCCParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.EnumTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.SimpleTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardAttribPage;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardElementPage;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.EnumTypeWizardDetailsPage.EnumTableModel;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDAttributeGroupContent;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDEnumerationFacet;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.XSDWildcard;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Extract type definitions from XSD file or WSDL file.
 * 
 * @author mzang
 * 
 */

public final class XSDUtils {

	// basic type namespace
	private static final String BASE_TYPE_NS = "http://www.w3.org/[0-9]*/XMLSchema";

	private static Pattern regExPattern = Pattern.compile(BASE_TYPE_NS);

	private static SOALogger logger = SOALogger.getLogger();

	// documentation node
	private static final String DOCUMENT_NODE_NAME = "documentation";

	// app node
	private static final String APP_NODE_NAME = "appinfo";

	// typeLibrarySource node
	private static final String TL_SOURCE_NODE_NAME = "typeLibrarySource";

	// typeLibrarySource node
	private static final String TL_ARRT_NAME = "library";

	// typeLibrarySource node
	private static final String TL_NS_ATTR_NAME = "namespace";

	// base type name when no base type specified.
	private static final String ANY_TYPE = "anyType";

	// attribute names
	private static final String ATTR_MAX_OCCUR = "maxOccurs";

	private static final String ATTR_MIN_OCCUR = "minOccurs";

	private static final String ATTR_TYPE_NAME = "type";

	private static final String ATTR_NAME = "name";

	private static final String UNBOUNDED = "unbounded";

	private static final String TYPE_VERSION = "1.0.0";

	private Map<QName, LibraryType> tlSourceMap = new ConcurrentHashMap<QName, LibraryType>();

	private LibraryType[] allTypes;

	public XSDUtils() {
		List<LibraryType> allTypesList;
		try {
			allTypesList = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
					.getAllTypes();
			allTypes = new LibraryType[allTypesList.size()];
			allTypesList.toArray(allTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static XSDUtils getInstance() {
		return new XSDUtils();
	}

	/**
	 * file must be WSDL file or XSD file.
	 * 
	 * @param wsdl
	 * @return
	 * @throws ImportTypeException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws WSDLException
	 */
	public List<ImportTypeModel> extractTypeDefinitionFromFile(
			String sourceFilePath) throws FileNotFoundException, IOException,
			ImportTypeException, WSDLException {
		List<ImportTypeModel> types = new ArrayList<ImportTypeModel>();
		try {
			File sourceFile = new File(sourceFilePath);
			if (sourceFile.exists() == false) {
				return types;
			}
			String sourceFilePathLow = sourceFilePath.toLowerCase();
			if (sourceFilePathLow.endsWith(SOAProjectConstants.XSD)) {
				types = extractTypeDefFromStreamUsingDOM(new FileInputStream(
						sourceFile));
			} else if (sourceFilePathLow.endsWith(SOAProjectConstants.WSDL)) {
				types = handelWSDLFile(sourceFilePath);
			} else {
				throw new ImportTypeException(StringUtil.formatString(
						SOAMessages.ERR_IMPORT_TYPES_FILE_NOT_SUPPORT,
						sourceFile.getName()));
			}

			handleInternalDependencies(types);
		} catch (Exception ex) {
			if (ex instanceof ImportTypeException) {
				throw (ImportTypeException) ex;
			}
			throw new ImportTypeException(
					"Unable to parse source file. Please check source file content.",
					ex);
		}

		return types;
	}

	private void handleInternalDependencies(List<ImportTypeModel> types)
			throws ImportTypeException {
		Map<QName, ImportTypeModel> typeMap = new ConcurrentHashMap<QName, ImportTypeModel>();
		for (ImportTypeModel type : types) {
			QName name = new QName(type.getNamespace(), type.getName());
			typeMap.put(name, type);
		}

		for (ImportTypeModel type : types) {
			TypeParamModel model = type.getTypeModel();
			if (model instanceof ComplexTypeParamModel == false) {
				continue;
			}
			ComplexTypeParamModel cModel = (ComplexTypeParamModel) model;
			ComplexTypeWizardElementPage.ElementTableModel[] elements = cModel
					.getElementTableModel();
			if (elements != null) {
				for (ComplexTypeWizardElementPage.ElementTableModel element : elements) {
					Object eleType = element.getRawElementType();
					if (eleType instanceof QName) {
						ImportTypeModel dep = typeMap.get(eleType);
						if (dep == null) {
							throw new ImportTypeException(
									"Unable to find dependency: " + eleType);
						}
						type.addDependency((QName) eleType, dep);
					}
				}
			}
			if (model instanceof ComplexTypeCCParamModel) {
				ComplexTypeCCParamModel ccModel = (ComplexTypeCCParamModel) model;
				Object baseType = ccModel.getBaseType();
				if (baseType instanceof QName) {
					ImportTypeModel dep = typeMap.get(baseType);
					if (dep == null) {
						throw new ImportTypeException(
								"Unable to find dependency: " + baseType);
					}
					type.addDependency((QName) baseType, dep);
				}
			}
		}
	}

	private List<ImportTypeModel> handelWSDLFile(String sourceFilePath)
			throws WSDLException, IOException, ImportTypeException {
		SAXParserForTypesInWSDL parser = new SAXParserForTypesInWSDL();
		List<StringBuffer> xsds = parser.getTypeDefsFromWSDL(sourceFilePath);
		List<ImportTypeModel> types = new ArrayList<ImportTypeModel>();
		for (StringBuffer xsd : xsds) {
			String content = xsd.toString();
			if (SOALogger.DEBUG)
				logger.debug(content);
			types.addAll(extractTypeDefFromStreamUsingDOM(IOUtils
					.toInputStream(content)));
		}
		return types;
	}

	/**
	 * create an instance of ComplexTypeCCParamModel and extract Complex Content
	 * specified properties.
	 * 
	 * @param complex
	 * @return
	 * @throws ImportTypeException
	 */
	private ImportTypeModel handleComplexTypeComplexContent(
			XSDComplexTypeDefinition complex) throws ImportTypeException {
		ComplexTypeCCParamModel ccModel = new ComplexTypeCCParamModel();
		ImportTypeModel importModel = new ImportTypeModel(ccModel);
		SOAXSDTemplateSubType ccSubType = SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT;
		ccModel.setTemplateCategory(ccSubType);
		ccModel.setTemplateName(getTemplateFile(ccSubType));

		XSDModelGroup modelGroup = TemplateUtils.getModelGroup(complex);
		EList<XSDParticle> particles = modelGroup.getParticles();
		ComplexTypeWizardElementPage.ElementTableModel[] elements = new ComplexTypeWizardElementPage.ElementTableModel[particles
				.size()];

		for (int i = 0; i < particles.size(); i++) {
			XSDParticle eleParticle = particles.get(i);
			if (eleParticle.getContent() instanceof XSDWildcard) {
				importModel.setUnSupported(true);
				importModel
						.setUnSupportedReason(ImportTypeModel.UNSUPPORTED_NODE_ANY_IN_TYPE);
				return importModel;
			}
			Element ele = eleParticle.getElement();
			String typeName = ele.getAttribute(ATTR_TYPE_NAME);
			elements[i] = new ComplexTypeWizardElementPage.ElementTableModel(
					ele.getAttribute(ATTR_NAME), typeName);

			elements[i].setDatatype(getDataType(complex.getSchema(), typeName));

			elements[i].setMaxOccurs(parseOccur(ele
					.getAttribute(ATTR_MAX_OCCUR)));
			elements[i].setMinOccurs(parseOccur(ele
					.getAttribute(ATTR_MIN_OCCUR)));
		}

		ccModel.setElementTableModel(elements);

		return importModel;
	}

	/**
	 * create an instance of ComplexTypeSCParamModel and extract Simple Content
	 * specified properties.
	 * 
	 * @param complex
	 * @return
	 * @throws ImportTypeException
	 */
	private ImportTypeModel handleComplexTypeSimpleContent(
			XSDComplexTypeDefinition complex) throws ImportTypeException {
		ComplexTypeSCParamModel csModel = new ComplexTypeSCParamModel();
		ImportTypeModel importModel = new ImportTypeModel(csModel);
		SOAXSDTemplateSubType csSubType = SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT;
		csModel.setTemplateCategory(csSubType);
		csModel.setTemplateName(getTemplateFile(csSubType));

		EList<XSDAttributeGroupContent> attributes = complex
				.getAttributeContents();

		ComplexTypeWizardAttribPage.AttribTableModel[] elements = new ComplexTypeWizardAttribPage.AttribTableModel[attributes
				.size()];

		for (int i = 0; i < attributes.size(); i++) {
			XSDAttributeGroupContent attribute = attributes.get(i);
			Element ele = attribute.getElement();
			elements[i] = new ComplexTypeWizardAttribPage.AttribTableModel();
			elements[i].setAttribName(ele.getAttribute(ATTR_NAME));
			String type = ele.getAttribute(ATTR_TYPE_NAME);
			// simple type will not use self defined type.
			String typeName = type.substring(type
					.indexOf(SOATypeLibraryConstants.COLON) + 1, type.length());

			elements[i]
					.setAttribType(getDataType(complex.getSchema(), typeName));

			String description = "";
			if (attribute instanceof XSDAttributeUse) {
				XSDAttributeUse attributeUse = (XSDAttributeUse) attribute;
				description = getDescriptionFromAnnotation(attributeUse
						.getContent().getAnnotation());
			}

			elements[i].setAttribDesc(description);
		}

		if (complex.getAttributeWildcardContent() != null) {
			importModel
					.setUnSupportedReason(ImportTypeModel.UNSUPPORTED_ATTR_ANY_IN_TYPE);
			importModel.setUnSupported(true);
		}

		csModel.setAttribTableModel(elements);
		return importModel;
	}

	/**
	 * create an instance of ComplexTypeParamModel and extract complex type
	 * specified properties.
	 * 
	 * @param complex
	 * @return
	 * @throws ImportTypeException
	 */
	private ImportTypeModel handleComplexType(XSDComplexTypeDefinition complex)
			throws ImportTypeException {
		ComplexTypeParamModel cModel = new ComplexTypeParamModel();
		ImportTypeModel importModel = new ImportTypeModel(cModel);
		SOAXSDTemplateSubType cSubType = SOAXSDTemplateSubType.COMPLEX;
		cModel.setTemplateCategory(cSubType);
		cModel.setTemplateName(getTemplateFile(cSubType));

		XSDModelGroup modelGroup = TemplateUtils.getModelGroup(complex);
		EList<XSDParticle> particles = modelGroup.getParticles();
		ComplexTypeWizardElementPage.ElementTableModel[] elements = new ComplexTypeWizardElementPage.ElementTableModel[particles
				.size()];

		for (int i = 0; i < particles.size(); i++) {
			XSDParticle eleParticle = particles.get(i);
			if (eleParticle.getContent() instanceof XSDWildcard) {
				importModel.setUnSupported(true);
				importModel
						.setUnSupportedReason(ImportTypeModel.UNSUPPORTED_NODE_ANY_IN_TYPE);
				return importModel;
			}
			Element ele = eleParticle.getElement();
			String typeName = ele.getAttribute(ATTR_TYPE_NAME);
			elements[i] = new ComplexTypeWizardElementPage.ElementTableModel(
					ele.getAttribute(ATTR_NAME), typeName);
			// XSDElementDeclaration particleContent = (XSDElementDeclaration)
			// eleParticle
			// .getContent();
			// String namespace = particleContent.getTypeDefinition()
			// .getTargetNamespace();
			// String name = particleContent.getTypeDefinition().getName();
			elements[i].setDatatype(getDataType(complex.getSchema(), typeName));

			elements[i].setMaxOccurs(parseOccur(ele
					.getAttribute(ATTR_MAX_OCCUR)));
			elements[i].setMinOccurs(parseOccur(ele
					.getAttribute(ATTR_MIN_OCCUR)));
		}

		cModel.setElementTableModel(elements);

		return importModel;
	}

	public Object getDataType(XSDSchema schema, String typeName) {
		String[] names = typeName.split(SOATypeLibraryConstants.COLON);
		if (names.length != 2) {
			return typeName;
		}
		String ns = names[0];
		String name = names[1];

		Object tl = getImportModelType(schema, ns, name);
		if (tl == null) {
			// it is a w3 standard type or a type depends on other type in the
			// same wsdl / xsd
			return getTypeName(typeName);
		} else {
			return tl;
		}
	}

	private int parseOccur(String number) {
		if (UNBOUNDED.equalsIgnoreCase(number)) {
			return TemplateUtils.UNBOUND;
		}
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException ex) {
			logger.warning("Unable to parse occur time: " + ex.getMessage());
			// TemplateUtils.NO_OCCURS means no Occurs attribute
			return TemplateUtils.NO_OCCURS;
		}
	}

	private String getTypeName(String typeName) {
		String[] names = typeName.split(SOATypeLibraryConstants.COLON);
		if (names.length == 2) {
			return names[1];
		} else if (names.length == 1) {
			return names[0];
		}
		return null;
	}

	/**
	 * process XSDComplexTypeDefinition instance
	 * 
	 * @param complex
	 * @return
	 * @throws ImportTypeException
	 */
	private ImportTypeModel processComplexTypeDef(
			XSDComplexTypeDefinition complex) throws ImportTypeException {
		TypeParamModel model = null;
		ImportTypeModel importModel = null;
		XSDComplexTypeContent content = complex.getContent();
		XSDTypeDefinition base = complex.getBaseType();
		if (content instanceof XSDSimpleTypeDefinition) {
			importModel = handleComplexTypeSimpleContent(complex);
		} else if (content instanceof XSDParticle) {
			if (ANY_TYPE.equalsIgnoreCase(base.getName())) {
				importModel = handleComplexType(complex);
			} else {
				importModel = handleComplexTypeComplexContent(complex);
			}
		}
		model = importModel.getTypeModel();

		String typeName = complex.getName();
		if (typeName == null) {
			typeName = "";
		}
		// String version = complex.getSchema().getVersion();

		String namespace = complex.getTargetNamespace();
		String description = getDescriptionFromAnnotations(complex
				.getAnnotations());

		model.setTypeName(typeName);
		// model.setVersion(version);

		// handle base type of complex type
		XSDTypeDefinition typeDef = complex.getBaseType();
		String baseType = typeDef.getName();
		model.setBaseType(baseType);
		if (model instanceof ComplexTypeCCParamModel) {
			Object baseTypeLib = getImportModelType(null, typeDef
					.getTargetNamespace(), baseType);
			if (baseTypeLib == null) {
				// it is a w3 standard type
				model.setBaseType(getTypeName(baseType));
			} else {
				model.setBaseType(baseTypeLib);
			}
		}

		model.setDescription(description);
		model.setNamespace(namespace);

		return importModel;
	}

	private String getTemplateFile(SOAXSDTemplateSubType typeEnu)
			throws ImportTypeException {
		List<SOAConfigTemplate> templateFiles = UIActivator
				.getFiles(typeEnu);
		if (templateFiles.size() < 1) {
			throw new ImportTypeException(
					SOAMessages.ERR_IMPORT_TYPES_NO_TEMPLATE_FOUND);
		}
		return templateFiles.get(0).getName();
	}

	/**
	 * get the first un-empty description from annotations. Return empty string
	 * if there is no un-empty description.
	 * 
	 * @param annotations
	 * @return
	 */
	private String getDescriptionFromAnnotations(
			EList<XSDAnnotation> annotations) {
		String description = "";
		if (annotations == null || annotations.size() == 0) {
			return description;
		}
		for (XSDAnnotation at : annotations) {
			EList<Element> list = at.getUserInformation();
			for (Element e : list) {
				String nodeName = e.getLocalName();
				String content = e.getTextContent();
				if (StringUtils.isEmpty(content) == true) {
					continue;
				}
				if (DOCUMENT_NODE_NAME.equalsIgnoreCase(nodeName)) {
					return content;
				}
			}
		}
		return description;
	}

	private String getDescriptionFromAnnotation(XSDAnnotation annotation) {
		String description = "";
		if (annotation == null) {
			return description;
		}
		EList<Element> list = annotation.getUserInformation();
		for (Element e : list) {
			String nodeName = e.getLocalName();
			String content = e.getTextContent();
			if (StringUtils.isEmpty(content) == true) {
				continue;
			}
			if (DOCUMENT_NODE_NAME.equalsIgnoreCase(nodeName)) {
				return content;
			}
		}
		return description;
	}

	/**
	 * get the real namespace of the type
	 * 
	 * @param annotations
	 * @return null if this is a type inside wsdl, not from TL outside
	 */
	private String getSourceNamespaceAnnotations(
			EList<XSDAnnotation> annotations) {
		String orginalNamespace = null;
		if (annotations == null || annotations.size() == 0) {
			return orginalNamespace;
		}
		for (XSDAnnotation at : annotations) {
			EList<Element> list = at.getApplicationInformation();
			for (Element e : list) {
				String nodeName = e.getLocalName();
				// String content = e.getTextContent();
				// if (StringUtils.isEmpty(nodeName) == true) {
				// continue;
				// }
				if (APP_NODE_NAME.equalsIgnoreCase(nodeName) == false) {
					continue;
				}
				NodeList appChildren = e.getChildNodes();
				int len = appChildren.getLength();
				for (int i = 0; i < len; i++) {
					Node appChild = appChildren.item(i);
					if (TL_SOURCE_NODE_NAME.equalsIgnoreCase(appChild
							.getNodeName()) == false) {
						continue;
					}
					NamedNodeMap tlSourceAttrs = appChild.getAttributes();
					Node tlNameNode = tlSourceAttrs.getNamedItem(TL_ARRT_NAME);
					Node typeNSNode = tlSourceAttrs
							.getNamedItem(TL_NS_ATTR_NAME);
					String tlName = tlNameNode.getNodeValue();
					String typeNS = typeNSNode.getNodeValue();
					if(typeNSNode == null || tlNameNode == null){
						return null;
					}
					if (SOALogger.DEBUG)
						logger.debug(tlName + "\t" + typeNS);
					return typeNS;
				}
			}
		}
		return orginalNamespace;
	}

	/**
	 * process XSDSimpleTypeDefinition instance
	 * 
	 * @param complex
	 * @return
	 * @throws ImportTypeException
	 */
	private ImportTypeModel processSimpleTypeDef(XSDSimpleTypeDefinition simple)
			throws ImportTypeException {
		TypeParamModel model = null;

		EList<XSDEnumerationFacet> enums = simple.getEnumerationFacets();
		if (enums == null || enums.size() == 0) {

			model = new SimpleTypeParamModel();
			SOAXSDTemplateSubType simpleType = SOAXSDTemplateSubType.SIMPLE;
			model.setTemplateCategory(simpleType);
			model.setTemplateName(getTemplateFile(simpleType));
		} else {
			EnumTypeParamModel enummodel = new EnumTypeParamModel();
			model = enummodel;
			SOAXSDTemplateSubType enumType = SOAXSDTemplateSubType.ENUM;
			model.setTemplateCategory(enumType);
			String templateFile = getTemplateFile(enumType);
			model.setTemplateName(templateFile);
			EnumTableModel[] enumItems = new EnumTableModel[enums.size()];
			for (int i = 0; i < enums.size(); i++) {
				XSDEnumerationFacet facet = enums.get(i);
				enumItems[i] = new EnumTableModel();
				enumItems[i].setEnumValue(facet.getLexicalValue());
				String description = getDescriptionFromAnnotation(facet
						.getAnnotation());
				enumItems[i].setEnumDesc(description);
			}
			enummodel.setEnumTableModel(enumItems);
		}

		String typeName = simple.getName();
		if (typeName == null) {
			typeName = "";
		}
		// String version = simple.getSchema().getVersion();
		XSDTypeDefinition typeDef = simple.getBaseType();
		String baseType = typeDef.getName();
		String namespace = simple.getTargetNamespace();
		String description = getDescriptionFromAnnotations(simple
				.getAnnotations());

		model.setTypeName(typeName);
		// model.setVersion(version);
		model.setBaseType(baseType);

		model.setDescription(description);
		model.setNamespace(namespace);

		ImportTypeModel importModel = new ImportTypeModel(model);

		return importModel;
	}

	/**
	 * create TypeParamModel instances from stream. Stream should be a XSD
	 * format content.
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws ImportTypeException
	 */
	public List<ImportTypeModel> extractTypeDefFromStreamUsingDOM(
			InputStream inputStream) throws IOException, ImportTypeException {
		List<ImportTypeModel> types = new ArrayList<ImportTypeModel>();
		XSDSchema schemas = TypeLibraryUtil.parseSchema(inputStream);
		EList<XSDTypeDefinition> defList = schemas.getTypeDefinitions();
		if (defList.isEmpty() == true && schemas.getAllDiagnostics().isEmpty() == false) {
			//wsdl has issues, throw an exception
			throw new ImportTypeException(schemas.getAllDiagnostics().get(0).getMessage());
		}
		List<XSDTypeDefinition> typeList = new ArrayList<XSDTypeDefinition>();

		for (XSDTypeDefinition def : defList) {

			String orginalNS = getSourceNamespaceAnnotations(def
					.getAnnotations());
			if (orginalNS == null) {
				typeList.add(def);
			} else {
				String typeName = def.getName();
				LibraryType type = getLibraryType(orginalNS, typeName);
				if (type == null) {
					throw new ImportTypeException("Type Not Found: "
							+ orginalNS + "\t" + typeName);
				}
				QName wsdlQN = new QName(def.getTargetNamespace(), typeName);
				tlSourceMap.put(wsdlQN, type);
			}
		}

		for (XSDTypeDefinition def : typeList) {
			ImportTypeModel importModel = null;
			if (def instanceof XSDSimpleTypeDefinition) {
				importModel = processSimpleTypeDef((XSDSimpleTypeDefinition) def);
			} else if (def instanceof XSDComplexTypeDefinition) {
				importModel = processComplexTypeDef((XSDComplexTypeDefinition) def);
			}
			if (importModel != null) {
				if(importModel.getName() == null){
					importModel.setName("");
				}
				importModel.getTypeModel().setVersion(TYPE_VERSION);
				types.add(importModel);
			}
		}
		return types;
	}

	/**
	 * create TypeParamModel instances from stream. Stream should be a XSD
	 * format content.
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws ImportTypeException
	 */
	public List<CommonTypeProp> extractTypeDefFromStreamUsingStream(
			InputStream inputStream) {
		XSDTypeParser parser = new XSDTypeParser(inputStream);
		List<CommonTypeProp> types = parser.getAllTypes();
		return types;

	}

	/**
	 * fine specified Library Type.
	 * 
	 * @param schema
	 * @param typeName
	 * @param nameSpace
	 * @return
	 */

	private LibraryType getLibraryType(String namespace, String typeName) {
		LibraryType referredType = tlSourceMap.get(new QName(namespace,
				typeName));
		if (referredType != null) {
			return referredType;
		}
		try {
			for (LibraryType type : allTypes) {
				if (SOALogger.DEBUG && typeName.equals(type.getName())) {
					logger.debug("Found:\t" + type.getName() + "\t"
							+ type.getNamespace());
					logger.debug("Expect:\t" + typeName + "\t" + namespace);
				}
				if (typeName.equals(type.getName())
						&& namespace.equals(type.getNamespace())) {
					return type;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getImportModelType(XSDSchema schema, String ns,
			String typeName) {
		try {
			String nameSpace = ns;
			if (schema != null) {
				Map<String, String> qNamesMap = schema
						.getQNamePrefixToNamespaceMap();
				nameSpace = qNamesMap.get(nameSpace);
			}
			LibraryType type = getLibraryType(nameSpace, typeName);
			if (type == null) {
				if (isBasicTypeNamespace(nameSpace) == true) {
					return typeName;
				} else {
					QName name = new QName(nameSpace, typeName);
					return name;
				}
			} else {
				return type;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isBasicTypeNamespace(String namespace) {
		Matcher matcher = regExPattern.matcher(namespace);
		boolean match = matcher.matches();
		return match;
	}
}
