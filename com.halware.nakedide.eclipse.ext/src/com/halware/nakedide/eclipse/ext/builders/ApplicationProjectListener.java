package com.halware.nakedide.eclipse.ext.builders;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.halware.nakedide.eclipse.core.IApplicationProjectListener;
import com.halware.nakedide.eclipse.core.NakedObjectsCore;
import com.halware.nakedide.eclipse.ext.nature.NatureUtil;

public class ApplicationProjectListener implements IApplicationProjectListener {

    private final static Logger LOGGER = Logger.getLogger(ApplicationProjectListener.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

    public boolean configure(IProject pluginProject,
            IConfigurationElement config) {
        try {
            IProject javaProject = pluginProject.getProject();
            NatureUtil.addProjectNature(javaProject,
                    NakedObjectsCore.NATURE_ID, new NullProgressMonitor());
            BasicNewProjectResourceWizard.updatePerspective(config);
            return true;
        } catch (CoreException e) {
            getLOGGER().error("Unable to add NakedObjects nature - skipping");
            return false;
        }
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
