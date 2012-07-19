package com.halware.nakedide.eclipse.core.launches;

import org.apache.log4j.Logger;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.halware.eclipseutil.util.ReflectionUtils;

public class NakedObjectsMainTab extends JavaMainTab {

    private final static Logger LOGGER = Logger.getLogger(NakedObjectsMainTab.class);
    public static Logger getLOGGER() {
        return LOGGER;
    }
    
    @SuppressWarnings("restriction")
    @Override
    public void createControl(
            Composite parent) {
        super.createControl(parent);
        fMainText.setEnabled(false);
        try {
            Button button;
            button = (Button) ReflectionUtils.getField(JavaMainTab.class, this, "fSearchExternalJarsCheckButton");
            button.setEnabled(false);
            button = (Button) ReflectionUtils.getField(JavaMainTab.class, this, "fConsiderInheritedMainButton");
            button.setEnabled(false);
        } catch (Exception e) {
            getLOGGER().warn("Unable to disable search in external jars check button");
        }
    }
    
    @Override
    public void setDefaults(
            ILaunchConfigurationWorkingCopy configuration) {
        super.setDefaults(configuration);
        LaunchUtils.setLaunchConfigurationMainTypeDefault(configuration);
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
