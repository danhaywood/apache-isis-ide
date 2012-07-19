package com.halware.nakedide.eclipse.ext.perspectives;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

import com.halware.nakedide.eclipse.ext.annot.action.NakedObjectActionsView;
import com.halware.nakedide.eclipse.ext.annot.coll.NakedObjectCollectionsView;
import com.halware.nakedide.eclipse.ext.annot.objectspec.NakedObjectSpecView;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectPropertiesView;
import com.halware.nakedide.eclipse.ext.annot.strprop.NakedObjectStringPropertiesView;
import com.halware.nakedide.eclipse.ext.outline.NakedObjectOutlineView;

public class NakedObjectsPerspectiveFactory implements IPerspectiveFactory {

	public static final String NAKED_OBJECT_OUTLINE_VIEW = NakedObjectOutlineView.ID;
    public static final String NAKED_OBJECT_ACTIONS_VIEW = NakedObjectActionsView.ID;
    public static final String NAKED_OBJECT_COLLECTIONS_VIEW = NakedObjectCollectionsView.ID;
    public static final String NAKED_OBJECT_STRING_PROPERTIES_VIEW = NakedObjectStringPropertiesView.ID;
    public static final String NAKED_OBJECT_PROPERTIES_VIEW = NakedObjectPropertiesView.ID;
    public static final String NAKED_OBJECT_SPEC_VIEW = NakedObjectSpecView.ID;
    private static final String UNTITLED_TEXT_FILE_WIZARD = "org.eclipse.ui.editors.wizards.UntitledTextFileWizard"; //$NON-NLS-1$
	private static final String NEW_FILE_WIZARD = "org.eclipse.ui.wizards.new.file"; //$NON-NLS-1$
	private static final String NEW_FOLDER_WIZARD = "org.eclipse.ui.wizards.new.folder"; //$NON-NLS-1$
	private static final String NEW_INTERFACE_WIZARD = "org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"; //$NON-NLS-1$
	private static final String NEW_CLASS_WIZARD = "org.eclipse.jdt.ui.wizards.NewClassCreationWizard"; //$NON-NLS-1$
	private static final String NEW_PACKAGE_WIZARD = "org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"; //$NON-NLS-1$
	private static final String NAVIGATE_ACTION_SET = IPageLayout.ID_NAVIGATE_ACTION_SET;
	private static final String JAVA_ELEMENT_CREATION_ACTION_SET = JavaUI.ID_ELEMENT_CREATION_ACTION_SET;
	private static final String JAVA_ACTION_SET = JavaUI.ID_ACTION_SET;
	private static final String DEBUG_LAUNCH_ACTION_SET = IDebugUIConstants.LAUNCH_ACTION_SET;
	private static final String JAVADOC_VIEW = JavaUI.ID_JAVADOC_VIEW;
	private static final String PROBLEM_VIEW = IPageLayout.ID_PROBLEM_VIEW;
	private static final String NAVIGATOR_VIEW = IPageLayout.ID_RES_NAV;
	private static final String PACKAGE_EXPLORER_VIEW = JavaUI.ID_PACKAGES;
	private static final String SEARCH_VIEW = NewSearchUI.SEARCH_VIEW_ID;
	private static final String CONSOLE_VIEW = IConsoleConstants.ID_CONSOLE_VIEW;
    private static final String DEBUG_VIEW = IDebugUIConstants.ID_DEBUG_VIEW;
	private static final String TASKS_VIEW = IPageLayout.ID_TASK_LIST;//$NON-NLS-1$
	private static final String CALL_HIERARCHY_VIEW = "org.eclipse.jdt.callhierarchy.view";//$NON-NLS-1$
	private static final String JUNIT_VIEW = "org.eclipse.jdt.junit.ResultView";//$NON-NLS-1$
	
