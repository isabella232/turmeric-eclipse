/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants.ProjectType;
import org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;



/**
 * @author yayu
 *
 */
public final class TurmericPluginConfigLoader {
	private static final Map<String, Model> projectPlugins 
	= new ConcurrentHashMap<String,Model>();
	
	
	public static final String PROPS_KEY_PLUGINS = ".pom.xml";
	
	public static Model getPluginConfigurations(ProjectType projType) throws IOException, MavenEclipseApiException {
		return getPluginConfigurations(projType.name().toLowerCase(Locale.US));
	}
	
	public static Model getPluginConfigurations(String projType) throws IOException, MavenEclipseApiException {
		final String key = projType.toLowerCase(Locale.US) + PROPS_KEY_PLUGINS;
		if (projectPlugins.containsKey(key) == false) {
			InputStream ins = null;
			try {
				URL url = SOAGlobalConfigAccessor.getOrganizationResource(TurmericConstants.TURMERIC_ID, 
						TurmericConstants.TURMERIC_ID, key);
				if (url != null) {
					ins = url.openStream();
					Model model = MavenApiPlugin.getDefault()
					.getMavenEclipseApi().parsePom(ins);
					if (model != null) {
						projectPlugins.put(key, model);
					}
				}
			} finally {
				IOUtils.closeQuietly(ins);
			}
		}
		return projectPlugins.get(key);
	}

	/**
	 * 
	 */
	private TurmericPluginConfigLoader() {
		super();
	}

}
