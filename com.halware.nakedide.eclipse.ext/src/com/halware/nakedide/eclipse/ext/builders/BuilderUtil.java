package com.halware.nakedide.eclipse.ext.builders;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.halware.nakedide.eclipse.core.NakedObjectsCore;

public final class BuilderUtil {
    private BuilderUtil() {}

    /**
     * Removes given builder from specified project.
     */
    public static void removeProjectBuilder(IProject project, String builder,
            IProgressMonitor monitor) throws CoreException {
        if (project != null && builder != null) {
            IProjectDescription desc = project.getDescription();
            ICommand[] commands = desc.getBuildSpec();
            for (int i = commands.length - 1; i >= 0; i--) {
                if (commands[i].getBuilderName().equals(builder)) {
                    ICommand[] newCommands = new ICommand[commands.length - 1];
                    System.arraycopy(commands, 0, newCommands, 0, i);
                    System.arraycopy(commands, i + 1, newCommands, i,
                            commands.length - i - 1);
                    // Commit the spec change into the project
                    desc.setBuildSpec(newCommands);
                    project.setDescription(desc, monitor);
                    break;
                }
            }
        }
    }

    /**
     * Removes all Spring problem markers (including the inherited ones) from
     * given resource.
     */
    public static void deleteProblemMarkers(IResource resource) {
        if (resource != null && resource.isAccessible()) {
            try {
                resource.deleteMarkers(Constants.PROBLEM_MARKER, true,
                        IResource.DEPTH_ZERO);
            } catch (CoreException e) {
                NakedObjectsCore.log(e);
            }
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
