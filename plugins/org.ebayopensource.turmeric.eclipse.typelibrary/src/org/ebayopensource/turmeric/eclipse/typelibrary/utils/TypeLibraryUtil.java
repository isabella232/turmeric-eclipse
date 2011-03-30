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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.config.exception.SOAConfigAreaCorruptedException;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOATypeCreationFailedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeCreator;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeDepMarshaller;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.wst.WTPCopyUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.exception.ImportTypeException;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProject;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProjectResolver;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeCCParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ImportTypeModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.TypeLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeWizardElementPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.ebayopensource.turmeric.tools.library.TypeLibraryConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author smathew
 * 
 *         Util class for Type Lib
 */
public class TypeLibraryUtil {

	/**
	 * Get Template Category Map
	 * 
	 * @return
	 */
	public static Map<SOAXSDTemplateSubType, String> getTemplateCategoryMap() {
		Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> templateCategoryFiles = getTemplateCategoryFiles();
		Map<SOAXSDTemplateSubType, String> templateCategoryMap = new LinkedHashMap<SOAXSDTemplateSubType, String>();
		for (SOAXSDTemplateSubType subType : templateCategoryFiles.keySet()) {
			if (subType.equals(SOAXSDTemplateSubType.SIMPLE))
				templateCategoryMap.put(SOAXSDTemplateSubType.SIMPLE,
						SOATypeLibraryConstants.SIMPLE_TYPE_NAME);
			else if (subType.equals(SOAXSDTemplateSubType.COMPLEX))
				templateCategoryMap.put(SOAXSDTemplateSubType.COMPLEX,
						SOATypeLibraryConstants.COMPLEX_TYPE_NAME);
			else if (subType.equals(SOAXSDTemplateSubType.ENUM))
				templateCategoryMap.put(SOAXSDTemplateSubType.ENUM,
						SOATypeLibraryConstants.ENUM_TYPE_NAME);
			else if (subType
					.equals(SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT))
				templateCategoryMap.put(
						SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT,
						SOATypeLibraryConstants.COMPLEXSC_TYPE_NAME);
			else if (subType
					.equals(SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT))
				templateCategoryMap.put(
						SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT,
						SOATypeLibraryConstants.COMPLEXCC_TYPE_NAME);
		}
		return templateCategoryMap;
	}

	/**
	 * Get all template Category files
	 * 
	 * @return
	 */
	public static Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> getTemplateCategoryFiles() {
		try {
			final String organization = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem()
					.getActiveOrganizationProvider().getName();
			return SOAConfigExtensionFactory.getXSDTemplates(organization);
		} catch (SOAConfigAreaCorruptedException e) {
			UIUtil.showErrorDialog(e);
		}
		return null;
	}

	/**
	 * Get files inside a template category
	 * 
	 * @return
	 */
	public static List<SOAConfigTemplate> getFiles(SOAXSDTemplateSubType subType) {
		Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> templates = getTemplateCategoryFiles();
		if (templates != null) {
			return templates.get(subType);
		}
		return null;
	}

	/**
	 * @param typeName
	 * @return
	 * 
	 *         Answers the file location relative to the project structure. In
	 *         short project.getFile() with this output should return the file.
	 */
	public static String getXsdFileLocation(String typeName, IProject project) {
		String retValue = "";
		if (!StringUtils.isEmpty(typeName)) {
			if (isNewTypLibrary(project)) {
				retValue = SOATypeLibraryConstants.FOLDER_META_SRC_TYPES
						+ WorkspaceUtil.PATH_SEPERATOR + project.getName()
						+ WorkspaceUtil.PATH_SEPERATOR + typeName
						+ SOATypeLibraryConstants.EXT_XSD;

			} else {
				retValue = SOATypeLibraryConstants.FOLDER_META_SRC_TYPES
						+ WorkspaceUtil.PATH_SEPERATOR + typeName
						+ SOATypeLibraryConstants.EXT_XSD;
			}
		}
		return retValue;
	}

	/**
	 * Just appending the extn to the XSD name. Extn names could be changed :)),
	 * You never know. This is just a dumb helper
	 * 
	 * @param typeName
	 * @return
	 */
	public static String getXsdFileNameFromTypeName(String typeName) {
		String retValue = typeName;
		if (!StringUtils.isEmpty(typeName)
				&& !typeName.endsWith(SOATypeLibraryConstants.EXT_XSD))
			retValue = retValue + SOATypeLibraryConstants.EXT_XSD;
		return retValue;
	}

