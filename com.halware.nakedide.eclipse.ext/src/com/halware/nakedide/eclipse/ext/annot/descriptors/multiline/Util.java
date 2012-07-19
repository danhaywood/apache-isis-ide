package com.halware.nakedide.eclipse.ext.annot.descriptors.multiline;

import java.util.LinkedHashMap;

import com.halware.eclipseutil.util.Generics;
import com.halware.eclipseutil.util.StringUtil;

public class Util {
	private Util() {}


	/**
	 * Converts the values of the supplied object (assumed to be a <tt>LinkedHashMap</tt>)
	 * into a string using the supplied separator.
	 *  
	 * @param value - a <tt>LinkedHashMap&lt;String, Object>
	 * @param separator - the separator to use.
	 * 
	 * @return representation as a String.  A null value or empty map is converted into 
	 *         an empty string; a map where all values are <tt>null</tt> is 
	 *         converted into a single '-', otherwise returns a separated string
	 *         with the {{@link #toString()}} representation of each non-<tt>null</tt>
	 *         component, and a '-' if the component is <tt>null</tt>.
	 */
	public final static String fromLinkedHashMap(Object value, String separator) {
		LinkedHashMap<String,Object> memberValues = Generics.asT(value);
		StringBuilder buf = new StringBuilder();
		if (memberValues == null || memberValues.size() == 0) {
			// append nothing
		} else {
			Integer numberOfLines = (Integer) memberValues.get("numberOfLines");
			Boolean preventWrapping = (Boolean) memberValues.get("preventWrapping");
			if (numberOfLines == null && preventWrapping == null) {
				buf.append("-");
			} else {
				StringUtil.append(buf, numberOfLines);
				buf.append(separator);
				StringUtil.append(buf, preventWrapping);
			}
		}
		return buf.toString();
	}

	/**
	 * As per {@link #fromLinkedHashMap(Object, String)}, using <tt>:</tt> as a
	 * separator.
	 * 
	 * @param value
	 * @return
	 */
	public final static String fromLinkedHashMap(Object value) {
		return fromLinkedHashMap(value, ":");
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
