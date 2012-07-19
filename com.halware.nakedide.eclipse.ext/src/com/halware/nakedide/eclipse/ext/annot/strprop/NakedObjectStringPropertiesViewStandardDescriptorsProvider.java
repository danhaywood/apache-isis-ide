package com.halware.nakedide.eclipse.ext.annot.strprop;

import java.util.ArrayList;
import java.util.List;

import com.halware.nakedide.eclipse.ext.annot.descriptors.MaskMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MaxLengthMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MultiLineMemberNumberOfLinesMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MultiLineMemberPreventWrappingMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.RegexMemberCaseSensitiveMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.RegexMemberFormatMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.RegexMemberValidationMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.prop.PropertyMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.prop.PropertyReturnTypeMetadataDescriptor;

public final class NakedObjectStringPropertiesViewStandardDescriptorsProvider implements IMetadataDescriptorsProvider {

	public List<MetadataDescriptor> getDescriptors() {
		
		List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
		
		descriptors.add(new PropertyMetadataDescriptor());
		descriptors.add(new PropertyReturnTypeMetadataDescriptor());
        descriptors.add(new MaxLengthMetadataDescriptor());
        descriptors.add(new MultiLineMemberNumberOfLinesMetadataDescriptor());
        descriptors.add(new MultiLineMemberPreventWrappingMetadataDescriptor());
        descriptors.add(new MaskMetadataDescriptor());
        descriptors.add(new RegexMemberValidationMetadataDescriptor());
        descriptors.add(new RegexMemberFormatMetadataDescriptor());
        descriptors.add(new RegexMemberCaseSensitiveMetadataDescriptor());

		return descriptors;
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
