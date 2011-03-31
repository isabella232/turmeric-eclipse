/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.template;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;


/**
 * @author smathew
 * 
 * This class performs the basic common steps required for any xsd
 */
public class BasicProcessor implements ICommand {

	public boolean execute(Object parameter, IProgressMonitor monitor) throws CommandFailedException {
		if (parameter != null && parameter instanceof CommonXSDProcessorParam) {
			CommonXSDProcessorParam templateProcessorParam = (CommonXSDProcessorParam) parameter;

			XSDSchema xsdSchema = templateProcessorParam.getOutPutSchema();
			TypeParamModel uiModel = templateProcessorParam
					.getInputTypeParamModel();

			xsdSchema.setVersion(StringUtils
					.defaultString(uiModel.getVersion()));
			xsdSchema.setTargetNamespace(StringUtils.defaultString(uiModel
					.getNamespace()));
			xsdSchema.getQNamePrefixToNamespaceMap().put("tns",
					uiModel.getNamespace());
			ProgressUtil.progressOneStep(monitor);
			
			if (!(xsdSchema.getTypeDefinitions().get(0) instanceof XSDTypeDefinition))
				throw new CommandFailedException(
						"Type Definition couldnt be retrieved from the template");
			XSDTypeDefinition xsdTypeDefinition = (XSDTypeDefinition) xsdSchema
					.getTypeDefinitions().get(0);
			if (xsdTypeDefinition == null)
				throw new CommandFailedException("The template is corrupted.");
			xsdTypeDefinition.setName(StringUtils.defaultString(uiModel
					.getTypeName()));
			ProgressUtil.progressOneStep(monitor);
			return true;
		} else {
			throw new CommandFailedException(
					"Input is not compatible. Possible reason is a corrupted template area.");
		}

	}

}
