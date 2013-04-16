/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.model.services;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.core.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.ServiceImplType;

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
	 * The workspace root directory. This may be different than the users IDE
	 * workspace.
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
	 * Base consumer source directory for both implemenation and consumer
	 * projects.
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
	 * A Map of namespace to corresponding java package name mappings. Used by
	 * TypeLibs.
	 */
	Map<String, String> namespaceToPacakgeMappings = new LinkedHashMap<String, String>();

	/**
	 * The original WSDL URL location that the service was generated.
	 */
	URL originalWsdlUrl;

	/**
	 * 
	 */
	ServiceImplType serviceImplType;

	/**
	 * Protocols used by a service. separated by ,
	 */
	String serviceNonXSDProtocols;
	
	/*
	 * Indicates whether the interface project is created as part of raptor service impl
	 */
	private boolean raptorSvcImpl;
	
	/*
	 * Soa deployable unit- web project name
	 */
	
	private String webProjectName;
	/*
	 * Soa app name, defaults to lowercase web project name;
	 */
	private String appName;
	/*
	 * web project Description
	 */
	private String webProjectDesc;
	/*
	 * web project group id
	 */
	private String webProjectGroupID;
	/*
	 * web project archetype details;
	 */
	
	private String webProjectArchetypeGid;
	/*
	 * ReUseOFWebapps
	 */
	private Boolean reuse;
	public Boolean getReuse() {
		return reuse;
	}

	public void setReuse(Boolean reuse) {
		this.reuse = reuse;
	}
	private String webProjectArchetypeArtId;
	public String getWebProjectArchetypeGid() {
		return webProjectArchetypeGid;
	}

	public void setWebProjectArchetypeGid(String webProjectArchetypeGid) {
		this.webProjectArchetypeGid = webProjectArchetypeGid;
	}

	public String getWebProjectArchetypeArtId() {
		return webProjectArchetypeArtId;
	}

	public void setWebProjectArchetypeArtId(String webProjectArchetypeArtId) {
		this.webProjectArchetypeArtId = webProjectArchetypeArtId;
	}

	public String getWebProjectArchetypeVsn() {
		return webProjectArchetypeVsn;
	}

	public void setWebProjectArchetypeVsn(String webProjectArchetypeVsn) {
		this.webProjectArchetypeVsn = webProjectArchetypeVsn;
	}

	private String webProjectArchetypeVsn;

	private String domainParentVersion;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWebProjectDesc() {
		return webProjectDesc;
	}

	public void setWebProjectDesc(String webProjectDesc) {
		this.webProjectDesc = webProjectDesc;
	}

	public String getWebProjectGroupID() {
		return webProjectGroupID;
	}

	public void setWebProjectGroupID(String webProjectGroupID) {
		this.webProjectGroupID = webProjectGroupID;
	}

	public String getWebProjectName() {
		return webProjectName;
	}

	public void setWebProjectName(String webProjectName) {
		this.webProjectName = webProjectName;
	}

	/**
	 * 
	 * @return the public service name
	 */
	public String getPublicServiceName() {
		return publicServiceName;
	}

	/**
	 * Sets the public service name.
	 *
	 * @param publicServiceName the public service name
	 */
	public void setPublicServiceName(String publicServiceName) {
		this.publicServiceName = publicServiceName;
	}

	/**
	 * Gets the type namespace.
	 *
	 * @return the type library namespace
	 */
	public String getTypeNamespace() {
		return typeNamespace;
	}

	/**
	 * Sets the type namespace.
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
	 * Sets the base consumer src dir.
	 *
	 * @param baseConsumerDir the base consumer source directory.
	 */
	public void setBaseConsumerSrcDir(String baseConsumerDir) {
		this.baseConsumerSrcDir = baseConsumerDir;
	}

	/**
	 * Gets the target namespace.
	 *
	 * @return the target namespace
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * Sets the target namespace.
	 *
	 * @param targetNamespace the target namespace
	 */
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
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
	 * @param serviceName the service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the service interface.
	 *
	 * @return the service interface
	 */
	public String getServiceInterface() {
		return serviceInterface;
	}

	/**
	 * Sets the service interface.
	 *
	 * @param serviceInterface the service interface
	 */
	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	/**
	 * Checks if is override workspace root.
	 *
	 * @return whether the workspace root location is over ridden
	 */
	public boolean isOverrideWorkspaceRoot() {
		return overrideWorkspaceRoot;
	}

	/**
	 * Sets the override workspace root.
	 *
	 * @param overrideWorkspaceRoot true or false to enable/disable the workspace root override.
	 */
	public void setOverrideWorkspaceRoot(boolean overrideWorkspaceRoot) {
		this.overrideWorkspaceRoot = overrideWorkspaceRoot;
	}

	/**
	 * Gets the workspace root directory.
	 *
	 * @return the workspace root location
	 */
	public String getWorkspaceRootDirectory() {
		return workspaceRootDirectory;
	}

	/**
	 * Sets the workspace root directory.
	 *
	 * @param workspaceRootDirectory the workspace root directory
	 */
	public void setWorkspaceRootDirectory(String workspaceRootDirectory) {
		this.workspaceRootDirectory = workspaceRootDirectory;
	}

	/**
	 * Gets the impl name.
	 *
	 * @return the implementation name
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * Sets the impl name.
	 *
	 * @param implProjectName the implementation name
	 */
	public void setImplName(String implProjectName) {
		this.implName = implProjectName;
	}

	/**
	 * Gets the service impl.
	 *
	 * @return the service implementation
	 */
	public String getServiceImpl() {
		return serviceImpl;
	}

	/**
	 * Sets the service impl.
	 *
	 * @param serviceImpl the service implementation
	 */
	public void setServiceImpl(String serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	/**
	 * Gets the service version.
	 *
	 * @return the service version
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}

	/**
	 * Sets the service version.
	 *
	 * @param serviceVersion the service version
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
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
	 * @param serviceLayer the service layer
	 */
	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	/**
	 * Gets the interface libs.
	 *
	 * @return A Set of interface libraries
	 */
	public Set<String> getInterfaceLibs() {
		return interfaceLibs;
	}

	/**
	 * Sets the interface libs.
	 *
	 * @param interfaceLibs a Set of interface libraries
	 */
	public void setInterfaceLibs(Set<String> interfaceLibs) {
		this.interfaceLibs = interfaceLibs;
	}

	/**
	 * Gets the interface projects.
	 *
	 * @return a Set of interface projects
	 */
	public Set<String> getInterfaceProjects() {
		return interfaceProjects;
	}

	/**
	 * Sets the interface projects.
	 *
	 * @param interfaceProjects a set of interface projects
	 */
	public void setInterfaceProjects(Set<String> interfaceProjects) {
		this.interfaceProjects = interfaceProjects;
	}

	/**
	 * Gets the impl libs.
	 *
	 * @return a Set of implemenation libraries
	 */
	public Set<String> getImplLibs() {
		return implLibs;
	}

	/**
	 * Sets the impl libs.
	 *
	 * @param implLibs a Set of implementation libraries
	 */
	public void setImplLibs(Set<String> implLibs) {
		this.implLibs = implLibs;
	}

	/**
	 * Gets the impl projects.
	 *
	 * @return a Set of implementation projects
	 */
	public Set<String> getImplProjects() {
		return implProjects;
	}

	/**
	 * Sets the impl projects.
	 *
	 * @param implProjects a Set of implemenation projects
	 */
	public void setImplProjects(Set<String> implProjects) {
		this.implProjects = implProjects;
	}

	/**
	 * Gets the wSDL source type.
	 *
	 * @return the wsdl source type
	 */
	public SOAProjectConstants.InterfaceWsdlSourceType getWSDLSourceType() {
		return wsdlSource;
	}

	/**
	 * Sets the wSDL source type.
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
	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Gets the original wsdl url.
	 *
	 * @return URL location of the original WSDL
	 */
	public URL getOriginalWsdlUrl() {
		return originalWsdlUrl;
	}

	/**
	 * Sets the original wsdl url.
	 *
	 * @param originalWsdlUrl URL location of the original wsdl.
	 */
	public void setOriginalWsdlUrl(URL originalWsdlUrl) {
		this.originalWsdlUrl = originalWsdlUrl;
	}

	/**
	 * Gets the namespace to pacakge mappings.
	 *
	 * @return A Map contain the namespace to java package names
	 */
	public Map<String, String> getNamespaceToPacakgeMappings() {
		return namespaceToPacakgeMappings;
	}

	/**
	 * Sets the namespace to pacakge mappings.
	 *
	 * @param namespaceToPacakgeMappings A Map containing the namespace to java package names
	 */
	public void setNamespaceToPacakgeMappings(
			Map<String, String> namespaceToPacakgeMappings) {
		this.namespaceToPacakgeMappings = namespaceToPacakgeMappings;
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
	 * @param serviceDomain the service domain.
	 */
	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}

	/**
	 * Gets the namespace part.
	 *
	 * @return the namespace part
	 */
	public String getNamespacePart() {
		return namespacePart;
	}

	/**
	 * Sets the namespace part.
	 * @param namespacePart
	 *            the namespace part
	 */
	public void setNamespacePart(String namespacePart) {
		this.namespacePart = namespacePart;
	}

	/**
	 * @return
	 */
	public ServiceImplType getServiceImplType() {
		return serviceImplType;
	}

	/**
	 * @param serviceImplType
	 */
	public void setServiceImplType(ServiceImplType serviceImplType) {
		this.serviceImplType = serviceImplType;
	}

	/**
	 * 
	 * @return
	 */
	public String getServiceProtocols() {
		return serviceNonXSDProtocols;
	}

	/**
	 * 
	 * @param serviceProtocols
	 */
	public void setServiceNonXSDProtocols(String serviceProtocols) {
		this.serviceNonXSDProtocols = serviceProtocols;
	}

	public boolean isRaptorSvcImpl() {
		return raptorSvcImpl;
	}

	public void setRaptorSvcImpl(boolean raptorSvcImpl) {
		this.raptorSvcImpl = raptorSvcImpl;
	}

	public void setWebProjectDomainParentVersion(String property) {
		// TODO Auto-generated method stub
		this.domainParentVersion = property;
	}

	public String getDomainParentVersion() {
		return domainParentVersion;
	}

}