	/**
	 * Just taking out the extn from the file name. Extn names could be changed
	 * :)), You never know. This is just a dumb helper
	 * 
	 * @param typeName
	 * @return
	 */
	public static String getXsdTypeNameFromFileName(String fileName) {
		String retValue = fileName;
		if (!StringUtils.isEmpty(fileName)) {
			retValue = StringUtils.substringBeforeLast(fileName,
					SOATypeLibraryConstants.EXT_XSD);
		}
		return retValue;
	}

	/**
	 * Adding the TypeLibProtocal to the name for the xsd entry.
	 * 
	 * @param typeName
	 * @return
	 */
	public static String getProtocolString(LibraryType type) {
		String retValue = null;
		if (type != null)
			retValue = SOATypeLibraryConstants.TURMERIC_XSD_FILE_PROTOCOL
					+ SOATypeLibraryConstants.PROTOCOL_DELIMITER_START
					+ type.getLibraryInfo().getLibraryName()
					+ SOATypeLibraryConstants.PROTOCOL_DELIMITER
					+ getXsdFileNameFromTypeName(type.getName());
		return retValue;
	}

	/**
	 * Adding the TypeLibProtocal to the name for the xsd entry.
	 * 
	 * @param typeName
	 * @return
	 */
	public static IFile getDependencyFile(IProject project) {
		return project.getFile(SOATypeLibraryConstants.FOLDER_META_SRC_META_INF
				+ WorkspaceUtil.PATH_SEPERATOR + project.getName()
				+ WorkspaceUtil.PATH_SEPERATOR
				+ SOATypeLibraryConstants.FILE_TYPE_DEP_XML);
	}

