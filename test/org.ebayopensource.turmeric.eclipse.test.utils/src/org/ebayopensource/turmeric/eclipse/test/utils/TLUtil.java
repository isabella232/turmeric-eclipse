/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.buildsystem.TypeCreator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.buildsystem.TypeLibraryCreator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.ImportTypeFromTypeLibrary;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.RemoveType;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.UpdateTypeVersion;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.WTPTypeLibUtil;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.EnumTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.SimpleTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardAttribPage.AttribTableModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardElementPage.ElementTableModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.EnumTypeWizardDetailsPage.EnumTableModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericOrganizationProvider;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.XSDTypeDefinition;

import org.ebayopensource.turmeric.common.config.LibraryType;

/*
 * Utilities to be used by TypeLibrary Functional Tests
 */
public class TLUtil {

	public static final String functionDomain = "Blogs";
	public static final String MAJOR_VERSION_PREFIX = "V";
	public static final String SERVICE_MAJOR_VERSION = "1";

	/*
	 * TypeLibrary Creation
	 */
	public static boolean createTypeLibrary(String typeLibraryName,
			String version, String category, String location) {
		try {
			TypeLibraryParamModel model = new TypeLibraryParamModel();
			model.setTypeLibraryName(typeLibraryName);
			model.setFunctionDomain(functionDomain);
			model.setVersion(version);
			model.setCategory(category);
			model.setWorkspaceRoot(location);
			String targetNamespace = getTargetNamespace(functionDomain);
			model.setNamespace(targetNamespace);
			SimpleTestUtil.setAutoBuilding(false);
			System.out.println(location);
			TypeLibraryCreator.createTypeLibrary(model,
					ProgressUtil.getDefaultMonitor(null));
			SimpleTestUtil.setAutoBuilding(true);
			WorkspaceUtil.getProject(model.getTypeLibraryName()).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createTypeLibraryInCustomNS(String typeLibraryName,
			String version, String category, String location, String customNS) {
		try {
			TypeLibraryParamModel model = new TypeLibraryParamModel();
			model.setTypeLibraryName(typeLibraryName);
			model.setVersion(version);
			model.setCategory(category);
			model.setWorkspaceRoot(location);
			if (customNS != null)
				model.setNamespace(customNS);
			else
				model.setNamespace(TurmericConstants.DEFAULT_SERVICE_NAMESPACE);
			SimpleTestUtil.setAutoBuilding(true);
			System.out.println(location);
			TypeLibraryCreator.createTypeLibrary(model,
					ProgressUtil.getDefaultMonitor(null));
			WorkspaceUtil.getProject(model.getTypeLibraryName()).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Types Creation
	 */
	public static boolean createType(String typeName, String typeLibraryName,
			String parentType, SOAXSDTemplateSubType category,
			String templateName, String base) {
		try {

			// TypeParamModel typeParamModel = null;
			if (category == SOAXSDTemplateSubType.SIMPLE) {
				SimpleTypeParamModel typeParamModel = new SimpleTypeParamModel();
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				// SOAServiceConstants.DEFAULT_SERVICE_NAMESPACE
				typeParamModel.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));

				IProject typeLibProject = WorkspaceUtil
						.getProject(typeLibraryName);
				IFile xsdFile = typeLibProject.getFile("meta-src"
						+ File.separator + "types" + File.separator
						+ typeLibraryName + File.separator + typeName + ".xsd");
				System.out.println("Type Name --- "
						+ xsdFile.getFullPath().toString());

				TypeCreator.postProcessTypeCreation(typeName,
						typeParamModel.getVersion(), typeLibraryName, xsdFile);

				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
			} else if (category == SOAXSDTemplateSubType.COMPLEX) {
				ComplexTypeParamModel typeParamModel = new ComplexTypeParamModel();
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				// typeParamModel.setNamespace(SOAServiceConstants.DEFAULT_SERVICE_NAMESPACE);
				typeParamModel.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");

				List<ElementTableModel> elements = new ArrayList<ElementTableModel>();
				elements.add(new ElementTableModel("element","string"));
				typeParamModel.setElementTableModel(elements.toArray(new ElementTableModel[0]));

				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));

				IProject typeLibProject = WorkspaceUtil
						.getProject(typeLibraryName);
				IFile xsdFile = typeLibProject.getFile("meta-src"
						+ File.separator + "types" + File.separator
						+ typeLibraryName + File.separator + typeName + ".xsd");
				System.out.println("Type Name --- "
						+ xsdFile.getFullPath().toString());

				TypeCreator.postProcessTypeCreation(typeName,
						typeParamModel.getVersion(), typeLibraryName, xsdFile);

				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
			} else if (category == SOAXSDTemplateSubType.ENUM) {

				EnumTypeParamModel typeParamModel = new EnumTypeParamModel();

				EnumTableModel[] z = new EnumTableModel[2];

				if (base.equalsIgnoreCase("string")) {
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Value ON");
					z[0].setEnumValue("ON");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Value OFF");
					z[1].setEnumValue("OFF");
				} else if (base.equalsIgnoreCase("int")) {
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Value 0");
					z[0].setEnumValue("0");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Value 1");
					z[1].setEnumValue("1");
				} else if (base.equalsIgnoreCase("boolean")) {
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Value True");
					z[0].setEnumValue("true");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Value False");
					z[1].setEnumValue("false");
				} else if (base.equalsIgnoreCase("decimal")) {
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Value 1.5");
					z[0].setEnumValue("1.5");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Value 2.5");
					z[1].setEnumValue("2.5");
				}
				typeParamModel.setEnumTableModel(z);
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				// typeParamModel.setNamespace(SOAServiceConstants.DEFAULT_SERVICE_NAMESPACE);
				typeParamModel.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));

				IProject typeLibProject = WorkspaceUtil
						.getProject(typeLibraryName);
				IFile xsdFile = typeLibProject.getFile("meta-src"
						+ File.separator + "types" + File.separator
						+ typeLibraryName + File.separator + typeName + ".xsd");
				System.out.println("Type Name --- "
						+ xsdFile.getFullPath().toString());

				TypeCreator.postProcessTypeCreation(typeName,
						typeParamModel.getVersion(), typeLibraryName, xsdFile);

				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));

			} else if (category == SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT) {
				ComplexTypeSCParamModel typeParamModel = new ComplexTypeSCParamModel();
				AttribTableModel[] z = new AttribTableModel[2];
				z[0] = new AttribTableModel();
				z[0].setAttribDesc("Attr A description");
				z[0].setAttribName("AttrA");
				z[0].setAttribType("decimal");
				z[1] = new AttribTableModel();
				z[1].setAttribDesc("Attr B description");
				z[1].setAttribName("AttrB");
				z[1].setAttribType("string");
				typeParamModel.setAttribTableModel(z);
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				typeParamModel.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));

				IProject typeLibProject = WorkspaceUtil
						.getProject(typeLibraryName);
				IFile xsdFile = typeLibProject.getFile("meta-src"
						+ File.separator + "types" + File.separator
						+ typeLibraryName + File.separator + typeName + ".xsd");
				System.out.println("Type Name --- "
						+ xsdFile.getFullPath().toString());

				TypeCreator.postProcessTypeCreation(typeName,
						typeParamModel.getVersion(), typeLibraryName, xsdFile);

				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
			} else if (category == SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT) {
				// ComplexTypeCCParamModel typeParamModel = new
				// ComplexTypeCCParamModel();
				// ComplexTypeWizardElementPage x = new
				// ComplexTypeWizardElementPage();
				// ElementTableModel[]
			}

			// typeParamModel.setTypeName(typeName);
			// typeParamModel.setVersion("1.0.0");
			// typeParamModel.setTypeLibraryName(typeLibraryName);
			// typeParamModel.setNamespace(SOAServiceConstants.DEFAULT_SERVICE_NAMESPACE);
			// typeParamModel.setTemplateCategory(category);
			// typeParamModel.setDescription("Automated Test");
			// typeParamModel.setTemplateName(templateName);
			// typeParamModel.setBaseType(base);
			// SimpleTestUtil.setAutoBuilding(false);
			// TypeCreator.createType(typeParamModel,
			// ProgressUtil.getDefaultMonitor(null));
			// WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(IncrementalProjectBuilder.FULL_BUILD,
			// ProgressUtil.getDefaultMonitor(null));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Types Creation in custom namespace
	 */
	public static boolean createTypeInCustomNS(String typeName,
			String typeLibraryName, String parentType,
			SOAXSDTemplateSubType category, String templateName, String base,
			String customNS) {
		try {

			// TypeParamModel typeParamModel = null;
			if (category == SOAXSDTemplateSubType.SIMPLE) {
				SimpleTypeParamModel typeParamModel = new SimpleTypeParamModel();
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				if (customNS != null)
					typeParamModel.setNamespace(customNS);
				else
					typeParamModel
							.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
			} else if (category == SOAXSDTemplateSubType.COMPLEX) {
				ComplexTypeParamModel typeParamModel = new ComplexTypeParamModel();
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				typeParamModel.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
			} else if (category == SOAXSDTemplateSubType.ENUM) {

				EnumTypeParamModel typeParamModel = new EnumTypeParamModel();

				EnumTableModel[] z = null;

				if (base.equalsIgnoreCase("string")) {
					z = new EnumTableModel[3];
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Value COLOR1");
					z[0].setEnumValue("RED");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Value COLOR2");
					z[1].setEnumValue("BLUE");
					z[2] = new EnumTableModel();
					z[2].setEnumDesc("Value COLOR3");
					z[2].setEnumValue("GREEN");
				} else if (base.equalsIgnoreCase("token")) {
					z = new EnumTableModel[4];
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Amex");
					z[0].setEnumValue("Active");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Visa");
					z[1].setEnumValue("Pending");
					z[2] = new EnumTableModel();
					z[2].setEnumDesc("MasterCard");
					z[2].setEnumValue("Inactive");
					z[3] = new EnumTableModel();
					z[3].setEnumDesc("Reserved for internal or future use");
					z[3].setEnumValue("CustomCode");
				} else if (base.equalsIgnoreCase("decimal")) {
					z = new EnumTableModel[2];
					z[0] = new EnumTableModel();
					z[0].setEnumDesc("Value 1.5");
					z[0].setEnumValue("1.5");
					z[1] = new EnumTableModel();
					z[1].setEnumDesc("Value 2.5");
					z[1].setEnumValue("2.5");
				}
				typeParamModel.setEnumTableModel(z);
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				if (customNS != null)
					typeParamModel.setNamespace(customNS);
				else
					typeParamModel
							.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));

			} else if (category == SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT) {
				ComplexTypeSCParamModel typeParamModel = new ComplexTypeSCParamModel();
				AttribTableModel[] z = new AttribTableModel[2];
				z[0] = new AttribTableModel();
				z[0].setAttribDesc("Attr A description");
				z[0].setAttribName("A");
				z[0].setAttribType("decimal");
				z[1] = new AttribTableModel();
				z[1].setAttribDesc("Attr B description");
				z[1].setAttribName("B");
				z[1].setAttribType("string");
				typeParamModel.setAttribTableModel(z);
				typeParamModel.setTypeName(typeName);
				typeParamModel.setVersion("1.0.0");
				typeParamModel.setTypeLibraryName(typeLibraryName);
				if (customNS != null)
					typeParamModel.setNamespace(customNS);
				else
					typeParamModel
							.setNamespace(getTargetNamespace(functionDomain));
				typeParamModel.setTemplateCategory(category);
				typeParamModel.setDescription("Automated Test");
				typeParamModel.setTemplateName(templateName);
				typeParamModel.setBaseType(base);
				SimpleTestUtil.setAutoBuilding(true);
				TypeCreator.createType(typeParamModel,
						ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
			} else if (category == SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT) {
				// ComplexTypeCCParamModel typeParamModel = new
				// ComplexTypeCCParamModel();
				// ComplexTypeWizardElementPage x = new
				// ComplexTypeWizardElementPage();
				// ElementTableModel[]
			}

			// typeParamModel.setTypeName(typeName);
			// typeParamModel.setVersion("1.0.0");
			// typeParamModel.setTypeLibraryName(typeLibraryName);
			// typeParamModel.setNamespace(SOAServiceConstants.DEFAULT_SERVICE_NAMESPACE);
			// typeParamModel.setTemplateCategory(category);
			// typeParamModel.setDescription("Automated Test");
			// typeParamModel.setTemplateName(templateName);
			// typeParamModel.setBaseType(base);
			// SimpleTestUtil.setAutoBuilding(false);
			// TypeCreator.createType(typeParamModel,
			// ProgressUtil.getDefaultMonitor(null));
			// WorkspaceUtil.getProject(typeParamModel.getTypeName()).build(IncrementalProjectBuilder.FULL_BUILD,
			// ProgressUtil.getDefaultMonitor(null));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Import Action on xsd and WSDL
	 */
	public static boolean importAction(String importType, String projectName,
			String typeName, String importTypeName, String importTypeNS) {
		IFile xsdFile, wsdlFile = null;

		IEditorPart editorPart;

		try {
			if (importType.contentEquals("TYPELIB")) {
				IProject typeLibProject = ProjectUtil.getProject(projectName);
				xsdFile = typeLibProject.getFile(File.separator + "meta-src"
						+ File.separator + "types" + File.separator
						+ projectName + File.separator + typeName);
				WorkspaceUtil.refresh(xsdFile);
				System.out.println("Type Name --- "
						+ xsdFile.getFullPath().toString());

				IDE.openEditor(UIUtil.getActiveWorkBenchWindow()
						.getActivePage(), xsdFile);

				editorPart = UIUtil.getActiveEditor();
				LibraryType type = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
						.getType(new QName(importTypeNS, importTypeName));

				LibraryType[] selectedTypes = new LibraryType[1];
				selectedTypes[0] = type;
				System.out.println("SelectedType[0] -- "
						+ selectedTypes[0].getName());
				Object adaptedObject = TypeLibraryUtil
						.getAdapterClassFromWTPEditors(editorPart);

				ImportTypeFromTypeLibrary.performImportTasksForXSDEditor(
						(XSDSchema) adaptedObject, selectedTypes, xsdFile);

				editorPart.doSave(ProgressUtil.getDefaultMonitor(null));
				UIUtil.getActivePage().closeAllEditors(true);
				WorkspaceUtil.refresh(xsdFile);

			} else {
				// IProject typeLibProject =
				// ProjectUtil.getProject(projectName);
				wsdlFile = SOAServiceUtil.getWsdlFile(projectName);
				if (wsdlFile == null) {
					System.out
							.println("WSDL file does not exist.Hence returning false");
					return false;
				}
				WorkspaceUtil.refresh(wsdlFile);

				System.out.println("Wsdl File --- "
						+ wsdlFile.getFullPath().toString() + "Wsdl Exists -- "
						+ wsdlFile.exists());
				IDE.openEditor(UIUtil.getActiveWorkBenchWindow()
						.getActivePage(), wsdlFile);

				editorPart = UIUtil.getActiveEditor();
				// TypeLibraryType typeLibInfo = SOAGlobalRegistryAdapter
				// .getGlobalRegistry().getTypeLibrary("HardwareTypeLibrary");
				// LibraryType type =
				// TypeLibraryUtil.getLibraryType(importTypeName, "1.0.0",
				// typeLibInfo);

				LibraryType type = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
						.getType(new QName(importTypeNS, importTypeName));

				LibraryType[] selectedTypes = new LibraryType[1];
				selectedTypes[0] = type;
				System.out.println("SelectedType[0] -- "
						+ selectedTypes[0].getName());
				Object adaptedObject = TypeLibraryUtil
						.getAdapterClassFromWTPEditors(editorPart);

				// Import from wsdl
				ImportTypeFromTypeLibrary.performInlineOperationsForWSDLEditor(
						(Definition) adaptedObject, selectedTypes, wsdlFile);
				editorPart.doSave(ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.refresh(wsdlFile);
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Remove Action on XSD and Wsdl
	 */
	public static boolean removeAction(String removeType, String projectName,
			String typeName, String typeLibraryName, String removeTypeName, 
			String removeTypeNS) {
		IFile xsdFile = null;
		IEditorPart editorPart;

		try {
			UIUtil.getActivePage().saveAllEditors(true);
			UIUtil.getActivePage().closeAllEditors(true);
			IProject typeLibProject = ProjectUtil.getProject(projectName);
			xsdFile = typeLibProject.getFile(File.separator + "meta-src"
					+ File.separator + "types" + File.separator + projectName
					+ File.separator + typeName);
			System.out.println("Type Name --- "
					+ xsdFile.getFullPath().toString());

			IDE.openEditor(UIUtil.getActiveWorkBenchWindow().getActivePage(),
					xsdFile);

			editorPart = UIUtil.getActiveEditor();

			LibraryType type = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
					.getType(new QName(removeTypeNS, removeTypeName));

			ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();
			selectedTypes.add(type);

			Object adaptedObject = TypeLibraryUtil
					.getAdapterClassFromWTPEditors(editorPart);
			XSDSchema parentXSDSchema = (XSDSchema) adaptedObject;

			Map<LibraryType, XSDSchemaDirective> importedTypesMap = TypeLibraryActivator
					.getAllTypeLibImports(parentXSDSchema);

			if (removeType.contentEquals("TYPELIB")) {
				new RemoveType().modifyXSD(selectedTypes, parentXSDSchema,
						importedTypesMap, typeLibProject, xsdFile);
				editorPart.doSave(ProgressUtil.getDefaultMonitor(null));

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void deleteAction(String projectName, String typeName, String typeNS)
			throws Exception {

		IFile xsdFile = null;

		IProject typeLibProject = ProjectUtil.getProject(projectName);
		xsdFile = typeLibProject.getFile(File.separator + "meta-src"
				+ File.separator + "types" + File.separator + projectName
				+ File.separator + typeName);
		System.out.println("Type Name --- " + xsdFile.getFullPath().toString());

		if (typeName.contains(".")) {
			int index = typeName.indexOf(".");
			typeName = typeName.substring(0, index);

		}
		LibraryType libraryType = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
				.getType(new QName(typeNS, typeName));
		ProgressUtil.progressOneStep(ProgressUtil.getDefaultMonitor(null));
		SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().removeTypeFromRegistry(
				libraryType);
		ProgressUtil.progressOneStep(ProgressUtil.getDefaultMonitor(null));
		ProgressUtil.progressOneStep(ProgressUtil.getDefaultMonitor(null));
		WorkspaceUtil.refresh(ProgressUtil.getDefaultMonitor(null),
				typeLibProject);
		SOAGlobalRegistryAdapter.getInstance().populateRegistry(typeLibProject.getName());
		ProgressUtil.progressOneStep(ProgressUtil.getDefaultMonitor(null));

	}

	public static boolean updateVersionInWsdl(String name, String typeName,
			String libraryName) {
		try {
			UpdateTypeVersion update = new UpdateTypeVersion();
			// Update the wsdl
			IProject project = ProjectUtil.getProject(name);
			IFile wsdlFile = SOAServiceUtil.getWsdlFile(name);
			if (wsdlFile == null) {
				System.out
						.println("WSDL file does not exist.Hence returning false");
				return false;
			}
			System.out.println("Wsdl File --- "
					+ wsdlFile.getFullPath().toString() + "Wsdl Exists -- "
					+ wsdlFile.exists());
			IDE.openEditor(UIUtil.getActiveWorkBenchWindow().getActivePage(),
					wsdlFile);

			IEditorPart editorPart = UIUtil.getActiveEditor();

			SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
			SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance()
					.getGlobalRegistry();
			LibraryType type = typeRegistry.getType(typeName, libraryName);
			ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();

			selectedTypes.add(type);
			System.out.println("SelectedType[0] -- "
					+ selectedTypes.get(0).getName());
			Object adaptedObject = TypeLibraryUtil
					.getAdapterClassFromWTPEditors(editorPart);

			Definition definition = (Definition) adaptedObject;
			Map<LibraryType, XSDTypeDefinition> importedTypesMap = TypeLibraryActivator
					.getTypeLibraryTypes(definition);

			update.modifyWSDL(selectedTypes, definition, importedTypesMap,
					project);

			editorPart.doSave(ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(name).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(name + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			return true;

		} catch (Exception e) {
			System.out.println("Exception --- " + e.getCause());
			return false;
		}
	}

	public static String getTargetNamespace(String domainClassifier) {
		return TurmericOrganizationProvider.INSTANCE.generateTypeLibraryTargetNamespace(domainClassifier, 
				StringUtils.lowerCase(domainClassifier), "1.0.0");
	}

}
