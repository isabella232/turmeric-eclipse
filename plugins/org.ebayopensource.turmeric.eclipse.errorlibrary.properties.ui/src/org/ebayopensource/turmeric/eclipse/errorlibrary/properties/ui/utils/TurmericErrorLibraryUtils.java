/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.xbean.classloader.JarFileClassLoader;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model.PropertiesSOAError;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model.PropertiesSOAErrorDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model.PropertiesSOAErrorLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.ErrorObjectXMLParser;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.SOAErrorBundleVO;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.SOAErrorVO;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jdom.JDOMException;


/**
 * @author yayu
 *
 */
public final class TurmericErrorLibraryUtils {
	private static final MessageFormat errorPropsFormat = new MessageFormat(PropertiesSOAConstants.PROPS_FILE_DEFAULT_ERROR_PROPERTIES);

	/**
	 * 
	 */
	private TurmericErrorLibraryUtils() {
		super();
	}
	
	public static IFile getDomainListPropsFile(IProject project) {
		return project.getFile(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN + "/"
				+ project.getName() + "/"
				+ PropertiesSOAConstants.PROPS_FILE_ERROR_LIBRARY_PROJECT);
	}
	
	public static IFile getErrorPropsFile(IProject project, String domainName) throws CoreException {
		IFolder domainFolder = getErrorDomainFolder(project, domainName);
		return getErrorPropsFile(domainFolder);
	}
	
	public static IFile getErrorPropsFile(IFolder domainFolder) throws CoreException {
		IFile defaultFile = getErrorPropsFile(domainFolder, PropertiesSOAConstants.DEFAULT_LOCALE);
		IFile noSuffixFile = domainFolder.getFile(PropertiesSOAConstants.PROPS_FILE_NO_LOCALE);
		if (defaultFile.isAccessible()) {
			return defaultFile;
		} else if (noSuffixFile.isAccessible()) {
			return noSuffixFile;
		} else if (domainFolder.isAccessible()){
			for (IResource res : domainFolder.members()) {
				if (res instanceof IFile) {
					final IFile file = (IFile)res;
					if (file.getName().startsWith("Errors_")) {
						return file;
					}
				}
			}
		}
		return null;
	}
	
	public static IFile getErrorPropsFile(IProject project, String domainName, String locale) {
		IFolder domainFolder = getErrorDomainFolder(project, domainName);
		return getErrorPropsFile(domainFolder, locale);
	}
	
	public static IFile getErrorPropsFile(IFolder domainFolder, String locale) {
		String propsFilename = errorPropsFormat.format(new Object[]{locale});
		return domainFolder.getFile(propsFilename);
	}
	
	public static Collection<String> getAllErrorDomains(IProject project) throws CoreException {
		final Collection<String> domains = new ArrayList<String>();
		if (project != null && project.isAccessible() == true) {
			IFolder domainParentFolder = project.getFolder(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN);
			if (domainParentFolder.isAccessible() == false) {
				return domains;
			}
			for (IResource res : domainParentFolder.members()) {
				if (res instanceof IFolder) {
					final IFolder folder = (IFolder)res;
					if (folder.findMember(
							PropertiesSOAConstants.FILE_ERROR_DATA) != null) {
						domains.add(folder.getName());
					}
				}
			}
		}
		return domains;
	}
	
	public static IFolder getErrorDomainFolder(IProject project, String errorDomainName) {
		return project.getFolder(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN).getFolder(errorDomainName);
	}
	
	public static ISOAErrLibrary loadErrorLibrary(AssetInfo assetInfo) 
	throws Exception {
		ISOAErrLibrary result = null;
		final IProject project = WorkspaceUtil.getProject(assetInfo.getName());
		if (project.isAccessible()) {
			PropertiesSOAErrorLibrary errLib = new PropertiesSOAErrorLibrary();
			errLib.setName(assetInfo.getName());
			errLib.setVersion(assetInfo.getVersion());

			for (SOAErrorBundleVO bundle : ErrorObjectXMLParser.getErrorBundle(project)){
				if (bundle != null) {
					PropertiesSOAErrorDomain domain = new PropertiesSOAErrorDomain();
					domain.setLibrary(errLib);
					domain.setName(bundle.getDomain());
					domain.setOrganization(bundle.getOrganization());
					domain.setVersion(bundle.getVersion());
					domain.setPackageName(bundle.getPackageName());
					errLib.addErrorDomain(domain);
					//load error property file
					IFile errorPropFile = getErrorPropsFile(project, domain.getName());
					Properties errorProperties = new Properties();
					InputStream input = null;
					if (errorPropFile != null && errorPropFile.isAccessible()) {
						try {
							input = errorPropFile.getContents();
							errorProperties.load(input);
						} finally {
							IOUtils.closeQuietly(input);
						}
					}
					for (SOAErrorVO vo : bundle.getList().getErrors()) {
						PropertiesSOAError err = new PropertiesSOAError();
						err.setCategory(vo.getCategory());
						err.setDomain(domain);
						err.setId(String.valueOf(vo.getId()));
						err.setName(vo.getName());
						err.setSeverity(vo.getSeverity());
						err.setSubDomain(vo.getSubdomain());
						err.setMessage(errorProperties.getProperty(err.getName() + "." + PropertiesSOAConstants.PROPS_KEY_MESSAGE ));
						err.setResolution(errorProperties.getProperty(err.getName() + "." + PropertiesSOAConstants.PROPS_KEY_RESOLUTION));
						domain.addError(err);
					}
				}
			}
			result = errLib;
		} else {
			result = loadErrorLibraryFromJar(assetInfo);
		}
		return result;
	}
	
