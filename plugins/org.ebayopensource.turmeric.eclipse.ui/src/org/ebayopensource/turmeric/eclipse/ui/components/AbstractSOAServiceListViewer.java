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
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.ui.dialogs.DependenciesDialog;
import org.ebayopensource.turmeric.eclipse.ui.dialogs.SOAClientConfigEnvironmentDialog;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;


/**
 * The Class AbstractSOAServiceListViewer.
 *
 * @author yayu
 * @since 1.0.0
 */
public abstract class AbstractSOAServiceListViewer {
	private SOAConsumerServicesViewer serviceList;
	private Button addEnvButton;
	private Button removeEnvButton;
	private Button addServiceButton;
	private Button removeServiceButton;
	
	private Map<String, ProjectInfo> allAvailableServiecs;
	private IProject project;
	private int helpID = -1;
	
	private boolean isZeroConfig = false;
	
	/**
	 * if current service is zero config.
	 *
	 * @return true, if is zero config
	 */
	public boolean isZeroConfig() {
		return isZeroConfig;
	}

	/**
	 * set zero config value.
	 *
	 * @param isZeroConfig the new zero config
	 */
	public void setZeroConfig(boolean isZeroConfig) {
		this.isZeroConfig = isZeroConfig;
	}


	/**
	 * Instantiates a new abstract soa service list viewer.
	 *
	 * @param project the project
	 */
	public AbstractSOAServiceListViewer(IProject project) {
		super();
		this.project = project;
	}
	
	/**
	 * Instantiates a new abstract soa service list viewer.
	 *
	 * @param project the project
	 * @param helpID the help id
	 */
	public AbstractSOAServiceListViewer(IProject project, int helpID) {
		this(project);
		this.helpID = helpID;
	}

