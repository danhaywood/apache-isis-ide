package com.halware.nakedide.eclipse.ext.annot.tracker;

import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Allows a part (usually a view) to be notified of its own state
 * and also that of the text editor.
 * 
 * <p>
 *  
 * @author dkhaywood
 *
 */
public interface ITextEditorWatcher {

	/**
	 * An (already-opened) {@link ITextEditor} has just been activated.
	 * 
	 * <p>
	 * The {@link #getEditor()} should return the supplied
	 * editor afterwards.
	 * 
	 * @param textEditor
	 */
	public abstract void textEditorActivated(ITextEditor textEditor);

	/**
	 * Called if the {@link ITextEditor} part just closed is the
	 * same as that returned by {@link #getEditor()}. 
	 * 
	 * <p>
	 * The {@link #getEditor()} should return <tt>null</tt>
	 * afterwards.
	 */
	public abstract void textEditorClosed();

	public abstract ITextEditor getEditor();

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
