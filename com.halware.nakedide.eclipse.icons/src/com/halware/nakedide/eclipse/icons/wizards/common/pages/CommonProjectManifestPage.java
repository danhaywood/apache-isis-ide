package com.halware.nakedide.eclipse.icons.wizards.common.pages;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.plugin.AbstractFieldData;
import org.eclipse.pde.internal.ui.wizards.plugin.ContentPage;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.halware.nakedide.eclipse.icons.wizards.common.ProjectWizardsMessages;


@SuppressWarnings("restriction")
public class CommonProjectManifestPage extends ContentPage {

	public CommonProjectManifestPage(String pageName, IProjectProvider provider,
			CommonProjectCreationPage page,AbstractFieldData data) {
		super(pageName, provider, page, data);
        setTitle(ProjectWizardsMessages.ManifestPage_title);  
        setDescription(ProjectWizardsMessages.ManifestPage_desc);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.pde.internal.ui.wizards.plugin.ContentPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		
		createPluginPropertiesGroup(container);
        fIdText.setEnabled(false); // defaulted to project name, prevent from changing.
		
		Dialog.applyDialogFont(container);
		setControl(container);	
		PlatformUI.getWorkbench().getHelpSystem().setHelp(
			getControl(), IHelpContextIds.NEW_PROJECT_REQUIRED_DATA);
	}

	private void createPluginPropertiesGroup(Composite container) {
		Group propertiesGroup = new Group(container, SWT.NONE);
		propertiesGroup.setLayout(new GridLayout(2, false));
		propertiesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		propertiesGroup.setText(PDEUIMessages.ContentPage_pGroup); 

		Label label = new Label(propertiesGroup, SWT.NONE);
		label.setText(ProjectWizardsMessages.ManifestPage_pid); // "Plug-in &ID:"  
		fIdText = createText(propertiesGroup, propertiesListener);

		label = new Label(propertiesGroup, SWT.NONE);
		label.setText(ProjectWizardsMessages.ManifestPage_pversion); 
		fVersionText = createText(propertiesGroup, propertiesListener);

		label = new Label(propertiesGroup, SWT.NONE);
		label.setText(ProjectWizardsMessages.ManifestPage_pname); 
		fNameText = createText(propertiesGroup, propertiesListener);

		label = new Label(propertiesGroup, SWT.NONE);
		label.setText(ProjectWizardsMessages.ManifestPage_pprovider); 
		fProviderText = createText(propertiesGroup, propertiesListener);

		fLibraryLabel = new Label(propertiesGroup, SWT.NONE);
		fLibraryLabel.setText(ProjectWizardsMessages.ManifestPage_classpath); 
		fLibraryText = createText(propertiesGroup, propertiesListener);
	}

	public void updateData() {
		super.updateData();
		PluginFieldData data = (PluginFieldData)fData;
		data.setDoGenerateClass(false);
	}
	
	
    /* (non-Javadoc)
     * @see org.eclipse.pde.internal.ui.wizards.plugin.ContentPage#setVisible(boolean)
     */
    public void setVisible(boolean visible) {
    	if (visible) {
    		fMainPage.updateData();
    	}
        super.setVisible(visible);
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.pde.internal.ui.wizards.plugin.ContentPage#validatePage()
	 */
	protected void validatePage() {
		String errorMessage = validateProperties();
		setErrorMessage(errorMessage);
		setPageComplete(errorMessage == null);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.pde.internal.ui.wizards.plugin.ContentPage#getNameFieldQualifier()
	 */
	protected String getNameFieldQualifier() {
		return PDEUIMessages.ContentPage_plugin; 
	}
	
}
