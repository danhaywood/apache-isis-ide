package com.halware.nakedide.eclipse.ext.annot.prop;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.halware.nakedide.eclipse.core.util.MethodUtils;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractMember;

public class NakedObjectProperty extends AbstractMember {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectProperty.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}


	public NakedObjectProperty(
			ICompilationUnit compilationUnit, 
			String accessorMethodName, 
			MethodDeclaration methodDeclaration) {
		super(compilationUnit, methodDeclaration);
		this.accessorMethodName = accessorMethodName;
	}

	private final String accessorMethodName;
	public String getAccessorMethodName() {
		return accessorMethodName;
	}


	public String toString() {
		return getAccessorMethodName();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null || obj.getClass() != this.getClass()) { return false; }
		return equals((NakedObjectProperty)obj);
	}

	public boolean equals(NakedObjectProperty obj) {
		return getCompilationUnit() == obj.getCompilationUnit() &&
		       getDeclaration() == obj.getDeclaration();
	}

	@Override
	public int hashCode() {
		return getAccessorMethodName().hashCode();
	}


    public boolean isValueType() {
        MethodDeclaration declaration = getDeclaration();
        Type returnType2 = declaration.getReturnType2();
        if (returnType2.isPrimitiveType()) {
            return true;
        }
        if (MethodUtils.isBuiltInValueType(declaration)) {
            return true;
        }
        return false;
    }


    public boolean isStringType() {
        MethodDeclaration declaration = getDeclaration();
        if (MethodUtils.isStringValueType(declaration)) {
            return true;
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
