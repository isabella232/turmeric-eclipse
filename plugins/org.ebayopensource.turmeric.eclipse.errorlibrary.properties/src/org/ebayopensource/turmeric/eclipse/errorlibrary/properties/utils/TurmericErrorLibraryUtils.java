/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.ErrorObjectXMLParser;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.SOAErrorBundleVO;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.SOAErrorVO;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
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
 * The Class TurmericErrorLibraryUtils.
 *
 * @author yayu
 */
public final class TurmericErrorLibraryUtils {
	private static final MessageFormat errorPropsFormat = new MessageFormat(PropertiesSOAConstants.PROPS_FILE_DEFAULT_ERROR_PROPERTIES);

	/**
	 * 
	 */
	private TurmericErrorLibraryUtils() {
		super();
	}
	
	/**
	 * Gets the domain list props file.
	 *
	 * @param project the project
	 * @return the domain list props file
	 */
	public static IFile getDomainListPropsFile(IProject project) {
		return project.getFile(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN + "/"
				+ project.getName() + "/"
				+ PropertiesSOAConstants.PROPS_FILE_ERROR_LIBRARY_PROJECT);
	}
	
	/**
	 * Gets the error props file.
	 *
	 * @param project the project
	 * @param domainName the domain name
	 * @return the error props file
	 * @throws CoreException the core exception
	 */
	public static IFile getErrorPropsFile(IProject project, String domainName) throws CoreException {
		IFolder domainFolder = getErrorDomainFolder(project, domainName);
		return getErrorPropsFile(domainFolder);
	}
	
	/**
	 * Gets the error props file.
	 *
	 * @param domainFolder the domain folder
	 * @return the error props file
	 * @throws CoreException the core exception
	 */
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
	
	/**
	 * Gets the error props file.
	 *
	 * @param project the project
	 * @param domainName the domain name
	 * @param locale the locale
	 * @return the error props file
	 */
	public static IFile getErrorPropsFile(IProject project, String domainName, String locale) {
		IFolder domainFolder = getErrorDomainFolder(project, domainName);
		return getErrorPropsFile(domainFolder, locale);
	}
	
	/**
	 * Gets the error props file.
	 *
	 * @param domainFolder the domain folder
	 * @param locale the locale
	 * @return the error props file
	 */
	public static IFile getErrorPropsFile(IFolder domainFolder, String locale) {
		String propsFilename = errorPropsFormat.format(new Object[]{locale});
		return domainFolder.getFile(propsFilename);
	}
	
	/**
	 * Gets the all error domains.
	 *
	 * @param project the project
	 * @return the all error domains
	 * @throws CoreException the core exception
	 */
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
	
	/**
	 * Gets the error domain folder.
	 *
	 * @param project the project
	 * @param errorDomainName the error domain name
	 * @return the error domain folder
	 */
	public static IFolder getErrorDomainFolder(IProject project, String errorDomainName) {
		return project.getFolder(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN).getFolder(errorDomainName);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Adds the domain list props.
	 *
	 * @param project the project
	 * @param domainName the domain name
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
					input = file.getContents();
					PropertiesFileUtil.updatePropertyByKey(input, output, key,
							domainListStr);
				String contents = output.toString();
				WorkspaceUtil.writeToFile(contents, file, monitor);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
	}
	
	/**
	 * Removes the domain from props.
	 *
	 * @param project the project
	 * @param domainName the domain name
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
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
	
	/**
	 * Removes the error from xml data.
	 *
	 * @param domainFolder the domain folder
	 * @param error the error
	 * @throws CoreException the core exception
	 * @throws JDOMException the jDOM exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
	
	/**
	 * Removes the error from props file.
	 *
	 * @param domainFolder the domain folder
	 * @param error the error
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
	
	/**
	 * Adds the error to props file.
	 *
	 * @param domainFolder the domain folder
	 * @param model the model
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
