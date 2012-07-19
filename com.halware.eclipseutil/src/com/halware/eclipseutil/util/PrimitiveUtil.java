package com.halware.eclipseutil.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Static methods to aid with primitive classes
 * @author Mike
 */
@SuppressWarnings("unchecked")
public class PrimitiveUtil {
	

    private static final Map<Class,Class> WRAPPERS;
	private static final Map<Class,Method> VALUE_OF_METHODS;
	
	static {
		WRAPPERS = new HashMap<Class,Class>();
		PrimitiveUtil.WRAPPERS.put( boolean.class, Boolean.class );
		PrimitiveUtil.WRAPPERS.put( byte.class, Byte.class );
		PrimitiveUtil.WRAPPERS.put( char.class, Character.class );
		PrimitiveUtil.WRAPPERS.put( short.class, Short.class );
		PrimitiveUtil.WRAPPERS.put( int.class, Integer.class );
		PrimitiveUtil.WRAPPERS.put( long.class, Long.class );
		PrimitiveUtil.WRAPPERS.put( float.class, Float.class );
		PrimitiveUtil.WRAPPERS.put( double.class, Double.class );
		
		VALUE_OF_METHODS = new HashMap<Class,Method>();
		try {
			Method method;
			Class clazz;
			
			clazz = Byte.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			PrimitiveUtil.VALUE_OF_METHODS.put( clazz, method );
			PrimitiveUtil.VALUE_OF_METHODS.put( byte.class, method );
			
			clazz = Short.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			PrimitiveUtil.VALUE_OF_METHODS.put( clazz, method );
			PrimitiveUtil.VALUE_OF_METHODS.put( short.class, method );
			
			clazz = Integer.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			PrimitiveUtil.VALUE_OF_METHODS.put( clazz, method );
			PrimitiveUtil.VALUE_OF_METHODS.put( int.class, method );
			
			clazz = Long.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			PrimitiveUtil.VALUE_OF_METHODS.put( clazz, method );
			PrimitiveUtil.VALUE_OF_METHODS.put( long.class, method );
			
			clazz = Float.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			PrimitiveUtil.VALUE_OF_METHODS.put( clazz, method );
			PrimitiveUtil.VALUE_OF_METHODS.put( float.class, method );
			
			clazz = Double.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			PrimitiveUtil.VALUE_OF_METHODS.put( clazz, method );
			PrimitiveUtil.VALUE_OF_METHODS.put( double.class, method );	
		}
		catch ( NoSuchMethodException nsme ) {
			assert false : "no valueof method " ; //$NON-NLS-1$
		}
	}
	
	/**
	 * Whether the passed class is a wrapper class to a primitive.
	 * @param clazz
	 * @return
	 */
	public static final boolean isWrapperClass( final Class clazz ) {
		return PrimitiveUtil.WRAPPERS.values().contains( clazz );
	}
	
	/**
	 * Returns the wrapper class for the passed primitve class - or throws
	 * an <code>IllegalArgumentException</code> if not an primitive class
	 * @param clazz
	 * @return
	 */
	public static final Class getWrapperClass( final Class clazz ) {
		if( clazz == null ) {
			throw new IllegalArgumentException();
		}
		if( !clazz.isPrimitive() ) {
			throw new IllegalArgumentException();
		}
		return PrimitiveUtil.WRAPPERS.get( clazz );
	}
	
	/**
	 * Whether the passed class has a 'valueOf(String)' method.
	 * <br>If not a primitive or a wrapper class throws an 
	 * <code>IllegalArgumentException</code> 
	 * @param clazz
	 * @return
	 */
	public static final boolean hasValueOfMethod( final Class clazz ) {
		if( clazz == null ) {
			throw new IllegalArgumentException();
		}
		if( !clazz.isPrimitive() && !PrimitiveUtil.isWrapperClass( clazz ) ) {
			throw new IllegalArgumentException();
		}
		return PrimitiveUtil.VALUE_OF_METHODS.get( clazz ) != null;
	}
	
	/**
	 * Returns the passed class has a 'valueOf(String)' method - could
	 * be null - check with <code>hasValueOfMethod</code> if necessary
	 * <br>If not a primitive or a wrapper class throws an 
	 * <code>IllegalArgumentException</code> 
	 * @param clazz
	 * @return
	 */
	public static final Method getValueOfMethod( final Class clazz ) {
		if( clazz == null ) {
			throw new IllegalArgumentException();
		}
		if( !clazz.isPrimitive() && !PrimitiveUtil.isWrapperClass( clazz ) ) {
			throw new IllegalArgumentException();
		}
		return PrimitiveUtil.VALUE_OF_METHODS.get( clazz );		
	}

	// prevent instantiation
	private PrimitiveUtil() {
		super();
	}

}
