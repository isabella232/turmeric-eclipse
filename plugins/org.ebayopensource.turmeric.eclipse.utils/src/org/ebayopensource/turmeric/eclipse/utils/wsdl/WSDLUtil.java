/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.wsdl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaReference;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.xml.sax.InputSource;


/**
 * The Class WSDLUtil.
 *
 * @author yayu
 */
public final class WSDLUtil {
	
	/** The Constant URL_PREFIX_JAR_FILE. */
	public static final String URL_PREFIX_JAR_FILE = "jar:file:";
	
	/** The Constant JAR_FILE_SEPARATOR. */
	public static final String JAR_FILE_SEPARATOR = "!" + WorkspaceUtil.PATH_SEPERATOR;
	
	/** The Constant ATTR_ID_TARGETNAMESPACE. */
	public static final String ATTR_ID_TARGETNAMESPACE = "targetNamespace";
	
	/** The Constant NAMESPACE_PREFIX_W3ORG. */
	public static final String NAMESPACE_PREFIX_W3ORG = "http://www.w3.org/";
	
	/** The Constant NAMESPACE_PREFIX_XMLSOAP. */
	public static final String NAMESPACE_PREFIX_XMLSOAP = "http://schemas.xmlsoap.org";

	private WSDLUtil() {
		super();
	}

