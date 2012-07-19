package com.halware.nakedide.eclipse.core.templates;


import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.halware.nakedide.eclipse.core.Activator;

public class GenericTemplateRegistry  {
	
    private static final String KEY = "org.nakedobjects.ide.eclipse.core";

	
	/** The template store. */
	private TemplateStore fStore;
	/** The context type registry. */
	private ContributionContextTypeRegistry fRegistry;

    private GenericTemplateRegistry(String key) {
        this.key = key;
	}

    private String key;
    public String getKey() {
        return key;
    }
    
    private static GenericTemplateRegistry templateRegistry;
    public static GenericTemplateRegistry getDefault() {
        if (templateRegistry == null) {
            templateRegistry = new GenericTemplateRegistry(KEY);
        }
        return templateRegistry;
    }


    
	/**
	 * Returns this plug-in's template store.
	 * 
	 * @return the template store of this plug-in instance
	 */
	public TemplateStore getTemplateStore() {
		if (fStore == null) {
			fStore= new ContributionTemplateStore(
                getContextTypeRegistry(), 
                Activator.getDefault().getPreferenceStore(), getKey());
			try {
				fStore.load();
			} catch (IOException e) {
				Activator.getDefault().getLog().log(
                    new Status(IStatus.ERROR, 
                        "com.halware.nakedide.eclipse.core.templates", IStatus.OK, "", e)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return fStore;
	}

	/**
	 * Returns this plug-in's context type registry.
	 * 
	 * @return the context type registry for this plug-in instance
	 */
	public ContributionContextTypeRegistry getContextTypeRegistry() {
		if (fRegistry == null) {
			// create an configure the contexts available in the template editor
			fRegistry= new ContributionContextTypeRegistry();
		}
		return fRegistry;
	}

	/* Forward plug-in methods to javaeditor example plugin default instance */
	public ImageRegistry getImageRegistry() {
		return Activator.getDefault().getImageRegistry();
	}

	public static ImageDescriptor imageDescriptorFromPlugin(String string, String default_image) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(string, default_image);
	}

	public IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public void savePluginPreferences() {
        Activator.getDefault().savePluginPreferences();
	}

}

/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
