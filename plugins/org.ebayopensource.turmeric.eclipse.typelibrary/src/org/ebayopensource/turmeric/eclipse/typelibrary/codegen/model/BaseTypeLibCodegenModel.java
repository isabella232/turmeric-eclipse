/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;


/**
 * This is the Base Model used for type library codegen operations. As such this
 * class will not be used except for base modeling. The subclasses are used for
 * the invoker
 * 
 * @author smathew
 * 
 */

public class BaseTypeLibCodegenModel extends BaseCodeGenModel {

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = new ConcurrentHashMap<String, String>();
		result.put(PARAM_GENTYPE, getGenType());
		if (!StringUtils.isEmpty(getLibraryName())) {
			result.put(PARAM_LIBRARYNAME, getLibraryName());
		}
		if (!StringUtils.isEmpty(getProjectRoot())) {
			result.put(PARAM_PR, getProjectRoot());
		}
		if (!StringUtils.isEmpty(getLibraryVersion())) {
			result.put(PARAM_LIBVERSION, getLibraryVersion());
		}
		if (!StringUtils.isEmpty(getLibraryCategory())) {
			result.put(PARAM_LIBCATEGORY, getLibraryCategory());
		}
		if (!StringUtils.isEmpty(getXjcClassPath())) {
			result.put(PARAM_XJCCLASSPATH, getXjcClassPath());
		}
		if (!StringUtils.isEmpty(getLibNamespace())) {
			result.put(PARAM_LIBNAMESPACE, getLibNamespace());
		}
		if (getTypes().isEmpty() == false) {
			final String types = StringUtils.join(getTypes(),
					SOAProjectConstants.DELIMITER_COMMA);
			result.put(PARAM_TYPE, types);
		}
		return result;
	}

	private static final String PARAM_LIBRARYNAME = "-libName";

	private static final String PARAM_TYPE = "-type";

	private static final String PARAM_LIBVERSION = "-libVersion";

	private static final String PARAM_LIBCATEGORY = "-libCategory";

	private static final String PARAM_LIBNAMESPACE = "-libNamespace";

	private static final String PARAM_XJCCLASSPATH = "-classPathToXJC";

	private ArrayList<String> types = new ArrayList<String>();

	private String libraryName;

	private String libraryVersion;

	private String libraryCategory;

	private String xjcClassPath;

	private String libNamespace;

	/**
	 * Gets the types.
	 *
	 * @return the types
	 */
	public ArrayList<String> getTypes() {
		return types;
	}

	/**
	 * Sets the types.
	 *
	 * @param types the new types
	 */
	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}

	/**
	 * Gets the library name.
	 *
	 * @return the library name
	 */
	public String getLibraryName() {
		return libraryName;
	}

	/**
	 * Sets the library name.
	 *
	 * @param libraryName the new library name
	 */
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	/**
	 * Gets the library version.
	 *
	 * @return the library version
	 */
	public String getLibraryVersion() {
		return libraryVersion;
	}

	/**
	 * Sets the library version.
	 *
	 * @param libraryVersion the new library version
	 */
	public void setLibraryVersion(String libraryVersion) {
		this.libraryVersion = libraryVersion;
	}

	/**
	 * Gets the library category.
	 *
	 * @return the library category
	 */
	public String getLibraryCategory() {
		return libraryCategory;
	}

	/**
	 * Sets the library category.
	 *
	 * @param libraryCategory the new library category
	 */
	public void setLibraryCategory(String libraryCategory) {
		this.libraryCategory = libraryCategory;
	}

	/**
	 * Gets the xjc class path.
	 *
	 * @return the xjc class path
	 */
	public String getXjcClassPath() {
		return xjcClassPath;
	}

	/**
	 * Sets the xjc class path.
	 *
	 * @param xjcClassPath the new xjc class path
	 */
	public void setXjcClassPath(String xjcClassPath) {
		this.xjcClassPath = xjcClassPath;
	}

	/**
	 * Gets the lib namespace.
	 *
	 * @return the lib namespace
	 */
	public String getLibNamespace() {
		return libNamespace;
	}

	/**
	 * Sets the lib namespace.
	 *
	 * @param libNamespace the new lib namespace
	 */
	public void setLibNamespace(String libNamespace) {
		this.libNamespace = libNamespace;
	}
}
