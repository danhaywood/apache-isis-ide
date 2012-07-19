package com.halware.nakedide.eclipse.ext.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.halware.nakedide.eclipse.core.NakedObjectsCore;
import com.halware.nakedide.eclipse.ext.builders.Constants;

public final class NatureUtil {
    private NatureUtil() {}

    /**
     * Adds given nature as first nature to specified project.
     */
    public static void addProjectNature(
            IProject project, String nature,
            IProgressMonitor monitor) throws CoreException {
        if (project != null && nature != null) {
            if (!project.hasNature(nature)) {
                IProjectDescription desc = project.getDescription();
                String[] oldNatures = desc.getNatureIds();
                String[] newNatures = new String[oldNatures.length + 1];
                newNatures[0] = nature;
                if (oldNatures.length > 0) {
                    System.arraycopy(oldNatures, 0, newNatures, 1,
                            oldNatures.length);
                }
                desc.setNatureIds(newNatures);
                project.setDescription(desc, monitor);
            }
        }
    }

    /**
     * Removes given nature from specified project.
     */
    public static void removeProjectNature(IProject project, String nature,
            IProgressMonitor monitor) throws CoreException {
        if (project != null && nature != null) {
            if (project.exists() && project.hasNature(nature)) {
    
                // first remove all problem markers (including the
                // inherited ones) from Spring beans project
                if (nature.equals(Constants.PROBLEM_MARKER)) {
                    project.deleteMarkers(Constants.PROBLEM_MARKER, true,
                            IResource.DEPTH_INFINITE);
                }
    
                // now remove project nature
                IProjectDescription desc = project.getDescription();
                String[] oldNatures = desc.getNatureIds();
                String[] newNatures = new String[oldNatures.length - 1];
                int newIndex = oldNatures.length - 2;
                for (int i = oldNatures.length - 1; i >= 0; i--) {
                    if (!oldNatures[i].equals(nature)) {
                        newNatures[newIndex--] = oldNatures[i];
                    }
                }
                desc.setNatureIds(newNatures);
                project.setDescription(desc, monitor);
            }
        }
    }

    /**
     * Returns true if given resource is a Spring project.
     */
    public static boolean isNakedObjectsProject(IResource resource) {
        if (resource instanceof IProject && resource.isAccessible()) {
            try {
                return ((IProject) resource)
                        .hasNature(NakedObjectsCore.NATURE_ID);
            } catch (CoreException e) {
                NakedObjectsCore.log(e);
            }
        }
        return false;
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
