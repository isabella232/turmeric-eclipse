/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.eclipse.core.runtime.IStatus;


/**
 * @author smathew
 * 
 */
public class NameValidator extends AbstractSOAValidator {
	private static final NameValidator instance = new NameValidator();
	
	public static NameValidator getInstance() {
		return instance;
	}
	
	private NameValidator() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator#validate(java.lang.Object)
	 *      Dir name should be alphanumeric.
	 */
	@Override
	public IStatus validate(Object inputObject) throws ValidationInterruptedException{
		IStatus status = super.validate(inputObject);
		if (status.isOK()) {
			if (inputObject instanceof InputObject) {
				InputObject input = (InputObject) inputObject;
				boolean result = validate(input.getValue(), input.getPattern());
				if (!result)
					return getBasicStatusModel(input.getErrorMsg());			
			} else if (inputObject instanceof String) {
				String value = (String) inputObject;
				if (StringUtils.isBlank(value))
					return getBasicStatusModel("Value cannot be blank");
			} else
				return getBasicStatusModel("Invalid input object");
		}
		return status;

	}
	
	private boolean validate(String value, String pattern) {
		Pattern regExPattern = Pattern.compile(pattern);
		Matcher matcher = regExPattern.matcher(value);
		if (!matcher.matches())
			return false;
		return true;
	}	
}
