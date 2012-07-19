package com.halware.nakedide.eclipse.ext.builders.checkers;

import org.eclipse.core.resources.IMarker;

/**
 * Modelled after <tt>PDEMarkerFactory</tt>
 * @author dkhaywood
 *
 */
public final class MarkerConstants {

    private MarkerConstants() {}


    /**
     * The value of key to store the id, as per {@link IMarker#getAttribute(String)}.  
     */
    public static final String ATTR_ID_KEY = 
        MarkerConstants.class.getPackage().getName() + ".id";

    /**
     * The value of key to store the method (if known), as per {@link IMarker#getAttribute(String)}.
     * 
     */
    public static final String ATTR_METHOD_KEY = 
        MarkerConstants.class.getPackage().getName() + ".method";

    public static final int ID_NO_RESOLUTION = -1;

    public static final int ID_AMBIGUOUS_METHOD = 0x0100;

    public static final int ID_ORPHANED_MUTATOR_METHOD = 0X0200;
    public static final int ID_ORPHANED_MODIFY_METHOD = 0X0201;
    public static final int ID_ORPHANED_CLEAR_METHOD = 0X0202;
    public static final int ID_ORPHANED_ADD_TO_METHOD = 0X0203;
    public static final int ID_ORPHANED_REMOVE_FROM_METHOD = 0X0204;
    public static final int ID_ORPHANED_VALIDATE_METHOD = 0X0205;
    public static final int ID_ORPHANED_VALIDATE_ADD_TO_METHOD = 0X0206;
    public static final int ID_ORPHANED_VALIDATE_REMOVE_FROM_METHOD = 0X0207;
    public static final int ID_ORPHANED_DISABLE_METHOD = 0X0208;
    public static final int ID_ORPHANED_HIDE_METHOD = 0X0209;
    public static final int ID_ORPHANED_DEFAULT_METHOD = 0X0210;
    public static final int ID_ORPHANED_CHOICES_METHOD = 0X0211;

    public static final int ID_PARAMETER_TYPE_INCORRECT_MUTATOR_METHOD = 0X0300;
    public static final int ID_PARAMETER_TYPE_INCORRECT_MODIFY_METHOD = 0X0301;
    public static final int ID_PARAMETER_TYPE_INCORRECT_ADD_TO_METHOD = 0X0303;
    public static final int ID_PARAMETER_TYPE_INCORRECT_REMOVE_FROM_METHOD = 0X0304;
    public static final int ID_PARAMETER_TYPE_INCORRECT_VALIDATE_METHOD = 0X0305;
    public static final int ID_PARAMETER_TYPE_INCORRECT_VALIDATE_ADD_TO_METHOD = 0X0306;
    public static final int ID_PARAMETER_TYPE_INCORRECT_VALIDATE_REMOVE_FROM_METHOD = 0X0307;
    public static final int ID_PARAMETER_TYPE_INCORRECT_DISABLE_METHOD = 0X0308;
    public static final int ID_PARAMETER_TYPE_INCORRECT_HIDE_METHOD = 0X0309;
    public static final int ID_PARAMETER_TYPE_INCORRECT_DEFAULT_METHOD = 0X0310;
    public static final int ID_PARAMETER_TYPE_INCORRECT_CHOICES_METHOD = 0X0311;

    public static final int ID_NUMBER_PARAMETERS_INCORRECT_SET_METHOD = 0X0400;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_MODIFY_METHOD = 0X0401;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_CLEAR_METHOD = 0X0402;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_ADD_TO_METHOD = 0X0403;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_REMOVE_FROM_METHOD = 0X0404;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_VALIDATE_METHOD = 0X0405;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_VALIDATE_ADD_TO_METHOD = 0X0406;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_VALIDATE_REMOVE_FROM_METHOD = 0X0407;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_DISABLE_METHOD = 0X0408;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_HIDE_METHOD = 0X0409;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_DEFAULT_METHOD = 0X040A;
    public static final int ID_NUMBER_PARAMETERS_INCORRECT_CHOICES_METHOD = 0X040B;

    public static final int ID_VALIDATE_PREFIX_INCORRECT = 0X0500;

    public static final int ID_TITLE_NOT_RETURNING_STRING = 0x0600;
    public static final int ID_NO_TITLE_OR_OVERRIDDEN_STRING = 0x0601;




    
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
