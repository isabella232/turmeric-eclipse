/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.properties;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectProtoBufFileUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.PropertiesUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.Binding;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.DataBinding;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.MessageProtocol;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientConfig;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientEnvironment;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.ui.components.SOAConsumerServicesViewer;
import org.ebayopensource.turmeric.eclipse.ui.components.SimpleComboBoxEditor;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.jdom.JDOMException;

// TODO: Auto-generated Javadoc
/**
 * The Class SOAServiceDependenciesPage.
 *
 * @author yayu
 */
public class SOAServiceDependenciesPage extends FieldEditorPreferencePage
		implements IWorkbenchPropertyPage {
	
	/** The Constant REQUIRED_SERVICES_DELIMITER. */
	public static final String REQUIRED_SERVICES_DELIMITER = ",";
	
	/** The project. */
	private IProject project;
	
	/** The is old client config dir structure. */
	private boolean isOldClientConfigDirStructure = false;

	/** The editor parent map. */
	private Map<FieldEditor, Composite> editorParentMap = new ConcurrentHashMap<FieldEditor, Composite>();
	// single selection list
	/** The service list. */
	private SOAConsumerServicesViewer serviceList;
	
	/** The service location editor. */
	private StringFieldEditor serviceLocationEditor;
	
	/** The service binding editor. */
	private SimpleComboBoxEditor serviceBindingEditor;
	
	/** The service request binding editor. */
	private SimpleComboBoxEditor serviceRequestBindingEditor;
	
	/** The service response binding editor. */
	private SimpleComboBoxEditor serviceResponseBindingEditor;
	
	/** The message protocol editor. */
	private SimpleComboBoxEditor messageProtocolEditor;

	/** The current admin name. */
	private String currentAdminName = null;
	
	/** The current can use proto buf. */
	private boolean currentCanUseProtoBuf = false;

	/** The current service location. */
	private String currentServiceLocation = "";

	/** The Constant logger. */
	private static final SOALogger logger = SOALogger.getLogger();

	/** The FUL l_ protoco l_ list. */
	private static String[][] FULL_PROTOCOL_LIST;

	/** The DEFAUL t_ protoco l_ list. */
	private static String[][] DEFAULT_PROTOCOL_LIST;
	
	/** The is env empty. */
	private boolean isEnvEmpty = true;

	static {
		FULL_PROTOCOL_LIST = new String[DataBinding.values().length][2];
		DEFAULT_PROTOCOL_LIST = new String[DataBinding.values().length - 1][2];
		int index = 0;
		int defaultIndex = 0;
		for (DataBinding binding : DataBinding.values()) {
			String bindingName = binding.name();
			FULL_PROTOCOL_LIST[index][0] = bindingName;
			FULL_PROTOCOL_LIST[index][1] = bindingName;
			index++;
			if ("PROTOBUF".equalsIgnoreCase(bindingName) == false) {
				DEFAULT_PROTOCOL_LIST[defaultIndex][0] = bindingName;
				DEFAULT_PROTOCOL_LIST[defaultIndex][1] = bindingName;
				defaultIndex++;
			}
		}
	}

	/**
	 * Instantiates a new sOA service dependencies page.
	 */
	public SOAServiceDependenciesPage() {
		super(FieldEditorPreferencePage.GRID);
		noDefaultAndApplyButton();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		if (project != null) {
			try {
				addServiceDependenciesList();
				// create a group
				Composite parent = getFieldEditorParent();
				Group group = new Group(parent, SWT.NONE);
				group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						false, 2, 1));// why 2? 2 is the max num of controls
				group.setText("Client Config Details");
				// add controls in the group
				addServiceLocation(group);
				addServiceBinding(group);
				addServiceRequestDataBinding(group);
				addServiceResponseDataBinding(group);
				addMessageProtocol(group);

				/*
				 * Do not move the following 6 lines of code above, the
				 * createControl method of FieldEditor would always create a new
				 * layout for group. so we create a new layout after all
				 * controls are inited and thus re-layout the page.
				 */
				GridLayout layout = new GridLayout(2, false);
				initGridLayout(layout, true);
				group.setLayout(layout);
				Layout parentLayout = parent.getLayout();
				if (parentLayout instanceof GridLayout) {
					((GridLayout) parentLayout).verticalSpacing = 12;
				}
			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(e);
			}
		}
	}

	/**
	 * Inits the grid layout.
	 *
	 * @param layout the layout
	 * @param margins the margins
	 * @return the grid layout
	 */
	private GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth = 0;
			layout.marginHeight = 0;
		}

		return layout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAdaptable getElement() {
		return null;
	}

	/**
	 * Gets the current selected service.
	 *
	 * @return the current selected service
	 */
	private SOAClientEnvironment getCurrentSelectedService() {
		if (serviceList != null
				&& serviceList.getTree().getSelectionCount() == 1) {
			TreeItem item = serviceList.getTree().getSelection()[0];
			if (item.getParentItem() == null)
				return null;

			Object parentObj = item.getParentItem().getData();
			currentAdminName = item.getData().toString();
			if (parentObj instanceof EnvironmentItem) {
				String envName = isOldClientConfigDirStructure ? null
						: ((EnvironmentItem) parentObj).getName();
				return new SOAClientEnvironment(envName, currentAdminName);
			}
		}
		return null;
	}

	/**
	 * Adds the service dependencies list.
	 *
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addServiceDependenciesList() throws CoreException, IOException {
		Composite parent = getFieldEditorParent();
		/*
		 * { Composite composite = new Composite(parent, SWT.NONE);
		 * composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		 * composite.setLayout(new GridLayout(2, false)); Label label = new
		 * Label(composite, SWT.NONE); label.setText("Client Name:"); Text text
		 * = new Text(composite, SWT.BORDER); text.setEditable(false);
		 * text.setText(SOAConsumerUtil.getServiceClientName(project));
		 * text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); }
		 */

		final Group listComposite = new Group(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gridData.heightHint = 200;
		listComposite.setLayoutData(gridData);
		listComposite.setLayout(new GridLayout(1, false));
		listComposite.setText("Client Configurations");

		serviceList = new SOAConsumerServicesViewer(listComposite, false);
		List<EnvironmentItem> envList = SOAConsumerUtil
				.getClientConfigStructure(project);
		isEnvEmpty = envList == null || envList.isEmpty();
		serviceList.setInput(envList);

		this.serviceList
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						try {
							load();
						} catch (Exception eX) {
							logger.error(eX);
							UIUtil.showErrorDialog(eX);
						}
					}

				});
	}

	/**
	 * Adds the service location.
	 *
	 * @param group the group
	 */
	private void addServiceLocation(Group group) {
		serviceLocationEditor = new StringFieldEditor("", "Service Location: ",
				group);
		// serviceLocationEditor.getTextControl(group).setEnabled(false);
		editorParentMap.put(serviceLocationEditor, group);
		addField(serviceLocationEditor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		try {
			isOldClientConfigDirStructure = SOAConsumerUtil
					.isOldClientConfigDirStructure(project);
			if (isOldClientConfigDirStructure) {
				String message = GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getProjectHealthChecker()
						.getWarningMessageConsumeProjectStructureOld();
				setMessage(message, IStatus.WARNING);
			}
		} catch (CoreException e) {
			logger.warning(e);
		}
		if (this.serviceList != null
				&& this.serviceList.getTree().getItemCount() > 0) {
			TreeItem item = this.serviceList.getTree().getItem(0);

			if (item.getData() instanceof EnvironmentItem
					&& item.getItemCount() > 0) {
				item = item.getItem(0);
			}
			this.serviceList.getTree().setSelection(item);
			try {
				load();
			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(e);
			}
		} else {
			setFieldEditorEnabled(false, true);
		}

	}

	/**
	 * Load.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	private void load() throws IOException, JDOMException {
		final SOAClientEnvironment clientEnv = getCurrentSelectedService();
		if (clientEnv == null) {
			// currently selected is the environment
			setFieldEditorEnabled(false, true);
			return;
		}

		SOAClientConfig config = null;
		try {
			if (isOldClientConfigDirStructure) {
				config = SOAConsumerUtil.loadClientConfig(project, null,
						clientEnv.getServiceName());
			} else {
				config = SOAConsumerUtil.loadClientConfig(project,
						clientEnv.getEnvironment(), clientEnv.getServiceName());
			}
		} catch (CoreException e) {
			logger.error(e);
		}

		if (config != null) {
			final String serviceLocation = config != null
					&& config.getServiceLocation() != null ? config
					.getServiceLocation() : "";
			currentServiceLocation = serviceLocation;
			currentCanUseProtoBuf = validateProtoBuf(serviceLocation);
			String[][] nvPares = DEFAULT_PROTOCOL_LIST;
			if (currentCanUseProtoBuf == true) {
				nvPares = FULL_PROTOCOL_LIST;
			}
			final String serviceBinding = config != null
					&& config.getServiceBinding() != null ? config
					.getServiceBinding() : Binding.LOCAL.name();
			final String requestBinding = config != null
					&& config.getRequestDataBinding() != null ? config
					.getRequestDataBinding() : DataBinding.XML.name();
			final String responseBinding = config != null
					&& config.getResponseDataBinding() != null ? config
					.getResponseDataBinding() : DataBinding.XML.name();
			final String messageProtocol = config != null
					&& config.getMessageProtocol() != null ? config
					.getMessageProtocol() : MessageProtocol.NONE.name();

			serviceLocationEditor.setStringValue(serviceLocation);

			serviceRequestBindingEditor.updateProtocolList(requestBinding,
					nvPares);
			serviceResponseBindingEditor.updateProtocolList(responseBinding,
					nvPares);
			// serviceRequestBindingEditor.getComboBoxControl(
			// editorParentMap.get(serviceRequestBindingEditor)).setText(
			// requestBinding);
			// serviceResponseBindingEditor.getComboBoxControl(
			// editorParentMap.get(serviceResponseBindingEditor)).setText(
			// responseBinding);
			messageProtocolEditor.getComboBoxControl(
					editorParentMap.get(messageProtocolEditor)).setText(
					messageProtocol);

			// invoke this so that the modify listener will be triggered.
			serviceBindingEditor.getComboBoxControl(
					editorParentMap.get(serviceBindingEditor)).setText(
					serviceBinding);

			setFieldEditorEnabled(true, false);
		}
	}

	/**
	 * Validate proto buf.
	 *
	 * @param serviceLocation the service location
	 * @return true, if successful
	 */
	private boolean validateProtoBuf(String serviceLocation) {
		try {
			// if this project is intf project and have nonXSDFormats=protobuf
			// in sipp, return true;
			IProject project = WorkspaceUtil.getProject(currentAdminName);
			if (project.isAccessible() == true) {
				ISOAProject soaProject = GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getAssetRegistry()
						.getSOAProject(project);
				if (soaProject instanceof SOAIntfProject) {
					IFile sipp = project
							.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE);
					if (sipp.isAccessible()) {
						String noXSDSchema = PropertiesFileUtil
								.getPropertyValueByKey(
										sipp.getContents(),
										SOAProjectConstants.PROP_KEY_NON_XSD_FORMATS);
						if (noXSDSchema != null
								&& noXSDSchema
										.contains(SOAProjectConstants.SVC_PROTOCOL_BUF)) {
							return true;
						}
					}
					return false;
				}
			}
			// try to find service in service jar

			String noXSDSchema = getValueFromServiceProps(currentAdminName,
					SOAProjectConstants.PROP_KEY_NON_XSD_FORMATS);
			if (noXSDSchema != null
					&& noXSDSchema
							.contains(SOAProjectConstants.SVC_PROTOCOL_BUF)) {
				return true;
			}
			// try to access service location?proto
			String content = ProjectProtoBufFileUtil
					.getProtoBufFile(serviceLocation);
			return StringUtils.isNotBlank(content);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the value from service props.
	 *
	 * @param serviceName the service name
	 * @param key the key
	 * @return the value from service props
	 * @throws Exception the exception
	 */
	private static String getValueFromServiceProps(final String serviceName,
			final String key) throws Exception {
		final ISOAAssetRegistry registry = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry();
		final String assetLocation = registry.getAssetLocation(serviceName);
		final Properties props = SOAIntfUtil.loadMetadataProps(assetLocation,
				serviceName);
		if (props != null) {
			return StringUtils.trim(props.getProperty(key));
		}
		return null;
	}

	/**
	 * Sets the field editor enabled.
	 *
	 * @param enabled the enabled
	 * @param clearData the clear data
	 */
	private void setFieldEditorEnabled(boolean enabled, boolean clearData) {
		for (FieldEditor editor : editorParentMap.keySet()) {
			if (editor instanceof SimpleComboBoxEditor) {
				((SimpleComboBoxEditor) editor).getComboBoxControl(
						editorParentMap.get(editor)).setEnabled(enabled);
				if (clearData == true)
					((SimpleComboBoxEditor) editor).getComboBoxControl(
							editorParentMap.get(editor)).clearSelection();
			} else if (editor instanceof StringFieldEditor) {
				((StringFieldEditor) editor).getTextControl(
						editorParentMap.get(editor)).setEnabled(enabled);
				if (clearData == true)
					((StringFieldEditor) editor).getTextControl(
							editorParentMap.get(editor)).setText("");
			}
		}
	}

	/**
	 * Adds the service binding.
	 *
	 * @param group the group
	 */
	private void addServiceBinding(Group group) {
		String strArray[][] = new String[Binding.values().length][2];
		int index = 0;
		for (Binding binding : Binding.values()) {
			strArray[index][0] = binding.name();
			strArray[index][1] = binding.name();
			index++;
		}
		serviceBindingEditor = new SimpleComboBoxEditor("", "Service Binding:",
				strArray, group);
		addField(serviceBindingEditor);
		editorParentMap.put(serviceBindingEditor, group);

		final Combo bindingList = serviceBindingEditor
				.getComboBoxControl(editorParentMap.get(serviceBindingEditor));
		bindingList.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String bindingStr = bindingList.getText();
				Text svcLoctext = serviceLocationEditor
						.getTextControl(editorParentMap
								.get(serviceLocationEditor));
				if (Binding.LOCAL.name().equals(bindingStr)) {
					svcLoctext.setText(currentServiceLocation);
					svcLoctext.setEditable(false);
				} else {
					svcLoctext.setEditable(true);
				}
			}
		});
	}

	/**
	 * Adds the service request data binding.
	 *
	 * @param group the group
	 */
	private void addServiceRequestDataBinding(Group group) {
		serviceRequestBindingEditor = new SimpleComboBoxEditor("",
				"Request DataBinding:", FULL_PROTOCOL_LIST, group);
		addField(serviceRequestBindingEditor);
		editorParentMap.put(serviceRequestBindingEditor, group);
	}

	/**
	 * Adds the service response data binding.
	 *
	 * @param group the group
	 */
	private void addServiceResponseDataBinding(Group group) {
		serviceResponseBindingEditor = new SimpleComboBoxEditor("",
				"Response DataBinding:", FULL_PROTOCOL_LIST, group);
		addField(serviceResponseBindingEditor);
		editorParentMap.put(serviceResponseBindingEditor, group);
	}

	/**
	 * Validate protocol.
	 *
	 * @return the i status
	 */
	private IStatus validateProtocol() {
		if (currentCanUseProtoBuf == true) {
			return Status.OK_STATUS;
		}

		String newRequestDataBinding = getText(serviceRequestBindingEditor);
		String newResponseDataBinding = getText(serviceResponseBindingEditor);

		if ("protobuf".equalsIgnoreCase(newRequestDataBinding)
				|| "protobuf".equalsIgnoreCase(newResponseDataBinding)) {
			return EclipseMessageUtils.createErrorStatus(
					"ProtoBuf is not supported by current service.", null);
		}
		return Status.OK_STATUS;
	}

	/**
	 * Adds the message protocol.
	 *
	 * @param group the group
	 */
	private void addMessageProtocol(Group group) {
		String strArray[][] = new String[MessageProtocol.values().length][2];
		int index = 0;
		for (MessageProtocol protocol : MessageProtocol.values()) {
			strArray[index][0] = protocol.name();
			strArray[index][1] = protocol.name();
			index++;
		}
		messageProtocolEditor = new SimpleComboBoxEditor("",
				"Message Protocol:", strArray, group);
		addField(messageProtocolEditor);
		editorParentMap.put(messageProtocolEditor, group);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setElement(IAdaptable element) {
		try {
			if (element.getAdapter(IProject.class) instanceof IProject) {
				project = (IProject) element.getAdapter(IProject.class);
			}
		} catch (Exception exception) {
			logger.error(exception);
			UIUtil.showErrorDialog(exception);
		}
	}

	/**
	 * Gets the text.
	 *
	 * @param stringFieldEditor the string field editor
	 * @return the text
	 */
	private String getText(StringFieldEditor stringFieldEditor) {
		return stringFieldEditor.getTextControl(
				editorParentMap.get(stringFieldEditor)).getText();
	}

	/**
	 * Gets the text.
	 *
	 * @param simpleComboEditor the simple combo editor
	 * @return the text
	 */
	private String getText(SimpleComboBoxEditor simpleComboEditor) {
		return simpleComboEditor.getComboBoxControl(
				editorParentMap.get(simpleComboEditor)).getText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		try {
			// so we should collect all required information right now.
			final SOAClientEnvironment clientEnv = getCurrentSelectedService();
			if (clientEnv == null)
				// no service selected
				return true;
			final String serviceName = clientEnv.getServiceName();
			final String newServiceLocation = getText(serviceLocationEditor);
			final String newServiceBinding = getText(serviceBindingEditor);
			final String newMessageProtocol = getText(messageProtocolEditor);
			final String newRequestDataBinding = getText(serviceRequestBindingEditor);
			final String newResponseDataBinding = getText(serviceResponseBindingEditor);
			final String[] requiredServices = {};// serviceList.getItems();

			final ProjectInfo intfProjectInfo = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getAssetRegistry().getProjectInfo(serviceName);
			final String implProjectName = intfProjectInfo
					.getImplementationProjectName();
			IStatus status = PropertiesUtil.validate(serviceName,
					implProjectName, newServiceLocation, newServiceBinding);

			IStatus statusProtocol = validateProtocol();

			if (statusProtocol != Status.OK_STATUS) {
				setErrorMessage(status.getMessage());
				Throwable t = null;
				UIUtil.showErrorDialog(getShell(), "Validation Failed",
						status.getMessage(), t);
				return false;
			}

			if (status == Status.OK_STATUS) {
				setErrorMessage(null);
				final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						monitor.beginTask(
								"Modifying the properties and dependencies for SOA service -> "
										+ serviceName,
								ProgressUtil.PROGRESS_STEP * 15);
						try {
							PropertiesUtil.modifyServiceDependencies(project,
									clientEnv.getEnvironment(), serviceName,
									implProjectName, newServiceLocation,
									newServiceBinding, newMessageProtocol,
									newRequestDataBinding,
									newResponseDataBinding, requiredServices,
									monitor);
						} catch (Exception e) {
							throw new SOAResourceModifyFailedException(
									"Failed to modify dependencies and/or properties for SOA service -> "
											+ serviceName, e);
						} finally {
							monitor.done();
						}
					}
				};
				new ProgressMonitorDialog(getControl().getShell()).run(true,
						true, operation);
			} else {
				setErrorMessage(status.getMessage());
				Throwable t = null;
				UIUtil.showErrorDialog(getShell(), "Validation Failed",
						status.getMessage(), t);
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#checkState()
	 */
	@Override
	protected void checkState() {
		try {
			if (TurmericServiceUtils.isSOAImplProject(project)) {
				// if the underlying project is impl, we should skip the
				// validation.
				return;
			}
		} catch (CoreException e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
		super.checkState();
		if (isValid() == false && isEnvEmpty == true) {
			setValid(true);
		}
	}

}
