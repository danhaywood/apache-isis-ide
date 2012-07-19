package com.halware.eclipseutil.extpt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;


public final class ExtensionPointUtil
{

	private ExtensionPointUtil() {}
	
    /**
     * Finds all the parameter subelements of the given configuration element 
     * and returns a Map that contains all parameter values indexed by thier 
     * associated parameter names.
     * 
     * For instance, passing the IConfigurationElement for the element should 
     * below... 
     * 
     * <run>
     *  <parameter name="aaa" value="value1"/>
     *  <parameter name="bbb" value="value2"/>
     * </run>
     * 
     * ...would return a map with two keys, 'aaa' & 'bbb'.   
     *    
     */
    static public Map<String,String> getParameters(final IConfigurationElement configurationElement) {
        Map<String,String> map= new HashMap<String,String>();
        if (configurationElement != null) {
            IConfigurationElement[] children= configurationElement.getChildren("parameter");
            for (int i= 0; i < children.length; i++) {
                IConfigurationElement child= children[i];
                String name= child.getAttribute("name");
                String value= child.getAttribute("value");
                map.put(name, value);
            }
        }
        return map;
    }

}
