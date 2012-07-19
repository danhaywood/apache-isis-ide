package com.halware.nakedide.eclipse.ext.refact.renameField;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;

import com.halware.nakedide.eclipse.ext.refact.CollectionDescriptor;
import com.halware.nakedide.eclipse.ext.refact.RefactoringSupport;

public class RenameCollectionRefactoring extends AbstractRenameRefactoring {

	private CollectionDescriptor oldCollectionDescriptor;
	public CollectionDescriptor getOldCollectionDescriptor() {
		return oldCollectionDescriptor;
	}

	private CollectionDescriptor newCollectionDescriptor;
	public CollectionDescriptor getNewCollectionDescriptor() {
		return newCollectionDescriptor;
	}

	@SuppressWarnings("restriction")
	public RenameCollectionRefactoring(
			IField field, String newFieldName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) {
		this(field, newFieldName, textChangeManager, false);
	}

	@SuppressWarnings("restriction")
	public RenameCollectionRefactoring(
			IField field, String newFieldName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			boolean refactorField) {
		this(textChangeManager, refactorField);
		this.oldCollectionDescriptor = new CollectionDescriptor(field);
		this.newCollectionDescriptor = new CollectionDescriptor(field, newFieldName);
	}

	@SuppressWarnings("restriction")
	public RenameCollectionRefactoring(
			IMethod method, String newMethodName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) {
		this(method, newMethodName, textChangeManager, false);
	}

	@SuppressWarnings("restriction")
	public RenameCollectionRefactoring(
			IMethod method, String newMethodName, 
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			boolean refactorField) {
		this(textChangeManager, refactorField);
		
		this.oldCollectionDescriptor = new CollectionDescriptor(method);
		this.newCollectionDescriptor = new CollectionDescriptor(method, newMethodName);
	}

	@SuppressWarnings("restriction")
	private RenameCollectionRefactoring(
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			boolean refactorField) {
		super(textChangeManager);
		this.refactorField = refactorField;
	}

	private final boolean refactorField;
	private RefactoringProcessor fieldProcessor;
	private RefactoringProcessor addToMethodProcessor; 
	private RefactoringProcessor removeFromMethodProcessor; 
	private RefactoringProcessor disableMethodProcessor; 
	private RefactoringProcessor validateAddToMethodProcessor; 
	private RefactoringProcessor validateRemoveFromMethodProcessor;
	private RefactoringProcessor hideMethodProcessor; 

	@SuppressWarnings("restriction")
	public void checkConditions(
			IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException, CoreException {
		
		if (refactorField) {
			IField field = getOldCollectionDescriptor().getField();
			if (field != null) {
				fieldProcessor = 
					RefactoringSupport.createRenameFieldProcessor(
						field, 
						getNewCollectionDescriptor().getFieldName(), 
						getTextChangeManager());
				fieldProcessor.checkFinalConditions(pm, context);
			}
		}
		
		IMethod addToMethod = getOldCollectionDescriptor().getAddToMethod();
		if (addToMethod != null) {
			addToMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					addToMethod, 
					getNewCollectionDescriptor().getAddToMethodName(), 
					getTextChangeManager());
			addToMethodProcessor.checkFinalConditions(pm, context);
		}
		
		IMethod removeFromMethod = getOldCollectionDescriptor().getRemoveFromMethod();
		if (removeFromMethod != null) {
			removeFromMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					removeFromMethod, 
					getNewCollectionDescriptor().getRemoveFromMethodName(), 
					getTextChangeManager());
			removeFromMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod disableMethod = getOldCollectionDescriptor().getDisableMethod();
		if (disableMethod != null) {
			disableMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					disableMethod, 
					getNewCollectionDescriptor().getDisableMethodName(), 
					getTextChangeManager());
			disableMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod validateAddToMethod = getOldCollectionDescriptor().getValidateAddToMethod();
		if (validateAddToMethod != null) {
			validateAddToMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					validateAddToMethod, 
					getNewCollectionDescriptor().getValidateAddToMethodName(), 
					getTextChangeManager());
			validateAddToMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod validateRemoveFromMethod = getOldCollectionDescriptor().getValidateRemoveFromMethod();
		if (validateRemoveFromMethod != null) {
			validateRemoveFromMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
					validateRemoveFromMethod, 
					getNewCollectionDescriptor().getValidateRemoveFromMethodName(), 
					getTextChangeManager());
			validateRemoveFromMethodProcessor.checkFinalConditions(pm, context);
		}

		IMethod hideMethod = getOldCollectionDescriptor().getHideMethod();
		if (hideMethod != null) {
			hideMethodProcessor = 
				RefactoringSupport.createRenameMethodProcessor(
						hideMethod, 
					getNewCollectionDescriptor().getHideMethodName(), 
					getTextChangeManager());
			hideMethodProcessor.checkFinalConditions(pm, context);
		}
	}

	@SuppressWarnings("restriction")
	public void createChanges(
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, 
			IProgressMonitor pm) throws OperationCanceledException, CoreException {
		createChangeUsing(fieldProcessor, textChangeManager, pm);
		createChangeUsing(addToMethodProcessor, textChangeManager, pm);
		createChangeUsing(removeFromMethodProcessor, textChangeManager, pm);
		createChangeUsing(disableMethodProcessor, textChangeManager, pm);
		createChangeUsing(validateAddToMethodProcessor, textChangeManager, pm);
		createChangeUsing(validateRemoveFromMethodProcessor, textChangeManager, pm);
		createChangeUsing(hideMethodProcessor, textChangeManager, pm);
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
