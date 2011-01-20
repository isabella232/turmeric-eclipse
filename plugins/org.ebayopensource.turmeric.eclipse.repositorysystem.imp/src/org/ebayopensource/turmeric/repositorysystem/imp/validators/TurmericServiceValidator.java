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
package org.ebayopensource.turmeric.repositorysystem.imp.validators;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.ebayopensource.turmeric.repositorysystem.imp.resources.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


/**
 * @author yayu
 *
 */
public class TurmericServiceValidator extends AbstractSOAValidator {

	/**
	 * 
	 */
	public TurmericServiceValidator() {
		super();
	}

	public IStatus validate(Object obj) throws ValidationInterruptedException {
		IStatus status = super.validate(obj);
		if (status.isOK()) {
			try {
				if (obj instanceof String) {
					status = validateInputs((String) obj);
				} else if (obj instanceof String[]) {
					status = validateInputs((String[]) obj);
				} else if (obj instanceof IPath) {
					status = validateInputs(obj.toString());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return status;
	}
	
	private IStatus validateInputs(String... inputs) throws Exception {
		boolean[] result;
		result = MavenCoreUtils.serviceExists(inputs);
		final List<IStatus> statuses = new ArrayList<IStatus>(result.length);
		for (int i = 0; i < result.length; i++) {
			if (result[i] == true) {
				statuses.add(getBasicStatusModel(
						StringUtil.formatString(Messages.ERROR_SERVICE_ALREADY_EXIST, inputs[i])
						));
			}
		}
		return statuses.isEmpty() ? Status.OK_STATUS : 
			EclipseMessageUtils.createErrorMultiStatus(statuses, 
					Messages.ERROR_SERVICE_VALIDATION_FAILED);
	}
}
