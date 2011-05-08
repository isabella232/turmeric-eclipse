/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.frameworkjars.handlers;

import org.ebayopensource.turmeric.frameworkjars.ui.CopyLibraryDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class GetTurmericFrameworkJars extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public GetTurmericFrameworkJars() {
		super();
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 *
	 * @param event the event
	 * @return the object
	 * @throws ExecutionException the execution exception
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		CopyLibraryDialog copyDialog = new CopyLibraryDialog(window.getShell());
		copyDialog.open();
		//ChangeServiceVersionDialog dialog = new ChangeServiceVersionDialog(window.getShell());
		//dialog.open();
		return null;
	}
}
