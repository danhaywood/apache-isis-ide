package com.halware.nakedide.eclipse.icons.wizards.common;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.NewWizard;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.TemplateOption;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.halware.nakedide.eclipse.icons.Activator;
import com.halware.nakedide.eclipse.icons.Images;
import com.halware.nakedide.eclipse.icons.wizards.common.pages.CommonProjectCreationPage;
import com.halware.nakedide.eclipse.icons.wizards.common.pages.CommonProjectManifestPage;
import com.halware.nakedide.eclipse.icons.wizards.common.pages.ISuggestedProjectNameProvider;

/**
 * <pre>
 * ProjectWizard#performFinish()
 *   -> CreationOperation#getContentWizard()
 *     -> ProjectTemplateWizard#createTemplateSections()
 *        -> ProjectTemplateSection#getDependencies()
 *           -> ProjectRequiredBundles
 * </pre>
 * 
 */
@SuppressWarnings("restriction")
public abstract class AbstractProjectWizard extends NewWizard implements IExecutableExtension {

    public abstract Logger getLOGGER();

    private IConfigurationElement config;
    protected final PluginFieldData pluginData;
    private IProjectProvider projectProvider;
    
    protected CommonProjectCreationPage mainPage;
    private CommonProjectManifestPage manifestPage;

    public AbstractProjectWizard(String windowTitle) {
        setWindowTitle(windowTitle);
        setDefaultPageImageDescriptor(Images.create("hal_banner.gif"));
        setDialogSettings(PDEPlugin.getDefault().getDialogSettings());
        setNeedsProgressMonitor(true);
        PDEPlugin.getDefault().getLabelProvider().connect(this);
        pluginData = new PluginFieldData();
    }

    protected String getSelectedProjectIdIfAny() {
        IWorkbenchWindow wnd = Activator.getActiveWorkbenchWindow();
        IWorkbenchPage pg = wnd.getActivePage();
        ISelection selection = pg.getSelection();

        if (selection == null || selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
            return null;
        }
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        Object firstElement = structuredSelection.getFirstElement();
        if (firstElement instanceof IProject) {
            IProject project = (IProject)firstElement;
            return project.getName();
        } else
        if (firstElement instanceof IJavaProject) {
            IJavaProject project = (IJavaProject)firstElement;
            return project.getProject().getName();
        }
        return null;
    }


    public AbstractProjectWizard(String windowTitle, String osgiFramework) {
        this(windowTitle);
        pluginData.setOSGiFramework(osgiFramework);
    }

    private class MainPageProjectProvider implements IProjectProvider {
        public String getProjectName() {
            return mainPage.getProjectName();
        }
        public IProject getProject() {
            return mainPage.getProjectHandle();
        }
        public IPath getLocationPath() {
            return mainPage.getLocationPath();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        ISuggestedProjectNameProvider suggestedProjectNameProvider = doAddPages();
        
        mainPage = new CommonProjectCreationPage(
                "main", pluginData,
                getCreationPageTitle(),   
                getCreationPageDescription());

        addPage(mainPage);

        if (suggestedProjectNameProvider != null) {
            suggestedProjectNameProvider.addSuggestedProjectNameListener(mainPage);    
        }

        projectProvider = new MainPageProjectProvider();
        manifestPage = new CommonProjectManifestPage(
                "bundleInfo", projectProvider, mainPage, pluginData); //$NON-NLS-1$
        addPage(manifestPage);
    }


    public String getProjectName() {
        return mainPage.getProjectName();
    }

    protected abstract String getCreationPageDescription();
    protected abstract String getCreationPageTitle();

    /**
     * Hook method allowing subclasses to optionally add additional 
     * pages, using {@link #addPage(IWizardPage)}.
     */
    protected ISuggestedProjectNameProvider doAddPages() {
        return null;
    }

    /**
     * Can only finish on the manifest page.
     * 
     * @see org.eclipse.jface.wizard.Wizard#canFinish()
     */
    @Override
    public boolean canFinish() {
        IWizardPage page = getContainer().getCurrentPage();
        return super.canFinish() && page == getPages()[getPageCount()-1];
    }

    @Override
    public boolean performFinish() {
        try {
            mainPage.updateData();
            manifestPage.updateData();
            doAdditionalPagesUpdateData();
            getContainer().run(
                    false,
                    true,
                    createProjectCreationOperation(pluginData, projectProvider));
            IProject project = projectProvider.getProject();
            doAddLaunchConfigurations(project);
            IJavaProject javaProject = JavaCore.create(project);
            addJava5Preference(javaProject);
            doAddAdditionalSourceFolders(javaProject);
            BasicNewProjectResourceWizard.updatePerspective(config);
            return true;
        } catch (InvocationTargetException e) {
            PDEPlugin.logException(e);
        } catch (InterruptedException e) {
        }
        return false;
    }

    /**
     * Hook method to allow subclasses that have installed additional pages
     * (using {@link #addPages()}) to update the {@link IFieldData} (as per
     * {@link #createFieldData()} with extra information.
     *
     * <p>
     * The reason for doing this is so that when the {@link AbstractProjectCreationOperation}
     * executes, it can through its own hook method 
     * {@link AbstractProjectCreationOperation#updateOptionsForKeywordSubstitution()}
     * provide the facility to update any {@link TemplateOption}s using the field data. 
     */
    protected void doAdditionalPagesUpdateData() {
    }

    protected abstract AbstractProjectCreationOperation createProjectCreationOperation(
            PluginFieldData pluginData, IProjectProvider projectProvider);

