/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.core.wst;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils.SOAResourceStatus;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.core.internal.preferences.XMLCorePreferenceNames;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationReport;
import org.eclipse.wst.xsd.core.internal.validation.XSDValidationConfiguration;
import org.eclipse.wst.xsd.core.internal.validation.eclipse.XSDValidator;


/**
 * @author smathew
 * 
 */
public class SOAXSDValidator extends AbstractSOAValidator {

	@SuppressWarnings("restriction")
	@Override
	public IStatus validate(Object obj) throws ValidationInterruptedException {

		IStatus status = super.validate(obj);
		if (obj instanceof IFile) {
			final IFile file = (IFile)obj;
			XSDValidator xsdValidator = XSDValidator.getInstance();
			try {
				XSDValidationConfiguration config = new XSDValidationConfiguration();
				boolean honourAllSchemaLocations = Platform.getPreferencesService().getBoolean(
						XMLCorePlugin.getDefault().getBundle().getSymbolicName(), 
						XMLCorePreferenceNames.HONOUR_ALL_SCHEMA_LOCATIONS, 
						Boolean.FALSE, null);
				config.setFeature(XSDValidationConfiguration.HONOUR_ALL_SCHEMA_LOCATIONS, honourAllSchemaLocations);
				ValidationReport validationReport = xsdValidator
						.validate(file.getLocationURI().toString(), file.getContents(), config);
				ValidationMessage[] validationMessages = validationReport
						.getValidationMessages();

				if (validationMessages != null && validationMessages.length >= 0) {
					status = EclipseMessageUtils
							.createEmptyErrorMultiStatus("XSD Validation");
					for (ValidationMessage validationMessage : validationMessages) {
						SOAResourceStatus resourceStatus = EclipseMessageUtils
								.createSOAResourceErrorStatus(((IFile) obj),
										validationMessage.getMessage(), null);
						((MultiStatus) status).add(resourceStatus);

					}
				}
			} catch (Exception e) {
				throw new ValidationInterruptedException(e);
			}
		}
		return status;
	}

	public static void main(String[] args) {

	}

}
