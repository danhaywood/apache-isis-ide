package com.halware.eclipseutil.jobs;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;

/**
 * Action that runs the <code>Job</code> passed on construction
 * @author Mike
 * @see org.eclipse.core.runtime.jobs.Job
 */
public class JobAction extends Action {

	private final Job _job;

	
	/**
	 * Constructor passed the job to use.  
	 * <br>Note that the job's name is used as the action text - overwrite after
	 * this if you want a specific name.
	 * @param job
	 */
	public JobAction( final Job job ) {
		if ( job == null ) {
			throw new IllegalArgumentException();
		}
		this._job = job;
		setText( job.getName() );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {
		_job.schedule();
	}
	
	

}
