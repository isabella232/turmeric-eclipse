/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.test.command;

import org.ebayopensource.turmeric.eclipse.ui.actions.BaseEditorActionDelegate;
import org.eclipse.jface.action.IAction;
import org.junit.Test;



/**
 * @author yayu
 *
 */
public class TestBaseEditorActionDelegate {
	
	@Test
	public void testAction() {
		//more features will be tested with the subclasses of BaseEditorActionDelegate
		BaseEditorActionDelegate action = new BaseEditorActionDelegate() {

			@Override
			public void run(IAction action) {
				System.out.println("Testing...");
			}
			
		};
		action.selectionChanged(null, null);
		action.setActiveEditor(null, null);
		action.run(null);
	}

}
