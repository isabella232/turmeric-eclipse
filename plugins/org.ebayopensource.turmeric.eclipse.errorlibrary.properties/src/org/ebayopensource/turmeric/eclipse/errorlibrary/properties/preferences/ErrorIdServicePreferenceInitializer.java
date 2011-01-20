/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.osgi.framework.Bundle;

/**
 * Class used to initialize default preference values.
 */
public class ErrorIdServicePreferenceInitializer extends
		AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(ErrorIdServicePreferenceConstants.USELOCALHOST, true);
		store.setDefault(ErrorIdServicePreferenceConstants.USEREMOTEHOST, false);
		store.setDefault(ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL,
				"http://www.example.org/AdminASV1");
		store.setDefault(ErrorIdServicePreferenceConstants.LOCALFILEPATH,
				getTurmericConfFolderName());
	}

	public static String getErrorIdServiceEndpoint() {
		return Activator.getDefault().getPreferenceStore()
				.getString(ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL);
	}

	public static boolean useLocalHost() {
		String currentSelection = Activator.getDefault().getPreferenceStore()
				.getString(ErrorIdServicePreferenceConstants.HOSTSELECTION);
		return ErrorIdServicePreferenceConstants.USELOCALHOST
				.equals(currentSelection)
				|| currentSelection == null
				|| currentSelection.trim().length() == 0;
	}
	
	public static String getLocalErrorDataPath() {
		return Activator.getDefault().getPreferenceStore()
				.getString(ErrorIdServicePreferenceConstants.LOCALFILEPATH);
	}
	
	private String getTurmericConfFolderName() {
		String path = new StringBuilder().append(System.getProperty("user.home")).append(File.separator).append(".turmeric").toString();
		File fPath = new File(path); 
		if (!fPath.exists()) {
			try {
				fPath.mkdir();
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
		}
		File errorDataFile = new File (path+File.separator+"TurmericErrorIDs.xml");
		if (!errorDataFile.exists()) {
			Bundle bundle = Activator.getDefault().getBundle();
			URL templateURL = FileLocator.find(bundle, new Path("templates/TurmericErrorIDs.xml"), null);
			InputStream input = null;
			OutputStream output = null;
			try {
				input = templateURL.openStream();
				output = new FileOutputStream(errorDataFile);
				IOUtils.copy(input, output);
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		}
		return path;
	}
}
