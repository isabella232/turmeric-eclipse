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
 * @author haozhou
 *
 */
public class SOAErrorListVO {
	
	private List<SOAErrorVO> errors;
	
	public SOAErrorListVO() {
		errors = new ArrayList<SOAErrorVO>();
	}
	
	@XmlElement(name = "error")
	public List<SOAErrorVO> getErrors() {
		return errors;
	}

	public void setErrors(List<SOAErrorVO> errors) {
		this.errors = errors;
	}
	
	public void addError(SOAErrorVO error) {
		this.errors.add(error);
	}
	
	public void removeError(SOAErrorVO error) {
		this.errors.remove(error);
	}
}
