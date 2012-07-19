package com.halware.nakedide.eclipse.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class Constants {
    private Constants() {}
    
	public static final String PREFIX_CHOICES = "choices";
	public static final String PREFIX_DEFAULT = "default";
	public static final String PREFIX_HIDE = "hide";
	public static final String PREFIX_DISABLE = "disable";
	public static final String PREFIX_VALIDATE_REMOVE_FROM = "validateRemoveFrom";
	public static final String PREFIX_VALIDATE_ADD_TO = "validateAddTo";
	public static final String PREFIX_VALIDATE = "validate";
	public static final String PREFIX_REMOVE_FROM = "removeFrom";
	public static final String PREFIX_ADD_TO = "addTo";
	public static final String PREFIX_MODIFY = "modify";
    public static final String PREFIX_CLEAR = "clear";
	public static final String PREFIX_SET = "set";
	public static final String PREFIX_GET = "get";
    
	public final static String TITLE_METHOD_NAME = "title";
	public final static String TO_STRING_METHOD_NAME = "toString";

    public static final Map<String,String> BUILT_IN_VALUE_TYPES = asMap(
        new String[]{
            "java.lang.String",
            "java.lang.Byte",
            "java.lang.Short",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Float",
            "java.lang.Double",
            "java.lang.Boolean",
            "java.lang.Character",
            "byte",
            "short",
            "int",
            "long",
            "float",
            "double",
            "boolean",
            "char",
            "java.util.Date",
            "java.sql.Date",
            "java.sql.Time",
            "java.math.BigDecimal",
            "org.apache.isis.applib.value.Color",
            "org.apache.isis.applib.value.Date",
            "org.apache.isis.applib.value.DateTime",
            "org.apache.isis.applib.value.Image",
            "org.apache.isis.applib.value.Money",
            "org.apache.isis.applib.value.MultilineString",
            "org.apache.isis.applib.value.Password",
            "org.apache.isis.applib.value.Percentage",
            "org.apache.isis.applib.value.Quantity",
            "org.apache.isis.applib.value.Time",
            "org.apache.isis.applib.value.TimeStamp",
        });
    
    public static final Pattern COLLECTION_TYPE_UNRESOLVED_SIGNATURE_PATTERN = Pattern.compile("^Q([^<]+)<Q([^;]+);>;");
    public static final Map<String,String> COLLECTION_TYPES = asMap(
            new String[] {
                "List",
                "Set",
                "SortedSet",
                "Map",
                "SortedMap",
                "java.util.List",
                "java.util.Set",
                "java.util.SortedSet",
                "java.util.Map",
                "java.util.SortedMap",
            });
    public final static String[] COLLECTION_PREFIXES = {
        PREFIX_GET,
        PREFIX_SET,
        PREFIX_ADD_TO,
        PREFIX_REMOVE_FROM,
        PREFIX_DISABLE,
        PREFIX_VALIDATE_ADD_TO,
        PREFIX_VALIDATE_REMOVE_FROM,
        PREFIX_HIDE,
        PREFIX_DEFAULT,
        PREFIX_CHOICES
    };
    public final static String[] PROPERTY_PREFIXES = {
        PREFIX_GET,
        PREFIX_SET,
        PREFIX_MODIFY,
        PREFIX_CLEAR,
        PREFIX_DISABLE,
        PREFIX_VALIDATE,
        PREFIX_HIDE,
        PREFIX_DEFAULT,
        PREFIX_CHOICES
    };
    public final static String[] ACTION_PREFIXES = {
        PREFIX_VALIDATE,
        PREFIX_DISABLE,
        PREFIX_HIDE,
        PREFIX_DEFAULT,
        PREFIX_CHOICES,
    };
    /**
     * Those prefixes of action that can have a no-arg form.
     */
    public static final String[] ACTION_NO_ARG_SUPPORTING_PREFIXES = {
        PREFIX_DISABLE,
        PREFIX_HIDE,
        PREFIX_DEFAULT,
        PREFIX_CHOICES,
    };

    private static Map<String, String> asMap(String[] strings) {
        Map<String, String> map = new HashMap<String,String>();
        for(String string:strings) {
            map.put(string, string);
        }
        return map;
    }

}

/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
