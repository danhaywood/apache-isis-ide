package com.halware.nakedide.eclipse.ext.annot.common;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.texteditor.ITextEditor;

import com.halware.nakedide.eclipse.ext.Activator;
import com.halware.nakedide.eclipse.ext.annot.tracker.EditorContent;
import com.halware.nakedide.eclipse.ext.annot.tracker.IEditorContentListener;

public abstract class AbstractNodeViewEditorContentListener<T extends AbstractNodeView> implements IEditorContentListener {

	public abstract Logger getLOGGER();

	public AbstractNodeViewEditorContentListener(Class<T> viewClass) {
		this.viewClass = viewClass;
	}
	
	private final Class<T> viewClass;
	public Class<T> getViewClass() {
		return viewClass;
	}

	public void editorContentAboutToChange(EditorContent editorContent) {
		AbstractNodeView view = getView(getPage(editorContent));
		if (view == null) {
			return;
		}
		view.editorContentAboutToChange(editorContent);
	}

	public void editorContentChanged(EditorContent editorContent, IStructuredSelection selectionHint) {
		AbstractNodeView view = getView(getPage(editorContent));
		if (view == null) {
			return;
		}
		view.editorContentChanged(editorContent, selectionHint);
	}

	private T getView(IWorkbenchPage page) {
		return Activator.getDefault().getView(page, viewClass);
	}


	private IWorkbenchPage getPage(EditorContent editorContent) {
		if (!editorContent.isValid() || editorContent.isNull()) {
			return null;
		}
		ITextEditor editor = editorContent.getEditor();
		// belt-n-braces
		if (editor == null) {
			return null;
		}
		IWorkbenchPartSite site = editor.getSite();
		// more belt-n-braces
		if (site == null) {
			return null;
		}
		return site.getPage();
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
