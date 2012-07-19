package com.halware.eclipseutil.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;

import com.halware.eclipseutil.Activator;


/**
 * Puts up a 'not implemented' dialog.
 * @author Mike
 *
 */
public class NotImplementedJob extends AbstractUserJob {
	
	private final String _title;

	/**
	 * No-arg constructor - title set to <code>null</code>.
	 */
	public NotImplementedJob() {
		this( null );
	}
	
	/**
	 * Constructor passed title to display - can be <code>null</code>.
	 * @param title
	 */
	public NotImplementedJob( final String title ) {
		super( NotImplementedJob.class.getSimpleName() );
		this._title = title;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(final IProgressMonitor monitor) {
		MessageDialog.openWarning( 
				null, 
				_title,
				Activator.getResourceString( "NotImplementedJob.Msg") ); //$NON-NLS-1$
		return Status.OK_STATUS;
	}
}
