package com.halware.eclipseutil.util;

import java.util.List;

public final class CollectionUtil {
	
	private CollectionUtil(){}
	
	public static <T> T firstOrNull(List<T> list) {
		return list.size() > 0? list.get(0): null;
	}

}