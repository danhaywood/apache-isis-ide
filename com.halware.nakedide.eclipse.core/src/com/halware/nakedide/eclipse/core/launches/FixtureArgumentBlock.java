package com.halware.nakedide.eclipse.core.launches;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.search.JavaWorkspaceScope;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

public class FixtureArgumentBlock extends AbstractArgumentBlock {

	public FixtureArgumentBlock(
			NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		super(nakedObjectsArgumentsTab);
	}

    private Text fixtureText;
    private Button searchButton;
    

	public void createControl(Composite parent) {
        Font font = parent.getFont();
		
		Group group= new Group(parent, SWT.NONE);
		group.setText("&Fixture");
		GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd1);
		
		GridLayout layout1 = new GridLayout();
		layout1.numColumns = 2;
		group.setLayout(layout1);
		group.setFont(font);
		
		fixtureText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gd1 = new GridData(GridData.FILL_HORIZONTAL);
		fixtureText.setLayoutData(gd1);
		fixtureText.setFont(font);
		fixtureText.addModifyListener(new MakeDirtyModifyListener(getTab()));
		
		searchButton = getTab().createPushButton(group, "&Search...", null); // LauncherMessages.AbstractJavaMainTab_2 
		searchButton.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e) {
		        handleSearchButtonSelected();
		    }
		});
		
	}

    /**
     * Show a dialog that lists all main types
     */
    protected void handleSearchButtonSelected() {
        try {
            IJavaSearchScope searchScope = createSearchScope("org.nakedobjects.nof.core.persist.Fixture");
            SelectionDialog dialog = null;
                dialog = JavaUI.createTypeDialog(
                            getTab().getShell(),
                            getTab().getLaunchConfigurationDialog(),
                            searchScope,
                            IJavaElementSearchConstants.CONSIDER_CLASSES, 
                            false,
                            "*"); //$NON-NLS-1$
                dialog.setTitle("Select Fixture Type"); // LauncherMessages.JavaMainTab_Choose_Main_Type_11 
                dialog.setMessage("Select &type (? = any character, * = any String, TZ = TimeZone):"); // LauncherMessages.JavaMainTab_Choose_a_main__type_to_launch__12 
                if (dialog.open() == Window.CANCEL) {
                    return;
                }
                Object[] results = dialog.getResult();  
                IType type = (IType)results[0];
                if (type != null) {
                    fixtureText.setText(type.getFullyQualifiedName());
                }
        } catch (JavaModelException e) {
            getTab().setErrorMessage(e.getMessage());
            return;
        }
    }   

    private IJavaSearchScope createSearchScope(final String typeName) throws JavaModelException {
        IType type = findType(typeName);
        if (type == null) {
            return new JavaWorkspaceScope();
        }
        return SearchEngine.createHierarchyScope(type);
    }
    
    private IType findType(final String typeName) throws JavaModelException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IJavaModel javaModel = JavaCore.create(workspace.getRoot());
        IJavaProject projects[];
        projects = javaModel.getJavaProjects();
        for (int n = 0; n < projects.length; n++) {
            IJavaProject project = projects[n];
            IType type = project.findType(typeName);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

	public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
        String fixture = configuration.getAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_FIXTURE, "");
        fixtureText.setText(fixture);
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_FIXTURE, "");
	}
    
	public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
		LaunchUtils.performApply(
                argumentsBuffer, "-D nakedobjects.fixture=", fixtureText, 
                configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_FIXTURE);
	}



}
