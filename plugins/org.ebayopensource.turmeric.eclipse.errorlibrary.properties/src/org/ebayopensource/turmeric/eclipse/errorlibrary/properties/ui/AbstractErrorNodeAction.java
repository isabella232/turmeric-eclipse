/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * @author yayu
 *
 */
public abstract class AbstractErrorNodeAction extends Action {
	private static final SOALogger logger = SOALogger.getLogger();
	private ISOAErrUIComp selectedErrorNode;
	private StructuredViewer viewer;

	/**
	 * @param text
	 */
	public AbstractErrorNodeAction(String text, StructuredViewer viewer) {
		super(text);
		this.viewer = viewer;
	}

	/**
	 * @param errorNode the errorNode to set
	 */
	public void setSelectedErrorNode(ISOAErrUIComp errorNode) {
		this.selectedErrorNode = errorNode;
	}

	/**
	 * @return the errorNode
	 */
	public ISOAErrUIComp getSelectedErrorNode() {
		return selectedErrorNode;
	}
	
	protected IStatus preValidation() {
		if (this.selectedErrorNode == null) {
			return EclipseMessageUtils.createErrorStatus(
					"Invalid selection->" + this.selectedErrorNode);
		}
		return Status.OK_STATUS;
	}
	
	/**
	 * @return the viewer
	 */
	public StructuredViewer getViewer() {
		return viewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			IStatus status = preValidation();
			if (status.isOK() == false) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), 
						SOAMessages.TITLE_VALIDATION_FALIED, status);
				return;
			}
			status = execute(this.selectedErrorNode);
			if (status.isOK()== true) {
				this.viewer.refresh();
			} else if (status.getSeverity() != IStatus.CANCEL){
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), 
						SOAMessages.TITLE_EXECUTION_FALIED, status);
				return;
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		} finally {
			this.selectedErrorNode = null;
		}
	}

	/**
	 * @param selectedErrorNode
	 * @return Staus.OK if succeed
	 * @throws Exception
	 */
	public abstract IStatus execute(ISOAErrUIComp selectedErrorNode) throws Exception;
}
