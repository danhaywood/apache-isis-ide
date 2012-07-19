package com.halware.nakedide.eclipse.ext.annot.action.params;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.halware.eclipseutil.util.ColorUtil;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;

public class NakedObjectActionParametersLabelProvider 
		extends AbstractNodeLabelProvider<NakedObjectActionParameter> {
	
	private final static Logger LOGGER = Logger.getLogger(NakedObjectActionParametersLabelProvider.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

    private static Color inactiveBackground;

	public NakedObjectActionParametersLabelProvider(MetadataDescriptorSet metadataDescriptorSet) {
		super(metadataDescriptorSet);
        if (inactiveBackground == null) {
            inactiveBackground = ColorUtil.getColor(SWT.COLOR_GRAY);
        }
	}

	protected String doGetText(NakedObjectActionParameter noap) {
		return noap.getParameterName();
    }

    public Color doGetBackground(NakedObjectActionParameter noap, int columnIndex) {
        MetadataDescriptor metadataDescriptor = getMetadataDescriptor(columnIndex);
        if (!metadataDescriptor.isApplicable(noap)) {
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
