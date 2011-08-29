/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;


// TODO: Auto-generated Javadoc
/**
 * The Class SOAImplProject.
 *
 * @author smathew
 */
public class SOAImplProject extends SOAConsumerProject implements ISOAConsumerProject{

	/**
	 * An array of source directory strings.
	 */
	public static final String[] SOURCE_DIRECTORIES = {
		SOAProjectConstants.FOLDER_SRC,
		SOAProjectConstants.FOLDER_GEN_TEST,
		SOAProjectConstants.FOLDER_GEN_SRC_SERVICE,
		SOAProjectConstants.FOLDER_GEN_META_SRC,
		SOAProjectConstants.FOLDER_META_SRC };

	/**
	 * Directory path for generated services configuration file.
	 */
	public static final String GEN_META_SRC_SERVICES_CONFIG = SOAProjectConstants.GEN_META_SRC_META_INF
	+ "/soa/services/config";

	/**
	 * Directory path for the client configuration file.
	 */
	public static final String META_SRC_ClIENT_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/client/config";
	
	/**
	 * Directory path for the services configuration file.
	 */
	public static final String META_SRC_SERVICES_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/services/config";

	/**
	 * Creates an instance of the SOAImplProject.  This probably should be a constructor.
	 *
	 * @param implMetadata implementation meta data
	 * @param eclipseMetadata eclipse meta data
	 * @return an instance of SOAImplProject
	 * @throws Exception the exception
	 */
	public static SOAImplProject create(SOAImplMetadata implMetadata,
			SOAProjectEclipseMetadata eclipseMetadata) throws Exception{
		SOAImplProject implProject = new SOAImplProject();
		implProject.setMetadata(implMetadata);
		implProject.setEclipseMetadata(eclipseMetadata);
		return implProject;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject#getMetadata()
	 */
	@Override
	public SOAImplMetadata getMetadata() {
		final AbstractSOAMetadata metadata = super.getMetadata();
		return metadata instanceof SOAImplMetadata ? (SOAImplMetadata)metadata : null;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject#getSOAMetadataClass()
	 */
	@Override
	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return SOAImplMetadata.class;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject#getSourceSubFolders()
	 */
	@Override
	public List<String> getSourceSubFolders() {
		List<String> subFolders = new ArrayList<String>();
		subFolders.add(GEN_META_SRC_SERVICES_CONFIG);
		//	subFolders.add(META_SRC_ClIENT_CONFIG);
		subFolders.add(META_SRC_SERVICES_CONFIG + WorkspaceUtil.PATH_SEPERATOR
				+ getMetadata().getIntfMetadata().getServiceName());
		return subFolders;
	}

	/**
	 * Gets the service config file.
	 *
	 * @return the service configuration file as an IFile
	 */
	public IFile getServiceConfigFile() {
		return SOAImplUtil.getServiceConfigFile(getProject(), 
				getMetadata().getIntfMetadata().getServiceName());
	}

	/**
	 * Gets the impl project properties file.
	 *
	 * @return the implementation project properties file as an IFile
	 */
	public IFile getImplProjectPropertiesFile() {
		return getProject().getFile(SOAProjectConstants.PROPS_FILE_SERVICE_IMPL);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject#getSourceDirectoryNames()
	 */
	@Override
	public List<String> getSourceDirectoryNames() {
		final List<String> result = ListUtil.arrayList(SOURCE_DIRECTORIES);
		if (getMetadata() != null) {
			final String baseConsumerDir = getMetadata().getBaseConsumerSrcDir();
			if (StringUtils.isNotBlank(baseConsumerDir) && 
					result.contains(baseConsumerDir) == false) {
				result.add(baseConsumerDir);
			}
		}
		return result;
	}

	/**
	 * Gets the web content root.
	 *
	 * @return the IFolder that contains the web content root
	 */
	public IFolder getWebContentRoot() {
		return getProject().getFolder(SOAProjectConstants.FOLDER_WEB_CONTENT);
	}

}
