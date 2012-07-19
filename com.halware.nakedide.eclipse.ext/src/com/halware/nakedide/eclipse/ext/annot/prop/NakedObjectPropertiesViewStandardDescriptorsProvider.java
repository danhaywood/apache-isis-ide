package com.halware.nakedide.eclipse.ext.annot.prop;

import java.util.ArrayList;
import java.util.List;

import com.halware.nakedide.eclipse.ext.annot.descriptors.DescribedAsMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DisabledMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.HiddenMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MemberOrderMemberNameMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MemberOrderMemberSequenceMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NamedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NotPersistedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.OptionalMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.TitleAbbreviatedToMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.TitleAppendMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.TitlePrependMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.TitleSequenceMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.TypicalLengthMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;

public final class NakedObjectPropertiesViewStandardDescriptorsProvider implements IMetadataDescriptorsProvider {


    public List<MetadataDescriptor> getDescriptors() {
		
		List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
		
		descriptors.add(new PropertyMetadataDescriptor());
		descriptors.add(new PropertyReturnTypeMetadataDescriptor());
		descriptors.add(new MemberOrderMemberSequenceMetadataDescriptor());
		descriptors.add(new MemberOrderMemberNameMetadataDescriptor());
		descriptors.add(new TitleSequenceMetadataDescriptor());
		descriptors.add(new TitlePrependMetadataDescriptor());
		descriptors.add(new TitleAppendMetadataDescriptor());
		descriptors.add(new TitleAbbreviatedToMetadataDescriptor());
        descriptors.add(new TypicalLengthMetadataDescriptor());
        descriptors.add(new DisabledMetadataDescriptor());
		descriptors.add(new OptionalMetadataDescriptor());
        descriptors.add(new NotPersistedMetadataDescriptor());
        descriptors.add(new HiddenMetadataDescriptor());
        descriptors.add(new NamedMetadataDescriptor());
		descriptors.add(new DescribedAsMetadataDescriptor());

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
