/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.template;

import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.eclipse.xsd.XSDSchema;


/**
 * @author smathew
 * 
 * This is the common input parameter for all processors except for
 * FileProcessor
 */
public class CommonXSDProcessorParam {
	TypeParamModel inputTypeParamModel;
	XSDSchema outPutSchema;

	public CommonXSDProcessorParam(TypeParamModel inputTypeParamModel) {
		this.inputTypeParamModel = inputTypeParamModel;
	}

	public TypeParamModel getInputTypeParamModel() {
		return inputTypeParamModel;
	}

	public void setInputTypeParamModel(TypeParamModel inputTypeParamModel) {
		this.inputTypeParamModel = inputTypeParamModel;
	}

	public XSDSchema getOutPutSchema() {
		return outPutSchema;
	}

	public void setOutPutSchema(XSDSchema outPutSchema) {
		this.outPutSchema = outPutSchema;
	}

}
