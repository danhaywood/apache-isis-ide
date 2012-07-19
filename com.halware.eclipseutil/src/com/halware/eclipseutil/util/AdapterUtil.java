package com.halware.eclipseutil.util;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

public final class AdapterUtil {
	
	private AdapterUtil() {}

    public static Object getAdapter(final Object object, final Class<?> target) 
    {
        Object adapter= null;
        
        if (object == null) {
			return null;
		}
        
        if (object instanceof IAdaptable) {
			adapter= ((IAdaptable)object).getAdapter(target);
		}
       
        if (adapter == null) {
			adapter= Platform.getAdapterManager().getAdapter(object, target);
		}
        
        if (adapter == null) {
			if (target.isAssignableFrom(object.getClass())) {
				adapter= object;
			}
		}
        
        return adapter;
    }


}
