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
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;


/**
 * @author smathew
 * 
 */
public class SOAImplProject extends SOAConsumerProject implements ISOAConsumerProject{

	public static final String[] SOURCE_DIRECTORIES = {
		SOAProjectConstants.FOLDER_SRC,
		SOAProjectConstants.FOLDER_GEN_TEST,
		SOAProjectConstants.FOLDER_GEN_SRC_SERVICE,
		SOAProjectConstants.FOLDER_GEN_META_SRC,
		SOAProjectConstants.FOLDER_META_SRC };

	public static final String GEN_META_SRC_SERVICES_CONFIG = SOAProjectConstants.GEN_META_SRC_META_INF
	+ "/soa/services/config";

	public static final String META_SRC_ClIENT_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/client/config";
	public static final String META_SRC_SERVICES_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/services/config";

	public static SOAImplProject create(SOAImplMetadata implMetadata,
			SOAProjectEclipseMetadata eclipseMetadata) throws Exception{
		SOAImplProject implProject = new SOAImplProject();
		implProject.setMetadata(implMetadata);
		implProject.setEclipseMetadata(eclipseMetadata);
		return implProject;
	}

	public SOAImplMetadata getMetadata() {
		final AbstractSOAMetadata metadata = super.getMetadata();
		return metadata instanceof SOAImplMetadata ? (SOAImplMetadata)metadata : null;
	}

	@Override
	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return SOAImplMetadata.class;
	}

	public List<String> getSourceSubFolders() {
		List<String> subFolders = new ArrayList<String>();
		subFolders.add(GEN_META_SRC_SERVICES_CONFIG);
		//	subFolders.add(META_SRC_ClIENT_CONFIG);
		subFolders.add(META_SRC_SERVICES_CONFIG + WorkspaceUtil.PATH_SEPERATOR
				+ getMetadata().getIntfMetadata().getServiceName());
		return subFolders;
	}

	public IFile getServiceConfigFile() {
		return SOAImplUtil.getServiceConfigFile(getProject(), 
				getMetadata().getIntfMetadata().getServiceName());
	}

	public IFile getImplProjectPropertiesFile() {
		return getProject().getFile(SOAProjectConstants.PROPS_FILE_SERVICE_IMPL);
	}

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

	public IFolder getWebContentRoot() {
		return getProject().getFolder(SOAProjectConstants.FOLDER_WEB_CONTENT);
	}

}
