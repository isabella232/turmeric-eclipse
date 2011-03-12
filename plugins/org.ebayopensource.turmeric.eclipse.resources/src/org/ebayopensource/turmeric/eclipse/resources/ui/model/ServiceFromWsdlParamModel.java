/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.ui.model;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;


/**
 * UI Model for service from WSDL Flow. This is a pure java object that
 * undergoes multiple transformation or is the root class for all service from
 * WSDL operations. Receives the user input, stores it and dies off after
 * getting converted to the domain project model object. Does not live until the
 * final code generation model is generated. It can be thought of as the first
 * input for the data process pipeline.
 * 
 * @author smathew
 * 
 */

public class ServiceFromWsdlParamModel extends BaseServiceParamModel {

	/**
	 * The public service name.
	 */
	String publicServiceName;
	
	/**
	 * The target namespace for the service.
	 */
	String targetNamespace;
	
	/**
	 * The service name.
	 */
	String serviceName;
	
	/**
	 * The service interface class.
	 */
	String serviceInterface;
	
	/**
	 * Whether to over ride the Workspace Root directory.
	 */
	boolean overrideWorkspaceRoot;
	
	/**
	 * The workspace root directory. This may be different than the users IDE workspace.
	 */
	String workspaceRootDirectory;
	
	/**
	 * Implementation class name.
	 */
	String implName;
	
	/**
	 * The service implemenation name.
	 */
	String serviceImpl;
	
	/**
	 * the service version in major.minor.rev format.
	 */
	String serviceVersion;
	
	/**
	 * Whether type folding is enabled or disabled for the service.
	 */
	boolean typeFolding;
	
	/**
	 * The namespace for the common types or specific types.
	 */
	String typeNamespace;
	
	/**
	 * Base consumer source directory for both implemenation and consumer projects.
	 */
	String baseConsumerSrcDir;
	
	/**
	 * The service layer which the service resides.
	 */
	String serviceLayer;
	
	/**
	 * Interface libraries.
	 */
	Set<String> interfaceLibs = new HashSet<String>();
	
	/**
	 * Interface Projects.
	 */
	Set<String> interfaceProjects = new HashSet<String>();
	
	/**
	 * Implementation Libraries.
	 */
	Set<String> implLibs = new HashSet<String>();
	
	/**
	 * Implementation Projects.
	 */
	Set<String> implProjects = new HashSet<String>();
	
	/**
	 * Service Domain.
	 */
	String serviceDomain;
	
	/**
	 * Namespace Part.
	 */
	String namespacePart;
	
	/**
	 * Wsdl Interface Source Type.
	 */
	SOAProjectConstants.InterfaceWsdlSourceType wsdlSource;
	
	/**
	 * A Map of namespace to corresponding java package name mappings.  Used by TypeLibs. 
	 */
	Map<String, String> namespaceToPacakgeMappings = new LinkedHashMap<String, String>();

	/**
	 * The original WSDL URL location that the service was generated.
	 */
	URL originalWsdlUrl;

	/**
	 * 
	 * @return the public service name
	 */
	public String getPublicServiceName() {
		return publicServiceName;
	}

	/**
	 * 
	 * @param publicServiceName the public service name
	 */
	public void setPublicServiceName(String publicServiceName) {
		this.publicServiceName = publicServiceName;
	}

	/**
	 * 
	 * @return the type library namespace
	 */
	public String getTypeNamespace() {
		return typeNamespace;
	}

	/**
	 * 
	 * @param typeNamespace type namespace
	 */
	public void setTypeNamespace(String typeNamespace) {
		this.typeNamespace = typeNamespace;
	}
	
	/**
	 * Probably should rename this to isTypeFoldingEnabled().
	 * 
	 * @return is folding enabled or not. 
	 */
	public boolean getTypeFolding() {
		return typeFolding;
	}

	/**
	 * Sets the status of type folding.  Probably better refactoring enableTypeFolding() disableTypeFolding().
	 * @param typeFolding true or false if type folding should be enabled or not.
	 */
	public void setTypeFolding(boolean typeFolding) {
		this.typeFolding = typeFolding;
	}

	/**
	 * Base Consumer Source Directory.
	 * @return the base consumer source directory
	 */
	public String getBaseConsumerSrcDir() {
		return baseConsumerSrcDir;
	}

	/**
	 * 
	 * @param baseConsumerDir the base consumer source directory.
	 */
	public void setBaseConsumerSrcDir(String baseConsumerDir) {
		this.baseConsumerSrcDir = baseConsumerDir;
	}

	/**
	 * 
	 * @return the target namespace
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * 
	 * @param targetNamespace the target namespace
	 */
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	/**
	 * 
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * @param serviceName the service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * @return the service interface
	 */
	public String getServiceInterface() {
		return serviceInterface;
	}

