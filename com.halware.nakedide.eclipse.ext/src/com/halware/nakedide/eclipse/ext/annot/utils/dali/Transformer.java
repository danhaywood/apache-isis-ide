package com.halware.nakedide.eclipse.ext.annot.utils.dali;

/**
 * Used by various "pluggable" classes to transform objects.
 */
public interface Transformer {

	/**
	 * Return the transformed object.
	 * The semantics of "transform" is determined by the
	 * contract between the client and the server.
	 */
	Object transform(Object o);


	Transformer NULL_INSTANCE =
		new Transformer() {
			// simply return the object, unchanged
			public Object transform(Object o) {
				return o;
			}
			public String toString() {
				return "NullTransformer";
			}
		};

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
