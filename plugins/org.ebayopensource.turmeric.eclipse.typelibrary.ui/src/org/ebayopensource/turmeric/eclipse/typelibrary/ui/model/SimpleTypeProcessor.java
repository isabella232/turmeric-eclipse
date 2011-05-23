/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.template.CommonXSDProcessorParam;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.SimpleTypeParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;


/**
 * The Class SimpleTypeProcessor.
 *
 * @author smathew
 * 
 * Simple Type specific things are handled in this class
 */
public class SimpleTypeProcessor implements ICommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute(Object object, IProgressMonitor monitor)
			throws CommandFailedException {

		CommonXSDProcessorParam processorInput = (CommonXSDProcessorParam) object;

		XSDTypeDefinition typeDefinition = processorInput
				.getOutPutSchema().getTypeDefinitions().get(0);

		TypeParamModel typeParamModel = processorInput.getInputTypeParamModel();
		ProgressUtil.progressOneStep(monitor);

		if (!(typeParamModel instanceof SimpleTypeParamModel)
				|| !(typeDefinition instanceof XSDSimpleTypeDefinition))
			throw new CommandFailedException(
					"Input param or the template is bad");

		String baseType = (String) typeParamModel.getBaseType();
		XSDSimpleTypeDefinition simpleTypeDefinition = (XSDSimpleTypeDefinition) typeDefinition;
		XSDSimpleTypeDefinition restriction = XSDFactory.eINSTANCE
				.createXSDSimpleTypeDefinition();
		restriction.setName(TypeLibraryUIActivator.getPrefix(simpleTypeDefinition
				.getSchema(), SOATypeLibraryConstants.W3C_NAMEPSACE)
				+ baseType);
		simpleTypeDefinition.setBaseTypeDefinition(restriction);
		ProgressUtil.progressOneStep(monitor);
		return true;
	}

}
