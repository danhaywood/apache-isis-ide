package com.halware.eclipseutil.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestUtils {
    
    private ManifestUtils(){}

    public static Map<String,String> manifestToProperties(Manifest m) {
        Attributes mainAttributes = m.getMainAttributes();
        Iterator iter = mainAttributes.keySet().iterator();
        Map<String,String> result = new LinkedHashMap<String,String>();
        while (iter.hasNext()) {
            Attributes.Name attrName = (Attributes.Name) iter.next();
            String key = attrName.toString();
            String val = (String) mainAttributes.get(attrName);
            result.put(key, val);
        }
        return result;
    }

}
