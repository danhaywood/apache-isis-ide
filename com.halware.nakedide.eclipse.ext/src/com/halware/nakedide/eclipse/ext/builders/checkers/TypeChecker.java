package com.halware.nakedide.eclipse.ext.builders.checkers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.halware.nakedide.eclipse.core.Constants;
import com.halware.nakedide.eclipse.core.util.MethodUtils;
import com.halware.nakedide.eclipse.core.util.TypeUtils;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;


public class TypeChecker extends AbstractChecker {

	public TypeChecker(IType type) {
		this.type = type;
	}

	private IType type;
    

	public void checkTitle() throws JavaModelException, CoreException {
        
        if (type.isInterface()) {
            return;
        }
        
        
        // ignore anything that isn't an @Entity.
        ASTParser newParser = ASTParser.newParser(AST.JLS3);;
        newParser.setSource(type.getCompilationUnit());
        newParser.setResolveBindings(true);
        ASTNode astNode = newParser.createAST(null);
        CompilationUnit parsedCompilationUnit = (CompilationUnit)astNode;
        TypeDeclaration typeDeclaration = AstUtils.determineTypeDeclaration(parsedCompilationUnit);
        if (!TypeUtils.isEntityType(typeDeclaration.resolveBinding())) {
            return;
        }
        
        
        IMethod titleMethod = titleMethod(type);
        if (titleMethod.exists()) {
            if (!titleMethodReturnsString(titleMethod)) {
                createProblemMarker(
                        titleMethod,
                        MarkerConstants.ID_TITLE_NOT_RETURNING_STRING,
                        "title method must return a String");
            }
            return;
        }
		
        if (toStringMethod(type).exists()) {
            return;
        }
        ITypeHierarchy supertypes = type.newSupertypeHierarchy(null);
        IType[] allSuperclasses = supertypes.getAllSuperclasses(type);
        for(IType superclass: allSuperclasses) {
            if ("Ljava/lang/Object;".equals(superclass.getKey())) {
                continue;
            }
            if (toStringMethod(superclass).exists()) {
                return;
            }
            titleMethod = titleMethod(superclass);
            if (MethodUtils.checkExists(titleMethod) != null) {
                return;
            }
        }
        createProblemMarker(
                type,
                MarkerConstants.ID_NO_TITLE_OR_OVERRIDDEN_STRING,
                "No title or overridden toString");
	}

    /**
     * 
     * @param titleMethod
     * @return true if there is a title method that returns string,
     *              but ALSO if there is no title method at all. 
     */
    private boolean titleMethodReturnsString(
            IMethod titleMethod) {
        if (MethodUtils.checkExists(titleMethod) == null) {
            return true;
        }
        try {
            String returnTypeSig = titleMethod.getReturnType();
            return returnTypeSig.equals("QString;");
        } catch (JavaModelException e) {
            return false;
        }
    }

    private IMethod titleMethod(final IType type) {
        return type.getMethod(Constants.TITLE_METHOD_NAME, new String[]{});
    }

    private IMethod toStringMethod(final IType type) {
        return type.getMethod("toString", new String[]{});
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
