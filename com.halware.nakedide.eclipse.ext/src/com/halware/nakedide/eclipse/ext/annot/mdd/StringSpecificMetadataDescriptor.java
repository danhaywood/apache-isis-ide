package com.halware.nakedide.eclipse.ext.annot.mdd;


import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParameter;
import com.halware.nakedide.eclipse.ext.annot.ast.MultipleValueAnnotationSingleMemberEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;

/**
 * Represents some metadata (annotation or other information) about
 * a class, a property/collection/action of a class, or a parameter 
 * of an action.
 */
public class StringSpecificMetadataDescriptor extends MetadataDescriptor {


    public StringSpecificMetadataDescriptor(
			String memberPropertyName, 
			IEvaluatorAndModifier evaluatorAndModifier) {
		this(
			memberPropertyName,  
            (String)null,
			evaluatorAndModifier);
	}
	
    public StringSpecificMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluatorAndModifier evaluatorAndModifier) {
        this(
            memberPropertyName,  
            memberPropertyDescription,  
            evaluatorAndModifier, evaluatorAndModifier, null);
    }
    
	public StringSpecificMetadataDescriptor(
			String memberPropertyName, 
			IEvaluator evaluator, 
			IModifier modifier) {
        this(
			memberPropertyName,
            (String)null,
			evaluator, modifier);
	}

    public StringSpecificMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluator evaluator, 
            IModifier modifier) {
        this(
            memberPropertyName,
            memberPropertyDescription,
            evaluator, modifier, null);
    }

	public StringSpecificMetadataDescriptor(
			String memberPropertyName, 
			IEvaluatorAndModifier evaluatorAndModifier,
			IEvaluatedLabelProvider evaluatedLabelProvider) {
        this(
			memberPropertyName,
            (String)null,
			evaluatorAndModifier, evaluatedLabelProvider);
	}
	

    public StringSpecificMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluatorAndModifier evaluatorAndModifier,
            IEvaluatedLabelProvider evaluatedLabelProvider) {
        this(
            memberPropertyName, memberPropertyDescription, 
            evaluatorAndModifier, evaluatorAndModifier, 
            evaluatedLabelProvider);
    }

    public StringSpecificMetadataDescriptor(
            String memberPropertyName, 
            MultipleValueAnnotationSingleMemberEvaluatorAndModifier mvasmeam) {
        this(memberPropertyName, (String)null, mvasmeam);
    }

    public StringSpecificMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            MultipleValueAnnotationSingleMemberEvaluatorAndModifier mvasmeam) {
        this(memberPropertyName, mvasmeam, mvasmeam, null);
    }

	public StringSpecificMetadataDescriptor(
			String memberPropertyName, 
			IEvaluator evaluator, 
			IModifier modifier, 
			IEvaluatedLabelProvider labelProvider) {
        this(memberPropertyName, null, evaluator, modifier, labelProvider);
	}


    public StringSpecificMetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluator evaluator, 
            IModifier modifier, 
            IEvaluatedLabelProvider labelProvider) {
        super(memberPropertyName, memberPropertyDescription, evaluator, modifier, labelProvider);
    }

    
    public boolean isApplicable(NakedObjectActionParameter noap) {
        return noap.isStringType();
    }


    public boolean isApplicable(NakedObjectProperty nop) {
        return nop.isStringType();
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
