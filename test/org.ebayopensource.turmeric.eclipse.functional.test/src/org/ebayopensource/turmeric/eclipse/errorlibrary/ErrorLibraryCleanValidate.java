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
package org.ebayopensource.turmeric.eclipse.errorlibrary;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;


/**
 * @author shrao
 * 
 */
public class ErrorLibraryCleanValidate {

	static String PARENT_DIR = getParentDir();
	static boolean validateMatch = true;

	public static String getParentDir() {
		String parentDir = null;
		parentDir = org.eclipse.core.runtime.Platform.getLocation()
				.toOSString();
		System.out.println(" --- eBox service projects parent directory = "
				+ parentDir);
		return parentDir;
	}


	
	public static boolean validateErrorLibraryArtifacts(IProject errorlibProj,
			String goldCopyFolder) {
		ErrorLibraryValidator pav = new ErrorLibraryValidator();
		pav.setGoldCopyRootDir(goldCopyFolder);
		try {
			errorlibProj.accept(pav);
			if (pav.isMatches() == false) {
				validateMatch = false;
				System.out.println("Goldcopy validation failed for "
						+ errorlibProj.getName());
			}
		} catch (CoreException e) {
			validateMatch = false;
			System.out.println("Exception in validateIntfArtifacts() for "
					+ errorlibProj.getName());
			e.printStackTrace();
		}
		return validateMatch;
	}

	

}
