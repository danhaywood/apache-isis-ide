package com.halware.nakedide.eclipse.ext.annot.action.params;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;

import com.halware.nakedide.eclipse.ext.annot.descriptors.DescribedAsMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MaskMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MaxLengthMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MultiLineMemberNumberOfLinesMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MultiLineMemberPreventWrappingMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NamedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.OptionalMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.RegexMemberCaseSensitiveMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.RegexMemberFormatMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.RegexMemberValidationMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.TypicalLengthMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

public final class NakedObjectActionParametersViewStandardDescriptorsProvider implements IMetadataDescriptorsProvider {

	public List<MetadataDescriptor> getDescriptors() {
		
		List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
		
		descriptors.add(
			new MetadataDescriptor("Parameter",
				new IEvaluator(){
					public Object evaluate(Object object) {
						NakedObjectActionParameter nakedObjectActionParameter = (NakedObjectActionParameter)object;
						return nakedObjectActionParameter.getParameterName();
					}
                    public MetadataDescriptorKind<? extends CellEditor> getKind() {
                        return MetadataDescriptorKind.STRING;
                    }
				}, null
			));

		descriptors.add(
				new MetadataDescriptor("Type",
					new IEvaluator(){
						public Object evaluate(Object object) {
							NakedObjectActionParameter nakedObjectActionParameter = (NakedObjectActionParameter)object;
							return nakedObjectActionParameter.getType();
						}
                        public MetadataDescriptorKind<? extends CellEditor> getKind() {
                            return MetadataDescriptorKind.STRING;
                        }
					}, null
				));

		descriptors.add(new NamedMetadataDescriptor());
		descriptors.add(new OptionalMetadataDescriptor());
		descriptors.add(new TypicalLengthMetadataDescriptor());
        descriptors.add(new MaxLengthMetadataDescriptor());
		descriptors.add(new MultiLineMemberNumberOfLinesMetadataDescriptor());
        descriptors.add(new MultiLineMemberPreventWrappingMetadataDescriptor());
        descriptors.add(new MaskMetadataDescriptor());
        descriptors.add(new RegexMemberValidationMetadataDescriptor());
        descriptors.add(new RegexMemberFormatMetadataDescriptor());
        descriptors.add(new RegexMemberCaseSensitiveMetadataDescriptor());
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
