package com.halware.eclipseutil.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;



public class ColorUtil {

    public final static Color getColor(
            int swt_color) {
        return Display.getCurrent().getSystemColor(swt_color);
    }

}
