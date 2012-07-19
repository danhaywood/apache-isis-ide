package com.halware.eclipseutil.parts;

import org.eclipse.ui.IWorkbenchPart;

/**
 * Allows a part (usually a view) to be notified of its own state.
 * 
 */
public interface ISelfWatcherPart extends IWorkbenchPart {

	public abstract void nowOpened();

	public abstract void nowActivated();

	public abstract void nowDeactivated();

	public abstract void nowClosed();

}