/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.template;

import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TemplateUtils;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDSchema;


/**
 * This processor writes the XSD model back to a file. The contents are
 * formatted before writing it. It uses the JDom Utility to convert the XML to
 * String and using the IOUtil saves the file. This processor is part of the XSD
 * templating stack and is the last one. From the use case point of view this is
 * a dumb class as far as XSD know how is concerned. Basically this class is
 * "what you get is what you save" class.
 * 
 * @author smathew
 * 
 * 
 */
public class FileProcessor implements ICommand {

	/*
	 * This is where the save logic resides. The parameter should be the file
	 * processor parameter for this API to react. (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.ICommand#execute(java.lang.Object,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean execute(Object object, IProgressMonitor monitor)
			throws CommandFailedException {
		if (object == null || !(object instanceof FileProcessorParam)) {
			throw new CommandFailedException("Input corrupted");
		}
		FileProcessorParam fileProcessorParam = (FileProcessorParam) object;
		String contents;
		try {
			// formatting the schema
			contents = TemplateUtils.formatContents(JDOMUtil
					.convertXMLToString(JDOMUtil
							.convertToJDom(fileProcessorParam.getInputSchema()
									.getDocument())));
			ProgressUtil.progressOneStep(monitor);

			IOUtil.writeTo(contents, fileProcessorParam.getOutputFile(),
					monitor);
			ProgressUtil.progressOneStep(monitor);
		} catch (CoreException e) {
			throw new CommandFailedException(e);
		} catch (IOException e) {
			throw new CommandFailedException(e);
		}
		return true;
	}

	/**
	 * The File processor parameter is the communication object for the
	 * enclosing File Processor. It contains the XSD definition as the input
	 * definition and output file as the final file(to be saved).
	 * 
	 * @author smathew
	 * 
	 */
	public static class FileProcessorParam {
		private XSDSchema inputSchema;
		private IFile outputFile;

		public XSDSchema getInputSchema() {
			return inputSchema;
		}

		public void setInputSchema(XSDSchema inputSchema) {
			this.inputSchema = inputSchema;
		}

		public IFile getOutputFile() {
			return outputFile;
		}

		public void setOutputFile(IFile outputFile) {
			this.outputFile = outputFile;
		}

		public FileProcessorParam(XSDSchema inputSchema, IFile outputFile) {
			super();
			this.inputSchema = inputSchema;
			this.outputFile = outputFile;
		}

	}

}
