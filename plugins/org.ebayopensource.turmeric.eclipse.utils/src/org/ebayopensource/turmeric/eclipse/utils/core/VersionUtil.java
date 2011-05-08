/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.core;

import java.util.logging.Logger;

import org.osgi.framework.Version;

/**
 * Utility Class for version comparison. Wraps the OSGI functionality.
 * 
 * @author smathew
 * 
 */
public class VersionUtil {

	/**
	 * if version 1 is greater than version 2 the a positive integer is
	 * returned. is both are equal then 0 otherwise a negative value
	 *
	 * @param strVersion1 the str version1
	 * @param strVersion2 the str version2
	 * @return the int
	 * @throws NumberFormatException the number format exception
	 */
	public static int compare(String strVersion1, String strVersion2)
			throws NumberFormatException {
		Version version1 = null;
		Version version2 = null;
		try {
			version1 = new Version(strVersion1);
			version2 = new Version(strVersion2);
			if (strVersion1.contains("-T40") == true)
				return -1;
			if (strVersion2.contains("-T40") == true)
				return 1;
		} catch (NumberFormatException exception) {
			Logger.getLogger(VersionUtil.class.getName()).warning("Invalid format. Details "
					+ exception.getLocalizedMessage());
			return strVersion1.compareTo(strVersion2);
		} catch (IllegalArgumentException exception){
			Logger.getLogger(VersionUtil.class.getName()).warning("Invalid format. Details "
					+ exception.getLocalizedMessage());
			return strVersion1.compareTo(strVersion2);
		}
		return version1.compareTo(version2);
	}
	
	/**
	 * Gets the version.
	 *
	 * @param version the version
	 * @return the version
	 * @throws Exception the exception
	 */
	public static Version getVersion(Object version) throws Exception{
		Version ver = null;

		if (version instanceof Version) {
			ver = (Version) version;
		} else {
			try {
				ver = new Version(version.toString());
			} catch (IllegalArgumentException ex) {
				throw new Exception(
						"Version is not a validate version:" + version, ex);
			}
		}
		return ver;
	}
}
