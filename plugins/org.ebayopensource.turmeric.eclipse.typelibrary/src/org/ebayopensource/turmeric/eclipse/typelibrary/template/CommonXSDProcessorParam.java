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
 * The Class CommonXSDProcessorParam.
 *
 * @author smathew
 * 
 * This is the common input parameter for all processors except for
 * FileProcessor
 */
public class CommonXSDProcessorParam {
	
	/** The input type param model. */
	TypeParamModel inputTypeParamModel;
	
	/** The out put schema. */
	XSDSchema outPutSchema;

	/**
	 * Instantiates a new common xsd processor param.
	 *
	 * @param inputTypeParamModel the input type param model
	 */
	public CommonXSDProcessorParam(TypeParamModel inputTypeParamModel) {
		this.inputTypeParamModel = inputTypeParamModel;
	}

	/**
	 * Gets the input type param model.
	 *
	 * @return the input type param model
	 */
	public TypeParamModel getInputTypeParamModel() {
		return inputTypeParamModel;
	}

	/**
	 * Sets the input type param model.
	 *
	 * @param inputTypeParamModel the new input type param model
	 */
	public void setInputTypeParamModel(TypeParamModel inputTypeParamModel) {
		this.inputTypeParamModel = inputTypeParamModel;
	}

	/**
	 * Gets the out put schema.
	 *
	 * @return the out put schema
	 */
	public XSDSchema getOutPutSchema() {
		return outPutSchema;
	}

	/**
	 * Sets the out put schema.
	 *
	 * @param outPutSchema the new out put schema
	 */
	public void setOutPutSchema(XSDSchema outPutSchema) {
		this.outPutSchema = outPutSchema;
	}

}
