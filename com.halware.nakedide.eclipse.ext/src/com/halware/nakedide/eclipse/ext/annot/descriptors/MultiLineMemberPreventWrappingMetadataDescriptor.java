package com.halware.nakedide.eclipse.ext.annot.descriptors;

import com.halware.nakedide.eclipse.ext.annot.ast.MultipleValueAnnotationSingleMemberEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;
import com.halware.nakedide.eclipse.ext.annot.mdd.StringSpecificMetadataDescriptor;

public class MultiLineMemberPreventWrappingMetadataDescriptor extends StringSpecificMetadataDescriptor {

	public MultiLineMemberPreventWrappingMetadataDescriptor() {
		super(
            "ML#preventWrapping", 
            new MultipleValueAnnotationSingleMemberEvaluatorAndModifier(
				"org.apache.isis.applib.annotation.MultiLine", 
				"preventWrapping",
                MetadataDescriptorKind.BOOLEAN)
        );
        
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
