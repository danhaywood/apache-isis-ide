package com.halware.nakedide.eclipse.ext.annot.tracker;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;

public class AstJob extends Job {

    private final static Logger LOGGER = Logger.getLogger(AstJob.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

	public AstJob(EditorContent editorContent, IStructuredSelection selectionHint) {
		this(editorContent, selectionHint, true, true);
        setSystem(true); // shouldn't appear in the progress view.
	}

	private AstJob(EditorContent editorContent, IStructuredSelection selectionHint, boolean resolveBindings, boolean statementsRecovery) {
		super( deriveJobName(editorContent) );
		this.editorContent = editorContent;
		this.selectionHint = selectionHint;
		this.resolveBindings = resolveBindings;
		this.statementsRecovery = statementsRecovery;
        this.scheduledAt = Calendar.getInstance().getTime();
	}

	private static String deriveJobName(EditorContent editorContent) {
		IJavaElement openable = (IJavaElement)editorContent.getOpenable();
		return openable!=null?openable.getElementName():"NULL";
	}

	private final EditorContent editorContent;
	public EditorContent getEditorContent() {
		return editorContent;
	}


	private final boolean resolveBindings;
	public boolean isResolveBindings() {
		return resolveBindings;
	}
	private final boolean statementsRecovery;
	public boolean isStatementsRecovery() {
		return statementsRecovery;
	}

    private final Date scheduledAt;
    public Date getScheduledAt() {
        return scheduledAt;
    }

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		getLOGGER().debug("run(): started");
		try {
			if (!editorContent.isValid() ||
				 editorContent.isNull()) {
				return Status.OK_STATUS;
			}
            if (isCancelRequested()) {
                return Status.CANCEL_STATUS;
            }
			ASTParser parser= ASTParser.newParser(AST.JLS3);
			parser.setResolveBindings(isResolveBindings());
			IOpenable openable = editorContent.getOpenable();
			if (openable instanceof ICompilationUnit) {
				parser.setSource((ICompilationUnit) openable);
			} else {
				parser.setSource((IClassFile) openable);
			}
			parser.setStatementsRecovery(isStatementsRecovery());
			CompilationUnit parsedCompilationUnit = (CompilationUnit) parser.createAST(null);
			editorContent.setParsedCompilationUnit( parsedCompilationUnit );

            if (isCancelRequested()) {
                return Status.CANCEL_STATUS;
            }
			
			return Status.OK_STATUS;
		} finally {
			getLOGGER().debug("run(): completed");
		}
	}


	private IStructuredSelection selectionHint;
	public IStructuredSelection getSelectionHint() {
		return selectionHint;
	}

    private boolean cancelRequested = false;
    public void cancelIfPossible() {
        cancel(); // if not yet scheduled
        this.cancelRequested = true;
    }
    public boolean isCancelRequested() {
        return cancelRequested;
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
