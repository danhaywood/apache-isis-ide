/**
 * 
 */
package com.halware.nakedide.eclipse.ext.annot.tracker;

import org.apache.log4j.Logger;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.IPath;

public class TextFileBufferListener implements IFileBufferListener {

	private final static Logger LOGGER = Logger.getLogger(TextFileBufferListener.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	private final ITextFileBufferOwner textFileBufferOwner;

	/**
	 * @param textFileBufferOwner
	 */
	public TextFileBufferListener(ITextFileBufferOwner textFileBufferOwner) {
		this.textFileBufferOwner = textFileBufferOwner;
	}

	public void bufferCreated(IFileBuffer buffer) {
	}

	public void bufferDisposed(IFileBuffer buffer) {
		if (buffer instanceof ITextFileBuffer) {
			ITextFileBuffer textFileBuffer = (ITextFileBuffer) buffer;
			getLOGGER().debug("bufferDisposed");
			this.textFileBufferOwner.bufferDisposed(textFileBuffer);
		}
	}

	public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
	}

	public void bufferContentReplaced(IFileBuffer buffer) {
	}

	public void stateChanging(IFileBuffer buffer) {
	}

	public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
	}

	public void stateValidationChanged(IFileBuffer buffer, boolean isStateValidated) {
	}

	public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
	}

	public void underlyingFileDeleted(IFileBuffer buffer) {
	}

	public void stateChangeFailed(IFileBuffer buffer) {
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
