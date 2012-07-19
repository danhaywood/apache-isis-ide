package com.halware.nakedide.eclipse.ext.refact;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.halware.nakedide.eclipse.core.util.MethodUtils;

/**
 * Represents a collection and its name (possibly pending).
 * 
 * <p>
 * The accessor, mutator, addTo and removeFrom names are
 * based on the name rather than then field's current name.  If the name
 * is pending then the equivalents returning {@link IMethod}s will
 * return <tt>null</tt>.  
 * 
 * @author dkhaywood
 *
 */
public class CollectionDescriptor extends AbstractDataMemberDescriptor {

	public CollectionDescriptor(IField field) {
		this(field, field.getElementName());
	}

	public CollectionDescriptor(IField field, String newFieldName) {
		super(field, newFieldName);
	}

	public CollectionDescriptor(IMethod method) {
		this(method, method.getElementName());
	}

	public CollectionDescriptor(IMethod method, String newMethodName) {
		super(method, newMethodName, MethodUtils.calcCollectionName(newMethodName, method));
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
				getMutatorMethodName(), new String[]{getCollectionTypeSig()}));
	}


	/**
	 * AddTo method name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getAddToMethodName() {
		return MethodUtils.addToMethodNameFor(getPropertyName());
	}
	
	/**
	 * AddTo method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getAddToMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getAddToMethodName(),
				paramTypeSigs()));
	}

	/**
	 * RemoveFrom method name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getRemoveFromMethodName() {
		return MethodUtils.removeFromMethodNameFor(getPropertyName());
	}

	/**
	 * RemoveFrom method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getRemoveFromMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getRemoveFromMethodName(),
				paramTypeSigs()));
	}
	
	/**
	 * Disable method name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getDisableMethodName() {
		return MethodUtils.disableMethodNameFor(getPropertyName());
	}

	/**
	 * Disable method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getDisableMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getDisableMethodName(), new String[]{}));
	}
	
	
	/**
	 * ValidateAddTo method name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getValidateAddToMethodName() {
		return MethodUtils.validateAddToMethodNameFor(getPropertyName());
	}

	/**
	 * ValidateAddTo method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getValidateAddToMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getValidateAddToMethodName(),
				paramTypeSigs()));
	}

	
	/**
	 * ValidateRemoveFrom method name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getValidateRemoveFromMethodName() {
		return MethodUtils.validateRemoveFromMethodNameFor(getPropertyName());
	}

	/**
	 * ValidateRemoveFrom method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getValidateRemoveFromMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getValidateRemoveFromMethodName(), 
				paramTypeSigs()));
	}

	
	/**
	 * Hide method name corresponding to the {@link #getPropertyName()}.
	 * @return
	 */
	public String getHideMethodName() {
		return MethodUtils.hideMethodNameFor(getPropertyName());
	}

	/**
	 * Hide method corresponding to the {@link #getPropertyName()}, if any.
	 * @return
	 */
	public IMethod getHideMethod() {
		return MethodUtils.checkExists(
			getDeclaringType().getMethod(
				getHideMethodName(), new String[]{}));
	}

	/**
	 * The type of the collection, if known, as a signature.
	 * 
	 * <p>
	 * Requires accessor method to be derivable.
	 * 
	 * @see #getReferencedType()
	 * 
	 * @return
	 */
	public String getCollectionTypeSig() {
		if (getAccessorMethod() == null) { return null; }
		try {
			return getAccessorMethod().getReturnType();
		} catch (JavaModelException e) {
			return null;
		}
	}
	
	/**
	 * The type of object referenced by the collection field, if known.
	 * 
	 * <p>
	 * For example, a <tt>Customer</tt>, <tt>com.mycompany.Order</tt> or <tt>Product</tt>.
	 * 
	 * <p>
	 * Requires accessor method to be derivable.  Note that may be fully qualified.
	 * 
	 * @see #getCollectionType()
	 * 
	 * @return
	 */
	public String getReferencedType() {
		if (!(getMember() instanceof IField)) {
			return null;
		}
		IField field = (IField)getMember();
		return MethodUtils.collectionType(field);
	}
	
	/**
	 * Returns the referenced type signature for the collection.
	 * 
	 * <p>
	 * If the underlying member is a field, just uses the 
	 * Returns the referencedType signature for the collection, based on the 
	 * return type of the {@link #getAccessorMethod()}.
	 * 
	 * @return
	 */
	public String getReferencedTypeSig() {
		if (getMember() instanceof IField) {
			IField field = (IField)getMember();
			return MethodUtils.toUnresolvedSignature(
					MethodUtils.collectionType(field));
		}
		if (getMember() instanceof IMethod) {
			IMethod accessorMethod = getAccessorMethod();
			if (accessorMethod == null) {
				return null;
			}
			try {
				return MethodUtils.collectionTypeQSig(accessorMethod.getReturnType());
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	

	/**
	 * If the referencedType for the collection could be determined, then return as
	 * a 1-element array of strings (eg to locate addTo/removeFrom etc).
	 * 
	 * @return
	 */
	private String[] paramTypeSigs() {
		String referencedTypeSig = getReferencedTypeSig();
		return referencedTypeSig!=null?
				new String[]{referencedTypeSig}:new String[]{};
	}
	

	@Override
	public String toString() {
		return String.format(
			"member    : %s\n" + 
			"newName   : %s\n" + 
			"accessor  : %s\n" + 
			"mutator   : %s\n" + 
			"addTo     : %s\n" + 
			"removeFrom: %s\n" +
			"disable   : %s\n" +
			"validateAddTo     : %s\n" + 
			"validateRemoveFrom: %s\n" +
			"hide      : %s\n",
			getMember().getElementName(), 
			getPropertyName(),
			getAccessorMethodName(),
			getMutatorMethodName(), 
			getAddToMethodName(),
			getRemoveFromMethodName(),
			getDisableMethodName(),
			getValidateAddToMethod(),
			getValidateRemoveFromMethodName(),
			getHideMethodName() ); 
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
