package com.halware.nakedide.eclipse.ext.refact.renameMethod;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.halware.eclipseutil.util.ReflectionUtils;
import com.halware.nakedide.eclipse.core.Constants;
import com.halware.nakedide.eclipse.core.util.MethodUtils;
import com.halware.nakedide.eclipse.ext.nature.NatureUtil;
import com.halware.nakedide.eclipse.ext.refact.renameField.RenameActionRefactoring;
import com.halware.nakedide.eclipse.ext.refact.renameField.RenameCollectionRefactoring;
import com.halware.nakedide.eclipse.ext.refact.renameField.RenamePropertyRefactoring;

@SuppressWarnings("restriction")
public class RenameMethodRefactoringParticipant extends RenameParticipant {

    private static Logger LOGGER = Logger.getLogger(RenameMethodRefactoringParticipant.class);
    public static Logger getLOGGER() {
        return LOGGER;
    }
    
	private IMethod method;

	public IMethod getMethod() {
		return method;
	}

	private org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager;

	/**
	 * As provided to us when initialized; the original method processor.
	 * 
	 * @return
	 */
	private org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor renameMethodProcessor;

	public boolean initialize(RefactoringProcessor processor, Object element,
			RefactoringArguments arguments) {
		if (processor instanceof org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor) {
			try {
				renameMethodProcessor = (org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor) processor;
				textChangeManager = (org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager) ReflectionUtils
						.getField(
								org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor.class,
								renameMethodProcessor, "fChangeManager");
			} catch (SecurityException e) {
                getLOGGER().equals(e);
			} catch (NoSuchFieldException e) {
                getLOGGER().equals(e);
			} catch (IllegalArgumentException e) {
                getLOGGER().equals(e);
			} catch (IllegalAccessException e) {
                getLOGGER().equals(e);
			}
		}
		return super.initialize(processor, element, arguments);
	}

	public boolean isPropertyRefactoring() {
		if (getMethod() == null) {
			return false;
		}
		return MethodUtils.representsProperty(getMethod());
	}

	public boolean isCollectionRefactoring() {
		if (getMethod() == null) {
			return false;
		}
		return MethodUtils.representsCollection(getMethod());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isActionRefactoring() {
		if (getMethod() == null) {
			return false;
		}
		return MethodUtils.representsAction(getMethod());
	}

	// initialized if this is a property refactoring.
	private RenamePropertyRefactoring propertyRefactoring;

	// initialized if this is a property refactoring.
	private RenameCollectionRefactoring collectionRefactoring;

	 // initialized if this is an action refactoring.
	 private RenameActionRefactoring actionRefactoring;

	protected boolean initialize(Object element) {

		// being called as a side-effect of a renameField; ignore.
		if (renameMethodProcessor == null) {
			return true;
		}

		method = (IMethod) element;

		// one of the following may be is true...
		if (isCollectionRefactoring()) {
			collectionRefactoring = new RenameCollectionRefactoring(
					getMethod(), 
					renameMethodProcessor.getNewElementName(),
					textChangeManager, true);
		} else if (isPropertyRefactoring()) {
			propertyRefactoring = new RenamePropertyRefactoring(
					getMethod(),
					renameMethodProcessor.getNewElementName(),
					textChangeManager, true);
		} else if (isActionRefactoring()) {
			 actionRefactoring = new RenameActionRefactoring(
					 getMethod(), 
					 renameMethodProcessor.getNewElementName(),
					 textChangeManager);
		}

		return true;
	}

	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {

		if (method == null) {
			return null;
		}
		String prefix = MethodUtils.getPrefix(method);
        if (MethodUtils.in(prefix, Constants.ACTION_NO_ARG_SUPPORTING_PREFIXES) && method.getNumberOfParameters() == 0) {
            // bit of a cop-out
            return RefactoringStatus.createFatalErrorStatus(
                "Rename refactoring not supported for no-arg supporting action methods; rename action instead");
            
        }
		String newPrefix = MethodUtils.getPrefix(renameMethodProcessor.getNewElementName());
		if (prefix    != null && !prefix.equals(newPrefix) ||
			newPrefix != null && !newPrefix.equals(prefix)   ) {
			return RefactoringStatus.createWarningStatus("Prefix does not match any property");
		}
		try {
			if (isCollectionRefactoring()) {
				collectionRefactoring.checkConditions(pm, context);
			} else if (isPropertyRefactoring()) {
				propertyRefactoring.checkConditions(pm, context);
			} else if (isActionRefactoring()) {
				actionRefactoring.checkConditions(pm, context);
			}

			return null; // treated as OK
		} catch (CoreException e) {
            return RefactoringStatus.createFatalErrorStatus(
                    "Failed to locate associator or dissociator");
		}
	}

	public String getName() {
		return "Rename NakedObject methods";
	}

	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {

		// being called as a side-effect of a renameField; ignore.
		if (renameMethodProcessor == null) {
			return null;
		}

		if (!NatureUtil.isNakedObjectsProject(
                method.getJavaProject().getProject())) {
			return null;
		}

		if (isCollectionRefactoring()) {
			collectionRefactoring.createChanges(textChangeManager, pm);
		} else if (isPropertyRefactoring()) {
			propertyRefactoring.createChanges(textChangeManager, pm);
		} else if (isActionRefactoring()) {
			actionRefactoring.createChanges(textChangeManager, pm);
		}

		return null;
	}

	protected void clearParentOfTextChangeManagerChanges() {
		TextChange[] allChanges = textChangeManager.getAllChanges();
		for (int i = 0; i < allChanges.length; i++) {
			try {
				ReflectionUtils.invokeMethod(Change.class, allChanges[i],
						"setParent", new Class[] { Change.class },
						new Object[] { null });
			} catch (SecurityException e) {
                getLOGGER().error(e);
			} catch (NoSuchMethodException e) {
                getLOGGER().error(e);
			} catch (IllegalArgumentException e) {
                getLOGGER().error(e);
			} catch (IllegalAccessException e) {
                getLOGGER().error(e);
			} catch (InvocationTargetException e) {
                getLOGGER().error(e);
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
