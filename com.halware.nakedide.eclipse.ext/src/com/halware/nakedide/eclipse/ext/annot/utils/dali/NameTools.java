package com.halware.nakedide.eclipse.ext.annot.utils.dali;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Various helper methods for generating names.
 */
public final class NameTools {

	/**
	 * Given a "root" name and a set of existing names, generate a unique,
	 * Java-legal name that is either the "root" name or some variation on
	 * the "root" name (e.g. "root2", "root3",...).
	 * The names are case-sensitive.
	 */
	@SuppressWarnings("unchecked")
	public static String uniqueJavaNameFor(String rootName, Iterator existingNames) {
		Collection existingNames2 = CollectionTools.set(existingNames);
		existingNames2.addAll(JAVA_RESERVED_WORDS_SET);
		return uniqueNameFor(rootName, existingNames2, rootName);
	}
	
	/**
	 * Given a "root" name and a set of existing names, generate a unique,
	 * Java-legal name that is either the "root" name or some variation on
	 * the "root" name (e.g. "root2", "root3",...).
	 * The names are case-sensitive.
	 */
	@SuppressWarnings("unchecked")
	public static String uniqueJavaNameFor(String rootName, Collection existingNames) {
		Collection existingNames2 = new HashSet(existingNames);
		existingNames2.addAll(JAVA_RESERVED_WORDS_SET);
		return uniqueNameFor(rootName, existingNames2, rootName);
	}

	/**
	 * Given a "root" name and a set of existing names, generate a unique
	 * name that is either the "root" name or some variation on the "root"
	 * name (e.g. "root2", "root3",...). The names are case-sensitive.
	 */
	public static String uniqueNameFor(String rootName, Iterator existingNames) {
		return uniqueNameFor(rootName, CollectionTools.set(existingNames));
	}
	
	/**
	 * Given a "root" name and a set of existing names, generate a unique
	 * name that is either the "root" name or some variation on the "root"
	 * name (e.g. "root2", "root3",...). The names are case-sensitive.
	 */
	public static String uniqueNameFor(String rootName, Collection existingNames) {
		return uniqueNameFor(rootName, existingNames, rootName);
	}

	/**
	 * Given a "root" name and a set of existing names, generate a unique
	 * name that is either the "root" name or some variation on the "root"
	 * name (e.g. "root2", "root3",...). The names are NOT case-sensitive.
	 */
	public static String uniqueNameForIgnoreCase(String rootName, Iterator existingNames) {
		return uniqueNameForIgnoreCase(rootName, CollectionTools.set(existingNames));
	}

	/**
	 * Given a "root" name and a set of existing names, generate a unique
	 * name that is either the "root" name or some variation on the "root"
	 * name (e.g. "root2", "root3",...). The names are NOT case-sensitive.
	 */
	public static String uniqueNameForIgnoreCase(String rootName, Collection existingNames) {
		return uniqueNameFor(rootName, convertToLowerCase(existingNames), rootName.toLowerCase());
	}

	/**
	 * use the suffixed "template" name to perform the comparisons, but RETURN
	 * the suffixed "root" name; this allows case-insensitive comparisons
	 * (i.e. the "template" name has been morphed to the same case as
	 * the "existing" names, while the "root" name has not, but the "root" name
	 * is what the client wants morphed to be unique)
	 */
	private static String uniqueNameFor(String rootName, Collection existingNames, String templateName) {
		if ( ! existingNames.contains(templateName)) {
			return rootName;
		}
		String uniqueName = templateName;
		for (int suffix = 2; true; suffix++) {
			if ( ! existingNames.contains(uniqueName + suffix)) {
				return rootName.concat(String.valueOf(suffix));
			}
		}
	}

	/**
	 * Convert the specified collection of strings to a collection of the same
	 * strings converted to lower case.
	 */
	@SuppressWarnings("unchecked")
	private static Collection convertToLowerCase(Collection strings) {
		Collection result = new HashBag(strings.size());
		for (Iterator stream = strings.iterator(); stream.hasNext(); ) {
			result.add(((String) stream.next()).toLowerCase());
		}
		return result;
	}

	/**
	 * Build a fully-qualified name for the specified database object.
	 * Variations:
	 *     catalog.schema.name
	 *     catalog..name
	 *     schema.name
	 *     name
	 */
	public static String buildQualifiedDatabaseObjectName(String catalog, String schema, String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if ((catalog == null) && (schema == null)) {
			return name;
		}

		StringBuffer sb = new StringBuffer(100);
		if (catalog != null) {
			sb.append(catalog);
			sb.append('.');
		}
		if (schema != null) {
			sb.append(schema);
		}
		sb.append('.');
		sb.append(name);
		return sb.toString();
	}