	/**
	 * Adding the TypeLibProtocal to the name for the xsd entry.
	 * 
	 * @param typeName
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static InputStream getDependencyStream(String typeLibName,
			String baseLocation) throws CoreException, IOException {

		if (baseLocation.toLowerCase().startsWith("jar:file:/")) {
			JarFile jarFile = new JarFile(getJarFile(baseLocation.substring(10,
					baseLocation.length())));
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().startsWith(
						SOATypeLibraryConstants.INFO_DEP_XML_PATH_IN_JAR)
						&& entry.getName().endsWith(
								SOATypeLibraryConstants.FILE_TYPE_DEP_XML)) {
					return jarFile.getInputStream(entry);
				}
			}
		} else {
			IProject project = WorkspaceUtil.getProject(typeLibName);
			if (project.isAccessible()
					&& project.hasNature(GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().getProjectNatureId(
									SupportedProjectType.TYPE_LIBRARY))) {
				return project.getFile(
						SOATypeLibraryConstants.FOLDER_META_SRC_META_INF
								+ WorkspaceUtil.PATH_SEPERATOR
								+ project.getName()
								+ WorkspaceUtil.PATH_SEPERATOR
								+ SOATypeLibraryConstants.FILE_TYPE_DEP_XML)
						.getContents();
			}
		}
		throw new IOException(
				"InpuStream could not be obtained with the type lib Name "
						+ typeLibName + " and base location " + baseLocation);
	}

	public static LibraryType getLibraryType(String name, String version,
			TypeLibraryType libInfo) throws Exception {
		SOATypeRegistry typeRegistry = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getTypeRegistryBridge()
				.getSOATypeRegistry();
		LibraryType libType = typeRegistry.getType(name, libInfo
				.getLibraryName());
		if (libType != null) {
			return libType;
		}
		LibraryType libraryType = new LibraryType();
		libraryType.setName(name);
		libraryType.setVersion(version);
		libraryType.setLibraryInfo(libInfo);
		return libraryType;
	}

	public static URL getXSD(LibraryType libType) throws Exception {
		return getXSD(libType.getLibraryInfo().getLibraryName(), libType
				.getName());
	}

	public static URL getXSD(String libraryName, String typeName)
			throws Exception {
		String jarLocation = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry()
				.getAssetLocation(libraryName);
		if (!StringUtils.isEmpty(jarLocation)
				&& jarLocation.endsWith(SOAProjectConstants.JAR_EXT)) {
			String xsdName = TypeLibraryUtil
					.getXsdFileNameFromTypeName(typeName);
			URL jarURL = IOUtil.toURL(jarLocation);
			String jarEntryPath = "";
			if (TypeLibraryUtil.isNewTypLibrary(jarURL, libraryName)) {
				jarEntryPath = SOATypeLibraryConstants.TYPES_LOCATION_IN_JAR
						+ WorkspaceUtil.PATH_SEPERATOR + libraryName
						+ WorkspaceUtil.PATH_SEPERATOR + xsdName;
			} else {
				jarEntryPath = SOATypeLibraryConstants.TYPES_LOCATION_IN_JAR
						+ WorkspaceUtil.PATH_SEPERATOR + xsdName;
			}
			return IOUtil.getNonLockingURL(jarURL, jarEntryPath);
		} else {
			IProject project = WorkspaceUtil.getProject(libraryName);
			if (project.isAccessible()) {
				IFile file = project.getFile(TypeLibraryUtil
						.getXsdFileLocation(typeName, project));
				return file.getLocation().toFile().toURI().toURL();
			}
			return null;
		}
	}

	public static String getTypeNameFromProtocolString(String typeLibString) {
		String retValue = null;
		if (!StringUtils.isEmpty(typeLibString)
				&& typeLibString
						.startsWith(SOATypeLibraryConstants.TURMERIC_XSD_FILE_PROTOCOL)) {
			
			// removing type lib start string
			retValue = StringUtils.substringAfterLast(typeLibString,
					SOATypeLibraryConstants.PROTOCOL_DELIMITER);
			
			// removing xsd extn end string
			retValue = StringUtils.substringBeforeLast(retValue,
					SOATypeLibraryConstants.EXT_XSD);
		}
		return retValue;

	}

	public static String getLibraryNameFromProtocolString(String typeLibString) {
		String retValue = null;
		if (!StringUtils.isEmpty(typeLibString)
				&& typeLibString
						.startsWith(SOATypeLibraryConstants.TURMERIC_XSD_FILE_PROTOCOL)) {
			if (isNewStylePrototocol(typeLibString)) {
				retValue = StringUtils.substringBetween(typeLibString,
						SOATypeLibraryConstants.PROTOCOL_DELIMITER,
						SOATypeLibraryConstants.PROTOCOL_DELIMITER);
			}
		}
		return retValue;
	}

	private static boolean isNewStylePrototocol(String typeLibString) {
		return StringUtils.countMatches(typeLibString,
				SOATypeLibraryConstants.PROTOCOL_DELIMITER) == 2;
	}

	public static String getTypeNameFromXSDSchemaLocation(String schemaLocation) {
		return getXsdTypeNameFromFileName(StringUtils.substringAfterLast(
				schemaLocation, "/"));
	}

	public static LibraryType createLibraryType(String typeName) {
		LibraryType libraryType = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getTypeRegistryBridge()
				.createLibraryTypeInstance();
		libraryType.setName(typeName);
		libraryType.setVersion("1.0.0");
		return libraryType;
	}

	public static IFile getTypeInformationFile(IProject project) {
		return project
				.getFile(SOATypeLibraryConstants.FOLDER_GEN_META_SRC_META_INF
						+ WorkspaceUtil.PATH_SEPERATOR + project.getName()
						+ WorkspaceUtil.PATH_SEPERATOR
						+ SOATypeLibraryConstants.FILE_TYPE_INFO_XML);

	}

	public static TypeLibraryType getTypeLibraryType(IProject project)
			throws CoreException {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getTypeRegistryBridge().unmarshalTypeInformationType(
						getTypeInformationFile(project).getContents());

	}

	public static List<IFile> getAllXsdFiles(IProject project,
			boolean checkExistance) throws Exception {
		List<IFile> files = null;
		if (isNewTypLibrary(project)) {
			files = WorkspaceUtil
					.getFilesWithExtensions(
							project
									.getFolder(SOATypeLibraryConstants.FOLDER_META_SRC_TYPES
											+ WorkspaceUtil.PATH_SEPERATOR
											+ project.getName()),
							checkExistance, SOATypeLibraryConstants.XSD);

		} else {
			files = WorkspaceUtil.getFilesWithExtensions(project
					.getFolder(SOATypeLibraryConstants.FOLDER_META_SRC_TYPES),
					checkExistance, SOATypeLibraryConstants.XSD);
		}
		return files;
	}

	public static String getDragNDropString(LibraryType type) {
		String retValue = "";
		IEditorPart editorPart = UIUtil.getActiveEditor();
		IFileEditorInput editorInput = (IFileEditorInput) editorPart
				.getEditorInput();
		InputStream readInputStream = null;
		try {
			if (!StringUtils.isEmpty(type.getName())) {
				if (StringUtils.equalsIgnoreCase(editorInput.getFile()
						.getFileExtension(), SOATypeLibraryConstants.XSD)) {
					String tagBegin = null;
					String tagEnd = null;
					boolean needTargetNamespace = false;
					XSDSchema xsdSchema = parseSchema(editorInput.getFile()
							.getContents());
					if (StringUtils.equals(type.getNamespace(), xsdSchema
							.getTargetNamespace())) {
						tagBegin = SOATypeLibraryConstants.DND_PREFIX;
						tagEnd = SOATypeLibraryConstants.DND_SUFFIX;
					} else {
						tagBegin = SOATypeLibraryConstants.DND_PREFIX_IMPORT;
						tagEnd = SOATypeLibraryConstants.DND_SUFFIX_IMPORT;
						needTargetNamespace = true;
					}
					StringBuffer buf = new StringBuffer();
					buf.append(tagBegin);
					buf.append(getProtocolString(type));
					if (needTargetNamespace == true) {
						buf.append("\" namespace=\"");
						buf.append(type.getNamespace());
					}
					buf.append(tagEnd);
					retValue = buf.toString();
				} else if (StringUtils.equalsIgnoreCase(editorInput.getFile()
						.getFileExtension(), "wsdl")) {
					XSDSchema xsdSchema = parseSchema(TypeLibraryUtil
							.getXSD(type));
					XSDTypeDefinition typeDef = (XSDTypeDefinition) xsdSchema
							.getTypeDefinitions().get(0);
					StringBuffer buf = new StringBuffer();
					// add typelib source tag
					WTPCopyUtil.addAnnotation(typeDef, type);
					buf
							.append(XMLUtil.convertXMLToString(typeDef
									.getElement()));
					retValue = TemplateUtils.formatContents(buf.toString());
				}
			}
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
		} finally {
			IOUtils.closeQuietly(readInputStream);
		}
		return retValue;
	}

	/**
	 * This is just for WTP WSDL and XSD editors. Dont get confused and use it
	 * for generic pruposes.
	 * 
	 * @param editorPart
	 * @return
	 */
	public static Object getAdapterClassFromWTPEditors(IEditorPart editorPart) {
		Object retValue = null;
		if (editorPart != null) {

			IEditorSite editorSite = editorPart.getEditorSite();
			if (editorSite != null && editorSite instanceof MultiPageEditorSite) {
				MultiPageEditorSite multiPageEditorSite = (MultiPageEditorSite) editorSite;
				MultiPageEditorPart multiPageEditorPart = multiPageEditorSite
						.getMultiPageEditor();
				if (multiPageEditorPart != null) {
					Object adaptedObject = multiPageEditorPart
							.getAdapter(Definition.class);
					if (adaptedObject != null
							&& adaptedObject instanceof Definition) {
						retValue = adaptedObject;
					} else {
						adaptedObject = multiPageEditorPart
								.getAdapter(XSDSchema.class);
						if (adaptedObject != null
								&& adaptedObject instanceof XSDSchema) {
							retValue = adaptedObject;
						}
					}
				}
			} else {
				Object adaptedObject = editorPart.getAdapter(Definition.class);
				if (adaptedObject != null
						&& adaptedObject instanceof Definition) {
					retValue = adaptedObject;
				} else {
					adaptedObject = editorPart.getAdapter(XSDSchema.class);
					if (adaptedObject != null
							&& adaptedObject instanceof XSDSchema) {
						retValue = adaptedObject;
					}
				}
			}
		}
		return retValue;
	}

