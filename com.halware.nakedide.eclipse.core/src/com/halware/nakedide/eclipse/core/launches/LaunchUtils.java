package com.halware.nakedide.eclipse.core.launches;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public final class LaunchUtils {
    
    private static final String DEFAULT_APPLICATION_PROJECT_NAME = "com.mycompany.myapp";

    private LaunchUtils() {}

	
    static void performApply(
            StringBuilder argumentsBuffer,
            String longOpt,
            Text text,
            ILaunchConfigurationWorkingCopy configuration,
            String attribute) {
        String textVal = text.getText();
        if (textVal.length() == 0) {
            return;
        }
        configuration.setAttribute(attribute, textVal);
        LaunchUtils.append(argumentsBuffer, longOpt);
        boolean aDflag = longOpt.endsWith("=");
        boolean addSpace = !aDflag; // don't add a space if -D xxx=yyy
        LaunchUtils.append(argumentsBuffer, textVal, addSpace);
    }

    static void performApply(
            StringBuilder argumentsBuffer,
            String longOpt,
            List<Button> buttons, 
            ILaunchConfigurationWorkingCopy configuration, String attribute) {
        boolean addedArg = false;
        for(Button button: buttons) {
            if (button.getSelection()) {
                String data = LaunchUtils.getData(button);
                
                configuration.setAttribute(
                    attribute, data);
                if (!addedArg) {
                    LaunchUtils.append(argumentsBuffer, longOpt);
                    addedArg = true;
                }
                LaunchUtils.append(argumentsBuffer, data);
            }
        }
    }

    static void performApply(
            Text text,
            StringBuilder argumentsBuffer) {
        String textVal = text.getText();
        if (textVal.length() == 0) {
            return;
        }
        LaunchUtils.append(argumentsBuffer, textVal);
    }

    public static void setLaunchConfigurationMainTypeDefault(
            ILaunchConfigurationWorkingCopy workingCopy) {
        workingCopy.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                LaunchConstants.MAIN_CLASS_NAKEDOBJECTS_BOOT);
        workingCopy.setAttribute(
                "org.eclipse.jdt.debug.ui.INCLUDE_EXTERNAL_JARS", true);
    }
    
    public static void setLaunchConfigurationResourceBaseDefault(
            ILaunchConfigurationWorkingCopy workingCopy, String applicationProjectName) {
        if (applicationProjectName == null) {
            applicationProjectName = DEFAULT_APPLICATION_PROJECT_NAME;
        }
        workingCopy.setAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_RESOURCE_BASE_DIR,
                String.format("${workspace_loc:%s/%s}", applicationProjectName, "src/main/resources"));
    }
    


    static StringBuilder append(
            StringBuilder buf,
            String data) {
        return append(buf, data, true);
    }

    static StringBuilder append(
            StringBuilder buf,
            String data, boolean addSpace) {
        if (data == null || data.length() == 0) { 
            return buf;
        }
        if (addSpace) {
            buf.append(" ");
        }
        buf.append(data);
        return buf;
    }

    static String getData(
            Control control) {
        return (String)control.getData();
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
