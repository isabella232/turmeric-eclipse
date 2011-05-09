/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.template.CommonXSDProcessorParam;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.util.XSDParser;


/**
 * The Class ModelProcessor.
 *
 * @author smathew
 * 
 * Builds a model out from Schema
 */
public class ModelProcessor implements ICommand {

	/**
	 * {@inheritDoc}
	 */
	public boolean execute(Object object, IProgressMonitor monitor) throws CommandFailedException {
		if (object == null
				|| !(object instanceof CommonXSDProcessorParam)) {
			throw new CommandFailedException("Input is not good");
		}
		CommonXSDProcessorParam processorParameter = (CommonXSDProcessorParam) object;
		TypeParamModel typeParamModel = processorParameter
		.getInputTypeParamModel();

		try {
			InputStream templateInputStream = TypeLibraryUtil
			.getTemplateStream(typeParamModel.getTemplateCategory(),
					typeParamModel.getTemplateName());
			ProgressUtil.progressOneStep(monitor);
			try {
				XSDParser xSDParser = new XSDParser();
				xSDParser.parse(templateInputStream);
				processorParameter.setOutPutSchema(xSDParser.getSchema());
				return true;
			} finally {
				if (templateInputStream != null)
					IOUtils.closeQuietly(templateInputStream);
				ProgressUtil.progressOneStep(monitor);
			}
		} catch (IOException e) {
			throw new CommandFailedException("Could not read the template");
		}
	}

}
