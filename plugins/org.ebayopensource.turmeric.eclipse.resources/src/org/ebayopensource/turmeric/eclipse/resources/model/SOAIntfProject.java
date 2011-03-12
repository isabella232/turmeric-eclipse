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

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;


/**
 * @author smathew
 * Intf Project model
 */
public class SOAIntfProject extends SOABaseProject {

	/**
	 * An array of source directories.
	 */
	public static final String[] SOURCE_DIRECTORIES = {
			SOAProjectConstants.FOLDER_SRC,
			SOAProjectConstants.FOLDER_GEN_SRC_CLIENT,
			SOAProjectConstants.FOLDER_GEN_META_SRC,
			SOAProjectConstants.FOLDER_META_SRC };

	/**
	 * The location of the generated client configuration.
	 */
	public static final String GEN_META_SRC_CLIENT_CONFIG = SOAProjectConstants.GEN_META_SRC_META_INF
			+ "/soa/client/config";
	
	/**
	 * The location of the SOA common configuration.
	 */
	public static final String FOLDER_SOA_COMMON_CONFIG = "/soa/common/config";
	
	/**
	 * The location of the generated common configuration.
	 */
	public static final String GEN_META_SRC_COMMON_CONFIG = SOAProjectConstants.GEN_META_SRC_META_INF
			+ FOLDER_SOA_COMMON_CONFIG;


	/**
	 * The location of the WSDL.
	 */
	public static final String META_SRC_WSDL = SOAProjectConstants.META_SRC_META_INF
			+ "/soa/services/wsdl";
	
	/**
	 * The location of the common configuration.
	 */
	public static final String META_SRC_COMMON_CONFIG = SOAProjectConstants.META_SRC_META_INF
			+ FOLDER_SOA_COMMON_CONFIG;

	/**
	 * Creates an instance of a SOA interface project.
	 * @param metadata the SOAIntfMetadata
	 * @param soaEclipseMetadata the Eclipse metadata
	 * @return an instance of a SOAIntfProject
	 * @throws Exception 
	 */
	public static SOAIntfProject create(final SOAIntfMetadata metadata,
			SOAProjectEclipseMetadata soaEclipseMetadata) throws Exception {
		SOAIntfProject intfProject = new SOAIntfProject();
		intfProject.setMetadata(metadata);
		intfProject.setEclipseMetadata(soaEclipseMetadata);		
		return intfProject;
	}

	@Override
	public SOAIntfMetadata getMetadata() {
		final AbstractSOAMetadata metadata = super.getMetadata();
		return metadata instanceof SOAIntfMetadata ? (SOAIntfMetadata)metadata : null;
	}

	@Override
	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return SOAIntfMetadata.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getSourceSubFolders() {
		List<String> subFolders = new ArrayList<String>();
		//subFolders.add(GEN_META_SRC_CLIENT_CONFIG);
		subFolders
				.add(GEN_META_SRC_COMMON_CONFIG + WorkspaceUtil.PATH_SEPERATOR
						+ getMetadata().getServiceName());
		subFolders.add(GEN_META_SRC_COMMON_CONFIG + WorkspaceUtil.PATH_SEPERATOR
				+ getMetadata().getServiceName());
		subFolders.add(META_SRC_WSDL + WorkspaceUtil.PATH_SEPERATOR
				+ getMetadata().getServiceName());
		return subFolders;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getSourceDirectoryNames() {
		return ListUtil.arrayList(SOURCE_DIRECTORIES);
	}
}
