package com.halware.nakedide.eclipse.icons.wizards.common.pages;

import org.apache.log4j.Logger;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.pde.internal.ui.wizards.plugin.AbstractFieldData;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationPage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.halware.eclipseutil.util.ReflectionUtils;


@SuppressWarnings("restriction")
public class CommonProjectCreationPage extends
        NewProjectCreationPage implements ISuggestedProjectNameListener {

    private static final String DEFAULT_ICON_PROJECT_NAME = "org.nakedobjects.icons";

    private final static Logger LOGGER = Logger.getLogger(CommonProjectCreationPage.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

    public CommonProjectCreationPage(
            String pageName,
            AbstractFieldData data, String pageTitle, String pageDescription) {

        super(pageName, data, false);
        setTitle(pageTitle);  
        setDescription(pageDescription);
    }

    public void createControl(Composite parent) {
        IPreferenceStore store = PreferenceConstants.getPreferenceStore();
        store.setDefault(PreferenceConstants.SRCBIN_SRCNAME, "src/main/java");
        super.createControl(parent);
        fJavaButton.setEnabled(false);
        
        hookProjectNameFieldListener();
        
        // hack
        getProjectNameText().setText(DEFAULT_ICON_PROJECT_NAME);

    }

    /**
     * Watch the (inherited) project name field and make sure follows any
     * conventions.
     */
    private void hookProjectNameFieldListener() {
        final Text projectNameText = getProjectNameText();
        if (projectNameText == null) {
            getLOGGER().warn("Unable to generate any warning messages on project name");
            return;
        }
        projectNameText.addModifyListener(new ModifyListener() {
            public void modifyText(
                    ModifyEvent e) {
                if (getSuggestedProjectName() == null) {
                    return;
                }
                String projectName = projectNameText.getText();
                if (!projectName.equals(getSuggestedProjectName())) {
                    setMessage(
                        String.format("Suggest project name is '%s'", getSuggestedProjectName()), 
                        IMessageProvider.WARNING);
                }
            }});
    }
    
    
    /**
     * Returns the projectName {@link Text} field from the superclass.
     * 
     * <p>
     * This is unfortunately very hacky because the widget is
     * not visible to us.
     */
    public Text getProjectNameText() {
        try {
            return (Text)ReflectionUtils.getField(
                    WizardNewProjectCreationPage.class,
                    this,
                    "projectNameField");
        } catch (Exception e) {
            getLOGGER().warn("Unable to obtain project name field", e);
            return null;
        }
    }

    private String suggestedProjectName;
    public String getSuggestedProjectName() {
        return suggestedProjectName;
    }

    public void setSuggestedProjectName(
            String suggestedProjectName) {
        getLOGGER().info(String.format("Suggested project name is '%s'", suggestedProjectName));
        Text projectNameText = getProjectNameText();
        this.suggestedProjectName = suggestedProjectName;
        if (projectNameText != null) {
            projectNameText.setText(suggestedProjectName);
        }
    }

    
    

}
