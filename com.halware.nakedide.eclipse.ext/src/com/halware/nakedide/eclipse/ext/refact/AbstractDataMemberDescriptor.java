package com.halware.nakedide.eclipse.ext.refact;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;

import com.halware.nakedide.eclipse.core.util.MethodUtils;

public abstract class AbstractDataMemberDescriptor extends AbstractMemberDescriptor {

	public AbstractDataMemberDescriptor(IMember member) {
		super(member);
		this.propertyName = getMemberName();
	}

	public AbstractDataMemberDescriptor(IMember member, String newMemberName) {
		this(member, newMemberName, newMemberName);
	}
	
	public AbstractDataMemberDescriptor(IMember member, String newMemberName, String propertyName) {
		super(member, newMemberName);
		this.propertyName = propertyName; 
	}

	public IField getField() {
		if (getMember() instanceof IField) {
			return (IField)getMember();
		}
		return MethodUtils.checkExists(
			getDeclaringType().getField(getFieldName()));
	}

	public String getFieldName() {
		return getPropertyName();
	}

	private final String propertyName;
	public String getPropertyName() {
		return propertyName;
	}



	/**
	 * Property name corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getPropertyNameUpperCaseFirst() {
		return MethodUtils.propertyNameUpperCaseFirst(getPropertyName());
	}
	
	/**
	 * Accessor name corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getAccessorMethodName() {
		return MethodUtils.accessorMethodNameFor(getPropertyName());
	}

	/**
	 * Accessor corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public abstract IMethod getAccessorMethod();


	/**
	 * Mutator name corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public String getMutatorMethodName() {
		return MethodUtils.mutatorMethodNameFor(getPropertyName());
	}


	/**
	 * Mutator corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public abstract IMethod getMutatorMethod();

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