	/**
	 * Creates the control.
	 *
	 * @param parent the parent
	 * @param availableServices the available services
	 * @return the composite
	 * @throws CoreException the core exception
	 */
	public Composite createControl(final Composite parent, 
			final Collection<EnvironmentItem> availableServices) throws CoreException {
		final Composite container = new Composite(parent, SWT.None);
		GridLayout layout=new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		
		new Label(container, SWT.LEFT).setText("&Client Configurations:");

		Composite servicelistPanel = new Composite(container, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		servicelistPanel.setLayout(layout);
		servicelistPanel.setLayoutData(new GridData(GridData.FILL_BOTH));


		serviceList = new SOAConsumerServicesViewer(servicelistPanel, true, isZeroConfig);
		final Tree tree = serviceList.getTree();
		
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = getSelectedObject();
				boolean selectEnv = obj instanceof EnvironmentItem;
				if (removeEnvButton != null) {
					removeEnvButton.setEnabled(selectEnv);
				}
				removeServiceButton.setEnabled(!selectEnv);
			}
		});
		serviceList.setInput(availableServices);

		final Composite buttonComposite = new Composite(servicelistPanel,
				SWT.None);
		buttonComposite.setLayout(new GridLayout(1, true));
		buttonComposite
				.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		

		if (isZeroConfig() == false) {
			addEnvButton = new Button(buttonComposite, SWT.PUSH);
			addEnvButton.setText("Add Environment...");
			final List<String> existingEnvs = SOAConsumerUtil.getClientEnvironmentList(project, null);
			addEnvButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					SOAClientConfigEnvironmentDialog dialog = new SOAClientConfigEnvironmentDialog(
							UIUtil.getActiveShell(), getServiceInfoList(), 
							existingEnvs);
					if (dialog.open() == Window.OK) {
						List<EnvironmentItem> items = getServiceInfoList();
						EnvironmentItem item = new EnvironmentItem(dialog.getEnvironmentName());
						if (items.isEmpty() == false) {
							//already has enviroment defined
							item.setServiceData(items.get(0).getServiceData());
							item.setServices(items.get(0).getServices());
						}
						items.add(item);
						serviceList.setInput(items);
						enviromentAdded(dialog.getEnvironmentName(), dialog.getCloneEnvironment());
					}
				}
			});

			removeEnvButton = new Button(buttonComposite, SWT.PUSH);
			removeEnvButton.setText("Remove Environment");
			removeEnvButton.setEnabled(false);
			removeEnvButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Object obj = 
						((IStructuredSelection)serviceList.getSelection()).getFirstElement();
					if (obj instanceof EnvironmentItem) {
						List<EnvironmentItem> items = getServiceInfoList();
						items.remove(obj);
						serviceList.setInput(items);
						removeEnvButton.setEnabled(false);
						environmentRemoved((EnvironmentItem)obj);
					}
				}
			});

			new Label(buttonComposite, SWT.NONE);
		}
		
		addServiceButton = new Button(buttonComposite, SWT.PUSH);
		addServiceButton.setText("&Add Service...");
		
		final SelectionListener selectionListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				if (serviceList.getTree().getItemCount() == 0) {
					MessageDialog.openError(UIUtil.getActiveShell(), 
							"Error", "Please add an environment before adding services");
					return;
				}
				
				final DependenciesDialog dialog = new DependenciesDialog(
						UIUtil.getActiveShell(), "Select Service",
						"Select services to create client", null, true, helpID) {
					@Override
					protected IStructuredContentProvider getContentProvider() {
						
						final IStructuredContentProvider delegate = super
								.getContentProvider();
						final IStructuredContentProvider provider = new IStructuredContentProvider() {
							public Object[] getElements(
									final Object inputElement) {
								
								// this should be all avaialbe services for the
								// current repository system
								if (allAvailableServiecs == null) {
									final Object[] elements = delegate
									.getElements(inputElement);
									allAvailableServiecs = new ConcurrentHashMap<String, ProjectInfo>(elements.length);
									for (Object obj : elements) {
										if (obj instanceof ProjectInfo) {
											final ProjectInfo info = (ProjectInfo) obj;
											allAvailableServiecs.put(info.getDescription(), info);
										}
									}
								}
								// Removing incompatible services due to service
								// layer constraints.
								final Collection<AssetInfo> includedServices = new ArrayList<AssetInfo>();
								final List<EnvironmentItem> items = getServiceInfoList();
								if (items.size() > 0) {
									for (String serviceName : items.get(0).getServices()) {
										Loop: for (ProjectInfo info : allAvailableServiecs.values()) {
											if (info.equals(serviceName)) {
												includedServices.add(info);
												break Loop;
											}
										}
									}
								}
								
								final Collection<AssetInfo> result = new ArrayList<AssetInfo>();
								final ISOAAssetRegistry assetRegistry = GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().getAssetRegistry();
								try {
									if (project != null && TurmericServiceUtils.isSOAImplProject(project)) {
										//existing impl project
										//we should only show services which are compatible for the underlying service 
										final ISOAProject soaProject = assetRegistry.getSOAProject(project);
										if (soaProject instanceof SOAImplProject) {
											final SOAImplProject implProject = (SOAImplProject) soaProject;
											final SOAIntfMetadata metadata = implProject.getMetadata().getIntfMetadata();
											final String layer = metadata.getServiceLayer();
											for (final ProjectInfo info : allAvailableServiecs.values()) {
												if (SOAServiceUtil.validateServiceLayer(layer, info.getServiceLayer()) == true
														&& includedServices.contains(info) == false
														&& metadata.getServiceName().equals(info.getName()) == false)
													result.add(info);
											}
										}
									} else {
										//It is a consumer or a new project, can consume any services which have valid layers
										for (final ProjectInfo info : allAvailableServiecs.values()) {
												if (SOAServiceUtil
														.isValidServiceLayer(info
																.getServiceLayer()) == true
																&& includedServices
																.contains(info.getDescription()) == false) {
													if (isZeroConfig == false) {
														result.add(info);
													} else {
														final String assetLocation = assetRegistry
														.getAssetLocation(info);
														final SOAIntfMetadata metadata = SOAIntfUtil.loadIntfMetadata(
																assetLocation, info.getName());
														if (metadata.isZeroConfig()) {
															result.add(info);
														}
													}

												}
										}
									}
								} catch (Exception e) {
									SOALogger.getLogger().error(e);
									UIUtil.showErrorDialog(e);
									return result.toArray();
								}
								
								return result.toArray();
							}

							public void dispose() {
								delegate.dispose();
							}

							public void inputChanged(final Viewer viewer,
									final Object oldInput, final Object newInput) {
								delegate.inputChanged(viewer, oldInput,
										newInput);
							}
						};
						return provider;
					}
				};
				
				Set<AssetInfo> filteredServices = new HashSet<AssetInfo>();
				final Set<EnvironmentItem> currentServices = SetUtil.set(getServiceInfoList());
				if (currentServices.size() > 0) {
					for (Object obj: currentServices.iterator().next().getServiceData().values()) {
						if (obj instanceof AssetInfo) {
							filteredServices.add((AssetInfo)obj);
						}
					}
				}
				
				try {
					if (project != null && TurmericServiceUtils.isSOAImplProject(project)) {
						final ProjectInfo implProjectInfo = GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getAssetRegistry().getProjectInfo(project.getName());
						filteredServices.add(implProjectInfo);
					}
				} catch (Exception e1) {
					SOALogger.getLogger().error(e1);
					UIUtil.showErrorDialog(e1);
					return;
				}
				
				dialog.setFilteredServices(filteredServices.toArray(new AssetInfo[0]));
				final List<Integer> statuses = new ArrayList<Integer>(1);
				final Runnable runnable = new Runnable() {
					public void run() {
						statuses.add(dialog.open());
					}
				};
				BusyIndicator.showWhile(UIUtil.display(), runnable);
				if( statuses.isEmpty() || statuses.get(0) != Window.OK
						|| dialog.getSelected().isEmpty())
					return;
                
				final Set<EnvironmentItem> services = SetUtil.set(getServiceInfoList());
				final Set<AssetInfo> addedServices = dialog.getSelected();
				if (services.isEmpty()) {
					services.add(new EnvironmentItem(SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT));
				}
				for (EnvironmentItem item: services) {
					for (AssetInfo info : addedServices) {
						item.addServiceData(info);
					}
					
				}
				
				serviceList.setInput(services);
				serviceAdded(addedServices);
			}
		};
		addServiceButton.addSelectionListener(selectionListener);

		removeServiceButton = new Button(buttonComposite, SWT.PUSH);
		removeServiceButton.setText("&Remove Service");
		removeServiceButton.setEnabled(false);
		final SelectionListener removeListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				final List<EnvironmentItem> infos = getServiceInfoList();
				final Object selectedService = getSelectedObject();
				if (selectedService == null)
					return;
				for (EnvironmentItem item : infos) {
					if (selectedService instanceof AssetInfo) {
						item.removeService(((AssetInfo)selectedService).getName());
					} else {
						item.removeService(selectedService.toString());
					}
				}
				serviceList.setInput(infos);
				removeServiceButton.setEnabled(false);
				serviceRemoved(selectedService);
			}
		};
		removeServiceButton.addSelectionListener(removeListener);
		
		//calc proper width hint
		if (isZeroConfig() == false) {
			UIUtil.setEqualWidthHintForButtons(addEnvButton, removeEnvButton, 
					addServiceButton,removeServiceButton);
		} else {
			UIUtil.setEqualWidthHintForButtons( 
					addServiceButton,removeServiceButton);
		}
		return container;
	}
	
	
	/**
	 * Gets the selected object.
	 *
	 * @return the selected object
	 */
	public Object getSelectedObject() {
		final IStructuredSelection selection = (IStructuredSelection)serviceList.getSelection();
		if (selection.isEmpty() == true)
			return null;
		return selection.getFirstElement();
	}
	
	/**
	 * Gets the service.
	 *
	 * @param serviceDescription the service description
	 * @return the service
	 */
	public ProjectInfo getService(String serviceDescription) {
		if (allAvailableServiecs != null)
			allAvailableServiecs.get(serviceDescription);
		
		return null;
	}
	
	/**
	 * Gets the service info list.
	 *
	 * @return the service info list
	 */
	public List<EnvironmentItem> getServiceInfoList() {
		final List<EnvironmentItem> result = ListUtil.list();
		if (serviceList == null || 
				(serviceList.getInput() instanceof Collection<?>) == false)
			return result;
		for (final Object obj: (Collection<?>)serviceList.getInput()) {
			if (obj instanceof EnvironmentItem) {
				result.add((EnvironmentItem)obj);
			}
		}
		return result;
	}
	
	/**
	 * Checks if is environment list empty.
	 *
	 * @return true, if is environment list empty
	 */
	public boolean isEnvironmentListEmpty() {
		return this.serviceList != null && 
		this.serviceList.getTree().getItemCount() == 0;
	}

	/**
	 * Checks if is service list empty.
	 *
	 * @return true, if is service list empty
	 */
	public boolean isServiceListEmpty() {
		if (isEnvironmentListEmpty() == false) {
			return getServiceInfoList().get(0).getServices().isEmpty();
		}
		return false;
	}

	/**
	 * Gets the service list.
	 *
	 * @return the service list
	 */
	public TreeViewer getServiceList() {
		return serviceList;
	}

	/**
	 * Gets the adds the service button.
	 *
	 * @return the adds the service button
	 */
	public Button getAddServiceButton() {
		return addServiceButton;
	}

	/**
	 * Gets the removes the service button.
	 *
	 * @return the removes the service button
	 */
	public Button getRemoveServiceButton() {
		return removeServiceButton;
	}
	
	/**
	 * Enviroment added.
	 *
	 * @param environmentName the environment name
	 * @param environmentForCopy the environment for copy
	 */
	protected abstract void enviromentAdded(String environmentName, 
			EnvironmentItem environmentForCopy);
	
	/**
	 * Environment removed.
	 *
	 * @param environment the environment
	 */
	protected abstract void environmentRemoved(EnvironmentItem environment);
	
	/**
	 * Service added.
	 *
	 * @param services the services
	 */
	protected abstract void serviceAdded(final Collection<AssetInfo> services);
	
	/**
	 * Service removed.
	 *
	 * @param removedService the removed service
	 */
	protected abstract void serviceRemoved(final Object removedService);
	
	
	
	
	
}
