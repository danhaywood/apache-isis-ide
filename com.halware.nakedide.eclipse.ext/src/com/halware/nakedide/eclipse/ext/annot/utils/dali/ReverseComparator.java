package com.halware.nakedide.eclipse.ext.annot.utils.dali;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This comparator will reverse the order of the specified comparator.
 * If the comparator is null, the natural ordering of the objects will be used.
 */
public class ReverseComparator
	implements Comparator, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final Comparator comparator;

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public ReverseComparator() {
		this(null);
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public ReverseComparator(Comparator comparator) {
		super();
		this.comparator = comparator;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public int compare(Object o1, Object o2) {
		if (this.comparator == null) {
			return ((Comparable) o2).compareTo(o1);
		}
		return this.comparator.compare(o2, o1);
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
