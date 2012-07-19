package com.halware.nakedide.eclipse.core.launches;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class NakedObjectsArgumentsTab extends JavaArgumentsTab {

    private final static Logger LOGGER = Logger.getLogger(NakedObjectsArgumentsTab.class);
    public static Logger getLOGGER() {
        return LOGGER;
    }

    private final static boolean INCLUDE_RESOURCE_WORKING_BLOCK = false;
    boolean isIncludeResourceWorkingBlock() {
    	return INCLUDE_RESOURCE_WORKING_BLOCK;
    }
    

    public NakedObjectsArgumentsTab() {
    	typeArgumentBlock = new TypeArgumentBlock(this);
    	viewerArgumentBlock = new ViewerArgumentBlock(this);
    	authenticationBlock = new AuthenticationBlock(this);
    	persistorArgumentBlock = new PersistorArgumentBlock(this);
    	connectionArgumentBlock = new ConnectionArgumentBlock(this);
    	resourceBaseWorkingBlock = new ResourceBaseWorkingBlock(this);
    	fixtureArgumentBlock = new FixtureArgumentBlock(this);
    	otherArgumentsBlock = new OtherArgumentsBlock(this);
    	
    	typeArgumentBlock.setViewerArguments(viewerArgumentBlock);
    	typeArgumentBlock.setAuthenticationBlock(authenticationBlock);
    	typeArgumentBlock.setResourceBaseWorkingBlock(resourceBaseWorkingBlock);
    	typeArgumentBlock.setPersistorArguments(persistorArgumentBlock);
    	typeArgumentBlock.setConnectorArguments(connectionArgumentBlock);
    	
    	viewerArgumentBlock.setTypeArguments(typeArgumentBlock);
    	viewerArgumentBlock.setAuthenticationBlock(authenticationBlock);
    	viewerArgumentBlock.setResourceBaseWorkingBlock(resourceBaseWorkingBlock);
    	
    	authenticationBlock.setViewerArguments(viewerArgumentBlock);
    	
    	persistorArgumentBlock.setTypeArgumentBlock(typeArgumentBlock);
    }

    private final TypeArgumentBlock typeArgumentBlock;
    private final ViewerArgumentBlock viewerArgumentBlock;
    private final AuthenticationBlock authenticationBlock;
    private final PersistorArgumentBlock persistorArgumentBlock;
    private final ConnectionArgumentBlock connectionArgumentBlock;
    private final ResourceBaseWorkingBlock resourceBaseWorkingBlock;
    private final FixtureArgumentBlock fixtureArgumentBlock;
    private final OtherArgumentsBlock otherArgumentsBlock;


    public void createControl(
            Composite controlParent) {
        super.createControl(controlParent);
        
        Font font = controlParent.getFont();

        GridLayout layout;
        GridData gd ;
        
        Composite parent = new Composite(controlParent, SWT.NONE);
        layout = new GridLayout(1, true);
        parent.setLayout(layout);
        parent.setFont(font);
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));
        setControl(parent);
        
        Composite radioButtonGroups = new Composite(parent, SWT.NONE);
        layout = new GridLayout(5, true);
        radioButtonGroups.setLayout(layout);
        radioButtonGroups.setFont(font);
        
        gd = new GridData(GridData.FILL_BOTH);
        radioButtonGroups.setLayoutData(gd);
        
        typeArgumentBlock.createControl(radioButtonGroups);
        viewerArgumentBlock.createControl(radioButtonGroups);
        authenticationBlock.createControl(radioButtonGroups);
        persistorArgumentBlock.createControl(radioButtonGroups);
        connectionArgumentBlock.createControl(radioButtonGroups);
        fixtureArgumentBlock.createControl(parent);

        resourceBaseWorkingBlock.createControl(parent);
        resourceBaseWorkingBlock.disable();

        otherArgumentsBlock.createControl(parent);

