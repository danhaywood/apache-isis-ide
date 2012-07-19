package com.halware.nakedide.eclipse.core.launches;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class ConnectionArgumentBlock extends AbstractArgumentBlock {

	
	public ConnectionArgumentBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		super(nakedObjectsArgumentsTab);
	}

    private Group connectorFactoryGroup;
    private Button xstreamRB;
    Button getXstreamRB() {
		return xstreamRB;
	}
    
    private List<Button> connectorRadioButtons = new ArrayList<Button>();
    public List<Button> getConnectorRadioButtons() {
		return connectorRadioButtons;
	}

    /**
     * Those that apply only for type=server (and not for client)
     */
    private List<Button> serverOnlyConnectorRadioButtons = new ArrayList<Button>();
    public List<Button> getServerOnlyConnectorRadioButtons() {
		return serverOnlyConnectorRadioButtons;
	}


	public void createControl(Composite radioButtonGroups) {
        connectorFactoryGroup = createGroup(radioButtonGroups, "&Connector");
		
        xstreamRB = addConnectorRB(createButton(connectorFactoryGroup, "xstream", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_XSTREAM));
        addConnectorRB(createButton(connectorFactoryGroup, "serialize", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_SERIALIZING));
        addConnectorRB(createButton(connectorFactoryGroup, "byte-encoded", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_BYTE_ENCODED));
        //addConnectorRBForServerOnly(createButton(connectorFactoryGroup, "telnet", LaunchConstants.lAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR_BYTE_ENCODED));
        //addConnectorRBForServer(createButton(connectorFactoryGroup, "telnet", LaunchConstants.lAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_WEB));

	}

    @SuppressWarnings("unused")
	private Button addConnectorRBForServerOnly(
            Button button) {
        addConnectorRB(button);
        serverOnlyConnectorRadioButtons.add(button);
        return button;
    }

    private Button addConnectorRB(
            Button button) {
        connectorRadioButtons.add(button);
        return button;
    }

	public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
        LaunchUtils.performApply(
                argumentsBuffer, "--connection", connectorRadioButtons, 
                configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_CONNECTOR);
	}


	public void disableAllRadioButtons() {
        if (anySelected(getConnectorRadioButtons())) {
            deselect(getConnectorRadioButtons());
        }
        disable(getConnectorRadioButtons());
	}


	public void ensureClientOnlyRadioButtonsEnabled() {
        if (anySelected(getServerOnlyConnectorRadioButtons())) {
            deselect(getServerOnlyConnectorRadioButtons());
        }
        enable(getConnectorRadioButtons());
        disable(getServerOnlyConnectorRadioButtons());
        if (!anySelected(getConnectorRadioButtons())) {
            select(getXstreamRB());
        }
	}

	public void ensureRadioButtonEnabled() {
        enable(getConnectorRadioButtons());
        if (!anySelected(getConnectorRadioButtons())) {
            select(getXstreamRB());
        }
	}
    

}
