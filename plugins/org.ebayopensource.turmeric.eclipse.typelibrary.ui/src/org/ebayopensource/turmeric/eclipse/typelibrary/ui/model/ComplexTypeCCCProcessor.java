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
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.template.CommonXSDProcessorParam;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeCCParamModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardElementPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;


/**
 * The Class ComplexTypeCCCProcessor.
 *
 * @author smathew
 * 
 * 
 * COmplex Type with Complex Content Processor
 */
public class ComplexTypeCCCProcessor implements ICommand {

	/**
	 * {@inheritDoc}
	 */
	public boolean execute(Object object, IProgressMonitor monitor)
			throws CommandFailedException {
		CommonXSDProcessorParam processorInput = (CommonXSDProcessorParam) object;

		XSDTypeDefinition typeDefinition = (XSDTypeDefinition) processorInput
				.getOutPutSchema().getTypeDefinitions().get(0);

		TypeParamModel typeParamModel = processorInput.getInputTypeParamModel();

		if (!(typeParamModel instanceof ComplexTypeCCParamModel)
				|| !(typeDefinition instanceof XSDComplexTypeDefinition))
			throw new CommandFailedException(
					"Input param or the template is bad");
		ProgressUtil.progressOneStep(monitor);

		ComplexTypeCCParamModel complexTypeParamModel = (ComplexTypeCCParamModel) typeParamModel;
		XSDComplexTypeDefinition complexTypeDefinition = (XSDComplexTypeDefinition) typeDefinition;

		Object baseType = typeParamModel.getBaseType();
		if (baseType != null) {
			TypeLibraryUIActivator.setBaseTypeForComplexTypes(complexTypeDefinition,
					baseType, complexTypeParamModel.getTypeName(),
					complexTypeParamModel.getTypeLibraryName(),
					complexTypeParamModel.getVersion());

		}
		ProgressUtil.progressOneStep(monitor);

		for (ComplexTypeWizardElementPage.ElementTableModel row : complexTypeParamModel
				.getElementTableModel()) {
			TypeLibraryUIActivator.addElementDeclaration(complexTypeDefinition,
					complexTypeParamModel, row.getElementName(), row
							.getRawElementType(), row.getMinOccurs(), row
							.getMaxOccurs());
			ProgressUtil.progressOneStep(monitor, 10);
		}
		return true;
	}
}
