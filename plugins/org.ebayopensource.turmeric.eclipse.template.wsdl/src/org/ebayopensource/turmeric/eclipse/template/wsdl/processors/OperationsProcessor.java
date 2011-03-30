/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.template.wsdl.processors;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.template.wsdl.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.template.wsdl.util.ServiceTemplateUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.xsd.XSDComplexTypeDefinition;


/**
 * Takes the UI model and process it and add the operations to the template.
 * Processing can be of two types EMF based or template based. Here this class
 * is EMF based. For an operation this class adds the input and output element
 * declarations as well. The new elements would be inheriting from the base type
 * mentioned in the "service_conf.properties", Any issues with the properties
 * file, the operation names, the parameter names all affect this class's
 * execution. Fits into the processor chain of template processing.
 * 
 * @author smathew
 */
public class OperationsProcessor implements ICommand {

	private Definition definition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.ICommand#execute(java.lang.Object,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean execute(Object parameter, IProgressMonitor monitor)
			throws CommandFailedException {
		if (parameter != null && parameter instanceof CommonWSDLProcessorParam) {
			try {
				CommonWSDLProcessorParam processorModel = (CommonWSDLProcessorParam) parameter;
				definition = processorModel.getDefinition();
				PortType portType = ServiceTemplateUtil
						.getDefaultPort(definition);
				ProgressUtil.progressOneStep(monitor);
				for (org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.Operation operationModel : processorModel
						.getInputParamModel().getOperations()) {
					if (ServiceTemplateUtil.operationExists(portType,
							operationModel.getName()) == false) {
						Operation wsdlOperation = ServiceTemplateUtil
								.addOperation(portType, operationModel
										.getName());
						
						// Input Paramaters
						if (operationModel.getInputParameter() != null) {
							Part inputPart = ServiceTemplateUtil.addInput(
									wsdlOperation, operationModel
											.getInputParameter().getName());
							XSDComplexTypeDefinition inputComplexType = ServiceTemplateUtil
									.createInputComplexType(inputPart,
											wsdlOperation, processorModel
													.getInputParamModel()
													.getTypeFolding());
							for (IParameterElement inputParam : operationModel
									.getInputParameter().getElements())
								addElementDecl(inputParam, inputComplexType,
										processorModel.getInputParamModel()
												.getTypeFolding());
						}
						ProgressUtil.progressOneStep(monitor, 5);
						// Output Paramaters
						if (operationModel.getOutputParameter() != null) {
							Part outputPart = ServiceTemplateUtil.addOutput(
									wsdlOperation, operationModel
											.getOutputParameter().getName());
							XSDComplexTypeDefinition outputComplexType = ServiceTemplateUtil
									.createOutputComplexType(outputPart,
											wsdlOperation, processorModel
													.getInputParamModel()
													.getTypeFolding());
							for (IParameterElement ouputParam : operationModel
									.getOutputParameter().getElements())
								addElementDecl(ouputParam, outputComplexType,
										processorModel.getInputParamModel()
												.getTypeFolding());
						}
						ProgressUtil.progressOneStep(monitor, 5);
					}
				}
				return true;
			} catch (Exception e) {
				throw new CommandFailedException(SOAMessages.OP_ERR, e);
			}

		} else {
			throw new CommandFailedException(SOAMessages.INPUT_ERR);
		}

	}

	private void addElementDecl(IParameterElement paramElement,
			XSDComplexTypeDefinition complexTypeDefinition, boolean typeFolding)
			throws Exception {
		ServiceTemplateUtil.addElementDeclaration(paramElement,
				complexTypeDefinition, typeFolding);
	}
}
