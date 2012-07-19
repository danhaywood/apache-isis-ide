package com.halware.nakedide.eclipse.ext.annot.objectspec;

import java.util.ArrayList;
import java.util.List;

import com.halware.nakedide.eclipse.ext.annot.descriptors.AggregatedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.BoundedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DescribedAsMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.HiddenMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ImmutableMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MaskMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NamedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ObjectTypeMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.PluralMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ValueMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ViewModelMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;

public final class NakedObjectSpecViewStandardDescriptorsProvider implements IMetadataDescriptorsProvider {

	public List<MetadataDescriptor> getDescriptors() {
		
		List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
		
		descriptors.add(new ObjectTypeMetadataDescriptor());
		descriptors.add(new NamedMetadataDescriptor());
		descriptors.add(new PluralMetadataDescriptor());
		descriptors.add(new BoundedMetadataDescriptor());
		descriptors.add(new ImmutableMetadataDescriptor());
		descriptors.add(new HiddenMetadataDescriptor());
		descriptors.add(new ValueMetadataDescriptor());
		descriptors.add(new MaskMetadataDescriptor());
		descriptors.add(new ViewModelMetadataDescriptor());
        descriptors.add(new AggregatedMetadataDescriptor());
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
