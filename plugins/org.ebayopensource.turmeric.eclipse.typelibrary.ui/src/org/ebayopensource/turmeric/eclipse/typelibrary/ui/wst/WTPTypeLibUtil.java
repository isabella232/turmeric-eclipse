/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredType;
import org.ebayopensource.turmeric.common.config.ReferredTypeLibraryType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.eclipse.buildsystem.TypeDepMarshaller;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.exception.core.SOABadParameterException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.views.registry.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.commands.AddTypesCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDSchemaCommand;
import org.eclipse.wst.xsd.ui.internal.adt.editor.CommonMultiPageEditor;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * WTP Type Library Utility contains the WTP related utility functions. The WSDL
 * is manipulated using the wtp emf model. Here in SOA it is used for both
 * reading and writing the wsdl and xsd. There are some discouraged classes
 * being used but will be removed in future. Most of them are getters and are
 * being used in the templating and import use cases.
 * 
 * @author smathew
 * 
 */
public class WTPTypeLibUtil {

	/**
	 * Scans the schema and returns all the type imports pertaining to SOA
	 * protocol. Remember this will not return the normal XSD imports.ie the
	 * imports without the typelib: protocol. The registry is being queried for
	 * the type name before adding it to the returned map.
	 * 
	 * @param xsdSchema
	 * @return
	 * @throws Exception
	 */
	public static Map<LibraryType, XSDSchemaDirective> getAllTypeLibImports(
			XSDSchema xsdSchema) throws Exception {
		Map<LibraryType, XSDSchemaDirective> typeImportsList = new ConcurrentHashMap<LibraryType, XSDSchemaDirective>();
		for (Object xsdContent : xsdSchema.getContents()) {
			if (xsdContent instanceof XSDSchemaDirective) {
				XSDSchemaDirective xsdSchemaDirective = (XSDSchemaDirective) xsdContent;
				String xsdSchemaLocation = xsdSchemaDirective
						.getSchemaLocation();
				String typeName = TypeLibraryUtil
						.getTypeNameFromProtocolString(xsdSchemaLocation);
				String importedTypeNameSpace = "";
				if (xsdSchemaDirective instanceof XSDInclude) {
					if (xsdSchemaDirective.getResolvedSchema() != null) {
						importedTypeNameSpace = ((XSDInclude)xsdSchemaDirective).getResolvedSchema().getTargetNamespace();
					} else {
						importedTypeNameSpace = xsdSchema.getTargetNamespace();
					}
				} else if (xsdSchemaDirective instanceof XSDImport) {
					importedTypeNameSpace = ((XSDImport) xsdSchemaDirective)
							.getNamespace();
				}
				if (!StringUtils.isEmpty(typeName)) {
					SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
					LibraryType libType = typeRegistry.getType(new QName(importedTypeNameSpace,
									typeName));
					if (libType != null) {
						typeImportsList.put(libType,
								xsdSchemaDirective);
					}
				}
			}
		}
		return typeImportsList;
	}

	/**
	 * Returns the types defined in the given WSDL definition that are part of
	 * the global registry. Basically the API scan the WSDL and finds all the
	 * types and then query the Global registry with the type name and will add
	 * it to the returned map only if the registry has the type. In short it
	 * returns all the type library types in the WSDL definition.
	 * 
	 * @param definition
	 * @return
	 * @throws Exception
	 */
	public static Map<LibraryType, XSDTypeDefinition> getTypeLibraryTypes(
			Definition definition) throws Exception {
		Map<LibraryType, XSDTypeDefinition> allSchemas = new ConcurrentHashMap<LibraryType, XSDTypeDefinition>();
		Types types = ((Types) definition.getTypes());
		// Do we have a schema already?
		for (Object objSchema : types.getSchemas()) {
			XSDSchema xsdSchema = (XSDSchema) objSchema;
			for (Object xsdSchemaContent : xsdSchema.getContents()) {
				if (xsdSchemaContent instanceof XSDTypeDefinition) {
					XSDTypeDefinition xsdTypeDefinition = (XSDTypeDefinition) xsdSchemaContent;
					// Annotations will take precedence over the XSD target
					// namespace
					String nameSpace = getNameSpace(xsdTypeDefinition);
					SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry(); 
					LibraryType libraryType = typeRegistry.getType(
									new QName(nameSpace, xsdTypeDefinition
											.getName()));
					if (libraryType != null) {
						allSchemas.put(libraryType, xsdTypeDefinition);
					}
				}
			}
		}
		return allSchemas;
	}

