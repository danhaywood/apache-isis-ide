package com.halware.nakedide.eclipse.ext.annot.action.params;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.action.NakedObjectAction;
import com.halware.nakedide.eclipse.ext.annot.common.ICompilationUnitOwner;

/**
 * Provides a collection of {@link NakedObjectActionParameter}s based on the supplied
 * {@link ICompilationUnitOwner}.
 * 
 */
public class NakedObjectActionParametersContentProvider 
implements IStructuredContentProvider {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectActionParametersContentProvider.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

	private NakedObjectAction nakedObjectAction; 
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		nakedObjectAction = (NakedObjectAction)newInput;
	}

	public Object[] getElements(Object parent) {
		getLOGGER().debug("getElements(parent); parent=>>" + parent + "<<");
		
		List<NakedObjectActionParameter> parameterList = new ArrayList<NakedObjectActionParameter>(); 
		MethodDeclaration methodDeclaration = nakedObjectAction.getDeclaration();
		List<SingleVariableDeclaration> parameterDeclarations = Generics.asT(methodDeclaration.parameters());
		for(SingleVariableDeclaration parameterDeclaration: parameterDeclarations) {
			NakedObjectActionParameter parameter = 
				new NakedObjectActionParameter(nakedObjectAction, parameterDeclaration);
			parameterList.add(parameter);
		}
		return parameterList.toArray();
	}

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
