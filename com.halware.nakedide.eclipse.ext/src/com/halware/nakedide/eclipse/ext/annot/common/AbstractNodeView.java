package com.halware.nakedide.eclipse.ext.annot.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.halware.eclipseutil.util.EditorUtil;
import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.Activator;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatedLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorSetOwner;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;
import com.halware.nakedide.eclipse.ext.annot.objectspec.NakedObjectSpec;
import com.halware.nakedide.eclipse.ext.annot.tracker.EditorContent;
import com.halware.nakedide.eclipse.ext.annot.tracker.EditorTracker;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

public abstract class AbstractNodeView 
	extends ViewPart  
	implements IMetadataDescriptorSetOwner, ICompilationUnitOwner {
	
	public abstract Logger getLOGGER();

	protected AbstractNodeView(String metadataDescriptorsProviderExtensionPoint) {
		this.metadataDescriptorsProviderExtensionPoint = metadataDescriptorsProviderExtensionPoint;
	}
	
	private TableViewer tableViewer;
	public TableViewer getTableViewer() {
		return tableViewer;
	}
	
	private Action doubleClickAction;


	///////// EditorTracker ////////////////////

	public EditorTracker getEditorTracker() {
		return Activator.getDefault().getEditorTracker(getPage());
	}
	
	public ICompilationUnit getCompilationUnit() {
		return getEditorTracker().getCompilationUnit();
	}

	
	private MetadataDescriptorSet metadataDescriptorSet;
	private final String metadataDescriptorsProviderExtensionPoint;
	/**
	 * Populated only after {@link #createPartControl(Composite)} has been called. 
	 */
	public MetadataDescriptorSet getPropertyDescriptorSet() {
		return metadataDescriptorSet;
	}


	
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
			internalCreatePartControl(parent);
		} finally {
			getLOGGER().info("createPartControl: completed");
		}
	}


	private void internalCreatePartControl(Composite parent) {

		// HACK: as late as possible to ensure there is an activePage
		Activator.getDefault().setView(this); 

		tableViewer = createViewer(parent);
		metadataDescriptorSet = configureViewer();
		
		tableViewer.setContentProvider(createContentProvider());
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		try {
			setInputToCurrentEditor();
		} catch (CoreException e) {
			getLOGGER().error("createPartControl: Unable to set input to current editor", e);
		}
	}

	public IWorkbenchPage getPage() {
		return getSite().getPage();
	}

	protected abstract IContentProvider createContentProvider();

	/**
	 * Used internally to set up table, but <tt>protected</tt> so can be re-used
	 * by subclasses if they need similar looking tables.
	 * 
	 * @param parent
	 * @return
	 */
	protected TableViewer createViewer(Composite parent) {
		layoutParent(parent);
		Table table = createTable(parent);
		TableViewer tableViewer = new TableViewer(table);
		
		return tableViewer;
		
	}

	private void layoutParent(Composite parent) {
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayoutData(gridData);
	}

	private Table createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
        SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		Table table = new Table(parent, style);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		return table;
	}

	private MetadataDescriptorSet configureViewer() {
		
		MetadataDescriptorSet metadataDescriptorSet = createPropertyDescriptors();
		
		tableViewer.setCellModifier(metadataDescriptorSet.getCellModifier());
		tableViewer.setCellEditors(metadataDescriptorSet.getCellEditors());
		tableViewer.setColumnProperties(metadataDescriptorSet.getColumnProperties());
		tableViewer.setLabelProvider(metadataDescriptorSet.getLabelProvider());
		
		return metadataDescriptorSet;
	}

	private MetadataDescriptorSet createPropertyDescriptors() {
		MetadataDescriptorSet metadataDescriptorSet = new MetadataDescriptorSet(getMetadataDescriptorSetOwner());
		
		for(IMetadataDescriptorsProvider provider: loadMetadataDescriptorProviders()) {
			metadataDescriptorSet.add(provider);	
		}
		
		return metadataDescriptorSet;
	}

    protected IMetadataDescriptorSetOwner getMetadataDescriptorSetOwner() {
        return this;
    }
	
	private List<IMetadataDescriptorsProvider> loadMetadataDescriptorProviders() {
		return loadMetadataDescriptorProviders(getMetadataDescriptorsExtensionPoint());
	}

	/**
	 * Used internally to set up descriptors, but <tt>protected</tt> so can be re-used
	 * by subclasses.
	 * 
	 * @param parent
	 * @return
	 */
	protected List<IMetadataDescriptorsProvider> loadMetadataDescriptorProviders(String metadataDescriptorsExtensionPoint) {
		List<IMetadataDescriptorsProvider> providers = new ArrayList<IMetadataDescriptorsProvider>();
		
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(metadataDescriptorsExtensionPoint);
		for (IExtension extension: p.getExtensions()) {
			for (IConfigurationElement ce: extension.getConfigurationElements()) {
				String attribute = ce.getAttribute("class");
				try {
					Class<IMetadataDescriptorsProvider> providerClass = Generics.asT(Class.forName(attribute));
					providers.add(providerClass.newInstance());
				} catch (InstantiationException e) {
					getLOGGER().error("Failed to instantiate " + attribute + "; ignoring", e);
				} catch (IllegalAccessException e) {
					getLOGGER().error("Failed to instantiate " + attribute + "; ignoring", e);
				} catch (ClassNotFoundException e) {
					getLOGGER().error("Failed to instantiate " + attribute + "; ignoring", e);
				}
			}
		}
		return providers;
	}


	protected String getMetadataDescriptorsExtensionPoint() {
		return metadataDescriptorsProviderExtensionPoint;
	}


	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}


	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	protected void fillLocalPullDown(IMenuManager manager) {
	}

	protected void fillContextMenu(IMenuManager manager) {
	}
	
	protected void fillLocalToolBar(IToolBarManager manager) {
	}

	protected void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = tableViewer.getSelection();
				IStructuredSelection structuredSelection = (IStructuredSelection)selection;
				selectInEditor(structuredSelection);
			}
		};
	}
	private void selectInEditor(IStructuredSelection selectedElement) {
		if (selectedElement == null) {
			return;
		}
		Object firstElement = selectedElement.getFirstElement();
		if (firstElement instanceof AbstractMember) {
			selectInEditor((AbstractMember)firstElement);
		} else if (firstElement instanceof NakedObjectSpec) {
			selectInEditor((NakedObjectSpec)firstElement);
		} else if (firstElement instanceof BodyDeclaration) {
			selectInEditor((BodyDeclaration)firstElement);
		} 
	}
	private void selectInEditor(AbstractMember selectedElement) {
		selectInEditor(selectedElement.getDeclaration());
	}
	private void selectInEditor(NakedObjectSpec selectedElement) {
		selectInEditor(selectedElement.getDeclaration());
	}

	/**
	 * For use by sublasses.
	 * 
	 * @param methodDeclaration
	 */
	protected void selectInEditor(BodyDeclaration declaration) {
		AstUtils.OffsetAndLength offsetAndLength = AstUtils.calculateOffset(declaration);
		EditorUtil.selectInEditor(getEditorTracker().getEditor(), offsetAndLength.offset, offsetAndLength.length);
	}
	
	/**
	 * Locate the method based on its declaration in a previous AST.
	 *  
	 * @param searchMethodDeclaration
	 */
	private void reselectInEditor(BodyDeclaration searchDeclaration) {
		if (searchDeclaration == null) {
			return;
		}
		
		CompilationUnit parsedCompilationUnit = getEditorTracker().getParsedCompilationUnit();
		if (parsedCompilationUnit == null) {
			return;
		}
		
		BodyDeclaration newDeclaration = 
			AstUtils.findDeclaration(
				parsedCompilationUnit, searchDeclaration );
		if (newDeclaration == null) {
			return;
		}
		
		selectInEditor(newDeclaration);
	}

	/**
	 * Locate the method based the supplied selection hint.
	 * 
	 * <p>
	 * If the supplied selection hint is a {@link MethodDeclaration}, then
	 * delegates to {@link #reselectInEditor(MethodDeclaration)}.  Otherwise
	 * does nothing.
	 */
	private void reselectInEditor(IStructuredSelection selectionHint) {
		if (selectionHint == null) {
			return;
		}
		AbstractNode<BodyDeclaration> firstElement = Generics.asT(selectionHint.getFirstElement());
		reselectInEditor(firstElement.getDeclaration());
	}

	

	private void hookDoubleClickAction() {
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the tableViewer's control.
	 */
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}
	
	
	
	////////////// setInput ////////////////////////////////
	
	public void setInputToCurrentEditor() throws CoreException {
		IEditorPart part= EditorUtil.getActiveEditor();
		if (part instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) part;
			getLOGGER().debug("setInputToCurrentEditor; textEditor=" + textEditor);
			getEditorTracker().scheduleAstJob(textEditor, null);
		}
	}


	public void resetView(CompilationUnit root) {
		resetView(root, null);
	}

	public void resetView(CompilationUnit root, IStructuredSelection selectionHint) {
		if (tableViewer == null) {
			return;
		}
		
		tableViewer.setInput(root);

		if (selectionHint == null) {
			return;
		}
		
		AbstractNode<ASTNode> selectedNode = 
			Generics.asT(selectionHint.getFirstElement());
		ASTNode searchDeclaration = selectedNode.getDeclaration();
		
		CompilationUnit parsedCompilationUnit = getEditorTracker().getParsedCompilationUnit();
		if (parsedCompilationUnit == null) {
			return;
		}

		ASTNode newDeclaration = 
			AstUtils.findDeclaration(
				parsedCompilationUnit, searchDeclaration );
		if (newDeclaration == null) {
			return;
		}
		
		AbstractNode<ASTNode> node = find(parsedCompilationUnit, newDeclaration);
		if (node != null) {
			tableViewer.setSelection(new StructuredSelection(new Object[]{node}), true);
		}
	}
	
	protected AbstractNode<ASTNode> find(CompilationUnit parsedCompilationUnit, ASTNode searchDeclaration) {
		IStructuredContentProvider contentProvider = 
			(IStructuredContentProvider) tableViewer.getContentProvider();
		Object[] elements = contentProvider.getElements(parsedCompilationUnit);
		for(Object el: elements) {
			AbstractNode<ASTNode> node = Generics.asT(el);
			if (node.getDeclaration() == searchDeclaration) {
				return node;
			}
		}
		return null;
	}


	////////////// IMetadataDescriptorSetOwner /////////////////

	public void refresh() {
		refresh((IStructuredSelection)null);
	}

	public void refresh(IStructuredSelection selectionHint) {
		IEditorPart part= EditorUtil.getActiveEditor();
		if (part instanceof ITextEditor) {
			getEditorTracker().scheduleAstJob(((ITextEditor) part), selectionHint);
		}
	}


	///////// IEditorContentListener ////////////////////

	public void editorContentAboutToChange(EditorContent editorContent) {
		resetView(null);
	}


	public void editorContentChanged(EditorContent editorContent, IStructuredSelection selectionHint) {
		if ( editorContent.isValid() && 
			!editorContent.isNull()) {

			CompilationUnit parsedCompilationUnit = getEditorTracker().getParsedCompilationUnit();
			setContentDescription(
					parsedCompilationUnit == null?
					"AST could not be created.":""); //$NON-NLS-1$
			resetView(parsedCompilationUnit, selectionHint);

			if (selectionHint!=null) {
				reselectInEditor(selectionHint);
			}
		} else {
			resetView(null);
		}
	}

	



	///////// CellModifier, LabelProviders ////////////////////

	public abstract <Q extends ICellModifier> Q getCellModifier(MetadataDescriptorSet set);

	public abstract <Q extends IBaseLabelProvider> Q getLabelProvider(MetadataDescriptorSet set);

	/**
	 * Used whenever there isn't an explicitly provided {@link ILabelProvider}.
	 */
	public IEvaluatedLabelProvider getDefaultLabelProvider() {
		return new DefaultEvaluatedLabelProvider();
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
