package com.halware.nakedide.eclipse.icons.wizards.iconproj;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;

import com.halware.nakedide.eclipse.icons.wizards.common.AbstractProjectCreationOperation;
import com.halware.nakedide.eclipse.icons.wizards.common.AbstractProjectWizard;
import com.halware.nakedide.eclipse.icons.wizards.common.ProjectWizardsMessages;

@SuppressWarnings("restriction")
public class IconProjectWizard extends AbstractProjectWizard {

    private static final Logger LOGGER = Logger.getLogger(IconProjectWizard.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

    public IconProjectWizard() {
        super(ProjectWizardsMessages.NewIconProject_windowTitle);
    }

    public IconProjectWizard(String osgiFramework) {
        super(ProjectWizardsMessages.NewIconProject_windowTitle, osgiFramework);
    }


    @Override
    protected String getCreationPageDescription() {
        return ProjectWizardsMessages.NewIconProjectWizard_MainPage_desc;
    }
    
    @Override
    protected String getCreationPageTitle() {
        return ProjectWizardsMessages.NewIconProjectWizard_MainPage_title;
    }

    @Override
    public void addPages() {
        super.addPages();
    }
    
    /**
     * Note that this has a hard-coded dependency on the templates.
     */
    @Override
    protected AbstractProjectCreationOperation createProjectCreationOperation(
            PluginFieldData pluginData, 
            IProjectProvider projectProvider) {
        IconProjectCreationOperation projectCreationOperation = new IconProjectCreationOperation(pluginData, projectProvider);
        projectCreationOperation.setBundleIdProvider(null);
        projectCreationOperation.setExportedPackageProvider(null);
        return projectCreationOperation;
    }

    
    @Override
    protected void doAddAdditionalSourceFolders(
            IJavaProject javaProject) {
        addAdditionalSourceFolder(javaProject, "src/main/resources");
    }

}
