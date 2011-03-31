/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.model;

import org.apache.commons.lang.StringUtils;

/**
 * @author smathew Thsi class encapsulates the information in the template This
 *         is an evolving class. Right now just has the documentation
 */
public class TemplateModel {

	private String documentation;

	public String getDocumentation() {
		return StringUtils.defaultString(documentation);
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

}
