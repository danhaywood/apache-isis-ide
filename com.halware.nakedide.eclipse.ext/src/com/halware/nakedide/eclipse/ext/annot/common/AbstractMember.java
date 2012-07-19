package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Common to all of the members.
 * 
 */
public abstract class AbstractMember extends AbstractNode<MethodDeclaration> {

	public abstract Logger getLOGGER();

	public AbstractMember(
			ICompilationUnit compilationUnit, 
			MethodDeclaration methodDeclaration) {
		super(compilationUnit);
		this.methodDeclaration = methodDeclaration;
	}

	private final MethodDeclaration methodDeclaration;
	/**
	 * The significant ("lead") method for the member.
	 * 
	 * <p>
	 * For a property and collection this will be the accessor method.
	 * For an action this will be the action's method itself.  
	 *  
	 * @return
	 */
	public MethodDeclaration getDeclaration() {
		return methodDeclaration;
	}


	public IMethodBinding getMethodBinding() {
		return methodDeclaration.resolveBinding();
	}

	/**
	 * The (unqualified) name of the return type of the
	 * {@link #getMethodBinding()}.
	 */
	public String getReturnTypeName() {
		if (getMethodBinding() == null) {
			return null;
		}
		ITypeBinding returnType = getMethodBinding().getReturnType();
		return returnType != null? returnType.getName(): null;
	}

	/**
	 * Annotations (if any) of the {@link #getMethodBinding()}.
	 */
	public IAnnotationBinding[] getAnnotations() {
		return getMethodBinding().getAnnotations();
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
