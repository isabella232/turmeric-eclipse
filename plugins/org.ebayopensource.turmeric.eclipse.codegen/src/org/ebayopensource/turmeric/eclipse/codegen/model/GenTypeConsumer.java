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

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * The Class GenTypeConsumer.
 *
 * @author yayu
 */
public class GenTypeConsumer extends ConsumerCodeGenModel implements IMultiCodeGenModel {
	private String defaultEnvironmentName;
	private String serviceLocation; //-sl
	private String envMapper;
	
	/**
	 * Instantiates a new gen type consumer.
	 */
	public GenTypeConsumer() {
		super();
		this.setGenType(GENTYPE_CONSUMER);
	}

	/**
	 * Instantiates a new gen type consumer.
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
	 * @param genFolder the gen folder
	 * @param serviceLocation the service location
	 */
	public GenTypeConsumer(String namespace,
			String serviceLayerFile, String serviceInterface,
			String serviceName, String serviceVersion, String serviceImpl,
			String projectRoot, String serviceLayer, String sourceDirectory,
			String destination, String outputDirectory, String genFolder, 
			String serviceLocation) {
		super(GENTYPE_CONSUMER, namespace, serviceLayerFile, serviceInterface,
				serviceName, serviceVersion, serviceImpl, projectRoot,
				serviceLayer, sourceDirectory, destination, outputDirectory);
		super.setGenFolder(genFolder);
		this.serviceLocation = serviceLocation; 
	}

	/**
	 * Gets the default environment name.
	 *
	 * @return the default environment name
	 */
	public String getDefaultEnvironmentName() {
		return defaultEnvironmentName;
	}

	/**
	 * Sets the default environment name.
	 *
	 * @param defaultEnvironmentName the new default environment name
	 */
	public void setDefaultEnvironmentName(String defaultEnvironmentName) {
		this.defaultEnvironmentName = defaultEnvironmentName;
	}

	/**
	 * Gets the service location.
	 *
	 * @return the service location
	 */
	public String getServiceLocation() {
		return serviceLocation;
	}

	/**
	 * Sets the service location.
	 *
	 * @param serviceLocation the new service location
	 */
	public void setServiceLocation(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}

	/**
	 * Gets the env mapper.
	 *
	 * @return the env mapper
	 */
	public String getEnvMapper() {
		return envMapper;
	}

	/**
	 * Sets the env mapper.
	 *
	 * @param envMapper the new env mapper
	 */
	public void setEnvMapper(String envMapper) {
		this.envMapper = envMapper;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		Map<String, String> result = super.getCodeGenOptions();
		if (this.serviceLocation != null)
			result.put(PARAM_SL, this.serviceLocation);
//		result.remove(PARAM_BIN);
		if (this.defaultEnvironmentName != null)
			result.put(PARAM_ENVIRONMENT, this.defaultEnvironmentName);
		if (StringUtils.isNotBlank(this.envMapper))
			result.put(PARAM_ENV_MAPPER, this.envMapper);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IMultiCodeGenModelIterator iterator() {
		return new IMultiCodeGenModelIterator() {
			private Iterator<String> it = getRequiredServices().keySet().iterator();
			public boolean hasNext() {
				return it.hasNext();
			}

			public Map<String, String> nextInputOptions() {
				if (hasNext() == false) {
					throw new IllegalArgumentException("all options have ben processed");
				}
				final String serviceName = it.next();
				Map<String, String> paramModel = getCodeGenOptions();
				paramModel.putAll(getRequiredServices().get(serviceName));
				return paramModel;
			}
			
		};
	}
}
