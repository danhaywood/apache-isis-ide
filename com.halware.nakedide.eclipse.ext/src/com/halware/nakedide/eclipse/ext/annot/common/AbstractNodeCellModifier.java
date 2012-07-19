package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;

public abstract class AbstractNodeCellModifier<T> implements ICellModifier {

	public abstract Logger getLOGGER();

	public AbstractNodeCellModifier(MetadataDescriptorSet metadataDescriptorSet) {
		this.metadataDescriptorSet = metadataDescriptorSet; 
	}

	private final MetadataDescriptorSet metadataDescriptorSet;
	protected MetadataDescriptorSet getPropertyDescriptorSet() {
		return metadataDescriptorSet;
	}

	/**
	 * For use by {@link #canModify(Object, String)}, {@link #getValue(Object, String)}
	 * and so on.
	 *  
	 * @param property
	 * @return
	 */
	protected MetadataDescriptor getMetadataDescriptor(String property) {
		return metadataDescriptorSet.getByName(property);
	}

    /**
     * Subclasses that deal with nodes that have a type (eg properties and
     * action parameters) whereby the metadata descriptor may only apply for
     * strings (eg <tt>MaxLength</tt>) should override and call
     * {@link MetadataDescriptor#isStringSpecific()).
     */
	public boolean canModify(Object element, String property) {
		MetadataDescriptor metadataDescriptor = getMetadataDescriptor(property);
		return metadataDescriptor.isModifiable();
	}

	public Object getValue(Object element, String property) {
		
		MetadataDescriptor metadataDescriptor = getMetadataDescriptor(property);
		Object value = metadataDescriptor.evaluate(element);
		
		getLOGGER().debug("getValue() returning: >>" + value + "<<");
		
		return value;
	}

	public void modify(Object element, String property, Object value) {
		getLOGGER().debug("modify(..., value): value = " + value);
		
		TableItem tableItem = (TableItem)element;
		T nakedObjectProperty = Generics.asT(tableItem.getData());
	
		MetadataDescriptor metadataDescriptor = getMetadataDescriptor(property);
		metadataDescriptor.modify(nakedObjectProperty, value);
	
		doModify(nakedObjectProperty, property, value);
	}
	
	protected abstract void doModify(T element, String property, Object value);
	

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
