/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.codegen.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientEnvironment;


/**
 * Simple Model class for type mappings generation. Gentype used is
 * "ClientConfig".
 * 
 * @author smathew
 * 
 */
public class GenTypeClientConfig extends ConsumerCodeGenModel implements
		IMultiCodeGenModel {
	private String metadataDirectory;
	private List<String> environments = new ArrayList<String>();

	public GenTypeClientConfig() {
		super();
		setGenType(GENTYPE_CLIENT_CONFIG);
	}

	public String getMetadataDirectory() {
		return metadataDirectory;
	}

	public void setMetadataDirectory(String metadataDirectory) {
		this.metadataDirectory = metadataDirectory;
	}

	public List<String> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	@Override
	public Map<String, String> getCodeGenOptions() {
		Map<String, String> result = super.getCodeGenOptions();
		if (this.metadataDirectory != null)
			result.put(PARAM_MDEST, this.metadataDirectory);
		return result;
	}

	public IMultiCodeGenModelIterator iterator() {
		final List<SOAClientEnvironment> envs = new ArrayList<SOAClientEnvironment>();
		for (String env : environments) {
			for(String svc : getRequiredServices().keySet()) {
				envs.add(new SOAClientEnvironment(env, svc));
			}
		}
		return new IMultiCodeGenModelIterator() {
			private Iterator<SOAClientEnvironment> it = envs.iterator();

			public boolean hasNext() {
				return it.hasNext();
			}

			public Map<String, String> nextInputOptions() {
				if (hasNext() == false) {
					throw new IllegalArgumentException(
							SOAMessages.OPTIONS_PROCESSED);
				}
				final SOAClientEnvironment env = it.next();
				final String envName = env.getEnvironment();
				final String serviceName = env.getServiceName();
				Map<String, String> paramModel = getCodeGenOptions();
				paramModel.putAll(getRequiredServices().get(serviceName));
				paramModel.put(PARAM_ENVIRONMENT, envName);
				return paramModel;
			}
		};
	}
}
