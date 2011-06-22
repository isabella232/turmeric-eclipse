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
package org.ebayopensource.turmeric.eclipse.services.search;

import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.models.SimpleAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceReader;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * The Class SOASearchPage.
 *
 * @author yayu
 */
public class SOASearchPage extends DialogPage implements ISearchPage {
	private static final SOALogger logger = SOALogger.getLogger();
	
	private ISearchPageContainer searchContainer;
	private CCombo assetTypeList;
	private Button localWorkspaceBtn;
	private Button buildSystemBtn;
	private Button repositoryBtn;
	
	private Text serviceNameText;
	private CCombo serviceLayerList;
	//private CCombo serviceDomainList;
	//private static final List<String> DOMAIN_LIST = new ArrayList<String>();
	
	/**
	 * Instantiates a new sOA search page.
	 */
	public SOASearchPage() {
		super();
	}

	/**
	 * Instantiates a new sOA search page.
	 *
	 * @param title the title
	 */
	public SOASearchPage(String title) {
		super(title);
	}

	/**
	 * Instantiates a new sOA search page.
	 *
	 * @param title the title
	 * @param image the image
	 */
	public SOASearchPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performAction() {
		NewSearchUI.activateSearchResultView();
		NewSearchUI.runQueryInBackground(new SOASearchQuery(
				buildSystemBtn.getSelection(), 
				serviceNameText.getText(), 
				serviceLayerList.getText(), 
				"")); 
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContainer(ISearchPageContainer container) {
		this.searchContainer = container;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		
		createAssetTypeField(composite);
		
		createConditionArea(composite);
		
		createSearchCriteriaArea(composite);
	}
	
	private void createAssetTypeField(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(3, false));
		
		final Label label = new Label(composite, SWT.LEFT);
		label.setText("Asset &Type:");
		
		assetTypeList = new CCombo(composite, SWT.BORDER | SWT.READ_ONLY);
		assetTypeList.setItems(new String[]{SimpleAssetModel.ASSET_TYPE_SERVICE});
		UIUtil.decorateControl(null, assetTypeList, "the type of the asset");
		assetTypeList.select(0);
		GridData data = new GridData();
		data.widthHint = 200;
		assetTypeList.setLayoutData(data);
		assetTypeList.setBackground(UIUtil.display()
				.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	}
	
	private void createConditionArea(Composite parent) {
		final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(3, true));
		group.setText("Scope");
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		localWorkspaceBtn = new Button(group, SWT.RADIO);
		localWorkspaceBtn.setText("Local Workspace");
		localWorkspaceBtn.setLayoutData(data);
		localWorkspaceBtn.setSelection(true);
		
		buildSystemBtn = new Button(group, SWT.RADIO);
		buildSystemBtn.setText("Build System");
		buildSystemBtn.setLayoutData(data);
		
		repositoryBtn = new Button(group, SWT.RADIO);
		repositoryBtn.setText("Repository");
		repositoryBtn.setLayoutData(data);
		try {
			repositoryBtn.setEnabled(ExtensionPointFactory.getSOARegistryProvider() != null);
		} catch (CoreException e) {
			repositoryBtn.setEnabled(false);
			logger.warning(e);
		}
		
		final SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final boolean repoSelected = repositoryBtn.getSelection();
				if (e.getSource() == repositoryBtn && repoSelected == true) {
					MessageDialog.openInformation(UIUtil.getActiveShell(), 
							"SOA Search", 
							"Please use the Repository Search tab for searching SOA services in the repository.");
					repositoryBtn.setSelection(false);
				}
				SOASearchPage.this.searchContainer.setPerformActionEnabled(
						repoSelected == false);
			}
		};
		localWorkspaceBtn.addSelectionListener(listener);
		buildSystemBtn.addSelectionListener(listener);
		repositoryBtn.addSelectionListener(listener);
	}
	
	private void createSearchCriteriaArea(Composite parent) {
		final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(2, false));
		group.setText("Search Criteria");
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		Label label = new Label(group, SWT.NONE);
		label.setText("Service &Name:");
		serviceNameText = new Text(group, SWT.BORDER);
		serviceNameText.setLayoutData(data);
		UIUtil.decorateControl(null, serviceNameText, 
				"the name of the service for searching");
		
		label = new Label(group, SWT.NONE);
		label.setText("Service &Layer:");
		serviceLayerList = new CCombo(group, SWT.BORDER | SWT.READ_ONLY);
		serviceLayerList.setBackground(UIUtil.display()
				.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		serviceLayerList.setLayoutData(data);
		UIUtil.decorateControl(null, serviceLayerList, 
				"the service layer of the service for searching");
		final List<String> layers = PreferenceReader.getServiceLayer();
		Collections.reverse(layers);
		serviceLayerList.setItems(
				layers.toArray(new String[0]));
	}

}
