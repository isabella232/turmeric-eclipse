/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

/**
 * @author smathew
 * 
 * Provider for Service Explorer View. Right Now Only Refresh is added.
 * This doesnt override any existing additions.
 */
public class ActionProvider extends CommonActionProvider {

	private RefreshServicesAction refreshAction;

	@Override
	public void init(ICommonActionExtensionSite site) {
		refreshAction = new RefreshServicesAction(site.getStructuredViewer());
	}

	@Override
	public void fillActionBars(IActionBars theActionBars) {
		IMenuManager menu = theActionBars.getMenuManager();

		if (refreshAction != null
				&& !(menu.find(RefreshServicesAction.ID) != null)) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS + "-end",
					refreshAction);

			theActionBars.getToolBarManager().add(refreshAction);
		}

		theActionBars.updateActionBars();
	}

}