	/**
	 * This is just for WTP WSDL and XSD editors. Dont get confused and use it
	 * for generic pruposes.
	 * 
	 * @param editorPart
	 * @return
	 */
	public static ITextOperationTarget getFormatter(IEditorPart editorPart) {
		ITextOperationTarget retValue = null;
		if (editorPart != null) {
			IEditorSite editorSite = editorPart.getEditorSite();
			if (editorSite != null && editorSite instanceof MultiPageEditorSite) {
				MultiPageEditorSite multiPageEditorSite = (MultiPageEditorSite) editorSite;
				MultiPageEditorPart multiPageEditorPart = multiPageEditorSite
						.getMultiPageEditor();
				if (multiPageEditorPart != null) {
					Object adaptedObject = multiPageEditorPart
							.getAdapter(ITextOperationTarget.class);
					if (adaptedObject != null
							&& adaptedObject instanceof ITextOperationTarget) {
						retValue = (ITextOperationTarget) adaptedObject;
					}
				}
			} else {
				Object adaptedObject = editorPart
						.getAdapter(ITextOperationTarget.class);
				if (adaptedObject != null
						&& adaptedObject instanceof ITextOperationTarget) {
					retValue = (ITextOperationTarget) adaptedObject;
				}
			}
		}
		return retValue;
	}

	public static XSDSchema parseSchema(URL url) throws IOException {
		InputStream inputStream = null;
		inputStream = url.openStream();
		return parseSchema(inputStream);
	}

