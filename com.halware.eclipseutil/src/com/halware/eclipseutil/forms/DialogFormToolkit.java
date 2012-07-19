package com.halware.eclipseutil.forms;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * A toolkit suitable for creating dialog controls.
 * 
 * @author ted stockwell
 */
public class DialogFormToolkit extends FormToolkit {

	public DialogFormToolkit(final Display pDisplay) {
		super(pDisplay);
	}

	public DialogFormToolkit(final FormColors pColors) {
		super(pColors);
	}

    
	@Override
	public void adapt(final Control control, final boolean trackFocus, final boolean trackKeyboard) {
		Color b = control.getBackground();
		Color f = control.getForeground();
		try {
			super.adapt(control, trackFocus, trackKeyboard);
		}
		finally {
			control.setBackground(b);
			control.setForeground(f);
		}
	}
	
	@Override
	public void adapt(final Composite composite) {
		Color b = composite.getBackground();
		Color f = composite.getForeground();
		try {
			super.adapt(composite);
		}
		finally {
			composite.setBackground(b);
			composite.setForeground(f);
		}
	}
}
