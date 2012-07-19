package com.halware.nakedide.eclipse.ext.annot.descriptors.when;

import org.eclipse.swt.graphics.Image;

import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatedLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;

public class WhenValueLabelProvider implements IEvaluatedLabelProvider {

	public Image getImageFor(IEvaluator evaluator, Object object) {
		return null;
	}

	public String getTextFor(IEvaluator evaluator, Object object) {
        Integer position = (Integer)evaluator.evaluate(object);
        return WhenValue.decode(position).getLabel();
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
