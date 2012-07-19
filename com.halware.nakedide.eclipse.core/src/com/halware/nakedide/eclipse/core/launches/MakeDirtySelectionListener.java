/**
 * 
 */
package com.halware.nakedide.eclipse.core.launches;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class MakeDirtySelectionListener extends SelectionAdapter {
	
	private NakedObjectsArgumentsTab tab;
	public NakedObjectsArgumentsTab getTab() {
		return tab;
	}
	
	MakeDirtySelectionListener(NakedObjectsArgumentsTab tab) {
		this.tab = tab;
	}
	
    public void widgetDefaultSelected(
            SelectionEvent e) {
    }
    public void widgetSelected(
            SelectionEvent e) {
        getTab().setDirty(true);
        getTab().updateLaunchConfigurationDialog(); 
    }
}