package com.halware.eclipseutil.util;


public final class Generics {
	
	private Generics() {}

	@SuppressWarnings("unchecked")
	public static <T> T asT(Object obj) {
		return (T) obj;
	}


}