	public static String getNameSpace(XSDTypeDefinition xsdTypeDefinition)
			throws SOABadParameterException {
		for (XSDAnnotation annotation : xsdTypeDefinition.getAnnotations()) {
			for (Element element : annotation.getApplicationInformation()) {
				NodeList children = element.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					Node node = children.item(i);
					if (node instanceof Element) {
						Element childElement = (Element) node;
						if (StringUtils.equals(childElement.getNodeName(),
								SOATypeLibraryConstants.TAG_TYPE_LIB)) {
							if (StringUtils
									.isEmpty(childElement
											.getAttribute(SOATypeLibraryConstants.ATTR_NMSPC))) {
								throw new SOABadParameterException(
										StringUtil.formatString(
												SOAMessages.ERR_NMSPC,
												xsdTypeDefinition.getName()));
							}
							return childElement
									.getAttribute(SOATypeLibraryConstants.ATTR_NMSPC);
						}
					}

				}
			}
		}
		return xsdTypeDefinition.getTargetNamespace();
	}

	/**
	 * Scan the given map of schemas and returns a new map containing only the
	 * types which has a version in the type dependency XML lower than the one
	 * in the the registry. Basically if Type A has version 1.0.0 in type
	 * dependency XML and the type information XML file inside the type library
	 * that holds Type A has version greater than 1.0.0.
	 *
	 * @param allSchemas the all schemas
	 * @param project the project
	 * @return the updated schemas
	 * @throws Exception the exception
	 */
	public static Map<LibraryType, XSDTypeDefinition> getUpdatedSchemas(
			Map<LibraryType, XSDTypeDefinition> allSchemas, IProject project)
			throws Exception {
		Map<LibraryType, XSDTypeDefinition> newSchemas = new ConcurrentHashMap<LibraryType, XSDTypeDefinition>();
		IFile typeDepFile = TurmericCoreActivator.getDependencyFile(project);
		if (typeDepFile.exists() == false) {
			return newSchemas;
		}
		SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();

		TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
				.unmarshallIt(typeDepFile);
		// compare type library by type library, because of ENF = false, type
		// name could be duplicated.
		for (TypeDependencyType type : typeLibraryDependencyType.getType()) {
			for (ReferredTypeLibraryType referredTypeLibraryType : type
					.getReferredTypeLibrary()) {
				// get the type library be referred type library name. No
				// duplicated types in type library.
				String refTLName = referredTypeLibraryType.getName();
				List<LibraryType> typesInTL = typeRegistry
						.getTypesOfLibrary(refTLName);
				for (LibraryType typeInTL : typesInTL) {
					for (ReferredType refType : referredTypeLibraryType
							.getReferredType()) {
						if (StringUtils.equals(typeInTL.getName(), refType
								.getName()) == false) {
							continue;
						}
						if (VersionUtil.compare(typeInTL.getVersion(), refType
								.getVersion()) <= 0) {
							continue;
						}
						newSchemas.put(typeInTL, allSchemas.get(typeInTL));
					}
				}
			}
		}

		// Set<ReferredType> referredTypes = TypeDepMarshaller
		// .getALLReferredTypes(typeLibraryDependencyType);
		// // Add all other dependent libraries this type is referring to
		// for (LibraryType libraryType : allSchemas.keySet()) {
		// for (ReferredType refType : referredTypes) {
		// if (StringUtils
		// .equals(libraryType.getName(), refType.getName()) == false) {
		// continue;
		// }
		// if (VersionUtil.compare(libraryType.getVersion(), refType
		// .getVersion()) <= 0) {
		// continue;
		// }
		// newSchemas.put(libraryType, allSchemas.get(libraryType));
		// }
		// }

		return newSchemas;
	}

	/**
	 * Making sure that this is a WTP editor and is a candidate for SOA to add
	 * their context menus.
	 *
	 * @param editorPart the editor part
	 * @return true, if successful
	 */
	public static boolean validateEditorForContextMenus(Object editorPart) {
		return (editorPart != null && (editorPart instanceof StructuredTextEditor || editorPart instanceof CommonMultiPageEditor));
	}

	/**
	 * Removes the type from the model. Remember this is an IO operation. This
	 * just modifies the WTP EMF model. Returns true if the type was found in
	 * the definition and was successfully removed, Otherwise returns false.
	 * Editor will be dirty after successful execution of this operation.
	 *
	 * @param definition the definition
	 * @param qname the qname
	 * @return true, if successful
	 */
	public static boolean removeType(Definition definition, QName qname) {
		if (hasSchema(definition, qname.getNamespaceURI())) {
			XSDTypeDefinition xsdTypeDefinition = getType(definition, qname);
			if (xsdTypeDefinition != null) {
				return xsdTypeDefinition.getSchema().getContents().remove(
						xsdTypeDefinition);
			}
		}
		return false;
	}

	private static XSDTypeDefinition getType(Definition definition, QName qname) {
		XSDTypeDefinition typeDefinition = null;
		for (XSDSchema schema : getXSDSchema(definition, qname
				.getNamespaceURI())) {
			typeDefinition = getXSDTypeDefinition(schema, qname);
			break;
		}
		return typeDefinition;
	}

	/**
	 * This code is copy pasted from inlineType function because today is the
	 * release and we want to minimize the impact. We have to refactor it out
	 * after the release. Basically this API delays the imports till the end of
	 * the execution
	 *
	 * @param impTypeSet the imp type set
	 * @param addditionalImpTypeSet the addditional imp type set
	 * @param definition the definition
	 * @param typeFolding the type folding
	 * @throws Exception the exception
	 */
	public static void wrapImport(Set<LibraryType> impTypeSet,
			Set<LibraryType> addditionalImpTypeSet, Definition definition,
			boolean typeFolding) throws Exception {
		Map<XSDSchema, List<LibraryType>> srcTypes = new ConcurrentHashMap<XSDSchema, List<LibraryType>>();

		for (LibraryType libraryType : impTypeSet) {
			removeType(definition, TypeLibraryUtil.toQName(libraryType));
			if (isTypeAlreadyInlined(libraryType.getName(), definition,
					libraryType.getLibraryInfo().getLibraryNamespace(),
					typeFolding)) {
				return;
			} else {
				XSDSchema dstSchema = null;
				XSDSchema srcSchema = TypeLibraryUtil
						.parseSchema(TypeLibraryUtil.getXSD(libraryType));
				String typeNamespace = libraryType.getLibraryInfo()
						.getLibraryNamespace();
				if (typeFolding)
					typeNamespace = definition.getTargetNamespace();
				// If its a new schema section we want to have the element form
				// default values copied from the source.
				if (!hasSchema(definition, typeNamespace)) {
					dstSchema = getXSDSchema(definition, typeNamespace).get(0);
					dstSchema.setElementFormDefault(srcSchema
							.getElementFormDefault());
				} else {
					dstSchema = getXSDSchema(definition, typeNamespace).get(0);
				}

				if (srcTypes.get(dstSchema) == null) {
					srcTypes.put(dstSchema, new ArrayList<LibraryType>());
				}
				srcTypes.get(dstSchema).add(libraryType);

				for (Object objXsdSchemaContent : srcSchema.getContents()) {
					if (objXsdSchemaContent != null
							&& objXsdSchemaContent instanceof XSDImport) {
						XSDImport xsdSchemaImport = (XSDImport) objXsdSchemaContent;
						XSDImport xsdSchemaDirective = XSDFactory.eINSTANCE
								.createXSDImport();
						xsdSchemaDirective.setNamespace(xsdSchemaImport
								.getNamespace());
						if (!importExists(dstSchema, xsdSchemaImport
								.getNamespace()))
							dstSchema.getContents().add(0, xsdSchemaDirective);
					}
				}
			}
		}
		// adding the imports for request and response and the elements inside
		// it.
		if (!typeFolding) {
			XSDSchema mainSchema = getXSDSchema(definition,
					definition.getTargetNamespace()).get(0);
			for (LibraryType libraryType : addditionalImpTypeSet) {
				addImportDirective(mainSchema, libraryType.getNamespace());
			}
			addImportDirective(mainSchema, GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getConfigurationRegistry()
					.getBaseServiceResponseNameSpace());

			addImportDirective(mainSchema, GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getConfigurationRegistry()
					.getBaseServiceRequestNameSpace());

		}
		for (Entry<XSDSchema, List<LibraryType>> entry : srcTypes.entrySet()) {
			XSDSchema destSchema = entry.getKey();
			List<LibraryType> libTypes = entry.getValue();
			for (LibraryType libType : libTypes) {
				WTPCopyUtil.copy(destSchema, libType, typeFolding);
			}
		}

	}

	private static void addImportDirective(XSDSchema schema,
			String libElementNamespace) {
		if (!StringUtil.broadEquals(schema.getTargetNamespace(),
				libElementNamespace)
				&& !importExists(schema, libElementNamespace)) {
			XSDImport xsdSchemaDirective = XSDFactory.eINSTANCE
					.createXSDImport();
			xsdSchemaDirective.setNamespace(libElementNamespace);
			schema.getContents().add(0, xsdSchemaDirective);
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

	// /**
	// * Wrapper over the method.
	// *
	// * @see {@link WTPTypeLibUtil#inlineType(LibraryType, Definition,
	// boolean)}
	// * @param libraryType
	// * @param definition
	// * @throws MalformedURLException
	// * @throws IOException
	// * @throws Exception
	// */
	// public static void inlineType(LibraryType libraryType,
	// Definition definition)
	// throws MalformedURLException, IOException, Exception {
	// inlineType(libraryType, definition, true, typeFolding);
	// }

	/**
	 * In-lines the given WTP WSDL definition objects with the given library
	 * type object(converted to an XSD model object after parsing). If the check
	 * for duplicate boolean is true the operation will become a no operation if
	 * the type is already in-lined. Uses internally the
	 * AddXSDTypeDefinitionCommand from WTP.
	 *
	 * @param libraryType the library type
	 * @param definition the definition
	 * @param checkForDuplicates -
	 * If true, this function will execute a duplicate check and will
	 * return silently without doing anything if the type already
	 * exists in the definition passed.
	 * @param typeFolding the type folding
	 * @return the i status
	 * @throws MalformedURLException the malformed url exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception the exception
	 */
	public static IStatus inlineType(LibraryType libraryType,
			Definition definition, boolean checkForDuplicates,
			boolean typeFolding) throws MalformedURLException, IOException,
			Exception {
		if (checkForDuplicates
				&& isTypeAlreadyInlined(libraryType.getName(), definition,
						libraryType.getLibraryInfo().getLibraryNamespace(),
						typeFolding)) {
			return EclipseMessageUtils.createStatus(
					StringUtil.formatString(SOAMessages.ERR_SELECTED_TYPE_ALREADY_IMPORT, 
							libraryType.getName()), IStatus.WARNING);
		} else {
			XSDSchema dstSchema = null;
			XSDSchema srcSchema = TypeLibraryUtil.parseSchema(TypeLibraryUtil
					.getXSD(libraryType));
			String typeNamespace = libraryType.getLibraryInfo()
					.getLibraryNamespace();
			if (typeFolding)
				typeNamespace = definition.getTargetNamespace();
			// If its a new schema section we want to have the element form
			// default values copied from the source.
			if (!hasSchema(definition, typeNamespace)) {
				dstSchema = getXSDSchema(definition, typeNamespace).get(0);
				dstSchema.setElementFormDefault(srcSchema
						.getElementFormDefault());
			} else {
				dstSchema = getXSDSchema(definition, typeNamespace).get(0);
			}

			WTPCopyUtil.copy(dstSchema, libraryType, typeFolding);
			
			// find all importted namespace in current schema
			Set<String> importedNamespaces = new HashSet<String>();
			for(Object dstXSDContentObj : dstSchema.getContents()){
				if (dstXSDContentObj instanceof XSDImport) {
					XSDImport importNode = (XSDImport)dstXSDContentObj;
					importedNamespaces.add(importNode.getNamespace());
				}
			}

			for (Object objXsdSchemaContent : srcSchema.getContents()) {
				if (objXsdSchemaContent != null
						&& objXsdSchemaContent instanceof XSDImport) {
					XSDImport xsdSchemaImport = (XSDImport) objXsdSchemaContent;
					if(importedNamespaces.contains(xsdSchemaImport.getNamespace()) == true){
						// this import is already included, skip it.
						continue;
					}
					XSDImport xsdSchemaDirective = XSDFactory.eINSTANCE
							.createXSDImport();
					xsdSchemaDirective.setNamespace(xsdSchemaImport
							.getNamespace());
					
					dstSchema.getContents().add(0, xsdSchemaDirective);
				}
			}

		}
		return Status.OK_STATUS;
	}

	private static XSDTypeDefinition getXSDTypeDefinition(XSDSchema schema,
			QName typeQName) {

		for (Object objTypeDefinition : schema.getTypeDefinitions()) {
			if (objTypeDefinition instanceof XSDTypeDefinition) {
				XSDTypeDefinition typeDefinition = (XSDTypeDefinition) objTypeDefinition;
				if (StringUtils.equals(typeQName.getLocalPart(), typeDefinition
						.getName())
						&& StringUtils.equals(typeQName.getNamespaceURI(),
								typeDefinition.getTargetNamespace())) {
					return typeDefinition;
				}
			}

		}
		return null;
	}

	public static List<XSDSchema> getXSDSchema(Definition definition,
			String targetNamespace) {
		List<XSDSchema> schemaList = getTypes(definition).getSchemas(
				targetNamespace);
		if (schemaList == null || schemaList.isEmpty()) {
			schemaList = new ArrayList<XSDSchema>();
			AddXSDSchemaCommand command = new AddXSDSchemaCommand(definition,
					targetNamespace);
			command.run();
			schemaList.add(((XSDSchemaExtensibilityElement) command
					.getWSDLElement()).getSchema());
		}
		return schemaList;
	}

	private static boolean hasSchema(Definition definition,
			String targetNamespace) {
		List<XSDSchema> schemaList = getTypes(definition).getSchemas(
				targetNamespace);
		return schemaList != null && !schemaList.isEmpty();
	}

	/**
	 * Return the types.
	 * 
	 * @param definition the definition
	 * @return the types
	 */
	public static Types getTypes(Definition definition) {
		Types types = (Types) definition.getTypes();
		if (types != null)
			return types;
		else {
			AddTypesCommand command = new AddTypesCommand(definition);
			command.run();
			return (Types) command.getWSDLElement();
		}
	}

	/**
	 * @param typeName
	 * @param definition
	 * @param nameSpace
	 * @return
	 */
	private static boolean isTypeAlreadyInlined(String typeName,
			Definition definition, String nameSpace, boolean typeFolding) {
		if (definition != null) {
			if (definition.getTypes() != null) {
				Types types = (Types) definition.getTypes();
				List objSchemas = null;
				// if type is folded then there is only one name space ideally
				// and essentially does not match the types namespace. We might
				// have to scan the annotation to find out the name space of the
				// the type.
				if (typeFolding)
					objSchemas = types.getSchemas();
				else
					objSchemas = types.getSchemas(nameSpace);

				for (Object schemaObj : objSchemas) {
					if (schemaObj instanceof XSDSchema) {
						XSDSchema xsdSchema = (XSDSchema) schemaObj;
						for (Object objXsdTypeDefinition : xsdSchema
								.getTypeDefinitions()) {
							if (objXsdTypeDefinition != null
									&& (objXsdTypeDefinition instanceof XSDTypeDefinition)
									&& StringUtils
											.equals(
													((XSDTypeDefinition) objXsdTypeDefinition)
															.getName(),
													typeName)) {
								// name check is enough for single name space
								// WSDL.
								if (typeFolding) {
									return true;
								}
								// Both name space and names have to be equal
								// for multiple name space WSDL.
								else if (StringUtils
										.equals(
												((XSDTypeDefinition) objXsdTypeDefinition)
														.getTargetNamespace(),
												nameSpace)) {
									return true;
								}
							}
						}
					}
				}

			}
		}
		return false;
	}
	
	/**
	 * Removes the inline type from wsdl definition.
	 *
	 * @param definition the definition
	 * @param selectedType the selected type
	 * @param importedTypesMap the imported types map
	 */
	public static void removeInlineTypeFromWSDLDefinition(Definition definition,
			LibraryType selectedType,
			Map<LibraryType, XSDTypeDefinition> importedTypesMap) {

		Types types = ((Types) definition.getTypes());

		XSDTypeDefinition deletedXsdTypeDefinition = importedTypesMap
				.get(selectedType);
		if (deletedXsdTypeDefinition == null) {
			return;
		}

		XSDSchema deletedSchemasParent = null;
		// Do we have a schema already?
		Iterator<?> iterator = types.getSchemas().iterator();
		while (iterator.hasNext()) {
			XSDSchema xsdSchema = (XSDSchema) iterator.next();
			for (Object xsdSchemaContent : xsdSchema.getContents()) {
				if (xsdSchemaContent == deletedXsdTypeDefinition) {
					deletedSchemasParent = xsdSchema;
					break;
				}
			}
		}

		if (deletedSchemasParent == null) {
			return;
		}
		// remove xsd type from definition
		deletedSchemasParent.getContents().remove(deletedXsdTypeDefinition);

		int typeCount = 0;
		List<XSDImport> importNodes = new ArrayList<XSDImport>();
		Set<String> importedNamespaces = new HashSet<String>();
		// get all imported namespaces in current schema content and get xsd
		// type definition count
		for (Object xsdSchemaContent : deletedSchemasParent.getContents()) {
			if (xsdSchemaContent instanceof XSDTypeDefinition) {
				// found one xsd type in current schema
				typeCount++;
				XSDTypeDefinition type = (XSDTypeDefinition) xsdSchemaContent;
				// find all import node in the xsd type via library type
				LibraryType libraryType = null;
				try {
					QName qname = new QName(type.getTargetNamespace(), type
							.getName());
					libraryType = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
							.getType(qname);
				} catch (Exception e) {
					continue;
				}
				if (libraryType == null) {
					continue;
				}
				try {
					XSDSchema typeSchema = TypeLibraryUtil
							.parseSchema(TypeLibraryUtil.getXSD(libraryType));
					for (Object objXsdSchemaContent : typeSchema.getContents()) {
						if (objXsdSchemaContent instanceof XSDImport) {
							XSDImport importNode = (XSDImport) objXsdSchemaContent;
							// add import namespace into a set
							importedNamespaces.add(importNode.getNamespace());
						}
					}
				} catch (IOException e) {
					continue;
				} catch (Exception e) {
					continue;
				}

			} else if (xsdSchemaContent instanceof XSDImport) {
				// found all import node in current schema and add it to a set
				importNodes.add((XSDImport) xsdSchemaContent);
			}
		}

		// remove unused import nodes
		List<XSDImport> removeImportNodes = new ArrayList<XSDImport>();
		for (XSDImport importNode : importNodes) {
			String namespace = importNode.getNamespace();
			// if a import in current schema is not used by any of xsd types in
			// current schema
			if (importedNamespaces.contains(namespace) == false) {
				// plan to remove it
				removeImportNodes.add(importNode);
			}
		}
		// do remove
		deletedSchemasParent.getContents().removeAll(removeImportNodes);
	}
}
