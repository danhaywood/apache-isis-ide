/**
 * 
 */
package com.halware.nakedide.eclipse.ext.builders;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResourceVisitor implements IResourceVisitor {
	
	public ResourceVisitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	private ResourceValidator resourceValidator = new ResourceValidator();

	@SuppressWarnings("unused")
	private IProgressMonitor monitor;

	public boolean visit(IResource resource) {
		resourceValidator.validate(resource);
		return true;
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
