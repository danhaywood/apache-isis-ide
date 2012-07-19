package com.halware.eclipseutil.jobs;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

/**
 * Super class for all GUI jobs caused (not necessarily directly fired) by the user.
 */
public abstract class AbstractUserJob extends UIJob {


	/**
	 * @param jobDisplay
	 * @param name
	 */
	public AbstractUserJob(final Display jobDisplay, final String name) {
		super(jobDisplay, name);
	}

	/**
	 * @param name
	 */
	public AbstractUserJob(final String name) {
		super(name);
	}

	


}
