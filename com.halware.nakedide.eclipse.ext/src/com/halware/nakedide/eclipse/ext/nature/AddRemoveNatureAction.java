package com.halware.nakedide.eclipse.ext.nature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.halware.nakedide.eclipse.core.Activator;
import com.halware.nakedide.eclipse.core.NakedObjectsCore;


/**
 * This action toggles the selected project's nature.
 */
public class AddRemoveNatureAction implements IObjectActionDelegate {

	private List selected = new ArrayList();

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		selected.clear();
		if (selection instanceof IStructuredSelection) {
			boolean enabled = true;
			Iterator iter = ((IStructuredSelection)selection).iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof IJavaProject) {
					obj = ((IJavaProject) obj).getProject();
				}
				if (obj instanceof IProject) {
					IProject project = (IProject) obj;
					if (!project.isOpen()) {
						enabled = false;
						break;
					} else {
						selected.add(project);
					}
				} else {
					enabled = false;
					break;
				}
			}
			action.setEnabled(enabled);
		}
	}

	public void run(IAction action) {
		Iterator iter = selected.iterator();
		while (iter.hasNext()) {
			IProject project = (IProject) iter.next();
			if (NatureUtil.isNakedObjectsProject(project)) {
				try {
					NatureUtil.removeProjectNature(project,
                            NakedObjectsCore.NATURE_ID, new NullProgressMonitor());
				} catch (CoreException e) {
					MessageDialog.openError(
							Activator.getActiveWorkbenchShell(),
                            "Remove Naked Objects Nature",
							"Error when removing Naked Objects nature"
                            );
				}
			} else {
				try {
					NatureUtil.addProjectNature(project,
                            NakedObjectsCore.NATURE_ID, new NullProgressMonitor());
				} catch (CoreException e) {
					MessageDialog.openError(
							Activator.getActiveWorkbenchShell(),
							"Add Naked Objects Nature",
							"Error when adding Naked Object nature"
                            );
				}
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