    /**
     * Hook method to add additional source folders.
     * 
     * <p>
     * Subclasses overriding this should use {@link #addAdditionalSourceFolder(IJavaProject, String)} to 
     * do the work.  If <i>do</i> call this, then should also override
     * {@link AbstractProjectCreationOperation#doCreateSourceOutputBuildEntries(org.eclipse.pde.internal.core.build.WorkspaceBuildModel, org.eclipse.pde.core.build.IBuildModelFactory)} 
     * to add source folders to the <tt>source..</tt> entry in <tt>build.properties</tt>.
     * 
     * @param javaProject
     */
    protected void doAddAdditionalSourceFolders(
            IJavaProject javaProject) {
    }

    protected void doAddLaunchConfigurations(IProject pluginProject) {
    }

    /**
     * For subclasses; deletes any existing launch configuration of the given name, and
     * creates an unsaved launch configuration working copy ready to have
     * attributes set on it and then saved.
     * 
     * @param pluginProject
     * @param launchConfigName
     * @param launchConfigurationTypeId
     * @return
     */
    protected ILaunchConfigurationWorkingCopy recreateLaunchConfiguration(
            IProject pluginProject, 
            String localLaunchConfigName, 
            String launchConfigurationTypeId) {
        String launchConfigName = 
            deriveQualifiedLaunchConfigName(pluginProject, localLaunchConfigName);
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type = 
            manager.getLaunchConfigurationType(launchConfigurationTypeId);
        
        try {
            // delete if already exists.
            ILaunchConfiguration[] launchConfigurations = manager.getLaunchConfigurations(type);
            for (ILaunchConfiguration launchConfiguration : launchConfigurations) {
                if (launchConfiguration.getName().equals(launchConfigName)) {
                    launchConfiguration.delete();
                }
            }
            // (re)create
            ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(
                    pluginProject, launchConfigName);
            // put in 'exec' folder if possible
            IFolder execFolder = pluginProject.getFolder(new Path("exec"));
            if (execFolder.exists()) {
                workingCopy.setContainer(execFolder);
            } else {
                workingCopy.setContainer(pluginProject);
            }
            return workingCopy;
        } catch (CoreException e1) {
            getLOGGER().warn(
                    "Failed to (re)create launch configuration - skipping", e1);
            return null;
        }
    }

    /**
     * Simply appends <tt>" (plugin.project.name)"</tt> to end of
     * supplied name.
     * 
     * @param pluginProject
     * @param localLaunchConfigName
     * @return
     */
    private String deriveQualifiedLaunchConfigName(
            IProject pluginProject,
            String localLaunchConfigName) {
        String pluginName = pluginProject.getName();
        return localLaunchConfigName + " (" + pluginName + ")";
    }



    private void addJava5Preference(IJavaProject javaProject) {
        if (javaProject == null) {
            getLOGGER().warn("Unable to add Java 5 preference to project - skipping");
            return;
        }
        javaProject.setOption(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
        javaProject.setOption(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
        javaProject.setOption(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
        javaProject.setOption(JavaCore.COMPILER_PB_ASSERT_IDENTIFIER, JavaCore.ERROR);
        javaProject.setOption(JavaCore.COMPILER_PB_ENUM_IDENTIFIER, JavaCore.ERROR);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        PDEPlugin.getDefault().getLabelProvider().disconnect(this);
    }

    /*
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
     *      java.lang.String, java.lang.Object)
     */
    public void setInitializationData(final IConfigurationElement config,
            String propertyName, Object data) throws CoreException {
        this.config = config;
    }

    public String getPluginId() {
        return pluginData.getId();
    }

    
    ///////////////////////////////////
    
    /**
     * For subclasses.
     * 
     * @param javaProject
     * @param sourcePath
     */
    protected final void addAdditionalSourceFolder(
            IJavaProject javaProject,
            String sourcePath) {
        if (javaProject == null) {
            getLOGGER().warn("Unable to make " + sourcePath + " as source folder - skipping");
            return;
        }
        IProject project = javaProject.getProject();
        if (project == null) {
            getLOGGER().warn("Unable to make " + sourcePath + " as source folder - skipping");
            return;
        }
        Path srcMainResourcesPathRelative = new Path(sourcePath);
        if (!project.exists(srcMainResourcesPathRelative)) {
            getLOGGER().warn("Unable to make " + sourcePath + " as source folder - skipping");
            return;
        }
        IPath srcMainResourcesPathAbsolute = project.getFullPath().append(srcMainResourcesPathRelative);
        try {
            addClasspathEntry(
                    javaProject, JavaCore.newSourceEntry(srcMainResourcesPathAbsolute));
        } catch (JavaModelException e) {
            getLOGGER().warn("Unable to make " + sourcePath + " as source folder - skipping");
            return;
        }
    }

    /**
     * For use by subclasses.
     * 
     * @param javaProject
     * @param newSourceEntry
     * @throws JavaModelException
     */
    protected final void addClasspathEntry(
            IJavaProject javaProject, IClasspathEntry newSourceEntry) throws JavaModelException {
        IClasspathEntry[] entries = appendClasspathEntry(javaProject.getRawClasspath(), newSourceEntry);
        javaProject.setRawClasspath(entries, new NullProgressMonitor());
    }

    /**
     * For use by subclasses.
     * 
     * @param rawClasspath
     * @param newSourceEntry
     * @return
     * @throws JavaModelException
     */
    protected final IClasspathEntry[] appendClasspathEntry(
            IClasspathEntry[] rawClasspath, IClasspathEntry newSourceEntry) throws JavaModelException {
        IClasspathEntry[] entries = new IClasspathEntry[rawClasspath.length+1];
        System.arraycopy(rawClasspath, 0, entries, 0, rawClasspath.length);
        entries[entries.length-1] = newSourceEntry;
        return entries;
    }


}
