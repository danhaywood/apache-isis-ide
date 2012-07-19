package com.halware.nakedide.eclipse.core;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;


/**
 * Derived from equivalent Spring IDE.
 */
public class NakedObjectsCore extends Plugin {

	/**
	 * Plugin identifier for NO Core.
	 */
	public static final String PLUGIN_ID = NakedObjectsCore.class.getPackage().getName();


    /**
     * The identifier for the Naked Objects nature.
     * 
     * <p>
     * The presence of this nature on a project indicates that it is 
     * NO-capable.
     * 
     * <p>
     * Even though this is defined in IDE, it is only enabled by IDE PE.
     * It exists here so that the AbstractProjectWizard can filter more effectively.
     *
     * @see org.eclipse.core.resources.IProject#hasNature(java.lang.String)
     */
    public static final String NATURE_ID = 
        PLUGIN_ID + ".nature";


	private static final String RESOURCE_NAME = PLUGIN_ID + ".messages";

	/** The shared instance */
	private static NakedObjectsCore plugin; 

	/** Resource bundle */
	private ResourceBundle resourceBundle;

	/**
	 * Creates the NakedObjects core plug-in.
	 * <p>
	 * The plug-in instance is created automatically by the Eclipse platform.
	 * Clients must not call.
	 */
	public NakedObjectsCore() {
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
		} catch (MissingResourceException e) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the single instance of the Spring core plug-in runtime class.
	 * 
	 * @return the single instance of the Spring core plug-in runtime class
	 */
	public static NakedObjectsCore getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
	    String bundleString;
		ResourceBundle bundle = getDefault().getResourceBundle();
		if (bundle != null) {
			try {
				bundleString = bundle.getString(key);
			} catch (MissingResourceException e) {
			    log(e);
				bundleString = "!" + key + "!";
			}
		} else {
			bundleString = "!" + key + "!";
		}
		return bundleString;
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static boolean isDebug(String option) {
		String value = Platform.getDebugOption(option);
		return (value != null && value.equalsIgnoreCase("true") ? true : false);
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/**
	 * Writes the message to the plug-in's log
	 * 
	 * @param message the text to write to the log
	 */
	public static void log(String message, Throwable exception) {
		IStatus status = createErrorStatus(message, exception);
		getDefault().getLog().log(status);
	}
	
	public static void log(Throwable exception) {
		getDefault().getLog().log(createErrorStatus(
						getResourceString("Plugin.internal_error"), exception));
	}

	/**
	 * Returns a new <code>IStatus</code> with status "ERROR" for this plug-in.
	 */
	public static IStatus createErrorStatus(String message,
											Throwable exception) {
		if (message == null) {
			message = ""; 
		}		
		return new Status(Status.ERROR, PLUGIN_ID, 0, message, exception);
	}

	public static String getFormattedMessage(String key, String arg) {
		return getFormattedMessage(key, new String[] { arg });
	}

	public static String getFormattedMessage(String key, String[] args) {
		return MessageFormat.format(getResourceString(key), (Object[])args);
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