	/**
	 * To url.
	 *
	 * @param file the file
	 * @return the uRL
	 */
	public static URL toURL(final File file) {
		if (file == null)
			return null;
		try {
			return file.toURI().toURL();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Checks if is valid url.
	 *
	 * @param url the url
	 * @return true, if is valid url
	 */
	public static boolean isValidURL(final String url) {
		//return Pattern.matches(VALID_URL_PATTERN, url);
		if (StringUtils.isBlank(url))
			return false;
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	/**
	 * Validate url.
	 *
	 * @param url the url
	 * @return the string
	 */
	public static String validateURL(final String url) {
		if (StringUtils.isBlank(url))
			return "url is blank.";
		try {
			new URL(StringEscapeUtils.escapeHtml(url));
		} catch (final MalformedURLException e) {
			return e.getLocalizedMessage();
		}
		return "";
	}

	/**
	 * Read wsdl.
	 *
	 * @param wsdlLocation The fully qualified location of the WSDL file.
	 * @return The WSDL definition instance.
	 * @throws WSDLException If the any errors encountered during the deserialization.
	 */
	public static Definition readWSDL(final String wsdlLocation)
			throws WSDLException {
		final WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		return reader.readWSDL(wsdlLocation);
	}
	
	/**
	 * This is intended to be used for wsdl file inside jar file.
	 *
	 * @param wsdlStream the wsdl stream
	 * @return the definition
	 * @throws WSDLException the wSDL exception
	 */
	public static Definition readWSDL(final InputStream wsdlStream)
	throws WSDLException {
		return readWSDL(null, wsdlStream);
	}
	
	/**
	 * Reading wsdl from the provided jar file.
	 * The format for documetn base URI would be "jar:file:[JAR_FILE_LOCATION]!/[WSDL_JAR_ENTRY_PATH]"
	 *
	 * @param file the file
	 * @param jarEntryLocation the jar entry location
	 * @return The instance of WSDL definition, or null is could not read it.
	 * @throws WSDLException the wSDL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Definition readWSDLFromJarFile(final File file, 
			final String jarEntryLocation)
	throws WSDLException, IOException {
		InputStream wsdlStream = null;
		if (file.exists() && file.canRead()) {
			final JarFile jarFile = new JarFile(file);
			final JarEntry jarEntry = jarFile.getJarEntry(jarEntryLocation);
			if (jarEntry != null) {
				// found the wsdl file
				wsdlStream = jarFile.getInputStream(jarEntry);
				return WSDLUtil.readWSDL(StringUtil.toString(
						URL_PREFIX_JAR_FILE, file.getAbsolutePath(), 
						JAR_FILE_SEPARATOR, jarEntryLocation), 
						wsdlStream);
			}
		}
		return null;
	}

	/**
	 * Read wsdl.
	 *
	 * @param documentBaseURI The parent folder of the wsdl file
	 * @param wsdlStream The input stream
	 * @return The WSDL definition instance.
	 * @throws WSDLException If the any errors encountered during the deserialization.
	 */
	public static Definition readWSDL(final String documentBaseURI, final InputStream wsdlStream)
			throws WSDLException {
		try {
			final WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
			InputSource inputSource = new InputSource(wsdlStream);
			return reader.readWSDL(documentBaseURI, inputSource);
		} finally {
			IOUtils.closeQuietly(wsdlStream);
		}
	}

	private static String[] getParentFolder(String fullFileName) {
		String[] result = new String[2];
		final String slash = "/";
		if (StringUtils.isNotBlank(fullFileName)) {
			fullFileName = StringUtils.replaceChars(fullFileName, File.separator, slash);
			result[0] = fullFileName.indexOf(slash) >= 0 ? StringUtils
					.substringBeforeLast(fullFileName, slash) : "";
			result[1] = StringUtils.substringAfterLast(fullFileName, "/");
		}
		return result;
	}

	/**
	 * The use of this method is discouraged, because it will try to read the
	 * WSDL first, which might cause some performance issues.
	 *
	 * @param wsdl the wsdl
	 * @param fileName the file name
	 * @throws WSDLException the wSDL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeWSDL(final String wsdl, final String fileName)
			throws WSDLException, IOException {
		writeWSDL(readWSDL(wsdl), fileName);
	}

	/**
	 * Write wsdl.
	 *
	 * @param wsdl the wsdl
	 * @param fileName the file name
	 * @throws WSDLException the wSDL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeWSDL(final Definition wsdl, final String fileName)
			throws WSDLException, IOException {
		writeWSDL(wsdl, fileName, new HashSet<String>());
	}
	
	private static InputStream readUrl(String locUri) throws MalformedURLException,IOException {
		URL url =  URI.create(locUri).toURL();
		return url.openStream();

	}

	private static void handleImports(final Collection<List<Import>> imports,
			final String parentFolder, final Set<String> processedSchemas,
			final Map<InputStream, String> streamsToCopy) throws IOException {
		for (List<Import> list : imports) {
			for (Import _import : list) {
				if(isRelativeURI(_import.getLocationURI())){// it is using a relative path
					final String locUri = _import.getDefinition().getDocumentBaseURI();

					if (processedSchemas.contains(locUri) == false) {
						final String schemaParentFolder = getParentFolder(_import
								.getLocationURI())[0];
						if (schemaParentFolder != null && parentFolder != null) {
							String targetFileLocation = parentFolder
									+ File.separator + schemaParentFolder
									+ File.separator
									+ FilenameUtils.getName(locUri);// a output
																	// file
																	// name;
							InputStream stream = readUrl(locUri);
							if (stream != null) {
								streamsToCopy.put(stream, targetFileLocation);
							}
						}
						processedSchemas.add(locUri);
					}
				}

				// as of now, we only support two levels of import
				if (_import.getDefinition().getTypes() != null) {
					final String schemaParentFolder = getParentFolder(_import
							.getLocationURI())[0];
					if (schemaParentFolder != null && parentFolder != null) {
						String targetLocation = parentFolder + File.separator
								+ schemaParentFolder;
						handleSchemas(_import.getDefinition().getTypes(),
								targetLocation, processedSchemas, streamsToCopy);
					}
				}
			}
		}
	}

	/**
	 * <p>
	 * Serialize the provided WSDL definition instance to the designated
	 * location.
	 * </p>
	 * 
	 * @param wsdl
	 *            The WSDL definition instance
	 * @param fileName
	 *            The fully qualified WSDL file name
	 * @param processedSchemas
	 *            A list of schema file location which has already been
	 *            processed. This prevents the issue that two schemas import
	 *            each other.
	 * @throws WSDLException
	 *             If failed to handle the WSDL serialization
	 * @throws IOException 
	 */
	private static void writeWSDL(final Definition wsdl, final String fileName,
			final Set<String> processedSchemas) throws WSDLException,
			IOException {
		final WSDLWriter writer = WSDLFactory.newInstance().newWSDLWriter();
		final String parentFolder = getParentFolder(fileName)[0];
		final Map<InputStream, String> streamsToCopy = new ConcurrentHashMap<InputStream, String>();

		// if there are any schema imports, we should be able to handle it
		if (wsdl.getImports() != null && wsdl.getImports().isEmpty() == false) {
			handleImports(
					(Collection<List<Import>>) wsdl.getImports().values(),
					parentFolder, processedSchemas, streamsToCopy);
		}
		if (wsdl.getTypes() != null) {
			handleSchemas(wsdl.getTypes(), parentFolder, processedSchemas,
					streamsToCopy);
		}

		File parentDir = new File(parentFolder);
		if (parentDir.exists() == false)
			FileUtils.forceMkdir(parentDir);

		for (InputStream srcStream : streamsToCopy.keySet()) {
			File targetFile = new File(streamsToCopy.get(srcStream));	
			FileUtils.forceMkdir(targetFile.getParentFile());

			try {
				FileOutputStream output = new FileOutputStream(targetFile);
				try {
					IOUtils.copy(srcStream, output);
				} finally {
					IOUtils.closeQuietly(output);
				}
			} finally {
				IOUtils.closeQuietly(srcStream);
			}
		}

		//output wsdl
		OutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			writer.writeWSDL(wsdl, out);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private static void handleSchemaReference(List<SchemaReference> schemaRefs,
			String parentFolder, final Set<String> processedSchemas,
			final Map<InputStream, String> streamsToCopy) throws IOException {
		for (SchemaReference schemaReference : schemaRefs) {
			if (!isRelativeURI(schemaReference.getSchemaLocationURI())) {
				continue;
			}
			
			final Schema refSchema = schemaReference.getReferencedSchema();
			final String locUri = refSchema != null ? refSchema
					.getDocumentBaseURI() : schemaReference
					.getSchemaLocationURI();

			if (processedSchemas.contains(locUri) == false && refSchema != null) {
				String schemaParentFolder = getParentFolder(schemaReference
						.getSchemaLocationURI())[0];
				
				if (schemaParentFolder != null && parentFolder != null) {
					String targetFileLocation = parentFolder + File.separator
							+ schemaParentFolder + File.separator
							+ FilenameUtils.getName(locUri);

					InputStream stream = readUrl(locUri);
					if (stream != null) {
						streamsToCopy.put(stream, targetFileLocation);
					}
				}
				
				processedSchemas.add(locUri);

				// next round
				if (refSchema.getImports().isEmpty() == false) {
					for (Object o : refSchema.getImports().values()) {
						handleSchemaReference((List<SchemaReference>) o,
								parentFolder, processedSchemas, streamsToCopy);
					}
				}

				if (CollectionUtils.isNotEmpty(refSchema.getIncludes())) {
					handleSchemaReference(refSchema.getIncludes(),
							parentFolder, processedSchemas, streamsToCopy);

				}
			}
		}
	}

	private static void handleSchemas(Types types, String parentFolder,
			final Set<String> processedSchemas,
			final Map<InputStream, String> streamsToCopy) throws IllegalArgumentException, IOException {
		for (Object obj : types.getExtensibilityElements()) {
			if (obj instanceof Schema) {
				Schema schema = (Schema) obj;
				for (Object key : schema.getImports().keySet()) {
					Object value = schema.getImports().get(key);
					if (value instanceof List) {
						handleSchemaReference((List<SchemaReference>) value,
								parentFolder, processedSchemas, streamsToCopy);
					}
				}
				
				if (CollectionUtils.isNotEmpty(schema.getIncludes())) {
					handleSchemaReference(schema.getIncludes(), parentFolder,
							processedSchemas, streamsToCopy);
				}
				
			} else if (obj instanceof UnknownExtensibilityElement) {
				throw new IllegalArgumentException(
						"The WSDL4J in the current environment is rather old and does not support the \"Schema\" datatype. Please make sure your environment use a newer version.");
			}
		}
	}
	
	/**
	 * Gets the service name from wsdl.
	 *
	 * @param wsdl the wsdl
	 * @return the service name from wsdl
	 */
	public static String getServiceNameFromWSDL(final Definition wsdl) {
		if (wsdl != null && wsdl.getServices().isEmpty() == false) {
			return ((javax.wsdl.Service)wsdl.getServices()
					.values().toArray()[0]).getQName().getLocalPart();
		}
		return null;
	}
	
	/**
	 * Gets the service location from wsdl.
	 *
	 * @param wsdl the wsdl
	 * @return the service location from wsdl
	 */
	public static String getServiceLocationFromWSDL(final Definition wsdl) {
		String serviceLocation = null;
		if (wsdl != null
				&& wsdl.getServices().isEmpty() == false) {
			for (Object obj : ((javax.wsdl.Service) (wsdl
					.getServices().values().toArray()[0]))
					.getPorts().values()) {
				javax.wsdl.Port port = (javax.wsdl.Port) obj;
				if (port.getExtensibilityElements().size() > 0) {
					Object elem = port
							.getExtensibilityElements().get(0);
					if (elem instanceof javax.wsdl.extensions.http.HTTPAddress) {
						serviceLocation = ((javax.wsdl.extensions.http.HTTPAddress) elem)
								.getLocationURI();
						break;
					} else if (elem instanceof javax.wsdl.extensions.soap.SOAPAddress) {
						serviceLocation = ((javax.wsdl.extensions.soap.SOAPAddress) elem)
								.getLocationURI();
						break;
					} else {
						try {
							final Method method = elem.getClass().getMethod("getLocationURI");
							final Object result = method.invoke(elem);
							if (result != null) {
								serviceLocation = result.toString();
								//although we have found the service location, 
								//but we would still prefer the http and soap addresses,
								//thus we will not break from the loop.
							}
						} catch (Exception e) {
							StackTraceElement[] elements = new Throwable().getStackTrace();
							Logger.getLogger(WSDLUtil.class.getName()).throwing(
									elements[0].getClassName(), elements[0].getMethodName(), e);
						}
					}
				}
			}
		}
		return serviceLocation;
	}

	/**
	 * Gets the target namespace.
	 *
	 * @param location The location of the WSDL file
	 * @return the target namespace
	 * @throws WSDLException the wSDL exception
	 */
	public static String getTargetNamespace(final String location)
			throws WSDLException {
		return readWSDL(location).getTargetNamespace();
	}
	
	/**
	 * Gets the target namespace.
	 *
	 * @param wsdl the wsdl
	 * @return the target namespace
	 * @throws WSDLException the wSDL exception
	 */
	public static String getTargetNamespace(final Definition wsdl)
	throws WSDLException {
		return wsdl.getTargetNamespace();
	}

	/**
	 * Gets the target namespace.
	 *
	 * @param documentBaseURI the document base uri
	 * @param inpuStream the inpu stream
	 * @return the target namespace
	 * @throws WSDLException the wSDL exception
	 */
	public static String getTargetNamespace(final String documentBaseURI, final InputStream inpuStream)
			throws WSDLException {
		return readWSDL(documentBaseURI, inpuStream).getTargetNamespace();
	}
	
	//Moving util methods from WSDLUtilTest class to here
	/**
	 * Gets the plugin os path.
	 *
	 * @param pluginId the plugin id
	 * @param subDirPath the sub dir path
	 * @return the plugin os path
	 */
	public static String getPluginOSPath(String pluginId, String subDirPath) {
		URL platformContextURL = getPluginOSURL(pluginId, subDirPath);
		String fullPath = (new File(platformContextURL.getPath())).getAbsolutePath();
		IPath osPath = new Path(fullPath);
		return osPath.toString();
	}

	
	/**
	 * Gets the plugin osurl.
	 *
	 * @param pluginId the plugin id
	 * @param subDirPath the sub dir path
	 * @return the plugin osurl
	 */
	public static URL getPluginOSURL(String pluginId, String subDirPath) {
		Bundle bundle = Platform.getBundle(pluginId);
		if (bundle == null) {
			return null;
		}

		URL installLocation = bundle.getEntry("/");
		URL local = null;
		URL platformContextURL = null;
		try {
			local = FileLocator.toFileURL(installLocation);

			platformContextURL = subDirPath == null ? local : new URL(local, subDirPath.toString());
		} catch (Exception _ex) {
			return null;
		}
		return platformContextURL;
	}

	
	private static boolean isRelativeURI(String url) {
		if(StringUtils.isEmpty(url))
			return false;
		
		URI uri = null;
		try {
			uri = URI.create(url);
		} catch (RuntimeException e) {
			// ignore it
		}

		return uri != null && !uri.isAbsolute();
	}
	
	/**
	 * Known W3 and XMLSOAP namespaces will not be included.
	 *
	 * @param wsdl the wsdl
	 * @return a list of TargetNamespaces from the schemas including the imported
	 */
	public static Collection<String> getAllTargetNamespaces(final Definition wsdl) {
		final Collection<String> result = new LinkedHashSet<String>();
		if (wsdl == null)
			return result;
		/*if (StringUtils.isNotBlank(wsdl.getTargetNamespace()))
			result.add(wsdl.getTargetNamespace());*/
		
		if (wsdl.getImports() != null && wsdl.getImports().isEmpty() == false) {
			for (List<Import> list : (Collection<List<Import>>) wsdl.getImports().values()) {
				for (Import _import : list) {
					result.addAll(getAllTargetNamespaces(_import.getDefinition()));
				}
			}
		}
		
		if (wsdl.getTypes() != null) {
			for (Object obj : wsdl.getTypes().getExtensibilityElements()) {
				if (obj instanceof Schema) {
					getAllTargetNamespaces((Schema)obj, result, new HashSet<String>());
				} else if (obj instanceof UnknownExtensibilityElement) {
					throw new IllegalArgumentException(
					"The WSDL4J in the current environment is rather old and does not support the \"Schema\" datatype. Please make sure your environment use a newer version.");
				}
			}
		}
		return result;
	}
	
	private static void getAllTargetNamespaces(final Schema schema, final Collection<String> namespaces, 
			final Set<String> processedSchemas) {
		if (schema != null) {
			final String locUri = schema.getDocumentBaseURI();
			//we need to check whether this schema has been processed or not
			if (processedSchemas.contains(locUri))
				return;
			processedSchemas.add(locUri);
			final String targetNamespace = schema.getElement().getAttribute(ATTR_ID_TARGETNAMESPACE);
			if (StringUtils.isNotBlank(targetNamespace) 
					&& targetNamespace.startsWith(NAMESPACE_PREFIX_W3ORG) == false
					&& targetNamespace.startsWith(NAMESPACE_PREFIX_XMLSOAP) == false)
				namespaces.add(targetNamespace.trim());
			for (Object key : schema.getImports().keySet()) {
				Object value = schema.getImports().get(key);
				if (value instanceof List) {
					for (final SchemaReference ref : (List<SchemaReference>)value) {
						getAllTargetNamespaces(ref.getReferencedSchema(), namespaces, processedSchemas);
					}
				}
			}
			
			if (CollectionUtils.isNotEmpty(schema.getIncludes())) {
				for (final SchemaReference ref : (List<SchemaReference>)schema.getIncludes()) {
					getAllTargetNamespaces(ref.getReferencedSchema(), namespaces, processedSchemas);
				}
			}
		}
	}
}
