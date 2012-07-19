package com.halware.nakedide.eclipse.core.templates.propl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public abstract class AbstractCompltnProplComputer implements
		IJavaCompletionProposalComputer {

    public AbstractCompltnProplComputer(
            GenericTemplCompltnProcessor processor) {
        this.processor = processor;
    }
    
	private final GenericTemplCompltnProcessor processor;


    public List computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		if (!(context instanceof JavaContentAssistInvocationContext)) {
			return Collections.EMPTY_LIST;
		}
		JavaContentAssistInvocationContext javaContext = (JavaContentAssistInvocationContext)context;
        
		ITextViewer viewer = javaContext.getViewer();
		int offset = javaContext.getInvocationOffset();
		ICompletionProposal[] computeCompletionProposals = 
			processor.computeCompletionProposals(viewer, offset);
		return Arrays.asList(computeCompletionProposals);
	}


	public List computeContextInformation(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Collections.EMPTY_LIST;
	}

	public String getErrorMessage() {
		return null;
	}

	public void sessionEnded() {
	}

	public void sessionStarted() {
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
