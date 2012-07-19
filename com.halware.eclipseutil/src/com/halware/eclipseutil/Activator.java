package com.halware.eclipseutil;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.halware.eclipseutil";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		ServiceReference ref = context.getServiceReference(
				PackageAdmin.class.getName());
		_packageAdmin = (PackageAdmin)context.getService( ref );
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
	public static Activator getDefault() {
		return plugin;
	}

	
	
	private PackageAdmin _packageAdmin = null;

	/**
	 * Accessor to platform utility class.
	 * @return
	 */
	public PackageAdmin getPackageAdmin() {
		assert _packageAdmin != null;
		return _packageAdmin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or the key value if not found.
	 * <br>Note that this implementation is <b>not</b> the default given
	 * by the plugin creation wizard but instead accesses resources
	 * via the Plugin's OSGI bundle.
	 */
	public static String getResourceString(final String key) {
		ResourceBundle bundle =
			Platform.getResourceBundle( getDefault().getBundle() );
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} 
		catch (MissingResourceException e) {
			return key;
		}
	}

}
