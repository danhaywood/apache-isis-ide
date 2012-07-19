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
package com.halware.nakedide.eclipse.ext.annot.utils;


import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

/**
 *
 */
public class JdtUtil {
	private JdtUtil() {
		super();
	}

	public static IOpenable getJavaInput(IEditorPart part) {
		IEditorInput editorInput= part.getEditorInput();
		if (editorInput != null) {
			IJavaElement input= javaUIgetEditorInputJavaElement(editorInput);
			if (input instanceof IOpenable) {
				return (IOpenable) input;
			}
		}
		return null;	
	}

	/**
	 * Note: This is an inlined version of {@link JavaUI#getEditorInputJavaElement(IEditorInput)},
	 * which is not available in 3.1.
	 */
	private static IJavaElement javaUIgetEditorInputJavaElement(IEditorInput editorInput) {
		Assert.isNotNull(editorInput);
		IJavaElement je= JavaUI.getWorkingCopyManager().getWorkingCopy(editorInput); 
		if (je != null)
			return je;
		
		/*
		 * This needs works, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=120340
		 */
		return (IJavaElement)editorInput.getAdapter(IJavaElement.class);
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
