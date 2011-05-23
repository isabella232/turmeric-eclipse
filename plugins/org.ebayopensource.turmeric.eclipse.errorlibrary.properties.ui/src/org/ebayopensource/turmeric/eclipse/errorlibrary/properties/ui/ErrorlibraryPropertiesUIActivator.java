package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.preferences.ErrorIdServicePreferenceConstants;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class ErrorlibraryPropertiesUIActivator extends AbstractUIPlugin {

	/**
	 *  The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui"; //$NON-NLS-1$

	// The shared instance
	private static ErrorlibraryPropertiesUIActivator plugin;
	
	/**
	 * The constructor.
	 */
	public ErrorlibraryPropertiesUIActivator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static ErrorlibraryPropertiesUIActivator getDefault() {
		return plugin;
	}
	
	
	private InstanceScope scope = new InstanceScope();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		String pluginId = Activator.PLUGIN_ID;
		ScopedPreferenceStore prefStore = new ScopedPreferenceStore(scope,
				pluginId);
		return prefStore;
	}
	
	/**
	 * Use local host.
	 *
	 * @return whether to use Local Host or not.
	 */
	public boolean useLocalHost() {
		return getPreferenceStore()
				.getBoolean(ErrorIdServicePreferenceConstants.USELOCALHOST);
	}

	/**
	 * Gets the error id service endpoint.
	 *
	 * @return the endpoint for the error id service
	 */
	public String getErrorIdServiceEndpoint() {
		return getPreferenceStore()
				.getString(ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL);
	}
	
}
