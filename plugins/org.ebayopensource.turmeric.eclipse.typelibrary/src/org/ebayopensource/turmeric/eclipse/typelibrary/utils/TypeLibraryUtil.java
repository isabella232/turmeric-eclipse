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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;
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
