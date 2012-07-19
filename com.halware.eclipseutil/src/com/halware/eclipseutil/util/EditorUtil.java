package com.halware.eclipseutil.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.texteditor.ITextEditor;

public final class EditorUtil {
	
	private EditorUtil() {
	}

	public static IEditorPart getActiveEditor() {
		return WorkbenchUtil.getActiveEditor();
	}
	
	
	public static void selectInEditor(ITextEditor editor, int offset, int length) {
        if (editor == null) {
            return;
        }
        
		IEditorPart active = getActiveEditor();
        
        // try to activate this editor if not active
        // (but give up if unable to do so).
		if (active != editor) {
			IWorkbenchPartSite site = editor.getSite();
            if (site == null) {
                return;
            }
            IWorkbenchPage page = site.getPage();
            if (page == null) {
                return;
            }
            page.activate(editor);
		}
        
		editor.selectAndReveal(offset, length);
	}
}
