package com.halware.nakedide.eclipse.ext.builders;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.halware.nakedide.eclipse.ext.builders.checkers.MethodChecker;
import com.halware.nakedide.eclipse.ext.builders.checkers.TypeChecker;

public class ResourceValidator {

    public final static Logger LOGGER = Logger.getLogger(ResourceValidator.class);
    public static Logger getLOGGER() {
        return LOGGER;
    }
    
	public void validate(IResource resource) {
		try {
			resource.deleteMarkers(Constants.PROBLEM_MARKER, false, IResource.DEPTH_ONE);
		} catch (JavaModelException e) {
			getLOGGER().error(e);
		} catch (CoreException e) {
            getLOGGER().error(e);
		}
		if (!(resource instanceof IFile)) {
			return;
		}
		IFile file = (IFile)resource;
		validate(file);
	}

	public void validate(IFile file) {
		IJavaElement javaElement = JavaCore.create(file);
		if (!(javaElement instanceof ICompilationUnit)) {
			return;
		}
		
		ICompilationUnit compilationUnit = (ICompilationUnit)javaElement;
		validate(compilationUnit);
	}

	public void validate(ICompilationUnit compilationUnit) {
		try {
			IType[] allTypes = compilationUnit.getAllTypes();
			for(IType type: allTypes) {
				validate(type);
			}
		} catch (JavaModelException e) {
            getLOGGER().error(e);
		}
	}

	public void validate(IType type) {

		try {
			TypeChecker typeChecker = new TypeChecker(type);
			typeChecker.checkTitle();

			IMethod[] methods = type.getMethods();
			for(IMethod method: methods) {
				MethodChecker methodChecker = new MethodChecker(method);
				methodChecker.checkPrefixes();
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
