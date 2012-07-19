package com.halware.nakedide.eclipse.icons.wizards.iconproj;

import com.halware.nakedide.eclipse.icons.wizards.common.AbstractProjectTemplateWizard;
import com.halware.nakedide.eclipse.icons.wizards.common.ProjectWizardsMessages;


@SuppressWarnings("restriction")
public class IconProjectTemplateWizard extends AbstractProjectTemplateWizard {

    
    public IconProjectTemplateWizard() {
    }

    /*
     * @see NewExtensionTemplateWizard#createTemplateSections()
     */
    @Override
    public IconProjectTemplateSection createTemplateSection() {
        return new IconProjectTemplateSection();
    }

    protected String getProjectWindowTitle() {
        return ProjectWizardsMessages.NewIconProject_windowTitle;
    }



}
