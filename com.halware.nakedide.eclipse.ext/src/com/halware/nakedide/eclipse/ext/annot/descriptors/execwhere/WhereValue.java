package com.halware.nakedide.eclipse.ext.annot.descriptors.execwhere;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils.QualifiedNameValue;

/**
 * Corresponds to the values of <tt>When</tt> in the
 * applib.
 *
 * <p>
 * As used by the <tt>@Hidden</tt>, <tt>@Immutable</tt> and
 * <tt>@Disabled</tt> annotations.
 * 
 * @author dkhaywood
 */
public enum WhereValue {
    NOT_SPECIFIED("", "(not specified)", 0, null),
    LOCALLY("Where.LOCALLY", "Where.LOCALLY", 1, new AstUtils.QualifiedNameValue("Executed.Where", "LOCALLY")),
    REMOTELY("Where.REMOTELY", "Where.REMOTELY", 2, new AstUtils.QualifiedNameValue("Executed.Where", "REMOTELY"));
    
    private String label;
    public String getLabel() {
        return label;
    }
    private String dropDownLabel;
    public String getDropDownLabel() {
        return dropDownLabel;
    }
    private int position;
    public int getPosition() {
        return position;
    }
    private AstUtils.QualifiedNameValue annotationValue;
    public AstUtils.QualifiedNameValue getAnnotationValue() {
        return annotationValue;
    }

    WhereValue(String label, String dropDownLabel, int position, AstUtils.QualifiedNameValue annotationValue) {
        this.label = label;
        this.dropDownLabel = dropDownLabel;
        this.position = position;
        this.annotationValue = annotationValue;
    }
    
    private static EnumSet<WhereValue> all = EnumSet.allOf(WhereValue.class);
    
    public static WhereValue encode(AstUtils.QualifiedNameValue enumMember) {
        for(WhereValue value: all) {
            QualifiedNameValue annotationValue = value.getAnnotationValue();
            if (annotationValue == null && enumMember == null) {
                return value;
            }
            if (annotationValue != null && annotationValue.equals(enumMember)) {
                return value;
            }
        }
        return NOT_SPECIFIED;
    }

    public static WhereValue decode(int position) {
        for(WhereValue value: all) {
            if (value.getPosition() == position) {
                return value;
            }
        }
        return NOT_SPECIFIED;
    }

    public static String[] dropDownLabels() {
        List<String> labels = new ArrayList<String>();
        for(WhereValue value: all) {
            labels.add(value.getDropDownLabel());
        }
        return labels.toArray(new String[]{});
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
