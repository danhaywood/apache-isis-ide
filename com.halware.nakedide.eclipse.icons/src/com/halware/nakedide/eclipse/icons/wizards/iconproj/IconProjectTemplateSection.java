package com.halware.nakedide.eclipse.icons.wizards.iconproj;

import com.halware.nakedide.eclipse.icons.wizards.common.AbstractProjectTemplateSection;
import com.halware.nakedide.eclipse.icons.wizards.common.IRequiredBundles;


@SuppressWarnings("restriction")
public class IconProjectTemplateSection extends AbstractProjectTemplateSection {

    protected IconProjectTemplateSection(){
    }

    @Override
    public String getSectionId() {
        return "nakedObjectsIconProject";
    }

    @Override
    protected String getWizardPageDescription() {
        return "Create a new icon project"; //ProjectWizardsMessages.PluginProjectTemplate_desc;
    }

    @Override
    protected String getWizardPageTitle() {
        return "Create a set of icons"; // ProjectWizardsMessages.PluginProjectTemplate_title;
    }

    @Override
    protected String getDefaultFormattedPackageName() {
        return "org.nakedobjects.icons";
    }

    @Override
    protected IRequiredBundles getRequiredBundles() {
        return new IconProjectRequiredBundles();
    }
}
