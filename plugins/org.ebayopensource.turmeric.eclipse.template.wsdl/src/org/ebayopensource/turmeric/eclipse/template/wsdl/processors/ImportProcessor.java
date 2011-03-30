/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.template.wsdl.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.compare.LibraryTypeComparator;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.template.wsdl.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.template.wsdl.util.ServiceTemplateUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.wst.WTPTypeLibUtil;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.PortType;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * This class process the preference for imports from the
 * "service_conf.properties" file and import the types to the WSDL. Throws
 * exception if the properties file is not in proper format. This file is
 * organization specific and has the common properties required for template
 * process. Remember this does not come into picture for existing WSDL flow.
 * This class depends on the registry to fetch the types and an updated registry
 * is very important for this processor to finish the operation successfully.
 * 
 * @author smathew
 */
public class ImportProcessor implements ICommand {

	private Definition definition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.ICommand#execute(java.lang.Object,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean execute(Object parameter, IProgressMonitor monitor)
			throws CommandFailedException {
		if (parameter != null && parameter instanceof CommonWSDLProcessorParam) {
			try {

				CommonWSDLProcessorParam processorModel = (CommonWSDLProcessorParam) parameter;
				definition = processorModel.getDefinition();
				// This is the set of types we want to import
				TreeSet<LibraryType> impTypeSet = new TreeSet<LibraryType>(
						new LibraryTypeComparator());
				// this set is used to add imports for elements in
				// the request and response in advance.
				TreeSet<LibraryType> additionalImpTypeSet = new TreeSet<LibraryType>(
						new LibraryTypeComparator());

				for (QName qName : getTypeNames(GlobalRepositorySystem
						.instanceOf().getActiveRepositorySystem()
						.getConfigurationRegistry().getTypesInWsdl())) {
					LibraryType libType = SOAGlobalRegistryAdapter.getInstance()
							.getGlobalRegistry().getType(qName);
					if (libType != null) {
						impTypeSet.add(libType);
						impTypeSet.addAll(SOAGlobalRegistryAdapter.getInstance()
								.getGlobalRegistry().getDependentParentTypeFiles(
										libType));
					}
				}
				PortType portType = ServiceTemplateUtil
						.getDefaultPort(definition);
				for (org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.Operation operationModel : processorModel
						.getInputParamModel().getOperations()) {
					if (!ServiceTemplateUtil.operationExists(portType,
							operationModel.getName())) {
						if (operationModel.getInputParameter() != null) {
							for (IParameterElement inputParam : operationModel
									.getInputParameter().getElements()) {
								if (inputParam.getDatatype() != null
										&& inputParam.getDatatype() instanceof LibraryType) {
									LibraryType libType = (LibraryType) inputParam
											.getDatatype();
									impTypeSet.add(libType);
									additionalImpTypeSet.add(libType);
									impTypeSet.addAll(SOAGlobalRegistryAdapter.getInstance()
											.getGlobalRegistry()
											.getDependentParentTypeFiles(
													libType));
								}
							}
						}
						if (operationModel.getOutputParameter() != null) {
							for (IParameterElement outputParam : operationModel
									.getOutputParameter().getElements()) {
								if (outputParam.getDatatype() != null
										&& outputParam.getDatatype() instanceof LibraryType) {
									LibraryType libType = (LibraryType) outputParam
											.getDatatype();
									impTypeSet.add(libType);
									additionalImpTypeSet.add(libType);
									impTypeSet.addAll(SOAGlobalRegistryAdapter.getInstance()
											.getGlobalRegistry()
											.getDependentParentTypeFiles(
													libType));
								}
							}
						}
					}
				}
				WTPTypeLibUtil.wrapImport(impTypeSet, additionalImpTypeSet,
						definition, processorModel.getInputParamModel()
								.getTypeFolding());
				return true;
			} catch (Exception e) {
				throw new CommandFailedException(SOAMessages.IMPORT_ERR, e);
			}

		} else {
			throw new CommandFailedException(SOAMessages.INPUT_ERR);
		}

	}

	/**
	 * The format for this string is {a#b,c#d)
	 * 
	 * @param typeNamesStr
	 * @return
	 */
	private List<QName> getTypeNames(String typeNamesStr) {
		List<QName> retQNames = new ArrayList<QName>();
		if (!StringUtils.isEmpty(typeNamesStr)) {
			typeNamesStr = StringUtils.substringBetween(typeNamesStr, "{", "}");
			for (String tempStr : StringUtils.split(typeNamesStr, ",")) {
				String[] tempArr = StringUtils.split(tempStr, "#");
				QName qName = new QName(tempArr[0], tempArr[1]);
				retQNames.add(qName);
			}

		}
		return retQNames;
	}

}
