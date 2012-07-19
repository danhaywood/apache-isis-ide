package com.halware.nakedide.eclipse.ext.annot.descriptors.when;

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
 * <p>
 * The {@link #DEFAULT} constant indicates that the default value for
 * <tt>When</tt> should be used.
 * 
 * @author dkhaywood
 */
public enum WhenValue {
    NOT_SPECIFIED("", "(not specified)", 0, null),
    DEFAULT("(default value)", "(default value)", 1, QualifiedNameValue.DEFAULT_VALUE),
    ALWAYS("When.ALWAYS", "When.ALWAYS", 2, new AstUtils.QualifiedNameValue("When", "ALWAYS")),
    ONCE_PERSISTED("When.ONCE_PERSISTED", "When.ONCE_PERSISTED", 3, new AstUtils.QualifiedNameValue("When", "ONCE_PERSISTED")),
    UNTIL_PERSISTED("When.UNTIL_PERSISTED", "When.UNTIL_PERSISTED", 4, new AstUtils.QualifiedNameValue("When", "UNTIL_PERSISTED")),
    NEVER("When.NEVER", "When.NEVER", 5, new AstUtils.QualifiedNameValue("When", "NEVER"));
    
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

    WhenValue(String label, String dropDownLabel, int position, AstUtils.QualifiedNameValue annotationValue) {
        this.label = label;
        this.dropDownLabel = dropDownLabel;
        this.position = position;
        this.annotationValue = annotationValue;
    }
    
    private static EnumSet<WhenValue> all = EnumSet.allOf(WhenValue.class);
    
    public static WhenValue encode(AstUtils.QualifiedNameValue enumMember) {
        for(WhenValue value: all) {
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

    public static WhenValue decode(int position) {
        for(WhenValue value: all) {
            if (value.getPosition() == position) {
                return value;
            }
        }
        return NOT_SPECIFIED;
    }

    public static String[] dropDownLabels() {
        List<String> labels = new ArrayList<String>();
        for(WhenValue value: all) {
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
