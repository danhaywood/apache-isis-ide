package com.halware.nakedide.eclipse.ext.outline;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.halware.nakedide.eclipse.core.util.MemberType;
import com.halware.nakedide.eclipse.core.util.MethodUtils;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractMember;

public class NakedObjectMember extends AbstractMember {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectMember.class);
	@Override
	public Logger getLOGGER() {
		return LOGGER;
	}


	public NakedObjectMember(
			ICompilationUnit compilationUnit, 
			String memberName, 
			MethodDeclaration methodDeclaration, 
            MemberType memberType,
            Map<String, IMethod> methodsByName) {
		super(compilationUnit, methodDeclaration);
		this.memberName = memberName;
        this.memberType = memberType;
        parent = (TypeDeclaration) methodDeclaration.getParent();
        this.methodsByName = methodsByName;
	}



    private final String memberName;
	public String getMemberName() {
		return memberName;
	}

	private MemberType memberType;
    public MemberType getMemberType() {
        return memberType;
    }
    public String getMemberTypeName() {
        return getMemberType().name().toLowerCase();
    }
    
    private TypeDeclaration parent;

    private Map<String, IMethod> methodsByName;
    private boolean methodExists(
            TypeDeclaration type,
            String methodCandidateName) {
        return methodsByName.containsKey(methodCandidateName);
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null || obj.getClass() != this.getClass()) { return false; }
		return equals((NakedObjectMember)obj);
	}

	public boolean equals(NakedObjectMember obj) {
		return getCompilationUnit() == obj.getCompilationUnit() &&
		       getDeclaration() == obj.getDeclaration();
	}

	@Override
	public int hashCode() {
		return getMemberName().hashCode();
	}


    public boolean isValueType() {
        MethodDeclaration declaration = getDeclaration();
        Type returnType2 = declaration.getReturnType2();
        if (returnType2.isPrimitiveType()) {
            return true;
        }
        if (MethodUtils.isBuiltInValueType(declaration)) {
            return true;
        }
        return false;
    }


    public boolean isStringType() {
        MethodDeclaration declaration = getDeclaration();
        if (MethodUtils.isStringValueType(declaration)) {
            return true;
        }
        return false;
    }

    /**
     * Has a modify (properties only).
     * @return
     */
    public boolean hasModify() {
        if (this.memberType != MemberType.PROPERTY) { return false; }
        String methodCandidateName = MethodUtils.modifyMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }


    /**
     * Has a modify (properties only).
     * @return
     */
    public boolean hasClear() {
        if (this.memberType != MemberType.PROPERTY) { return false; }
        String methodCandidateName = MethodUtils.clearMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }

    /**
     * Has a validate (properties and actions only)
     * 
     * @return
     */
    public boolean hasValidate() {
        if (this.memberType == MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.validateMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }


    /**
     * Has an addTo (collections only)
     * 
     * @return
     */
    public boolean hasAddTo() {
        if (this.memberType != MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.addToMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }

    /**
     * Has an removeFrom (collections only)
     * 
     * @return
     */
    public boolean hasRemoveFrom() {
        if (this.memberType != MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.removeFromMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }


    /**
     * Has an validateAddTo (collections only)
     * 
     * @return
     */
    public boolean hasValidateAddTo() {
        if (this.memberType != MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.validateAddToMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }

    /**
     * Has an validateRemoveFrom (collections only)
     * 
     * @return
     */
    public boolean hasValidateRemoveFrom() {
        if (this.memberType != MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.validateRemoveFromMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }

    /**
     * Has a hide
     * 
     * @return
     */
    public boolean hasHide() {
        String methodCandidateName = MethodUtils.hideMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }
    
    /**
     * Has a disabled.
     * 
     * @return
     */
    public boolean hasDisable() {
        String methodCandidateName = MethodUtils.disableMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }
    
    /**
     * Has a default (properties and actions only)
     * 
     * @return
     */
    public boolean hasDefault() {
        if (this.memberType == MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.defaultMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }
    
    /**
     * Has a choices (properties and actions only)
     * 
     * @return
     */
    public boolean hasChoices() {
        if (this.memberType == MemberType.COLLECTION) { return false; }
        String methodCandidateName = MethodUtils.choicesMethodNameFor(getMemberName());
        return methodExists(parent, methodCandidateName);
    }
    


    public String toString() {
        return getMemberName();
    }


//    private boolean methodExists(
//            TypeDeclaration type,
//            String methodCandidateName) {
//
//        ITypeBinding typeBinding = type.resolveBinding();
//        if (typeBinding == null) {
//            return false;
//        }
//
//        IType domType = (IType) typeBinding.getJavaElement();
//        IMethod method;
//        try {
//            method = TypeUtils.findMethodSearchingSuperclasses(domType, methodCandidateName);
//            return method != null;
//        } catch (JavaModelException e) {
//            getLOGGER().error(e);
//            return false;
//        }
//    }


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
