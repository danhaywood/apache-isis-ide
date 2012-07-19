package com.halware.nakedide.eclipse.core.launches;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class TypeArgumentBlock extends AbstractArgumentBlock {

    private Group typeGroup;
    private Button explorationRB;
    public Button getExplorationRB() {
		return explorationRB;
	}
    private Button prototypeRB;
    @SuppressWarnings("unused")
    private Button standaloneRB;
    private Button clientRB;
    private Button serverRB;
    
    private List<Button> typeRadioButtons = new ArrayList<Button>();

	public TypeArgumentBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		super(nakedObjectsArgumentsTab);
	}

	public void createControl(Composite radioButtonGroups) {
		Font font = radioButtonGroups.getFont();
        typeGroup = createGroup(radioButtonGroups, font, "&Type");
        typeRadioButtons.add(
            explorationRB = createButton(typeGroup, "exploration", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_EXPLORATION));
        typeRadioButtons.add(
                prototypeRB = createButton(typeGroup, "prototype", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_PROTOTYPE));
        typeRadioButtons.add(
                standaloneRB = createButton(typeGroup, "standalone", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_STANDALONE));
        typeRadioButtons.add(
                clientRB = createButton(typeGroup, "client", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_CLIENT));
        typeRadioButtons.add(
                serverRB = createButton(typeGroup, "server", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_SERVER));
	}

	public void hookListeners() {
		SelectionListener persistorGroupListener = new SelectionAdapter() {
            public void widgetSelected(
                    SelectionEvent e) {
                handleViewerAndConnectorAndPersistorGroupEnablement();
            }
        };

		for(Button button: typeRadioButtons) {
            button.addSelectionListener(authenticationBlock);
            button.addSelectionListener(persistorGroupListener);
        }
	}

	public void handleViewerAndConnectorAndPersistorGroupEnablement() {
		
        if (isSelected(explorationRB) || isSelected(prototypeRB)) {

        	viewerArgumentBlock.ensureRadioButtonEnabled();
        	connectionArgumentBlock.disableAllRadioButtons();
        	persistorArgumentBlock.disableAllRadioButtons();
            
        } else if (isSelected(clientRB)) {

        	viewerArgumentBlock.ensureRadioButtonEnabled();
        	connectionArgumentBlock.ensureClientOnlyRadioButtonsEnabled();
        	persistorArgumentBlock.disableAllRadioButtons();

        } else if (isSelected(serverRB)) {

        	viewerArgumentBlock.disableAllRadioButtons();
        	connectionArgumentBlock.ensureRadioButtonEnabled();
        	persistorArgumentBlock.ensureRadioButtonEnabledExcludingInMemoryRB();

        } else if (isSelected(standaloneRB) ) {

        	viewerArgumentBlock.ensureRadioButtonEnabled();
        	connectionArgumentBlock.disableAllRadioButtons();
        	persistorArgumentBlock.ensureRadioButtonEnabled();
        }
	}

	public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
        String type = configuration.getAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_PROTOTYPE);
        for(Button button: typeRadioButtons) {
            button.setSelection(LaunchUtils.getData(button).equals(type));
        }
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE_PROTOTYPE);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
        LaunchUtils.performApply(
                argumentsBuffer, "--type", typeRadioButtons, 
                configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_TYPE);
	}

	private AuthenticationBlock authenticationBlock;
	public void setAuthenticationBlock(AuthenticationBlock authenticationBlock) {
		this.authenticationBlock = authenticationBlock;
	}

	private ViewerArgumentBlock viewerArgumentBlock;
	public void setViewerArguments(ViewerArgumentBlock viewerArgumentBlock) {
		this.viewerArgumentBlock = viewerArgumentBlock;
	}
	
	@SuppressWarnings("unused")
	private ResourceBaseWorkingBlock resourceBaseWorkingBlock;
	public void setResourceBaseWorkingBlock(
			ResourceBaseWorkingBlock resourceBaseWorkingBlock) {
		this.resourceBaseWorkingBlock = resourceBaseWorkingBlock;
	}

	private PersistorArgumentBlock persistorArgumentBlock;
	public void setPersistorArguments(PersistorArgumentBlock persistorArgumentBlock) {
		this.persistorArgumentBlock = persistorArgumentBlock;
	}
	
	private ConnectionArgumentBlock connectionArgumentBlock;
	public void setConnectorArguments(ConnectionArgumentBlock connectionArgumentBlock) {
		this.connectionArgumentBlock = connectionArgumentBlock;
	}

	public boolean isExplorationSelected() {
		return isSelected(getExplorationRB());
	}
	

}
