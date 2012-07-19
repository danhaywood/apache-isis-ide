package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

/**
 * Provides a collection of {@link NakedObjectProperty}s bsaed on the supplied
 * {@link ICompilationUnitOwner}.
 * 
 */
public abstract class AbstractNodesContentProvider implements IStructuredContentProvider {

	public abstract Logger getLOGGER();

	public AbstractNodesContentProvider(ICompilationUnitOwner compilationUnitOwner) {
		this.compilationUnitOwner = compilationUnitOwner;
	}

	private final ICompilationUnitOwner compilationUnitOwner;
	public ICompilationUnitOwner getCompilationUnitOwner() {
		return compilationUnitOwner;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object parent) {
		CompilationUnit parsedCompilationUnit = (CompilationUnit)parent;
		
		TypeDeclaration typeDeclaration = AstUtils.determineTypeDeclaration(parsedCompilationUnit);
        if (typeDeclaration == null) {
            return new Object[]{};
        }
		return doGetElements(typeDeclaration);
	}

    /**
     * @param typeDeclaration - guaranteed to be non-<tt>null</tt>.
     * @return
     */
	protected abstract Object[] doGetElements(TypeDeclaration typeDeclaration); 


	public void dispose() {
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
