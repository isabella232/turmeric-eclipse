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

	String publicServiceName;
	String targetNamespace;
	String serviceName;
	String serviceInterface;
	boolean overrideWorkspaceRoot;
	String workspaceRootDirectory;
	String implName;
	String serviceImpl;
	String serviceVersion;
	// boolean includeValidateInternals;
	// boolean includeTestJSP;
	boolean typeFolding;
	String typeNamespace;
	String baseConsumerSrcDir; // base consumer source directory for both impl
								// and consumer projects
	String serviceLayer;
	Set<String> interfaceLibs = new HashSet<String>();
	Set<String> interfaceProjects = new HashSet<String>();
	Set<String> implLibs = new HashSet<String>();
	Set<String> implProjects = new HashSet<String>();
	String serviceDomain;
	String namespacePart;
	SOAProjectConstants.InterfaceWsdlSourceType wsdlSource;
	Map<String, String> namespaceToPacakgeMappings = new LinkedHashMap<String, String>();

	URL originalWsdlUrl;

	public String getPublicServiceName() {
		return publicServiceName;
	}

	public void setPublicServiceName(String publicServiceName) {
		this.publicServiceName = publicServiceName;
	}

	public String getTypeNamespace() {
		return typeNamespace;
	}

	public void setTypeNamespace(String typeNamespace) {
		this.typeNamespace = typeNamespace;
	}
	
	public boolean getTypeFolding() {
		return typeFolding;
	}

	public void setTypeFolding(boolean typeFolding) {
		this.typeFolding = typeFolding;
	}

	public String getBaseConsumerSrcDir() {
		return baseConsumerSrcDir;
	}

	public void setBaseConsumerSrcDir(String baseConsumerDir) {
		this.baseConsumerSrcDir = baseConsumerDir;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public boolean isOverrideWorkspaceRoot() {
		return overrideWorkspaceRoot;
	}

	public void setOverrideWorkspaceRoot(boolean overrideWorkspaceRoot) {
		this.overrideWorkspaceRoot = overrideWorkspaceRoot;
	}

	public String getWorkspaceRootDirectory() {
		return workspaceRootDirectory;
	}

	public void setWorkspaceRootDirectory(String workspaceRootDirectory) {
		this.workspaceRootDirectory = workspaceRootDirectory;
	}

	public String getImplName() {
		return implName;
	}

	public void setImplName(String implProjectName) {
		this.implName = implProjectName;
	}

	public String getServiceImpl() {
		return serviceImpl;
	}

	public void setServiceImpl(String serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	/*
	 * public boolean isIncludeValidateInternals() { return
	 * includeValidateInternals; }
	 * 
	 * public void setIncludeValidateInternals(boolean includeValidateInternals) {
	 * this.includeValidateInternals = includeValidateInternals; }
	 * 
	 * public boolean isIncludeTestJSP() { return includeTestJSP; }
	 * 
	 * public void setIncludeTestJSP(boolean includeTestJSP) {
	 * this.includeTestJSP = includeTestJSP; }
	 */

	public String getServiceLayer() {
		return serviceLayer;
	}

	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public Set<String> getInterfaceLibs() {
		return interfaceLibs;
	}

	public void setInterfaceLibs(Set<String> interfaceLibs) {
		this.interfaceLibs = interfaceLibs;
	}

	public Set<String> getInterfaceProjects() {
		return interfaceProjects;
	}

	public void setInterfaceProjects(Set<String> interfaceProjects) {
		this.interfaceProjects = interfaceProjects;
	}

	public Set<String> getImplLibs() {
		return implLibs;
	}

	public void setImplLibs(Set<String> implLibs) {
		this.implLibs = implLibs;
	}

	public Set<String> getImplProjects() {
		return implProjects;
	}

	public void setImplProjects(Set<String> implProjects) {
		this.implProjects = implProjects;
	}

	public SOAProjectConstants.InterfaceWsdlSourceType getWSDLSourceType() {
		return wsdlSource;
	}

	public void setWSDLSourceType(
			SOAProjectConstants.InterfaceWsdlSourceType sourceType) {
		wsdlSource = sourceType;
	}

	public boolean validate() {
		return true;
	}

	public URL getOriginalWsdlUrl() {
		return originalWsdlUrl;
	}

	public void setOriginalWsdlUrl(URL originalWsdlUrl) {
		this.originalWsdlUrl = originalWsdlUrl;
	}

	public Map<String, String> getNamespaceToPacakgeMappings() {
		return namespaceToPacakgeMappings;
	}

	public void setNamespaceToPacakgeMappings(
			Map<String, String> namespaceToPacakgeMappings) {
		this.namespaceToPacakgeMappings = namespaceToPacakgeMappings;
	}

	public String getServiceDomain() {
		return serviceDomain;
	}

	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}

	public String getNamespacePart() {
		return namespacePart;
	}

	public void setNamespacePart(String namespacePart) {
		this.namespacePart = namespacePart;
	}
}
