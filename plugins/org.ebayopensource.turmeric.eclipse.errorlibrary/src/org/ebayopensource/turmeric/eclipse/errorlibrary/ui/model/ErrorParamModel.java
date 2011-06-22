/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.core.SOAErrorIdGeneratorAdapter;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.SOAErrorLibraryConstants;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
<<<<<<< HEAD
 * The Class ErrorParamModel.
 *
=======
>>>>>>> TURMERIC-1351
 * @author yayu
 * @since 1.0.0
 */
public class ErrorParamModel extends BaseServiceParamModel {

	private long nID = 2000;
	private String errorID = String.valueOf(nID);
	private String domain;
	private String category = SOAErrorLibraryConstants.DEfAULT_CATEGORY;
	private String name = SOAErrorLibraryConstants.DEfAULT_NAME;
	private String organization = SOAErrorLibraryConstants.DEFAULT_ORGANIZATION;
	private String severity = SOAErrorLibraryConstants.DEFAULT_SEVERITY;
	private String message = SOAErrorLibraryConstants.DEFAULT_MSG;
	private String resolution = "";
	private String errorLibrary;
	private String subdomain = SOAErrorLibraryConstants.DEFAULT_SUBDOMAIN;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new error param model.
	 */
	public ErrorParamModel() {
		super();
	}

	/**
	 * Generate error id.
	 *
	 * @param storeLocation the store location
	 * @param organizationName the organization name
	 * @param domain the domain
	 * @return the long
	 * @throws Exception the exception
	 */
	public static long generateErrorID(String storeLocation,
			String organizationName, String domain) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			SOAErrorIdGeneratorAdapter runnable = new SOAErrorIdGeneratorAdapter(
					storeLocation, organizationName, domain);
			if (Display.getCurrent() == null) {
				// non-UI thread
				runnable.run(ProgressUtil.getDefaultMonitor(null));
			} else {
				final IProgressService service = PlatformUI.getWorkbench()
						.getProgressService();
				service.run(false, false, runnable);
			}
			return runnable.getErrorId();
		} finally {
			if (SOALogger.DEBUG) {
				long duration = System.currentTimeMillis() - startTime;
				logger.info("Time taken for getting error id is ", duration,
						" ms.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This currently always returns true.
	 * 
	 */
	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * Sets the resolution.
	 *
	 * @param resolution the new resolution
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * Gets the subdomain.
	 *
	 * @return the subdomain
	 */
	public String getSubdomain() {
		return subdomain;
	}

	/**
	 * Sets the subdomain.
	 *
	 * @param subdomain the new subdomain
	 */
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	/**
	 * Gets the error id.
	 *
	 * @return the error id
	 */
	public String getErrorID() {
		return errorID;
	}

	/**
	 * Sets the error id.
	 *
	 * @param errorID the new error id
	 */
	public void setErrorID(String errorID) {
		this.errorID = errorID;
	}

	/**
	 * Gets the nID.
	 *
	 * @return the nID
	 */
	public long getNID() {
		return nID;
	}

	/**
	 * Sets the nID.
	 *
	 * @param nid the new nID
	 */
	public void setNID(long nid) {
		nID = nid;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the organization.
	 *
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * Sets the organization.
	 *
	 * @param organization the new organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Gets the severity.
	 *
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * Sets the severity.
	 *
	 * @param severity the new severity
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the error library.
	 *
	 * @return the error library
	 */
	public String getErrorLibrary() {
		return errorLibrary;
	}

	/**
	 * Sets the error library.
	 *
	 * @param errorLibrary the new error library
	 */
	public void setErrorLibrary(String errorLibrary) {
		this.errorLibrary = errorLibrary;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("ErrorID: ");
		buf.append(this.errorID);
		return buf.toString();
	}

	/**
	 * Gets the free marker data.
	 *
	 * @return the free marker data
	 */
	public Map<String, String> getFreeMarkerData() {
		final Map<String, String> data = new ConcurrentHashMap<String, String>();
		data.put(SOAErrorLibraryConstants.ID, errorID);
		data.put(SOAErrorLibraryConstants.ERR_PROP_NID, "" + nID);
		data.put(SOAErrorLibraryConstants.ERR_PROP_CATEGORY, category);
		data.put(SOAErrorLibraryConstants.ERR_PROP_NAME, name);
		data.put(SOAErrorLibraryConstants.ORGANIZATION, organization);
		data.put(SOAErrorLibraryConstants.ERR_PROP_SEVERITY, severity);
		data.put(SOAErrorLibraryConstants.ERR_PROP_MESSAGE, message);
		return data;
	}
}
