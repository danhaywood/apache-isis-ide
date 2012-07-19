package com.halware.eclipseutil.util;


/**
 * Locally used utility methods for manipulating method names etc.
 *
 */
public final class MethodNameUtil {

	private MethodNameUtil() {}
	
	/**
	 * Ensures that the first letter is lower case.  The remainder of the word
	 * is untouched.
	 * @param word
	 * @return
	 */
	public final String camelCase(final String word) {
		return
			Character.toLowerCase(word.charAt(0)) +
				(word.length() > 1?word.substring(1):"");
	}
	
	/**
	 * Ensures that the first letter is upper case.  The remainder of the word
	 * is untouched.
	 * @param word
	 * @return
	 */
	public final String titleCase(final String word) {
		return
		Character.toUpperCase(word.charAt(0)) +
			(word.length() > 1?word.substring(1):"");
	}

}
