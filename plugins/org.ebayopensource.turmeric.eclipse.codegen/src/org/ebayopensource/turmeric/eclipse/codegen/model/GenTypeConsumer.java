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
 * @author yayu
 *
 */
public class GenTypeConsumer extends ConsumerCodeGenModel implements IMultiCodeGenModel {
	private String defaultEnvironmentName;
	private String serviceLocation; //-sl
	private String envMapper;
	
	/**
	 * 
	 */
	public GenTypeConsumer() {
		super();
		this.setGenType(GENTYPE_CONSUMER);
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
	 * @param genFolder
	 * @param clientName
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

	public String getDefaultEnvironmentName() {
		return defaultEnvironmentName;
	}

	public void setDefaultEnvironmentName(String defaultEnvironmentName) {
		this.defaultEnvironmentName = defaultEnvironmentName;
	}

	public String getServiceLocation() {
		return serviceLocation;
	}

	public void setServiceLocation(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}

	public String getEnvMapper() {
		return envMapper;
	}

	public void setEnvMapper(String envMapper) {
		this.envMapper = envMapper;
	}

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
