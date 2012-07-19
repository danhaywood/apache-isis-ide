package com.halware.nakedide.eclipse.core.launches;

import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

@SuppressWarnings("restriction")
public final class LaunchConstants {
    
    private LaunchConstants() {}
    
    private static String getPackage() {
        return LaunchConstants.class.getPackage().getName();
    }

    /**
     * Value for attribute to store in {@link LaunchConfiguration};
     * set up as a default {@link IJavaLaunchConfigurationConstants#ATTR_MAIN_TYPE_NAME} 
     * by {@link NakedObjectsMainTab}.
     */
    public static final String MAIN_CLASS_NAKEDOBJECTS_BOOT = "org.nakedobjects.nof.boot.NakedObjects";


    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--type</tt> argument.
     * 
     * <p>
     * Valid values are <tt>exploration</tt>, <tt>prototype</tt>,
     * <tt>standalone</tt> and <tt>client</tt>.   
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_TYPE = getPackage() + ".launchConfigurationAttrArgType";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_TYPE
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_EXPLORATION = "exploration";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_TYPE
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_PROTOTYPE = "prototype";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_TYPE
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_STANDALONE = "standalone";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_TYPE
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_CLIENT = "client";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_TYPE
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_SERVER = "server";

    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--user</tt> argument.
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_USER = getPackage() + "launchConfigurationAttrArgUser";

    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--password</tt> argument.
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PASSWORD = getPackage() + "launchConfigurationAttrArgPassword";

    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--images</tt> argument.
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_RESOURCE_BASE_DIR = getPackage() + ".launchConfigurationAttrArgImagesDir";


    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--viewer</tt> argument.
     * 
     * <p>
     * Valid values are <tt>dnd</tt>, <tt>web</tt>, <tt>cli</tt>
     * and <tt>cli-awt</tt>.   
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER = getPackage() + ".launchConfigurationAttrArgViewer";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_DND = "dnd";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_HTML = "html";
    
    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_CLI = "cli";
    
    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_CLI_AWT = "cli-awt";
    
    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--persistor</tt> argument.
     * 
     * <p>
     * Valid values include <tt>in-memory</tt>, <tt>xml</tt> and
     * <tt>sql</tt>.  
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR = getPackage() + ".launchConfigurationAttrArgPersistor";

    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--connector</tt> argument.
     * 
     * <p>
     * Valid values are <tt>xstream</tt>, <tt>serializing</tt> and
     * <tt>telnet</tt>.  
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR = getPackage() + ".launchConfigurationAttrArgConnector";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_IN_MEMORY = "in-memory";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_XML = "xml";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_HIBERNATE = "hibernate";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_SQL = "sql";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_CACHE = "cache";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_SERIALIZING = "serializing";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_BYTE_ENCODED = "byte-encoded";

    /**
     * @see #LAUNCH_CONFIGURATION_ATTR_CONNECTOR
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_XSTREAM = "xstream";
    
    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing any additional command line arguments.
     * 
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_ADDITIONAL = getPackage() + ".launchConfigurationAttrArgAdditional";
    
    /**
     * Key for attribute to store in {@link LaunchConfiguration}, 
     * representing the <tt>--fixture</tt> argument.
     * 
     */
    public final static String LAUNCH_CONFIGURATION_ATTR_ARG_FIXTURE = getPackage() + ".launchConfigurationAttrArgFixture";

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
