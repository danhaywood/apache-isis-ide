/*******************************************************************************
 * Copyright (c) 2004 Coffee-Bytes.com and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/cpl.php
 * 
 * Contributors:
 *     Coffee-Bytes.com - initial API and implementation
 *******************************************************************************/
package com.cb.eclipse.folding;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.ui.text.DocumentCharacterIterator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.projection.IProjectionPosition;

@SuppressWarnings("restriction")
public class EnhancedPosition2 extends EnhancedPosition implements IProjectionPosition {
	
	public EnhancedPosition2(int start, int length, PositionMetadata metadata) {
		super(start, length, metadata);
	}

	

	public int computeCaptionOffset(IDocument document) throws BadLocationException {
		DocumentCharacterIterator sequence= new DocumentCharacterIterator(document, offset, offset + length);
		return findFirstContent(sequence, 0);
	}



	public IRegion[] computeProjectionRegions(IDocument document) throws BadLocationException {
		DocumentCharacterIterator sequence= new DocumentCharacterIterator(document, offset, offset + length);
		int prefixEnd= 0;
		int contentStart= findFirstContent(sequence, prefixEnd);

		int firstLine= document.getLineOfOffset(offset + prefixEnd);
		int captionLine= document.getLineOfOffset(offset + contentStart);
		int lastLine= document.getLineOfOffset(offset + length);

		Assert.isTrue(firstLine <= captionLine, "first folded line is greater than the caption line"); //$NON-NLS-1$
		Assert.isTrue(captionLine <= lastLine, "caption line is greater than the last folded line"); //$NON-NLS-1$

		IRegion preRegion;
		if (firstLine < captionLine) {
//			preRegion= new Region(offset + prefixEnd, contentStart - prefixEnd);
			int preOffset= document.getLineOffset(firstLine);
			IRegion preEndLineInfo= document.getLineInformation(captionLine);
			int preEnd= preEndLineInfo.getOffset();
			preRegion= new Region(preOffset, preEnd - preOffset);
		} else {
			preRegion= null;
		}

		if (captionLine < lastLine) {
			int postOffset= document.getLineOffset(captionLine + 1);
			IRegion postRegion= new Region(postOffset, offset + length - postOffset);

			if (preRegion == null)
				return new IRegion[] { postRegion };

			return new IRegion[] { preRegion, postRegion };
		}

		if (preRegion != null)
			return new IRegion[] { preRegion };

		return null;
	}
	
	/**
	 * Finds the offset of the first identifier part within <code>content</code>.
	 * Returns 0 if none is found.
	 *
	 * @param content the content to search
	 * @return the first index of a unicode identifier part, or zero if none can
	 *         be found
	 */
	private int findFirstContent(final CharSequence content, int prefixEnd) {
		int lenght= content.length();
		for (int i= prefixEnd; i < lenght; i++) {
			if (Character.isUnicodeIdentifierPart(content.charAt(i)))
				return i;
		}
		return 0;
	}


}