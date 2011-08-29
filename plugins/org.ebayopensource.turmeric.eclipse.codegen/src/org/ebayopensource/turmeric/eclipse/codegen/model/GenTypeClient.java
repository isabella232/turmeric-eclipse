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

// TODO: Auto-generated Javadoc
/**
 * The Class GenTypeClient.
 *
 * @author yayu
 */
public class GenTypeClient extends BaseCodeGenModel {

	/** The generate from wsdl. */
	private boolean generateFromWsdl = false;
	
	/** The gen interface pacakge name. */
	private String genInterfacePacakgeName; // -gip
	
	/** The gen interface class name. */
	private String genInterfaceClassName; // -gin

	/**
	 * Instantiates a new gen type client.
	 */
	public GenTypeClient() {
		super();
		setGenType(GENTYPE_CLIENT);
	}
	
	/**
	 * Instantiates a new gen type client.
	 *
	 * @param genType the gen type
	 * @param namespace the namespace
	 * @param serviceLayerFile the service layer file
	 * @param serviceInterface the service interface
	 * @param serviceName the service name
	 * @param serviceVersion the service version
	 * @param serviceImpl the service impl
	 * @param projectRoot the project root
	 * @param serviceLayer the service layer
	 * @param sourceDirectory the source directory
	 * @param destination the destination
	 * @param outputDirectory the output directory
	 * @param generateFromWsdl the generate from wsdl
	 * @param genFolder the gen folder
	 * @param genInterfacePacakgeName the gen interface pacakge name
	 * @param genInterfaceClassName the gen interface class name
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
	 * Instantiates a new gen type client.
	 *
	 * @param namespace the namespace
	 * @param serviceLayerFile the service layer file
	 * @param serviceInterface the service interface
	 * @param serviceName the service name
	 * @param serviceVersion the service version
	 * @param serviceImpl the service impl
	 * @param projectRoot the project root
	 * @param serviceLayer the service layer
	 * @param sourceDirectory the source directory
	 * @param destination the destination
	 * @param outputDirectory the output directory
	 * @param generateFromWsdl the generate from wsdl
	 * @param genFolder the gen folder
	 * @param genInterfacePacakgeName the gen interface pacakge name
	 * @param genInterfaceClassName the gen interface class name
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

	/**
	 * Checks if is generate from wsdl.
	 *
	 * @return true, if is generate from wsdl
	 */
	public boolean isGenerateFromWsdl() {
		return generateFromWsdl;
	}

	/**
	 * Sets the generate from wsdl.
	 *
	 * @param generateFromWsdl the new generate from wsdl
	 */
	public void setGenerateFromWsdl(boolean generateFromWsdl) {
		this.generateFromWsdl = generateFromWsdl;
	}

	/**
	 * Gets the gen interface pacakge name.
	 *
	 * @return the gen interface pacakge name
	 */
	public String getGenInterfacePacakgeName() {
		return genInterfacePacakgeName;
	}

	/**
	 * Sets the gen interface pacakge name.
	 *
	 * @param genInterfacePacakgeName the new gen interface pacakge name
	 */
	public void setGenInterfacePacakgeName(String genInterfacePacakgeName) {
		this.genInterfacePacakgeName = genInterfacePacakgeName;
	}

	/**
	 * Gets the gen interface class name.
	 *
	 * @return the gen interface class name
	 */
	public String getGenInterfaceClassName() {
		return genInterfaceClassName;
	}

	/**
	 * Sets the gen interface class name.
	 *
	 * @param genInterfaceClassName the new gen interface class name
	 */
	public void setGenInterfaceClassName(String genInterfaceClassName) {
		this.genInterfaceClassName = genInterfaceClassName;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
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
