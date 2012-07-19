package com.halware.nakedide.eclipse.ext.annot.mdd;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;


public class DelegatingMetadataDescriptorSetOwner implements IMetadataDescriptorSetOwner {
	
	private final IMetadataDescriptorSetOwner underlying;

	public DelegatingMetadataDescriptorSetOwner(IMetadataDescriptorSetOwner underlying) {
		this.underlying = underlying;
	}

	public <T extends ICellModifier> T getCellModifier(MetadataDescriptorSet set) {
		return underlying.getCellModifier(set);
	}

	public IEvaluatedLabelProvider getDefaultLabelProvider() {
		return underlying.getDefaultLabelProvider();
	}

	public <T extends IBaseLabelProvider> T getLabelProvider(MetadataDescriptorSet set) {
		return underlying.getLabelProvider(set);
	}

	public MetadataDescriptorSet getPropertyDescriptorSet() {
		return underlying.getPropertyDescriptorSet();
	}

	public TableViewer getTableViewer() {
		return underlying.getTableViewer();
	}

	public void refresh(IStructuredSelection selectionHint) {
		underlying.refresh(selectionHint);
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
