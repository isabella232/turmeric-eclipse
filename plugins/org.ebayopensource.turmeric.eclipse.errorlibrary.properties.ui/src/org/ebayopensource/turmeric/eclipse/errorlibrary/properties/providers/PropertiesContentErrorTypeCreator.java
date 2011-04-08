/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model.PropertiesSOAError;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.ErrorObjectXMLParser;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.SOAErrorBundleVO;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo.SOAErrorVO;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAFileNotWritableException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAErrorCreationFailedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.tools.errorlibrary.exception.ErrorIdGeneratorException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author haozhou
 *
 */
public class PropertiesContentErrorTypeCreator implements IErrorTypeCreator {

	/**
	 * {@inheritDoc}
	 */
	public void preCreation(ErrorParamModel model)
			throws SOAErrorCreationFailedException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	public void postCreation(ErrorParamModel model, IProgressMonitor monitor)
			throws SOAErrorCreationFailedException {
		String errorLibraryName = model.getErrorLibrary();
		String domain = model.getDomain();
		IProject libraryProject = WorkspaceUtil.getProject(errorLibraryName);
		IFile dataFile = getDomainFile(libraryProject, domain);
		ProgressUtil.progressOneStep(monitor);
		InputStream input = null;
		try {
			if (dataFile.isAccessible()) {
				WorkspaceUtil.refresh(dataFile);
				input = dataFile.getContents();
				SOAErrorBundleVO bundle = ErrorObjectXMLParser.getErrorBundle(input);
				ProgressUtil.progressOneStep(monitor);
				SOAErrorVO error = createErrorType(model);
				ProgressUtil.progressOneStep(monitor);
				bundle.getList().getErrors().add(error);
				String data = ErrorObjectXMLParser.convertErrorBundle(bundle);
				WorkspaceUtil.writeToFile(data, dataFile, monitor);
				ProgressUtil.progressOneStep(monitor);
				//add to error registry view
				PropertiesSOAError soaError = new PropertiesSOAError();
				soaError.setCategory(error.getCategory());
				soaError.setId("" + error.getId());
				soaError.setResolution(model.getResolution());
				soaError.setSeverity(model.getSeverity());
				soaError.setSubDomain(model.getSubdomain());
				soaError.setName(model.getName());
				soaError.setDomain(TurmericErrorRegistry.getErrorDomainByName(model.getDomain()));
				soaError.setMessage(model.getMessage());
				soaError.setResolution(model.getResolution());
				//update error properties
				updatePropsFile(libraryProject, monitor, model);
				ProgressUtil.progressOneStep(monitor);
				TurmericErrorRegistry.addError(model.getDomain(), soaError);
				ProgressUtil.progressOneStep(monitor);
			}
		} catch (Exception e) {
			SOAErrorCreationFailedException soae = new SOAErrorCreationFailedException(
					e);
			throw soae;
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	private void updatePropsFile(IProject project, IProgressMonitor monitor, ErrorParamModel model) throws CoreException, IOException {
		IFolder domainFolder = TurmericErrorLibraryUtils.getErrorDomainFolder(project, model.getDomain());
		TurmericErrorLibraryUtils.addErrorToPropsFile(domainFolder, model);
	}
	
	private SOAErrorVO createErrorType(ErrorParamModel model) throws IllegalArgumentException, IllegalStateException, SOAFileNotWritableException, ErrorIdGeneratorException {
		SOAErrorVO vo = new SOAErrorVO();
		vo.setCategory(model.getCategory());
		vo.setErrorGroups("");
		vo.setId(model.getNID());
		vo.setName(model.getName());
		vo.setSeverity(model.getSeverity());
		vo.setSubdomain(model.getSubdomain());
		return vo;
	}
	
	private IFile getDomainFile(IProject project, String domainName) {
		return project.getFile(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN + "/"
				+ domainName + "/" + PropertiesSOAConstants.FILE_ERROR_DATA);
	}
	
}
