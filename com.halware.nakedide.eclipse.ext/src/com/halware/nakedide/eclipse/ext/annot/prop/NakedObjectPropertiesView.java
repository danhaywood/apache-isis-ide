package com.halware.nakedide.eclipse.ext.annot.prop;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.Activator;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeView;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;


public class NakedObjectPropertiesView
	extends AbstractNodeView {  

    public static final String ID = NakedObjectPropertiesView.class.getCanonicalName();

	private final static Logger LOGGER = Logger.getLogger(NakedObjectPropertiesView.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

	public NakedObjectPropertiesView() {
		super(XP_PROPERTIES_METADATA_DESCRIPTORS_PROVIDER);
	}
	
	private static final String XP_PROPERTIES_METADATA_DESCRIPTORS_PROVIDER = 
		Activator.getPluginId() + ".propertiesMetadataDescriptorsProviders";


	public void init(IViewSite site) throws PartInitException {
		getLOGGER().info("init: started");
		try {
			super.setSite(site);
		} finally {
			getLOGGER().info("init: completed");
		}
	}


	/**
	 * Create the GUI and actions, set up providers and initial input.
	 */
	public void createPartControl(Composite parent) {
		getLOGGER().info("createPartControl: started");
		try {
			super.createPartControl(parent);
		} finally {
			getLOGGER().info("createPartControl: completed");
		}
	}





	///////// ContentProvider, CellModifier, LabelProvider ////////////////////

	protected IContentProvider createContentProvider() {
		return new NakedObjectPropertiesContentProvider(this);
	}

	private ICellModifier cellModifier;
	public <T extends ICellModifier> T getCellModifier(MetadataDescriptorSet set) {
		if (cellModifier == null) {
			cellModifier = new NakedObjectPropertiesCellModifier(set);
		}
		return Generics.asT(cellModifier);
	}

	private IBaseLabelProvider labelProvider;
	public <T extends IBaseLabelProvider> T getLabelProvider(MetadataDescriptorSet set) {
		if (labelProvider == null) {
			labelProvider = new NakedObjectPropertiesLabelProvider(set); 
		}
		return Generics.asT(labelProvider);
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
