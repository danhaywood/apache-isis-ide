package com.halware.nakedide.eclipse.ext.annot.descriptors.execwhere;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import com.halware.nakedide.eclipse.ext.annot.ast.SingleValueAnnotationEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;


public class WhereValueAnnotationEvaluatorAndModifier 
	extends SingleValueAnnotationEvaluatorAndModifier {
	
    private static final Logger LOGGER = Logger.getLogger(WhereValueAnnotationEvaluatorAndModifier.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

	public WhereValueAnnotationEvaluatorAndModifier(String annotationFullyQualifiedName) {
        super(
                annotationFullyQualifiedName, 
                new MetadataDescriptorKind<WhereValueCellEditorCombo>(
                       SWT.LEFT, WhereValueCellEditorCombo.class));
    }

    public Object evaluate(Object object) {
        AstUtils.QualifiedNameValue annotationValue = (AstUtils.QualifiedNameValue)super.evaluate(object);
        return WhereValue.encode(annotationValue).getPosition();
    }
    
	public void modify(Object object, Object value) {
        Integer position = (Integer)value;
        if (position == null) { position = 0; }
        super.modify(
            object, WhereValue.decode(position).getAnnotationValue());
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
