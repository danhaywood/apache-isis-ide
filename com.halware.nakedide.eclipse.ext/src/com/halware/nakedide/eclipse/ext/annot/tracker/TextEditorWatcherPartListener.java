package com.halware.nakedide.eclipse.ext.annot.tracker;

import org.apache.log4j.Logger;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * 
 */
public class TextEditorWatcherPartListener implements IPartListener2 {

	private final static Logger LOGGER = Logger.getLogger(TextEditorWatcherPartListener.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	private final ITextEditorWatcher textEditorWatcher;
	public TextEditorWatcherPartListener(ITextEditorWatcher textEditorWatcher) {
		this.textEditorWatcher = textEditorWatcher;
	}
	
	public void partVisible(IWorkbenchPartReference partRef) {
	}
	public void partHidden(IWorkbenchPartReference partRef) {
	}
	public void partOpened(IWorkbenchPartReference partRef) {
		getLOGGER().debug("Opened part: " + partRef.getContentDescription());
	}
	public void partActivated(IWorkbenchPartReference partRef) {
		if (!isEditor(partRef)) {
			getLOGGER().debug("Activated part (not editor): " + partRef.getContentDescription());
			return;
		}
		getLOGGER().debug("Activated editor: " + partRef.getContentDescription());
		ITextEditor textEditor = asEditor(partRef);
		textEditorWatcher.textEditorActivated(textEditor);
	}
	public void partDeactivated(IWorkbenchPartReference partRef) {
		getLOGGER().debug("Dectivated part: " + partRef.getContentDescription());
	}
	public void partClosed(IWorkbenchPartReference partRef) {
		if (!isEditor(partRef)) {
			getLOGGER().debug("Closed part (not editor): " + partRef.getContentDescription());
			return;
		}
		if (!isCurrentEditor(partRef)) {
			getLOGGER().debug("Closed editor (not current): " + partRef.getContentDescription());
			return;
		}
		getLOGGER().debug("Closed current editor: " + partRef.getContentDescription());
		textEditorWatcher.textEditorClosed();
	}
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	

	///////////////// Helpers ///////////////////
	
	private boolean isCurrentEditor(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		return textEditorWatcher.getEditor() != null && 
		       textEditorWatcher.getEditor() == part;
	}
	private ITextEditor asEditor(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		if (!(part instanceof ITextEditor)) { return null; }
		return (ITextEditor)part;
	}
	private boolean isEditor(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		return part instanceof IEditorPart;
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
