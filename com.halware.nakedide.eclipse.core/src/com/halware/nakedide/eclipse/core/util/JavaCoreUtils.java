package com.halware.nakedide.eclipse.core.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

public final class JavaCoreUtils {
    
    private JavaCoreUtils() {}


    public final static List<IClasspathEntry> getSourceFolders(IJavaProject javaProject) throws JavaModelException { 
        IClasspathEntry[] classpathEntries = javaProject.getResolvedClasspath(false);
        List<IClasspathEntry> sourceFolders = new ArrayList<IClasspathEntry>(); 
        for(IClasspathEntry classpathEntry: classpathEntries) {
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                sourceFolders.add(classpathEntry);
            }
        }
        return sourceFolders;
    }

    /**
     * Returns the first source folder (if any).
     * 
     * @param javaProject
     * @return
     * @throws JavaModelException
     */
    public final static IClasspathEntry getFirstSourceFolder(IJavaProject javaProject) throws JavaModelException {
        List<IClasspathEntry> sourceFolders = getSourceFolders(javaProject);
        return sourceFolders.size() > 0? sourceFolders.get(0): null;
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
