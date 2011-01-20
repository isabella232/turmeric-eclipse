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

import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.core.SOAErrorIdGeneratorAdapter;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.SOAErrorLibraryConstants;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
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
	 * 
	 */
	public ErrorParamModel() {
		super();
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.resources.ui.model.ISOAServiceParamModel#validate()
	 */
	public boolean validate() {
		return true;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	public String getErrorID() {
		return errorID;
	}

	public void setErrorID(String errorID) {
		this.errorID = errorID;
	}

	public long getNID() {
		return nID;
	}

	public void setNID(long nid) {
		nID = nid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorLibrary() {
		return errorLibrary;
	}

	public void setErrorLibrary(String errorLibrary) {
		this.errorLibrary = errorLibrary;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("ErrorID: ");
		buf.append(this.errorID);
		return buf.toString();
	}

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
