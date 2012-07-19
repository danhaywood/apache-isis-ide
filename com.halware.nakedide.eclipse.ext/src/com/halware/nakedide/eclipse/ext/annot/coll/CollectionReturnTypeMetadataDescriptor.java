package com.halware.nakedide.eclipse.ext.annot.coll;

import org.eclipse.jface.viewers.CellEditor;

import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

public class CollectionReturnTypeMetadataDescriptor extends MetadataDescriptor {

	public CollectionReturnTypeMetadataDescriptor() {
		super(
            "Type", 
			new IEvaluator() {
    			public Object evaluate(Object object) {
    				NakedObjectCollection nakedObjectCollection = (NakedObjectCollection)object;
    				String returnTypeName = nakedObjectCollection.getReturnTypeName();
    				return returnTypeName != null? returnTypeName: "void";
    			}

                public MetadataDescriptorKind<? extends CellEditor> getKind() {
                    return MetadataDescriptorKind.STRING;
                }
    		}, null);
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
