/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.util.Set;

import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;


/**
 * The Class DependencySelector.
 *
 * @author smathew
 * 
 * This is the dialog for selecting libraries
 */
public class DependencySelector extends TwoPaneElementSelector {

	/**
	 * Instantiates a new dependency selector.
	 *
	 * @param parent the parent
	 * @param items the items
	 * @param title the title
	 */
	public DependencySelector(Shell parent, Set<? extends AssetInfo> items, String title) {
		super(parent, new ElementRenderer(), new QualifiedRenderer());
		setTitle(title);
		try {

			setElements(items.toArray());
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

}
