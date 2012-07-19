package com.halware.nakedide.eclipse.ext.outline;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.Activator;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeView;
import com.halware.nakedide.eclipse.ext.annot.mdd.DelegatingMetadataDescriptorSetOwner;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorSetOwner;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectPropertiesLabelProvider;


public class NakedObjectOutlineView extends AbstractNodeView {
    
    private static final String XP_OUTLINE_METADATA_DESCRIPTORS_PROVIDER = 
        Activator.getPluginId() + ".outlineMetadataDescriptorsProviders";

    public NakedObjectOutlineView() {
        super(XP_OUTLINE_METADATA_DESCRIPTORS_PROVIDER);
    }


    public final static String ID = NakedObjectOutlineView.class.getCanonicalName();
    
    private static final Logger LOGGER = Logger.getLogger(NakedObjectOutlineView.class);

    @Override
    public Logger getLOGGER() {
        return LOGGER;
    }


    protected IMetadataDescriptorSetOwner getMetadataDescriptorSetOwner() {
        return new DelegatingMetadataDescriptorSetOwner(this) {
                private IBaseLabelProvider labelProvider;
                public <T extends IBaseLabelProvider> T getLabelProvider(MetadataDescriptorSet set) {
                    if (labelProvider == null) {
                        labelProvider = new OutlineViewLabelProvider(set); 
                    }
                    return Generics.asT(labelProvider);
                }
        };
    }
    


//	private void hookContextMenu() {
//		MenuManager menuMgr = new MenuManager("#PopupMenu");
//		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				NakedObjectOutlineView.this.fillContextMenu(manager);
//			}
//		});
//		Menu menu = menuMgr.createContextMenu(getTableViewer().getControl());
//		getTableViewer().getControl().setMenu(menu);
//		getSite().registerContextMenu(menuMgr, getTableViewer());
//	}

//    private Action action1;
//    private Action action2;
//    private Action doubleClickAction;
//
//
//	private void contributeToActionBars() {
//		IActionBars bars = getViewSite().getActionBars();
//		fillLocalPullDown(bars.getMenuManager());
//		fillLocalToolBar(bars.getToolBarManager());
//	}
//
//	@Override
//	public void fillLocalPullDown(IMenuManager manager) {
//        super.fillLocalPullDown(manager);
//		manager.add(action1);
//		manager.add(new Separator());
//		manager.add(action2);
//	}
//
//    @Override
//    protected void fillContextMenu(IMenuManager manager) {
//        super.fillContextMenu(manager);
//		manager.add(action1);
//		manager.add(action2);
//		// Other plug-ins can contribute there actions here
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//	}
//	
//    @Override
//    protected void fillLocalToolBar(IToolBarManager manager) {
//        super.fillLocalToolBar(manager);
//		manager.add(action1);
//		manager.add(action2);
//	}
//
//    @Override
//    protected void makeActions() {
//        super.makeActions();
//		action1 = new Action() {
//			public void run() {
//				showMessage("Action 1 executed");
//			}
//		};
//		action1.setText("Action 1");
//		action1.setToolTipText("Action 1 tooltip");
//		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
//			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
//		
//		action2 = new Action() {
//			public void run() {
//				showMessage("Action 2 executed");
//			}
//		};
//		action2.setText("Action 2");
//		action2.setToolTipText("Action 2 tooltip");
//		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
//				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
//		doubleClickAction = new Action() {
//			public void run() {
//				ISelection selection = getTableViewer().getSelection();
//				Object obj = ((IStructuredSelection)selection).getFirstElement();
//				showMessage("Double-click detected on "+obj.toString());
//			}
//		};
//	}
//
//	private void hookDoubleClickAction() {
//        getTableViewer().addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				doubleClickAction.run();
//			}
//		});
//	}
//	private void showMessage(String message) {
//		MessageDialog.openInformation(
//                getTableViewer().getControl().getShell(),
//			"OutlineView",
//			message);
//	}
//


    ///////// ContentProvider, CellModifier, LabelProvider ////////////////////


    protected IContentProvider createContentProvider() {
        return new OutlineViewContentProvider(this);
    }

    private ICellModifier cellModifier;
    public <T extends ICellModifier> T getCellModifier(MetadataDescriptorSet set) {
        if (cellModifier == null) {
            cellModifier = new CellModifier(set);
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
