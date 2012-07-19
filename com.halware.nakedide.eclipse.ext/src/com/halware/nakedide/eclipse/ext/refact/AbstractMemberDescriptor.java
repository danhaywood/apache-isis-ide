package com.halware.nakedide.eclipse.ext.refact;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;

public abstract class AbstractMemberDescriptor {

	public AbstractMemberDescriptor(IMember member) {
		this(member, member.getElementName());
	}

	public AbstractMemberDescriptor(IMember member, String memberName) {
		this.member = member;
		this.memberName = memberName;
	}

	private IMember member;
	public IMember getMember() {
		return member;
	}

	/**
	 * The member name, actual or pending.
	 * 
	 */
	private String memberName;
	public String getMemberName() {
		return memberName;
	}

	public IType getDeclaringType() {
		return member.getDeclaringType();
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