	/**
	 * The set of reserved words in the Java programming language.
	 * These words cannot be used as identifiers (i.e. names).
	 * http://java.sun.com/docs/books/tutorial/java/nutsandbolts/_keywords.html
	 */
	public static final String[] JAVA_RESERVED_WORDS = new String[] {
				"abstract",
				"assert",  // jdk 1.4
				"boolean",
				"break",
				"byte",
				"case",
				"catch",
				"char",
				"class",
				"const",  // unused
				"continue",
				"default",
				"do",
				"double",
				"else",
				"enum",  // jdk 5.0
				"extends",
				"false",
				"final",
				"finally",
				"float",
				"for",
				"goto",  // unused
				"if",
				"implements",
				"import",
				"instanceof",
				"int",
				"interface",
				"long",
				"native",
				"new",
				"null",
				"package",
				"private",
				"protected",
				"public",
				"return",
				"short",
				"static",
				"strictfp",  // jdk 1.2
				"super",
				"switch",
				"synchronized",
				"this",
				"throw",
				"throws",
				"transient",
				"true",
				"try",
				"void",
				"volatile",
				"while"
			};

	/**
	 * The set of reserved words in the Java programming language.
	 * These words cannot be used as identifiers (i.e. names).
	 * http://java.sun.com/docs/books/tutorial/java/nutsandbolts/_keywords.html
	 */
	public static final Set JAVA_RESERVED_WORDS_SET = CollectionTools.set(JAVA_RESERVED_WORDS);

	/**
	 * Return the set of Java programming language reserved words.
	 * These words cannot be used as identifiers (i.e. names).
	 * http://java.sun.com/docs/books/tutorial/java/nutsandbolts/_keywords.html
	 */
	@SuppressWarnings("restriction")
	public static Iterator javaReservedWords() {
		return new ArrayIterator(JAVA_RESERVED_WORDS);
	}

	/**
	 * Convert the specified string to a valid Java identifier
	 * by substituting an underscore '_' for any invalid characters
	 * in the string and capitalizing the string if it is a Java
	 * reserved word.
	 */
	public static String convertToJavaIdentifier(String string) {
		return convertToJavaIdentifier(string, '_');
	}

	/**
	 * Convert the specified string to a valid Java identifier
	 * by substituting the specified character for any invalid characters
	 * in the string and capitalizing the string if it is a Java
	 * reserved word.
	 */
	public static String convertToJavaIdentifier(String string, char c) {
		if (string.length() == 0) {
			return string;
		}
		if (JAVA_RESERVED_WORDS_SET.contains(string)) {
			// a reserved words is a valid identifier, we just need to tweak it a bit
			return StringTools.capitalize(string);
		}
		return new String(convertToJavaIdentifierInternal(string.toCharArray(), c));
	}

	/**
	 * Convert the specified string to a valid Java identifier
	 * by substituting an underscore '_' for any invalid characters
	 * in the string and capitalizing the string if it is a Java
	 * reserved word.
	 */
	public static char[] convertToJavaIdentifier(char[] string) {
		return convertToJavaIdentifier(string, '_');
	}

	/**
	 * Convert the specified string to a valid Java identifier
	 * by substituting the specified character for any invalid characters
	 * in the string and capitalizing the string if it is a Java
	 * reserved word.
	 */
	public static char[] convertToJavaIdentifier(char[] string, char c) {
		int length = string.length;
		if (length == 0) {
			return string;
		}
		if (JAVA_RESERVED_WORDS_SET.contains(new String(string))) {
			// a reserved words is a valid identifier, we just need to tweak it a bit
			return StringTools.capitalize(string);
		}
		return convertToJavaIdentifierInternal(string, c);
	}

	private static char[] convertToJavaIdentifierInternal(char[] string, char c) {
		if ( ! Character.isJavaIdentifierStart(string[0])) {
			if ( ! Character.isJavaIdentifierStart(c)) {
				throw new IllegalArgumentException("invalid Java identifier start char: '" + c + "'");
			}
			string[0] = c;
		}
		if ( ! Character.isJavaIdentifierPart(c)) {
			throw new IllegalArgumentException("invalid Java identifier part char: '" + c + "'");
		}
		for (int i = string.length; i-- > 1; ) {  // NB: end with 1
			if ( ! Character.isJavaIdentifierPart(string[i])) {
				string[i] = c;
			}
		}
		return string;
	}


	// ********** constructor **********

	/**
	 * Suppress default constructor, ensuring non-instantiability.
	 */
	private NameTools() {
		super();
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
