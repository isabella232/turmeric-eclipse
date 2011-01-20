/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.actions;

import java.util.ArrayList;
import java.util.Iterator;

import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.wst.ImportTypeFromTypeLibrary;
import org.ebayopensource.turmeric.eclipse.typelibrary.registry.TypeViewer;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author smathew
 * 
 * The Add Import Action for the type viewer. This action appears in the Types
 * Registry View
 * 
 */
public class AddImportAction extends Action {
	private static final SOALogger logger = SOALogger.getLogger();

	public TypeViewer typeViewer = null;

	public static final String MISSING_EDITOR_ERR_MSG = "The plugin could not find the right editor to import. Please open the XSD/WSDL document in a XSD/WTP editor and try again. ";

	/**
	 * The constructor stores the type Viewer
	 * 
	 * @param typeViewer
	 */
	public AddImportAction(TypeViewer typeViewer) {
		this.typeViewer = typeViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 * 
	 * computes the highlighted editor and imports the type to the editor. If
	 * the editor does not have wsdl or xsd, it throws an Error Message.
	 * 
	 * Otherwise It imports the code, updates the dependency.
	 */
	@Override
	public void run() {
		if (typeViewer != null && typeViewer.getSelection() != null
				&& typeViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) typeViewer
					.getSelection();
			ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();
			Iterator<?> iterator = structuredSelection.iterator();
			while (iterator.hasNext()) {
				Object selectedObject = iterator.next();
				if (selectedObject instanceof LibraryType) {
					LibraryType libraryType = (LibraryType) selectedObject;
					selectedTypes.add(libraryType);
				}
			}
			if (!selectedTypes.isEmpty()) {
				IEditorPart editorPart = UIUtil.getActiveEditor();
				if (editorPart == null) {
					UIUtil
							.showErrorDialog(
									null,
									"Editor couldnt be located",
									"Falied to locate an appropriate editor to import.",
									MISSING_EDITOR_ERR_MSG);
					return;
				}

				Object adaptedObject = TypeLibraryUtil
						.getAdapterClassFromWTPEditors(editorPart);
				if (adaptedObject == null) {
					showEditorErrorMsg();
					return;
				}

				if (editorPart.getEditorInput() == null
						|| !(editorPart.getEditorInput() instanceof IFileEditorInput)) {
					showEditorErrorMsg();
					return;
				}

				IFileEditorInput editorInput = (IFileEditorInput) editorPart
						.getEditorInput();
				IFile selectedFile = editorInput.getFile();
				if (!ImportTypeFromTypeLibrary
						.validateSelectedTypeForImport(selectedTypes
								.toArray(new LibraryType[0]), selectedFile)) {
					return;
				}
				if (adaptedObject instanceof Definition) {
					try {
						ImportTypeFromTypeLibrary
								.performInlineOperationsForWSDLEditor(
										(Definition) adaptedObject,
										selectedTypes
												.toArray(new LibraryType[0]),
										selectedFile);
					} catch (Exception e) {
						logger.error(e);
						showGeneralErrorMsg();
					}
					return;
				}
				if (adaptedObject instanceof XSDSchema) {
					try {
						ImportTypeFromTypeLibrary
								.performImportTasksForXSDEditor(
										(XSDSchema) adaptedObject,
										selectedTypes
												.toArray(new LibraryType[0]),
										selectedFile);
					} catch (Exception e) {
						logger.error(e);
						showGeneralErrorMsg();
					}
					return;
				}
			}
		}
	}

	private void showEditorErrorMsg() {
		UIUtil
				.showErrorDialog(
						null,
						"Editor couldnt be located",
						"Falied to locate an appropriate editor to import.",
						"Plugin located an editor, but is not a WTP editor. Make sure that you have opened the document in a XSD/WSDL editor and focus the editor before importing");
	}

	private void showGeneralErrorMsg() {
		UIUtil
				.showErrorDialog(
						null,
						"Import failed",
						"Plugin failed to import the selected schema types.",
						"This error is mostly due to the environment that you are using. Please refresh the type registry by clicking the Refresh button in this view, reopen the XSD/WSDL editor and try again.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getAccelerator()
	 */
	@Override
	public int getAccelerator() {
		return super.getAccelerator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText() {
		return "Add to Current XSD/WSDL editor";// Include Types in Schema
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "Includes the selected schemas to the editor in focus.";
	}

}
