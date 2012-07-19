package com.halware.nakedide.eclipse.ext.annot.descriptors.execwhere;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;


public class WhereValueCellEditorCombo extends ComboBoxCellEditor {

	private final static Logger LOGGER = Logger.getLogger(WhereValueCellEditorCombo.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	public WhereValueCellEditorCombo(Composite table) {
		super(table, WhereValue.dropDownLabels());
	}
	
	protected Object doGetValue() {
        Integer value = (Integer) super.doGetValue();
		
        getLOGGER().debug("doGetValue; value=>>>"+value+"<<<");
        return value;
	}
	
	@Override
	protected void doSetValue(Object value) {
        getLOGGER().debug("doSetValue; value=>>>"+value+"<<<");
		Integer valueInt = (Integer)value;
        if (valueInt == null) {
            valueInt = 0;
        }
		super.doSetValue(valueInt);
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
