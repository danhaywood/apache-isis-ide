package com.halware.eclipseutil.parts;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * 
 */
public class SelfWatcherPartListener implements IPartListener2 {

	private final static Logger LOGGER = Logger.getLogger(SelfWatcherPartListener.class);
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	private final ISelfWatcherPart selfWatcherPart;
	public SelfWatcherPartListener(ISelfWatcherPart selfWatcherPart) {
		this.selfWatcherPart = selfWatcherPart;
	}
	
	private boolean ownerVisible= true;
	public boolean isOwnerVisible() {
		return ownerVisible;
	}

	public void partVisible(IWorkbenchPartReference partRef) {
		ownerVisible = true;
	}
	public void partHidden(IWorkbenchPartReference partRef) {
		ownerVisible = false;
	}
	public void partOpened(IWorkbenchPartReference partRef) {
		getLOGGER().debug("Opened: " + partRef.getContentDescription());
		if (isThisPart(partRef)) {
			selfWatcherPart.nowOpened();
		}
	}
	public void partActivated(IWorkbenchPartReference partRef) {
		getLOGGER().debug("Activated: " + partRef.getContentDescription());
		if (isThisPart(partRef)) {
			selfWatcherPart.nowActivated();
		}
	}
	public void partDeactivated(IWorkbenchPartReference partRef) {
		getLOGGER().debug("Dectivated: " + partRef.getContentDescription());
		if (isThisPart(partRef)) {
			selfWatcherPart.nowDeactivated();
		} 
	}
	public void partClosed(IWorkbenchPartReference partRef) {
		getLOGGER().debug("Closed: " + partRef.getContentDescription());
		if (isThisPart(partRef)) {
			selfWatcherPart.nowClosed();
		}
	}
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	

	///////////////// Helpers ///////////////////
	
	private boolean isThisPart(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		return part == selfWatcherPart;
	}



}