package com.halware.nakedide.eclipse.ext.annot.objectspec;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;

public class NakedObjectSpec extends AbstractNode<TypeDeclaration> {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectSpec.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}


	public NakedObjectSpec(
			ICompilationUnit compilationUnit, 
			TypeDeclaration typeDeclaration) {
		super(compilationUnit);
		this.typeDeclaration = typeDeclaration;
	}

	private final TypeDeclaration typeDeclaration;
	public TypeDeclaration getDeclaration() {
		return typeDeclaration;
	}

	public String getTypeName() {
		return typeDeclaration.getName().getFullyQualifiedName();
	}


	public String toString() {
		return getTypeName();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null || obj.getClass() != this.getClass()) { return false; }
		return equals((NakedObjectSpec)obj);
	}

	public boolean equals(NakedObjectSpec obj) {
		return getCompilationUnit() == obj.getCompilationUnit() &&
		       getDeclaration() == obj.getDeclaration();
	}

	@Override
	public int hashCode() {
		return getTypeName().hashCode();
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
