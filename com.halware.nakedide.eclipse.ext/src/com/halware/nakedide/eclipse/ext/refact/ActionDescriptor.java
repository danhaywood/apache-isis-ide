package com.halware.nakedide.eclipse.ext.refact;

import org.eclipse.jdt.core.IMethod;

import com.halware.nakedide.eclipse.core.util.MethodUtils;

/**
 * <pre>
 * RetType Xxx(A, B, C)
 * String  validateXxx(A, B, C)
 * String  disableXxx()
 * boolean hideXxx(A, B, C)     else hideXxx()
 * Object[] defaultXxx(A, B, C) else defaultXxx()    
 * Object[] choicesXxx(A, B, C) else choicesXxx()
 * </pre>
 * 
 * @author dkhaywood
 */
public class ActionDescriptor extends AbstractMemberDescriptor {

	public ActionDescriptor(IMethod method) {
		this(method, method.getElementName());
	}

	public ActionDescriptor(IMethod method, String newMethodName) {
		super(method, newMethodName);
	}

	private IMethod getMethod() {
		return (IMethod)getMember();
	}


	/**
	 * The action method itself.
	 * @return
	 */
	public IMethod getActionMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getActionMethodName(), getParameterTypes()));
	}
	
	/**
	 * The name of the action method itself.
	 * @return
	 */
	public String getActionMethodName() {
		return MethodUtils.actionMethodNameFor(getMemberName());
	}
	
	
	/**
	 * Disable method corresponding to the action represented by {@link #getMemberName()}, if any.
	 * 
	 * @return
	 */
	public String getDisableMethodName() {
		return MethodUtils.disableMethodNameFor(getActionMethodName());
	}

	/**
	 * Validate method corresponding to the action represented by {@link #getMemberName()}, if any.
	 * @return
	 */
	public String getValidateMethodName() {
		return MethodUtils.validateMethodNameFor(getActionMethodName());
	}

    public IMethod getDisableMethod() {
        IMethod methodWithArgs = 
            getDeclaringType().getMethod(
                    getDisableMethodName(), getParameterTypes());
        IMethod methodNoArgs = 
            getDeclaringType().getMethod(
                    getDisableMethodName(), getNoArgParameters());
        return MethodUtils.coalesce(methodWithArgs, methodNoArgs);
    }

	public IMethod getValidateMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getValidateMethodName(), getParameterTypes()));
	}

	
	/**
	 * Hide method corresponding to the action represented by {@link #getMemberName()}, if any.
	 * @return
	 */
	public String getHideMethodName() {
		return MethodUtils.hideMethodNameFor(getActionMethodName());
	}

	public IMethod getHideMethod() {
		IMethod methodWithArgs = 
            getDeclaringType().getMethod(
                    getHideMethodName(), getParameterTypes());
        IMethod methodNoArgs = 
            getDeclaringType().getMethod(
                    getHideMethodName(), getNoArgParameters());
        return MethodUtils.coalesce(methodWithArgs, methodNoArgs);
	}
	
	
	/**
	 * Default name corresponding to the action represented by {@link #getMemberName()}, if any.
	 * @return
	 */
	public String getDefaultMethodName() {
		return MethodUtils.defaultMethodNameFor(getActionMethodName());
	}
	
	/**
	 * Defaults corresponding to the action represented by {@link #getMemberName()}, if any.
	 * @return
	 */
	public IMethod getDefaultMethod() {
		IMethod methodWithArgs = 
            getDeclaringType().getMethod(
                    getDefaultMethodName(), getParameterTypes());
        IMethod methodNoArgs = 
            getDeclaringType().getMethod(
                    getDefaultMethodName(), getNoArgParameters());
        return MethodUtils.coalesce(methodWithArgs, methodNoArgs);
	}

	/**
	 * Choices name corresponding to the action represented by {@link #getMemberName()}, if any.
	 * @return
	 */
	public String getChoicesMethodName() {
		return MethodUtils.choicesMethodNameFor(getActionMethodName());
	}

	/**
	 * Choices corresponding to the action represented by {@link #getMemberName()}, if any.
	 * @return
	 */
	public IMethod getChoicesMethod() {
		IMethod methodWithArgs = 
            getDeclaringType().getMethod(
                    getChoicesMethodName(), getParameterTypes());
        IMethod methodNoArgs = 
            getDeclaringType().getMethod(
                    getChoicesMethodName(), getNoArgParameters());
        return MethodUtils.coalesce(methodWithArgs, methodNoArgs);
	}

	/**
	 * The parameter type signatures of {@link #getMethod()}.
	 * 
	 * @return
	 */
	public String[] getParameterTypes() {
		return getMethod().getParameterTypes();
	}


    public String[] getNoArgParameters() {
        return new String[]{};
    }

	@Override
	public String toString() {
		return String.format(
			"member   : %s\n" + 
			"newName  : %s\n" + 
			"disable  : %s\n" + 
			"validate : %s\n" + 
			"hide     : %s\n" +
			"defaults : %s\n" +
			"choices  : %s\n",
			getMember().getElementName(), 
			getMemberName(),
			getDisableMethodName(),
			getValidateMethodName(), 
			getHideMethodName(),
			getDefaultMethodName(),
			getChoicesMethodName()
			); 
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
