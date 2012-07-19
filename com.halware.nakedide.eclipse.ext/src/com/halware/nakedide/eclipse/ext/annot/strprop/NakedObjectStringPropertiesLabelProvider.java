package com.halware.nakedide.eclipse.ext.annot.strprop;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;

public class NakedObjectStringPropertiesLabelProvider 
		extends AbstractNodeLabelProvider<NakedObjectProperty> {
	
	private final static Logger LOGGER = Logger.getLogger(NakedObjectStringPropertiesLabelProvider.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

    private static Color inactiveBackground;
    
	public NakedObjectStringPropertiesLabelProvider(MetadataDescriptorSet metadataDescriptorSet) {
		super(metadataDescriptorSet);
        if (inactiveBackground == null) {
            Display display = Display.getCurrent();
            inactiveBackground = display.getSystemColor(SWT.COLOR_GRAY);
        }
	}

	protected String doGetText(NakedObjectProperty nop) {
		return nop.getAccessorMethodName();
	}

    public Color doGetBackground(NakedObjectProperty nop) {
        if (!nop.isStringType()) {
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
