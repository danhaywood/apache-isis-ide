package com.halware.nakedide.eclipse.core.launches;

//import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;

@SuppressWarnings("restriction")
public class LaunchConfigurationTabGroup 
    extends AbstractLaunchConfigurationTabGroup {

    public LaunchConfigurationTabGroup() {
    }
    
    /**
     * @see ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
     */
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
            new NakedObjectsMainTab(),
            new NakedObjectsArgumentsTab(),
            new JavaJRETab(),
            new JavaClasspathTab(),
            //new SourceLookupTab(),  // TODO: complained when left this in.
            new EnvironmentTab(),
            new CommonTab()
        };
        setTabs(tabs);
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
