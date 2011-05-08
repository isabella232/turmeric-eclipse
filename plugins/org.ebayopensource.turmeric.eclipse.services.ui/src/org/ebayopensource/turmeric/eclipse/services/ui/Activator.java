package org.ebayopensource.turmeric.eclipse.services.ui;

import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.services.ui"; //$NON-NLS-1$
	
	/** The Constant ICON_PATH. */
	public static final String ICON_PATH = "icons/";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * Gets the image from registry.
	 *
	 * @param path the path
	 * @return the image from registry
	 */
	public static Image getImageFromRegistry(String path) {
		return UIActivator.getImageFromRegistry(getDefault(), ICON_PATH, path);
	}
	

}
