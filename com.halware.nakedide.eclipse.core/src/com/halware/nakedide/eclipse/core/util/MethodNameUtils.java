package com.halware.nakedide.eclipse.core.util;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import com.halware.nakedide.eclipse.core.Constants;

public class MethodNameUtils {
    
    private MethodNameUtils() {}

    public final static String[] RESERVED_METHOD_NAMES = new String[]{
        "title",
        "iconName",
        "creating",
        "created",
        "loading",
        "loaded",
        "updating",
        "updated",
        "deleting",
        "deleted",
        "validate",
    };

    public static String asReservedMethodName(MethodDeclaration methodDeclaration) {
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
        
        int numParameters = methodDeclaration.parameters().size();
        if (numParameters != 0) {
            return null;
        }
        
        String methodName = methodDeclaration.getName().getFullyQualifiedName();
        for(String reservedMethodName: RESERVED_METHOD_NAMES) {
            if (reservedMethodName.equals(methodName)) {
                return methodName;
            }
        }
        return null;
    }

    /**
     * Returns the property name for the method declaration, or (more likely
     * to be the case) <tt>null</tt> if the method doesn't represent the accessor of a property.
     *  
     * @param methodDeclaration
     * @return
     */
    public static String asPropertyName(MethodDeclaration methodDeclaration) {
    	
    	SimpleName name = methodDeclaration.getName();
    	String methodName = name.getFullyQualifiedName();
    	
    	
    	// determine if accessor for a property or collection
    	String candidatePropertyName = MethodUtils.unprefixed(methodName, Constants.PREFIX_GET);
    	if (candidatePropertyName == null) {
    		return null;
    	}
    
        // check is public
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }

        // check has no parameters
        if (methodDeclaration.parameters().size() > 0) {
            return null;
        }

        // check not actually a collection
        if (MethodNameUtils.asCollectionName(methodDeclaration) != null) {
            return null;
        }

        // check not a log4j logger either
        if (MethodNameUtils.isLog4jLogger(methodDeclaration)) {
            return null;
        }

        // after lobbying, we no longer care if the object is an @Entity or @Value etc.  
        // show it anyway.
    	
    	return candidatePropertyName;
    }

    /**
     * Returns the property name for the method declaration, or (more likely
     * to be the case) <tt>null</tt> if the method doesn't represent the accessor of a property.
     *  
     * @param methodDeclaration
     * @return
     */
    public static String asCollectionName(MethodDeclaration methodDeclaration) {
    	
    	IMethodBinding methodBinding = methodDeclaration.resolveBinding();
    	if (methodBinding == null) {
    		return "";
    	}
    	String methodName = methodBinding.getName();
    
    	// determine if accessor for a property or collection
    	String candidateCollectionName = MethodUtils.unprefixed(methodName, Constants.PREFIX_GET);
    	if (candidateCollectionName == null) {
    		return null;
    	}
    
        // check is public
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
    
        // check has no parameters
        if (methodDeclaration.parameters().size() > 0) {
            return null;
        }
    
    	// check the return type is a collection...
    	ITypeBinding returnType = methodBinding.getReturnType();
    	ITypeBinding accessorReturnCollectionType = returnType.getTypeDeclaration();
    	String qualifiedName = accessorReturnCollectionType.getQualifiedName();
        if (!MethodUtils.isCollectionType(qualifiedName)) {
    		return null;
    	}
        
        // ... and that has a single parameter
        
        // we no longer check that type is an @Entity
        // (might reintroduce as an option later)
//        ITypeBinding[] typeArguments = returnType.getTypeArguments();
//        switch (typeArguments.length) {
//        case 0:
//            // no check
//            break;
//        case 1:
//            ITypeBinding typeArgument = typeArguments[0];
//            if (!TypeUtils.isEntityType(typeArgument)) {
//                return null;
//            }
//            break;
//        default:
//            // cannot have 2 arguments.
//            return null;    
//        }
    	
    	return candidateCollectionName;
    }

    /**
     * Returns the action name for the method declaration, or (more likely
     * to be the case) <tt>null</tt> if the method doesn't represent the action method
     * of an action.
     *  
     * @param methodDeclaration
     * @return
     */
    public static String asActionName(MethodDeclaration methodDeclaration) {
    
        // check is public
        if (!MethodUtils.isPublic(methodDeclaration)) {
            return null;
        }
        
    	String methodName = methodDeclaration.getName().getFullyQualifiedName();
    
    	// determine if accessor for a property or collection
    	String anyPrefix = MethodUtils.getPrefix(methodName);
    	if (anyPrefix != null) {
    		return null;
    	}
    
        // check not reserved name
        if (asReservedMethodName(methodDeclaration) != null) {
            return null;
        }
        
    
    	return methodName;
    }
    
    public static boolean isLog4jLogger(MethodDeclaration methodDeclaration) {
        
        IMethodBinding methodBinding = methodDeclaration.resolveBinding();
        if (methodBinding == null) {
            return false;
        }

        // check the return type
        ITypeBinding returnType = methodBinding.getReturnType();
        String qualifiedName = returnType.getQualifiedName();
        
        return "org.apache.log4j.Logger".equals(qualifiedName);
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
