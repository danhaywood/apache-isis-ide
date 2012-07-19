package com.halware.nakedide.eclipse.ext.annot.coll;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.halware.nakedide.eclipse.core.util.MethodNameUtils;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodesContentProvider;
import com.halware.nakedide.eclipse.ext.annot.common.ICompilationUnitOwner;

/**
 * Provides a collection of {@link NakedObjectCollection}s bsaed on the supplied
 * {@link ICompilationUnitOwner}.
 * 
 */
public class NakedObjectCollectionsContentProvider 
		extends AbstractNodesContentProvider {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectCollectionsContentProvider.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}

	public NakedObjectCollectionsContentProvider(ICompilationUnitOwner compilationUnitOwner) {
		super(compilationUnitOwner);
	}

	protected Object[] doGetElements(TypeDeclaration typeDeclaration) {
		
		Map<String,NakedObjectCollection> membersByName = new TreeMap<String,NakedObjectCollection>();
		for(Object bodyDeclarationObj: typeDeclaration.bodyDeclarations()) {
			
			BodyDeclaration bodyDeclaration = (BodyDeclaration) bodyDeclarationObj;
			if (!(bodyDeclaration instanceof MethodDeclaration)) {
				continue;
			}
			
			MethodDeclaration methodDeclaration = (MethodDeclaration)bodyDeclaration;
			String collectionName = MethodNameUtils.asCollectionName(methodDeclaration);
			if (collectionName == null) {
				continue;
			}
			
			NakedObjectCollection nakedObjectCollection = membersByName.get(collectionName);
			if (nakedObjectCollection == null) {
				nakedObjectCollection = new NakedObjectCollection(
					getCompilationUnitOwner().getCompilationUnit(), 
					collectionName, methodDeclaration);
				membersByName.put(nakedObjectCollection.getAccessorMethodName(), nakedObjectCollection);
			}
		}
		
		return membersByName.values().toArray();
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
