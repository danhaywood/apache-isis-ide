package com.halware.nakedide.eclipse.ext.annot.mdd;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.halware.eclipseutil.util.SWTUtil;

public class StringCellEditor extends TextCellEditor {
	
	private final static Logger LOGGER = Logger.getLogger(StringCellEditor.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	public StringCellEditor(Composite table) {
		super(table);
	}
	
	/**
	 * @return an {@link String}, or <tt>null</tt>.
	 */
	@Override
	protected Object doGetValue() {
		Object retval = null;
		try {
			String value = (String) super.doGetValue();
			if (isEmptyString(value)) {
				retval = null;
			} else {
				retval = value;
			}
			return retval;
		} finally {
			getLOGGER().debug("doGetValue() returning: >>" + retval + "<<");
		}
	}

	/**
	 * Converts the supplied {@link String} (possibly null) into a string representation,
	 * and calls superclass (which requires a string). 
	 * 
	 * <p>
	 * <tt>null</tt> values are converted into an empty string.
	 * 
	 * @param value - an {@link Integer}, or <tt>null</tt>.
	 */
	@Override
	protected void doSetValue(Object valueObj) {
		getLOGGER().debug("doSetValue(value): value= >>" + valueObj + "<<");
		String valueStr = (String)valueObj;
		String value = valueStr != null? valueStr.toString():"";
		super.doSetValue(value);
	}
	
	private boolean isEmptyString(String value) {
		return value == null || value.length() == 0;
	}

	private String regex;
	public String getVerifyRegex() {
		return regex;
	}
	public void setVerifyRegex(final String regex) {
		this.regex = regex;
		if (regex != null) {
			final Text text = (Text)getControl();
			text.addVerifyListener(
					new VerifyListener() {
						public void verifyText(VerifyEvent e) {
						String resultantText = 
							SWTUtil.ifAcceptedVerifyEvent(text, e);
						e.doit = isOk(resultantText);
					}
				
					private boolean isOk(String text) {
						getLOGGER().debug("verifyListener#isOK; text=" + text + "; regex=" + regex);
						return text.equals("") ||
							   text.matches(regex);
					}
				});
		}
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