	/**
	 * 
	 * @param serviceInterface the service interface
	 */
	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	/**
	 * 
	 * @return whether the workspace root location is over ridden
	 */
	public boolean isOverrideWorkspaceRoot() {
		return overrideWorkspaceRoot;
	}

	/**
	 * 
	 * @param overrideWorkspaceRoot true or false to enable/disable the workspace root override.
	 */
	public void setOverrideWorkspaceRoot(boolean overrideWorkspaceRoot) {
		this.overrideWorkspaceRoot = overrideWorkspaceRoot;
	}

	/**
	 * 
	 * @return the workspace root location
	 */
	public String getWorkspaceRootDirectory() {
		return workspaceRootDirectory;
	}

	/**
	 * 
	 * @param workspaceRootDirectory the workspace root directory
	 */
	public void setWorkspaceRootDirectory(String workspaceRootDirectory) {
		this.workspaceRootDirectory = workspaceRootDirectory;
	}

	/**
	 * 
	 * @return the implementation name
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * 
	 * @param implProjectName the implementation name
	 */
	public void setImplName(String implProjectName) {
		this.implName = implProjectName;
	}

	/**
	 * 
	 * @return the service implementation
	 */
	public String getServiceImpl() {
		return serviceImpl;
	}

	/**
	 * 
	 * @param serviceImpl the service implementation
	 */
	public void setServiceImpl(String serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	/**
	 * 
	 * @return the service version
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}

	/**
	 * 
	 * @param serviceVersion the service version
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	/**
	 * 
	 * @return the service layer
	 */
	public String getServiceLayer() {
		return serviceLayer;
	}

	/**
	 * 
	 * @param serviceLayer the service layer
	 */
	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	/**
	 * 
	 * @return A Set of interface libraries
	 */
	public Set<String> getInterfaceLibs() {
		return interfaceLibs;
	}

	/**
	 * 
	 * @param interfaceLibs a Set of interface libraries
	 */
	public void setInterfaceLibs(Set<String> interfaceLibs) {
		this.interfaceLibs = interfaceLibs;
	}

	/**
	 * 
	 * @return a Set of interface projects
	 */
	public Set<String> getInterfaceProjects() {
		return interfaceProjects;
	}

	/**
	 * 
	 * @param interfaceProjects a set of interface projects
	 */
	public void setInterfaceProjects(Set<String> interfaceProjects) {
		this.interfaceProjects = interfaceProjects;
	}

	/**
	 * 
	 * @return a Set of implemenation libraries
	 * 
	 */
	public Set<String> getImplLibs() {
		return implLibs;
	}

	/**
	 * 
	 * @param implLibs a Set of implementation libraries
	 */
	public void setImplLibs(Set<String> implLibs) {
		this.implLibs = implLibs;
	}

	/**
	 * 
	 * @return a Set of implementation projects
	 */
	public Set<String> getImplProjects() {
		return implProjects;
	}

	/**
	 * 
	 * @param implProjects a Set of implemenation projects
	 */
	public void setImplProjects(Set<String> implProjects) {
		this.implProjects = implProjects;
	}

	/**
	 * 
	 * @return the wsdl source type
	 */
	public SOAProjectConstants.InterfaceWsdlSourceType getWSDLSourceType() {
		return wsdlSource;
	}

	/**
	 * 
	 * @param sourceType the wsdl source type
	 */
	public void setWSDLSourceType(
			SOAProjectConstants.InterfaceWsdlSourceType sourceType) {
		wsdlSource = sourceType;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean validate() {
		return true;
	}

	/**
	 * 
	 * @return URL location of the original WSDL
	 */
	public URL getOriginalWsdlUrl() {
		return originalWsdlUrl;
	}

	/**
	 * 
	 * @param originalWsdlUrl URL location of the original wsdl.
	 */
	public void setOriginalWsdlUrl(URL originalWsdlUrl) {
		this.originalWsdlUrl = originalWsdlUrl;
	}

	/**
	 * 
	 * @return A Map contain the namespace to java package names
	 */
	public Map<String, String> getNamespaceToPacakgeMappings() {
		return namespaceToPacakgeMappings;
	}

	/**
	 * 
	 * @param namespaceToPacakgeMappings A Map containing the namespace to java package names
	 */
	public void setNamespaceToPacakgeMappings(
			Map<String, String> namespaceToPacakgeMappings) {
		this.namespaceToPacakgeMappings = namespaceToPacakgeMappings;
	}

	/**
	 * 
	 * @return the service domain
	 */
	public String getServiceDomain() {
		return serviceDomain;
	}

	/**
	 * @param serviceDomain the service domain.
	 */
	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}

	/**
	 * 
	 * @return the namespace part
	 */
	public String getNamespacePart() {
		return namespacePart;
	}

	/**
	 * 
	 * @param namespacePart the namespace part
	 */
	public void setNamespacePart(String namespacePart) {
		this.namespacePart = namespacePart;
	}
}
