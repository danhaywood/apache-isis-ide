package com.halware.nakedide.eclipse.ext.annot.tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

import com.halware.eclipseutil.util.EditorUtil;
import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.Activator;

/**
 * Wraps up the current document and its content.
 * 
 * @author dkhaywood
 */
public class EditorTracker implements ITextFileBufferOwner, IDocumentOwner, ITextEditorWatcher
 {

	private final static Logger LOGGER = Logger.getLogger(EditorTracker.class);
	public Logger getLOGGER() {
		return LOGGER;
	}

	private IWorkbenchPage page;
	public IWorkbenchPage getPage() {
		return page;
	}

	public EditorTracker(IWorkbenchPage page) {
		this.page = page;
	}


	///////////////////// Listeners ////////////////////

	private DocumentListener documentListener = new DocumentListener(this);
	private TextEditorWatcherPartListener textEditorWatcherPartListener = new TextEditorWatcherPartListener(this);
	private TextFileBufferListener textFileBufferListener = new TextFileBufferListener(this);

	public void installListeners() {
		page.addPartListener(textEditorWatcherPartListener);
		FileBuffers.getTextFileBufferManager().addFileBufferListener(textFileBufferListener);
		
		editorContentListeners = Collections.unmodifiableList(loadEditorContentListeners());
	}



	private List<IEditorContentListener> editorContentListeners;
	/**
	 * The contributed implementations of {@link IEditorContentListener} that will be
	 * notified whenever the editor content has changed (ie a new AST is available).
	 * 
	 * <p>
	 * This collection is only populated once {@link #installListeners()} has been
	 * populated.
	 * 
	 * @return
	 */
	public List<IEditorContentListener> getEditorContentListeners() {
		return editorContentListeners;
	}

	private static final String XP_EDITOR_CONTENT_LISTENERS = 
		Activator.getPluginId() + ".editorContentListeners";

	private List<IEditorContentListener> loadEditorContentListeners() {
		
		List<IEditorContentListener> providers = new ArrayList<IEditorContentListener>();
		
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(XP_EDITOR_CONTENT_LISTENERS);
		for (IExtension extension: p.getExtensions()) {
			for (IConfigurationElement ce: extension.getConfigurationElements()) {
				String attribute = ce.getAttribute("class");
				try {
					Class<IEditorContentListener> providerClass = Generics.asT(Class.forName(attribute));
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




	
	///////////////////// State ////////////////////
	
	
	public void setInput(EditorContent editorContent) {
		if (editorContent == null) {
			if (document != null) {
				document.removeDocumentListener(documentListener);
			}
			editor = null;
			openable = null;
			parsedCompilationUnit = null;
			document = null;
		} else {
			editor = editorContent.getEditor();
			openable = editorContent.getOpenable();
			parsedCompilationUnit = editorContent.getParsedCompilationUnit();
			document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			document.addDocumentListener(documentListener);
		}
	}
	
	private ITextEditor editor;
	public ITextEditor getEditor() {
		return editor;
	}
	
	private IOpenable openable;

	private IDocument document;

	/**
	 * The currently opened type, as a {@link ICompilationUnit}
	 * (note: <b>NOT</b> as a {@link CompilationUnit} !).
	 */
	public ICompilationUnit getCompilationUnit() {
		if (openable instanceof ICompilationUnit) {
			return (ICompilationUnit)openable;
		}
		return null;
	}

	private CompilationUnit parsedCompilationUnit;
	public CompilationUnit getParsedCompilationUnit() {
		return parsedCompilationUnit;
	}
	public void setParsedCompilationUnit(CompilationUnit parsedCompilationUnit) {
		this.parsedCompilationUnit = parsedCompilationUnit;
	}
	
	
	
	public void bufferDisposed(ITextFileBuffer buffer) {
		setInput(null);
	}


	public void documentChanged(DocumentEvent event) {
		IEditorPart part= EditorUtil.getActiveEditor();
		if (part instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) part;
			scheduleAstJob(textEditor, null);
		}
	}



	public void textEditorActivated(ITextEditor textEditor) {
		scheduleAstJob(textEditor, null);
	}


	public void textEditorClosed() {
		scheduleAstJob(null, null);
	}

    
    private WeakHashMap<Object, AstJob> scheduledJobs = new WeakHashMap<Object, AstJob>();
    /**
     * Represents any AstJob that is queued in {@link #scheduledJobs} that is not valid. 
     * 
     * <p>
     * There can only be one such null job queued.
     */
    private Object KEY_NULL_JOB = new Object();

	public void scheduleAstJob(ITextEditor editor, IStructuredSelection selectionHint) {
		EditorContent editorContent = new EditorContent(editor);
		AstJob astJob = new AstJob(editorContent, selectionHint);
        
        // hold job in weak map, and cancel any already there.
        Object jobKey = editorContent.isValid()?editorContent.getOpenable():KEY_NULL_JOB;
        AstJob previouslyScheduledJob = scheduledJobs.get(jobKey);
        if (previouslyScheduledJob != null) {
            getLOGGER().debug("Cancelling previous job " + previouslyScheduledJob + " (scheduled at " + previouslyScheduledJob.getScheduledAt() + ")");
            previouslyScheduledJob.cancelIfPossible(); // if running
        }
        scheduledJobs.put(jobKey, astJob);
        
		astJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void aboutToRun(IJobChangeEvent event) {
				astJobAboutToRun(asAstJob(event));
			}
			@Override
			public void done(IJobChangeEvent event) {
				astJobDone(asAstJob(event));
			}
			private AstJob asAstJob(IJobChangeEvent event) {
				return (AstJob)event.getJob();
			}
		});

		astJob.schedule();
	}

	public void astJobAboutToRun(final AstJob astJob) {
		
		setInput(null);
		final EditorContent editorContent = astJob.getEditorContent();
		
		Display defaultDisplay = Display.getDefault();
		defaultDisplay.syncExec(new Runnable() {
			public void run() {
				for(IEditorContentListener listener: editorContentListeners) {
					listener.editorContentAboutToChange(editorContent);
				}
			}
		});
	}


	public void astJobDone(final AstJob astJob) {

		final EditorContent editorContent = astJob.getEditorContent();
		if ( editorContent.isValid() && 
			!editorContent.isNull()) {
			setInput(editorContent);
			
		} else {
			setInput(null);
		}
		final IStructuredSelection selectionHint = astJob.getSelectionHint();

		Display defaultDisplay = Display.getDefault();
		defaultDisplay.asyncExec(new Runnable() {
			public void run() {
				for(IEditorContentListener listener: editorContentListeners) {
					listener.editorContentChanged(editorContent, selectionHint);
				}
			}
		});
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
