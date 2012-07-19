package com.halware.nakedide.eclipse.ext.annot.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;

import com.halware.nakedide.eclipse.ext.annot.descriptors.DebugMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DescribedAsMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DisabledMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ExplorationMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.HiddenMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.IdempotentMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MemberOrderMemberNameMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MemberOrderMemberSequenceMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NamedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NotContributedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NotInServiceMenuMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.QueryOnlyMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

public final class NakedObjectActionsViewStandardDescriptorsProvider implements IMetadataDescriptorsProvider {

	public List<MetadataDescriptor> getDescriptors() {
		
		List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
		
		descriptors.add(
			new MetadataDescriptor("Action", 
				new IEvaluator(){
					public Object evaluate(Object object) {
						NakedObjectAction nakedObjectAction = (NakedObjectAction)object;
						return nakedObjectAction.getActionMethodName();
					}
                    public MetadataDescriptorKind<? extends CellEditor> getKind() {
                        return MetadataDescriptorKind.STRING;
                    }
				}, null
			));

		descriptors.add(new MemberOrderMemberSequenceMetadataDescriptor());
        descriptors.add(new MemberOrderMemberNameMetadataDescriptor());
		descriptors.add(new QueryOnlyMetadataDescriptor());
		descriptors.add(new IdempotentMetadataDescriptor());
		descriptors.add(new DisabledMetadataDescriptor());
		descriptors.add(new HiddenMetadataDescriptor());
		descriptors.add(new NotContributedMetadataDescriptor());
		descriptors.add(new NotInServiceMenuMetadataDescriptor());
        descriptors.add(new DebugMetadataDescriptor());
        descriptors.add(new ExplorationMetadataDescriptor());
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
