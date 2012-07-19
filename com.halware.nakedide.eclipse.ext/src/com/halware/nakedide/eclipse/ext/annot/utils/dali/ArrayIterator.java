package com.halware.nakedide.eclipse.ext.annot.utils.dali;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An object that iterates over the elements of an array
 * 
 * Copied from <tt>org.eclipse.core.internal.utils</tt>
 */
public class ArrayIterator implements Iterator {
	Object[] elements;
	int index;
	int lastElement;

	/**
	 * Returns new array enumeration over the given object array
	 */
	public ArrayIterator(Object[] elements) {
		this(elements, 0, elements.length - 1);
	}

	/**
	 * Returns new array enumeration over the given object array
	 */
	public ArrayIterator(Object[] elements, int firstElement, int lastElement) {
		super();
		this.elements = elements;
		index = firstElement;
		this.lastElement = lastElement;
	}

	/**
	 * Returns true if this enumeration contains more elements.
	 */
	public boolean hasNext() {
		return elements != null && index <= lastElement;
	}

	/**
	 * Returns the next element of this enumeration.
	 * @exception  NoSuchElementException  if no more elements exist.
	 */
	public Object next() throws NoSuchElementException {
		if (!hasNext())
			throw new NoSuchElementException();
		return elements[index++];
	}

	public void remove() {
		throw new UnsupportedOperationException();
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
