/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * The Class SOAErrorListVO.
 *
 * @author haozhou
 */
public class SOAErrorListVO {
	
	private List<SOAErrorVO> errors;
	
	/**
	 * Instantiates a new sOA error list vo.
	 */
	public SOAErrorListVO() {
		errors = new ArrayList<SOAErrorVO>();
	}
	
	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	@XmlElement(name = "error")
	public List<SOAErrorVO> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errors the new errors
	 */
	public void setErrors(List<SOAErrorVO> errors) {
		this.errors = errors;
	}
	
	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	public void addError(SOAErrorVO error) {
		this.errors.add(error);
	}
	
	/**
	 * Removes the error.
	 *
	 * @param error the error
	 */
	public void removeError(SOAErrorVO error) {
		this.errors.remove(error);
	}
}
