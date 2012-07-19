package com.halware.nakedide.eclipse.ext.outline;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.halware.eclipseutil.util.ColorUtil;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;

public class OutlineViewLabelProvider 
		extends AbstractNodeLabelProvider<NakedObjectMember> {
	
	private final static Logger LOGGER = Logger.getLogger(OutlineViewLabelProvider.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

    private static Color inactiveBackground;

	public OutlineViewLabelProvider(MetadataDescriptorSet metadataDescriptorSet) {
		super(metadataDescriptorSet);
        if (inactiveBackground == null) {
            inactiveBackground = ColorUtil.getColor(SWT.COLOR_GRAY);
        }
	}

	protected String doGetText(NakedObjectMember nom) {
		return nom.getMemberName();
    }

    protected Image doGetColumnImage(
            NakedObjectMember nom,
            int columnIndex) {
        MetadataDescriptor metadataDescriptor = getMetadataDescriptor(columnIndex);
        if (!metadataDescriptor.isApplicable(nom)) {
            return null;
        }
        return super.doGetColumnImage(nom, columnIndex);
    }

    public Color doGetBackground(NakedObjectMember nom, int columnIndex) {
        MetadataDescriptor metadataDescriptor = getMetadataDescriptor(columnIndex);
        if (!metadataDescriptor.isApplicable(nom)) {
            return inactiveBackground;
        }
        return null;
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
