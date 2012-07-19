package com.halware.nakedide.eclipse.ext.refact.renameField;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IField;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.halware.eclipseutil.util.ReflectionUtils;
import com.halware.nakedide.eclipse.core.util.MethodUtils;
import com.halware.nakedide.eclipse.ext.nature.NatureUtil;

public class RenameFieldRefactoringParticipant extends RenameParticipant {

	public RenameFieldRefactoringParticipant() {
	}

	private IField field;
	public IField getField() {
		return field;
	}

	@SuppressWarnings("restriction")
	private org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager;
	/**
	 * As provided to us when initialized; the original field processor.
	 * @return
	 */
	@SuppressWarnings("restriction")
	private org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor renameFieldProcessor;

	
	@SuppressWarnings("restriction")
	public boolean initialize(RefactoringProcessor processor, Object element, RefactoringArguments arguments) {
		try {
			renameFieldProcessor = (org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor)processor;
			textChangeManager = 
				(org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager)
				ReflectionUtils.getField(renameFieldProcessor, "fChangeManager");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.initialize(processor, element, arguments);
	}


	public boolean isPropertyRefactoring() {
		if (getField() == null) {
			throw new IllegalStateException("Not yet initialized.");
		}
		return MethodUtils.representsProperty(getField());
	}
	/**
	 * 
	 * @return
	 */
	public boolean isCollectionRefactoring() {
		if (getField() == null) {
			throw new IllegalStateException("Not yet initialized.");
		}
		return MethodUtils.representsCollection(getField());
	}

	// initialized if this is a property refactoring.
	private RenamePropertyRefactoring propertyRefactoring;
	

	// initialized if this is a property refactoring.
	private RenameCollectionRefactoring collectionRefactoring;

	@SuppressWarnings("restriction")
	protected boolean initialize(Object element) {
		field = (IField)element;
	
		// precisely one of the following is true...
		if (isCollectionRefactoring()) {
			collectionRefactoring =
				new RenameCollectionRefactoring(
					getField(), renameFieldProcessor.getNewElementName(), textChangeManager);
			
		} else if (isPropertyRefactoring()) {
			propertyRefactoring =
				new RenamePropertyRefactoring(
					getField(), renameFieldProcessor.getNewElementName(), textChangeManager);
		}
		return true;
	}

	@SuppressWarnings("restriction")
	public RefactoringStatus checkConditions(
			IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {

		try {
			if (isCollectionRefactoring()) {
				collectionRefactoring.checkConditions(pm, context);
			} else if (isPropertyRefactoring()) {
				propertyRefactoring.checkConditions(pm, context);
			}

			return null; // treated as OK
		} catch (CoreException e) {
			return RefactoringStatus.createFatalErrorStatus("Failed to locate modify or clear");
		}
	}

	public String getName() {
		if (getField() == null) {
			return "Rename NakedObject methods";
		}
		if (isCollectionRefactoring()) {
			return "Rename NakedObject Collection methods";
		}
		if (isCollectionRefactoring()) {
			return "Rename NakedObject Property methods";
		}
		return "Rename NakedObject methods";
	}

	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {

		if (!NatureUtil.isNakedObjectsProject(field.getJavaProject().getProject())) {
			return null;
		}
		
		if (isCollectionRefactoring()) {
			collectionRefactoring.createChanges(textChangeManager, pm);
		} else if (isPropertyRefactoring()) {
			propertyRefactoring.createChanges(textChangeManager, pm);
		}

		return null;
	}


	@SuppressWarnings("restriction")
	protected void clearParentOfTextChangeManagerChanges() {
		TextChange[] allChanges = textChangeManager.getAllChanges();
		for(int i=0; i<allChanges.length; i++) {
			try {
				ReflectionUtils.invokeMethod(
					Change.class, allChanges[i], 
					"setParent", new Class[]{Change.class}, new Object[]{null});
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public TextChange getTextChange(Object element) {
		return getProcessor().getRefactoring().getTextChange(element);
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
