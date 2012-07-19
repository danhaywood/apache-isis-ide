package com.halware.eclipseutil.util;

public final class BooleanUtil {
	private BooleanUtil(){}

	public static Boolean toBoolean(String boolStr) {
		String qbeYN = boolStr;
		if (!StringUtil.isEmptyString(qbeYN)) {
			if (qbeYN.equalsIgnoreCase("Y") || 
				qbeYN.equalsIgnoreCase("true") ||
				qbeYN.equalsIgnoreCase("T") ) {
				return Boolean.TRUE;
			}
			if (qbeYN.equalsIgnoreCase("N") || 
				qbeYN.equalsIgnoreCase("false") ||
				qbeYN.equalsIgnoreCase("F") ) {
				return Boolean.FALSE;
			}
		}
		return null;
	}
}
