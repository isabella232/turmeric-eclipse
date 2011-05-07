/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.runtime.IStatus;
import org.osgi.framework.Version;


/**
 * The Class ServiceVersionValidator.
 *
 * @author smathew
 * Checks the version format and the validity
 */
public class ServiceVersionValidator extends AbstractSOAValidator {
	private static final ServiceVersionValidator INSTANCE = new ServiceVersionValidator();

	/** The Constant SERVICE_VERSION_SEPARATOR. */
	public static final String SERVICE_VERSION_SEPARATOR = ".";
	
	/** The Constant ERROR_MESSAGE. */
	public static final String ERROR_MESSAGE = "Service Version must be of the type x.y.z, where x, y and z are positive "
			+ "integers for major version, minor version and maintenance";
	
	/** The Constant ERROR_MAJOR_VERSION. */
	public static final String ERROR_MAJOR_VERSION = "Service major version must be greater than zero";
	
	/** The Constant MAX_DIGIT. */
	public static final int MAX_DIGIT = 3;
	
	/** The Constant ERROR_TOO_MANY_DIGITS. */
	public static final String ERROR_TOO_MANY_DIGITS = 
		"The length of major/minor/maintenance version must not exceed " 
		+ MAX_DIGIT + " digits.";
	

	/**
	 * Gets the single instance of ServiceVersionValidator.
	 *
	 * @return single instance of ServiceVersionValidator
	 */
	public static ServiceVersionValidator getInstance() {
		return INSTANCE;
	}
	
	private ServiceVersionValidator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator#validate(java.lang.Object)
	 *      This method validates the format of the service version and the
	 *      content. should be in the format x.y.z, where x,y,z are non negative
	 *      integers. Also major, minor and maintenance version must be less than or equal to
	 *      three digits.
	 */
	@Override
	public IStatus validate(Object stringVersion) throws ValidationInterruptedException {
		IStatus status = super.validate(stringVersion);
		if (status.isOK()) {
			if (stringVersion instanceof String) {
				String versionString = (String) stringVersion;
				if (StringUtils.isBlank(versionString)) {
					return getBasicStatusModel("Service Version cannot be blank.");
				}

				try {
					new Version(versionString);
				} catch (IllegalArgumentException e) {
					return getBasicStatusModel(ERROR_MESSAGE);
				}
				final String[] version = StringUtils.split(versionString,
						SERVICE_VERSION_SEPARATOR);
				if (version == null || version.length != 3) {
					return getBasicStatusModel(ERROR_MESSAGE);
				}
				try {
					final int serviceVersionMajor = Integer
							.parseInt(version[0]);
					final int serviceVersionMinor = Integer
							.parseInt(version[1]);
					final int serviceVersionBugFix = Integer
							.parseInt(version[2]);
					if (serviceVersionMajor < 0 || serviceVersionMinor < 0
							|| serviceVersionBugFix < 0) {
						return getBasicStatusModel(ERROR_MESSAGE);
					}
					
					if (serviceVersionMajor <= 0) {
						return getBasicStatusModel(ERROR_MAJOR_VERSION);
					}
					
					//we only need to trim the zero for the major version, no need for the other two
					if (String.valueOf(serviceVersionMajor).length() > MAX_DIGIT
							|| version[1].length() > MAX_DIGIT
							|| version[2].length() > MAX_DIGIT) {
						return getBasicStatusModel(ERROR_TOO_MANY_DIGITS);
					}
				} catch (final NumberFormatException nfe) {
					return getBasicStatusModel(ERROR_MESSAGE);
				}
			}
		}
		return status;

	}

}
