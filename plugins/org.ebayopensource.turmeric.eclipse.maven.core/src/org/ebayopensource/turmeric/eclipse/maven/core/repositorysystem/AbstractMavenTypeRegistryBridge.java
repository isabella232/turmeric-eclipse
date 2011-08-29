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
package org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ITypeRegistryBridge;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;


/**
 * The Class AbstractMavenTypeRegistryBridge.
 *
 * @author yayu
 */
public abstract class AbstractMavenTypeRegistryBridge implements ITypeRegistryBridge {

	/**
	 * Instantiates a new abstract maven type registry bridge.
	 */
	public AbstractMavenTypeRegistryBridge() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<AssetInfo> getAllTypeLibraries() throws Exception {
		return MavenCoreUtils.getAllTypeLibraries();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AssetInfo> getAllLatestTypeLibraries() throws Exception {
		Map<String, AssetInfo> data = new ConcurrentHashMap<String, AssetInfo>();
		for (AssetInfo asset : MavenCoreUtils.getAllTypeLibraries()) {
			AssetInfo latestAsset = data.get(asset.getName());
			if (latestAsset == null) {
				data.put(asset.getName(), asset);
			} else if(VersionUtil.compare(
					asset.getVersion(), latestAsset.getVersion()) > 0) {
				//found a newer version
				data.put(asset.getName(), asset);
			}
			
		}
		return ListUtil.arrayList(data.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createTypeLibrary(SOABaseProject soaBaseProject)
			throws Exception {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteTypeLibrary(String typeLibName) throws Exception {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean typeLibraryExists(String typeLibName) throws Exception {
		return MavenCoreUtils.isTypeLibraryExist(typeLibName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getRequiredLibrariesForTypeLibraryProject() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getActiveOrganizationProvider()
		.getDefaultDependencies(SOAProjectConstants.SupportedProjectType.TYPE_LIBRARY);
	}
}
