/**
 * 
 */
package com.halware.eclipseutil.util;

import java.util.ArrayList;
import java.util.List;


public class ClosureUtil {

	private ClosureUtil() {}

	public interface IClosure<P,Q> {
		public Q eval(P obj);
	}
	
	public static <P,Q> List<Q> forEach(List<P> input, IClosure<P,Q> closure) {
		List<Q> resultList = new ArrayList<Q>();
		for(P each: input) {
			resultList.add(closure.eval(each));
		}
		return resultList;
	}
}