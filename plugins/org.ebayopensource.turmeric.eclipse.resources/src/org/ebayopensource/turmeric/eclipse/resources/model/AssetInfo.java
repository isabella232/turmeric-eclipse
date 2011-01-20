/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;


/**
 * @author smathew
 * 
 * This is the wrapper class for holding the project depndencies. This has been
 * made generic to hold both project and library depndency. The type defines
 * whether its a library or project. Used mainly by the create wizards and the
 * properties windows
 * 
 */
public class AssetInfo implements IAssetInfo, Comparable<AssetInfo> {

	public static final String VERSION_PREFIX = " v.";
	private final String name;
	private final String version;
	private final String dir;
	private List<String> jarNames = new ArrayList<String>();
	// can be project or library. Enum avoided.
	private String type;

	public AssetInfo(final String name, final String type) {
		this(name, "", "", type);
	}

	public AssetInfo(final String name, final String version, final String dir) {
		this(name, version, dir, null);
	}

	public AssetInfo(final String name, final String version, final String dir,
			final String type) {
		this(name, version, dir, SOAProjectConstants.ServiceLayer.UNKNOWN
				.name(), type);
	}

	public AssetInfo(String name, String version, String dir,
			String serviceLayer, String type) {
		super();
		this.name = StringUtils.defaultString(name).trim();
		this.version = StringUtils.defaultString(version).trim();
		this.dir = StringUtils.defaultString(dir).trim();
		this.type = StringUtils.defaultString(type).trim();
	}
	
	public static AssetInfo createAssetInfoWithVersion(String name, String version, String type) {
		return new AssetInfo(name, version, "", type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ((obj instanceof AssetInfo) == false)
			return false;
		final AssetInfo other = (AssetInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public String getDir() {
		return dir;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		if (TYPE_LIBRARY.equals(getType()))
			return getName() + VERSION_PREFIX + getVersion();
		return getName();
	}

	public String toString() {
		return getName();
	}

	public int compareTo(final AssetInfo info) {
		if (ObjectUtils.equals(info, this))
			return 0;
		if (name.compareTo(info.name) != 0)
			return name.compareTo(info.name);
		if (version.compareTo(info.version) != 0)
			return version.compareTo(info.version);
		if (dir.compareTo(info.dir) != 0)
			return dir.compareTo(info.dir);
		if (type.compareTo(info.type) != 0)
			return type.compareTo(info.type);
		return 0;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getJarNames() {
		return jarNames;
	}

	public void setJarNames(List<String> jarNames) {
		this.jarNames = jarNames;
	}

	public String getUniqueID() {
		return this.name;
	}
	
	/**
	 * @return The short description of the AssetInfo instance.
	 */
	public String getShortDescription() {
		return getName();
	}

	/**
	 * @return Returns a URL based on the dir and jarnames, Will check for null
	 *         but no check on file existance if the validateFiles boolean is
	 *         false. If its true then the non existing invalid files will be
	 *         filtered out from returned list
	 */
	public Set<File> getFiles(boolean validateFiles) {
		String dirName = getDir();
		Set<File> fileSet = new HashSet<File>();
		for (String jarName : getJarNames()) {
			File file = new File(dirName, jarName);
			if (validateFiles) {
				if (file.exists() && file.canRead()) {
					fileSet.add(file);
				}

			} else {// simple add it no check here
				fileSet.add(file);
			}
		}
		return fileSet;
	}
}
