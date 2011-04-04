/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.template;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.template.CommonXSDProcessorParam;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDTypeDefinition;


/**
 * @author smathew
 * 
 * Add the documentation to the XSD
 */
public class DocumentationProcessor implements ICommand {

	public boolean execute(Object object, IProgressMonitor monitor) throws CommandFailedException {

		CommonXSDProcessorParam processorInput = (CommonXSDProcessorParam) object;

		XSDTypeDefinition typeDefinition = (XSDTypeDefinition) processorInput
				.getOutPutSchema().getTypeDefinitions().get(0);
		String docValue = StringUtils.defaultString(processorInput
				.getInputTypeParamModel().getDescription());

		TypeLibraryUIActivator.addDocumentation(typeDefinition, docValue);
		ProgressUtil.progressOneStep(monitor);
		return false;
	}

}