	public void createInitialLayout(IPageLayout layout) {
 		String editorArea = layout.getEditorArea();
		
		IFolderLayout packageExplorerFolder = layout.createFolder("packageExplorerFolder", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
		packageExplorerFolder.addView(PACKAGE_EXPLORER_VIEW);
		
		
		IFolderLayout outlineFolder = layout.createFolder("searchFolder", IPageLayout.BOTTOM, (float)0.50, "packageExplorerFolder"); //$NON-NLS-1$
		outlineFolder.addView(NAKED_OBJECT_OUTLINE_VIEW);
        try {
            outlineFolder.addView("org.eclipse.jdt.astview.views.ASTView");
            } catch(Exception ex) {
                // ignore any problems.
        }

        
		IFolderLayout problemFolder= layout.createFolder("problemFolder", IPageLayout.BOTTOM, (float)0.60, editorArea); //$NON-NLS-1$
		problemFolder.addView(PROBLEM_VIEW);


		IFolderLayout nakedObjectsFolder = layout.createFolder("annotationViewsFolder", IPageLayout.TOP, (float)0.50, "problemFolder"); //$NON-NLS-1$
		nakedObjectsFolder.addView(NAKED_OBJECT_SPEC_VIEW);
		nakedObjectsFolder.addView(NAKED_OBJECT_PROPERTIES_VIEW);
        nakedObjectsFolder.addView(NAKED_OBJECT_STRING_PROPERTIES_VIEW);
		nakedObjectsFolder.addView(NAKED_OBJECT_COLLECTIONS_VIEW);
		nakedObjectsFolder.addView(NAKED_OBJECT_ACTIONS_VIEW);


		IFolderLayout consoleFolder= layout.createFolder("consoleFolder", IPageLayout.RIGHT, (float)0.45, "problemFolder"); //$NON-NLS-1$
        consoleFolder.addView(CONSOLE_VIEW);
        
        IFolderLayout debugFolder = layout.createFolder("debugFolder", IPageLayout.RIGHT, (float)0.70, "consoleFolder"); //$NON-NLS-1$
        debugFolder.addView(DEBUG_VIEW); 


		IFolderLayout javadocFolder= layout.createFolder("javadocFolder", IPageLayout.RIGHT, (float)0.75, editorArea); //$NON-NLS-1$
		javadocFolder.addView(JAVADOC_VIEW);
		
		
		layout.addActionSet(DEBUG_LAUNCH_ACTION_SET);
		layout.addActionSet(JAVA_ACTION_SET);
		layout.addActionSet(JAVA_ELEMENT_CREATION_ACTION_SET);
		layout.addActionSet(NAVIGATE_ACTION_SET);

        
        // fast views
        layout.addFastView(IProgressConstants.PROGRESS_VIEW_ID);
        layout.addFastView(CALL_HIERARCHY_VIEW);
        layout.addFastView(JUNIT_VIEW);
        layout.addFastView(IPageLayout.ID_BOOKMARKS);
        layout.addFastView(TASKS_VIEW);
        layout.addFastView(NAVIGATOR_VIEW);
        layout.addFastView(SEARCH_VIEW);
        
		// view shortcuts - NO
		layout.addShowViewShortcut(NAKED_OBJECT_SPEC_VIEW);
		layout.addShowViewShortcut(NAKED_OBJECT_PROPERTIES_VIEW);
        layout.addShowViewShortcut(NAKED_OBJECT_STRING_PROPERTIES_VIEW);
		layout.addShowViewShortcut(NAKED_OBJECT_COLLECTIONS_VIEW);
		layout.addShowViewShortcut(NAKED_OBJECT_ACTIONS_VIEW);
        layout.addShowViewShortcut(NAKED_OBJECT_OUTLINE_VIEW);
        

		// view shortcuts - ASTView (if available)
		layout.addShowViewShortcut("org.eclipse.jdt.astview.views.ASTView");


		// view shortcuts - java
		layout.addShowViewShortcut(PACKAGE_EXPLORER_VIEW);
		layout.addShowViewShortcut(JAVADOC_VIEW);

		// view shortcuts - search
		layout.addShowViewShortcut(SEARCH_VIEW);
		
		// view shortcuts - debugging
		layout.addShowViewShortcut(CONSOLE_VIEW);

		// view shortcuts - standard workbench
		layout.addShowViewShortcut(PROBLEM_VIEW);
		layout.addShowViewShortcut(NAVIGATOR_VIEW);
		layout.addShowViewShortcut(TASKS_VIEW);
				
		// new actions - Java project creation wizard
		layout.addNewWizardShortcut(NEW_PACKAGE_WIZARD); 
		layout.addNewWizardShortcut(NEW_CLASS_WIZARD);
		layout.addNewWizardShortcut(NEW_INTERFACE_WIZARD);
		layout.addNewWizardShortcut(NEW_FOLDER_WIZARD);
		layout.addNewWizardShortcut(NEW_FILE_WIZARD);
		layout.addNewWizardShortcut(UNTITLED_TEXT_FILE_WIZARD);
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
