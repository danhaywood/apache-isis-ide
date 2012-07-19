package com.halware.nakedide.eclipse.ext.refact.renameField;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;

import com.halware.nakedide.eclipse.ext.refact.ActionDescriptor;
import com.halware.nakedide.eclipse.ext.refact.RefactoringSupport;

@SuppressWarnings("restriction")
public class RenameActionRefactoring extends AbstractRenameRefactoring {

	private ActionDescriptor oldActionDescriptor;
	public ActionDescriptor getOldActionDescriptor() {
		return oldActionDescriptor;
	}

	private ActionDescriptor newActionDescriptor;
	public ActionDescriptor getNewActionDescriptor() {
		return newActionDescriptor;
	}


	public RenameActionRefactoring(
			IMethod method, String newMethodName, 
			TextChangeManager textChangeManager) {
		super(textChangeManager);
		this.oldActionDescriptor = new ActionDescriptor(method);
		this.newActionDescriptor = new ActionDescriptor(method, newMethodName);
	}

	private RefactoringProcessor actionMethodProcessor; 
	private RefactoringProcessor disableMethodProcessor; 
	private RefactoringProcessor validateMethodProcessor; 
	private RefactoringProcessor hideMethodProcessor; 
	private RefactoringProcessor defaultMethodProcessor;
	private RefactoringProcessor choicesMethodProcessor;

	public void checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException {
        
		IMethod actionMethod = getOldActionDescriptor().getActionMethod();
		if (actionMethod != null) {
			actionMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					actionMethod, 
					getNewActionDescriptor().getActionMethodName(), 
					getTextChangeManager());
			actionMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod validateMethod = getOldActionDescriptor().getValidateMethod();
		if (validateMethod != null) {
			validateMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					validateMethod, 
					getNewActionDescriptor().getValidateMethodName(), 
					getTextChangeManager());
			validateMethodProcessor.checkFinalConditions(pm, context);
		}

        IMethod disableMethod = getOldActionDescriptor().getDisableMethod();
        if (disableMethod != null) {
            disableMethodProcessor = 
                RefactoringSupport.createRenameMethodProcessor(
                    disableMethod, 
                    getNewActionDescriptor().getDisableMethodName(), 
                    getTextChangeManager());
            disableMethodProcessor.checkFinalConditions(pm, context);
        }

		IMethod hideMethod = getOldActionDescriptor().getHideMethod();
		if (hideMethod != null) {
			hideMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					hideMethod, 
					getNewActionDescriptor().getHideMethodName(), 
					getTextChangeManager());
			hideMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod defaultMethod = getOldActionDescriptor().getDefaultMethod();
		if (defaultMethod != null) {
			defaultMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					defaultMethod, 
					getNewActionDescriptor().getDefaultMethodName(), 
					getTextChangeManager());
			defaultMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod choicesMethod = getOldActionDescriptor().getChoicesMethod();
		if (choicesMethod != null) {
			choicesMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					choicesMethod, 
					getNewActionDescriptor().getChoicesMethodName(), 
					getTextChangeManager());
			choicesMethodProcessor.checkFinalConditions(pm, context);
		}
	}

	public void createChanges(TextChangeManager textChangeManager, IProgressMonitor pm) throws OperationCanceledException, CoreException {
		createChangeUsing(actionMethodProcessor, textChangeManager, pm);
		createChangeUsing(disableMethodProcessor, textChangeManager, pm);
		createChangeUsing(validateMethodProcessor, textChangeManager, pm);
		createChangeUsing(hideMethodProcessor, textChangeManager, pm);
		createChangeUsing(defaultMethodProcessor, textChangeManager, pm);
		createChangeUsing(choicesMethodProcessor, textChangeManager, pm);
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
