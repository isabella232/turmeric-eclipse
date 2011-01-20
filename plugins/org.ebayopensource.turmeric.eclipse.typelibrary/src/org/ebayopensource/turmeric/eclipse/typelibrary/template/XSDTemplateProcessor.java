/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.template;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.wst.SOAXSDValidator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeCCParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.EnumTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.SimpleTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.xsd.XSDSchema;


/**
 * @author smathew
 * 
 * 
 * Takes the UI models and process it and create the templates. Processing can
 * be of two types emf based or template based
 */
public class XSDTemplateProcessor {

	public void process(TypeParamModel typeParamModel, IFile destinationFile,
			IProgressMonitor monitor) throws CommandFailedException, ValidationInterruptedException {
		CommonXSDProcessorParam commonXSDTemplateProcessorParam = new CommonXSDProcessorParam(
				typeParamModel);
		List<ICommand> commands = new ArrayList<ICommand>();
		commands.add(new ModelProcessor());
		commands.add(new BasicProcessor());
		commands.add(new DocumentationProcessor());

		if (typeParamModel instanceof SimpleTypeParamModel) {
			commands.add(new SimpleTypeProcessor());
		}
		if (typeParamModel instanceof EnumTypeParamModel) {
			commands.add(new EnumTypeProcessor());
		}
		// this processor is just for complex type and not for complex type with
		// complex content
		if (typeParamModel instanceof ComplexTypeParamModel
				&& !(typeParamModel instanceof ComplexTypeCCParamModel)) {
			commands.add(new ComplexTypeProcessor());
		}
		if (typeParamModel instanceof ComplexTypeCCParamModel) {
			commands.add(new ComplexTypeCCCProcessor());
		}
		if (typeParamModel instanceof ComplexTypeSCParamModel) {
			commands.add(new ComplexTypeSCCProcessor());
		}
		ProgressUtil.progressOneStep(monitor);

		execute(commands, commonXSDTemplateProcessorParam, monitor);

		XSDSchema outSchema = commonXSDTemplateProcessorParam.getOutPutSchema();
		FileProcessor.FileProcessorParam fileProcessorParam = new FileProcessor.FileProcessorParam(
				outSchema, destinationFile);
		final FileProcessor fileProcessor = new FileProcessor();
		ProgressUtil.progressOneStep(monitor);
		fileProcessor.execute(fileProcessorParam, monitor);

		// validating the file
		SOAXSDValidator validator = new SOAXSDValidator();
		IStatus status = validator.validate(fileProcessorParam.getOutputFile());
		if (!status.isOK()) {
			throw new CommandFailedException(
					"XSD Validation Failed, Please fix the following issue:\r\n\n"
							+ EclipseMessageUtils.formatStatus(status));
		}
	}

	private void execute(List<ICommand> commandList,
			CommonXSDProcessorParam commonInput, IProgressMonitor monitor)
			throws CommandFailedException {
		for (ICommand command : commandList) {
			command.execute(commonInput, monitor);
		}
	}

}
