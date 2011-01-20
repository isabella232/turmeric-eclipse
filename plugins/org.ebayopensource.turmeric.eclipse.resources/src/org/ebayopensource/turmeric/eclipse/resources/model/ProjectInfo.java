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
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;


/**
 * @author yayu
 *
 */
public class ProjectInfo extends AssetInfo {

	private final String serviceLayer; //we still need this in order to show the service layer info
	private String interfaceProjectName;
	private String implementationProjectName;
	private Set<String> requiredLibraries = new TreeSet<String>();
	private Set<String> requiredProjects = new TreeSet<String>();
	private Set<String> requiredServices = new TreeSet<String>();
	
	public ProjectInfo(String name, String version, String dir, String serviceLayer) {
		this(name, version, dir, serviceLayer, AssetInfo.TYPE_PROJECT);
	}
	
	public ProjectInfo(String name, String version, String dir) {
		this(name, version, dir, SOAProjectConstants.ServiceLayer.UNKNOWN.toString(), 
				AssetInfo.TYPE_PROJECT);
	}
	
	public ProjectInfo(String name, String version, String dir, 
			String serviceLayer, String type) {
		super(name, version, dir, type);
		this.serviceLayer = StringUtils.defaultString(serviceLayer).trim();
	}
	
	public Set<String> getRequiredServices() {
		return requiredServices;
	}

	public String getInterfaceProjectName() {
		return interfaceProjectName;
	}

	public void setInterfaceProjectName(String interfaceProjectName) {
		this.interfaceProjectName = interfaceProjectName;
	}

	public String getImplementationProjectName() {
		return implementationProjectName;
	}

	public void setImplementationProjectName(String implementationProjectName) {
		this.implementationProjectName = implementationProjectName;
	}

	public void setRequiredLibraries(Set<String> requiredLibraries) {
		this.requiredLibraries = requiredLibraries;
	}

	public void setRequiredProjects(Set<String> requiredProjects) {
		this.requiredProjects = requiredProjects;
	}

	public Set<String> getRequiredProjects() {
		return requiredProjects;
	}

	public Set<String> getRequiredLibraries() {
		return requiredLibraries;
	}

	public String getServiceLayer() {
		return serviceLayer;
	}
}
