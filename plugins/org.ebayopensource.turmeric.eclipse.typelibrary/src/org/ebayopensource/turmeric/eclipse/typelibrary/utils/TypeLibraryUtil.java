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
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
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
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeLibraryUtil.
 *
 * @author smathew
 * 
 * Util class for Type Lib
 */
public class TypeLibraryUtil {

	/**
	 * Gets the xsd file location.
	 *
	 * @param typeName the type name
	 * @param project the project
	 * @return the xsd file location
	 * Answers the file location relative to the project structure. In
	 * short project.getFile() with this output should return the file.
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
	 * @param typeName the type name
	 * @return the xsd file name from type name
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
	 * @param fileName the file name
	 * @return the xsd type name from file name
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
	 * @param type the type
	 * @return the protocol string
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
	 * @param project the project
	 * @return the dependency file
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
	 * @param typeLibName the type lib name
	 * @param baseLocation the base location
	 * @return the dependency stream
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
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

	/**
	 * Gets the library type.
	 *
	 * @param name the name
	 * @param version the version
	 * @param libInfo the lib info
	 * @return the library type
	 * @throws Exception the exception
	 */
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

	/**
	 * Gets the xSD.
	 *
	 * @param libType the lib type
	 * @return the xSD
	 * @throws Exception the exception
	 */
	public static URL getXSD(LibraryType libType) throws Exception {
		return getXSD(libType.getLibraryInfo().getLibraryName(), libType
				.getName());
	}

	/**
	 * Gets the xSD.
	 *
	 * @param libraryName the library name
	 * @param typeName the type name
	 * @return the xSD
	 * @throws Exception the exception
	 */
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

	/**
	 * Gets the type name from protocol string.
	 *
	 * @param typeLibString the type lib string
	 * @return the type name from protocol string
	 */
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

	/**
	 * Gets the library name from protocol string.
	 *
	 * @param typeLibString the type lib string
	 * @return the library name from protocol string
	 */
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

	/**
	 * Checks if is new style prototocol.
	 *
	 * @param typeLibString the type lib string
	 * @return true, if is new style prototocol
	 */
	private static boolean isNewStylePrototocol(String typeLibString) {
		return StringUtils.countMatches(typeLibString,
				SOATypeLibraryConstants.PROTOCOL_DELIMITER) == 2;
	}

	/**
	 * Gets the type name from xsd schema location.
	 *
	 * @param schemaLocation the schema location
	 * @return the type name from xsd schema location
	 */
	public static String getTypeNameFromXSDSchemaLocation(String schemaLocation) {
		return getXsdTypeNameFromFileName(StringUtils.substringAfterLast(
				schemaLocation, "/"));
	}

	/**
	 * Creates the library type.
	 *
	 * @param typeName the type name
	 * @return the library type
	 */
	public static LibraryType createLibraryType(String typeName) {
		LibraryType libraryType = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getTypeRegistryBridge()
				.createLibraryTypeInstance();
		libraryType.setName(typeName);
		libraryType.setVersion("1.0.0");
		return libraryType;
	}

	/**
	 * Gets the type information file.
	 *
	 * @param project the project
	 * @return the type information file
	 */
	public static IFile getTypeInformationFile(IProject project) {
		return project
				.getFile(SOATypeLibraryConstants.FOLDER_GEN_META_SRC_META_INF
						+ WorkspaceUtil.PATH_SEPERATOR + project.getName()
						+ WorkspaceUtil.PATH_SEPERATOR
						+ SOATypeLibraryConstants.FILE_TYPE_INFO_XML);

	}

	/**
	 * Gets the type library type.
	 *
	 * @param project the project
	 * @return the type library type
	 * @throws CoreException the core exception
	 */
	public static TypeLibraryType getTypeLibraryType(IProject project)
			throws CoreException {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getTypeRegistryBridge().unmarshalTypeInformationType(
						getTypeInformationFile(project).getContents());

	}

	/**
	 * Gets the all xsd files.
	 *
	 * @param project the project
	 * @param checkExistance the check existance
	 * @return the all xsd files
	 * @throws Exception the exception
	 */
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
	 * Parses the schema.
	 *
	 * @param url the url
	 * @return the xSD schema
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XSDSchema parseSchema(URL url) throws IOException {
		InputStream inputStream = null;
		inputStream = url.openStream();
		return parseSchema(inputStream);
	}

	/**
	 * Parses the schema.
	 *
	 * @param inputStream the input stream
	 * @return the xSD schema
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Checks if is valid xsd file.
	 *
	 * @param fileObject the file object
	 * @return true, if is valid xsd file
	 * @throws CoreException the core exception
	 */
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

