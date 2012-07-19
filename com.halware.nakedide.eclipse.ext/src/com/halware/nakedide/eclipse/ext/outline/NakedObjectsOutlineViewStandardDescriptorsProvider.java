package com.halware.nakedide.eclipse.ext.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;

import com.halware.nakedide.eclipse.core.util.MemberType;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

public final class NakedObjectsOutlineViewStandardDescriptorsProvider 
        implements IMetadataDescriptorsProvider {

	public List<MetadataDescriptor> getDescriptors() {
		
		List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
		
        descriptors.add(
            new ReadonlyMetadataDescriptor("Member", 
                new IEvaluator() {
                    public Object evaluate(
                            Object object) {
                        NakedObjectMember nakedObjectMember = (NakedObjectMember)object;
                        return nakedObjectMember.getMemberName();
                    }
        
                    public MetadataDescriptorKind<? extends CellEditor> getKind() {
                        return MetadataDescriptorKind.STRING;
                    }
                }));
        descriptors.add(
                new ReadonlyMetadataDescriptor("Kind", "Property, collection, action or reserved", 
                    new IEvaluator() {
                        public Object evaluate(
                                Object object) {
                            NakedObjectMember nakedObjectMember = (NakedObjectMember)object;
                            return nakedObjectMember.getMemberTypeName();
                        }
                        public MetadataDescriptorKind<? extends CellEditor> getKind() {
                            return MetadataDescriptorKind.COMPACT_STRING;
                        }
                    }));

        descriptors.add(
                new MemberTypeMetadataDescriptor("Hi", "hide method", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasHide();
                        }
                    }, null, MemberType.ALL));
        descriptors.add(
                new MemberTypeMetadataDescriptor("Di", "disabled method", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasDisable();
                        }
                    }, null, MemberType.ALL));
        descriptors.add(
                new MemberTypeMetadataDescriptor("Mo", "modify method (properties only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasModify();
                        }
                    }, null, MemberType.PROPERTY_ONLY));
        descriptors.add(
                new MemberTypeMetadataDescriptor("Cl", "clear method (properties only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasClear();
                        }
                    }, null, MemberType.PROPERTY_ONLY));
        descriptors.add(
                new MemberTypeMetadataDescriptor("Va", "validate method (properties & actions)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasValidate();
                        }
                    }, null, MemberType.PROPERTY_AND_ACTION));
        descriptors.add(
                new MemberTypeMetadataDescriptor("AT", "addTo method (collections only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasAddTo();
                        }
                    }, null, MemberType.COLLECTION_ONLY));
        descriptors.add(
                new MemberTypeMetadataDescriptor("VAT", "validateAddTo method (collections only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasValidateAddTo();
                        }
                    }, null, MemberType.COLLECTION_ONLY));
        descriptors.add(
                new MemberTypeMetadataDescriptor("RF", "removeFrom method (collections only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasRemoveFrom();
                        }
                    }, null, MemberType.COLLECTION_ONLY));
        descriptors.add(
                new MemberTypeMetadataDescriptor("VRF", "validateRemoveFrom method (collections only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasValidateRemoveFrom();
                        }
                    }, null, MemberType.COLLECTION_ONLY));
        descriptors.add(
                new MemberTypeMetadataDescriptor("De", "default method (properties and action only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasDefault();
                        }
                    }, null, MemberType.PROPERTY_AND_ACTION));
        descriptors.add(
                new MemberTypeMetadataDescriptor("Ch", "choices method (properties and action only)", 
                    new BooleanNakedObjectMemberEvaluator() {
                        protected boolean doEvaluate(NakedObjectMember nakedObjectMember) {
                            return nakedObjectMember.hasChoices();
                        }
                    }, null, MemberType.PROPERTY_AND_ACTION));

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
