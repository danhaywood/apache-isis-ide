package com.halware.nakedide.eclipse.core.launches;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * Shamelessly copied from {@link WorkingDirectoryBlock}.
 * 
 * @author dkhaywood
 */
@SuppressWarnings("restriction")
public class ResourceBaseWorkingBlock extends JavaLaunchTab {

	public ResourceBaseWorkingBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		this.nakedObjectsArgumentsTab = nakedObjectsArgumentsTab;
	}

	private final NakedObjectsArgumentsTab nakedObjectsArgumentsTab;
	private NakedObjectsArgumentsTab getTab() {
		return nakedObjectsArgumentsTab;
	}

	private boolean isIncludeResourceWorkingBlock() {
		return getTab().isIncludeResourceWorkingBlock();
	}
    
    private Group group;
    private Button fWorkspaceButton;
    private Button fFileSystemButton;
    private Button fVariablesButton;

    private Text fResourceBaseText = null;

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
     */
    public String getName() {
        return "Resource &Base"; 
    }
    

    private ILaunchConfiguration fLaunchConfiguration;
    /**
     * The last launch config this tab was initialized from
     */
    protected ILaunchConfiguration getLaunchConfiguration() {
        return fLaunchConfiguration;
    }
    /**
     * Sets the java project currently specified by the
     * given launch config, if any.
     */
    protected void setLaunchConfiguration(ILaunchConfiguration config) {
        fLaunchConfiguration = config;
    }   

    
    public Text getText() {
        return fResourceBaseText;
    }
    /**
     * gets the path from the text box that is selected
     * @return the working directory the user wishes to use
     * @since 3.2
     */
    protected String getResourceBaseText() {
        return fResourceBaseText.getText().trim();
    }

    

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        if (!isIncludeResourceWorkingBlock()) {
        	return;
        }

        Font font = parent.getFont();
                
        group = new Group(parent, SWT.NONE);
        //PlatformUI.getWorkbench().getHelpSystem().setHelp(group, IJavaDebugHelpContextIds.WORKING_DIRECTORY_BLOCK);     
        GridLayout workingDirLayout = new GridLayout();
        workingDirLayout.numColumns = 2;
        workingDirLayout.makeColumnsEqualWidth = false;
        group.setLayout(workingDirLayout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        group.setFont(font);
        setControl(group);
        
        group.setText("Resource base"); 
        fResourceBaseText = new Text(group, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fResourceBaseText.setLayoutData(gd);
        fResourceBaseText.setFont(font);
        fResourceBaseText.addModifyListener(fListener);
        
        Composite buttonComp = new Composite(group, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        buttonComp.setLayout(layout);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gd.horizontalSpan = 2;
        buttonComp.setLayoutData(gd);
        buttonComp.setFont(font);       
        fWorkspaceButton = createPushButton(buttonComp, "W&orkspace...", null); // LauncherMessages.WorkingDirectoryBlock_0 
        fWorkspaceButton.addSelectionListener(fListener);
        
        fFileSystemButton = createPushButton(buttonComp, "File &System...", null); // LauncherMessages.WorkingDirectoryBlock_1 
        fFileSystemButton.addSelectionListener(fListener);
        
        fVariablesButton = createPushButton(buttonComp, "Variabl&es...", null); // LauncherMessages.WorkingDirectoryBlock_17 
        fVariablesButton.addSelectionListener(fListener);
    }

    /**
     * A listener to update for text changes and widget selection
     */
    private class WidgetListener extends SelectionAdapter implements ModifyListener {
        public void modifyText(ModifyEvent e) {
            setDirty(true);
            updateLaunchConfigurationDialog();
        }
        
        public void widgetSelected(SelectionEvent e) {
            Object source= e.getSource();
            if (source == fWorkspaceButton) {
                handleWorkspaceDirBrowseButtonSelected();
            }
            else if (source == fFileSystemButton) {
                handleWorkingDirBrowseButtonSelected();
            } 
            else if (source == fVariablesButton) {
                handleWorkingDirVariablesButtonSelected();
            } 
        }
    }
    private WidgetListener fListener = new WidgetListener();
    

    /**
     * Show a dialog that lets the user select a working directory
     */
    private void handleWorkingDirBrowseButtonSelected() {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setMessage("Select the resource base directory:"); // LauncherMessages.WorkingDirectoryBlock_7 
        String currentWorkingDir = getResourceBaseText();
        if (!currentWorkingDir.trim().equals("")) { //$NON-NLS-1$
            File path = new File(currentWorkingDir);
            if (path.exists()) {
                dialog.setFilterPath(currentWorkingDir);
            }//end if       
        }//end if
        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            fResourceBaseText.setText(selectedDirectory);
        }//end if       
    }//end handleQWrokingDirBrowseBUttonSelected

    /**
     * Show a dialog that lets the user select a working directory from 
     * the workspace
     */
    private void handleWorkspaceDirBrowseButtonSelected() {
        IContainer currentContainer= getContainer();
        if (currentContainer == null) {
            currentContainer = ResourcesPlugin.getWorkspace().getRoot();
        } 
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), currentContainer, false, "Select a &workspace relative resource base directory:"); // LauncherMessages.WorkingDirectoryBlock_4 
        dialog.showClosedProjects(false);
        dialog.open();
        Object[] results = dialog.getResult();      
        if ((results != null) && (results.length > 0) && (results[0] instanceof IPath)) {
            IPath path = (IPath)results[0];
            String containerName = path.makeRelative().toString();
            setOtherWorkingDirectoryText("${workspace_loc:" + containerName + "}"); //$NON-NLS-1$ //$NON-NLS-2$
        }           
    }
    
    /**
     * Returns the selected workspace container,or <code>null</code>
     */
    protected IContainer getContainer() {
        String path = getResourceBaseText();
        if (path.length() > 0) {
            IResource res = null;
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            
            if (path.startsWith("${workspace_loc:")) { //$NON-NLS-1$
                IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
                try {
                    path = manager.performStringSubstitution(path, false);
                    IContainer[] containers = root.findContainersForLocation(new Path(path));
                    if (containers.length > 0) {
                        res = containers[0];
                    }
                } catch (CoreException e) {
                    // ignore
                }
            } else {      
                res = root.findMember(path);
            }
            if (res instanceof IContainer) {
                return (IContainer)res;
            }
        }
        return null;
    }
        
    /**
     * The working dir variables button has been selected
     */
    private void handleWorkingDirVariablesButtonSelected() {
        StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
        dialog.open();
        String variableText = dialog.getVariableExpression();
        if (variableText != null) {
            fResourceBaseText.insert(variableText);
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
     */
    public boolean isValid(ILaunchConfiguration config) {
        setErrorMessage(null);
        setMessage(null);
        
        // if variables are present, we cannot resolve the directory
        String workingDirPath = getResourceBaseText();
        if (workingDirPath.indexOf("${") >= 0) { //$NON-NLS-1$
            IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
            try {
                manager.validateStringVariables(workingDirPath);
            } catch (CoreException e) {
                setErrorMessage(e.getMessage());
                return false;
            }
        } else if (workingDirPath.length() > 0) {
            IContainer container = getContainer();
            if (container == null) {
                File dir = new File(workingDirPath);
                if (dir.isDirectory()) {
                    return true;
                }
                setErrorMessage("Resource base directory does not exist"); // LauncherMessages.WorkingDirectoryBlock_10 
                return false;
            }
        } else if (workingDirPath.length() == 0) {
            setErrorMessage("Resource base directory not specified"); // LauncherMessages.WorkingDirectoryBlock_20
        }
        return true;
    }

    /**
     * Defaults are empty.
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
     */
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        if (!isIncludeResourceWorkingBlock()) {
        	return;
        }
        LaunchUtils.setLaunchConfigurationResourceBaseDefault(configuration, guessApplicationProjectName(configuration));
    }

    /**
     * Attempts to guess the application project name from the client project name 
     * (if any - as found in the supplied configuration) by stripping off 
     * <tt>.client</tt> suffix if present.
     * 
     * <p>
     * Otherwise just returns the project name.
     * 
     * @param configuration
     * @return
     * @throws CoreException
     */
    private String guessApplicationProjectName(
            ILaunchConfigurationWorkingCopy configuration) {
        String projectName = null;
        try {
            projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String)null);
        } catch (CoreException e) {
            // ignore, handled below
        }
        if (projectName == null) {
            return null;
        }
        Pattern clientProjectNamePattern = Pattern.compile("(.+)\\.client");
        Matcher matcher = clientProjectNamePattern.matcher(projectName);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(1);
    }


    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
     */
    public void initializeFrom(ILaunchConfiguration configuration) {
        if (!isIncludeResourceWorkingBlock()) {
        	return;
        }

        setLaunchConfiguration(configuration);
        try {           
            String id = configuration.getAttribute(LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_RESOURCE_BASE_DIR, (String)null);
            if (id != null) {
                setOtherWorkingDirectoryText(id);
            }
        } catch (CoreException e) {
            setErrorMessage("Exception occurred reading configuration:" + e.getStatus().getMessage()); // LauncherMessages.JavaArgumentsTab_Exception_occurred_reading_configuration___15 
            JDIDebugUIPlugin.log(e);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
     */
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
        		LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_RESOURCE_BASE_DIR, getResourceBaseText());
    }
    
	public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
    	if (!isIncludeResourceWorkingBlock()) {
    		return;
        }

    	LaunchUtils.performApply(
            argumentsBuffer, "-D nakedobjects.viewer.web.resourceBase=", getText(), 
            configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_RESOURCE_BASE_DIR);
	}
    

    
    /**
     * sets the other dir text
     * @param dir the new text
     * @since 3.2
     */
    protected void setOtherWorkingDirectoryText(String dir) {
        if(dir != null) {
            fResourceBaseText.setText(dir);
        }
    }
    
    @Override
    public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
        if (!isIncludeResourceWorkingBlock()) {
        	return;
        }
    	super.setLaunchConfigurationDialog(dialog);
    }
    
    @Override
    public String getMessage() {
        if (!isIncludeResourceWorkingBlock()) {
        	return null;
        }
    	return super.getMessage();
    }
    
    @Override
    public String getErrorMessage() {
        if (!isIncludeResourceWorkingBlock()) {
        	return null;
        }
    	return super.getErrorMessage();
    }
    
    public void enable() {
        setEnabled(true);
    }
    public void disable() {
        setEnabled(false);
    }

    /**
     * Allows this entire block to be enabled/disabled
     * @param enabled whether to enable it or not
     */
    protected void setEnabled(boolean enabled) {
        if (!isIncludeResourceWorkingBlock()) {
        	return;
        }

        fResourceBaseText.setEnabled(enabled);
        fWorkspaceButton.setEnabled(enabled);
        fVariablesButton.setEnabled(enabled);
        fFileSystemButton.setEnabled(enabled);
    }

        

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#dispose()
     */
    public void dispose() {}


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
