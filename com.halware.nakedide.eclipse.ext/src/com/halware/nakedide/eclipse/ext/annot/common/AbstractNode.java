package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Common to an object spec, or to any member.
 * 
 */
public abstract class AbstractNode<T extends ASTNode> {

	public abstract Logger getLOGGER();

	public AbstractNode(
			ICompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	private final ICompilationUnit compilationUnit;
	public ICompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
	
	public abstract T getDeclaration();


	public String toString() {
		return compilationUnit.getElementName();
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