	/**
	 * Checks if is valid xsd file deleted.
	 *
	 * @param fileObject the file object
	 * @return true, if is valid xsd file deleted
	 * @throws CoreException the core exception
	 */
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

	/**
	 * Checks if is valid xsd file modified.
	 *
	 * @param fileObject the file object
	 * @return true, if is valid xsd file modified
	 * @throws CoreException the core exception
	 */
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

	/**
	 * Gets the project name from wtp base location.
	 *
	 * @param baseLocationStr the base location str
	 * @return the project name from wtp base location
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Gets the jar file.
	 *
	 * @param baseLocation the base location
	 * @return the jar file
	 */
	private static File getJarFile(String baseLocation) {
		String[] parts = StringUtils.splitByWholeSeparator(baseLocation, "!/");
		File file = new File(parts[0]);
		return file;
	}

	/**
	 * Gets the type lib name.
	 *
	 * @param entry the entry
	 * @param jarFile the jar file
	 * @return the type lib name
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
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

	/**
	 * Checks if is new style base location.
	 *
	 * @param baseLocationStr the base location str
	 * @return true, if is new style base location
	 */
	public static boolean isNewStyleBaseLocation(String baseLocationStr) {
		return !(StringUtils.countMatches(
				StringUtils.substringAfterLast(baseLocationStr,
						SOATypeLibraryConstants.FOLDER_META_SRC_TYPES),
				WorkspaceUtil.PATH_SEPERATOR) == 1);
	}

	/**
	 * Gets the type name from wtp base location.
	 *
	 * @param baseLocationStr the base location str
	 * @return the type name from wtp base location
	 */
	public static String getTypeNameFromWTPBaseLocation(String baseLocationStr) {
		IPath path = new Path(baseLocationStr);
		// removing meta-src/types/abcd.xsd
		return getXsdTypeNameFromFileName(path.lastSegment());
	}

	/**
	 * To library type.
	 *
	 * @param qname the qname
	 * @return the library type
	 * @throws Exception the exception
	 */
	public static LibraryType toLibraryType(QName qname) throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
				.getType(qname);
	}

	/**
	 * To q name.
	 *
	 * @param libraryType the library type
	 * @return the q name
	 * @throws Exception the exception
	 */
	public static QName toQName(LibraryType libraryType) throws Exception {
		return new QName(getNameSpace(libraryType), libraryType.getName());
	}

	/**
	 * Gets the name space.
	 *
	 * @param libraryType the library type
	 * @return the name space
	 */
	public static String getNameSpace(LibraryType libraryType) {
		return !StringUtils.isEmpty(libraryType.getNamespace()) ? libraryType
				.getNamespace() : libraryType.getLibraryInfo()
				.getLibraryNamespace();
	}

	/**
	 * Gets the name space.
	 *
	 * @param projectName the project name
	 * @return the name space
	 * @throws Exception the exception
	 */
	public static String getNameSpace(String projectName) throws Exception {
		return SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
				.getTypeLibrary(projectName).getLibraryNamespace();
	}

	/**
	 * To q name.
	 *
	 * @param typeFile the type file
	 * @return the q name
	 * @throws Exception the exception
	 */
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
	 * meta-src\types\<typeLibName>\<xsd>.
	 *
	 * @param project the project
	 * @return true, if is new typ library
	 */
	public static boolean isNewTypLibrary(IProject project) {
		return project.getFolder(
				SOATypeLibraryConstants.FOLDER_META_SRC_TYPES
						+ WorkspaceUtil.PATH_SEPERATOR + project.getName())
				.exists();
	}

	/**
	 * Old type library jar(NOT in workspace) has the dir structure \types\<xsd>
	 * and the new one has meta-src\types\<typeLibName>\<xsd>.
	 *
	 * @param jarURL the jar url
	 * @param projectName the project name
	 * @return true, if is new typ library
	 * @throws IOException Signals that an I/O exception has occurred.
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
	 * Refresh type dependency in soa type registry.
	 *
	 * @param typeLibraryName the type library name
	 */
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
	/** The Constant TYPE_LIB_PRJ_PROPERTIES_FILE_NAME. */
	public static final String TYPE_LIB_PRJ_PROPERTIES_FILE_NAME = "type_library_project.properties";
	
	/** The Constant COMMENT. */
	private static final String COMMENT = "*** Generated file, any changes will be lost upon regeneration ***";

}
