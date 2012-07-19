package com.halware.nakedide.eclipse.ext.builders;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.halware.nakedide.eclipse.ext.nature.NatureUtil;

public class NakedObjectsIncrementalProjectBuilder 
				extends IncrementalProjectBuilder {

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		
		IProject project = getProject();
		IResourceDelta delta = 
			kind != FULL_BUILD ? getDelta(project) : null;
			
		if (delta == null || kind == FULL_BUILD) {
			if (NatureUtil.isNakedObjectsProject(project)) {
			    project.accept(new ResourceVisitor(monitor));
			}
		} else {
			delta.accept(new ResourceDeltaVisitor(monitor));
		}

		return null;
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
