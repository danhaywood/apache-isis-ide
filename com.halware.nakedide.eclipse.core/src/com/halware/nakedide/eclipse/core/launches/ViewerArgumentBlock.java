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

public class ViewerArgumentBlock extends AbstractArgumentBlock {

    public ViewerArgumentBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		super(nakedObjectsArgumentsTab);
	}

	private Group viewerGroup;
	
    private Button dndRB;
	public Button getDndRB() {
		return dndRB;
	}
	private boolean isDndSelected() {
		return isSelected(dndRB);
	}

    private Button htmlRB;
    public Button getHtmlRB() {
		return htmlRB;
	}
    private boolean isHtmlSelected() {
    	return isSelected(htmlRB);
    }
    
//    @SuppressWarnings("unused")
//    private Button cliRB;
//    @SuppressWarnings("unused")
//    private Button cliAwtRB;

    
    private List<Button> viewerRadioButtons = new ArrayList<Button>();
	public List<Button> getViewerRadioButtons() {
		return viewerRadioButtons;
	}
	
    private Button addViewerRB(
            Button button) {
        viewerRadioButtons.add(button);
        return button;
    }


	public void createControl(Composite radioButtonGroups) {
		Font font = radioButtonGroups.getFont();
        viewerGroup = createGroup(radioButtonGroups, font, "&Viewer");
        addViewerRB(
                dndRB = createButton(viewerGroup, "dnd", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_DND));
        addViewerRB(
                htmlRB = createButton(viewerGroup, "html", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_HTML));
//        addViewerRB(
//                cliRB = createButton(viewerGroup, "cli", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_CLI));
//        addViewerRB(
//                cliAwtRB = createButton(viewerGroup, "cli-awt", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_CLI_AWT));
		
	}


	public void hookListeners() {

		SelectionListener resourceBaseListener = new SelectionAdapter() {
            public void widgetSelected(
                    SelectionEvent e) {
                handleResourceBaseEnablement();
            }
        };

		for(Button button: viewerRadioButtons) {
            button.addSelectionListener(authenticationBlock);
            button.addSelectionListener(resourceBaseListener);
        }
	}

	public void handleResourceBaseEnablement() {
		if (isHtmlSelected()) {
			resourceBaseWorkingBlock.enable();
		} else {
			resourceBaseWorkingBlock.disable();
		}
	}

	public void handleAuthenticationBlockEnablement() {
        if (isDndSelected() && !typeArgumentBlock.isExplorationSelected()) {
            authenticationBlock.enable();
        } else {
            authenticationBlock.disable();
        }
	}


	public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
        String viewer = configuration.getAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_DND);
        for(Button button: viewerRadioButtons) {
            button.setSelection(LaunchUtils.getData(button).equals(viewer));
        }
	}


	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER_DND);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
        LaunchUtils.performApply(
                argumentsBuffer, "--viewer", viewerRadioButtons, 
                configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_VIEWER);
	}

	public void ensureRadioButtonEnabled() {
    	enable(getViewerRadioButtons());
        if (!anySelected(getViewerRadioButtons())) {
            select(getDndRB());
        }
	}
	public void disableAllRadioButtons() {
    	disable(getViewerRadioButtons());
	}

	
	private TypeArgumentBlock typeArgumentBlock;
	public void setTypeArguments(TypeArgumentBlock typeArgumentBlock) {
		this.typeArgumentBlock = typeArgumentBlock;
	}

	private AuthenticationBlock authenticationBlock;
	public void setAuthenticationBlock(AuthenticationBlock authenticationBlock) {
		this.authenticationBlock = authenticationBlock;
	}
	
	private ResourceBaseWorkingBlock resourceBaseWorkingBlock;
	public void setResourceBaseWorkingBlock(
			ResourceBaseWorkingBlock resourceBaseWorkingBlock) {
		this.resourceBaseWorkingBlock = resourceBaseWorkingBlock;
	}

}
