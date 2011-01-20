/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.test.utils;

import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorDomainCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.ErrorTypeCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IncrementalProjectBuilder;

//import com.ebay.tools.soa.errorlibrary.utils.SOAErrorLibraryConstants;

/**
 * @author shrao
 *
 */
public class ELUtil {
	/*
	 * ErrorLibrary Creation
	 */
	public static boolean createErrorLibrary(String errorLibraryName,
			String contentRepoRoot, String locale, String location) {
		try {
			ErrorLibraryParamModel model = new ErrorLibraryParamModel();			
			model.setProjectName(errorLibraryName);
			model.setContentRepoRoot(contentRepoRoot);
			model.setLocale(locale);
			model.setOverrideWorkspaceRoot(false);
			model.setWorkspaceRootDirectory(location);
			SimpleTestUtil.setAutoBuilding(true);
			System.out.println(location);
			ErrorLibraryCreator.createErrorLibrary(model, ProgressUtil
					.getDefaultMonitor(null));
			WorkspaceUtil.getProject(model.getProjectName()).build(
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
	 * Error Domain Creation
	 */
	public static boolean createErrorDomain(String domainName, String errorLibraryName,
			String contentRepoRoot, String locale, String organization) {
		try {
				DomainParamModel errParamModel = new DomainParamModel();
				errParamModel.setDomain(domainName);
				errParamModel.setErrorLibrary(errorLibraryName);
				errParamModel.setLocale(locale);
				errParamModel.setOrganization(organization);
				SimpleTestUtil.setAutoBuilding(false);
				ErrorDomainCreator.createErrorDomain(errParamModel, ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.getProject(errParamModel.getErrorLibrary()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Error Type Creation
	 */
	public static boolean createErrorType(long nID, String errorID, String domainName,
	String category, String name, String organization, String severity,	String message,
	String resolution,	String errorLibraryName, String subdomain) {
		try {
				ErrorParamModel errParamModel = new ErrorParamModel();
				errParamModel.setNID(nID);
				errParamModel.setErrorID(errorID);
				errParamModel.setDomain(domainName);
				errParamModel.setCategory(category);
				errParamModel.setName(name);
				errParamModel.setOrganization(organization);
				errParamModel.setSeverity(severity);
				errParamModel.setMessage(message);
				errParamModel.setResolution(resolution);
				errParamModel.setErrorLibrary(errorLibraryName);
				errParamModel.setSubdomain(subdomain);
				
				SimpleTestUtil.setAutoBuilding(false);
				ErrorTypeCreator.createErrorType(errParamModel, ProgressUtil.getDefaultMonitor(null));
				WorkspaceUtil.getProject(errParamModel.getErrorLibrary()).build(
						IncrementalProjectBuilder.FULL_BUILD,
						ProgressUtil.getDefaultMonitor(null));
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
