package com.halware.nakedide.eclipse.ext.annot.objectspec;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodesContentProvider;
import com.halware.nakedide.eclipse.ext.annot.common.ICompilationUnitOwner;

/**
 * Provides a collection of {@link NakedObjectSpec}s bsaed on the supplied
 * {@link ICompilationUnitOwner}.
 * 
 */
public class NakedObjectSpecContentProvider 
		extends AbstractNodesContentProvider {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectSpecContentProvider.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}

	public NakedObjectSpecContentProvider(ICompilationUnitOwner compilationUnitOwner) {
		super(compilationUnitOwner);
	}

	protected Object[] doGetElements(TypeDeclaration typeDeclaration) {

		return new Object[] {
			new NakedObjectSpec(
					getCompilationUnitOwner().getCompilationUnit(), typeDeclaration)
		};
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
