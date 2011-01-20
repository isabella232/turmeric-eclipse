/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.core.wst;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.typelibrary.registry.TypeSelector;
import org.ebayopensource.turmeric.eclipse.typelibrary.registry.TypeSelectorElementRenderer;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.LibraryTypeComparator;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDTypeDefinition;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * Updates the WSDL with latest versions of the XSDs for the existing types
 * being consumed in the WSDL. Scans the WSDL and find out all the types, then
 * look up the registry for the new versions of the same type and show all those
 * new types in a UI and let user select the ones he wants to update, Then the
 * selected types would be fetched from the appropriate libraries and pulled
 * into the WSDL.It also updates the type dependency.xml with the latest
 * versions being used.
 * 
 * 
 * @author smathew
 * 
 */
public class UpdateTypeVersion extends AbastractTypeLibraryAtion {

	public UpdateTypeVersion() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		try {
			if (WTPTypeLibUtil.validateEditorForContextMenus(editorPart)) {
				Object adaptedObject = TypeLibraryUtil
						.getAdapterClassFromWTPEditors(editorPart);

				if (adaptedObject instanceof Definition) {
					if (super.doValidation() == false)
						return;
					Definition definition = (Definition) adaptedObject;
					
					Map<LibraryType, XSDTypeDefinition> importedTypesMap = WTPTypeLibUtil
							.getTypeLibraryTypes(definition);
					Map<LibraryType, XSDTypeDefinition> modifiedTypesMap = WTPTypeLibUtil
							.getUpdatedSchemas(importedTypesMap,
									getSelectedProject());
					// if (modifiedTypesMap.isEmpty()) {
					// UIUtil.openChoiceDialog(SOAMessages.NO_UPDTS,
					// SOAMessages.NO_UPDTS_DTL,
					// MessageDialog.INFORMATION);
					// return;
					// }
					TypeSelector typeSelector = new TypeSelector(UIUtil
							.getActiveShell(), SOAMessages.NEW_VRSN,
							importedTypesMap.keySet().toArray(
									new LibraryType[0]), getSelectedProject()
									.getName(), new CustomTypeSelectorRenderer(
									modifiedTypesMap.keySet()));
					typeSelector.setMultipleSelection(true);

					if (typeSelector.open() == Window.OK) {
						modifyWSDL(typeSelector.getSelectedTypes(), definition,
								importedTypesMap, getSelectedProject());
						UIUtil.openChoiceDialog(SOAMessages.UPDTS_DONE,
								SOAMessages.UPDTS_DONE_DTL,
								MessageDialog.INFORMATION);
					}
				} else {
					ImportTypeFromTypeLibrary.showCommonErrorDialog(null);
				}

			}

		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			ImportTypeFromTypeLibrary.showCommonErrorDialog(e);
		}
	}

	/**
	 * Deletes the selected types from the WSDL and then pulls it from the type
	 * library registry. It also updates the type dependency.xml with the latest
	 * versions being used.
	 * 
	 * @param selectedTypes
	 * @param definition
	 * @param importedTypesMap
	 * @param project
	 * @throws Exception
	 */
	public void modifyWSDL(ArrayList<LibraryType> selectedTypes,
			Definition definition,
			Map<LibraryType, XSDTypeDefinition> importedTypesMap,
			IProject project) throws Exception {

		TreeSet<LibraryType> librarySet = new TreeSet<LibraryType>(
				new LibraryTypeComparator());
		SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
		for (LibraryType selectedType : selectedTypes) {
			librarySet.add(selectedType);
			for (LibraryType libraryType : typeRegistry.getDependentParentTypeFiles(
							selectedType)) {
				librarySet.add(libraryType);
			}
		}
		for (LibraryType selectedType : librarySet) {
			WTPTypeLibUtil.removeInlineTypeFromWSDLDefinition(definition, selectedType,
					importedTypesMap);
		}
		boolean typeFolding = SOAServiceUtil.getSOAIntfMetadata(
				SOAServiceUtil.getSOAEclipseMetadata(project)).getTypeFolding();
		for (LibraryType selectedType : librarySet) {
			WTPTypeLibUtil.inlineType(selectedType, definition, false,
					typeFolding);
		}

		TypeLibSynhcronizer.syncronizeWSDLandDepXml(definition, project);
		TypeLibSynhcronizer.synchronizeTypeDepandProjectDep(project, 
				ProgressUtil.getDefaultMonitor(null));
		TypeLibSynhcronizer.updateVersionEntryTypeDep(
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getTypeRegistryBridge().getTypeDependencyWsdlTypeName(),
				selectedTypes, project);
		WorkspaceUtil.refresh(project);
	}


	class CustomTypeSelectorRenderer extends TypeSelectorElementRenderer {
		private Set<LibraryType> newTypes;
		private Image image = null;

		public CustomTypeSelectorRenderer(Set<LibraryType> newTypes) {
			image = TypeLibraryActivator.getImageFromRegistry("icons/new.gif")
					.createImage();
			this.newTypes = newTypes;
		}

		@Override
		public Image getImage(Object element) {
			if (newTypes.contains(element)) {
				return image;
			}
			return null;
		}

		@Override
		public void dispose() {
			if (image != null)
				image.dispose();
		}
	}

}
