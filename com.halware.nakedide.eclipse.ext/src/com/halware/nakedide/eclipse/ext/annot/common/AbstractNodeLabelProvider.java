package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;

public abstract class AbstractNodeLabelProvider<T> 
		extends LabelProvider implements ITableLabelProvider, IColorProvider, ITableColorProvider {
	
	public abstract Logger getLOGGER();

    public AbstractNodeLabelProvider(MetadataDescriptorSet metadataDescriptorSet) {
        this.metadataDescriptorSet = metadataDescriptorSet;
    }

	private final MetadataDescriptorSet metadataDescriptorSet;
    protected MetadataDescriptorSet getMetadataDescriptorSet() {
        return metadataDescriptorSet;
    }

    protected MetadataDescriptor getMetadataDescriptor(
            int columnIndex) {
        return metadataDescriptorSet.getByPosition(columnIndex);
    }

	public Image getImage(Object obj) {
		return null;
	}

	public String getText(Object obj) {
		T asT = Generics.asT(obj);
		return doGetText(asT);
	}
	
	protected abstract String doGetText(T obj);
	
	public Image getColumnImage(Object element, int columnIndex) {
        T asT = Generics.asT(element);
        return doGetColumnImage(asT, columnIndex);
	}

    protected Image doGetColumnImage(
            T element,
            int columnIndex) {
        MetadataDescriptor metadataDescriptor = getMetadataDescriptor(columnIndex);
        return metadataDescriptor.getImageFor(element);
    }

	public String getColumnText(Object element, int columnIndex) {
        T asT = Generics.asT(element);
        return doGetColumnText(asT, columnIndex);
	}


    private String doGetColumnText(
            T asT,
            int columnIndex) {
        MetadataDescriptor metadataDescriptor = getMetadataDescriptor(columnIndex);
        return metadataDescriptor.getTextFor(asT);
    }

    public Color getBackground(Object element) {
        T asT = Generics.asT(element);
        return doGetBackground(asT);
    }

    /**
     * As per {@link IColorProvider#getBackground(Object)}.
     * 
     * @param obj
     * @return
     */
    protected Color doGetBackground(T obj) {
        return null;
    }

    public Color getForeground(Object element) {
        T asT = Generics.asT(element);
        return doGetForeground(asT);
    }

    /**
     * As per {@link IColorProvider#getForeground(Object)}.
     * 
     * @param obj
     * @return
     */
	protected Color doGetForeground(T obj) {
        return null;
    }

    public Color getForeground(
            Object element,
            int columnIndex) {
        T asT = Generics.asT(element);
        return doGetForeground(asT, columnIndex);
    }

    /**
     * As per {@link ITableColorProvider#getForeground(Object, int)}
     * 
     * @param obj
     * @param columnIndex
     * @return
     */
    protected Color doGetForeground(
            T obj,
            int columnIndex) {
        return null;
    }

    public Color getBackground(
            Object element,
            int columnIndex) {
        T asT = Generics.asT(element);
        return doGetBackground(asT, columnIndex);
    }


    /**
     * As per {@link ITableColorProvider#getBackground(Object, int)}
     * 
     * @param obj
     * @param columnIndex
     * @return
     */
    protected Color doGetBackground(
            T obj,
            int columnIndex) {
        return null;
    }

    public void dispose() {
		super.dispose();
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
