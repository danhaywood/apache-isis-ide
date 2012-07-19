package com.halware.nakedide.eclipse.ext.outline;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.halware.nakedide.eclipse.core.util.MemberType;
import com.halware.nakedide.eclipse.core.util.MethodNameUtils;
import com.halware.nakedide.eclipse.core.util.TypeUtils;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodesContentProvider;
import com.halware.nakedide.eclipse.ext.annot.common.ICompilationUnitOwner;

public class OutlineViewContentProvider 
    extends AbstractNodesContentProvider {
    
    private final static Logger LOGGER = Logger.getLogger(OutlineViewContentProvider.class);
    @Override
    public Logger getLOGGER() {
        return LOGGER;
    }
    
    public OutlineViewContentProvider(ICompilationUnitOwner compilationUnitOwner) {
        super(compilationUnitOwner);
    }

    protected Object[] doGetElements(TypeDeclaration typeDeclaration) {

        Map<String,IMethod> methodsByName = new TreeMap<String,IMethod>();
        ITypeBinding typeBinding = typeDeclaration.resolveBinding();
        if (typeBinding == null) {
            return new Object[]{};
        }
        IType type = (IType) typeBinding.getJavaElement();
        appendMethods(type, methodsByName);

        Map<String,NakedObjectMember> membersByName = new TreeMap<String,NakedObjectMember>();
        for(Object bodyDeclarationObj: typeDeclaration.bodyDeclarations()) {
            
            BodyDeclaration bodyDeclaration = (BodyDeclaration) bodyDeclarationObj;
            if (!(bodyDeclaration instanceof MethodDeclaration)) {
                continue;
            }
            
            MethodDeclaration methodDeclaration = (MethodDeclaration)bodyDeclaration;
            MemberType memberType = null;
            
            String memberName = MethodNameUtils.asPropertyName(methodDeclaration);
            if (memberName != null) {
                memberType = MemberType.PROPERTY;
            } else {
                memberName = MethodNameUtils.asCollectionName(methodDeclaration);
                if (memberName != null) {
                    memberType = MemberType.COLLECTION;
                } else {
                    memberName = MethodNameUtils.asActionName(methodDeclaration);
                    if (memberName != null) {
                        memberType = MemberType.ACTION;
                    } else {
                        memberName = MethodNameUtils.asReservedMethodName(methodDeclaration);
                        if (memberName != null) {
                            memberType = MemberType.RESERVED;
                        }
                    }
                }
            }

            if (memberName == null) {
                continue;
            }

            NakedObjectMember nakedObjectMember = membersByName.get(memberName);
            if (nakedObjectMember == null) {
                nakedObjectMember = new NakedObjectMember(
                    getCompilationUnitOwner().getCompilationUnit(), 
                    memberName, methodDeclaration, memberType, methodsByName);
                membersByName.put(nakedObjectMember.getMemberName(), nakedObjectMember);
            }
        }
        
        return membersByName.values().toArray();
    }
    
    private void appendMethods(
            IType type,
            Map<String, IMethod> methodsByName) {
        IMethod[] methods;
        try {
            methods = type.getMethods();
            for(int i=0; i<methods.length; i++) {
                methodsByName.put(methods[i].getElementName(), methods[i]);
            }
            IType superType = TypeUtils.findSuperType(type);
            if (superType != null) {
                appendMethods(superType, methodsByName);
            }
        } catch (JavaModelException e) {
            getLOGGER().error("Unable to find methods from (DOM) type", e);
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
