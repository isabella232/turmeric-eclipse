/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.views;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.ErrorLibraryUtil;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.exception.core.SOAPartNotFoundException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAOperationNotAvailableException;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.exception.ExceptionHandler;
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
	
	/** The error viewer. */
	TreeViewer errorViewer = null;

	/**
	 * Instantiates a new error viewer drag listener.
	 *
	 * @param errorViewer the error viewer
	 */
	public ErrorViewerDragListener(TreeViewer errorViewer) {
		this.errorViewer = errorViewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		try {
			event.data = getDndString();
		} catch (SOAOperationNotAvailableException e) {
			ExceptionHandler.handleException(e);
		} catch (SOAPartNotFoundException e) {
			ExceptionHandler.handleException(e);
		} catch (CoreException e) {
			ExceptionHandler.handleException(e);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
	}

}
