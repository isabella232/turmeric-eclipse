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
package org.ebayopensource.turmeric.eclipse.ui.dialogs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.ebayopensource.turmeric.eclipse.ui.components.SOACComboControlAdapter;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * User should enter the name of new environment which is not duplicate with existing ones.
 * User should also select an existing client config environment as starting pointing, which 
 * would copy the client config files from the chosen environment to the new environment dir.
 * 
 * @author yayu
 * @since 1.0.0
 */
public class SOAClientConfigEnvironmentDialog extends TitleAreaDialog {
	public static final String TITLE = "Manage Client Config Environments";
	public static final String MESSAGE = "Add new client config environments";
	public static final String MESSAGE_EXISTING_ENV = MESSAGE + " and choose an existing environment for a copy.";
	
	private int helpID = ISOAHelpProvider.WINDOW_DEPENDENCIES;
	private String environmentName = null;
	private EnvironmentItem cloneEnvironment = null;
	
	private CCombo environmentText;
	private Button chooseExistingEnvBtn;
	private Combo environmentCombo;
	private Map<String, EnvironmentItem> environments = 
		new LinkedHashMap<String, EnvironmentItem>();
	private List<String> existingEnvironments;
	
	/**
	 * @param parentShell
	 * @param currentEnvs the list of environments of the consumer projects
	 * @param existingEnvs the list of existing environments that already available
	 */
	public SOAClientConfigEnvironmentDialog(Shell parentShell, 
			List<EnvironmentItem> currentEnvs, List<String> existingEnvs) {
		super(parentShell);
		for (EnvironmentItem item: currentEnvs) {
			environments.put(item.getName(), item);
		}
		existingEnvironments = existingEnvs;
	}

	@Override
	protected Control createContents(final Composite parent) {
		final Control contents = super.createContents(parent);
		setTitle(TITLE);
		final String message = chooseExistingEnvBtn != null && chooseExistingEnvBtn.isEnabled()
		 ? MESSAGE_EXISTING_ENV : MESSAGE;
		setMessage(message);
		UIUtil.getHelpSystem().setHelp(parent,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider()
				.getHelpContextID(helpID));
		return contents;
	}
	
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite comp = (Composite) super.createDialogArea(parent);
		final GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 520;
		gd.heightHint = 300;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		comp.setLayoutData(gd);
		
		Composite composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Environment Name:");
		
		environmentText = new CCombo(composite, SWT.BORDER);
		environmentText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new AutoCompleteField(environmentText, new SOACComboControlAdapter(), 
				SOAProjectConstants.EBAY_POOL_TYPES);
		environmentText.setItems(SOAProjectConstants.EBAY_POOL_TYPES);
		environmentText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
			
		});
		
		final Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new GridLayout(2, false));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		
		chooseExistingEnvBtn = new Button(group, SWT.CHECK);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		chooseExistingEnvBtn.setLayoutData(data);
		chooseExistingEnvBtn.setText("Choose an existing environment to copy from");
		boolean hasExistingEnvs = existingEnvironments.isEmpty() == false;
		chooseExistingEnvBtn.setEnabled(hasExistingEnvs);
		chooseExistingEnvBtn.setSelection(hasExistingEnvs);
		chooseExistingEnvBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				environmentCombo.setEnabled(chooseExistingEnvBtn.getSelection());
				if (chooseExistingEnvBtn.getSelection() == false)
					environmentCombo.setText("");
				dialogChanged();
			}
		});
		
		label = new Label(group, SWT.NONE);
		label.setText("Existing Environments:");
		
		environmentCombo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		environmentCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		environmentCombo.setItems(existingEnvironments.toArray(new String[0]));
		environmentCombo.setEnabled(hasExistingEnvs);
		environmentCombo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});
		if (hasExistingEnvs) {
			environmentCombo.select(0);
		}
		environmentText.setFocus();
		return composite;
	}
	
	@Override
	public void create() {
		super.create();
		dialogChanged();
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		Button btn = getButton(IDialogConstants.OK_ID);
		if (btn != null)
			btn.setEnabled(message == null);
	}
	
	private void dialogChanged() {
		
		final String env = environmentText.getText();
		if (StringUtils.isBlank(env)) {
			updateStatus("Environment name must not be empty");
			return;
		}
		
		if (StringUtils.contains(env, " ")) {
			updateStatus("Environment name must not contain white spaces");
			return;
		}
		
		if (environments.containsKey(env) == true) {
			updateStatus("Environment name already exist");
			return;
		}
		
		final Pattern regExPattern = Pattern.compile(RegExConstants.PROJECT_NAME_EXP);
		final Matcher matcher = regExPattern.matcher(env);
		if (matcher.matches() == false) {
			updateStatus("Invalid environment name");
			return;
		}
		
		if (environmentCombo.getEnabled() == true 
				&& StringUtils.isBlank(environmentCombo.getText())) {
			updateStatus("Please choose an existing environment");
			return;
		}
		updateStatus(null);
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public EnvironmentItem getCloneEnvironment() {
		return cloneEnvironment;
	}

	@Override
	protected void okPressed() {
		environmentName = environmentText.getText();
		if (chooseExistingEnvBtn.getSelection() == true
				&& StringUtils.isNotBlank(environmentCombo.getText())) {
			cloneEnvironment = environments.get(environmentCombo.getText());
		} else {
			cloneEnvironment = null;
		}
		super.okPressed();
	}
}
