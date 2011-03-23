/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.typelibrary.actions.AddImportAction;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author smathew
 * @deprecated do not support dnd on type viewer. Instead, add a popup menu on
 *             type to add selected type to current XSD/WSDL editor. For a
 *             better impl, WTP/XSD editor should add PluginDropAdapter, SOA
 *             Type View should use use PluginTransfer, PluginTransferData, add
 *             an extension of "org.eclipse.ui.dropActions", to run the drop
 *             action.
 */
public class TypeViewerDragListenerListener implements DragSourceListener {
	TypeViewer typeViewer = null;

	public TypeViewerDragListenerListener(TypeViewer typeViewer) {
		this.typeViewer = typeViewer;
	}

	public void dragFinished(DragSourceEvent event) {

	}

	public void dragSetData(DragSourceEvent event) {
		event.data = getDndString();
	}

	private String getDndString() {
		StringBuffer retValueBuffer = new StringBuffer();
		if (typeViewer != null) {
			ISelection typeViewerSelection = typeViewer.getSelection();
			if (typeViewerSelection != null
					&& typeViewerSelection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) typeViewer
						.getSelection();
				Iterator<?> selectionIterator = structuredSelection.iterator();

				while (selectionIterator.hasNext()) {
					Object selectedObject = selectionIterator.next();
					if (selectedObject instanceof LibraryType) {
						LibraryType selectedType = (LibraryType) selectedObject;
						retValueBuffer.append(TypeLibraryUtil
								.getDragNDropString(selectedType));
					}

				}

			}
		}
		if (StringUtils.isEmpty(retValueBuffer.toString())) {
			UIUtil
					.showErrorDialog(
							null,
							"Plugin either could not find the data to import or appropriate editor to import does not have the focus.",
							"Import Failed",
							AddImportAction.MISSING_EDITOR_ERR_MSG);
		}
		return retValueBuffer.toString();
	}

	public void dragStart(DragSourceEvent event) {
		event.data = getDndString();
	}

}
