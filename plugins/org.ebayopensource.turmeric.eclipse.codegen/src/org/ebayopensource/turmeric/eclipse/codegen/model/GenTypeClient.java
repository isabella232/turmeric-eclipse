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
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * @author yayu
 * 
 */
public class GenTypeClient extends BaseCodeGenModel {

	private boolean generateFromWsdl = false;
	private String genInterfacePacakgeName; // -gip
	private String genInterfaceClassName; // -gin

	/**
	 * 
	 */
	public GenTypeClient() {
		super();
		setGenType(GENTYPE_CLIENT);
	}
	
	/**
	 * @param namespace
	 * @param serviceLayerFile
	 * @param serviceInterface
	 * @param serviceName
	 * @param serviceVersion
	 * @param serviceImpl
	 * @param projectRoot
	 * @param serviceLayer
	 * @param sourceDirectory
	 * @param destination
	 * @param outputDirectory
	 */
	public GenTypeClient(String genType, String namespace, String serviceLayerFile,
			String serviceInterface, String serviceName, String serviceVersion,
			String serviceImpl, String projectRoot, String serviceLayer,
			String sourceDirectory, String destination, String outputDirectory,
			boolean generateFromWsdl, String genFolder,
			String genInterfacePacakgeName, String genInterfaceClassName) {
		super(genType, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
		this.generateFromWsdl = generateFromWsdl;
		super.setGenFolder(genFolder);
		this.genInterfacePacakgeName = genInterfacePacakgeName;
		this.genInterfaceClassName = genInterfaceClassName;
	}

	/**
	 * @param namespace
	 * @param serviceLayerFile
	 * @param serviceInterface
	 * @param serviceName
	 * @param serviceVersion
	 * @param serviceImpl
	 * @param projectRoot
	 * @param serviceLayer
	 * @param sourceDirectory
	 * @param destination
	 * @param outputDirectory
	 */
	public GenTypeClient(String namespace, String serviceLayerFile,
			String serviceInterface, String serviceName, String serviceVersion,
			String serviceImpl, String projectRoot, String serviceLayer,
			String sourceDirectory, String destination, String outputDirectory,
			boolean generateFromWsdl, String genFolder,
			String genInterfacePacakgeName, String genInterfaceClassName) {
		this(GENTYPE_CLIENT, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory, 
				generateFromWsdl, genFolder, genInterfacePacakgeName, 
				genInterfaceClassName);
	}

	public boolean isGenerateFromWsdl() {
		return generateFromWsdl;
	}

	public void setGenerateFromWsdl(boolean generateFromWsdl) {
		this.generateFromWsdl = generateFromWsdl;
	}

	public String getGenInterfacePacakgeName() {
		return genInterfacePacakgeName;
	}

	public void setGenInterfacePacakgeName(String genInterfacePacakgeName) {
		this.genInterfacePacakgeName = genInterfacePacakgeName;
	}

	public String getGenInterfaceClassName() {
		return genInterfaceClassName;
	}

	public void setGenInterfaceClassName(String genInterfaceClassName) {
		this.genInterfaceClassName = genInterfaceClassName;
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = super.getCodeGenOptions();
		if (this.genInterfacePacakgeName != null)
			result.put(PARAM_GIP, this.genInterfacePacakgeName);
		if (this.genInterfaceClassName != null)
			result.put(PARAM_GIN, this.genInterfaceClassName);
		if (this.generateFromWsdl) {
			result.put(PARAM_WSDL, null);
			result.remove(PARAM_INTERFACE);
		}
		
		return result;
	}
}