	public static XSDSchema parseSchema(InputStream inputStream)
			throws IOException {
		try {
			XSDParser xSDParser = new XSDParser();
			xSDParser.parse(inputStream);
			return xSDParser.getSchema();
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
		}
	}

	public static boolean isValidXSDFile(Object fileObject)
			throws CoreException {
		IFile file = null;
		if (fileObject instanceof IFile) {
			file = (IFile) fileObject;
		}
		return file != null
				&& file.isAccessible()
				&& StringUtils.equalsIgnoreCase(SOATypeLibraryConstants.XSD,
						file.getFileExtension())
				&& file.getProject() != null
				&& file.getProject().isAccessible()
				&& file.getProject().hasNature(
						TypeLibraryProjectNature.getTypeLibraryNatureId());
	}

	public static boolean isValidXSDFileDeleted(Object fileObject)
			throws CoreException {
		IFile file = null;
		if (fileObject instanceof IFile) {
			file = (IFile) fileObject;
		}
		return file != null
				&& StringUtils.equalsIgnoreCase(SOATypeLibraryConstants.XSD,
						file.getFileExtension())
				&& !file.exists()
				&& file.getProject() != null
				&& file.getProject().isAccessible()
				&& file.getProject().hasNature(
						TypeLibraryProjectNature.getTypeLibraryNatureId());
	}

	public static boolean isValidXSDFileModified(Object fileObject)
			throws CoreException {
		IFile file = null;
		if (fileObject instanceof IFile) {
			file = (IFile) fileObject;
		}
		return file != null
				&& StringUtils.equalsIgnoreCase(SOATypeLibraryConstants.XSD,
						file.getFileExtension())
				&& file.exists()
				&& file.getProject() != null
				&& file.getProject().isAccessible()
				&& file.getProject().hasNature(
						TypeLibraryProjectNature.getTypeLibraryNatureId());
	}

