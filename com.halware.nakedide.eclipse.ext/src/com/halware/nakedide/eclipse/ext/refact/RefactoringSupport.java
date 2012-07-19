package com.halware.nakedide.eclipse.ext.refact;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.jdt.internal.corext.util.JdtFlags;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;

import com.halware.eclipseutil.util.ReflectionUtils;

@SuppressWarnings("restriction")
public class RefactoringSupport {

    private static Logger LOGGER = Logger.getLogger(RefactoringSupport.class);
    public static Logger getLOGGER() {
        return LOGGER;
    }

	public static RefactoringProcessor createRenameFieldProcessor(
			IField field, String fieldName, 
			TextChangeManager textChangeManager) {
		return createRenameFieldProcessor(field, fieldName, textChangeManager, true);
	}
	
	public static RefactoringProcessor createRenameFieldProcessor(
			IField field, String fieldName, 
			TextChangeManager textChangeManager, 
			boolean updateAccessorAndMutator) {
		
		RenameFieldProcessor processor = null;
		try {
			if (JdtFlags.isEnum(field)) {
				return null;
			} else {
				processor = new RenameFieldProcessor(field);
			}
		} catch (JavaModelException e) {
            getLOGGER().error(e);
			return null;
		}
		
		processor.setNewElementName(fieldName);
		processor.setRenameGetter(updateAccessorAndMutator);
		processor.setRenameSetter(updateAccessorAndMutator);
		
		try {
			ReflectionUtils.setField(
				processor, "fChangeManager", 
				textChangeManager);
			
			ReflectionUtils.setField(
				processor, "fCategorySet", 
				ReflectionUtils.getField(
					RenameTypeProcessor.class, 
					"CATEGORY_FIELD_RENAME"));

			ReflectionUtils.setField(
				processor, "fIsComposite", Boolean.TRUE);
			ReflectionUtils.setField(
				processor, "fDelegateUpdating", Boolean.FALSE);
			ReflectionUtils.setField(
				processor, "fDelegateDeprecation", Boolean.TRUE);

		} catch (SecurityException e) {
            getLOGGER().error(e);
			return null;
		} catch (NoSuchFieldException e) {
            getLOGGER().error(e);
			return null;
		} catch (IllegalArgumentException e) {
            getLOGGER().error(e);
			return null;
		} catch (IllegalAccessException e) {
            getLOGGER().error(e);
			return null;
		}
		
		return processor;
	}
	
	
	/**
	 * Creates a new rename support for the given {@link IMethod}.
	 * 
     * Adapted from {@link org.eclipse.jdt.ui.refactoring.RenameSupport#create(IMethod, String, int)}.
     * 
	 * @param method the {@link IMethod} to be renamed.
	 * @param newName the method's new name. <code>null</code> is a valid value
	 * indicating that no new name is provided.
	 * @return the {@link RenameSupport}.
	 * @throws CoreException if an unexpected error occurred.
	 */
	public static RefactoringProcessor createRenameMethodProcessor(
				IMethod method, String newName, 
				org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager textChangeManager) throws CoreException {
		org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor processor;
		if (org.eclipse.jdt.internal.corext.refactoring.rename.MethodChecks.isVirtual(method)) {
			processor= new org.eclipse.jdt.internal.corext.refactoring.rename.RenameVirtualMethodProcessor(method);
		} else {
			processor= new org.eclipse.jdt.internal.corext.refactoring.rename.RenameNonVirtualMethodProcessor(method);
		}
		
		processor.setNewElementName(newName);
		try {
			ReflectionUtils.setField(
				RenameMethodProcessor.class, 
				processor, "fChangeManager", 
				textChangeManager);
			
			ReflectionUtils.setField(
				RenameMethodProcessor.class, 
				processor, "fCategorySet", 
				ReflectionUtils.getField(
					org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor.class, 
					"CATEGORY_FIELD_RENAME"));

			ReflectionUtils.setField(
				RenameMethodProcessor.class, 
				processor, "fIsComposite", Boolean.TRUE);
			ReflectionUtils.setField(
				RenameMethodProcessor.class,
				processor, "fDelegateUpdating", Boolean.FALSE);
			ReflectionUtils.setField(
				RenameMethodProcessor.class,
				processor, "fDelegateDeprecation", Boolean.TRUE);

		} catch (SecurityException e) {
            getLOGGER().error(e);
			return null;
		} catch (NoSuchFieldException e) {
            getLOGGER().error(e);
			return null;
		} catch (IllegalArgumentException e) {
            getLOGGER().error(e);
			return null;
		} catch (IllegalAccessException e) {
            getLOGGER().error(e);
			return null;
		}
		return processor;
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
