package com.halware.nakedide.eclipse.ext.annot.strprop;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeCellModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;
import com.halware.nakedide.eclipse.ext.annot.mdd.StringSpecificMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;

public final class NakedObjectStringPropertiesCellModifier extends AbstractNodeCellModifier<NakedObjectProperty> {
	
	private final static Logger LOGGER = Logger.getLogger(NakedObjectStringPropertiesCellModifier.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	public NakedObjectStringPropertiesCellModifier(MetadataDescriptorSet metadataDescriptorSet) {
		super(metadataDescriptorSet); 
	}

    @Override
    public boolean canModify(Object element, String property) {
        NakedObjectProperty nop = (NakedObjectProperty)element;
        StringSpecificMetadataDescriptor metadataDescriptor = (StringSpecificMetadataDescriptor) getMetadataDescriptor(property);
        if (!metadataDescriptor.isApplicable(nop)) {
            return false;
        }
        return super.canModify(element, property);
    }
    
	protected void doModify(NakedObjectProperty nakedObjectProperty, String property, Object value) {
		getPropertyDescriptorSet().getOwner().refresh(
			new StructuredSelection(nakedObjectProperty));
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
