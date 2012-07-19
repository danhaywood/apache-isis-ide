package com.halware.eclipseutil.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Static library functions
 */
public final class ShellUtil {
	
	private ShellUtil() {} 


    /**
     * Centers a shell on its Display
     */
    static public void center(final Shell shell) {
        Display display = shell.getDisplay();
        Rectangle bounds = display.getBounds ();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 3;
        shell.setLocation(x, y);
    }


}
