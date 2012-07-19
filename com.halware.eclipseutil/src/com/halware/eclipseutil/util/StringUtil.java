package com.halware.eclipseutil.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;


/**
 * Static methods for handling <tt>Strings</tt>
 */
public final class StringUtil {

	private StringUtil() {}
	
	public static String simpleNameFor(final String id) {
		int lastIndexOf = id.lastIndexOf('.');
		if (lastIndexOf == -1) {
			return id;
		}
		return id.substring(lastIndexOf+1);
	}


	/**
	 * Left-pads the passed <code>String</code> with whitespace up to the 
	 * passed length.  If the <code>String</code> is already that length, 
	 * or longer, simply return the <code>String</code>.
	 * @param s
	 * @param length
	 * @return
	 */
	public static String padLeft( final String s, final int length ) {
		if ( s == null ) {
			throw new IllegalArgumentException();
		}
		if ( length < 1 ) {
			throw new IllegalArgumentException();
		}
		int numPads = length - s.length();
		if ( numPads < 1 ) {
			return s;
		}
		char[] pad = new char[numPads];
		Arrays.fill( pad, ' ' );
		return new String( pad ) + s;
	}
	
	/**
	 * Allows printf-style substitution.
	 * @param s
	 * @param args
	 * @return
	 * @see java.io.PrintStream#printf(java.lang.String, java.lang.Object[])
	 */
	public static String printf( final String s, final Object...args ) {
		if( s == null ) {
			throw new IllegalArgumentException();
		}
		if ( args == null ) {
			return s;
		}
		 ByteArrayOutputStream out = new ByteArrayOutputStream( s.length() );
		 PrintStream ps = null;
		 try {
			 ps = new PrintStream( out );
			 ps.printf( s, args );
			 ps.close();
			 ps = null;
			 return new String( out.toByteArray() );
		 }
		 finally {
			 if ( ps != null ) {
				ps.close();
			}
		 }
	}

    /**
     * Upper cases the first letter.
     * 
     * <p>
     * For example, <tt>someField</tt> -> <tt>SomeField</tt>.
     * 
     * @param value
     * @return
     */
	public static String titleCase(String value) {
        if (value == null || value.length() == 0) return value;
        StringBuilder buf = new StringBuilder();
        buf.append(value.substring(0, 1).toUpperCase());
        if (value.length() > 1) {
            buf.append(value.substring(1));
        }
        return buf.toString();
	}

    public static boolean isEmptyString(Object value) {
        return value == null || 
               (value instanceof String && ((String)value).length() == 0);
    }

	public static boolean isEmptyString(String value) {
		return value == null || value.length() == 0;
	}

	public final static StringBuilder append(StringBuilder buf, Integer value) {
		buf.append( value != null?value.toString():"-" );
		return buf;
	}
	public final static StringBuilder append(StringBuilder buf, String value) {
		buf.append( value != null?value:"-" );
		return buf;
	}

	public final static StringBuilder append(StringBuilder buf, Boolean value) {
		buf.append( value != null?value.booleanValue()?"Y":"N": "-" );
		return buf;
	}

	public static String join(List<? extends Object> list, String delimiter) {
		StringBuilder buf = new StringBuilder();
		for(int i=0; i<list.size(); i++) {
			buf.append(list.get(i).toString());
			if (i != list.size() - 1) {
				buf.append(delimiter);
			}
		}
		return buf.toString();
	}

    /**
     * Null-safe comparison of two strings.
     * 
     * <p>
     * Equal if both are <tt>null</tt> or if both are non-<tt>null</tt> and
     * are equal as per {@link String#equals(Object)}.  Otherwise not
     * equal.
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(
            String str1,
            String str2) {
        if (str1 == null && str2 == null) { return true; }
        if (str1 == null && str2 != null) { return false; }
        if (str1 != null && str2 == null) { return false; }
        return str1.equals(str2);
    }

    public static boolean isEmpty(
            String description) {
        return description != null && description.length() != 0;
    }

}
