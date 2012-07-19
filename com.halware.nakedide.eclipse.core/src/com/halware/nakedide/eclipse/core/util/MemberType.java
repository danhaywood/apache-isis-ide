package com.halware.nakedide.eclipse.core.util;

public enum MemberType {
    PROPERTY,
    COLLECTION,
    ACTION,
    RESERVED;
    
    public final static MemberType[] ALL = new MemberType[]{PROPERTY, COLLECTION, ACTION}; 
    public final static MemberType[] PROPERTY_ONLY = new MemberType[]{PROPERTY}; 
    public final static MemberType[] COLLECTION_ONLY = new MemberType[]{COLLECTION}; 
    public final static MemberType[] PROPERTY_AND_ACTION = new MemberType[]{PROPERTY, ACTION}; 

    public boolean within(
            MemberType[] memberTypes) {
        for(MemberType eachMemberType: memberTypes) {
            if (this == eachMemberType) { return true; }
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
