/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * @author smathew
 * 
 * Displays the Preference page for the repo. All contributed repos will be
 * queried for the display values using the GlobalRepo APis
 * @see GlobalRepositorySystem
 * @see ISOARepositorySystem
 */
public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	private static final SOALogger logger = SOALogger.getLogger();

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(RepositorySystemActivator.getDefault()
				.getPreferenceStore());
		setDescription("Turmeric SOA Plugin Preferences");
	}

	@Override
	public void createFieldEditors() {
		final List<ISOARepositorySystem> reposList = GlobalRepositorySystem
				.instanceOf().getAvailableRepositorySystems();
		final List<String[]> choices = new ArrayList<String[]>();
		final Map<String, List<String>> organizations = new LinkedHashMap<String, List<String>>();
		for (final ISOARepositorySystem repositorySystem : reposList) {
			choices.add(new String[] { repositorySystem.getDisplayName(),
					repositorySystem.getId() });
			final List<String> orgs = new ArrayList<String>(3);
			for (ISOAOrganizationProvider provider : repositorySystem.getOrganizationProviders()) {
				orgs.add(provider.getName());
			}
			organizations.put(repositorySystem.getId(), orgs);
		}
		final Composite buildSystemComposite = getFieldEditorParent();
		
		final CustomRadioGroupFieldEditor buildSystem = new CustomRadioGroupFieldEditor(
				PreferenceConstants.PREF_REPOSITORY_SYSTEM, "Build System", 1,
				choices.toArray(new String[0][]), organizations, 
				buildSystemComposite, true) {
			@Override
			protected FieldEditor createChoiceSubFieldEditor(
					final Composite parent, final Button radioBtn) {

				// will be implemented when stand alone comes into picture
				return null;
			}

			@Override
			protected void fireValueChanged(final String property,
					final Object oldValue, final Object newValue) {
				super.fireValueChanged(property, oldValue, newValue);
				if (ObjectUtils.equals(oldValue, newValue))
					return;
				final String currentSetting = getPreferenceStore().getString(
						PreferenceConstants.PREF_REPOSITORY_SYSTEM);
				final String currentOrgSetting = getPreferenceStore().getString(
						PreferenceConstants.PREF_ORGANIZATION);
				if (property.equals(VALUE) && 
						!openWarningDialogBox(newValue, currentSetting)) {
					// build system not being changed
					getPreferenceStore().setValue(
							PreferenceConstants.PREF_REPOSITORY_SYSTEM,
							currentSetting);
					getPreferenceStore().setValue(
							PreferenceConstants.PREF_ORGANIZATION,
							currentOrgSetting);
					doLoad();
					SOALogger.setBuildSystemName(currentSetting);
				} else {
					// clear the cached active repository system
					GlobalRepositorySystem.instanceOf()
							.setActiveRepositorySystem(null);
					SOALogger.setBuildSystemName(String.valueOf(newValue));
					logger.info(StringUtil.toString(
							"Build System changed from ", currentSetting,
							" to ", newValue));
				}
			}

			private boolean openWarningDialogBox(final Object newValue,
					final String currentValue) {
				if (ObjectUtils.equals(newValue, currentValue))
					return true;
				return UIUtil
						.openChoiceDialog(
								StringUtil.toString(
										"Changing Build System from ",
										currentValue, " to ", newValue),
								StringUtil
										.toString(
												"Changing the build system effectively invalidates any SOA project currently ",
												"existing in the workspace.  It is highly recommended to use the command once ",
												"when the workspace is established and to use different workspaces for different ",
												"Build Systems.  If you wish to continue, remove all SOA Projects from your workspace ",
												"and then reimport only those that go with the chosen build system."),
								IStatus.WARNING);
			}

			@Override
			protected void doLoadDefault() {
				final String defaultValue = getPreferenceStore()
						.getDefaultString(getPreferenceName());
				final String currentSetting = getPreferenceStore().getString(
						PreferenceConstants.PREF_REPOSITORY_SYSTEM);
				
				final String currentSubSetting = getPreferenceStore().getString(
						PreferenceConstants.PREF_ORGANIZATION);
				
				if (!openWarningDialogBox(defaultValue, currentSetting)) {
					// build system not being changed
					getPreferenceStore().setValue(
							PreferenceConstants.PREF_REPOSITORY_SYSTEM,
							currentSetting);
					getPreferenceStore().setValue(
							PreferenceConstants.PREF_ORGANIZATION,
							currentSubSetting);
					setPresentsDefaultValue(false);
					doLoad();
					SOALogger.setBuildSystemName(currentSetting);
					return;
				} else {
					// clear the cached active repository system
					GlobalRepositorySystem.instanceOf()
							.setActiveRepositorySystem(null);
					SOALogger.setBuildSystemName(defaultValue);
					logger.info(StringUtil.toString(
							"Build System changed from ", currentSetting,
							" to ", defaultValue));
				}
				super.doLoadDefault();
			}
		};
		addField(buildSystem);

	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		// nothing here for now

	}

}
