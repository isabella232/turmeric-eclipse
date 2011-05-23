/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.dialogs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.ui.components.DependenciesViewer;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


/**
 * The Class DependenciesDialog.
 *
 * @author smathew
 */
public class DependenciesDialog extends TitleAreaDialog {
	private final String title;
	private final String message;
	private final String serviceName;
	private final boolean multiSelect;
	private DependenciesViewer viewer = null;
	private Set<AssetInfo> services = null;
	private final Set<AssetInfo> selected = SetUtil.linkedSet();
	private final Map<String, AssetInfo> filteredServices = new ConcurrentHashMap<String, AssetInfo>();
	private int helpID = ISOAHelpProvider.WINDOW_DEPENDENCIES;
	private final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new dependencies dialog.
	 *
	 * @param parentShell the parent shell
	 * @param serviceName the service name
	 */
	public DependenciesDialog(final Shell parentShell, final String serviceName) {
		this(parentShell, serviceName, false);
	}

	/**
	 * Instantiates a new dependencies dialog.
	 *
	 * @param parentShell the parent shell
	 * @param serviceName the service name
	 * @param multiSelect the multi select
	 */
	public DependenciesDialog(final Shell parentShell,
			final String serviceName, final boolean multiSelect) {
		this(parentShell, "Add a Service Dependency",
				"Add a Service Dependency for Service: " + serviceName,
				serviceName, multiSelect);
	}

	/**
	 * Instantiates a new dependencies dialog.
	 *
	 * @param parentShell the parent shell
	 * @param title the title
	 * @param message the message
	 */
	public DependenciesDialog(final Shell parentShell, final String title,
			final String message) {
		this(parentShell, title, message, null, false);
	}

	/**
	 * Instantiates a new dependencies dialog.
	 *
	 * @param parentShell the parent shell
	 * @param title the title
	 * @param message the message
	 * @param serviceName the service name
	 * @param multiSelect the multi select
	 */
	public DependenciesDialog(final Shell parentShell, final String title,
			final String message, final String serviceName,
			final boolean multiSelect) {
		this(parentShell, title, message, serviceName, multiSelect, -1);
	}
	
	/**
	 * Instantiates a new dependencies dialog.
	 *
	 * @param parentShell the parent shell
	 * @param title the title
	 * @param message the message
	 * @param serviceName the service name
	 * @param multiSelect the multi select
	 * @param helpID the help id
	 */
	public DependenciesDialog(final Shell parentShell, final String title,
			final String message, final String serviceName,
			final boolean multiSelect, int helpID) {
		super(parentShell);
		this.title = title;
		this.message = message;
		this.serviceName = serviceName;
		this.multiSelect = multiSelect;
		if (helpID != -1)
			this.helpID = helpID;
	}

