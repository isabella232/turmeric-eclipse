/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.template.wsdl.processors;

import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.wsdl.Definition;


/**
 * The Class CommonWSDLProcessorParam.
 *
 * @author smathew
 * 
 * This is the common input parameter for all processors except for
 * FileProcessor
 */
public class CommonWSDLProcessorParam {
	private IFile targetFile;
	private ServiceFromTemplateWsdlParamModel inputParamModel;
	private Definition definition;

	/**
	 * Instantiates a new common wsdl processor param.
	 *
	 * @param inputParamModel the input param model
	 * @param targetFile the target file
	 */
	public CommonWSDLProcessorParam(
			ServiceFromTemplateWsdlParamModel inputParamModel, IFile targetFile) {
		this.inputParamModel = inputParamModel;
		this.targetFile = targetFile;
	}

	/**
	 * Gets the input param model.
	 *
	 * @return the input param model
	 */
	public ServiceFromTemplateWsdlParamModel getInputParamModel() {
		return inputParamModel;
	}

	/**
	 * Sets the input param model.
	 *
	 * @param inputParamModel the new input param model
	 */
	public void setInputParamModel(
			ServiceFromTemplateWsdlParamModel inputParamModel) {
		this.inputParamModel = inputParamModel;
	}

	/**
	 * Gets the definition.
	 *
	 * @return the definition
	 */
	public Definition getDefinition() {
		return definition;
	}

	/**
	 * Sets the definition.
	 *
	 * @param definition the new definition
	 */
	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	/**
	 * Gets the target file.
	 *
	 * @return the target file
	 */
	public IFile getTargetFile() {
		return targetFile;
	}

	/**
	 * Sets the target file.
	 *
	 * @param targetFile the new target file
	 */
	public void setTargetFile(IFile targetFile) {
		this.targetFile = targetFile;
	}

}
