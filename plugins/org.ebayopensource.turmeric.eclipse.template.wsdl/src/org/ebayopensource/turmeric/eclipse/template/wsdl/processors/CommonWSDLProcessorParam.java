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
 * @author smathew
 * 
 * This is the common input parameter for all processors except for
 * FileProcessor
 */
public class CommonWSDLProcessorParam {
	private IFile targetFile;
	private ServiceFromTemplateWsdlParamModel inputParamModel;
	private Definition definition;

	public CommonWSDLProcessorParam(
			ServiceFromTemplateWsdlParamModel inputParamModel, IFile targetFile) {
		this.inputParamModel = inputParamModel;
		this.targetFile = targetFile;
	}

	public ServiceFromTemplateWsdlParamModel getInputParamModel() {
		return inputParamModel;
	}

	public void setInputParamModel(
			ServiceFromTemplateWsdlParamModel inputParamModel) {
		this.inputParamModel = inputParamModel;
	}

	public Definition getDefinition() {
		return definition;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public IFile getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(IFile targetFile) {
		this.targetFile = targetFile;
	}

}
