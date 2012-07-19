/**
 * 
 */
package com.halware.nakedide.eclipse.ext.builders;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.IProgressMonitor;

import com.halware.nakedide.eclipse.ext.nature.NatureUtil;

public class ResourceDeltaVisitor implements IResourceDeltaVisitor {
	
	public ResourceDeltaVisitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
	
	@SuppressWarnings("unused")
	private IProgressMonitor monitor;
	
	private ResourceValidator resourceValidator = new ResourceValidator();

	/**
	 * @return whether to continue visiting children of the supplied resource.
	 */
	public boolean visit(IResourceDelta resourceDelta) {

		IResource resource = resourceDelta.getResource();
		
		if (resource instanceof IProject) {
			IProject project = (IProject) resource;
			return NatureUtil.isNakedObjectsProject(project);
		}
		
		if (resource instanceof IFolder) {
			return true;
		}
		
		if (resource instanceof IFile) {
			switch (resourceDelta.getKind()) {
				case IResourceDelta.ADDED :
					// fall through
				case IResourceDelta.CHANGED :
					
					resourceValidator.validate(resource);
					return true;

				case IResourceDelta.REMOVED :
					return false;
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
