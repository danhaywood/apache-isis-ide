package com.halware.nakedide.eclipse.icons;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = Activator.class.getPackage().getName();
    public static String getPluginId() {
        return PLUGIN_ID; 
    }

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

        getLOGGER().info("Started");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
        getLOGGER().info("Stopping");
        plugin = null;
        super.stop(context);
        getLOGGER().info("Stopped");
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

    

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    
    //////////////////// Logging //////////////////////
    
    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }
    
    public static void logErrorMessage(String message) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, null));
    }
    
    public static void logErrorStatus(String message, IStatus status) {
        if (status == null) {
            logErrorMessage(message);
            return;
        }
        MultiStatus multi= new MultiStatus(getPluginId(), IStatus.ERROR, message, null);
        multi.add(status);
        log(multi);
    }
    
    public static void log(String message, Throwable e) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, e));
    }


    
    //////////////////////////////////////////////////////////////////////////
    
    private final static Logger LOGGER = Logger.getLogger(Activator.class);
    public Logger getLOGGER() {
        return LOGGER;
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
            Platform.getResourceBundle( Activator.getDefault().getBundle() );
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } 
        catch (MissingResourceException e) {
            return key;
        }
    }
    

    public URL getInstallURL() {
        return getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
    }
    

    
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

}
