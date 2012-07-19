package com.halware.eclipseutil.util;

import org.eclipse.jface.text.IRegion;

public final class RegionUtil {

    private RegionUtil(){}

    public static boolean overlaps(
            IRegion region1,
            IRegion region2) {
        int region1StartPosition = region1.getOffset();
        int region2Offset = region2.getOffset();
        int region1Length = region1.getLength();
        return region2Offset >= region1StartPosition && 
               region2Offset < region1StartPosition + region1Length;
    }
    
}
