package com.halware.nakedide.eclipse.core.launches;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

class AuthenticationBlock extends AbstractArgumentBlock implements SelectionListener {
	
	public AuthenticationBlock(NakedObjectsArgumentsTab tab) {
		super(tab);
	}
    
	private Group authenticationGroup;
    private Text userText;
    private Text passwordText;
    void createControl(
            Composite parent) {
        Font font = parent.getFont();
        authenticationGroup = createGroup(parent, font, "Authentication");

        userText = createTextWithLabel(authenticationGroup, "user");
        passwordText = createTextWithLabel(authenticationGroup, "password");
    }
    
    private Text createTextWithLabel(
            Group group, 
            String labelText) {
        Font font = group.getFont();
        GridData gd;
        Label label = new Label(group, SWT.NONE);
        label.setFont(font);
        label.setText(labelText);
        Text text = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gd);
        text.setFont(font);
        text.addModifyListener(new MakeDirtyModifyListener(getTab()));
        return text;
    }


    void disable() {
        clear(userText);
        clear(passwordText);
        disable(userText);
        disable(passwordText);
    }
    void enable() {
        enable(userText);
        enable(passwordText);
    }
    void initializeFrom(
            ILaunchConfiguration configuration) throws CoreException {
        String user = configuration.getAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_USER, "");
        userText.setText(user);
        
        String password = configuration.getAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PASSWORD, "");
        passwordText.setText(password);

    }
    void performApply(
            ILaunchConfigurationWorkingCopy configuration,
            StringBuilder argumentsBuffer) {
    	LaunchUtils.performApply(
                argumentsBuffer, "--user", 
                userText, configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_USER);

    	LaunchUtils.performApply(
                argumentsBuffer, "--password", 
                passwordText, configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PASSWORD);
    }
    public void widgetSelected(
            SelectionEvent e) {
    	viewerArgumentBlock.handleAuthenticationBlockEnablement();
    }
    public void widgetDefaultSelected(
            SelectionEvent e) {
        // does nothing
    }
    
	private ViewerArgumentBlock viewerArgumentBlock;
	public void setViewerArguments(ViewerArgumentBlock viewerArgumentBlock) {
		this.viewerArgumentBlock = viewerArgumentBlock;
	}
	


}