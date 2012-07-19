package com.halware.nakedide.eclipse.ext.annot.coll;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeCellModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;

public final class NakedObjectCollectionsCellModifier extends AbstractNodeCellModifier<NakedObjectCollection> {
	
	private final static Logger LOGGER = Logger.getLogger(NakedObjectCollectionsCellModifier.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	public NakedObjectCollectionsCellModifier(MetadataDescriptorSet metadataDescriptorSet) {
		super(metadataDescriptorSet); 
	}
	
	protected void doModify(NakedObjectCollection nakedObjectCollection, String property, Object value) {
		getPropertyDescriptorSet().getOwner().refresh(
			new StructuredSelection(nakedObjectCollection));
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
