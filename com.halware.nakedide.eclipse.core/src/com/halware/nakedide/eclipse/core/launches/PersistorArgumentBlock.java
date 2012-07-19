package com.halware.nakedide.eclipse.core.launches;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class PersistorArgumentBlock extends AbstractArgumentBlock {

	public PersistorArgumentBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		super(nakedObjectsArgumentsTab);
	}
	
    private Group persistorFactoryGroup;
    private Button inMemoryRB;
    public Button getInMemoryRB() {
		return inMemoryRB;
	}
    private Button xmlRB;
    public Button getXmlRB() {
		return xmlRB;
	}
    
    private List<Button> persistorRadioButtons = new ArrayList<Button>();
    public List<Button> getPersistorRadioButtons() {
		return persistorRadioButtons;
	}

	public void createControl(Composite radioButtonGroups) {
		Font font = radioButtonGroups.getFont();
        persistorFactoryGroup = createGroup(radioButtonGroups, font, "&Persistor");
        
        inMemoryRB = addPersistorRB(createButton(persistorFactoryGroup, "in-memory", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_IN_MEMORY));
        xmlRB = addPersistorRB(createButton(persistorFactoryGroup, "xml", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_XML));
//        addPersistorRB(createButton(persistorFactoryGroup, "sql", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_SQL));
        addPersistorRB(createButton(persistorFactoryGroup, "hibernate", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_HIBERNATE));
//        addPersistorRB(createButton(persistorFactoryGroup, "cache", LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_CACHE));

	}

    private Button addPersistorRB(
            Button button) {
        persistorRadioButtons.add(button);
        return button;
    }

	public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
        String persistor = configuration.getAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_IN_MEMORY);
        for(Button button: persistorRadioButtons) {
            button.setSelection(LaunchUtils.getData(button).equals(persistor));
        }
	}


	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR_IN_MEMORY);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
        LaunchUtils.performApply(
                argumentsBuffer, "--persistor", persistorRadioButtons, 
                configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_PERSISTOR);
	}



	public void ensureRadioButtonEnabled() {
        enable(getPersistorRadioButtons());
        if (!anySelected(getPersistorRadioButtons())) {
            select(getInMemoryRB());
        }
	}

	public void ensureRadioButtonEnabledExcludingInMemoryRB() {
        enable(getPersistorRadioButtons());
        disable(getInMemoryRB());
        if (!anySelected(getPersistorRadioButtons())) {
            select(getXmlRB());
        }
	}


	public void disableAllRadioButtons() {
        if (anySelected(getPersistorRadioButtons())) {
            deselect(getPersistorRadioButtons());
        }
        disable(getPersistorRadioButtons());
	}

	@SuppressWarnings("unused")
	private TypeArgumentBlock typeArgumentBlock;
	public void setTypeArgumentBlock(TypeArgumentBlock typeArgumentBlock) {
		this.typeArgumentBlock = typeArgumentBlock;
	}

}
