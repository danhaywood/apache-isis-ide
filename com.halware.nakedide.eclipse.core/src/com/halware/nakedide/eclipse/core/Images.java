/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.halware.nakedide.eclipse.core;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;


public class Images {

	private static final IPath ICONS_PATH= new Path("$nl$/icons"); //$NON-NLS-1$

	
	//---- Helper methods to access icons on the file system --------------------------------------

	public static void setImageDescriptors(IAction action, String type) {
		ImageDescriptor id= create("d", type); //$NON-NLS-1$
		if (id != null)
			action.setDisabledImageDescriptor(id);
	
		id= create("c", type); //$NON-NLS-1$
		if (id != null) {
			action.setHoverImageDescriptor(id);
			action.setImageDescriptor(id);
		} else {
			action.setImageDescriptor(ImageDescriptor.getMissingImageDescriptor());
		}
	}
	
	public static ImageDescriptor create(String prefix, String name) {
		IPath path= ICONS_PATH.append(prefix).append(name);
		return createImageDescriptor(Activator.getDefault().getBundle(), path);
	}
	
	public static ImageDescriptor create(String name) {
		IPath path= ICONS_PATH.append(name);
		return createImageDescriptor(Activator.getDefault().getBundle(), path);
	}
	
	/*
	 * Since 3.1.1. Load from icon paths with $NL$
	 */
	public static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path) {
		URL url= FileLocator.find(bundle, path, null);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
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
