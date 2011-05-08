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
package org.ebayopensource.turmeric.eclipse.services.search;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.RepositoryUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.services.search.SOASearchResult.SOASearchResultService;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.Match;


/**
 * The Class SOASearchQuery.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOASearchQuery implements ISearchQuery{
	private SOASearchResult searchResult = null;
	/**
	 * False means only search projects loaded in the workspace, 
	 * or true searches the entire build system. 
	 */
	private boolean searchBuildSystem = false;
	private String serviceName;
	private String serviceLayer;
	private String serviceDomain;

	/**
	 * Instantiates a new sOA search query.
	 */
	public SOASearchQuery() {
		super();
	}

	/**
	 * Instantiates a new sOA search query.
	 *
	 * @param searchBuildSystem the search build system
	 * @param serviceName the service name
	 * @param serviceLayer the service layer
	 * @param serviceDomain the service domain
	 */
	public SOASearchQuery(boolean searchBuildSystem, String serviceName,
			String serviceLayer, String serviceDomain) {
		super();
		this.searchBuildSystem = searchBuildSystem;
		this.serviceName = serviceName;
		this.serviceLayer = serviceLayer;
		this.serviceDomain = serviceDomain;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the service layer.
	 *
	 * @return the service layer
	 */
	public String getServiceLayer() {
		return serviceLayer;
	}

	/**
	 * Sets the service layer.
	 *
	 * @param serviceLayer the new service layer
	 */
	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	/**
	 * Gets the service domain.
	 *
	 * @return the service domain
	 */
	public String getServiceDomain() {
		return serviceDomain;
	}

	/**
	 * Sets the service domain.
	 *
	 * @param serviceDomain the new service domain
	 */
	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRerun() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRunInBackground() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return "Searching for SOA Services";
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (StringUtils.isNotBlank(serviceName)) {
			buf.append("Name->");
			buf.append(serviceName);
		}
		if (StringUtils.isNotBlank(serviceLayer)) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append("Layer->");
			buf.append(serviceLayer);
		}
		if (StringUtils.isNotBlank(serviceDomain)) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append("Domain->");
			buf.append(serviceDomain);
		}
		
		return buf.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public ISearchResult getSearchResult() {
		if (searchResult == null) {
			searchResult = new SOASearchResult(this);
		}
		return searchResult;
	}

	/**
	 * Checks if is search build system.
	 *
	 * @return true, if is search build system
	 */
	public boolean isSearchBuildSystem() {
		return searchBuildSystem;
	}

	/**
	 * Sets the search build system.
	 *
	 * @param searchBuildSystem the new search build system
	 */
	public void setSearchBuildSystem(boolean searchBuildSystem) {
		this.searchBuildSystem = searchBuildSystem;
	}

	/**
	 * {@inheritDoc}
	 */
	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		monitor.beginTask("Searching for SOA Services...", IProgressMonitor.UNKNOWN);
		try {
			SOASearchResult result = (SOASearchResult)getSearchResult();
			if (searchBuildSystem == false) {
				//searching in the current workspace
				for (ProjectInfo project : RepositoryUtils
						.getInterfaceProjectsFromWorkSpace()) {
					if (matches(project)) {
						SOASearchResultService service = new SOASearchResultService(project.getName(), 
								project.getServiceLayer(), project.getVersion());
						
						if (result.getMatchCount(service) == 0) {
							result.addMatch(new Match(service, 
									Match.UNIT_LINE, 0, 0));
						}
						
					}
					monitor.worked(100);
				}
				
			} else {
				for (AssetInfo project : GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getAssetRegistry().getAllAvailableServices()) {
					if (project instanceof ProjectInfo && matches((ProjectInfo)project)) {
						result.addMatch(new Match(new SOASearchResultService(project.getName(), 
								((ProjectInfo)project).getServiceLayer(), project.getVersion()), 
								Match.UNIT_LINE, 0, 0));
					}
					monitor.worked(100);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}
	
	private boolean matches(ProjectInfo project) {
		if (StringUtils.isNotBlank(serviceName)
				&& serviceName.equals(project.getName()) == false) {
			return false;
		}
		if (StringUtils.isNotBlank(serviceLayer)
				&& serviceLayer.equals(project.getServiceLayer()) == false) {
			return false;
		}
		return true;
	}

}
