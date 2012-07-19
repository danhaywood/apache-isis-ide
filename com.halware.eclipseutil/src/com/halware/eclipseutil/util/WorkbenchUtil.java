package com.halware.eclipseutil.util;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Static library functions
 */
public final class WorkbenchUtil {
	
	private WorkbenchUtil() {} 

	public static IWorkbench getWorkbench(){
		return PlatformUI.getWorkbench(); 
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow(){
		IWorkbench workbench = getWorkbench();
		return workbench!=null?workbench.getActiveWorkbenchWindow():null; 
	}

	public static IWorkbenchPage getActivePage(){
		IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
		return activeWorkbenchWindow != null?activeWorkbenchWindow.getActivePage():null; 
	}

	public static final Shell getActiveShell() {
		IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
		return activeWorkbenchWindow!=null?activeWorkbenchWindow.getShell():null;
	}

	public static IEditorPart getActiveEditor(){
		IWorkbenchPage activePage = getActivePage();
		return activePage != null?activePage.getActiveEditor():null;
	}
	

	/**
	 * Returns the view in the current page with the given id or
	 * <code>null</code> if it does not exist.
	 * 
	 * @param id
	 * @return
	 */
	public static final IViewPart getView(final String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		IWorkbenchPage activePage = getActivePage();
		if (activePage == null) {
			return null;
		}
		IViewReference[] refs = activePage.getViewReferences();
		for (int i = 0, num = refs.length; i < num; i++) {
			if (refs[i].getId().equals(id)) {
				return refs[i].getView(false);
			}
		}
		return null;
	}

}
