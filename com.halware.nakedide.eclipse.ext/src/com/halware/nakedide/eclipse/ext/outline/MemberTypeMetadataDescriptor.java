package com.halware.nakedide.eclipse.ext.outline;



import com.halware.nakedide.eclipse.core.util.MemberType;
import com.halware.nakedide.eclipse.ext.annot.action.NakedObjectAction;
import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParameter;
import com.halware.nakedide.eclipse.ext.annot.ast.MultipleValueAnnotationSingleMemberEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.coll.NakedObjectCollection;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatedLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.IModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;

/**
 * Represents some metadata (annotation or other information) about
 * a class, a property/collection/action of a class, or a parameter 
 * of an action.
 */
public class MemberTypeMetadataDescriptor extends MetadataDescriptor {

    public MemberTypeMetadataDescriptor(
			String memberPropertyName, 
			IEvaluatorAndModifier evaluatorAndModifier,
            MemberType[] memberTypes) {
		this(
			memberPropertyName,  
            (String)null,
			evaluatorAndModifier, memberTypes);
	}
	
    public MemberTypeMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluatorAndModifier evaluatorAndModifier,
            MemberType[] memberTypes) {
        this(
            memberPropertyName,  
            memberPropertyDescription,  
            evaluatorAndModifier, evaluatorAndModifier, null, memberTypes);
    }
    
	public MemberTypeMetadataDescriptor(
			String memberPropertyName, 
			IEvaluator evaluator, 
			IModifier modifier,
            MemberType[] memberTypes) {
		this(
			memberPropertyName,
            (String)null,
			evaluator, modifier, memberTypes);
	}

    public MemberTypeMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluator evaluator, 
            IModifier modifier,
            MemberType[] memberTypes) {
        this(
            memberPropertyName,
            memberPropertyDescription,
            evaluator, modifier, null, memberTypes);
    }

	public MemberTypeMetadataDescriptor(
			String memberPropertyName, 
			IEvaluatorAndModifier evaluatorAndModifier,
			IEvaluatedLabelProvider evaluatedLabelProvider,
            MemberType[] memberTypes) {
		this(
			memberPropertyName,
            (String)null,
			evaluatorAndModifier, evaluatedLabelProvider, memberTypes);
	}
	

    public MemberTypeMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluatorAndModifier evaluatorAndModifier,
            IEvaluatedLabelProvider evaluatedLabelProvider,
            MemberType[] memberTypes) {
        this(
            memberPropertyName, memberPropertyDescription, 
            evaluatorAndModifier, evaluatorAndModifier, 
            evaluatedLabelProvider, memberTypes);
    }

    public MemberTypeMetadataDescriptor(
            String memberPropertyName, 
            MultipleValueAnnotationSingleMemberEvaluatorAndModifier mvasmeam,
            MemberType[] memberTypes) {
		this(memberPropertyName, (String)null, mvasmeam, memberTypes);
    }

    public MemberTypeMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            MultipleValueAnnotationSingleMemberEvaluatorAndModifier mvasmeam,
            MemberType[] memberTypes) {
        this(memberPropertyName, mvasmeam, mvasmeam, null, memberTypes);
    }

	public MemberTypeMetadataDescriptor(
			String memberPropertyName, 
			IEvaluator evaluator, 
			IModifier modifier, 
			IEvaluatedLabelProvider labelProvider,
            MemberType[] memberTypes) {
        this(memberPropertyName, null, evaluator, modifier, labelProvider, memberTypes);
	}


    public MemberTypeMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluator evaluator, 
            IModifier modifier, 
            IEvaluatedLabelProvider labelProvider,
            MemberType[] memberTypes) {
        super(memberPropertyName, memberPropertyDescription, evaluator, modifier, labelProvider);
        this.memberTypes = memberTypes;
    }

    private MemberType[] memberTypes;
    public MemberType[] getMemberTypes() {
        return memberTypes;
    }

    
    public boolean isApplicable(NakedObjectMember nom) {
        return nom.getMemberType().within(memberTypes);
    }

    public boolean isApplicable(NakedObjectProperty nop) {
        return false;
    }

    public boolean isApplicable(NakedObjectCollection noc) {
        return false;
    }

    public boolean isApplicable(NakedObjectAction noa) {
        return false;
    }

    public boolean isApplicable(NakedObjectActionParameter noap) {
        return false;
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
