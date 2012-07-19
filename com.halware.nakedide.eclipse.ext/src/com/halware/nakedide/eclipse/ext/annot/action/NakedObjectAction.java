package com.halware.nakedide.eclipse.ext.annot.action;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractMember;

public class NakedObjectAction extends AbstractMember {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectAction.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}


	public NakedObjectAction(
			ICompilationUnit compilationUnit, 
			String actionMethodName, 
			MethodDeclaration methodDeclaration) {
		super(compilationUnit, methodDeclaration);
		this.actionMethodName = actionMethodName;
	}

	private final String actionMethodName;
	public String getActionMethodName() {
		return actionMethodName;
	}


	public String toString() {
		return getActionMethodName();
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null || obj.getClass() != this.getClass()) { return false; }
		return equals((NakedObjectAction)obj);
	}

	public boolean equals(NakedObjectAction obj) {
		return getCompilationUnit() == obj.getCompilationUnit() &&
		       getDeclaration() == obj.getDeclaration();
	}

	@Override
	public int hashCode() {
		return getActionMethodName().hashCode();
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
