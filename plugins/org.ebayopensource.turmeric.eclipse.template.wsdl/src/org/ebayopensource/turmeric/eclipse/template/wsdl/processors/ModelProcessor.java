/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.template.wsdl.processors;

import java.io.File;

import javax.wsdl.WSDLException;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLReaderImpl;


/**
 * The Class ModelProcessor.
 *
 * @author smathew
 * 
 * 
 * Takes the UI models and process it and create the templates.
 * Processing can be of two types emf based or template based
 */
public class ModelProcessor implements ICommand {
	private File targetFile;

	/**
	 * Instantiates a new model processor.
	 */
	public ModelProcessor() {
		this(null);
	}

	/**
	 * Instantiates a new model processor.
	 *
	 * @param tempTargetFile the temp target file
	 */
	public ModelProcessor(File tempTargetFile) {
		this.targetFile = tempTargetFile;
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean execute(Object parameter, IProgressMonitor monitor)
			throws CommandFailedException {
		if (parameter != null && parameter instanceof CommonWSDLProcessorParam) {
			CommonWSDLProcessorParam processorModel = (CommonWSDLProcessorParam) parameter;
			String filePath = null;
			if (targetFile != null) {
				filePath = targetFile.getPath();
			} else {
				IFile file = processorModel.getTargetFile();
				filePath = file.getLocation().toString();
			}
			WSDLReaderImpl wsdlReaderImpl = new WSDLReaderImpl();
			try {
				Definition definition = (Definition) wsdlReaderImpl.readWSDL(filePath);
				processorModel.setDefinition(definition);
				return true;
			} catch (WSDLException e) {
				throw new CommandFailedException(
						"Could not build the WSDL Model from the template wsdl.",
						e);
			}
		} else {
			throw new CommandFailedException("Input paramater is corrupted.");
		}

	}
}
