package org.ebayopensource.turmeric.eclipse.repositorysystem.ui;

import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RepositorySystemUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.repositorysystem.ui"; //$NON-NLS-1$

	// The shared instance
	private static RepositorySystemUIActivator plugin;
	
	/**
	 * The constructor
	 */
	public RepositorySystemUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static RepositorySystemUIActivator getDefault() {
		return plugin;
	}
	
	private IScopeContext scope = new InstanceScope();

	/**
	 * {@inheritDoc}
	 * 
	 * This implements a ScopedPreferenceStore. Values are initialized in the repositorysystem plugin.
	 * This shares a preferencestore with the core plugin.
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		String pluginId = RepositorySystemActivator.PLUGIN_ID;
		ScopedPreferenceStore prefStore = new ScopedPreferenceStore(scope,
				pluginId);
		return prefStore;
	}	

}
