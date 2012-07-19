package com.halware.nakedide.eclipse.core.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.BindingKey;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeUtils {
    
    private TypeUtils() {}

    /**
     * @param type
     * @return
     * @throws JavaModelException
     */
    public static List<IType> findSuperTypeCandidates(
            IType type) throws JavaModelException {
        List<IType> candidates = new ArrayList<IType>();
        String superclassTypeSignature = type.getSuperclassTypeSignature();
        if (superclassTypeSignature != null) {
            String qualifier = Signature.getSignatureQualifier(superclassTypeSignature);
            String simpleName = Signature.getSignatureSimpleName(superclassTypeSignature);
            String superclassName = ((qualifier != null && qualifier.length() > 0)?qualifier+".":"") + simpleName; 
            String[][] superclassCandidates = type.resolveType(superclassName);
            if (superclassCandidates != null) {
                for(int i=0; i< superclassCandidates.length; i++) {
                    String superclassCandidatePackageName = superclassCandidates[i][0];
                    String superclassCandidateClassName = superclassCandidates[i][1];
                    IType findType = type.getJavaProject().findType(superclassCandidatePackageName, superclassCandidateClassName);
                    if (findType != null) {
                        candidates.add(findType);
                    }
                }
            }
        }
        return candidates;
    }

    /**
     * Returns the first candidate as a supertype for the given type.
     * 
     * <p>
     * Note that there could be more than one candidate, in which case use 
     * {@link findSuperTypeCandidates}. 
     * 
     * @param type
     * @return
     * @throws JavaModelException
     */
    public static IType findSuperType(
            IType type) throws JavaModelException {
        List<IType> findSuperTypeCandidates = findSuperTypeCandidates(type);
        return findSuperTypeCandidates.size() > 0?findSuperTypeCandidates.get(0): null;
    }

    /**
     * Returns a method that is known to exist, else <tt>null</tt>.
     * 
     * @param typeCandidate
     * @param methodCandidateName
     * @param methodParamTypes TODO
     * @return
     * @throws JavaModelException
     */
    public static IMethod findMethodSearchingSuperclasses(
            IType typeCandidate,
            String methodCandidateName, 
            String[] methodParamTypes) throws JavaModelException {
        IMethod methodCandidate = typeCandidate.getMethod(methodCandidateName, methodParamTypes);
        IMethod checkExists = MethodUtils.checkExists(methodCandidate);
        if (checkExists != null) {
            return methodCandidate;
        }
        IType superclassType = findSuperType(typeCandidate);
        if (superclassType != null) {
            return findMethodSearchingSuperclasses(superclassType, methodCandidateName, methodParamTypes);
        }
        return null;
    }

    /**
     * Similar to {@link #findMethodSearchingSuperclasses(IType, String, String[])}, but ignores
     * any parameter types.
     * 
     * @param typeCandidate
     * @param methodCandidateName
     * @return
     * @throws JavaModelException
     */
    public static IMethod findMethodSearchingSuperclasses(
            IType typeCandidate,
            String methodCandidateName) throws JavaModelException {
        IMethod[] methods = typeCandidate.getMethods();
        for(int i=0; i<methods.length; i++) {
            IMethod methodCandidate = methods[i];
            if (methodCandidate.getElementName().equals(methodCandidateName)) {
                return methodCandidate;
            }
        }
        IType superclassType = findSuperType(typeCandidate);
        if (superclassType != null) {
            return findMethodSearchingSuperclasses(superclassType, methodCandidateName);
        }
        return null;
    }

    @SuppressWarnings("restriction")
    public static boolean isEntityType(ITypeBinding typeBinding) {
        return TypeUtils.hasAnnotation(typeBinding, "javax.persistence.Entity");
    }

    public static boolean isValueType(ITypeBinding typeBinding) {
        return TypeUtils.hasAnnotation(typeBinding, "org.nakedobjects.applib.Value");
    }

    @SuppressWarnings("restriction")
    static boolean hasAnnotation(
            ITypeBinding typeBinding,
            String annotationTypeName) {
        
        if (typeBinding == null) {
            return false;
        }
    
        String annotationTypeSig = ("L" + annotationTypeName + ";");
    
        IAnnotationBinding[] annotations = typeBinding.getAnnotations();
        for(IAnnotationBinding annotationBinding: annotations) {
            IJavaElement javaElement = annotationBinding.getJavaElement();
            if (javaElement == null) {
                continue;
            }
            if (!(javaElement instanceof org.eclipse.jdt.internal.core.ResolvedBinaryType)) {
                continue;
            }
            org.eclipse.jdt.internal.core.ResolvedBinaryType rbt = (org.eclipse.jdt.internal.core.ResolvedBinaryType) javaElement;
            BindingKey bindingKey = new BindingKey(rbt.getKey());
            if (annotationTypeSig.equals(bindingKey.toSignature())) {
                return true;
            }
        }
        return false;
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
