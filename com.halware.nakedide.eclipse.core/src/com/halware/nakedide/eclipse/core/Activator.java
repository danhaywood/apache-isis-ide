package com.halware.nakedide.eclipse.core;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.halware.nakedide.eclipse.core.logging.LogController;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    private final static Logger LOGGER = Logger.getLogger(Activator.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

	public static final String PLUGIN_ID = Activator.class.getPackage().getName();

	private static Activator plugin;
    public static Activator getDefault() {
        return plugin;
    }
    
	public Activator() {
		plugin = this;
	}

	public static String getPluginId() {
		return PLUGIN_ID; 
	}

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        getLOGGER().info("Starting");
        
        super.start(context);


        // set up log4j
        new LogController();


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


	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static Shell getActiveWorkbenchShell() {
		return getActiveWorkbenchWindow().getShell();
	}

	public static IWorkbenchPage getActiveWorkbenchPage() {
		return getActiveWorkbenchWindow().getActivePage();
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
    


    //////////////////////////////////////////////////////////////////////////
    
    private ScopedPreferenceStore preferenceStore;

    /**
     * Returns the preference store for this UI plug-in.
     * This preference store is used to hold persistent settings for this plug-in in
     * the context of a workbench. Some of these settings will be user controlled, 
     * whereas others may be internal setting that are never exposed to the user.
     * <p>
     * If an error occurs reading the preference store, an empty preference store is
     * quietly created, initialized with defaults, and returned.
     * </p>
     * <p>
     * <strong>NOTE:</strong> As of Eclipse 3.1 this method is
     * no longer referring to the core runtime compatibility layer and so
     * plug-ins relying on Plugin#initializeDefaultPreferences
     * will have to access the compatibility layer themselves.
     * </p>
     *
     * @return the preference store 
     */
    public IPreferenceStore getPreferenceStore() {
        // Create the preference store lazily.
        if (preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(new InstanceScope(),getBundle().getSymbolicName());
        }
        return preferenceStore;
    }

}

/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