	public static String getProjectNameFromWTPBaseLocation(
			String baseLocationStr) throws ParserConfigurationException,
			SAXException, IOException {
		IPath path = new Path(baseLocationStr);
		// this is a jar location if base location starts with jar for eg:
		// "jar:file:/D:/Views/soapost22/v3jars/services/MarketPlaceServiceCommonTypeLibrary/3.0.0/java50/MarketPlaceServiceCommonTypeLibrary.jar!/types/BaseServiceResponse.xsd"
		if (baseLocationStr.toLowerCase().startsWith("jar:file:/")) {
			JarFile jarFile = new JarFile(getJarFile(baseLocationStr.substring(
					10, baseLocationStr.length())));
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().startsWith(
						SOATypeLibraryConstants.INFO_DEP_XML_PATH_IN_JAR)
						&& entry.getName().endsWith(
								SOATypeLibraryConstants.FILE_TYPE_INFO_XML)) {
					return getTypeLibName(entry, jarFile);
				}
			}
		} else {
			// New style has the type library name also. So removing an
			// additional segment at the end.
			if (isNewStyleBaseLocation(baseLocationStr)) {
				path = path.removeLastSegments(4);
			} else {
				// removing meta-src/types/abcd.xsd
				path = path.removeLastSegments(3);
			}
		}
		return path.lastSegment();
	}

	private static File getJarFile(String baseLocation) {
		String[] parts = StringUtils.splitByWholeSeparator(baseLocation, "!/");
		File file = new File(parts[0]);
		return file;
	}

	private static String getTypeLibName(JarEntry entry, JarFile jarFile)
			throws SAXException, IOException, ParserConfigurationException {
		String typeLibName = "Lib Not Found";
		InputStream stream = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			stream = jarFile.getInputStream(entry);
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();
			typeLibName = doc.getDocumentElement().getAttribute(
					SOATypeLibraryConstants.ATTR_TYPE_INFO_LIBNAME);
		} catch (ParserConfigurationException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (SAXException e) {
			throw e;
		} finally {
			if (stream != null)
				IOUtils.closeQuietly(stream);
		}

		return typeLibName;
	}

	public static boolean isNewStyleBaseLocation(String baseLocationStr) {
		return !(StringUtils.countMatches(
				StringUtils.substringAfterLast(baseLocationStr,
						SOATypeLibraryConstants.FOLDER_META_SRC_TYPES),
				WorkspaceUtil.PATH_SEPERATOR) == 1);
	}

	public static String getTypeNameFromWTPBaseLocation(String baseLocationStr) {
		IPath path = new Path(baseLocationStr);
		// removing meta-src/types/abcd.xsd
		return getXsdTypeNameFromFileName(path.lastSegment());
	}

	/**
	 * 
	 * @param category
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static InputStream getTemplateStream(SOAXSDTemplateSubType category,
			String type) throws IOException {
		List<SOAConfigTemplate> templateCategoryFiles = getTemplateCategoryFiles()
				.get(category);
		for (SOAConfigTemplate categoryFile : templateCategoryFiles) {
			if (categoryFile.getName().equals(type)) {
				return categoryFile.getUrl().openStream();
			}
		}

		return null;
	}

	public static LibraryType toLibraryType(QName qname) throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
				.getType(qname);
	}

	public static QName toQName(LibraryType libraryType) throws Exception {
		return new QName(getNameSpace(libraryType), libraryType.getName());
	}

	public static String getNameSpace(LibraryType libraryType) {
		return !StringUtils.isEmpty(libraryType.getNamespace()) ? libraryType
				.getNamespace() : libraryType.getLibraryInfo()
				.getLibraryNamespace();
	}

	public static String getNameSpace(String projectName) throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
				.getTypeLibrary(projectName).getLibraryNamespace();
	}

	public static QName toQName(IFile typeFile) throws Exception {
		String typeLibraryNameSpace = SOAGlobalRegistryAdapter.getInstance()
				.getGlobalRegistry().getTypeLibrary(
						typeFile.getProject().getName()).getLibraryNamespace();
		String typeName = getXsdTypeNameFromFileName(typeFile.getName());
		return new QName(typeLibraryNameSpace, typeName);
	}


	/**
	 * Old type library project (in workspace) has the dir structure
	 * meta-src\types\<xsd> and the new one has
	 * meta-src\types\<typeLibName>\<xsd>
	 * 
	 * @return
	 */
	public static boolean isNewTypLibrary(IProject project) {
		return project.getFolder(
				SOATypeLibraryConstants.FOLDER_META_SRC_TYPES
						+ WorkspaceUtil.PATH_SEPERATOR + project.getName())
				.exists();
	}

	/**
	 * Old type library jar(NOT in workspace) has the dir structure \types\<xsd>
	 * and the new one has meta-src\types\<typeLibName>\<xsd>
	 * 
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static boolean isNewTypLibrary(URL jarURL, String projectName)
			throws IOException {
		File file = new File(jarURL.getPath());
		JarFile jarFile;
		jarFile = new JarFile(file);
		return jarFile.getEntry(SOATypeLibraryConstants.TYPES_LOCATION_IN_JAR
				+ WorkspaceUtil.PATH_SEPERATOR + projectName) != null;

	}

	/**
	 * check if version update is needed. Then update type library version in
	 * property file, rebuild, update version in dependency XML.
	 * 
	 * @throws Exception
	 */
	// mzang 2010-4-22 modify library version. not used yet
	public static void updateTypeLibraryVersion(String newVersion,
			SOATypeLibraryProject typelibProject, IProgressMonitor monitor)
			throws Exception {
		ProgressUtil.progressOneStep(monitor);
		// check if update version needed.
		TypeLibraryParamModel model = SOATypeLibraryProjectResolver
				.loadTypeLibraryModel(typelibProject.getProject());
		String oldVersion = model.getVersion().trim();
		ProgressUtil.progressOneStep(monitor);
		if (oldVersion.equals(newVersion) == true) {
			return;
		}
		// update library version in property file.
		model.setVersion(newVersion);
		ProgressUtil.progressOneStep(monitor);
		try {
			Properties properties = new Properties();
			File propFile = new File(typelibProject.getProject().getLocation()
					.toFile(), TYPE_LIB_PRJ_PROPERTIES_FILE_NAME);
			FileInputStream propis = null;
			try {
				propis = new FileInputStream(propFile);
				properties.load(propis);
			} finally {
				if (propis != null) {
					propis.close();
				}
			}

			ProgressUtil.progressOneStep(monitor);

			properties.setProperty(TypeLibraryConstants.TYPE_LIBRARY_VERSION,
					newVersion);
			OutputStream out = new FileOutputStream(propFile);
			properties.store(out, COMMENT);
			out.flush();
			out.close();

			ProgressUtil.progressOneStep(monitor);

			// update version number in dependency file
			IFile file = getDependencyFile(typelibProject.getProject());
			TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
					.unmarshallIt(file);

			ProgressUtil.progressOneStep(monitor);

			typeLibraryDependencyType.setVersion(newVersion);
			TypeDepMarshaller.marshallIt(typeLibraryDependencyType, file);
			WorkspaceUtil.refresh(file);

			ProgressUtil.progressOneStep(monitor);

			// refresh is needed.
			WorkspaceUtil.refresh(typelibProject.getProject());
			typelibProject.getProject().build(
					IncrementalProjectBuilder.FULL_BUILD, monitor);
			WorkspaceUtil.refresh(typelibProject.getProject());

			monitor.done();
		} catch (Exception e) {
			UIUtil.showErrorDialog(e);
		}
	}

	public static void importTypesToTypeLibrarySAXP(List<TypeModel> types,
			String tlProjectName, IProgressMonitor monitor) {
		try {
			IProject project = WorkspaceUtil.getProject(tlProjectName);
			WorkspaceUtil.refresh(project);
			monitor.subTask(" -> Creating xsd files "
					+ "and updating type registery...");
			for (TypeModel model : types) {
				String xsdFileName = TypeLibraryUtil.getXsdFileLocation(model
						.getTypeName(), project);
				IFile xsdFile = WorkspaceUtil.createEmptyFile(project,
						xsdFileName, monitor);
				ProgressUtil.progressOneStep(monitor);

				String content = model.getTypeContent().toString();
				
				File xsdSysFile = xsdFile.getLocation().toFile();
				PrintWriter pw = new PrintWriter(xsdSysFile, "UTF-8");
				pw.write(content);
				pw.close();

				xsdFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
				ProgressUtil.progressOneStep(monitor);
				TypeCreator.postProcessTypeCreation(model.getTypeName(),
						"1.0.0", tlProjectName, project.getFile(xsdFileName),
						false, false);
				ProgressUtil.progressOneStep(monitor);

			}
			monitor.subTask(" -> Updating xsd dependencies...");
			TypeLibSynhcronizer.syncronizeAllXSDsandDepXml(project, null);
			ProgressUtil.progressOneStep(monitor);

			monitor.subTask(" -> Updating project dependencies...");
			TypeLibSynhcronizer.synchronizeTypeDepandProjectDep(project,
					monitor);
			ProgressUtil.progressOneStep(monitor);

			monitor.subTask(" -> Updating project classpath...");
			BuildSystemUtil.updateSOAClasspathContainer(WorkspaceUtil
					.getProject(tlProjectName));
			ProgressUtil.progressOneStep(monitor);

			monitor.subTask(" -> Generating code...");
			// TypeCreator.callCodegen(project, typeLibName, typeName);
			TypeLibraryUtil
					.refreshTypeDependencyInSOATypeRegistry(tlProjectName);
			
			// TypeCreator.createType(model, monitor);
			// // invoke this when a type is created, for dependency
			// // need
			// BuildSystemUtil.updateSOAClasspathContainer(WorkspaceUtil
			// .getProject(tlProjectName));
			//
			// monitor.subTask(" -> Calling code gen for type generation: "
			// + model.getTypeName());
			// ProgressUtil.progressOneStep(monitor);
			//
			// final IProject project = WorkspaceUtil.getProject(model
			// .getTypeLibraryName());
			// final String typeLibName = model.getTypeLibraryName();
			// final String typeName = model.getTypeName();
			// final String version = model.getVersion();
			//
			// try {
			// ProgressUtil.progressOneStep(monitor);
			// WorkspaceUtil.refresh(project);
			// ProgressUtil.progressOneStep(monitor);
			// String xsdFileName = TypeLibraryUtil.getXsdFileLocation(
			// typeName, project);
			// ProgressUtil.progressOneStep(monitor);
			// TypeCreator.postProcessTypeCreation(typeName, version,
			// typeLibName, project.getFile(xsdFileName), false,
			// false);
			// TypeLibraryUtil
			// .refreshTypeDependencyInSOATypeRegistry(tlProjectName);
			// } catch (Exception e) {
			// SOALogger.getLogger().error(e);
			// throw new SOATypeCreationFailedException(
			// "Failed to import schema type", e);
			// }
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog("Error Occurs",
					"Error Occurs during type creation", e);
			return;
		}
		ProgressUtil.progressOneStep(monitor);

	}

	public static void importTypesToTypeLibrary(List<ImportTypeModel> types,
			String tlProjectName, IProgressMonitor monitor) {
		List<ImportTypeModel> allResolvedModel = new ArrayList<ImportTypeModel>();
		List<ImportTypeModel> resolvedModel = new ArrayList<ImportTypeModel>();
		do {
			resolvedModel.clear();
			for (ImportTypeModel importModel : types) {
				if (allResolvedModel.containsAll(importModel.getDependencies()
						.values()) == false) {
					// dependency not resolved yet.
					continue;
				}
				TypeParamModel model = importModel.getTypeModel();

				if (model instanceof ComplexTypeParamModel == true) {
					ComplexTypeParamModel cModel = (ComplexTypeParamModel) model;
					ComplexTypeWizardElementPage.ElementTableModel[] elements = cModel
							.getElementTableModel();
					try {
						for (ComplexTypeWizardElementPage.ElementTableModel element : elements) {
							Object eleType = element.getRawElementType();
							if (eleType instanceof QName) {
								QName keyDepQName = (QName) eleType;
								ImportTypeModel depModel = importModel
										.getDependencies().get(keyDepQName);
								QName depQName = new QName(depModel
										.getNamespace(), depModel.getName());
								LibraryType depType = SOAGlobalRegistryAdapter
										.getInstance().getGlobalRegistry()
										.getType(depQName);

								if (depType == null) {
									throw new ImportTypeException(
											"Failed to resolve \""
													+ cModel.getTypeName()
													+ "\" type dependency:"
													+ depQName);
								}
								element.setDatatype(depType);
							}
						}
						if (model instanceof ComplexTypeCCParamModel) {
							ComplexTypeCCParamModel ccModel = (ComplexTypeCCParamModel) model;
							Object baseType = ccModel.getBaseType();
							if (baseType instanceof QName) {
								QName keyDepQName = (QName) baseType;
								ImportTypeModel depModel = importModel
										.getDependencies().get(keyDepQName);
								QName depQName = new QName(depModel
										.getNamespace(), depModel.getName());
								LibraryType depType = SOAGlobalRegistryAdapter
										.getInstance().getGlobalRegistry()
										.getType(depQName);
								if (depType == null) {
									throw new ImportTypeException(
											"Failed to resolve \""
													+ cModel.getTypeName()
													+ "\" type dependency:"
													+ depQName);
								}
								ccModel.setBaseType(depType);
							}
						}

					} catch (Exception e) {
						SOALogger.getLogger().error(e);
						UIUtil.showErrorDialog(e);
					}
				}

				monitor.subTask(" -> " + model.getTypeName());
				model.setTypeLibraryName(tlProjectName);
				try {
					TypeCreator.createType(model, monitor);
					// invoke this when a type is created, for dependency
					// need
					BuildSystemUtil.updateSOAClasspathContainer(WorkspaceUtil
							.getProject(tlProjectName));

					monitor
							.subTask(" -> Calling code gen for type generation: "
									+ model.getTypeName());
					ProgressUtil.progressOneStep(monitor);

					final IProject project = WorkspaceUtil.getProject(model
							.getTypeLibraryName());
					final String typeLibName = model.getTypeLibraryName();
					final String typeName = model.getTypeName();
					final String version = model.getVersion();

					try {
						TypeCreator.callCodegen(project, typeLibName, typeName);
						ProgressUtil.progressOneStep(monitor);
						WorkspaceUtil.refresh(project);
						ProgressUtil.progressOneStep(monitor);
						String xsdFileName = TypeLibraryUtil
								.getXsdFileLocation(typeName, project);
						ProgressUtil.progressOneStep(monitor);
						TypeCreator.postProcessTypeCreation(typeName, version,
								typeLibName, project.getFile(xsdFileName),
								false, false);
						TypeLibraryUtil
								.refreshTypeDependencyInSOATypeRegistry(tlProjectName);
					} catch (Exception e) {
						SOALogger.getLogger().error(e);
						throw new SOATypeCreationFailedException(
								"Failed to import schema type", e);
					}
				} catch (Exception e) {
					SOALogger.getLogger().error(e);
					UIUtil.showErrorDialog("Error Occurs",
							"Error Occurs during type creation", e);
					return;
				}
				resolvedModel.add(importModel);
				ProgressUtil.progressOneStep(monitor);
			}
			allResolvedModel.addAll(resolvedModel);
			types.removeAll(resolvedModel);
		} while (resolvedModel.size() > 0);

	}

	public static void refreshTypeDependencyInSOATypeRegistry(
			String typeLibraryName) {
		try {
			SOAGlobalRegistryAdapter.getInstance()
					.refreshTypeDependencyInSOATypeRegistry(typeLibraryName);
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
		}
	}

	// copied from
	// TypeLibraryProjectPropertiesGenerator.TYPE_LIB_PRJ_PROPERTIES_FILE_NAME
	public static final String TYPE_LIB_PRJ_PROPERTIES_FILE_NAME = "type_library_project.properties";
	private static final String COMMENT = "*** Generated file, any changes will be lost upon regeneration ***";

}
