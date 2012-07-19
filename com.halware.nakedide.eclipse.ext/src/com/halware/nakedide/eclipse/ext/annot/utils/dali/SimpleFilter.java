package com.halware.nakedide.eclipse.ext.annot.utils.dali;

import java.io.Serializable;

/**
 * Simple, abstract implementation of <code>Filter</code>
 * that holds on to a criterion object that can be used in the
 * <code>accept(Object)</code> or <code>reject(Object)</code>
 * methods. Subclasses can override either of these methods,
 * depending on which is easier to implement. Note that at least
 * one of these methods <em>must</em> be overridden or
 * an infinite loop will occur. If both of them are overridden,
 * only the <code>accept(Object)</code> method will be used.
 * <p>
 * Simplifies the implementation of straightforward inner classes.
 * Here is an example of a filter that can be used by a
 * <code>FilteringIterator</code> to return only those strings
 * in the nested iterator start with "prefix":
 * <pre>
 *	Filter filter = new SimpleFilter("prefix") {
 *		public boolean accept(Object next) {
 *			return ((STRING) next).startsWith((STRING) criterion);
 *		}
 *	};
 * </pre>
 */
public abstract class SimpleFilter
	implements Filter, Cloneable, Serializable
{
	protected final Object criterion;

	private static final long serialVersionUID = 1L;


	/**
	 * More useful constructor. The specified criterion can
	 * be used by a subclass to "accept" or "reject" objects.
	 */
	protected SimpleFilter(Object criterion) {
		super();
		this.criterion = criterion;
	}

	/**
	 * Construct a simple filter with a null criterion
	 */
	protected SimpleFilter() {
		this(null);
	}

	/**
	 * Return whether the the specified object should be "rejected".
	 * The semantics of "rejected" is determined by the client.
	 */
	protected boolean reject(Object next) {
		return ! this.accept(next);
	}

	/**
	 * Return whether the the specified object should be "accepted".
	 * The semantics of "accepted" is determined by the client.
	 */
	public boolean accept(Object next) {
		return ! this.reject(next);
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
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if ( ! (o instanceof SimpleFilter)) {
			return false;
		}
		SimpleFilter other = (SimpleFilter) o;
		return (this.criterion == null) ?
			(other.criterion == null) : this.criterion.equals(other.criterion);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return (this.criterion == null) ? 0 : this.criterion.hashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return StringTools.buildToStringFor(this, this.criterion);
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
