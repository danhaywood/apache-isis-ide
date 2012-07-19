package com.halware.nakedide.eclipse.ext.annot.descriptors.multiline;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.halware.eclipseutil.util.BooleanUtil;
import com.halware.eclipseutil.util.SWTUtil;
import com.halware.eclipseutil.util.StringUtil;


public class MultiLineCellEditor extends TextCellEditor {

	private final static Logger LOGGER = Logger.getLogger(MultiLineCellEditor.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	public MultiLineCellEditor(Composite table) {
		super(table);
		final Text text = (Text)getControl();
		text.addVerifyListener(
				new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						String resultantText = 
							SWTUtil.ifAcceptedVerifyEvent(text, e);
						e.doit = isOk(resultantText);
					}

					private boolean isOk(String text) {
						getLOGGER().debug("verifyListener#isOK; text=" + text);
						return text.equals("") || 
						       text.equals("-") || 
						       text.matches("^(([0-9][0-9]?)|-):?") ||
					           text.matches("^(([0-9][0-9]?)|-):[nNyY-]?$");
					}
				});
	}
	
	/**
	 * @return a Map&lt;String, Object>.  If either element is not present, then the object value
	 *         is <tt>null</tt>
	 */
	protected Object doGetValue() {
		String value = (String) super.doGetValue();
		
		getLOGGER().debug("super.doGetValue() = " + value);
		
		if (StringUtil.isEmptyString(value)) {
			return null;
		}
		LinkedHashMap<String, Object> memberValues = new LinkedHashMap<String, Object>();
	
		String[] valueComponents = value.split(":");
		
		Integer numberOfLines = null;
		if (valueComponents.length >= 1) {
			try {
				numberOfLines = Integer.valueOf(valueComponents[0]);
			} catch(NumberFormatException ex) {
				// ignore
			}
		}
		memberValues.put("numberOfLines", numberOfLines);
		
		Boolean wrapping = null;
		if (valueComponents.length >= 2) {
			wrapping = BooleanUtil.toBoolean(valueComponents[1]);
		}
		memberValues.put("preventWrapping", wrapping);
		
		return memberValues;
	}
	
	/**
	 * Converts the supplied map of key/value pairs into a String,
	 * and calls superclass (which requires a string).
	 * 
	 * <p>
	 * See {@link StringUtil#fromLinkedHashMap(Object)} for further details.
	 * 
	 * @param value - a <tt>LinkedHashMap&lt;String,Object></tt>.
	 */
	@Override
	protected void doSetValue(Object value) {
		String valueStr = Util.fromLinkedHashMap(value);
		getLOGGER().debug("super.doSetValue(valueStr); valueStr = " + valueStr);
		super.doSetValue(valueStr);
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
