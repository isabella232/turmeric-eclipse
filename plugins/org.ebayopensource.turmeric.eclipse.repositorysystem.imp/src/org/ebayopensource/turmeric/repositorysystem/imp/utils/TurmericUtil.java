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
package org.ebayopensource.turmeric.repositorysystem.imp.utils;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject.SOAProjectSourceDirectory;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Class TurmericUtil.
 *
 * @author haozhou
 */
public class TurmericUtil {
	
	/**
	 * Configure error library project.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return the sOA base project
	 * @throws CoreException the core exception
	 */
	public static SOABaseProject configureErrorLibraryProject(final SOABaseProject project, 
    		IProgressMonitor monitor) throws CoreException {
        final List<SOAProjectSourceDirectory> srcDirs = 
        	new ArrayList<SOAProjectSourceDirectory>();
        srcDirs.add(new SOAProjectSourceDirectory(SOAProjectConstants.FOLDER_GEN_SRC, 
        		SOAProjectConstants.FOLDER_OUTPUT_DIR));
        srcDirs.add(new SOAProjectSourceDirectory(SOAProjectConstants.FOLDER_META_SRC, 
        		SOAProjectConstants.FOLDER_OUTPUT_DIR));
        
        project.setSourceDirectories(srcDirs);
        project.setOutputFolder(SOAProjectConstants.FOLDER_OUTPUT_DIR);
        SOAResourceCreator.createFolders(project.getProject(),
        		project, monitor);
        return project;
    }
}