	/**
	 * Sets the filtered services.
	 *
	 * @param services the services
	 * @return the dependencies dialog
	 */
	public DependenciesDialog setFilteredServices(final AssetInfo... services) {
		filteredServices.clear();
		for (final AssetInfo service : services)
			filteredServices.put(service.getShortDescription(), service);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(final Composite parent) {
		final Control contents = super.createContents(parent);
		setTitle(title);
		setMessage(message);
		UIUtil.getHelpSystem().setHelp(parent,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider()
				.getHelpContextID(helpID));
		validate();
		return contents;
	}
	
	private void validate() {
		String errorMessage = null;
		if (viewer.getSelected().isEmpty() == true) {
			errorMessage = "Please select at least one service";
		}
		setErrorMessage(errorMessage);
		getButton(IDialogConstants.OK_ID).setEnabled(errorMessage == null);
	}

	/**
	 * Gets the content provider.
	 *
	 * @return the content provider
	 */
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public Object[] getElements(final Object inputElement) {
				if (!(inputElement instanceof IWorkspaceRoot))
					return null;
				if (services == null) {
					try {
						services = SetUtil.set();
						services.addAll(GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().getAssetRegistry()
								.getAllAvailableServices());
					} catch (Exception e) {
						services = null;
						logger.error(e);
						UIUtil.showErrorDialog(getShell(),
								"Error Occurred During Fetching Services",
								null, e);
					}
				}
				return services != null ? services.toArray() : Collections.EMPTY_LIST.toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(final Viewer viewer,
					final Object oldInput, final Object newInput) {
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite container = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		
		final GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 520;
		gd.heightHint = 300;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		container.setLayoutData(gd);
		
		viewer = new DependenciesViewer(container, multiSelect)
				.setServiceName(serviceName);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		viewer.getControl().setLayoutData(gridData);
		final IStructuredContentProvider contentProvider = getContentProvider();
		final ITableLabelProvider labelProvider = new ITableLabelProvider() {
			@SuppressWarnings("unused")
			public Image getImage(final Object element) {
				return null;
			}

			@SuppressWarnings("unused")
			public String getText(final Object element) {
				return null;
			}

			@Override
			public void dispose() {
			}

			@Override
			public Image getColumnImage(final Object element,
					final int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(final Object element,
					final int columnIndex) {
				if (element instanceof AssetInfo) {
					final AssetInfo info = (AssetInfo) element;
					switch (columnIndex) {
					case 1:
						return info.getShortDescription();
					case 2:
						return info.getVersion();
					case 3:
						return info instanceof ProjectInfo ? ((ProjectInfo) info)
								.getServiceLayer()
								: "";
					}
				}
				return "";
			}

			@Override
			public void addListener(final ILabelProviderListener listener) {
			}

			@Override
			public boolean isLabelProperty(final Object element,
					final String property) {
				return false;
			}

			@Override
			public void removeListener(final ILabelProviderListener listener) {
			}
		};
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.setSorter(new DependencySorter());
		final ViewerFilter filter = new ViewerFilter() {
			@Override
			public boolean select(final Viewer iviewer,
					final Object parentElement, final Object element) {
				if (element instanceof AssetInfo) {
					final DependenciesViewer viewer = (DependenciesViewer) iviewer;
					final AssetInfo selectedService = (AssetInfo) element;
					if (StringUtils.isNotBlank(viewer.getServiceName())
							&& selectedService.getName().equals(viewer.getServiceName())) {
						return false;
					}
						
					if (filteredServices.containsKey(selectedService.getShortDescription()))
						return false;
				}
				return true;
			}
		};
		viewer.addFilter(filter);
		viewer.setInput(WorkspaceUtil.getWorkspaceRoot());
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				validate();
			}
		});
		return composite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		selected.clear();
		final Set<AssetInfo> selection = viewer.getSelected();
		if (selection == null || selection.size() == 0)
			return;
		final Set<String> selectedServices = new HashSet<String>();
		for (final AssetInfo service : selection) {
			if (selectedServices.contains(service.getShortDescription())) {
				UIUtil.showErrorDialog((Shell)null, "Error", 
						"You can not choose more than one versions of the same service->"
						+ service.getShortDescription());
				return;
			}
			selected.add(service);
			selectedServices.add(service.getShortDescription());
		}
		super.okPressed();
	}

	/**
	 * Gets the selected.
	 *
	 * @return the selected
	 */
	public Set<AssetInfo> getSelected() {
		return selected;
	}

	/**
	 * Gets the first selected.
	 *
	 * @return the first selected
	 */
	public AssetInfo getFirstSelected() {
		if (selected == null || selected.size() == 0)
			return null;
		return selected.iterator().next();
	}

	/**
	 * The Class DependencySorter.
	 */
	static class DependencySorter extends ViewerSorter {
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof AssetInfo && e2 instanceof AssetInfo) {
				return ((AssetInfo) e1).getName().compareTo(
						((AssetInfo) e2).getName());
			}
			return super.compare(viewer, e1, e2);
		}

	}
}
