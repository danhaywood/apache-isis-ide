package com.halware.nakedide.eclipse.icons.wizards.common;

import org.eclipse.pde.ui.templates.AbstractTemplateSection;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

/**
 * This can't be collapsed into a common class, passing the specific stuff (window title 
 * and template sections) as args into the constructor, because - unfortunately - 
 * the {@link NewPluginTemplateWizard} calls the {@link #createTemplateSections()} from the constructor.
 */
@SuppressWarnings("restriction")
public abstract class AbstractProjectTemplateWizard extends NewPluginTemplateWizard {

    public AbstractProjectTemplateWizard() {
        setWindowTitle(getProjectWindowTitle()); 
	}

    private AbstractProjectTemplateSection projectTemplateSection;
    public AbstractProjectTemplateSection getProjectTemplateSection() {
        return projectTemplateSection;
    }

    /**
     * This method is unforunately from {@link NewPluginTemplateWizard}'s constructor,
     * which means it cannot reference any of the subclasses instance variables.
     * 
     * <p>
     * Stores the provided {@link AbstractTemplateSection} en route to passing up
     * to the superclass.
     */
    public ITemplateSection[] createTemplateSections() {
        projectTemplateSection = createTemplateSection();
        return new ITemplateSection [] { projectTemplateSection };
    }

    public abstract AbstractProjectTemplateSection createTemplateSection();
        
    protected abstract String getProjectWindowTitle();

    public void setBundleIdProvider(
            IRequiredBundleIdProvider requiredBundleIdProvider) {
        projectTemplateSection.setBundleIdProvider(requiredBundleIdProvider);
    }

//    public void setExportedPackageProvider(
//            IExportedPackageProvider exportedPackageProvider) {
//        projectTemplateSection.setExportedPackageProvider(exportedPackageProvider);
//    }
    
}
