package com.halware.nakedide.eclipse.ext.builders.markres;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

import com.halware.nakedide.eclipse.ext.builders.checkers.MarkerConstants;

public class MarkerResolutionGenerator implements IMarkerResolutionGenerator {

    private final static Logger LOGGER = Logger.getLogger(MarkerResolutionGenerator.class);
    public static Logger getLOGGER() {
        return LOGGER;
    }
    
    public IMarkerResolution[] getResolutions(
            IMarker marker) {
        
        Integer id = null;
        IMethod method = null;
        try {
            id = (Integer)marker.getAttribute(MarkerConstants.ATTR_ID_KEY);
            method = (IMethod)marker.getAttribute(MarkerConstants.ATTR_METHOD_KEY);
        } catch (CoreException e) {
            getLOGGER().error(e);
            return new IMarkerResolution[] {};
        }
        
        if (id == null) {
            return new IMarkerResolution[] {};
        }
        
        switch(id.intValue()) {
        case MarkerConstants.ID_AMBIGUOUS_METHOD:
            break;
        case MarkerConstants.ID_ORPHANED_MUTATOR_METHOD:
        case MarkerConstants.ID_ORPHANED_MODIFY_METHOD:
        case MarkerConstants.ID_ORPHANED_CLEAR_METHOD:
        case MarkerConstants.ID_ORPHANED_ADD_TO_METHOD:
        case MarkerConstants.ID_ORPHANED_REMOVE_FROM_METHOD:
        case MarkerConstants.ID_ORPHANED_VALIDATE_METHOD:
        case MarkerConstants.ID_ORPHANED_VALIDATE_ADD_TO_METHOD:
        case MarkerConstants.ID_ORPHANED_VALIDATE_REMOVE_FROM_METHOD:
        case MarkerConstants.ID_ORPHANED_DISABLE_METHOD:
        case MarkerConstants.ID_ORPHANED_HIDE_METHOD:
        case MarkerConstants.ID_ORPHANED_DEFAULT_METHOD:
        case MarkerConstants.ID_ORPHANED_CHOICES_METHOD:
            if (method == null) { break; }
            return new IMarkerResolution[] {new RemoveMethodResolution(method)};

        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_MUTATOR_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_MODIFY_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_ADD_TO_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_REMOVE_FROM_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_ADD_TO_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_REMOVE_FROM_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_DISABLE_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_HIDE_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_DEFAULT_METHOD:
        case MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_CHOICES_METHOD:
            if (method == null) { break; }
            break;

        case MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_DISABLE_METHOD:
        case MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_HIDE_METHOD:
        case MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_DEFAULT_METHOD:
        case MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_CHOICES_METHOD:
            if (method == null) { break; }
            return new IMarkerResolution[] {new RemoveParametersResolution(method)};

        case MarkerConstants.ID_VALIDATE_PREFIX_INCORRECT:
            if (method == null) { break; }
            break;

        case MarkerConstants.ID_TITLE_NOT_RETURNING_STRING:
            if (method == null) { break; }
            break;
        case MarkerConstants.ID_NO_TITLE_OR_OVERRIDDEN_STRING:
            break;
        }

        return new IMarkerResolution[] {};
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
