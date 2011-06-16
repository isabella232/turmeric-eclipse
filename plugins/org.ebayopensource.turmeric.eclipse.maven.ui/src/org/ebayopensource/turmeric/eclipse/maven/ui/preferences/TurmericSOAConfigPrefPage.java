/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.ui.preferences;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SOAFrameworkLibrary;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author yayu
 *
 */
public class TurmericSOAConfigPrefPage extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {
	private IWorkbench workbench;
	private static final SOALogger logger = SOALogger.getLogger();
	private StringFieldEditor preferredVerEditor;

	/**
	 * @param title
	 */
	public TurmericSOAConfigPrefPage() {
		super("Turmeric SOA Configurations", FieldEditorPreferencePage.GRID);
		this.setPreferenceStore(TurmericSOAConfigPrefInitializer.getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	@Override
	protected void createFieldEditors() {
		final IPreferenceStore prefStore = TurmericSOAConfigPrefInitializer.getPreferenceStore();
		final Composite composite = getFieldEditorParent();
		
		try {
			final Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout layout = new GridLayout(2, false);
			group.setLayout(layout);
			group.setText("Framework Version Configurations");
			final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getActiveOrganizationProvider();
			String soatoolsId = orgProvider.getSOAFrameworkLibraryIdentifier(SOAFrameworkLibrary.SOATOOLS);
			final ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata(soatoolsId);
			
			BooleanFieldEditor overwriteEditor = new BooleanFieldEditor(TurmericSOAConfigPrefInitializer.PREF_KEY_OVERWRITE_PREFERRED_VERSOIN, 
					"Overwrite Preferred Version", group) {

				@Override
				protected void valueChanged(boolean oldValue,
						boolean newValue) {
					enableVersionEditor(newValue);
					super.valueChanged(oldValue, newValue);
				}
				
				private void enableVersionEditor(boolean newValue) {
					if (preferredVerEditor != null) {
						preferredVerEditor.setEnabled(newValue, group);
						if (newValue == true && StringUtils.isBlank(preferredVerEditor.getStringValue())) {
							TurmericSOAConfigPrefPage.this.setErrorMessage("preferred version must not be empty");
							preferredVerEditor.setErrorMessage("preferred version must not be empty");
						}
					}
				}

				@Override
				protected void doLoadDefault() {
					
					super.doLoadDefault();
					enableVersionEditor(getBooleanValue());
				}
			};
			addField(overwriteEditor);
			final Collection<String> versions = new TreeSet<String>();
			if (metadata != null) {
				for (Artifact artifact: MavenCoreUtils.mavenEclipseAPI()
						.findArtifactByNameAndGroup(metadata.getArtifactId(), metadata.getGroupId())) {
					versions.add(artifact.getVersion());
				}
			}
			
			final String minVersion = orgProvider.getMinimumRequiredTurmericFrameworkVersion();
			this.preferredVerEditor = new StringFieldEditor(TurmericSOAConfigPrefInitializer.PREF_KEY_TURMERIC_PREFERRED_VERSOIN, 
					"Preferred Turmeric Version:", group) {

						@Override
						protected boolean doCheckState() {
							if (VersionUtil.compare(getStringValue(), minVersion) < 0) {
								this.setErrorMessage("Preferred version must be equal to or greater than " + minVersion);
								return false;
							}
							return super.doCheckState();
						}
				
			};
			this.preferredVerEditor.setEmptyStringAllowed(false);
			this.preferredVerEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
			
			addField(preferredVerEditor);
			
			if (versions.isEmpty() == false) {
				Text verText = this.preferredVerEditor.getTextControl(group);
				new AutoCompleteField(verText, new TextContentAdapter(), 
						versions.toArray(new String[0]));
			}
			
			this.preferredVerEditor.setEnabled(prefStore.getBoolean(
					TurmericSOAConfigPrefInitializer.PREF_KEY_OVERWRITE_PREFERRED_VERSOIN), group);
			
			prefStore.setValue(TurmericSOAConfigPrefInitializer.PREF_KEY_MINIMUM_REQUIRED_VERSOIN, 
					minVersion);
			prefStore.setDefault(TurmericSOAConfigPrefInitializer.PREF_KEY_MINIMUM_REQUIRED_VERSOIN, 
					minVersion);
			StringFieldEditor text = new StringFieldEditor(
					TurmericSOAConfigPrefInitializer.PREF_KEY_MINIMUM_REQUIRED_VERSOIN, "Minimum Required Version:", group);
			text.getTextControl(group).setEditable(false);
			text.setStringValue(orgProvider.getMinimumRequiredTurmericFrameworkVersion());
			addField(text);
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
	}

}
