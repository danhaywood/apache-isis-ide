package com.halware.nakedide.eclipse.ext;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.IWorkbenchPage;
import org.osgi.framework.BundleContext;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeView;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectPropertiesView;
import com.halware.nakedide.eclipse.ext.annot.tracker.EditorTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();
	public static String getPluginId() {
	    return PLUGIN_ID; 
	}

	// The shared instance
	private static Activator plugin;
	
    

	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

    
    
    //////////////////// EditorTracker //////////////////////


    private Map<IWorkbenchPage, EditorTracker> editorTrackerByPage = 
        new HashMap<IWorkbenchPage, EditorTracker>();
    public EditorTracker getEditorTracker(IWorkbenchPage page) {
        EditorTracker editorTracker = editorTrackerByPage.get(page);
        if (editorTracker == null) {
            editorTracker = new EditorTracker(page);
            editorTrackerByPage.put(page, editorTracker);
        }
        return editorTracker;
    }


    //////////////////// Access to views //////////////////////

    class ViewMap {
        private Map<Class<? extends AbstractNodeView>, AbstractNodeView> viewByClass = new HashMap<Class<? extends AbstractNodeView>, AbstractNodeView>();
        <T extends AbstractNodeView> T getView(Class<T> clazz) {
            return Generics.asT(viewByClass.get(clazz));
        }
        void putView(AbstractNodeView view) {
            Class<? extends AbstractNodeView> viewClass = view.getClass();
            putView(view, viewClass);
        }
        void putView(AbstractNodeView view, Class<? extends AbstractNodeView> clazz) {
            viewByClass.put(clazz, view);
        }
    }
    
    private Map<IWorkbenchPage, ViewMap> viewMapByPage = new HashMap<IWorkbenchPage, ViewMap>();

    /**
     * Called by {@link NakedObjectPropertiesView#init(org.eclipse.ui.IViewSite)}.
     * 
     * @return
     */
    public <T extends AbstractNodeView> T getView(IWorkbenchPage page, Class<T> viewClazz) {
        if (page == null) {
            return null;
        }
        ViewMap viewMap = viewMapByPage.get(page);
        if (viewMap == null) {
            return null;
        }
        return viewMap.getView(viewClazz);
    }
    /**
     * Registers this view, and moreover installs the {@link #getEditorTracker()}'s
     * listeners if not yet done.
     * 
     * <p>
     * HACK: we do this lazily because attempting to do so too early might result in 
     * there being no active page on which to install the listeners.
     * 
     * @param view
     */
    public void setView(AbstractNodeView view) {
        IWorkbenchPage page = view.getPage();
        ViewMap viewMap = viewMapByPage.get(page);
        if (viewMap == null) {
            viewMap = new ViewMap();
            viewMapByPage.put(page, viewMap);
            getEditorTracker(page).installListeners();
        }
        viewMap.putView(view);
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
