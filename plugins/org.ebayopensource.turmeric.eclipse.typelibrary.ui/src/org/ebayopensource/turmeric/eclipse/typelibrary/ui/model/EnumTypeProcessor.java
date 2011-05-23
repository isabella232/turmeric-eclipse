/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.template.CommonXSDProcessorParam;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.EnumTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib.EnumTypeWizardDetailsPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xsd.XSDEnumerationFacet;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDSchemaBuildingTools;


/**
 * The Class EnumTypeProcessor.
 *
 * @author smathew
 * 
 * 
 * performs Enum Specific processing: Add enum values, Add enum doc
 */
public class EnumTypeProcessor implements ICommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute(Object object, IProgressMonitor monitor) throws CommandFailedException {
		CommonXSDProcessorParam processorInput = (CommonXSDProcessorParam) object;

		XSDTypeDefinition typeDefinition = processorInput
				.getOutPutSchema().getTypeDefinitions().get(0);

		TypeParamModel typeParamModel = processorInput.getInputTypeParamModel();

		if (!(typeParamModel instanceof EnumTypeParamModel)
				|| !(typeDefinition instanceof XSDSimpleTypeDefinition))
			throw new CommandFailedException(
					"Input param or the template is bad");
		XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition) typeDefinition;
		EnumTypeParamModel enumParamModel = (EnumTypeParamModel) typeParamModel;
		XSDFactory factory = XSDSchemaBuildingTools.getXSDFactory();
		ProgressUtil.progressOneStep(monitor);
		
		for (EnumTypeWizardDetailsPage.EnumTableModel enumRow : enumParamModel
				.getEnumTableModel()) {
			XSDEnumerationFacet enumerationFacet = factory
					.createXSDEnumerationFacet();
			enumerationFacet.setLexicalValue(StringUtils.defaultString(enumRow
					.getEnumValue()));
			simpleType.getFacetContents().add(enumerationFacet);
			TypeLibraryUIActivator.addDocumentation(enumerationFacet, StringUtils
					.defaultString(enumRow.getEnumDesc()));
			simpleType.getFacetContents().add(enumerationFacet);
			
			ProgressUtil.progressOneStep(monitor, 3);
		}
		
		return false;
	}


}
