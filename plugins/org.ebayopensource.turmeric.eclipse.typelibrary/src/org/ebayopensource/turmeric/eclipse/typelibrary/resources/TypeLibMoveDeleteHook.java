/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryDeltaVisitor;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.tools.library.SOAGlobalRegistryFactory;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author smathew This is the move delete hook for XSD modification We wont
 *         allow to people to delete an XSd referred by some other XSD
 */
public class TypeLibMoveDeleteHook implements IResourceChangeListener {

	public TypeLibMoveDeleteHook() {
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if (IResourceChangeEvent.POST_CHANGE == event.getType()) {
			TypeLibraryDeltaVisitor typeLibraryDeltaVisitor = new TypeLibraryDeltaVisitor();
			try {
				event.getDelta().accept(typeLibraryDeltaVisitor);
			} catch (CoreException e1) {
				SOALogger.getLogger().error(e1);
			}
			ArrayList<IFile> deletedXSDs = typeLibraryDeltaVisitor
					.getDeletedXSDList();

			Map<LibraryType, List<LibraryType>> typesWithChildren = new ConcurrentHashMap<LibraryType, List<LibraryType>>();
			Map<LibraryType, List<LibraryType>> typesWithParents = new ConcurrentHashMap<LibraryType, List<LibraryType>>();
			final HashSet<String> projectSet = new HashSet<String>();
			// Warn if any dep exists
			try {
				for (IFile file : deletedXSDs) {

					LibraryType libraryType = SOAGlobalRegistryAdapter.getInstance()
							.getGlobalRegistry().getType(
									TypeLibraryUtil.toQName(file));
					if (libraryType == null)
						return;
					if (!CollectionUtil.isEmpty(SOAGlobalRegistryAdapter.getInstance()
							.getGlobalRegistry().getDependentChildTypeFiles(
									libraryType))) {
						typesWithChildren
								.put(libraryType,
										SOAGlobalRegistryAdapter.getInstance()
												.getGlobalRegistry()
												.getDependentChildTypeFiles(
														libraryType));
					}
					if (!CollectionUtil.isEmpty(SOAGlobalRegistryAdapter.getInstance()
							.getGlobalRegistry().getDependentParentTypeFiles(
									libraryType))) {
						typesWithParents.put(libraryType,
								SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
										.getDependentParentTypeFiles(
												libraryType));
					}
					if (file.getProject() != null)
						projectSet.add(file.getProject().getName());

				}
				if (typeLibraryDeltaVisitor.getDeletedProject().isEmpty() == false) {
					SOALogger.getLogger().info(
							"Removing type library projects from the SOA type registry->", 
							typeLibraryDeltaVisitor.getDeletedProject());
					SOATypeRegistry typeRegistry = GlobalRepositorySystem
							.instanceOf().getActiveRepositorySystem()
							.getTypeRegistryBridge().getSOATypeRegistry();
					for (IProject deletedProject : typeLibraryDeltaVisitor.getDeletedProject()) {
						typeRegistry.removeLibraryFromRegistry(deletedProject.getName());
					}
					
				}

			} catch (Exception exception) {
				UIUtil
						.openChoiceDialog(
								"Possible Issues",
								"There might be some issues in the build or registry. Please do a project clean and update the registry.",
								MessageDialog.INFORMATION);
				SOALogger.getLogger().error(exception);
			}

			StringBuffer errorStr = new StringBuffer(
					"There are some modifications required in the TypeDependency.xml and/or TypeInformation.xml with respect to the delete action you performed now, Please ignore this message if you used Schema Type --> Delete Type context menu. ");
			boolean showFlag = false;
			if (!typesWithChildren.isEmpty()) {
				errorStr
						.append("\n\r\n Severe Issue: The types you deleted is referred by some other types. Please make sure that while building it, either change the dependency from the parent type or please put this type back.");

				showFlag = true;
			}

			if (!typesWithParents.isEmpty()) {
				errorStr
						.append("\n\r\n Minor Issue: The types you deleted refer to some other types. The typedependency/typeinformation file of this type needs to be modified. Right click project -- > Schema Type --> Synchronize Dependencies or Select Project --> Clean. \r\n\n");

				showFlag = true;
			}
			if (showFlag) {
				UIUtil.showErrorDialog(null, "", "Found dependency issues",
						errorStr.toString());
			}
			// reactForSavedXSDs(modifiedXSDs);
		}
	}

	// private void reactForSavedXSDs(ArrayList<IFile> modifiedXSDs) {
	// Set<String> typeLibs = new TreeSet<String>();
	// for (IFile file : modifiedXSDs) {
	// if (file != null && file.isAccessible()
	// && file.getProject() != null
	// && file.getProject().isAccessible())
	// typeLibs.add(file.getProject().getName());
	// }
	//
	// try {
	// if (!CollectionUtil.isEmpty(typeLibs)) {
	// SOAGlobalRegistryAdapter.populateRegistry(typeLibs
	// .toArray(new String[0]));
	// }
	// // resource is locked here
	// // for (String projectName : typeLibs) {
	// // TypeLibSynhcronizer.syncronizeAllXSDsandDepXml(WorkspaceUtil
	// // .getProject(projectName));
	// // TypeLibSynhcronizer
	// // .synchronizeTypeDepandProjectDep(WorkspaceUtil
	// // .getProject(projectName));
	// //
	// // }
	// } catch (Exception e) {
	// UIUtil
	// .openChoiceDialog(
	// "Possible Issues",
	// "There might be some issues in the build or registry. Please do a project
	// clean and update the registry.",
	// MessageDialog.INFORMATION);
	// }
	//
	// }
}