//        fVMArgumentsBlock.createControl(pane);
//        fWorkingDirectoryBlock.createControl(pane);     

        hookListeners();
    }


    private void hookListeners() {
    	typeArgumentBlock.hookListeners();
        viewerArgumentBlock.hookListeners();
    }

    @Override
    public void initializeFrom(
            final ILaunchConfiguration configuration) {
        super.initializeFrom(configuration);
        try {
        	typeArgumentBlock.initializeFrom(configuration);
        	viewerArgumentBlock.initializeFrom(configuration);
            authenticationBlock.initializeFrom(configuration);
            fixtureArgumentBlock.initializeFrom(configuration);
            otherArgumentsBlock.initializeFrom(configuration);
            persistorArgumentBlock.initializeFrom(configuration);
            resourceBaseWorkingBlock.initializeFrom(configuration);
        } catch (CoreException e) {
            getLOGGER().error(e);
        }

        // enable/disable as required
        fireAllHandlers();fireAllHandlers(); 
    }

    private void fireAllHandlers() {
    	typeArgumentBlock.handleViewerAndConnectorAndPersistorGroupEnablement();
        viewerArgumentBlock.handleAuthenticationBlockEnablement();
        viewerArgumentBlock.handleResourceBaseEnablement();
    }
    
    
    public void performApply(
            ILaunchConfigurationWorkingCopy configuration) {
    	
        StringBuilder argumentsBuffer = new StringBuilder();
        viewerArgumentBlock.performApply(configuration, argumentsBuffer);
        typeArgumentBlock.performApply(configuration, argumentsBuffer);
        authenticationBlock.performApply(configuration, argumentsBuffer);
        persistorArgumentBlock.performApply(configuration, argumentsBuffer);
        connectionArgumentBlock.performApply(configuration, argumentsBuffer);
        fixtureArgumentBlock.performApply(configuration, argumentsBuffer);
        otherArgumentsBlock.performApply(configuration, argumentsBuffer);
    	resourceBaseWorkingBlock.performApply(configuration, argumentsBuffer);

//      fVMArgumentsBlock.performApply(configuration);
//      fWorkingDirectoryBlock.performApply(configuration);

        configuration.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, argumentsBuffer.toString());

    }

    public void setDefaults(
            ILaunchConfigurationWorkingCopy configuration) {
        super.setDefaults(configuration);

        typeArgumentBlock.setDefaults(configuration);
        viewerArgumentBlock.setDefaults(configuration);
        persistorArgumentBlock.setDefaults(configuration);
        fixtureArgumentBlock.setDefaults(configuration);
        resourceBaseWorkingBlock.setDefaults(configuration);
    }

    
    /**
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setLaunchConfigurationDialog(ILaunchConfigurationDialog)
     */
    public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
        super.setLaunchConfigurationDialog(dialog);
        resourceBaseWorkingBlock.setLaunchConfigurationDialog(dialog);
    }
    
    /**
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getErrorMessage()
     */
    public String getErrorMessage() {
        String m = super.getErrorMessage();
        if (m == null) {
            m = resourceBaseWorkingBlock.getErrorMessage();
        }
        return m;
    }

    /**
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getMessage()
     */
    public String getMessage() {
        String m = super.getMessage();
        if (m == null) {
        	m = resourceBaseWorkingBlock.getMessage();
        }
        return m;
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#activated(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
     */
    public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
    	resourceBaseWorkingBlock.initializeFrom(workingCopy);
    }


    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected Button createRadioButton(Composite parent, String label) {
		return super.createRadioButton(parent, label);	
	}	
	
    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected Button createPushButton(Composite parent, String label, Image image) {
		return super.createPushButton(parent, label, image);
	}


    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}
	
    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected Shell getShell() {
		return super.getShell();
	}
	
    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected ILaunchConfigurationDialog getLaunchConfigurationDialog() {
		return super.getLaunchConfigurationDialog();
	}
	
    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected void setDirty(boolean dirty) {
		super.setDirty(dirty);
	}
	
    /**
     * Make visible to helper classes in this package.
     */
	@Override
	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
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
