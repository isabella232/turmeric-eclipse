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
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;


// TODO: Auto-generated Javadoc
/**
 * The Class ProjectInfo.
 *
 * @author yayu
 */
public class ProjectInfo extends AssetInfo {

	/** The service layer. */
	private final String serviceLayer; //we still need this in order to show the service layer info
	
	/** The interface project name. */
	private String interfaceProjectName;
	
	/** The implementation project name. */
	private String implementationProjectName;
	
	/** The required libraries. */
	private Set<String> requiredLibraries = new TreeSet<String>();
	
	/** The required projects. */
	private Set<String> requiredProjects = new TreeSet<String>();
	
	/** The required services. */
	private Set<String> requiredServices = new TreeSet<String>();
	
	/**
	 * Instantiates a new project info.
	 *
	 * @param name the name
	 * @param version the version
	 * @param dir the dir
	 * @param serviceLayer the service layer
	 */
	public ProjectInfo(String name, String version, String dir, String serviceLayer) {
		this(name, version, dir, serviceLayer, IAssetInfo.TYPE_PROJECT);
	}
	
	/**
	 * Instantiates a new project info.
	 *
	 * @param name the name
	 * @param version the version
	 * @param dir the dir
	 */
	public ProjectInfo(String name, String version, String dir) {
		this(name, version, dir, SOAProjectConstants.ServiceLayer.UNKNOWN.toString(), 
				IAssetInfo.TYPE_PROJECT);
	}
	
	/**
	 * Instantiates a new project info.
	 *
	 * @param name the name
	 * @param version the version
	 * @param dir the dir
	 * @param serviceLayer the service layer
	 * @param type the type
	 */
	public ProjectInfo(String name, String version, String dir, 
			String serviceLayer, String type) {
		super(name, version, dir, type);
		this.serviceLayer = StringUtils.defaultString(serviceLayer).trim();
	}
	
	/**
	 * Gets the required services.
	 *
	 * @return A set of required services
	 */
	public Set<String> getRequiredServices() {
		return requiredServices;
	}

	/**
	 * Gets the interface project name.
	 *
	 * @return interface project name
	 */
	public String getInterfaceProjectName() {
		return interfaceProjectName;
	}

	/**
	 * Sets the interface project name.
	 *
	 * @param interfaceProjectName the new interface project name
	 */
	public void setInterfaceProjectName(String interfaceProjectName) {
		this.interfaceProjectName = interfaceProjectName;
	}

	/**
	 * Gets the implementation project name.
	 *
	 * @return Implementation project name
	 */
	public String getImplementationProjectName() {
		return implementationProjectName;
	}

	/**
	 * Sets the implementation project name.
	 *
	 * @param implementationProjectName the new implementation project name
	 */
	public void setImplementationProjectName(String implementationProjectName) {
		this.implementationProjectName = implementationProjectName;
	}

	/**
	 * Sets the required libraries.
	 *
	 * @param requiredLibraries the new required libraries
	 */
	public void setRequiredLibraries(Set<String> requiredLibraries) {
		this.requiredLibraries = requiredLibraries;
	}

	/**
	 * Sets the required projects.
	 *
	 * @param requiredProjects the new required projects
	 */
	public void setRequiredProjects(Set<String> requiredProjects) {
		this.requiredProjects = requiredProjects;
	}

	/**
	 * Gets the required projects.
	 *
	 * @return A Set of required projects.
	 */
	public Set<String> getRequiredProjects() {
		return requiredProjects;
	}

	/**
	 * Gets the required libraries.
	 *
	 * @return a Set of required libraries
	 */
	public Set<String> getRequiredLibraries() {
		return requiredLibraries;
	}

	/**
	 * Gets the service layer.
	 *
	 * @return Service layer
	 */
	public String getServiceLayer() {
		return serviceLayer;
	}
}
