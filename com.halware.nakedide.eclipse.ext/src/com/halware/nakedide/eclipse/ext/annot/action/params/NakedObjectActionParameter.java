package com.halware.nakedide.eclipse.ext.annot.action.params;


import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.halware.nakedide.eclipse.ext.annot.action.NakedObjectAction;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

public class NakedObjectActionParameter extends AbstractNode<SingleVariableDeclaration> {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectActionParameter.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}


	public NakedObjectActionParameter(
			NakedObjectAction nakedObjectAction, 
			SingleVariableDeclaration parameterDeclaration) {
		super(nakedObjectAction.getCompilationUnit());
		this.nakedObjectAction = nakedObjectAction;
		this.parameterDeclaration = parameterDeclaration;
	}

	private final NakedObjectAction nakedObjectAction;
	public NakedObjectAction getNakedObjectAction() {
		return nakedObjectAction;
	}

	private final SingleVariableDeclaration parameterDeclaration;
	public SingleVariableDeclaration getDeclaration() {
		return parameterDeclaration;
	}
	
	public String getParameterName() {
		return parameterDeclaration.getName().getFullyQualifiedName();	
	}

	
	public String getType() {
		return AstUtils.asString(parameterDeclaration.getType());
	}


	public String toString() {
		return getParameterName();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null || obj.getClass() != this.getClass()) { return false; }
		return equals((NakedObjectActionParameter)obj);
	}

	public boolean equals(NakedObjectActionParameter obj) {
		return getCompilationUnit() == obj.getCompilationUnit() &&
		       getDeclaration() == obj.getDeclaration();
	}

	@Override
	public int hashCode() {
		return getParameterName().hashCode();
	}


	
    public boolean isStringType() {
    	SingleVariableDeclaration declaration = getDeclaration();
    	Type type = declaration.getType();
    	ITypeBinding typeBinding = type.resolveBinding();
    	if (typeBinding == null) {
    		// java.lang.String should always resolve.
    		return false;
    	}
    	String qualifiedName = typeBinding.getQualifiedName();
        return "java.lang.String".equals(qualifiedName);
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
