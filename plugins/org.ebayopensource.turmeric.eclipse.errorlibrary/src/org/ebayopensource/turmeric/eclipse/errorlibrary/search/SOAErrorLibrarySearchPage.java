/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.errorlibrary.search;

import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 *
 */
public class SOAErrorLibrarySearchPage extends DialogPage implements
		ISearchPage {
	private static final SOALogger logger = SOALogger.getLogger();

	private ISearchPageContainer searchContainer;
	

	/**
	 * 
	 */
	public SOAErrorLibrarySearchPage() {
		super();
	}

	/**
	 * @param title
	 */
	public SOAErrorLibrarySearchPage(String title) {
		super(title);
	}

	/**
	 * @param title
	 * @param image
	 */
	public SOAErrorLibrarySearchPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();	
		gridLayout.marginTop = 20;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 10;
		gridLayout.marginBottom = 30;
		gridLayout.horizontalSpacing = 10;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		setControl(composite);
		
		final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayout(new GridLayout(2, false));
		group.setText("Search Criteria");
		
		Label label = new Label(group, SWT.LEFT);
		label.setText("Search Type:");
		
		Combo combo = new Combo(group, SWT.DROP_DOWN | SWT.BORDER);
		combo.setItems(new String[]{"Error Library", "Error Domain", "Error"});
		UIUtil.decorateControl(null, combo, "the type of the asset");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		//data.widthHint = 200;
		combo.setLayoutData(data);
		combo.setBackground(UIUtil.display()
				.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		label = new Label(group, SWT.LEFT);
		label.setText("Search Pattern:");
		
		Text text = new Text(group, SWT.SINGLE | SWT.BORDER);
		
		//TODO pass a null control decorator for now
		UIUtil.decorateControl(null, text, "the type of the asset");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setBackground(UIUtil.display()
				.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	}
	
	

	public boolean performAction() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setContainer(ISearchPageContainer container) {
		this.searchContainer = container;
		
	}

}
