package com.halware.nakedide.eclipse.ext.annot.utils.dali;

import java.io.Serializable;

/**
 * This simple container class simply puts a bit of semantics
 * around a pair of numbers.
 */
public class Range
	implements Cloneable, Serializable
{
	/** The starting index of the range. */
	public final int start;

	/** The ending index of the range. */
	public final int end;

	/**
	 * The size can be negative if the ending index
	 * is less than the starting index.
	 */
	public final int size;

	private static final long serialVersionUID = 1L;


	/**
	 * Construct with the specified start and end,
	 * both of which are immutable.
	 */
	public Range(int start, int end) {
		super();
		this.start = start;
		this.end = end;
		this.size = end - start + 1;
	}

	/**
	 * Return whether the range includes the specified
	 * index.
	 */
	public boolean includes(int index) {
		return (this.start <= index) && (index <= this.end);
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ( ! (o instanceof Range)) {
			return false;
		}
		Range otherRange = (Range) o;
		return (this.start == otherRange.start)
			&& (this.end == otherRange.end);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return this.start ^ this.end;
	}

	/**
	 * @see Object#clone()
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new InternalError();
		}
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "[" + this.start + ", " + this.end + ']';
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
