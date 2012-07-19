package com.halware.nakedide.eclipse.icons.wizards.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.internal.ui.wizards.templates.PDETemplateSection;
import org.eclipse.pde.internal.ui.wizards.templates.PluginReference;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.TemplateOption;
import org.osgi.framework.Bundle;

import com.halware.nakedide.eclipse.icons.Activator;

@SuppressWarnings("restriction")
public abstract class AbstractProjectTemplateSection extends PDETemplateSection {

    public AbstractProjectTemplateSection(){
        setPageCount(1);
        createOptions();
    }
    
    private TemplateOption applicationProjectNameOption;
    public TemplateOption getApplicationProjectNameOption() {
        return applicationProjectNameOption;
    }
    private TemplateOption fixtureProjectNameOption;
    public TemplateOption getFixtureProjectNameOption() {
        return fixtureProjectNameOption;
    }


    /**
     * Sets up <tt>$packageName$</tt>, <tt>$applicationProjectName$</tt> and
     * <tt>$fixtureProjectName$</tt>.
     *
     */
    protected void createOptions() {
        addOption(KEY_PACKAGE_NAME,
                "Package Name", //ProjectWizardsMessages.ClientProjectTemplate_packageName,
                (String) null, 0);
        applicationProjectNameOption = addOption("applicationProjectName",
                "com.mycompany.myapp",
                (String) null, 2);
        fixtureProjectNameOption = addOption("fixtureProjectName",
                "com.mycompany.myapp.fixtures",
                (String) null, 2);
    }

    /**
     * Effectively which subdirectory to use as the template, as per 
     * {@link #getTemplateLocation()}.
     */
    public abstract String getSectionId();

    /**
     * @see ITemplateSection#getNumberOfWorkUnits()
     */
    public int getNumberOfWorkUnits() {
        return super.getNumberOfWorkUnits() + 1;
    }

    public void addPages(Wizard wizard) {
        String helpContextId = null; // TODO: should provide, see
                                     // IHelpContextIds#TEMPLATE_HELLO_WORLD
        WizardPage page = createPage(0, helpContextId);
        page.setTitle(getWizardPageTitle());
        page.setDescription(getWizardPageDescription());
        wizard.addPage(page);
        markPagesAdded();
    }

    protected abstract String getWizardPageDescription();
    protected abstract String getWizardPageTitle();

    public void validateOptions(TemplateOption source) {
        if (source.isRequired() && source.isEmpty()) {
            flagMissingRequiredOption(source);
        } else {
            validateContainerPage(source);
        }
    }

    private void validateContainerPage(TemplateOption source) {
        TemplateOption[] allPageOptions = getOptions(0);
        for (int i = 0; i < allPageOptions.length; i++) {
            TemplateOption nextOption = allPageOptions[i];
            if (nextOption.isRequired() && nextOption.isEmpty()) {
                flagMissingRequiredOption(nextOption);
                return;
            }
        }
        resetPageState();
    }

    public boolean isDependentOnParentWizard() {
        return true;
    }

    protected void initializeFields(IFieldData data) {
        // In a new project wizard, we don't know this yet - 
        // the model has not been created
        String id = data.getId();
        initializeOption(KEY_PACKAGE_NAME, getFormattedPackageName(id));
    }

    public void initializeFields(IPluginModelBase model) {
        // In the new extension wizard, the model exists so
        // we can initialize directly from it
        String pluginId = model.getPluginBase().getId();
        initializeOption(KEY_PACKAGE_NAME, getFormattedPackageName(pluginId));
    }

    public String getUsedExtensionPoint() {
        return null;
    }

    /**
     * Filter out subversion files for icons helded in template.
     */
    @Override
    protected boolean isOkToCreateFile(File sourceFile) {
        return !isSubversionFile(sourceFile.getName()); 
    }

    /**
     * Filter out subversion folders for icons helded in template.
     */
    @Override
    protected boolean isOkToCreateFolder(File sourceFolder) {
        return !isSubversionFolder(sourceFolder.getName());
    }

    private boolean isSubversionFile(String fileName) {
        return fileName.endsWith(".svn-base");
    }
    
    private boolean isSubversionFolder(String folderName) {
        return folderName.equals(".svn");
    }

    /**
     * Configure plugin.xml if required.
     */
    protected void updateModel(IProgressMonitor monitor) throws CoreException {
        // IPluginBase pluginXml = model.getPluginBase();
    }

    /*
     * @see org.eclipse.pde.ui.templates.ITemplateSection#getFoldersToInclude()
     */
    public String[] getNewFiles() {
        return new String[] { /*"icons/"*/ }; //$NON-NLS-1$
    }


    /*
     * @see org.eclipse.pde.internal.ui.wizards.templates.PDETemplateSection#formatPackageName(java.lang.String)
     */
    protected String getFormattedPackageName(String id) {
        String packageName = super.getFormattedPackageName(id);
        if (packageName.length() != 0)
            return packageName;
        return getDefaultFormattedPackageName(); //$NON-NLS-1$
    }
    protected abstract String getDefaultFormattedPackageName();

    public URL getTemplateLocation() {
        try {
            String[] candidates = getDirectoryCandidates();
            for (int i = 0; i < candidates.length; i++) {
                Bundle bundle = Activator.getDefault().getBundle();
                if (bundle.getEntry(candidates[i]) != null) {
                    URL candidate = new URL(getInstallURL(), candidates[i]);
                    return candidate;
                }
            }
        } catch (MalformedURLException e) {
        }
        return null;
    }

    private String[] getDirectoryCandidates() {
        
        return new String[] { "wizardtemplates/" + getSectionId() + "/" };
    }

    protected URL getInstallURL() {
        return Activator.getDefault().getInstallURL();
    }

    /**
     * @see org.eclipse.pde.ui.IPluginContentWizard#getDependencies(String)
     */
    @SuppressWarnings("unchecked")
    public IPluginReference[] getDependencies(String schemaVersion) {
        // don't call super.getDependencies(schemaVersion), since that gives us org.eclipse.ui
        // (which we don't want).
        List<PluginReference> result = new ArrayList<PluginReference>();
        for(String bundleId: getRequiredBundles().getBundleIds()) {
            result.add(asPluginInReference(bundleId));
        }
        for(String bundleId: requiredBundleIdProvider.getBundleIds()) {
            result.add(asPluginInReference(bundleId));
        }
        return result.toArray(new IPluginReference[result.size()]);
    }

    private PluginReference asPluginInReference(
            String bundleId) {
        return new PluginReference(bundleId, null, 0);
    }

    protected abstract IRequiredBundles getRequiredBundles();

    private IRequiredBundleIdProvider requiredBundleIdProvider;
    protected IRequiredBundleIdProvider getBundleIdProvider() {
        return requiredBundleIdProvider;
    }
    public void setBundleIdProvider(
            IRequiredBundleIdProvider requiredBundleIdProvider) {
        this.requiredBundleIdProvider = requiredBundleIdProvider == null?IRequiredBundleIdProvider.NULL:requiredBundleIdProvider;
    }

}
