/**
 * 
 */
package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;

import com.halware.nakedide.eclipse.core.Images;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatedLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;

public final class DefaultEvaluatedLabelProvider implements IEvaluatedLabelProvider {
	
    private final Logger LOGGER = Logger.getLogger(DefaultEvaluatedLabelProvider.class);
    public Logger getLOGGER() {
        return LOGGER;
    }
    
	public String getTextFor(IEvaluator evaluator, Object object) {
		Object value = getValueElseEmptyString(evaluator, object);
        if (value instanceof Boolean) {
            return "";
        }
        return value.toString();
	}

    private Object getValueElseEmptyString(IEvaluator evaluator, Object object) {
        Object value = evaluator.evaluate(object);
        //getLOGGER().debug("getValueElseEmptyString(): evaluator returned >>>" + value + "<<<");
        if (value == null) {
            // if get back a null, then we assume we are dealing with a STRING, LONG_STRING or a CLASS_NAME, and convert to just
            // an empty string.  This is because the TextCellEditor does not allow null strings to
            // be set on the tableItem.
            return "";
        }
        return value;
    }

    public Image getImageFor(IEvaluator evaluator, Object object) {
		Object value = evaluator.evaluate(object);
		if (!(value instanceof Boolean)) {
			return null;
		}
		boolean valueAsBool = (Boolean)value;
		return valueAsBool?checkBoxTickedImage():checkBoxNotTickedImage();
	}

	private static final String CHECKBOX_TICKED = "checkbox_ticked.gif"; //$NON-NLS-1$
	private static final String CHECKBOX_NOT_TICKED = "checkbox_not_ticked.gif"; //$NON-NLS-1$

	private Image checkboxTickedImage;

	public Image checkBoxTickedImage() {
		if (checkboxTickedImage == null) {
			checkboxTickedImage = Images.create(CHECKBOX_TICKED).createImage();
		}
		return checkboxTickedImage;
	}

	private Image checkboxNotTickedImage;

	public Image checkBoxNotTickedImage() {
		if (checkboxNotTickedImage == null) {
			checkboxNotTickedImage = Images.create(CHECKBOX_NOT_TICKED).createImage();
		}
		return checkboxNotTickedImage;
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
