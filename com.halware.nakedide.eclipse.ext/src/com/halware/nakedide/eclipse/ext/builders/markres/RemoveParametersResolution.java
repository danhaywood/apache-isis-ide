package com.halware.nakedide.eclipse.ext.builders.markres;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

/**
 * Removes all parameters from a method.
 * 
 * @author dkhaywood
 */
@SuppressWarnings("restriction")
public class RemoveParametersResolution extends AbstractMarkedMethodResolution {

    private final static Logger LOGGER = Logger.getLogger(RemoveParametersResolution.class);
    public Logger getLOGGER() {
        return LOGGER;
    }
    
    public RemoveParametersResolution(
            final IMethod method) {
        super(method);
    }

    public String getLabel() {
        String[] parameterNames;
        boolean justOneParameter = false;
        try {
            parameterNames = getMethod().getParameterNames();
            justOneParameter = parameterNames.length == 1;
        } catch (JavaModelException e) {
            getLOGGER().warn(e);
        }
        return "Remove parameter" + (justOneParameter?"":"s");
    }

    public void run(
            IMarker marker) {

        if (getMethodDeclaration() == null) {
            return;
        }
        try {
            List<ASTNode> parameters = 
                Generics.asT(getMethodDeclaration().parameters());
            AstUtils.rewriteReplace(getParser(), getCompilationUnit(), parameters, null);
            selectInEditor();
        } catch (JavaModelException e) {
            getLOGGER().error(e);
        } catch (MalformedTreeException e) {
            getLOGGER().error(e);
        } catch (BadLocationException e) {
            getLOGGER().error(e);
        }

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
