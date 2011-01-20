/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.ErrorLibraryUtil;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAOperationNotAvailableException;
import org.ebayopensource.turmeric.eclipse.exception.ui.SOAPartNotFoundException;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;


/**
 * Drag listener from Error view to the java editor. Just compute the code and
 * do the text transfer.
 * 
 * @author smathew
 * 
 */
public class ErrorViewerDragListener implements DragSourceListener {
	TreeViewer errorViewer = null;

	public ErrorViewerDragListener(TreeViewer errorViewer) {
		this.errorViewer = errorViewer;
	}

	public void dragFinished(DragSourceEvent event) {
	}

	public void dragSetData(DragSourceEvent event) {
		try {
			event.data = getDndString();
		} catch (SOAOperationNotAvailableException e) {
			SOAExceptionHandler.handleException(e);
		} catch (SOAPartNotFoundException e) {
			SOAExceptionHandler.handleException(e);
		} catch (CoreException e) {
			SOAExceptionHandler.handleException(e);
		}
	}

	private String getDndString() throws SOAOperationNotAvailableException,
			SOAPartNotFoundException, CoreException {
		StringBuffer retValueBuffer = new StringBuffer();
		if (errorViewer != null) {
			ISelection typeViewerSelection = errorViewer.getSelection();
			if (typeViewerSelection != null
					&& typeViewerSelection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) errorViewer
						.getSelection();
				Iterator<?> selectionIterator = structuredSelection.iterator();
				while (selectionIterator.hasNext()) {
					Object selectedObject = selectionIterator.next();
					if (selectedObject != null
							&& selectedObject instanceof ISOAError) {
						ISOAError selectedError = (ISOAError) selectedObject;
						retValueBuffer.append(ErrorLibraryUtil
								.getImportSource(UIUtil
										.getActiveEditorsProject(),
										selectedError));
					}
				}

			}
		}
		if (StringUtils.isEmpty(retValueBuffer.toString())) {
			UIUtil.showErrorDialog(null, SOAMessages.DRAG_EERROR,
					SOAMessages.DRAG_FAILED, SOAMessages.DRAG_FAILED);
		}
		return retValueBuffer.toString();
	}

	public void dragStart(DragSourceEvent event) {
	}

}
