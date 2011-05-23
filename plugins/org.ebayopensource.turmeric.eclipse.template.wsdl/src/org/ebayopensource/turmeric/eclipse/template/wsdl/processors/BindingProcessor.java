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
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.template.wsdl.util.ServiceTemplateUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.wsdl.Definition;


/**
 * The Class BindingProcessor.
 *
 * @author smathew
 */
public class BindingProcessor implements ICommand {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute(Object parameter, IProgressMonitor monitor) throws CommandFailedException {
		if (parameter != null && parameter instanceof CommonWSDLProcessorParam) {
			try {
				CommonWSDLProcessorParam processorModel = (CommonWSDLProcessorParam) parameter;				
				Definition definition = processorModel.getDefinition();

				ServiceTemplateUtil.addBinding(definition,processorModel.getInputParamModel().getSelBindings());				
				ProgressUtil.progressOneStep(monitor);
				return true;
			} catch (Exception e) {
				throw new CommandFailedException(
						"Couldnt add binding to the wsdl", e);
			}

		} else {
			throw new CommandFailedException(
					"Input is not compatible. Possible reason is a corrupted template area.");
		}

	}
}
