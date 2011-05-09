/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.buildsystem;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.buildsystem.SynchronizeWsdlAndDepXML;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryBuilderUtils;
import org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model.GenTypeAddType;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.XSDTypeParser;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.CommonTypeProp;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.XSDTemplateProcessor;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardElementPage.ElementTableModel;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.ide.IDE;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 
 * The creation point for type. Creates the types, triggers code generation.
 * processes the template etc.
 * 
 * @author smathew
 */
public class TypeCreator {
	
	/** The Constant logger. */
	protected static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Creates the type. Performs file creation, process the template, calls
	 * code generation, refresh the registry and opens the created file in an
	 * editor.
	 *
	 * @param typeParamModel the type param model
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createType(TypeParamModel typeParamModel,
			IProgressMonitor monitor) throws Exception {

		String typeName = typeParamModel.getTypeName();
		String typeLibraryName = typeParamModel.getTypeLibraryName();

		IProject project = WorkspaceUtil.getProject(typeLibraryName);
		String xsdFileName = TypeLibraryUtil.getXsdFileLocation(typeName,
				project);
		IFile xsdFile = createXsd(project, xsdFileName, monitor);
		boolean needSync = false;
		if (typeParamModel.getBaseType() instanceof LibraryType) {
			needSync = true;
		} else if (typeParamModel instanceof ComplexTypeParamModel){
			ComplexTypeParamModel complexType = (ComplexTypeParamModel) typeParamModel;
			if (complexType.getElementTableModel() != null) {
				for (ElementTableModel model: complexType.getElementTableModel()) {
					if (model.getDatatype() instanceof LibraryType) {
						needSync = true;
						break;
					}
				}
			}
		}
		
		if (needSync == true) {
			//need to fix the case that a complex type reference types from other type libs.
			final List<IFile> xsdFiles = TypeLibraryUtil.getAllXsdFiles(project, true);
			xsdFiles.add(xsdFile);
			SynchronizeWsdlAndDepXML synch = new SynchronizeWsdlAndDepXML(project);
			synch.syncronizeAllXSDsandDepXml(xsdFiles);
			synch.synchronizeTypeDepandProjectDep(monitor);
		}

		processTemplate(typeParamModel, xsdFile, monitor);
		
	}
	
	/**
	 * Creates the type. Performs file creation, process the template, calls
	 * code generation, refresh the registry and opens the created file in an
	 * editor.
	 *
	 * @param typeParamModel the type param model
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createType(CommonTypeProp typeParamModel,
			IProgressMonitor monitor) throws Exception {
		String typeName = typeParamModel.getTypeName();
		String typeLibraryName = typeParamModel.getTypeLibraryName();

		IProject project = WorkspaceUtil.getProject(typeLibraryName);
		String xsdFileName = TypeLibraryUtil.getXsdFileLocation(typeName,
				project);
		IFile xsdFile = createXsd(project, xsdFileName, monitor);
		
		Configuration cfg = new Configuration();
        StringTemplateLoader tloader = new StringTemplateLoader();
        cfg.setTemplateLoader(tloader);
        tloader.putTemplate("ImportType", typeParamModel.getSchemaTemplate());
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        Template temp = cfg.getTemplate("ImportType");
        
        Writer writer = new StringWriter();
        Map<String, String> mapping = new ConcurrentHashMap<String, String>();
        mapping.put(XSDTypeParser.namespaceMarker, typeParamModel.getTargetNamespace());
        mapping.put(XSDTypeParser.typeNameMarker, typeParamModel.getTypeName());
        mapping.put(XSDTypeParser.documentMarker, typeParamModel.getDescription());
        temp.process(mapping, writer);
        IOUtil.writeTo(writer.toString(), xsdFile, monitor);
	}

	/**
	 * Call codegen.
	 *
	 * @param project the project
	 * @param typeLibName the type lib name
	 * @param typeName the type name
	 * @throws Exception the exception
	 */
	public static void callCodegen(IProject project, String typeLibName,
			String typeName) throws Exception {
		GenTypeAddType genTypeAddType = new GenTypeAddType();
		genTypeAddType.setProjectRoot(project.getLocation().toString());
		genTypeAddType.setLibraryName(typeLibName);
		ArrayList<String> types = new ArrayList<String>();
		types.add(TypeLibraryUtil.getXsdFileNameFromTypeName(typeName));
		genTypeAddType.setTypes(types);
		CodegenInvoker codegenInvoker = TypeLibraryBuilderUtils
				.initForTypeLib(project);
		codegenInvoker.execute(genTypeAddType);
	}

	private static void processTemplate(TypeParamModel typeParamModel,
			IFile destinationXSDFile, IProgressMonitor monitor)
			throws IOException, CoreException, CommandFailedException,
			ValidationInterruptedException {
		new XSDTemplateProcessor().process(typeParamModel, destinationXSDFile,
				monitor);
	}

	private static IFile createXsd(IProject project, String fileName,
			IProgressMonitor monitor) throws CoreException {
		IFile file = WorkspaceUtil.createEmptyFile(project, fileName, monitor);
		ProgressUtil.progressOneStep(monitor);
		return file;
	}

	/**
	 * Updates the type registry and opens the xsd in an editor.
	 *
	 * @param typeName the type name
	 * @param version the version
	 * @param typeLibName the type lib name
	 * @param xsdFile the xsd file
	 * @param refreshSOATypeRegistry the refresh soa type registry
	 * @param openXSDEditor the open xsd editor
	 * @throws Exception the exception
	 */
	public static void postProcessTypeCreation(String typeName,
			String version, String typeLibName, IFile xsdFile, boolean refreshSOATypeRegistry, boolean openXSDEditor) throws Exception {
		SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter.getInstance();
		SOATypeRegistry typeRegistry = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getTypeRegistryBridge().getSOATypeRegistry();//registryAdapter.getGlobalRegistry();
		TypeLibraryType typeLibInfo = typeRegistry.getTypeLibrary(typeLibName);
		LibraryType libraryType = TypeLibraryUtil.getLibraryType(typeName,
				version, typeLibInfo);

		registryAdapter.addTypeToRegistry(libraryType);

		if (SOALogger.DEBUG) {
			for (LibraryType libraryType2 : typeRegistry.getAllTypes()) {
				logger.debug(libraryType2.getName());
			}
		}
		
		if(refreshSOATypeRegistry == true){
			TypeLibraryUtil.refreshTypeDependencyInSOATypeRegistry(typeLibName);
		}
		
		if (openXSDEditor == true && xsdFile.isAccessible()) {
			IDE.openEditor(UIUtil.getActiveWorkBenchWindow().getActivePage(),
					xsdFile);
		}
	}
	
	/**
	 * Updates the type registry and opens the xsd in an editor.
	 *
	 * @param typeName the type name
	 * @param version the version
	 * @param typeLibName the type lib name
	 * @param xsdFile the xsd file
	 * @throws Exception the exception
	 */
	public static void postProcessTypeCreation(String typeName,
			String version, String typeLibName, IFile xsdFile) throws Exception {
		postProcessTypeCreation(typeName, version, typeLibName, xsdFile, true, true);
	}
}