	private static ISOAErrLibrary loadErrorLibraryFromJar(AssetInfo assetInfo) throws Exception {
		String assetLocation = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getAssetRegistry().getAssetLocation(assetInfo);
		String defaultErrorPropertyName = StringUtil.formatString(
				PropertiesSOAConstants.PROPS_FILE_DEFAULT_ERROR_PROPERTIES,
				PropertiesSOAConstants.DEFAULT_LOCALE);
		if (assetLocation != null) {
			File file = new File(assetLocation);
			if (file.canRead() == false)
				return null;
			URL url = file.toURI().toURL();
			JarFileClassLoader classLoader = null;
			InputStream ins = null;
			String vmFileEncoding = System.getProperty("file.encoding");
			try {
				URL decodedUrl = new URL(URLDecoder.decode(url.toString(), vmFileEncoding));
				classLoader = new JarFileClassLoader("ErrorLibJarFileLoader", 
						new URL[]{decodedUrl});
				String filePath = PropertiesSOAConstants.FOLDER_ERROR_DOMAIN_IN_JAR + "/"
				+ assetInfo.getName() + "/" + PropertiesSOAConstants.PROPS_FILE_ERROR_LIBRARY_PROJECT;
				URL domainListFile = classLoader.findResource(filePath);
				if (domainListFile == null)
					return null;
				ins = domainListFile.openStream();
				Properties props = new Properties();
				props.load(ins);
				PropertiesSOAErrorLibrary errLib = new PropertiesSOAErrorLibrary();
				errLib.setName(assetInfo.getName());
				errLib.setVersion(assetInfo.getVersion());
				for (String domainName : StringUtils.split(props.getProperty(
						PropertiesSOAConstants.PROPS_LIST_OF_DOMAINS, ""), ",")) {
					PropertiesSOAErrorDomain domain = new PropertiesSOAErrorDomain();
					domain.setLibrary(errLib);
					String domainFilePath = PropertiesSOAConstants.FOLDER_ERROR_DOMAIN_IN_JAR + "/"
					+ domainName + "/" + PropertiesSOAConstants.FILE_ERROR_DATA;
					String errorPropFilePath = PropertiesSOAConstants.FOLDER_ERROR_DOMAIN_IN_JAR
							+ "/" + domainName + "/" + defaultErrorPropertyName;
					URL domainUrl = classLoader.findResource(domainFilePath);
					URL errorPropUrl = classLoader.findResource(errorPropFilePath);
					if (domainUrl == null || errorPropUrl == null) {
						SOALogger.getLogger().log(Level.WARNING, "ErrorData.xml or error properties file is missing");
						continue;
					}
					InputStream in = null;
					InputStream errorPropInput = null;
					Properties errorProperties = new Properties();
					try {
						in = domainUrl.openStream();
						errorPropInput = errorPropUrl.openStream();
						errorProperties.load(errorPropInput);
						SOAErrorBundleVO bundle = ErrorObjectXMLParser.getErrorBundle(in);
						domain.setName(bundle.getDomain());
						domain.setOrganization(bundle.getOrganization());
						domain.setVersion(bundle.getVersion());
						domain.setPackageName(bundle.getPackageName());
						errLib.addErrorDomain(domain);
						for (SOAErrorVO vo : bundle.getList().getErrors()) {
							PropertiesSOAError err = new PropertiesSOAError();
							err.setCategory(vo.getCategory());
							err.setDomain(domain);
							err.setId(String.valueOf(vo.getId()));
							err.setName(vo.getName());
							err.setSeverity(vo.getSeverity());
							err.setSubDomain(vo.getSubdomain());
							err.setMessage(errorProperties.getProperty(err.getName() + "." + PropertiesSOAConstants.PROPS_KEY_MESSAGE ));
							err.setResolution(errorProperties.getProperty(err.getName() + "." + PropertiesSOAConstants.PROPS_KEY_RESOLUTION));
							domain.addError(err);
						}
					} finally {
						IOUtils.closeQuietly(in);
						IOUtils.closeQuietly(errorPropInput);
					}
				}
				return errLib;
			} finally {
				if (classLoader != null) {
					classLoader.destroy();
				}
				IOUtils.closeQuietly(ins);
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static void addDomainListProps(IProject project, String domainName, IProgressMonitor monitor) throws CoreException, IOException {
		IFile file = TurmericErrorLibraryUtils.getDomainListPropsFile(project);
		OutputStream output = new ByteArrayOutputStream();
		InputStream input = null;
		if (file.isAccessible()) {
			try {
				input = file.getContents();
				String key = PropertiesSOAConstants.PROPS_LIST_OF_DOMAINS;
				Collection<String> domains = TurmericErrorLibraryUtils
						.getAllErrorDomains(project);
				if (!domains.contains(domainName)) {
					domains.add(domainName);
				}
				String domainListStr = StringUtils.join(domains, ",");
//				if (PropertiesFileUtil.getPropertyValueByKey(input, key) == null) {
//					input = file.getContents();
//					Map<String, String> newProps = new HashMap<String, String>();
//					newProps.put(key, domainListStr);
//					PropertiesFileUtil.addProperty(input, output, newProps);
//				} else {
					input = file.getContents();
					PropertiesFileUtil.updatePropertyByKey(input, output, key,
							domainListStr);
//				}
				String contents = output.toString();
				WorkspaceUtil.writeToFile(contents, file, monitor);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
	}
	
	public static void removeDomainFromProps(IProject project, String domainName)
			throws IOException, CoreException {
		IFile file = TurmericErrorLibraryUtils.getDomainListPropsFile(project);
		if (file.isAccessible()) {
			String key = PropertiesSOAConstants.PROPS_LIST_OF_DOMAINS;
			OutputStream output = null;
			InputStream input = null;
			try {
				output = new ByteArrayOutputStream();
				input = file.getContents();
				Collection<String> domains = TurmericErrorLibraryUtils
				.getAllErrorDomains(project);
				if (domains.contains(domainName)) {
					domains.remove(domainName);
				}
				String domainListStr = StringUtils.join(domains, ",");
				PropertiesFileUtil.updatePropertyByKey(input, output, key,
						domainListStr);
				String contents = output.toString();
				WorkspaceUtil.writeToFile(contents, file, null);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
	}
	
	public static void removeErrorFromXmlData(IFolder domainFolder, ISOAError error) throws CoreException, JDOMException, IOException {
		IFile dataFile = domainFolder.getFile(PropertiesSOAConstants.FILE_ERROR_DATA);
		if (dataFile.isAccessible()) {
			InputStream input = null;
			try {
				input = dataFile.getContents();
				SOAErrorBundleVO bundle = ErrorObjectXMLParser.getErrorBundle(input);
				SOAErrorVO toBeDeleted = null;
				for (SOAErrorVO vo : bundle.getList().getErrors()) {
					if (String.valueOf(vo.getId()).equals(error.getId())) {
						toBeDeleted = vo;
						break;
					}
				}
				if (toBeDeleted != null) {
					bundle.getList().removeError(toBeDeleted);
				}
				String contents = ErrorObjectXMLParser.convertErrorBundle(bundle);
				WorkspaceUtil.writeToFile(contents, dataFile, new NullProgressMonitor());
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
	}
	
	public static void removeErrorFromPropsFile(IFolder domainFolder, ISOAError error) throws CoreException, IOException {
		IFile propsFile = getErrorPropsFile(domainFolder);
		if (propsFile != null && propsFile.isAccessible()) {
			InputStream input = null;
			OutputStream output = new ByteArrayOutputStream();
			try {
				input = propsFile.getContents();
				String[] keysToBeDeleted = new String[2];
				keysToBeDeleted[0] = error.getName() + "." + PropertiesSOAConstants.PROPS_KEY_MESSAGE;
				keysToBeDeleted[1] = error.getName() + "." + PropertiesSOAConstants.PROPS_KEY_RESOLUTION;
				PropertiesFileUtil.removePropertyByKey(input, output, keysToBeDeleted);
				String contents = output.toString();
				WorkspaceUtil.writeToFile(contents, propsFile, new NullProgressMonitor());
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
	}
	
	public static void addErrorToPropsFile(IFolder domainFolder, ErrorParamModel model) throws CoreException, IOException {
		IFile propsFile = getErrorPropsFile(domainFolder);
		if (propsFile != null && propsFile.isAccessible()) {
			InputStream input = null;
			OutputStream output = new ByteArrayOutputStream();
			try {
				input = propsFile.getContents();
				Map<String, String> newProps = new ConcurrentHashMap<String, String>();
				newProps.put(model.getName() + "." + PropertiesSOAConstants.PROPS_KEY_MESSAGE, model.getMessage());
				newProps.put(model.getName() + "." + PropertiesSOAConstants.PROPS_KEY_RESOLUTION, model.getResolution());
				PropertiesFileUtil.addProperty(input, output, newProps);
				String contents = output.toString();
				WorkspaceUtil.writeToFile(contents, propsFile, new NullProgressMonitor());
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
	}

}
