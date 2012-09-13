/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.ui.pref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.ui.RepositorySystemUIActivator;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;


/**
 * The Class PreferencePage.
 *
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

	/**
	 * Instantiates a new preference page.
	 */
	public PreferencePage() {
		super(GRID);
		setPreferenceStore(RepositorySystemUIActivator.getDefault().getPreferenceStore());
		setDescription("Turmeric SOA Plugin Preferences");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createFieldEditors() {
		final List<ISOARepositorySystem> reposList = GlobalRepositorySystem
				.instanceOf().getAvailableRepositorySystems();
		final List<String[]> choices = new ArrayList<String[]>();
		final Map<String, Map<String, String>> organizations = new LinkedHashMap<String, Map<String, String>>();
			choices.add(new String[] { PreferenceConstants.PREF__WARNING,
					PreferenceConstants.PREF__WARNING });
			choices.add(new String[] { PreferenceConstants.PREF__ERROR,
			PreferenceConstants.PREF__ERROR });
		
		final Composite buildSystemComposite = getFieldEditorParent();
		
		final CustomRadioGroupFieldEditor buildSystem = new CustomRadioGroupFieldEditor(
				PreferenceConstants.PREF_ERROR_LEVEL_NAME, "Error Level For Split Package Validation", 1,
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
					final Object oldValue, Object newValue) {
				super.fireValueChanged(property, oldValue, newValue);
				if (ObjectUtils.equals(oldValue, newValue))
					return;
				RepositorySystemActivator.getDefault().getPluginPreferences().setValue(
							PreferenceConstants.PREF_ERROR_LEVEL_NAME,
							(String)newValue);
					doLoad();
			}

			@Override
			protected void doLoadDefault() {
				final String defaultValue = getPreferenceStore()
						.getDefaultString(getPreferenceName());
					setPresentsDefaultValue(false);
					doLoad();
					return;
				} 
		};
		addField(buildSystem);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IWorkbench workbench) {
		// nothing here for now

	}

}
