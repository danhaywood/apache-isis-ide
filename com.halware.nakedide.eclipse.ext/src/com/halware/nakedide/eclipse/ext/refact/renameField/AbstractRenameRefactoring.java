package com.halware.nakedide.eclipse.ext.refact.renameField;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;

import com.halware.eclipseutil.util.ReflectionUtils;

public abstract class AbstractRenameRefactoring {

	@SuppressWarnings("restriction")
	protected AbstractRenameRefactoring(
			org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) {
		this.textChangeManager = textChangeManager;
	}
	
	@SuppressWarnings("restriction")
	private final org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager;
	@SuppressWarnings("restriction")
	protected org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager getTextChangeManager() {
		return textChangeManager;
	}
	
	
	@SuppressWarnings("restriction")
	protected Change createChangeUsing(RefactoringProcessor refactoringProcessor, org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager, IProgressMonitor pm) throws OperationCanceledException, CoreException {
		if (refactoringProcessor == null) {
			return null;
		}
		clearParentOfTextChangeManagerChanges(textChangeManager);
		return refactoringProcessor.createChange(pm);
	}
	
	@SuppressWarnings("restriction")
	private void clearParentOfTextChangeManagerChanges(org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) {
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
