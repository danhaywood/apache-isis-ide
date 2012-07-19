package com.halware.nakedide.eclipse.ext.annot.mdd;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * For cell editors of annotations that verify the text in some way.
 */
public abstract class AbstractTextCellEditor extends TextCellEditor {

	public abstract Logger getLOGGER();
	
	public AbstractTextCellEditor(final Composite table) {
		super(table);
		final Text text = (Text)getControl();
        doHookListeners(text);
	}

    /**
     * Subclasses are expected to hook listeners such as
     * verify listeners.
     * 
     * @param text
     */
    protected void doHookListeners(final Text text) {
    }
	
    @Override
	protected Object doGetValue() {
        String value = (String) super.doGetValue();
        getLOGGER().debug("super.doGetValue() = " + value);
        return value;
    }
	
	@Override
	protected void doSetValue(Object value) {
        getLOGGER().debug("super.doSetValue(value); value = " + value);
        super.doSetValue(value);
    }

    protected boolean isEmptyString(String value) {
        return value == null || value.length() == 0;
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
