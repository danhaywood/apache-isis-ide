package com.halware.nakedide.eclipse.ext.annot.coll;

import org.apache.log4j.Logger;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeViewEditorContentListener;

public class NakedObjectCollectionsViewEditorContentListener extends AbstractNodeViewEditorContentListener<NakedObjectCollectionsView> {

	private final static Logger LOGGER = Logger.getLogger(NakedObjectCollectionsViewEditorContentListener.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

	public NakedObjectCollectionsViewEditorContentListener() {
		super(NakedObjectCollectionsView.class);
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
