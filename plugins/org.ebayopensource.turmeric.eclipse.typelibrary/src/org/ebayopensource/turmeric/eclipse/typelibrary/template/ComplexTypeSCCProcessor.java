/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.template;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TemplateUtils;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.ComplexTypeWizardAttribPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;


/**
 * @author smathew
 * 
 */
public class ComplexTypeSCCProcessor implements ICommand {

	public boolean execute(Object object, IProgressMonitor monitor)
			throws CommandFailedException {
		CommonXSDProcessorParam processorInput = (CommonXSDProcessorParam) object;

		XSDTypeDefinition typeDefinition = (XSDTypeDefinition) processorInput
				.getOutPutSchema().getTypeDefinitions().get(0);

		TypeParamModel typeParamModel = processorInput.getInputTypeParamModel();

		if (!(typeParamModel instanceof ComplexTypeSCParamModel)
				|| !(typeDefinition instanceof XSDComplexTypeDefinition))
			throw new CommandFailedException(
					"Input param or the template is bad");
		ProgressUtil.progressOneStep(monitor);

		ComplexTypeSCParamModel complexTypeParamModel = (ComplexTypeSCParamModel) typeParamModel;
		XSDComplexTypeDefinition complexTypeDefinition = (XSDComplexTypeDefinition) typeDefinition;

		Object baseType = typeParamModel.getBaseType();
		if (baseType != null) {
			TemplateUtils.setBaseTypeForComplexTypes(complexTypeDefinition,
					baseType, complexTypeParamModel.getTypeName(),
					complexTypeParamModel.getTypeLibraryName(),
					complexTypeParamModel.getVersion());

		}
		ProgressUtil.progressOneStep(monitor);

		for (ComplexTypeWizardAttribPage.AttribTableModel row : complexTypeParamModel
				.getAttribTableModel()) {

			TemplateUtils.addAttributeDeclarations(complexTypeDefinition,
					complexTypeParamModel, row.getAttribName(), row
							.getRawDataType(), row.getAttribDesc());
			ProgressUtil.progressOneStep(monitor, 10);
		}
		return true;
	}
}
