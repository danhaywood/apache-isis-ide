package com.halware.eclipseutil.util;

public final class ClassUtil {

	private ClassUtil() {}
	
	public static <T> Class<T> forName(final String className) throws ClassNotFoundException {
		return Generics.asT(Class.forName(className));
	}

    /**
     * If what you are trying to do is figure out the underlying class for a 
     * NakedObjectSpecification, you might want to try nos.getExtension(Class.class) instead.
     * 
     * @param <T>
     * @param className
     * @return
     */
    public static <T> Class<T> forNameOrNull(final String className) {
        try {
            return Generics.asT(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
