package com.halware.nakedide.eclipse.ext.refact.renameField;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;

import com.halware.nakedide.eclipse.ext.refact.PropertyDescriptor;
import com.halware.nakedide.eclipse.ext.refact.RefactoringSupport;

public class RenamePropertyRefactoring extends AbstractRenameRefactoring {

	private PropertyDescriptor oldPropertyDescriptor;
	public PropertyDescriptor getOldPropertyDescriptor() {
		return oldPropertyDescriptor;
	}

	private PropertyDescriptor newPropertyDescriptor;
	public PropertyDescriptor getNewPropertyDescriptor() {
		return newPropertyDescriptor;
	}

	@SuppressWarnings("restriction")
	public RenamePropertyRefactoring(
			IField field, String newFieldName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) {
		this(field, newFieldName, textChangeManager, false);
	}

	@SuppressWarnings("restriction")
	public RenamePropertyRefactoring(
			IField field, String newFieldName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			boolean refactorField) {
		this(textChangeManager, refactorField);
		this.oldPropertyDescriptor = new PropertyDescriptor(field);
		this.newPropertyDescriptor = new PropertyDescriptor(field, newFieldName);
	}


	@SuppressWarnings("restriction")
	public RenamePropertyRefactoring(
			IMethod method, String newMethodName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) {
		this(method, newMethodName, textChangeManager, false);
	}

	@SuppressWarnings("restriction")
	public RenamePropertyRefactoring(
			IMethod method, String newMethodName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			boolean refactorField) {
		this(textChangeManager, refactorField);
		this.oldPropertyDescriptor = new PropertyDescriptor(method);
		this.newPropertyDescriptor = new PropertyDescriptor(method, newMethodName);
	}

	@SuppressWarnings("restriction")
	private RenamePropertyRefactoring(
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			boolean refactorField) {
		super(textChangeManager);
		this.refactorField = refactorField;
	}

	private final boolean refactorField;
	private RefactoringProcessor fieldProcessor;
	private RefactoringProcessor modifyMethodProcessor; 
	private RefactoringProcessor clearMethodProcessor; 
	private RefactoringProcessor disableMethodProcessor; 
	private RefactoringProcessor validateMethodProcessor; 
	private RefactoringProcessor hideMethodProcessor; 
	private RefactoringProcessor defaultMethodProcessor; 
	private RefactoringProcessor choicesMethodProcessor; 

	@SuppressWarnings("restriction")
	public void checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException {
		if (refactorField) {
			IField field = getOldPropertyDescriptor().getField();
			if (field != null) {
				fieldProcessor = 
					RefactoringSupport.createRenameFieldProcessor(
						field, 
						getNewPropertyDescriptor().getFieldName(), 
						getTextChangeManager());
				fieldProcessor.checkFinalConditions(pm, context);
			}
		}

		IMethod modifyMethod = getOldPropertyDescriptor().getModifyMethod();
		if (modifyMethod != null) {
			modifyMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					modifyMethod, 
					getNewPropertyDescriptor().getModifyMethodName(), 
					getTextChangeManager());
			modifyMethodProcessor.checkFinalConditions(pm, context);
		}
	
		IMethod clearMethod = getOldPropertyDescriptor().getClearMethod();
		if (clearMethod != null) {
			clearMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					clearMethod, 
					getNewPropertyDescriptor().getClearMethodName(), 
					getTextChangeManager());
			clearMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod disableMethod = getOldPropertyDescriptor().getDisableMethod();
		if (disableMethod != null) {
			disableMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					disableMethod, 
					getNewPropertyDescriptor().getDisableMethodName(), 
					getTextChangeManager());
			disableMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod validateMethod = getOldPropertyDescriptor().getValidateMethod();
		if (validateMethod != null) {
			validateMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					validateMethod, 
					getNewPropertyDescriptor().getValidateMethodName(), 
					getTextChangeManager());
			validateMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod hideMethod = getOldPropertyDescriptor().getHideMethod();
		if (hideMethod != null) {
			hideMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					hideMethod, 
					getNewPropertyDescriptor().getHideMethodName(), 
					getTextChangeManager());
			hideMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod defaultMethod = getOldPropertyDescriptor().getDefaultMethod();
		if (defaultMethod != null) {
			defaultMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					defaultMethod, 
					getNewPropertyDescriptor().getDefaultMethodName(), 
					getTextChangeManager());
			defaultMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod choicesMethod = getOldPropertyDescriptor().getChoicesMethod();
		if (choicesMethod != null) {
			choicesMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					choicesMethod, 
					getNewPropertyDescriptor().getChoicesMethodName(), 
					getTextChangeManager());
			choicesMethodProcessor.checkFinalConditions(pm, context);
		}
	}

	@SuppressWarnings("restriction")
	public void createChanges(org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, IProgressMonitor pm) throws OperationCanceledException, CoreException {
		createChangeUsing(fieldProcessor, textChangeManager, pm);
		createChangeUsing(modifyMethodProcessor, textChangeManager, pm);
		createChangeUsing(clearMethodProcessor, textChangeManager, pm);
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
