package com.halware.nakedide.eclipse.core.launches;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractArgumentBlock {

	public AbstractArgumentBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
		this.nakedObjectsArgumentsTab = nakedObjectsArgumentsTab;
	}

	private final NakedObjectsArgumentsTab nakedObjectsArgumentsTab;
	protected NakedObjectsArgumentsTab getTab() {
		return nakedObjectsArgumentsTab;
	}

    protected Group createGroup(
            Composite owner,
            String label) {
        return createGroup(owner, owner.getFont(), label);
    }

    protected Group createGroup(
            Composite owner,
            Font font,
            String label) {
        Group group = new Group(owner, SWT.NONE);
        group.setFont(font);
        if (label != null) {
            group.setText(label);
        }
        GridLayout layout = new GridLayout();
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        return group;
    }

    protected Button createButton(
            Group owner,
            String label,
            String data) {
        Button button = nakedObjectsArgumentsTab.createRadioButton(owner, label);
        button.setData(data);
        button.addSelectionListener(new MakeDirtySelectionListener(getTab()));
        return button;
    }

    protected boolean isSelected(
            Button button) {
        return button.getSelection();
    }


    protected void disable(
            List<? extends Control> controls) {
        for(Control control: controls) {
            disable(control);
        }
    }

    protected void enable(
            List<? extends Control> controls) {
        for(Control control: controls) {
            enable(control);
        }
    }

    protected void enable(
            Control control) {
        control.setEnabled(true);        
    }

    protected void disable(
            Control widget) {
        widget.setEnabled(false);        
    }

    protected void clear(
            Text text) {
        text.setText("");
    }

    protected void select(Button button) {
        button.setSelection(true);
    }

    protected void deselect(Button button) {
        button.setSelection(false);
    }

    protected void deselect(
            List<Button> buttons) {
        for(Button button: buttons) {
            deselect(button);
        }
    }
    
    
    /**
     * Returns <tt>true</tt> if any of the buttons in
     * the supplied list are selected, <tt>false</tt>
     * otherwise.
     * 
     * @param buttons
     * @return
     */
    protected boolean anySelected(
            List<Button> buttons) {
        for(Button button: buttons) {
            if (isSelected(button)) {
                return true;
            }
        }
        return false;
    }
    


}
