/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model.PropertiesSOAErrorDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAErrorDomainCreationFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;


/**
 * @author haozhou
 *
 */
public class PropertiesContentErrorDomainCreator implements IErrorDomainCreator {

	/**
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorDomainCreator#preCreation(org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel)
	 */
	public void preCreation(DomainParamModel model)
			throws SOAErrorDomainCreationFailedException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorDomainCreator#postCreation(org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postCreation(DomainParamModel model, IProgressMonitor monitor)
			throws SOAErrorDomainCreationFailedException {
		Map<String, String> params = new ConcurrentHashMap<String, String>();
		String errorLibraryName = model.getErrorLibrary();
		String domainName = model.getDomain();
		params.put("packageName", model.getPackageName());
		params.put("domain", domainName);
		params.put("org", model.getOrganization());
		params.put("library", errorLibraryName);
		IProject libraryProject = WorkspaceUtil.getProject(errorLibraryName);
		try {
			IAssetInfo assetInfo = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getAssetRegistry().getAsset(libraryProject);
			if (assetInfo != null) {
				params.put("version", assetInfo.getVersion());
			} else {
				params.put("version", "");
			}
		} catch (Exception e1) {
			throw new SOAErrorDomainCreationFailedException(e1);
		}
		
		IPath path = libraryProject.getFile(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN).getLocation();
		
		IPath targetFolder = path.append("/").append(domainName);
		
		final URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path(PropertiesSOAConstants.PATH_TEMPLATE), null);
		FileOutputStream output = null;
		try {
			File targetFolderFile = targetFolder.toFile();
			if (!targetFolderFile.exists()) {
				targetFolderFile.mkdir();
			}
			File target = targetFolder.append("/").append(PropertiesSOAConstants.FILE_ERROR_DATA).toFile();
			target.createNewFile();
			output = new FileOutputStream(target);
			FreeMarkerUtil.generate(params, url, PropertiesSOAConstants.FILE_TEMPLATE, output);
			//update property file
			TurmericErrorLibraryUtils.addDomainListProps(libraryProject, domainName, monitor);
			//add to error registry view
			PropertiesSOAErrorDomain domain = new PropertiesSOAErrorDomain();
			domain.setLibrary(TurmericErrorRegistry.getErrorLibraryByName(errorLibraryName));
			domain.setName(domainName);
			domain.setOrganization(model.getOrganization());
			domain.setPackageName(model.getPackageName());
			TurmericErrorRegistry.addErrorDomain(errorLibraryName, domain);
			//create empty error property
			createPropsFile(libraryProject, monitor, model);
		} catch (Exception e) {
			SOAErrorDomainCreationFailedException exp = new SOAErrorDomainCreationFailedException(e);
			throw exp;
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	private void createPropsFile(IProject project, IProgressMonitor monitor, DomainParamModel model) throws CoreException, IOException {
		IFile file = TurmericErrorLibraryUtils.getErrorPropsFile(project, model.getDomain(), model.getLocale());
		OutputStream output = null;
		WorkspaceUtil.refresh(file.getParent(), monitor);
		try {
			WorkspaceUtil.writeToFile("", file, monitor);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

}
