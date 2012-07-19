/**
 * 
 */
package com.halware.nakedide.eclipse.ext.outline;

import org.eclipse.jface.viewers.CellEditor;

import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

public abstract class BooleanNakedObjectMemberEvaluator implements IEvaluator {
    public Object evaluate(
            Object object) {
        NakedObjectMember nakedObjectMember = (NakedObjectMember)object;
        return doEvaluate(nakedObjectMember);
    }

    protected abstract boolean doEvaluate(NakedObjectMember nakedObjectMember);
    

    public MetadataDescriptorKind<? extends CellEditor> getKind() {
        return MetadataDescriptorKind.COMPACT_BOOLEAN;
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
