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
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;


/**
 * The Class WSDLValidator.
 *
 * @author yayu
 */
public class WSDLValidator extends AbstractSOAValidator {
	private static final WSDLValidator instance = new WSDLValidator();
		
	/**
	 * Gets the single instance of WSDLValidator.
	 *
	 * @return single instance of WSDLValidator
	 */
	public static WSDLValidator getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new wSDL validator.
	 */
	public WSDLValidator() {
		super();
	}
	
	/**
	 * Gets the wSDLURL.
	 *
	 * @param wsdl the wsdl
	 * @return the wSDLURL
	 */
	protected String getWSDLURL(Object wsdl) {
		String wsdlUrl = null;
		if (wsdl instanceof String) {
			wsdlUrl = wsdl.toString();
		} else if (wsdl instanceof URL) {
			wsdlUrl = ((URL) wsdl).toString();
		} else if (wsdl instanceof URI) {
			wsdlUrl = ((URI) wsdl).toString();
		} else if (wsdl instanceof File) {
			wsdlUrl = ((File) wsdl).getPath();
		} else if (wsdl instanceof IPath) {
			wsdlUrl = ((IPath) wsdl).toString();
		} else if (wsdl instanceof IFile) {
			wsdlUrl = ((IFile)wsdl).getLocationURI().toString();
		}
		
		return wsdlUrl;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator#validate(java.lang.Object)
	 */
	@SuppressWarnings("restriction")
	@Override
	public IStatus validate(Object wsdl) throws ValidationInterruptedException {
		IStatus status = super.validate(wsdl);
		if (status.isOK() == false)
			return status;
		
		IResource wsdlResource = null;
		final String wsdlUrl = getWSDLURL(wsdl);
		if (wsdl instanceof IFile) {
			wsdlResource = (IFile)wsdl;
		}
		
		//

		if (wsdlUrl != null) {
			final MultiStatus mStatus = (MultiStatus)
			EclipseMessageUtils.createEmptyOKMultiStatus("WSDL Validation");
			status = mStatus;
			if (!IOUtil.validateURL(wsdlUrl)) {
				mStatus.add(getBasicStatusModel("Invalid WSDL specified->" + wsdlUrl));
			}

			IValidationMessage[] validationMessages = org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator
			.getInstance().validate(wsdlUrl).getValidationMessages();

			if (validationMessages != null && validationMessages.length >= 0) {
				for (IValidationMessage validationMessage : validationMessages) {
					if (validationMessage.getSeverity() == IValidationMessage.SEV_ERROR) {
						//we will always use warning level
						final IStatus msg;
						if (wsdlResource != null) {
							msg = EclipseMessageUtils.createSOAResourceWarnStatus(wsdlResource, 
									validationMessage.getMessage(), null);
						} else {
							msg = getBasicStatusModel(
									validationMessage.getMessage(), IStatus.WARNING);
						}
						mStatus.add(msg);
					}
				}
			}
		}

		return status;
	}
	
	/*public static final Version PREFERRED_WSDL_VALIDATION_VERSION = new Version(1, 1, 201);
	public static final String ERROR_MESSAGE = 
		"You are using an old version of WTP, and some of the WSDL validation errors might only be warnings. " +
		"It is highly recommended to change to version equals to or later than "
		+ PREFERRED_WSDL_VALIDATION_VERSION + ".";
	
	public static boolean isUsingOldWSDLValidationPlugin() {
		final Bundle bundle = Platform.getBundle( "org.eclipse.wst.wsdl.validation" );
		if (bundle != null) {
			final Object object = bundle.getHeaders().get(Constants.BUNDLE_VERSION);
			if (object != null) {
				final String versionID = String.valueOf(object);
				final Version version = Version.parseVersion(versionID);
				return  (PREFERRED_WSDL_VALIDATION_VERSION.compareTo(version)) >= 0;
			}
		}
		
		return false;
	}*/
}
