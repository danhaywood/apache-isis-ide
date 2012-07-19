package com.halware.nakedide.eclipse.ext.annot.tracker;

import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.texteditor.ITextEditor;

import com.halware.nakedide.eclipse.ext.annot.utils.JdtUtil;

public class EditorContent {
	
	public EditorContent(ITextEditor editor) {
		this.editor = editor;
		boolean isValid = true;
		
		if (editor != null) {
			this.openable = JdtUtil.getJavaInput(editor);
			try {
				isValid = openable != null && 
							 openable.getBuffer() != null;
			} catch (JavaModelException e) {
				isValid = false;
			}
		} else {
			this.openable = null;
		}
		this.valid = isValid;
	}

	private ITextEditor editor;
	public ITextEditor getEditor() {
		return editor;
	}


	private final boolean valid;
    /**
     * If not null ({@link #isNull()} returns <tt>false</tt>) and also the
     * openable (from {@link #getOpenable()}) has a buffer (as per 
     * {@link IOpenable#getBuffer()).
     *  
     * @return
     */
	public boolean isValid() {
		return valid;
	}
	
	public boolean isNull() {
		return openable == null;
	}
	
	private IOpenable openable;
	/**
	 * Derived from {{@link #getEditor()}}.
	 * 
	 * <p>
	 * However, will be <tt>null</tt> if there were problems (in which
	 * case {@link #isValid()} will be <tt>false</tt>.
	 *  
	 * @return
	 */
	public IOpenable getOpenable() {
		return openable;
	}
	

	private CompilationUnit parsedCompilationUnit;
	/**
	 * Initially <tt>null</tt>, but is updateable.
	 * 
	 * @return
	 */
	public CompilationUnit getParsedCompilationUnit() {
		return parsedCompilationUnit;
	}
	/**
	 * Intended to be called only by {@link AstJob}.
	 *  
	 * @param parsedCompilationUnit
	 */
	void setParsedCompilationUnit(CompilationUnit parsedCompilationUnit) {
		this.parsedCompilationUnit = parsedCompilationUnit;
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
