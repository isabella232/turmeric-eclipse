/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorRegistryViewProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.ErrLibComparator;
import org.ebayopensource.turmeric.eclipse.ui.SOAPerspectiveFactory;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;


/**
 * @author smathew
 * 
 */
public class ErrorRegistryView extends ViewPart {

	private static final SOALogger logger = SOALogger.getLogger();
	private TreeViewer errorLibraryViewer;
	private Text searchError;
	//private ErrorSelectionListener errorSelectionListener;
	
	@Override
	public void createPartControl(Composite parent) {
		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(1, true));
		createSearchArea(composite, toolkit);
		try {
			createClientArea(composite, toolkit);
			createToolbar();
			createContextMenu();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
	
	

	private void createSearchArea(final Composite parent, FormToolkit toolkit) {
		final Composite composite = toolkit.createComposite(parent);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));

		final Label label = toolkit.createLabel(composite, SOAMessages.UI_FILTER_ERROR);
		label.setLayoutData(new GridData());

		searchError = toolkit.createText(composite, "", SWT.LEFT | SWT.BORDER);
		final GridData data = new GridData();
		data.widthHint = 200;
		searchError.setLayoutData(data);
		searchError.addModifyListener(new RegModifyListener());
		searchError.setToolTipText(SOAMessages.SEARCH_TIP);
	}

	private void createClientArea(Composite parent, FormToolkit toolkit)
			throws Exception {
		// TypeLibrary viewer
		final Group errorLibGroup = new Group(parent,
				SWT.SHADOW_ETCHED_OUT);
		errorLibGroup.setBackground(toolkit.getColors().getBackground());
		errorLibGroup.setText(SOAMessages.ERROR_LIBRARIES);
		errorLibGroup.setToolTipText(SOAMessages.ERROR_LIBRARIES);
		errorLibGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		final FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight = 5;
		errorLibGroup.setLayout(layout);
		final Tree typeLibTree = toolkit.createTree(errorLibGroup, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.SINGLE);
		// typeLibTree.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		errorLibraryViewer = new TreeViewer(typeLibTree);
		errorLibraryViewer
				.setContentProvider(new ErrorLibViewContentProvider());
		errorLibraryViewer.setLabelProvider(new ErrorLibViewLabelProvider());
		errorLibraryViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				IViewPart view = UIUtil.getActivePage().findView(SOAPerspectiveFactory.VIEWID_PROPERTY);
				if (view == null) {
					//if the Properties view is not opened yet, then open it automatically
					try {
						UIUtil.getActivePage().showView(SOAPerspectiveFactory.VIEWID_PROPERTY);
					} catch (PartInitException e) {
						logger.error(e);
					}
				}
			}
		});
		errorLibraryViewer.addFilter(new ViewerFilter(){

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				boolean result = false;
				if (searchError != null
						&& StringUtils.isNotBlank(searchError.getText())) {
					if (element instanceof ISOAError) {
						return isMatch(((ISOAError)element).getName(), searchError.getText());
					} else if (element instanceof ISOAErrDomain) {
						if (!isMatch(((ISOAErrDomain)element).getName() , searchError.getText())) {
							for (ISOAError err : ((ISOAErrDomain)element).getErrors()) {
								if (StringUtils.contains(err.getName(), searchError.getText())) {
									return true;
								}
							}
						} else {
							return true;
						}
					} else if (element instanceof ISOAErrLibrary) {
						result = isMatch(((ISOAErrLibrary)element).getName() , searchError.getText());
						if (result != true) {
							for (ISOAErrDomain domain : ((ISOAErrLibrary)element).getDomains()) {
								if (!isMatch(domain.getName() , searchError.getText())) {
									for (ISOAError err : domain.getErrors()) {
										if (isMatch(err.getName(), searchError.getText())) {
											return true;
										}
									}
								} else {
									return true;
								}
							}
							
						}
					}
					return result;
				}
				return true;
			}
			
		});
		errorLibraryViewer.setInput(SOAErrContentFactory.getProvider());
		errorLibraryViewer.getControl().setToolTipText(SOAMessages.TREE_TIP);
		
		{
			int ops = DND.DROP_COPY | DND.DROP_MOVE;
			Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
			errorLibraryViewer.addDragSupport(ops, transfers, 
					new ErrorViewerDragListener(errorLibraryViewer));
		}
		
		getSite().setSelectionProvider(errorLibraryViewer);
	}
	
	private boolean isMatch(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return StringUtils.contains(str.toUpperCase(), searchStr.toUpperCase());
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	private void createContextMenu() throws Exception {
		IErrorRegistryViewProvider vProvider = 
			ErrorLibraryProviderFactory.getPreferredProvider().getErrorRegistryViewProvider();
		if (vProvider != null) {
			MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
			menuMgr.setRemoveAllWhenShown(true);
			vProvider.createContextMenu(menuMgr, this.errorLibraryViewer);
			Menu menu = menuMgr.createContextMenu(this.errorLibraryViewer.getTree());
			this.errorLibraryViewer.getTree().setMenu(menu);
			getSite().registerContextMenu(menuMgr, this.errorLibraryViewer);
		}
	}

	/**
	 * Create toolbar.
	 */
	private void createToolbar() throws Exception {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		RefreshErrRegistry refreshAction = new RefreshErrRegistry(
				errorLibraryViewer);
		
		mgr.add(refreshAction);
		final ErrLibComparator errLibComparator = new ErrLibComparator();
		final ViewerComparator comparator = new ViewerComparator(
				errLibComparator) {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof ISOAError && e2 instanceof ISOAError) {
					return errLibComparator.compare((ISOAError) e1,
							(ISOAError) e2);
				}
				return String.CASE_INSENSITIVE_ORDER.compare(e1.toString(), e2
						.toString());
			}

		};

		mgr.add(new SortRegistry(errorLibraryViewer, comparator));
		
		IErrorRegistryViewProvider vProvider = 
			ErrorLibraryProviderFactory.getPreferredProvider().getErrorRegistryViewProvider();
		if (vProvider != null) {
			vProvider.createToolBar(mgr, this.errorLibraryViewer);
		}
	}

	class RegModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			if (e.getSource() instanceof Text) {
				if (errorLibraryViewer != null) {
					errorLibraryViewer.refresh(true);
				}
			}
		}
	}
}
