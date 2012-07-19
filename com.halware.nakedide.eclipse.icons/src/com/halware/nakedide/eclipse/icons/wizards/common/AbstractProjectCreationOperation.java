package com.halware.nakedide.eclipse.icons.wizards.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.framework.internal.core.Constants;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModelFactory;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationOperation;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.TemplateOption;

import com.halware.eclipseutil.util.IOUtils;
import com.halware.eclipseutil.util.ManifestUtils;

@SuppressWarnings("restriction")
public abstract class AbstractProjectCreationOperation extends NewProjectCreationOperation {

    protected abstract Logger getLOGGER();

    public AbstractProjectCreationOperation(
            IFieldData data,
            IProjectProvider provider,
            AbstractProjectTemplateWizard contentWizard) {
        super(data, provider, contentWizard);
        this.projectProvider = provider;
        this.fieldData = data;
        this.contentWizard = contentWizard;
    }

    private IProjectProvider projectProvider;
    public IProjectProvider getProjectProvider() {
        return projectProvider;
    }

    private IFieldData fieldData;
    public IFieldData getFieldData() {
        return fieldData;
    }

    private AbstractProjectTemplateWizard contentWizard;
    public AbstractProjectTemplateWizard getContentWizard() {
        return contentWizard;
    }
    
    
    protected void execute(IProgressMonitor monitor) 
        throws CoreException, InvocationTargetException, InterruptedException {
        updateOptionsForKeywordSubstitution();
        super.execute(monitor);
    }
    
    /**
     * Update any {@link TemplateOption}s
     * for keyword substitution by {@link AbstractProjectTemplateSection}s.
     *
     */
    protected void updateOptionsForKeywordSubstitution() {
    }



    @Override
    protected void fillBinIncludes(
            IProject project, 
            IBuildEntry binEntry) throws CoreException {
        binEntry.addToken("META-INF/"); //$NON-NLS-1$
        binEntry.addToken("."); //$NON-NLS-1$
    }

    @Override
    protected final void createSourceOutputBuildEntries(WorkspaceBuildModel model,
            IBuildModelFactory factory) throws CoreException {
        super.createSourceOutputBuildEntries(model, factory);
        doAddAdditionalSourceOutputBuildEntries(model, factory);
    }

    /**
     * Hook method allowing subclasses to add source folders to the
     * output build entry.
     * 
     * <P>
     * That is, it adds an entry to <tt>source..</tt> in <tt>build.properties</tt>.  
     * 
     * <p>
     * Subclasses overriding this should first call the superclass' implementation, and then
     * invoke {@link #addSourceOutputBuildEntry(WorkspaceBuildModel, String)} to do the
     * actual work.  Subclasses that <i>do</i> override Should also 
     * override {@link AbstractProjectWizard#doAddAdditionalSourceFolders(org.eclipse.jdt.core.IJavaProject)}
     * to actually add the folder as a source path.
     * @throws CoreException 
     */
    protected void doAddAdditionalSourceOutputBuildEntries(
            WorkspaceBuildModel model,
            IBuildModelFactory factory) throws CoreException {
    }

    /**
     * Adds an entry to the source output, for convenience of subclasses.
     * 
     * <P>
     * That is, it adds an entry to <tt>source..</tt> in <tt>build.properties</tt>.  Should be
     * called from {@link #createSourceOutputBuildEntries(WorkspaceBuildModel, IBuildModelFactory)}
     * (after calling superclass' implementation first).
     * 
     * <p>
     * If do call this, then should also call 
     * 
     * 
     * @param model
     * @param sourcePath
     * @throws CoreException
     */
    protected void addSourceOutputBuildEntry(
            WorkspaceBuildModel model,
            String sourcePath) throws CoreException {
        IBuildEntry[] buildEntries = model.getBuild().getBuildEntries();
        for(IBuildEntry entry: buildEntries) {
            String entryName = entry.getName();
            if (entryName.equals("source..")) {
                entry.addToken(new Path(sourcePath).addTrailingSeparator().toString());
            }
        }
    }

    @Override
    protected void adjustManifests(
            IProgressMonitor monitor,
            IProject project) throws CoreException {
        super.adjustManifests(monitor, project);
        
        IFile manifestFile = project.getFile("META-INF/MANIFEST.MF");
        if (manifestFile == null || !manifestFile.exists()) {
            getLOGGER().warn("Unable to export packages");
            return;
        }
        
        InputStream manifestStream = null;
        Map<String,String> manifestProperties = null;
        try {
            manifestStream = manifestFile.getContents(true);
            Manifest manifest = new Manifest(manifestStream);
            manifestProperties = 
                ManifestUtils.manifestToProperties(manifest);
            manifestProperties.put(Constants.EXPORT_PACKAGE, getExportedPackageValue());
        } catch (Exception e) {
            getLOGGER().warn("Unable to export packages", e);
            return;
        } finally {
            IOUtils.closeSafely(manifestStream);
        }

        ManifestContents manifestContents = new ManifestContents();
        manifestContents.writeEntries(manifestProperties);

        try {
            manifestStream = new ByteArrayInputStream(
                    manifestContents.getContents().getBytes("UTF-8")); //$NON-NLS-1$
            manifestFile.setContents(manifestStream, false, false, null);
        } catch (Exception e) {
            getLOGGER().warn("Unable to export packages", e);
        } finally {
            IOUtils.closeSafely(manifestStream);
        }
    }

    public void setBundleIdProvider(
            IRequiredBundleIdProvider requiredBundleIdProvider) {
        contentWizard.setBundleIdProvider(requiredBundleIdProvider);
    }


    private IExportedPackageProvider exportedPackageProvider;
    protected IExportedPackageProvider getExportedPackageProvider() {
        return exportedPackageProvider;
    }
    public void setExportedPackageProvider(
            IExportedPackageProvider exportedPackageProvider) {
        this.exportedPackageProvider = exportedPackageProvider != null? exportedPackageProvider: IExportedPackageProvider.NULL;
    }

    private String getExportedPackageValue() {
        StringBuilder exportedPackagesBuf = new StringBuilder();
        for(String exportedPackage: getExportedPackageProvider().getExportedPackages()) {
            if (exportedPackagesBuf.length() > 0) {
                exportedPackagesBuf.append(",\n ");
            }
            exportedPackagesBuf.append(exportedPackage);
        }
        return exportedPackagesBuf.toString();
    }
    

}
