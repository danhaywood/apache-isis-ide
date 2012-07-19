package com.halware.nakedide.eclipse.ext.refact;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.halware.nakedide.eclipse.core.util.MethodUtils;

/**
 * Represents a property and its name (possibly pending).
 * 
 * <p>
 * The accessor, mutator, modify and clear names are
 * based on the name rather than then field's current name.  If the name
 * is pending then the equivalents returning {@link IMethod}s will
 * return <tt>null</tt>.  
 */
public class PropertyDescriptor extends AbstractDataMemberDescriptor {

	public PropertyDescriptor(IField field) {
		this(field, field.getElementName());
	}

	public PropertyDescriptor(IField field, String newFieldName) {
		super(field, newFieldName);
	}

	public PropertyDescriptor(IMethod method) {
		this(method, method.getElementName());
	}

	public PropertyDescriptor(IMethod method, String newMethodName) {
		super(method, newMethodName, MethodUtils.calcPropertyName(newMethodName, method));
	}


	/**
	 * Accessor corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getAccessorMethod() {
		return  
			MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getAccessorMethodName(), new String[]{}));
	}
	
	/**
	 * Mutator corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getMutatorMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getMutatorMethodName(), new String[]{getPropertyType()}));
	}


	/**
	 * Modify name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getModifyMethodName() {
		return MethodUtils.modifyMethodNameFor(getPropertyName());
	}
	
	/**
	 * Modify corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getModifyMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getModifyMethodName(), new String[]{getPropertyType()}));
	}

	/**
	 * Clear name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getClearMethodName() {
		return MethodUtils.clearMethodNameFor(getPropertyName());
	}

	/**
	 * Clear corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getClearMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getClearMethodName(), new String[]{}));
	}
	
	
	/**
	 * Disable method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getDisableMethodName() {
		return MethodUtils.disableMethodNameFor(getPropertyName());
	}

	public IMethod getDisableMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getDisableMethodName(), new String[]{}));
	}

	/**
	 * Validate method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getValidateMethodName() {
		return MethodUtils.validateMethodNameFor(getPropertyName());
	}

	public IMethod getValidateMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getValidateMethodName(), new String[]{getPropertyType()}));
	}

	
	/**
	 * Hide method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getHideMethodName() {
		return MethodUtils.hideMethodNameFor(getPropertyName());
	}

	public IMethod getHideMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getHideMethodName(), new String[]{}));
	}

	
	/**
	 * Default method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getDefaultMethodName() {
		return MethodUtils.defaultMethodNameFor(getPropertyName());
	}

	public IMethod getDefaultMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getDefaultMethodName(), new String[]{}));
	}

	

	
	/**
	 * Choices method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getChoicesMethodName() {
		return MethodUtils.choicesMethodNameFor(getPropertyName());
	}

	public IMethod getChoicesMethod() {
		return MethodUtils.checkExists(
				getDeclaringType().getMethod(
					getChoicesMethodName(), new String[]{}));
	}

	/**
	 * Type (if known - requires accessor method to be derivable).
	 * 
	 * @return
	 */
	public String getPropertyType() {
		if (getAccessorMethod() == null) { return null; }
		try {
			return getAccessorMethod().getReturnType();
		} catch (JavaModelException e) {
			return null;
		}
	}


	@Override
	public String toString() {
		return String.format(
			"member   : %s\n" + 
			"newName  : %s\n" + 
			"accessor : %s\n" + 
			"mutator  : %s\n" + 
			"modify   : %s\n" + 
			"clear    : %s\n" +
			"disable  : %s\n" +
			"validate : %s\n" +
			"hide     : %s\n" +
			"default  : %s\n" +
			"choices  : %s\n",
			getMember().getElementName(), 
			getPropertyName(),
			getAccessorMethodName(),
			getMutatorMethodName(), 
			getModifyMethodName(),
			getClearMethodName(),
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
